<%-- 
    Document   : body
    Created on : 08/11/2011, 11:02:29 AM
    Author     : Ing. Felix D. Lopez M. - flex.developments@gmail.com | flopez@suscerte.gob.ve
                 Ing. Yessica De Ascencao - yessicadeascencao@gmail.com | ydeascencao@suscerte.gob.ve
    Version    : 1.0
--%>
<!DOCTYPE HTML>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="resources/estilos/estilos.css" rel="stylesheet" type="text/css">
        <style>
           .sizeFrame{height: 95%;}
        </style>
        <script type="text/javascript">
        function redimensionar() {
            if (navigator.appName == 'Microsoft Internet Explorer') {		
                    document.getElementById('tabla').style.height = '96%';
            } else  {
                    var alto=window.innerHeight -20;
                    document.getElementById('tabla').style.height = alto + 'px';
            }
        }
        </script>
    </head>
    <body onload="javascript:redimensionar()">
        <table style="height: 347px;" id="tabla" align="left" border="0" width="570">
            <tbody>
                <tr>
                    <td valign="top" width="200">
                        <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" height="340" width="200">
                            <param name="movie" value="recursos/menuHTML.swf">
                            <param name="quality" value="high">
                            <embed src="resources/menuHTML.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" height="340" width="200">
                        </object>
                    </td>
                    <td valign="top" height="100%"> 
                        <iframe class="sizeFrame" name="informacion" src="informacion.jsp" frameborder="0" scrolling="auto" width="620"></iframe>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>