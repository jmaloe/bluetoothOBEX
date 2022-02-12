
import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jesus
 */
public class encontrados {

    public static Vector devicesDiscovered;
   public static Object inquiryCompletedEvent = new Object();

   public static void main(String arg[]){

        /**
         * The DiscoveryListener interface allows an application to
         * receive device discovery and service discovery events.
         */

       System.out.println("Iniciando...");
        DiscoveryListener listener = new DiscoveryListener(){
            
            public void inquiryCompleted(int discType){
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }
            // Not used in this example.

            public void serviceSearchCompleted(int transID, int respCode) {}           

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                
                devicesDiscovered.addElement(btDevice);
                try{
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                }catch (IOException cantGetDeviceName) {}
            }

            public void servicesDiscovered(int arg0, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) {                    
                // String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                String url = servRecord[i].getConnectionURL(ServiceRecord.AUTHENTICATE_NOENCRYPT, false);
                if (url == null) {
                 continue;
                }
                devicesDiscovered.add(url);
                DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                if (serviceName != null)
                    System.out.println("service " + serviceName.getValue() + " found " + url);
                 else
                System.out.println("service found " + url);
               }
            }
        };

        System.out.println("saliendo...");
   }
}
