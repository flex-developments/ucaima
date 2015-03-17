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

package flex.ucaima.services;

import flex.ucaima.Ucaima;
import flex.ucaima.UcaimaLogger;
import flex.ucaima.exceptions.UcaimaException;
import flex.ucaima.UcaimaServerObjects;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Stateless
@Path("/generatesignfromencodedbytes")
/**
 * GenerateSignFromEncodedBytes
 * 
 * @author Ing. Felix D. Lopez M. - flex.developments@gmail.com
 * @author Ing. Yessica De Ascencao - yessicadeascencao@gmail.com
 * @version 1.0
 */
public class GenerateSignFromEncodedBytes {
    
    @POST
    public String GenerateSignFromEncodedBytes(
        @FormParam("signStandard") String signStandard,
        @FormParam("smime") String smime,
        @FormParam("signAlg") String signAlg
    ) {
        try {
            Ucaima server = new Ucaima();
            
            String result = server.generateSignFromEncodedBytes(
                signStandard, 
                smime, 
                signAlg
            );
            
            server = null;
            return result;
        
        } catch (UcaimaException ex) {
            UcaimaLogger.writeErrorLog(ex);
            return UcaimaServerObjects.getServerMessageError();
        }
    }
}
