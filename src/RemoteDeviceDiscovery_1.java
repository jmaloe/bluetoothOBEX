import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;

public class RemoteDeviceDiscovery_1 {
    public static final Vector<RemoteDevice> devicesDiscovered = new Vector<RemoteDevice>();

    public static void main(String[] args){

        final Object inquiryCompletedEvent = new Object();
	
		/**
		 * The DiscoveryListener interface allows an application to 
		 * receive device discovery and service discovery events.
		 */

        DiscoveryListener listener = new DiscoveryListener() {
			/**
			 * Called when a device is found during an inquiry.
			 */
			
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");				
                devicesDiscovered.addElement(btDevice);				
                try{
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } 
				catch (IOException cantGetDeviceName) {}
            }
			
			/**
			 * Called when an inquiry is completed.
			 */
            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
				
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {}
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {}
			
        };	// End of DiscoveryListener

		////////////////////////////////////////////////////////////////

        synchronized(inquiryCompletedEvent) {
		
            boolean started = false;
            try {
                started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            } catch (Exception e) {
                //Logger.getLogger(RemoteDeviceDiscovery_1.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error:"+e.toString());
            }
			
            if (started) {
                System.out.println("Starting Device Discovery process ...");
                try {                  
                    inquiryCompletedEvent.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RemoteDeviceDiscovery_1.class.getName()).log(Level.SEVERE, null, ex);
                }
				
                System.out.println("There was " + devicesDiscovered.size() +  " device(s) found");
            }
			else{
				System.out.println("The Device Discovery process failed!");
			}
        }
    }
} // End of class
