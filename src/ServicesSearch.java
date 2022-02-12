
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.*;

/**
* @author vlads
*
* Minimal ServicesSearch example for javadoc.
*/
public class ServicesSearch {

static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);

public ServicesSearch(){


}


public static void main(String[] args) throws IOException, InterruptedException {

// First run RemoteDeviceDiscovery and use discoved device
RemoteDeviceDiscovery.main(null);



UUID serviceUUID = OBEX_OBJECT_PUSH;
 if ((args != null) && (args.length > 0)) {
   serviceUUID = new UUID(args[0], false);
 }

final Object serviceSearchCompletedEvent = new Object();

DiscoveryListener servicios = new BuscarServicios();
//BuscarDispositivos dispositivos = new BuscarDispositivos();

 UUID[] searchUuidSet = new UUID[] { serviceUUID };
  int[] attrIDs = new int[] {  0x0100 }; // Service name

   for(Enumeration en = BuscarDispositivos.devicesDiscovered.elements(); en.hasMoreElements(); ) {
      RemoteDevice DispBTooth = (RemoteDevice)en.nextElement();

      synchronized(serviceSearchCompletedEvent) {
        System.out.println("search services on " + DispBTooth.getBluetoothAddress() + " " + DispBTooth.getFriendlyName(false));
        LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, DispBTooth, servicios);
        serviceSearchCompletedEvent.wait();
      }
}

}

}

class BuscarServicios implements DiscoveryListener{

 final Object serviceSearchCompletedEvent = new Object();
 public static final Vector/*<String>*/ serviceFound = new Vector();

 public BuscarServicios(){
   serviceFound.clear();
 }

 public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) { }

 public void inquiryCompleted(int discType) { }

 public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
  for (int i = 0; i < servRecord.length; i++) {
     // String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
     String url = servRecord[i].getConnectionURL(ServiceRecord.AUTHENTICATE_NOENCRYPT, false);
      if (url == null)
         continue;
     serviceFound.add(url);
     DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
      if (serviceName != null) {
         System.out.println("servicio " + serviceName.getValue() + " encontrado " + url);
      } else {
         System.out.println("servicio encontrado " + url);
      }
  }
 }

 public void serviceSearchCompleted(int transID, int respCode) {
     System.out.println("service search completed!");
    synchronized(serviceSearchCompletedEvent){
       serviceSearchCompletedEvent.notifyAll();
    }
 }
}

class BuscarDispositivos implements DiscoveryListener{
    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();

    public BuscarDispositivos(){
      devicesDiscovered.clear();
    }

    public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {

    }

    public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {

    }

    public void serviceSearchCompleted(int arg0, int arg1) {  }

    public void inquiryCompleted(int arg0) {   }

}