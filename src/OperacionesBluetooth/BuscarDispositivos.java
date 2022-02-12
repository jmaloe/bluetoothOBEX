/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OperacionesBluetooth;

import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 *
 * @author jesus
 */
public class BuscarDispositivos {
   public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();

   public void buscarD(){
     final Object inquiryCompletedEvent = new Object();
     devicesDiscovered.clear();

DiscoveryListener dispositivos = new DiscoveryListener(){
     public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
       System.out.println("Dispositivo " + btDevice.getBluetoothAddress() + " encontrado");
       devicesDiscovered.addElement(btDevice);
       try {
        System.out.println(" Nombre: " + btDevice.getFriendlyName(false));
       }catch (Exception cantGetDeviceName) { cantGetDeviceName.printStackTrace();}
     }

     public void inquiryCompleted(int discType) {
       System.out.println("La busqueda ha finalizado!");
       synchronized(inquiryCompletedEvent){
        inquiryCompletedEvent.notifyAll();
       }
    }

 public void serviceSearchCompleted(int transID, int respCode) { }

 public void servicesDiscovered(int transID, ServiceRecord[] servRecord) { }

};

  synchronized(inquiryCompletedEvent) {
   try{
       //iniciamos la busqueda...
    boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, dispositivos);
      if (started) {
       System.out.println("Buscando dispositivos...");
       inquiryCompletedEvent.wait();
       System.out.println(devicesDiscovered.size() + " dispositivo(s) encontrado(s)");
      }
   }catch(Exception e){}
  }
 } //buscar()
}
