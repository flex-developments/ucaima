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

package flex.ucaima.test;

import flex.eSign.helpers.AlgorithmsHelper;
import flex.eSign.operators.signers.EncodedSignOperator;
import flex.ucaima.Ucaima;

/**
 * UcaimaTest
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @author Ing. Yessica De Ascencao - yessicadeascencao en gmail
 * @version 1.0
 */
public class UcaimaSignStringTest {
    private static final Ucaima server = new Ucaima();
    
    public static void main(String[] args) throws Exception {
        tryUcaimaSignString();
        
        System.out.println("End...");
        System.exit(0);
    }
    
    private static void tryUcaimaSignString() throws Exception {
        String signStandard = EncodedSignOperator.ENCODED_SIGN_STANDARD_BASIC;
        String data = "test";
        String signAlg = AlgorithmsHelper.SIGN_ALGORITHM_SHA256_RSA;
        
        String sign = server.generateEncodedSignOfString(signStandard, data, signAlg);
        System.out.println(sign);
    }
}
