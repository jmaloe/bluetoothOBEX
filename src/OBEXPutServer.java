import java.io.IOException;
import java.io.InputStream;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.obex.*;

public class OBEXPutServer {

    static final int serverUUID = 0x01101; //nombre del servicio

    public static void main(String[] args) throws IOException {
       // LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
      SessionNotifier serverConnection = (SessionNotifier) Connector.open("btgoep://localhost:"+serverUUID + ";name=Ejemplo");

      int count = 0;
        while(count < 2) {
            RequestHandler handler = new RequestHandler();
            serverConnection.acceptAndOpen(handler);
            System.out.println("Received OBEX connection " + (++count));
        }
    }

    private static class RequestHandler extends ServerRequestHandler {

        @Override
        public int onPut(Operation op) {
            try {
                HeaderSet hs = op.getReceivedHeaders();
                String name = (String) hs.getHeader(HeaderSet.NAME);
                if (name != null) {
                    System.out.println("put name:" + name);
                }

                InputStream is = op.openInputStream();

                StringBuffer buf = new StringBuffer();
                int data;
                while ((data = is.read()) != -1) {
                    buf.append((char) data);
                }

                System.out.println("got:" + buf.toString());

                op.close();
                return ResponseCodes.OBEX_HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
            }
        }
    }
}
