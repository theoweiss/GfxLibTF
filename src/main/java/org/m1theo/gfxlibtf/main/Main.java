package org.m1theo.gfxlibtf.main;

import java.io.IOException;
import java.net.UnknownHostException;

import org.m1theo.gfxlibtf.GfxLibTF128x64;
import org.m1theo.gfxlibtf.IGfxLib;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletOLED128x64;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String host = args[0];
        String port = args[1];
        String uid = args[2];
        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletOLED128x64 oled = new BrickletOLED128x64(uid, ipcon); // Create device object
        try {
            ipcon.connect(host, Integer.parseInt(port));
            // Clear display
            oled.clearDisplay();
            oled.newWindow((short) 0, (short) 128, (short) 0, (short) 7);
            IGfxLib bricklet = new GfxLibTF128x64(oled);
            // try {
            // snake(oled, bricklet);
            // } catch (InterruptedException e) {
            // System.exit(0);
            // }
            // bricklet.drawLine(0, 0, 127, 56, 1);
            // bricklet.print("hans");
            bricklet.drawCircle(60, 60, 40, 1);
            System.out.println("Press ctrl+c to exit");
            bricklet.flush();
            // while (true) {
            // try {
            // Thread.sleep(1000);
            // } catch (InterruptedException e) {
            // System.exit(0);
            // }
            // }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void snake(BrickletOLED128x64 oled, IGfxLib bricklet)
            throws TimeoutException, NotConnectedException, InterruptedException {
        int x = 125;
        int y = 0;
        for (x = 0; x < 128; x += 1) {
            bricklet.drawPixel(x, y, 1);
            bricklet.drawPixel(x + 1, y, 1);
            bricklet.drawPixel(x + 2, y, 1);
            bricklet.drawPixel(x, y + 1, 1);
            bricklet.drawPixel(x + 1, y + 1, 1);
            bricklet.drawPixel(x + 2, y + 1, 1);
            bricklet.flush();
            if (x == 127) {
                x = -1;
                y += 1;
            }
            Thread.sleep(100);
            bricklet.clear();
        }

    }
}
