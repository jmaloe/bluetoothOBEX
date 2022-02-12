/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OperacionesBluetooth;

import GUI.Bluetooth;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.swing.JOptionPane;

/**
 *
 * @author jesus
 */
public class Lanzador {

    private int length=0;

    public static void main(String[] args) throws IOException {
        Lanzador l = new Lanzador();
        l.mandarArchivo("btgoep://001CA4FD09F6:6", "/home/jesus/Imágenes/Paz/P_dove.png");
    }

    synchronized public void mandarArchivo(String url, String archivo) throws IOException{

      ClientSession clientSession = (ClientSession) Connector.open(url);
      HeaderSet hsConnectReply = clientSession.connect(null);
      
      HeaderSet hsOperation = clientSession.createHeaderSet();
        hsOperation.setHeader(HeaderSet.NAME, mocharCadena(archivo));
        hsOperation.setHeader(HeaderSet.TYPE, "File");        
//Create PUT Operation
        Operation putOperation = clientSession.put(hsOperation);
//Canal de escritura hacia el dispositivo con bluetooth
        OutputStream os = putOperation.openOutputStream();
//       
//        if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
//            System.out.println("Fallo la operacion al intentar conectar");
//            return;
//        }else{
//            System.out.println("el cuate ha aceptado");
//            Bluetooth.enviando=true;
//            t.start();
//        }

//          
//Iniciar la URL del archivo y crear el canal de lectura
        URL dir = new URL("file://"+archivo);       
        byte[] buff = new byte[1024];      
        FileInputStream ff = new FileInputStream(dir.getFile());
        //Bluetooth.progresoEnvio.setIndeterminate(false);
        Bluetooth.progresoEnvio.setVisible(true);
        Bluetooth.progresoEnvio.setString("Esperando respuesta");
        Bluetooth.progresoEnvio.setValue(0);
        totalKB = ff.available();
        totalKB = totalKB/1024;
        avance=0;
        Bluetooth.progresoEnvio.setMaximum(totalKB);

        Thread hiloAvance = new Thread(){
                @Override
          public void run(){
           avanzar();
          }
        };

        try {
            length = ff.read(buff);
            os.write(buff);
            os.flush();
        Bluetooth.enviando=true;
        hiloAvance.start();       
            while (true) {                
                 length = ff.read(buff);
                if (length == -1) {
                    break;
                }                 
                os.write(buff);
                os.flush();
                avance++;
            }
        } catch (IOException e) {           
            hiloAvance.interrupt();
            JOptionPane.showMessageDialog(null, "El envio ha sido rechazado", "Error de E/S", JOptionPane.CLOSED_OPTION);
        }
       Bluetooth.enviando=false;
       Bluetooth.progresoEnvio.setVisible(false);
       hiloAvance.interrupt();       
        ff.close();
        os.close();
        putOperation.close();
        clientSession.disconnect(null);
        clientSession.close();        
    }

    int inc;
    public String mocharCadena(String archi){       
       for( inc = archi.length()-1; inc>0; inc--){
          if(archi.charAt(inc)=='/' | archi.charAt(inc)=='\\') {
            break;
          }          
         }
        return archi.substring(inc+1,archi.length());
    }

    int avance = 0, totalKB=0;
    public void avanzar(){
      while(length!=-1){
        Bluetooth.progresoEnvio.setValue(avance);
        Bluetooth.progresoEnvio.setString(avance+" KB enviados de "+totalKB+" KB ~("+((avance*100)/totalKB)+"%)~");
      }
    }
}
