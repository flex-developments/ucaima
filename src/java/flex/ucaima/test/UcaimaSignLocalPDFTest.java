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
import flex.ucaima.Ucaima;
import flex.helpers.FileHelper;
import flex.helpers.SMimeCoderHelper;
import java.io.File;

/**
 * UcaimaSignLocalPDFTest
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @author Ing. Yessica De Ascencao - yessicadeascencao en gmail
 * @version 1.0
 */
public class UcaimaSignLocalPDFTest {
    private static final String resources = 
        System.getProperty("user.home") + File.separator + "resources" + File.separator;
    private static final Ucaima server = new Ucaima();
    
    public static void main(String[] args) throws Exception {
        tryUcaimaSignLocalPDF();
        
        System.out.println("End...");
        System.exit(0);
    }
    
    private static void tryUcaimaSignLocalPDF() throws Exception {
        String pdfInPath = resources + "prueba.pdf";
        String pdfOutPath = resources + "prueba-Firmado.pdf";
        byte[] imageInByte = FileHelper.getBytes(resources + "fondo_firma.png");
        String imagePath = SMimeCoderHelper.getSMimeEncoded(imageInByte);
        
        System.out.println(
            server.signLocalPDF(pdfInPath, 
                pdfOutPath, 
                null, 
                null, 
                "razon", 
                "locacion", 
                "contacto", 
                AlgorithmsHelper.SIGN_ALGORITHM_SHA1_RSA, 
                "false", 
                "true", 
                "0", 
                imagePath, 
                "1", 
                "1", 
                "100", 
                "100", 
                "0")
        );
    }
}
