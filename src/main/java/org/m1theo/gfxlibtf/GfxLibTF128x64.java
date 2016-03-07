package org.m1theo.gfxlibtf;

import com.tinkerforge.BrickletOLED128x64;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class GfxLibTF128x64 extends GfxLib {

    private BrickletOLED128x64 tinkerforgeDevice;

    public GfxLibTF128x64(BrickletOLED128x64 bricklet) {
        super(128, 64);
        this.tinkerforgeDevice = bricklet;
    }

    @Override
    public void flush() {
        try {
            int length = 64;
            // int offset = 0;
            while (poledbuff.remaining() > length) {
                byte[] dst = new byte[length];
                System.out.println("remaining: " + poledbuff.remaining());
                System.out.println("length: " + length);
                // System.out.println("offset: " + offset);
                poledbuff.get(dst, 0, length);
                // offset += 64;
                tinkerforgeDevice.write(byte2short(dst));
            }
            if (poledbuff.hasRemaining()) {
                byte[] dst = new byte[length];
                poledbuff.get(dst, 0, poledbuff.remaining());
                tinkerforgeDevice.write(byte2short(dst));
            }
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private short[] byte2short(byte[] bytes) {
        short[] shorts = new short[bytes.length];
        byte hi = (byte) 0;
        for (int i = 0; i < bytes.length; i++) {
            byte lo = bytes[i];
            short val = (short) (((hi & 0xFF) << 8) | (lo & 0xFF));
            shorts[i] = val;
        }
        return shorts;
    }
}
