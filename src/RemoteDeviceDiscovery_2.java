import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.*;
import javax.bluetooth.DeviceClass;
public class RemoteDeviceDiscovery_2 
{
	////////////////////////////////////////////////////////////////////

	/**
	 * List of discovered devices
	 */

    public static final Vector devicesDiscovered = new Vector();

	////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws IOException, InterruptedException 
	{
		/**
		 * Synchronization object.
		 */

        final Object inquiryCompletedEvent = new Object();

		////////////////////////////////////////////////////////////////

		/**
		 * The DiscoveryListener interface allows an application to 
		 * receive device discovery and service discovery events.
		 */

        DiscoveryListener listener = new DiscoveryListener() 
		{
			/**
			 * Called when a device is found during an inquiry.
			 */
			
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) 
			{
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
				
                devicesDiscovered.addElement(btDevice);
				
                try 
				{
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } 
				catch (IOException cantGetDeviceName) {}
            }
			
			/**
			 * Called when an inquiry is completed.
			 */

            public void inquiryCompleted(int discType) 
			{
                System.out.println("Device Inquiry completed!");
				
                synchronized(inquiryCompletedEvent)
				{
                    inquiryCompletedEvent.notifyAll();
                }
            }
			
			/**
			 * Called when a service search is completed or was terminated because of an error.
			 */

            public void serviceSearchCompleted(int transID, int respCode) {}

			/**
			 * Called when service(s) are found during a service search.
			 */

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {}
			
        };	// End of DiscoveryListener

		////////////////////////////////////////////////////////////////

        synchronized(inquiryCompletedEvent) 
		{

			/**
			 * Get the DiscoveryAgent of the LocalDevice and start the
			 * Discovery process
			 */

            boolean s = LocalDevice.isPowerOn();

            System.out.println("s="+s);
           // boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
           // if (started)
			{
                System.out.println("Starting Device Discovery process ...");
				
				/**
				 * Wait for Discovery Process end
				 */
				
                //inquiryCompletedEvent.wait();
				
                System.out.println("There was " + devicesDiscovered.size() +  " device(s) found");
            }
			//else
			//{
			//	System.out.println("The Device Discovery process failed!");
			//}
        }
    }

	////////////////////////////////////////////////////////////////////

} // End of class
