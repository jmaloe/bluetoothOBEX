import javax.bluetooth.BluetoothStateException;

import javax.bluetooth.LocalDevice;


public class PcServer {

/**
* @declarations
*/
protected LocalDevice lDevice;

public PcServer()throws BluetoothStateException {
 try{
    // retrieve the local Bluetooth device object
    lDevice = LocalDevice.getLocalDevice();
  }catch(Exception e){
   e.printStackTrace();
  }

// retrieve the Bluetooth address of the local device
String address = lDevice.getBluetoothAddress();
    System.out.println("La direccion de mi bluetooth es:"+address);

// retrieve the name of the local Bluetooth device
String name = lDevice.getFriendlyName();
    System.out.println("Mi friendly name es:"+name);
}

/**
* @param args
*/
public static void main(String[] args) throws BluetoothStateException {
// TODO Auto-generated method stub
    PcServer pcSvr = new PcServer();
    System.out.println("Servidor..."+pcSvr);
 }
}