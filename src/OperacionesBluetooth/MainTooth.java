/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package OperacionesBluetooth;

import java.io.IOException;

/**
 *
 * @author jesus
 */
public class MainTooth {
    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.println("paso1");
        BuscarDispositivos bb = new BuscarDispositivos();
        bb.buscarD();
        System.out.println("paso2");
        BuscarServicios b = new BuscarServicios(null);
        b.buscarS();
    }
}
