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

package flex.ucaima.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * I18n
 * Clase estatica para internacionalizacion de los mensajes.
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * @version 1.0
 */
public class UcaimaPropertiesBundle {
    final private static String BUNDLE_PATH = "flex/ucaima/resources/UcaimaConfig";
    
    //List Resource Keys........................................................
    final public static String UCAIMA_GENERAL_MESSAGE_ERROR = "UCAIMA_GENERAL_MESSAGE_ERROR";
    final public static String UCAIMA_CERTIFICATE_PATH = "UCAIMA_CERTIFICATE_PATH";
    final public static String UCAIMA_KEYSTORE_PATH = "UCAIMA_KEYSTORE_PATH";
    final public static String UCAIMA_KEYSTORE_PASS = "UCAIMA_KEYSTORE_PASS";
    final public static String UCAIMA_KEYSTORE_ALIAS = "UCAIMA_KEYSTORE_ALIAS";
    final public static String UCAIMA_DRIVER_VERIFY_INTEGRITY = "UCAIMA_DRIVER_VERIFY_INTEGRITY";
    final public static String UCAIMA_NTP_SERVERS = "UCAIMA_NTP_SERVERS";
    final public static String UCAIMA_VERIFY_CERTIFICATE_TRY_DOWNLOAD_LCR = "UCAIMA_VERIFY_CERTIFICATE_TRY_DOWNLOAD_LCR";
    final public static String UCAIMA_VERIFY_CERTIFICATE_WITH_HOST = "UCAIMA_VERIFY_CERTIFICATE_WITH_HOST";
    final public static String UCAIMA_VERIFY_CERTIFICATE_WITH_OCSP = "UCAIMA_VERIFY_CERTIFICATE_WITH_OCSP";
    final public static String UCAIMA_CERTIFICATE_INVALID_ON_NTP_FAIL = "UCAIMA_CERTIFICATE_INVALID_ON_NTP_FAIL";
    final public static String UCAIMA_CERTIFICATE_INVALID_ON_OCSP_FAIL = "UCAIMA_CERTIFICATE_INVALID_ON_OCSP_FAIL";
    final public static String UCAIMA_CERTIFICATE_INVALID_ON_LCR_FAIL = "UCAIMA_CERTIFICATE_INVALID_ON_LCR_FAIL";
    final public static String UCAIMA_CERTIFICATE_INVALID_ON_BOTH_FAIL = "UCAIMA_CERTIFICATE_INVALID_ON_BOTH_FAIL";
    final public static String UCAIMA_CERTIFICATE_VERIFY_VERBOSE = "UCAIMA_CERTIFICATE_VERIFY_VERBOSE";
    //--------------------------------------------------------------------------
    
    /**
     * Obtener String internacionalizado.
     * 
     * @param key Clave del string dentro del bundle.
     * 
     * @return valor de la clave dentro del bundle.
     */
    public static String get(String key) {
        return ResourceBundle.getBundle(BUNDLE_PATH).getString(key);
    }
    
    /**
     * Obtener String internacionalizado con formato.
     * 
     * @param key Clave del string dentro del bundle.
     * @param arguments Argumentos para el formato.
     * 
     * @return valor de la clave dentro del bundle con formato procesado.
     */
    public static String get(String key, Object ... arguments) {
        MessageFormat temp = new MessageFormat(get(key));
        return temp.format(arguments);
    }

    public static String getLangPath() {
        return BUNDLE_PATH;
    }
}
