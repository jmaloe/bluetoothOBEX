/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jesus
 */

import javax.microedition.io.*;
import javax.bluetooth.*;
import java.io.*;
import java.util.*;
public class buscando {
    /**
 * @author jesus
 */

  //Creamos las variables necesarias

  //Objetos Bluetooth necesarios
  public LocalDevice dispositivoLocal;
  public DiscoveryAgent da;
  //Lista de dispositivos y servicios encontrados
  public static Vector dispositivos_encontrados = new Vector();
  public static Vector servicios_encontrados = new Vector();
  public static int dispositivo_seleccionado = -1;

  public static DataOutputStream out;
  public static DataInputStream in;


  Calendar cal;
  public static buscador bus=null;

  //Constructor
  public buscando(){
      cal = Calendar.getInstance();
      String s = String.valueOf(cal.getTime());
      s = s.substring(4, 19);
  }
  //Implementamos el ciclo de vida del MIDlet
  public static void main(String arg[]){
      System.out.println("Iniciando...");
      bus = new buscador();
      System.out.println("Ternimando...");     
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
  }
  //Este metodo se encarga de las tareas necesarias para salir del MIDlet

  //Este metodo se encarga de dar un aviso de alarma cuando se produce una excepcion

//Este metodo se va a encargar de enviar un mensaje al primer ServiceRecord usando el
//Serial Port Profile
//public static void prepararConexion(){
//        System.out.println("preparando conexion...");
//   ServiceRecord sr = (ServiceRecord)servicios_encontrados.elementAt(0);
//   //Obtenemos la URL asociada a este servicio en el dispositivo remoto
//   String URL = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,false);
//   try{
//      //Obtenemos la conexio y el stream de este servicio
//      StreamConnection con = (StreamConnection)Connector.open(URL);
//      out = con.openDataOutputStream();
//      in = con.openDataInputStream();
//      //Escribimos datos en el stream
//   }catch(Exception e){
//       System.out.println("Error:"+e.toString());
//   }
//  }

   //Implementamos el DiscoveryListener
  public static class buscador implements DiscoveryListener{
  //Implementamos los metodos del interfaz DiscoveryListener

      public void deviceDiscovered(RemoteDevice dispositivoRemoto, DeviceClass clase){
        System.out.println("Se ha encontrado un dspositivo Bluetooth");
        dispositivos_encontrados.addElement(dispositivoRemoto);
      }

      public void inquiryCompleted(int completado){
        System.out.println("Se ha completado la busqueda de dispositivos");
        if(dispositivos_encontrados.size()==0){
            System.out.println("Ningun dispositivos encontrado...");
        }
        else{
            mostrarDispositivos();
            System.out.println(dispositivos_encontrados.size()+"dispositivos encontrados...!");
        }
      }

    public void mostrarDispositivos(){
     if(dispositivos_encontrados.size()>0){
        for(int i=0;i<dispositivos_encontrados.size();i++){
          try{
            RemoteDevice dispositivoRemoto = (RemoteDevice) dispositivos_encontrados.elementAt(i);
            System.out.println("disp:"+dispositivos_encontrados.elementAt(i));
          }catch(Exception e){
            System.out.println("Se ha producido una excepcion");
          }
         }
        }
        else
              System.out.println("No hay dispositivos...");
      }

      public void servicesDiscovered(int transID, ServiceRecord[] servRecord){
        System.out.println("Se ha encontrado un servicio remoto");
        for(int i=0;i<servRecord.length;i++){
            ServiceRecord record = servRecord[i];
            servicios_encontrados.addElement(record);
        }
      }

      public void serviceSearchCompleted(int transID, int respCode){
        System.out.println("Terminada la busqueda de servicios");
        //Si encontramos un servicio, lo usamos para mandar el mensaje(todos los
        //servicios que hemos buscado son de puerto serie)        
        //Si no encontramos ningun servicio de puerto serie
        System.out.println("aqui...");
            mostrarDispositivos();       
      }
  }
}
