/*
 * ucaima
 * 
 * Copyright (C) 2010
 * Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * 
 * Desarrollo apoyado por la Superintendencia de Servicios de Certificación 
 * Electrónica (SUSCERTE) durante 2010-2014 por:
 * Ing. Felix D. Lopez M. - fdmarchena2003@hotmail.com | flopez@suscerte.gob.ve
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
import flex.helpers.SMimeCoderHelper;
import flex.kawi.applet.AppletKawi;
import flex.kawi.components.pack.KawiPack;
import flex.kawi.components.pack.KawiPackHeaders;
import flex.ucaima.Ucaima;

/**
 * UcaimaGetHexHashToSignFromEncodedBytesTest
 *
 * @author Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * @version 1.0
 */
public class UcaimaGetHexHashToSignFromEncodedBytesTest {
    private static final Ucaima server = new Ucaima();
    
    public static void main(String[] args) throws Exception {
        tryUcaimaGetHexHashToSignFromEncodedBytes();
        
        System.out.println("End...");
        System.exit(0);
    }
    
    private static void tryUcaimaGetHexHashToSignFromEncodedBytes() throws Exception {
        String smime = SMimeCoderHelper.getSMimeEncoded("test".getBytes());
            
        String hash = server.getHexHashToSignFromEncodedBytes(
            EncodedSignOperator.ENCODED_SIGN_STANDARD_BASIC, 
            smime,
            AlgorithmsHelper.SIGN_ALGORITHM_SHA512_RSA
        );
        
        //Levantar el applet
        AppletKawi applet = new AppletKawi();
        applet.setConfiguration("dHlwZT1QS0NTMTIKcGF0aD0vaG9tZS9mbG9wZXovcmVzb3VyY2VzL2MxLTIucDEy");
        
        //Agregar hash para que sea tratado como archivo remoto
        applet.addData("signID", hash, 2);
        
        String dataToSend = applet.generateKawiPack();
        System.out.println("Signed...");
        
        System.out.println("\n*********************Transmision**********************************\n");
        
        //Reconstruir paquete en el WS
        String xmlPackage = server.unpackKawiPackToXML(dataToSend);
        KawiPack kawiPackage = new KawiPack(xmlPackage);
        System.out.println(kawiPackage.toString());
        
        //Obtengo cabeceras que desee
        System.out.println(kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_DATE));
        System.out.println(kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_CONFIGURATION));
        
        //Verificacion de firmas
            //Obtener algoritmo de firma desde la cabecera
            String signAlg = kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_SIGN_ALG);
            System.out.println(signAlg);
            
            //Obtener certificado del firmante desde la cabecera
            String signCert = kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_CERTIFICATE);
            System.out.println(signCert);
        
        String sign = kawiPackage.getSignatures().getSignature("signID");
        String date = kawiPackage.getSignatures().getDate("signID");
            
        System.out.println("Verified ? " + server.verifySignFromEncodedBytes(smime, signCert, sign, date, signAlg));
    }
}
