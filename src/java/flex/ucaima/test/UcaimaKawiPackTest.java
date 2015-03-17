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

import flex.kawi.applet.AppletKawi;
import flex.kawi.components.pack.KawiPack;
import flex.ucaima.Ucaima;
import flex.helpers.FileHelper;
import flex.helpers.SMimeCoderHelper;
import flex.kawi.components.pack.KawiPackHeaders;
import flex.kawi.components.queue.KawiDataQueueNode;
import java.util.ArrayList;
import java.util.List;

/**
 * UcaimaTest
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @author Ing. Yessica De Ascencao - yessicadeascencao en gmail
 * @version 1.0
 */
public class UcaimaKawiPackTest {
    private static final Ucaima server = new Ucaima();
    
    public static void main(String[] args) {
        tryUcaimaKawiPack();
        
        System.out.println("End...");
        System.exit(0);
    }
    
    private static void tryUcaimaKawiPack() {
        try {
            //Levantar el applet y dejo su configuracion por defecto
            AppletKawi applet = new AppletKawi();

            //Agregar data para fimar con el applet
            applet.addData("test1", "test1", KawiDataQueueNode.KAWI_QUEUE_DATA_NODE_TREATMENT_STRING);
            applet.addData("test2", "test2", KawiDataQueueNode.KAWI_QUEUE_DATA_NODE_TREATMENT_STRING);
            applet.addData("test3", "test3", KawiDataQueueNode.KAWI_QUEUE_DATA_NODE_TREATMENT_STRING);

            //Agregar una ruta adicional para probar el tratamiento de archivos
    //        applet.addData("id-local-file", resources + "prueba.pdf", KawiDataQueue.KAWI_TREATMENT_LOCAL_FILE);
    //        System.out.println("Agregando <prueba.pdf>");

            //Generar firma
            String dataToSend = applet.generateKawiPack();
            System.out.println("Firma Generada...");

            System.out.println("\n*********************Transmision**********************************\n");

            //Reconstruir paquete en el WS
            String xmlPackage = server.unpackKawiPackToXML(dataToSend);
            KawiPack kawiPackage = new KawiPack(xmlPackage);
            System.out.println(kawiPackage.toString());

            //Obtenger cabeceras que desee
            System.out.println(kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_DATE));
            System.out.println(kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_CONFIGURATION));

            //Verificacion de firmas
                //Obtener algoritmo de firma desde la cabecera
                String signAlg = kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_SIGN_ALG);
                System.out.println(signAlg);

                //Obtener certificado del firmante desde la cabecera
                String signCert = kawiPackage.getHeaders().getHeader(KawiPackHeaders.KAWI_PACK_HEADER_CERTIFICATE);
                System.out.println(signCert);

                //Construyo la lista de data para verificar
                List<String> dataList = new ArrayList<>();
                dataList.add("test1");
                dataList.add("test2");
                dataList.add("test3");

                //Agregar una ruta adicional para probar el tratamiento de archivos
    //            dataList.add(resources + "prueba.pdf");
    //            System.out.println("Agregando <prueba.pdf>");
                
                for(int x = 0; x < dataList.size(); x++) {
                    String data = dataList.get(x);
                    String sign = kawiPackage.getSignatures().getSignature(String.valueOf(x));
                    String date = kawiPackage.getSignatures().getDate(String.valueOf(x));
                    
                    System.out.println("Verified? = " + server.verifyEncodedSignOfString(data, signCert, sign, date, signAlg));
                }

                //Verificar la firma electrónica generada sobre el archivo
                for(int x = 0; x < dataList.size(); x++) {
                    String encodeFile = SMimeCoderHelper.getSMimeEncoded(FileHelper.getBytes(dataList.get(x)));
                    String sign = kawiPackage.getSignatures().getSignature(String.valueOf(x));
                    String date = kawiPackage.getSignatures().getDate(String.valueOf(x));
                    
                    System.out.println("Verifed? = " + server.verifySignFromEncodedBytes(encodeFile, signCert, sign, date, signAlg));
                }
                
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
