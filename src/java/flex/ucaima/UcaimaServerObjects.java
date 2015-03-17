/*
 * ucaima
 * 
 * Copyright (C) 2010
 * Ing. Felix D. Lopez M. - flex.developments en gmail
 * 
 * Desarrollo apoyado por la Superintendencia de Servicios de Certificación 
 * Electrónica (SUSCERTE) durante 2010-2014 por:
 * Ing. Felix D. Lopez M. - flex.developments en gmail | flopez en suscerte gob ve
 * 
 * Este programa es software libre; Usted puede usarlo bajo los terminos de la
 * licencia de software GPL version 2.0 de la Free Software Foundation.
 * 
 * Este programa se distribuye con la esperanza de que sea util, pero SIN
 * NINGUNA GARANTIA; tampoco las implicitas garantias de MERCANTILIDAD o
 * ADECUACION A UN PROPOSITO PARTICULAR.
 * Consulte la licencia GPL para mas detalles. Usted debe recibir una copia
 * de la GPL junto con este programa; si no, escriba a la Free Software
 * Foundation Inc. 51 Franklin Street,5 Piso, Boston, MA 02110-1301, USA.
 */

package flex.ucaima;

import flex.eSign.helpers.CertificateHelper;
import flex.eSign.helpers.exceptions.CertificateHelperException;
import flex.eSign.operators.CertificateVerifierOperator;
import flex.eSign.operators.components.CertificateVerifierOperatorResults;
import flex.eSign.operators.components.CertificateVerifierConfig;
import flex.pkikeys.PKIKeys;
import flex.pkikeys.Repositories.PKCS12.PKCS12Configuration;
import flex.pkikeys.Repositories.RepositoriesWhiteList;
import flex.pkikeys.exceptions.PKIKeysException;
import flex.ucaima.exceptions.UcaimaException;
import flex.ucaima.i18n.I18n;
import flex.ucaima.resources.UcaimaPropertiesBundle;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UcaimaServerObjects
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @version 1.0
 */
public class UcaimaServerObjects {
    //Atributos estaticos
    final private static String UCAIMA_REPOSITORIES_WHITE_LIST_LOCATION = "resources/UcaimaRepositoryWhiteList";
    final private static CertificateVerifierConfig vcConfig = new CertificateVerifierConfig();
    
    //Atributos que se cargan desde el properties
    private static String SERVER_MESSAGE_ERROR = UcaimaException.SERVIDOR_RETURN_ERROR;
    private static String SERVER_KEYSTORE_PATH;
    private static String SERVER_KEYSTORE_PASSWORD;
    private static PKIKeys serverKeys = null;
    
    private static boolean ableToSign = false;
    private static boolean propertiesLoaded = false;
    
    public UcaimaServerObjects() throws UcaimaException {
        init();
    }
    
    private static void init() throws UcaimaException {
        try {
            //Cargar properties principal
            SERVER_MESSAGE_ERROR = UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_GENERAL_MESSAGE_ERROR);
            SERVER_KEYSTORE_PATH = UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_KEYSTORE_PATH);
            SERVER_KEYSTORE_PASSWORD = UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_KEYSTORE_PASS);
            PKCS12Configuration configuracionKeystore = new PKCS12Configuration(SERVER_KEYSTORE_PATH, SERVER_KEYSTORE_PASSWORD);
            serverKeys = new PKIKeys(configuracionKeystore);
            serverKeys.setVerifyRepositoryIntegrity(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_DRIVER_VERIFY_INTEGRITY).trim()));
            
            //Cargar lista blanca de repositorios
            if(serverKeys.isVerifyRepositoryIntegrity()) {
                RepositoriesWhiteList repositoriesWhiteList = null;
                try (InputStream whiteListReader = Ucaima.class.getResourceAsStream(UCAIMA_REPOSITORIES_WHITE_LIST_LOCATION)) {
                    repositoriesWhiteList = new RepositoriesWhiteList(whiteListReader);
                }
                serverKeys.setWitheListRepositories(repositoriesWhiteList);
            }
            
            serverKeys.loadKeys(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_KEYSTORE_ALIAS));

            //Cargar propiedades para verificacion de certificado de servidor
            vcConfig.setCertificate(serverKeys.getSignCertificate());
            vcConfig.setVerifyDateWithHost(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_VERIFY_CERTIFICATE_WITH_HOST).trim()));

            List<String> ntpServers = new ArrayList<>();
            String[] ntps = UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_NTP_SERVERS).trim().split(",");
            ntpServers.addAll(Arrays.asList(ntps));
            vcConfig.setNtpServers(ntpServers);

            vcConfig.setVerifyWithOCSP(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_VERIFY_CERTIFICATE_WITH_OCSP).trim()));
            vcConfig.setInvalidOnNTPFail(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_INVALID_ON_NTP_FAIL).trim()));
            vcConfig.setInvalidOnOCSPFail(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_INVALID_ON_OCSP_FAIL).trim()));
            vcConfig.setInvalidOnCRLFail(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_INVALID_ON_LCR_FAIL).trim()));
            vcConfig.setInvalidOnCRLandOCSPFail(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_INVALID_ON_BOTH_FAIL).trim()));
            vcConfig.setTryDownloadCRL(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_VERIFY_CERTIFICATE_TRY_DOWNLOAD_LCR).trim()));
            vcConfig.setVerbose(Boolean.parseBoolean(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_VERIFY_VERBOSE).trim()));
            
            try (InputStream authoritiesReader = new FileInputStream(UcaimaPropertiesBundle.get(UcaimaPropertiesBundle.UCAIMA_CERTIFICATE_PATH))) {
                vcConfig.setAuthorities(CertificateHelper.loadPEMCertificate(authoritiesReader));
            }
            
            propertiesLoaded = true;
            UcaimaLogger.writeInfoLog(I18n.get(I18n.L_UCAIMA_INITIALIZED));
            
            verifyCertificateServer();
            
        } catch (IOException | PKIKeysException | CertificateHelperException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    private static void verifyCertificateServer() {
        vcConfig.setCertificate(serverKeys.getSignCertificate());
        CertificateVerifierOperatorResults verResult = CertificateVerifierOperator.verifyToNewSignature(vcConfig);
        ableToSign = verResult.isAutorized();
        
        if(ableToSign) {
            UcaimaLogger.writeInfoLog(I18n.get(I18n.L_UCAIMA_LOADED_CERITIFICATE));
            
        } else {
            UcaimaLogger.writeWarningLog(UcaimaException.SERVER_UNABLE_TO_SIGN + verResult.getDetails());
        }
    }

    public static String getServerMessageError() {
        try {
            if(!propertiesLoaded) init();
            return SERVER_MESSAGE_ERROR;
        } catch (UcaimaException ex) {
            return "Critical server error! An error has occurred in Ucaima. In addition, UCAIMA_SERVER_MESSAGE_ERROR it is not defined! Please check de UcaimaConfig.properties";
        }
    }

    public static PKIKeys getServerKeys() throws UcaimaException {
        if(!propertiesLoaded) init();
        return serverKeys;
    }

    public static CertificateVerifierConfig getVcConfig() throws UcaimaException {
        if(!propertiesLoaded) init();
        return vcConfig;
    }

    public static boolean isAbleToSign() throws UcaimaException {
        if(!propertiesLoaded) init();
        return ableToSign;
    }
}
