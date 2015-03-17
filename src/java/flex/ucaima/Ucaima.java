/*
 * ucaima
 * 
 * Copyright (C) 2010
 * Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * 
 * Desarrollo apoyado por la Superintendencia de Servicios de Certificación 
 * Electrónica (SUSCERTE) durante 2010-2014 por:
 * Ing. Felix D. Lopez M. - flex.developments@gmail.com | flopez@suscerte.gob.ve
 * Ing. Yessica De Ascencao - yessicadeascencao@gmail.com | ydeascencao@suscerte.gob.ve
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
import flex.eSign.operators.signers.EncodedSignOperator;
import flex.eSign.operators.signers.PDFOperator;
import flex.eSign.operators.components.CertificateVerifierOperatorResults;
import flex.eSign.operators.components.CertificateVerifierConfig;
import flex.eSign.operators.exceptions.PDFOperadorException;
import flex.eSign.operators.exceptions.CertificateVerifierOperatorException;
import flex.eSign.operators.exceptions.EncodedSignOperatorException;
import flex.kawi.Kawi;
import flex.kawi.exception.KawiException;
import flex.ucaima.exceptions.UcaimaException;
import flex.helpers.FileHelper;
import flex.helpers.DateHelper;
import flex.helpers.SMimeCoderHelper;
import flex.helpers.exceptions.DateHelperException;
import flex.helpers.exceptions.SMimeCoderHelperException;
import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Ucaima
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * @author Ing. Yessica De Ascencao - yessicadeascencao@gmail.com
 * @version 1.0
 */
public class Ucaima {
    final private static String UCAIMA_POSSITIVE_ANSWER = "TRUE";
    
    /**
     * Verificar certificado para una firma pre-existente.
     * 
     * @param signDate
     * @param certificate
     * @throws UcaimaException 
     */
    private void verifyCertificateToOldSign(
        X509Certificate certificate,
        Date signDate
    ) throws UcaimaException {
        CertificateVerifierConfig vcConfigToOldSignature = 
            CertificateVerifierConfig.getInstanceToOldSign(
                certificate,
                UcaimaServerObjects.getVcConfig().getAuthorities(),
                UcaimaServerObjects.getVcConfig().getCRLs(),
                signDate, 
                UcaimaServerObjects.getVcConfig().isVerbose()
        );
        vcConfigToOldSignature.setVerifyWithOCSP(UcaimaServerObjects.getVcConfig().isVerifyWithOCSP());
        vcConfigToOldSignature.setInvalidOnOCSPFail(UcaimaServerObjects.getVcConfig().isInvalidOnOCSPFail());
        vcConfigToOldSignature.setInvalidOnCRLFail(UcaimaServerObjects.getVcConfig().isInvalidOnCRLFail());
        vcConfigToOldSignature.setInvalidOnCRLandOCSPFail(UcaimaServerObjects.getVcConfig().isInvalidOnCRLandOCSPFail());
        
        //OJO... Desarrollar método inteligente para descarga LCRs de los certificados firmantes
        vcConfigToOldSignature.setTryDownloadCRL(false);
        
        CertificateVerifierOperatorResults verification;
        try {
            verification = CertificateVerifierOperator.verifyToOldSignature(vcConfigToOldSignature);
            if(!verification.isAutorized()) throw new UcaimaException(verification.getDetails(), verification.getCause());
            
        } catch (CertificateVerifierOperatorException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param signStandard
     * @param string
     * @param signAlg
     * @return
     * @throws UcaimaException 
     */
    public String generateEncodedSignOfString(
        String signStandard,
        String string,
        String signAlg
    ) throws UcaimaException {
        if(!UcaimaServerObjects.isAbleToSign())
            throw new UcaimaException(UcaimaException.SERVER_UNABLE_TO_SIGN);

        //OJO... De momento se confía en la hora del server
        try {
            return EncodedSignOperator.generateSMimeEncodedSignOfString(
                signStandard, 
                string.getBytes(), 
                new Date(),
                UcaimaServerObjects.getServerKeys().getPrivateKey(), 
                UcaimaServerObjects.getServerKeys().getSignCertificate(), 
                signAlg,
                UcaimaServerObjects.getServerKeys().getRepositoryCryptographyProvider()
            );
            
        } catch (EncodedSignOperatorException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    public String verifyEncodedSignOfString(
        String string,
        String signDate,
        String encodedSign,
        String encodedCertificate,
        String signAlg
    ) throws UcaimaException {
        try {
            X509Certificate certificate;
            if(encodedCertificate == null) certificate = UcaimaServerObjects.getServerKeys().getSignCertificate();
            else certificate = CertificateHelper.decode(encodedCertificate);
            
            Date date = null;
            try {
                date = DateHelper.stringToDate(signDate);
            } catch (DateHelperException ex) {
                date = null;
            }
            
            verifyCertificateToOldSign(certificate, date);

            return Boolean.toString(
                EncodedSignOperator.verifySMimeEncodedSignOfString(
                    string.getBytes(),
                    date,
                    encodedSign,
                    certificate,
                    signAlg,
                    null
                )
            );
            
        } catch (EncodedSignOperatorException | CertificateHelperException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public String generateSignFromEncodedBytes(
        String signStandard,
        String smime,
        String signAlg
    ) throws UcaimaException {
        if(!UcaimaServerObjects.isAbleToSign())
            throw new UcaimaException(UcaimaException.SERVER_UNABLE_TO_SIGN);
        
        //OJO... De momento se confía en la hora del server
        try {
            byte[] data = SMimeCoderHelper.getSMimeDecoded(smime);
            
            return EncodedSignOperator.generateSMimeEncodedSignOfString(
                signStandard, 
                data, 
                new Date(),
                UcaimaServerObjects.getServerKeys().getPrivateKey(), 
                UcaimaServerObjects.getServerKeys().getSignCertificate(), 
                signAlg,
                UcaimaServerObjects.getServerKeys().getRepositoryCryptographyProvider()
            );
            
        } catch (SMimeCoderHelperException | EncodedSignOperatorException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    public String verifySignFromEncodedBytes(
        String smime,
        String signDate,
        String encodedSign,
        String encodedCertificate,
        String signAlg
    ) throws UcaimaException {
        Date date = null;
        try {
            date = DateHelper.stringToDate(signDate);
        } catch (DateHelperException ex) {
            date = null;
        }

        try {
            byte[] data = SMimeCoderHelper.getSMimeDecoded(smime);
            
            X509Certificate certificate;
            if(encodedCertificate == null) certificate = UcaimaServerObjects.getServerKeys().getSignCertificate();
            else certificate = CertificateHelper.decode(encodedCertificate);
            
            verifyCertificateToOldSign(certificate, date);
            
            return Boolean.toString(
                EncodedSignOperator.verifySMimeEncodedSignOfString(
                    data,
                    date,
                    encodedSign,
                    certificate,
                    signAlg,
                    null
                )
            );
            
        } catch (SMimeCoderHelperException | CertificateHelperException | EncodedSignOperatorException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Preprocesar data[] codificada en SMime/Base64 que será finalmente firmada
     * del lado del cliente.
     * 
     * @param signStandard
     * @param smime
     * @param signAlg
     * @return
     * @throws UcaimaException 
     */
    public String getHexHashToSignFromEncodedBytes(
        String signStandard,
        String smime,
        String signAlg
    ) throws UcaimaException {
        if(!UcaimaServerObjects.isAbleToSign())
            throw new UcaimaException(UcaimaException.SERVER_UNABLE_TO_SIGN);
        
        //OJO... De momento se confía en la hora del server
        try {
            byte[] data = SMimeCoderHelper.getSMimeDecoded(smime);
            
            return EncodedSignOperator.getHexHashToSign(
                signStandard, 
                data, 
                new Date(), 
                signAlg
            );
            
        } catch (SMimeCoderHelperException | EncodedSignOperatorException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public String unpackKawiPackToXML(String encryptedPack) throws UcaimaException {
        try {
            return Kawi.decryptSMimeKawiPack(encryptedPack, UcaimaServerObjects.getServerKeys().getPrivateKey());
            
        } catch (KawiException ex) {
            throw new UcaimaException(ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public String signLocalPDF(
        String pdfInPath,
        String pdfOutPath, 
        String readPass, 
        String writePass, 
        String reason, 
        String location, 
        String contact, 
        String signAlg, 
        String noModify, 
        String visible, 
        String page,
        String image, 
        String imgP1X, 
        String imgP1Y, 
        String imgP2X, 
        String imgP2Y, 
        String imgRotation
    ) throws UcaimaException {
        if(!UcaimaServerObjects.isAbleToSign())
            throw new UcaimaException(UcaimaException.SERVER_UNABLE_TO_SIGN);
        
        try {
            visible = visible.trim();
            page = page.trim();
            byte[] img = SMimeCoderHelper.getSMimeDecoded(image);
            Float x1 = Float.parseFloat(imgP1X);
            Float y1 = Float.parseFloat(imgP1Y);
            Float x2 = Float.parseFloat(imgP2X);
            Float y2 = Float.parseFloat(imgP2Y);
            int rotation = Integer.parseInt(imgRotation);
            
            File inPdfFile = new File(pdfInPath);
            
            byte[] signedPDF = 
                PDFOperator.signLocalPDF(
                    PDFOperator.getPdfReader(inPdfFile, readPass), 
                    UcaimaServerObjects.getServerKeys().getPrivateKey(), 
                    UcaimaServerObjects.getServerKeys().getSignCertificate(),
                    readPass, 
                    writePass, 
                    reason, 
                    location, 
                    contact, 
                    new Date(), 
                    signAlg, 
                    Boolean.parseBoolean(noModify), 
                    Boolean.parseBoolean(visible), 
                    Integer.parseInt(page),
                    img,
                    x1,
                    y1,
                    x2,
                    y2,
                    rotation,
                    UcaimaServerObjects.getServerKeys().getRepositoryCryptographyProvider()
            );
            
            FileHelper.write(pdfOutPath, signedPDF);
            
            return UCAIMA_POSSITIVE_ANSWER;
            
        } catch (NumberFormatException | PDFOperadorException | IOException | SMimeCoderHelperException ex) {
            throw new UcaimaException(ex);
        }
    }
}
