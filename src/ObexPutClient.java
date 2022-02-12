import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.microedition.io.Connector;
import javax.obex.*;

public class ObexPutClient {

    public static void main(String[] args) {
       try{
           String[] a = {,};
       new ObexPutClient().iniciar(a);
       }catch(Exception e){}
    }

    public void iniciar(String[] args) throws IOException, InterruptedException {

        //String serverURL = null;// "btgoep://001CA4FD09F6:6"; //"btgoep://0015832B8516:6";   este es del dispositivo local
        String serverURL = "btgoep://001CA4FD09F6:6"; //   M@LO
        if ((args != null) && (args.length > 0)) {
            serverURL = args[0];
        }

        if (serverURL == null) {
            String[] searchArgs = null;
            // Connect to OBEXPutServer from examples
             searchArgs = new String[] { "6" };
            ServicesSearch.main(searchArgs);
//            if (ServicesSearch.serviceFound.size() == 0) {
//                System.out.println("OBEX service not found");
//                return;
//            }
            // Select the first service found
//            serverURL = (String)ServicesSearch.serviceFound.elementAt(0);
        }

        System.out.println("Connecting to " + serverURL);

        ClientSession clientSession = (ClientSession) Connector.open(serverURL);
        HeaderSet hsConnectReply = clientSession.connect(null);
        if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
            System.out.println("Failed to connect");
            return;
        }

        HeaderSet hsOperation = clientSession.createHeaderSet();
        hsOperation.setHeader(HeaderSet.NAME, "1.jpg");
        hsOperation.setHeader(HeaderSet.TYPE, "Image");
        
        //Create PUT Operation
        Operation putOperation = clientSession.put(hsOperation);
        
        OutputStream os = putOperation.openOutputStream();
        // Send some text to server

        InputStream in = getClass().getResourceAsStream("1.jpg");
        byte[] buff = new byte[1024];
      //  ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

        try {
            int length;
            while (true) {
                 length = in.read(buff);
                if (length == -1) {
                    break;
                }
                os.write(buff);
                os.flush();
                //baos.write(buff, 0, length);
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen..."+getClass().getResourceAsStream("admin.png").toString());
        }

      //  byte todo[] = im.toString().getBytes();
       // byte data[] = "Hola chico malo!".getBytes("UTF-8");

        //FileOutputStream f = new FileOutputStream(dir.getFile());

        
       // os.write(data);
       // os.flush();
        os.close();
        putOperation.close();
        clientSession.disconnect(null);
        clientSession.close();
    }

    public URL getURL(){
        try {
            return getClass().getClassLoader().getResource("admin.png");
        } catch (Exception e) {
        }
        return null;
    }
}