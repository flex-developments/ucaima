/*
 * ucaima
 * 
 * Copyright (C) 2010
 * Ing. Felix D. Lopez M. - flex.developments en gmail
 * 
 * Desarrollo apoyado por la Superintendencia de Servicios de Certificación 
 * Electrónica (SUSCERTE) durante 2010-2014 por:
 * Ing. Felix D. Lopez M. - flex.developments en gmail | flopez en suscerte gob ve
 * Ing. Yessica De Ascencao - yessicadeascencao en gmail | ydeascencao en suscerte gob ve
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

import flex.helpers.LoggerHelper;

/**
 * KawiLogger
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @author Ing. Yessica De Ascencao - yessicadeascencao en gmail
 * @version 1.0
 */
public class UcaimaLogger {
    private static final LoggerHelper log = new LoggerHelper("UciamaLogger", LoggerHelper.LOG_TYPE_SINGLE_DATED);
    
    public static void writeInfoLog(String message) {
        log.writeInfoLog(message);
    }
    
    public static void writeWarningLog(String message) {
        log.writeWarningLog(message);
    }
    
    public static void writeErrorLog(String message) {
        log.writeErrorLog(message);
    }
    
    public static void writeErrorLog(Throwable ex) {
        log.writeErrorLog(ex);
    }
    
//    public static String getExceptionTrace(Exception ex) {
//        String message = "UCAIMA - " + ex.getLocalizedMessage();
//        
//        for(int i = 0; i < ex.getCause().getStackTrace().length ; i++) {
//            message = message + "\n ";
//            for(int o = 0; o < i; o++) message = message + " ";
//            
//            StackTraceElement e = ex.getCause().getStackTrace()[i];
//            message = message + e.toString();
//            if(e.getClassName().equals(Ucaima.class.getName())) break;
//        }
//        return message;
//    }
}
