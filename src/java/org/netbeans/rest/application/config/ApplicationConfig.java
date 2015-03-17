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

package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * ApplicationConfig
 *
 * @author Ing. Felix D. Lopez M. - flex.developments en gmail
 * @version 1.0
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(flex.ucaima.services.GenerateEncodedSignOfString.class);
        resources.add(flex.ucaima.services.GenerateSignFromEncodedBytes.class);
        resources.add(flex.ucaima.services.GetHexHashToSignFromEncodedBytes.class);
        resources.add(flex.ucaima.services.SignLocalPDF.class);
        resources.add(flex.ucaima.services.UnpackKawiPackToXML.class);
        resources.add(flex.ucaima.services.VerifyEncodedSignOfString.class);
        resources.add(flex.ucaima.services.VerifySignFromEncodedBytes.class);
    }
}
