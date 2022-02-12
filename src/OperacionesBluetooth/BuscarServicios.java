/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OperacionesBluetooth;

import java.awt.Color;
import java.awt.Label;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jesus
 */
public class BuscarServicios{

 
 public static final Vector/*<String>*/ serviceFound = new Vector();
 //public static final Vector dispConServicio = new Vector();
 //public static final Vector nombreDisp = new Vector();
 static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);
 private boolean hayServicio=false;
 JTable tabla;
 int cont=-1;

 public BuscarServicios(JTable t){
   tabla = t;
 }

 public void buscarS() throws IOException, InterruptedException{
    serviceFound.clear(); 
    UUID serviceUUID = OBEX_OBJECT_PUSH;
    final Object serviceSearchCompletedEvent = new Object();
    final DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();    

DiscoveryListener servicios = new DiscoveryListener(){
  public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) { }

  public void inquiryCompleted(int discType) { }

  public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
  for (int i = 0; i < servRecord.length; i++) {
      String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
     //String url = servRecord[i].getConnectionURL(ServiceRecord.AUTHENTICATE_NOENCRYPT, false);
      if (url == null)
         continue;
     serviceFound.add(url);
     DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
      if (serviceName != null) {
         System.out.println("servicio " + serviceName.getValue() + " encontrado " + url);         
         tableModel.addRow(new Object[1]);
         tabla.setValueAt(url, ++cont, 1);
         hayServicio=true;  //bandera
      } else {
         System.out.println("servicio encontrado " + url);
      }
    }
   }

   public void serviceSearchCompleted(int transID, int respCode) {
     System.out.println("Busqueda de servicio completada!");
    synchronized(serviceSearchCompletedEvent){
       serviceSearchCompletedEvent.notifyAll();
    }
   }

  }; //servicios

  UUID[] searchUuidSet = new UUID[] { serviceUUID };
  int[] attrIDs = new int[] {  0x0100 }; // Service name
   for(Enumeration en = BuscarDispositivos.devicesDiscovered.elements(); en.hasMoreElements(); ) {
      RemoteDevice DispBTooth = (RemoteDevice)en.nextElement();

      synchronized(serviceSearchCompletedEvent) {
        System.out.println("Buscando servicio en " + DispBTooth.getBluetoothAddress() + " " + DispBTooth.getFriendlyName(false));
        LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, DispBTooth, servicios);
        serviceSearchCompletedEvent.wait();
        if(hayServicio){
          if(tabla!=null){             
             tabla.setValueAt(DispBTooth.getFriendlyName(false), cont, 0);
             hayServicio=true;
          }
          //nombreDisp.add(DispBTooth.getFriendlyName(false));
          //hayServicio=false;
        }
      }
   }
 } //buscarS
}
