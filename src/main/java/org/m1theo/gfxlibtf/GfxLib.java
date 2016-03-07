/*
This is the core graphics library for all our displays, providing a common
set of graphics primitives (points, lines, circles, etc.).  It needs to be
paired with a hardware-specific library for each display device we carry
(to handle the lower-level functions).
Adafruit invests time and resources providing this open source code, please
support Adafruit & open-source hardware by purchasing products from Adafruit!

Copyright (c) 2013 Adafruit Industries.  All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
- Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.
- Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

/*
 * Copyright (C) 2015 jcruz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.m1theo.gfxlibtf;

import java.nio.ByteBuffer;

/**
 *
 * @author theo@m1theo.org
 */
public abstract class GfxLib implements IGfxLib {

    public static final int WHITE = 1;
    public static final int BLACK = 0;
    protected ByteBuffer poledbuff;
    int _width;
    int _height;

    int rotation = 0;
    int cursor_y = 0;
    int cursor_x = 0;
    int textsize = 1;
    int textcolor = 0xFFFF;
    int textbgcolor = 0xFFFF;
    boolean wrap = true;

    public GfxLib(int width, int height) {
        // TODO hier muss die window groesse rein
        this._width = width;
        this._height = height;
        poledbuff = ByteBuffer.wrap(new byte[width * height / 8]);
    }

    @Override
    public void clear() {
        poledbuff = ByteBuffer.wrap(new byte[this._width * this._height / 8]);
    }

    // the most basic function, set a single pixel
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawPixel(int, int, int)
     */
    @Override
    public void drawPixel(int x, int y, int color) {
        // TODO hier muss die window groesse rein
        // ausserdem braucht man eine flush funktion, die das eigentlich Schreiben uebernimmmt

        if ((x < 0) || (x >= this._width) || (y < 0) || (y >= this._height)) {
            System.out.println("width: " + this._width);
            System.out.println("height " + this._height);
            System.out.println("coordinates out of bound x " + x + " y " + y);
            return;
        }

        poledbuff.clear();
        // Get where to do the change in the buffer
        int valor = poledbuff.get((x + (y / 8) * this._width));

        // x is which column
        if (color == 1) {
            poledbuff.put((x + (y / 8) * this._width), (byte) (valor | (1 << (y % 8))));
        } else {
            poledbuff.put((x + (y / 8) * this._width), (byte) (valor & ~(1 << (y % 8))));
        }
    }

    public void drawPixel1(int x, int y, int color) {

        if ((x < 0) || (x >= 128) || (y < 0) || (y >= 64)) {
            return;
        }

        poledbuff.clear();
        // Get where to do the change in the buffer
        int valor = poledbuff.get((x + (y / 8) * 128));

        // x is which column
        if (color == 1) {
            poledbuff.put((x + (y / 8) * 128), (byte) (valor | (1 << (y % 8))));
        } else {
            poledbuff.put((x + (y / 8) * 128), (byte) (valor & ~(1 << (y % 8))));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#flush(java.nio.ByteBuffer)
     */
    @Override
    public abstract void flush();

    short font[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x3E, 0x5B, 0x4F, 0x5B, 0x3E, 0x3E, 0x6B, 0x4F, 0x6B, 0x3E, 0x1C,
            0x3E, 0x7C, 0x3E, 0x1C, 0x18, 0x3C, 0x7E, 0x3C, 0x18, 0x1C, 0x57, 0x7D, 0x57, 0x1C, 0x1C, 0x5E, 0x7F, 0x5E,
            0x1C, 0x00, 0x18, 0x3C, 0x18, 0x00, 0xFF, 0xE7, 0xC3, 0xE7, 0xFF, 0x00, 0x18, 0x24, 0x18, 0x00, 0xFF, 0xE7,
            0xDB, 0xE7, 0xFF, 0x30, 0x48, 0x3A, 0x06, 0x0E, 0x26, 0x29, 0x79, 0x29, 0x26, 0x40, 0x7F, 0x05, 0x05, 0x07,
            0x40, 0x7F, 0x05, 0x25, 0x3F, 0x5A, 0x3C, 0xE7, 0x3C, 0x5A, 0x7F, 0x3E, 0x1C, 0x1C, 0x08, 0x08, 0x1C, 0x1C,
            0x3E, 0x7F, 0x14, 0x22, 0x7F, 0x22, 0x14, 0x5F, 0x5F, 0x00, 0x5F, 0x5F, 0x06, 0x09, 0x7F, 0x01, 0x7F, 0x00,
            0x66, 0x89, 0x95, 0x6A, 0x60, 0x60, 0x60, 0x60, 0x60, 0x94, 0xA2, 0xFF, 0xA2, 0x94, 0x08, 0x04, 0x7E, 0x04,
            0x08, 0x10, 0x20, 0x7E, 0x20, 0x10, 0x08, 0x08, 0x2A, 0x1C, 0x08, 0x08, 0x1C, 0x2A, 0x08, 0x08, 0x1E, 0x10,
            0x10, 0x10, 0x10, 0x0C, 0x1E, 0x0C, 0x1E, 0x0C, 0x30, 0x38, 0x3E, 0x38, 0x30, 0x06, 0x0E, 0x3E, 0x0E, 0x06,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x5F, 0x00, 0x00, 0x00, 0x07, 0x00, 0x07, 0x00, 0x14, 0x7F, 0x14,
            0x7F, 0x14, 0x24, 0x2A, 0x7F, 0x2A, 0x12, 0x23, 0x13, 0x08, 0x64, 0x62, 0x36, 0x49, 0x56, 0x20, 0x50, 0x00,
            0x08, 0x07, 0x03, 0x00, 0x00, 0x1C, 0x22, 0x41, 0x00, 0x00, 0x41, 0x22, 0x1C, 0x00, 0x2A, 0x1C, 0x7F, 0x1C,
            0x2A, 0x08, 0x08, 0x3E, 0x08, 0x08, 0x00, 0x80, 0x70, 0x30, 0x00, 0x08, 0x08, 0x08, 0x08, 0x08, 0x00, 0x00,
            0x60, 0x60, 0x00, 0x20, 0x10, 0x08, 0x04, 0x02, 0x3E, 0x51, 0x49, 0x45, 0x3E, 0x00, 0x42, 0x7F, 0x40, 0x00,
            0x72, 0x49, 0x49, 0x49, 0x46, 0x21, 0x41, 0x49, 0x4D, 0x33, 0x18, 0x14, 0x12, 0x7F, 0x10, 0x27, 0x45, 0x45,
            0x45, 0x39, 0x3C, 0x4A, 0x49, 0x49, 0x31, 0x41, 0x21, 0x11, 0x09, 0x07, 0x36, 0x49, 0x49, 0x49, 0x36, 0x46,
            0x49, 0x49, 0x29, 0x1E, 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x40, 0x34, 0x00, 0x00, 0x00, 0x08, 0x14, 0x22,
            0x41, 0x14, 0x14, 0x14, 0x14, 0x14, 0x00, 0x41, 0x22, 0x14, 0x08, 0x02, 0x01, 0x59, 0x09, 0x06, 0x3E, 0x41,
            0x5D, 0x59, 0x4E, 0x7C, 0x12, 0x11, 0x12, 0x7C, 0x7F, 0x49, 0x49, 0x49, 0x36, 0x3E, 0x41, 0x41, 0x41, 0x22,
            0x7F, 0x41, 0x41, 0x41, 0x3E, 0x7F, 0x49, 0x49, 0x49, 0x41, 0x7F, 0x09, 0x09, 0x09, 0x01, 0x3E, 0x41, 0x41,
            0x51, 0x73, 0x7F, 0x08, 0x08, 0x08, 0x7F, 0x00, 0x41, 0x7F, 0x41, 0x00, 0x20, 0x40, 0x41, 0x3F, 0x01, 0x7F,
            0x08, 0x14, 0x22, 0x41, 0x7F, 0x40, 0x40, 0x40, 0x40, 0x7F, 0x02, 0x1C, 0x02, 0x7F, 0x7F, 0x04, 0x08, 0x10,
            0x7F, 0x3E, 0x41, 0x41, 0x41, 0x3E, 0x7F, 0x09, 0x09, 0x09, 0x06, 0x3E, 0x41, 0x51, 0x21, 0x5E, 0x7F, 0x09,
            0x19, 0x29, 0x46, 0x26, 0x49, 0x49, 0x49, 0x32, 0x03, 0x01, 0x7F, 0x01, 0x03, 0x3F, 0x40, 0x40, 0x40, 0x3F,
            0x1F, 0x20, 0x40, 0x20, 0x1F, 0x3F, 0x40, 0x38, 0x40, 0x3F, 0x63, 0x14, 0x08, 0x14, 0x63, 0x03, 0x04, 0x78,
            0x04, 0x03, 0x61, 0x59, 0x49, 0x4D, 0x43, 0x00, 0x7F, 0x41, 0x41, 0x41, 0x02, 0x04, 0x08, 0x10, 0x20, 0x00,
            0x41, 0x41, 0x41, 0x7F, 0x04, 0x02, 0x01, 0x02, 0x04, 0x40, 0x40, 0x40, 0x40, 0x40, 0x00, 0x03, 0x07, 0x08,
            0x00, 0x20, 0x54, 0x54, 0x78, 0x40, 0x7F, 0x28, 0x44, 0x44, 0x38, 0x38, 0x44, 0x44, 0x44, 0x28, 0x38, 0x44,
            0x44, 0x28, 0x7F, 0x38, 0x54, 0x54, 0x54, 0x18, 0x00, 0x08, 0x7E, 0x09, 0x02, 0x18, 0xA4, 0xA4, 0x9C, 0x78,
            0x7F, 0x08, 0x04, 0x04, 0x78, 0x00, 0x44, 0x7D, 0x40, 0x00, 0x20, 0x40, 0x40, 0x3D, 0x00, 0x7F, 0x10, 0x28,
            0x44, 0x00, 0x00, 0x41, 0x7F, 0x40, 0x00, 0x7C, 0x04, 0x78, 0x04, 0x78, 0x7C, 0x08, 0x04, 0x04, 0x78, 0x38,
            0x44, 0x44, 0x44, 0x38, 0xFC, 0x18, 0x24, 0x24, 0x18, 0x18, 0x24, 0x24, 0x18, 0xFC, 0x7C, 0x08, 0x04, 0x04,
            0x08, 0x48, 0x54, 0x54, 0x54, 0x24, 0x04, 0x04, 0x3F, 0x44, 0x24, 0x3C, 0x40, 0x40, 0x20, 0x7C, 0x1C, 0x20,
            0x40, 0x20, 0x1C, 0x3C, 0x40, 0x30, 0x40, 0x3C, 0x44, 0x28, 0x10, 0x28, 0x44, 0x4C, 0x90, 0x90, 0x90, 0x7C,
            0x44, 0x64, 0x54, 0x4C, 0x44, 0x00, 0x08, 0x36, 0x41, 0x00, 0x00, 0x00, 0x77, 0x00, 0x00, 0x00, 0x41, 0x36,
            0x08, 0x00, 0x02, 0x01, 0x02, 0x04, 0x02, 0x3C, 0x26, 0x23, 0x26, 0x3C, 0x1E, 0xA1, 0xA1, 0x61, 0x12, 0x3A,
            0x40, 0x40, 0x20, 0x7A, 0x38, 0x54, 0x54, 0x55, 0x59, 0x21, 0x55, 0x55, 0x79, 0x41, 0x21, 0x54, 0x54, 0x78,
            0x41, 0x21, 0x55, 0x54, 0x78, 0x40, 0x20, 0x54, 0x55, 0x79, 0x40, 0x0C, 0x1E, 0x52, 0x72, 0x12, 0x39, 0x55,
            0x55, 0x55, 0x59, 0x39, 0x54, 0x54, 0x54, 0x59, 0x39, 0x55, 0x54, 0x54, 0x58, 0x00, 0x00, 0x45, 0x7C, 0x41,
            0x00, 0x02, 0x45, 0x7D, 0x42, 0x00, 0x01, 0x45, 0x7C, 0x40, 0xF0, 0x29, 0x24, 0x29, 0xF0, 0xF0, 0x28, 0x25,
            0x28, 0xF0, 0x7C, 0x54, 0x55, 0x45, 0x00, 0x20, 0x54, 0x54, 0x7C, 0x54, 0x7C, 0x0A, 0x09, 0x7F, 0x49, 0x32,
            0x49, 0x49, 0x49, 0x32, 0x32, 0x48, 0x48, 0x48, 0x32, 0x32, 0x4A, 0x48, 0x48, 0x30, 0x3A, 0x41, 0x41, 0x21,
            0x7A, 0x3A, 0x42, 0x40, 0x20, 0x78, 0x00, 0x9D, 0xA0, 0xA0, 0x7D, 0x39, 0x44, 0x44, 0x44, 0x39, 0x3D, 0x40,
            0x40, 0x40, 0x3D, 0x3C, 0x24, 0xFF, 0x24, 0x24, 0x48, 0x7E, 0x49, 0x43, 0x66, 0x2B, 0x2F, 0xFC, 0x2F, 0x2B,
            0xFF, 0x09, 0x29, 0xF6, 0x20, 0xC0, 0x88, 0x7E, 0x09, 0x03, 0x20, 0x54, 0x54, 0x79, 0x41, 0x00, 0x00, 0x44,
            0x7D, 0x41, 0x30, 0x48, 0x48, 0x4A, 0x32, 0x38, 0x40, 0x40, 0x22, 0x7A, 0x00, 0x7A, 0x0A, 0x0A, 0x72, 0x7D,
            0x0D, 0x19, 0x31, 0x7D, 0x26, 0x29, 0x29, 0x2F, 0x28, 0x26, 0x29, 0x29, 0x29, 0x26, 0x30, 0x48, 0x4D, 0x40,
            0x20, 0x38, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x38, 0x2F, 0x10, 0xC8, 0xAC, 0xBA, 0x2F, 0x10,
            0x28, 0x34, 0xFA, 0x00, 0x00, 0x7B, 0x00, 0x00, 0x08, 0x14, 0x2A, 0x14, 0x22, 0x22, 0x14, 0x2A, 0x14, 0x08,
            0xAA, 0x00, 0x55, 0x00, 0xAA, 0xAA, 0x55, 0xAA, 0x55, 0xAA, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x10, 0x10, 0x10,
            0xFF, 0x00, 0x14, 0x14, 0x14, 0xFF, 0x00, 0x10, 0x10, 0xFF, 0x00, 0xFF, 0x10, 0x10, 0xF0, 0x10, 0xF0, 0x14,
            0x14, 0x14, 0xFC, 0x00, 0x14, 0x14, 0xF7, 0x00, 0xFF, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x14, 0x14, 0xF4, 0x04,
            0xFC, 0x14, 0x14, 0x17, 0x10, 0x1F, 0x10, 0x10, 0x1F, 0x10, 0x1F, 0x14, 0x14, 0x14, 0x1F, 0x00, 0x10, 0x10,
            0x10, 0xF0, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x10, 0x10, 0x10, 0x10, 0x1F, 0x10, 0x10, 0x10, 0x10, 0xF0, 0x10,
            0x00, 0x00, 0x00, 0xFF, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0xFF, 0x10, 0x00, 0x00, 0x00,
            0xFF, 0x14, 0x00, 0x00, 0xFF, 0x00, 0xFF, 0x00, 0x00, 0x1F, 0x10, 0x17, 0x00, 0x00, 0xFC, 0x04, 0xF4, 0x14,
            0x14, 0x17, 0x10, 0x17, 0x14, 0x14, 0xF4, 0x04, 0xF4, 0x00, 0x00, 0xFF, 0x00, 0xF7, 0x14, 0x14, 0x14, 0x14,
            0x14, 0x14, 0x14, 0xF7, 0x00, 0xF7, 0x14, 0x14, 0x14, 0x17, 0x14, 0x10, 0x10, 0x1F, 0x10, 0x1F, 0x14, 0x14,
            0x14, 0xF4, 0x14, 0x10, 0x10, 0xF0, 0x10, 0xF0, 0x00, 0x00, 0x1F, 0x10, 0x1F, 0x00, 0x00, 0x00, 0x1F, 0x14,
            0x00, 0x00, 0x00, 0xFC, 0x14, 0x00, 0x00, 0xF0, 0x10, 0xF0, 0x10, 0x10, 0xFF, 0x10, 0xFF, 0x14, 0x14, 0x14,
            0xFF, 0x14, 0x10, 0x10, 0x10, 0x1F, 0x00, 0x00, 0x00, 0x00, 0xF0, 0x10, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xF0,
            0xF0, 0xF0, 0xF0, 0xF0, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x0F, 0x0F, 0x0F, 0x0F,
            0x0F, 0x38, 0x44, 0x44, 0x38, 0x44, 0x7C, 0x2A, 0x2A, 0x3E, 0x14, 0x7E, 0x02, 0x02, 0x06, 0x06, 0x02, 0x7E,
            0x02, 0x7E, 0x02, 0x63, 0x55, 0x49, 0x41, 0x63, 0x38, 0x44, 0x44, 0x3C, 0x04, 0x40, 0x7E, 0x20, 0x1E, 0x20,
            0x06, 0x02, 0x7E, 0x02, 0x02, 0x99, 0xA5, 0xE7, 0xA5, 0x99, 0x1C, 0x2A, 0x49, 0x2A, 0x1C, 0x4C, 0x72, 0x01,
            0x72, 0x4C, 0x30, 0x4A, 0x4D, 0x4D, 0x30, 0x30, 0x48, 0x78, 0x48, 0x30, 0xBC, 0x62, 0x5A, 0x46, 0x3D, 0x3E,
            0x49, 0x49, 0x49, 0x00, 0x7E, 0x01, 0x01, 0x01, 0x7E, 0x2A, 0x2A, 0x2A, 0x2A, 0x2A, 0x44, 0x44, 0x5F, 0x44,
            0x44, 0x40, 0x51, 0x4A, 0x44, 0x40, 0x40, 0x44, 0x4A, 0x51, 0x40, 0x00, 0x00, 0xFF, 0x01, 0x03, 0xE0, 0x80,
            0xFF, 0x00, 0x00, 0x08, 0x08, 0x6B, 0x6B, 0x08, 0x36, 0x12, 0x36, 0x24, 0x36, 0x06, 0x0F, 0x09, 0x0F, 0x06,
            0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00, 0x10, 0x10, 0x00, 0x30, 0x40, 0xFF, 0x01, 0x01, 0x00, 0x1F, 0x01,
            0x01, 0x1E, 0x00, 0x19, 0x1D, 0x17, 0x12, 0x00, 0x3C, 0x3C, 0x3C, 0x3C, 0x00, 0x00, 0x00, 0x00, 0x00, };

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }

    // bresenham's algorithm - thx wikpedia
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawLine(int, int, int, int, int)
     */
    @Override
    public void drawLine(int x0, int y0, int x1, int y1, int color) {
        int temp;
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);

        if (steep) {
            // swap(x0, y0);
            temp = x0;
            x0 = y0;
            y0 = temp;

            // swap(x1, y1);
            temp = x1;
            x1 = y1;
            y1 = temp;

        }

        if (x0 > x1) {
            // swap(x0, x1);
            temp = x0;
            x0 = x1;
            x1 = temp;

            // swap(y0, y1);
            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        int dx, dy;
        dx = x1 - x0;
        dy = Math.abs(y1 - y0);

        int err = dx / 2;
        int ystep;

        if (y0 < y1) {
            ystep = 1;
        } else {
            ystep = -1;
        }

        for (; x0 <= x1; x0++) {
            if (steep) {
                drawPixel(y0, x0, color);
            } else {
                drawPixel(x0, y0, color);
            }
            err -= dy;

            if (err < 0) {
                y0 += ystep;
                err += dx;
            }
        }
    }

    public void drawFastVLine(int x, int y, int h, int color) {
        drawLine(x, y, x, y + h - 1, color);
    }

    public void fillRect(int x, int y, int w, int h, int color) {
        for (int i = x; i < x + w; i++) {
            drawFastVLine(i, y, h, color);
        }
    }

    // draw a character
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawChar(int, int, byte, int, int, int)
     */
    @Override
    public void drawChar(int x, int y, byte c, int color, int bg, int size) {

        if ((x >= this._width) || // Clip right
                (y >= this._height) || // Clip bottom
                ((x + 5 * size - 1) < 0) || // Clip left
                ((y + 8 * size - 1) < 0)) // Clip top
        {
            return;
        }

        for (int i = 0; i < 6; i++) {
            short line;
            if (i == 5) {
                line = 0x0;
            } else {
                line = font[(c * 5) + i];
            }
            for (int j = 0; j < 8; j++) {
                if ((line & 0x01) == 0x01) {
                    if (size == 1) // default size
                    {
                        drawPixel(x + i, y + j, color);
                    } else { // big size
                        fillRect(x + (i * size), y + (j * size), size, size, color);
                    }
                } else if (bg != color) {
                    if (size == 1) // default size
                    {
                        drawPixel(x + i, y + j, bg);
                    } else { // big size
                        fillRect(x + i * size, y + j * size, size, size, bg);
                    }
                }

                line >>= 1;
            }
        }
    }

    public int write(char c) {
        if (c == '\n') {
            cursor_y += textsize * 8;
            cursor_x = 0;
        } else if (c == '\r') {
            // skip em
        } else {
            drawChar(cursor_x, cursor_y, (byte) c, textcolor, textbgcolor, textsize);
            cursor_x += textsize * 6;

            if (wrap && (cursor_x > (_width - textsize * 6))) {
                cursor_y += textsize * 8;
                cursor_x = 0;
            }
        }
        return 1;
    }

    // the print function
    public void print(String cad) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(cad.length());
        buffer.put(cad.getBytes());
        buffer.clear();
        for (int i = 0; i < buffer.capacity(); i++) {
            write((char) buffer.get(i));
        }

    }

    // draw a circle outline
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawCircle(int, int, int, int)
     */
    @Override
    public void drawCircle(int x0, int y0, int r, int color) {
        int f = 1 - r;
        int ddF_x = 1;
        int ddF_y = -2 * r;
        int x = 0;
        int y = r;

        drawPixel(x0, y0 + r, color);
        drawPixel(x0, y0 - r, color);
        drawPixel(x0 + r, y0, color);
        drawPixel(x0 - r, y0, color);

        while (x < y) {
            if (f >= 0) {
                y--;
                ddF_y += 2;
                f += ddF_y;
            }

            x++;
            ddF_x += 2;
            f += ddF_x;

            drawPixel(x0 + x, y0 + y, color);
            drawPixel(x0 - x, y0 + y, color);
            drawPixel(x0 + x, y0 - y, color);
            drawPixel(x0 - x, y0 - y, color);
            drawPixel(x0 + y, y0 + x, color);
            drawPixel(x0 - y, y0 + x, color);
            drawPixel(x0 + y, y0 - x, color);
            drawPixel(x0 - y, y0 - x, color);

        }
    }

    public void drawCircleHelper(int x0, int y0, int r, int cornername, int color) {
        int f = 1 - r;
        int ddF_x = 1;
        int ddF_y = -2 * r;
        int x = 0;
        int y = r;

        while (x < y) {
            if (f >= 0) {
                y--;
                ddF_y += 2;
                f += ddF_y;
            }

            x++;
            ddF_x += 2;
            f += ddF_x;
            if ((cornername & 0x4) == 0x4) {
                drawPixel(x0 + x, y0 + y, color);
                drawPixel(x0 + y, y0 + x, color);
            }
            if ((cornername & 0x2) == 0x2) {
                drawPixel(x0 + x, y0 - y, color);
                drawPixel(x0 + y, y0 - x, color);
            }
            if ((cornername & 0x8) == 0x8) {
                drawPixel(x0 - y, y0 + x, color);
                drawPixel(x0 - x, y0 + y, color);
            }
            if ((cornername & 0x1) == 0x1) {
                drawPixel(x0 - y, y0 - x, color);
                drawPixel(x0 - x, y0 - y, color);
            }
        }
    }

    public void fillCircle(int x0, int y0, int r, int color) {
        drawFastVLine(x0, y0 - r, 2 * r + 1, color);
        fillCircleHelper(x0, y0, r, 3, 0, color);
    }

    // used to do circles and roundrects!
    public void fillCircleHelper(int x0, int y0, int r, int cornername, int delta, int color) {
        int f = 1 - r;
        int ddF_x = 1;
        int ddF_y = -2 * r;
        int x = 0;
        int y = r;

        while (x < y) {
            if (f >= 0) {
                y--;
                ddF_y += 2;
                f += ddF_y;
            }

            x++;
            ddF_x += 2;
            f += ddF_x;

            if ((cornername & 0x1) == 0x1) {
                drawFastVLine(x0 + x, y0 - y, 2 * y + 1 + delta, color);
                drawFastVLine(x0 + y, y0 - x, 2 * x + 1 + delta, color);
            }

            if ((cornername & 0x2) == 0x2) {
                drawFastVLine(x0 - x, y0 - y, 2 * y + 1 + delta, color);
                drawFastVLine(x0 - y, y0 - x, 2 * x + 1 + delta, color);
            }
        }
    }

    // draw a rectangle
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawRect(int, int, int, int, int)
     */
    @Override
    public void drawRect(int x, int y, int w, int h, int color) {
        drawFastHLine(x, y, w, color);
        drawFastHLine(x, y + h - 1, w, color);
        drawFastVLine(x, y, h, color);
        drawFastVLine(x + w - 1, y, h, color);
    }

    public void drawFastHLine(int x, int y, int w, int color) {
        drawLine(x, y, x + w - 1, y, color);
    }

    // draw a vertical bargraph and fill it with percent value (0%..100%)
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawVerticalBargraph(int, int, int, int, int, int)
     */
    @Override
    public void drawVerticalBargraph(int x, int y, int w, int h, int color, int percent) {
        int vsize;

        // Create rectangle
        drawRect(x, y, w, h, color);

        // Do not do stupid job
        if (h > 2 && w > 2) {
            // calculate pixel size of bargraph
            vsize = ((h - 2) * percent) / 100;

            // Fill it from bottom (0%) to top (100%)
            fillRect(x + 1, y + 1 + ((h - 2) - vsize), w - 2, vsize, color);
        }
    }

    // draw a horizontal bargraph and fill it with percent value (0%..100%)
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawHorizontalBargraph(int, int, int, int, int, int)
     */
    @Override
    public void drawHorizontalBargraph(int x, int y, int w, int h, int color, int percent) {
        int hsize;

        // Create rectangle
        drawRect(x, y, w, h, color);

        // Do not do stupid job
        if (h > 2 && w > 2) {
            // calculate pixel size of bargraph
            hsize = ((w - 2) * percent) / 100;

            // Fill it from left (0%) to right (100%)
            fillRect(x + 1, y + 1, hsize, h - 2, color);
        }
    }

    public void fillScreen(int color) {
        fillRect(0, 0, _width, _height, color);
    }

    // draw a rounded rectangle!
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawRoundRect(int, int, int, int, int, int)
     */
    @Override
    public void drawRoundRect(int x, int y, int w, int h, int r, int color) {

        // smarter version
        drawFastHLine(x + r, y, w - 2 * r, color); // Top
        drawFastHLine(x + r, y + h - 1, w - 2 * r, color); // Bottom
        drawFastVLine(x, y + r, h - 2 * r, color); // Left
        drawFastVLine(x + w - 1, y + r, h - 2 * r, color); // Right

        // draw four corners
        drawCircleHelper(x + r, y + r, r, 1, color);
        drawCircleHelper(x + w - r - 1, y + r, r, 2, color);
        drawCircleHelper(x + w - r - 1, y + h - r - 1, r, 4, color);
        drawCircleHelper(x + r, y + h - r - 1, r, 8, color);
    }

    // fill a rounded rectangle!
    public void fillRoundRect(int x, int y, int w, int h, int r, int color) {
        // smarter version
        fillRect(x + r, y, w - 2 * r, h, color);

        // draw four corners
        fillCircleHelper(x + w - r - 1, y + r, r, 1, h - 2 * r - 1, color);
        fillCircleHelper(x + r, y + r, r, 2, h - 2 * r - 1, color);
    }

    // draw a triangle!
    /*
     * (non-Javadoc)
     *
     * @see org.m1theo.gfxlibtf.IGfxLib2#drawTriangle(int, int, int, int, int, int, int)
     */
    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        drawLine(x0, y0, x1, y1, color);
        drawLine(x1, y1, x2, y2, color);
        drawLine(x2, y2, x0, y0, color);
    }

    // fill a triangle!
    public void fillTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {

        int a, b, y, last, temp;

        // Sort coordinates by Y order (y2 >= y1 >= y0)
        if (y0 > y1) {
            // swap(y0, y1);
            temp = y0;
            y0 = y1;
            y1 = temp;

            // swap(x0, x1);
            temp = x0;
            x0 = x1;
            x1 = temp;
        }
        if (y1 > y2) {
            // swap(y2, y1);
            temp = y2;
            y2 = y1;
            y1 = temp;

            // swap(x2, x1);
            temp = x2;
            x2 = x1;
            x1 = temp;
        }
        if (y0 > y1) {
            // swap(y0, y1);
            temp = y0;
            y0 = y1;
            y1 = temp;
            // swap(x0, x1);
            temp = x0;
            x0 = x1;
            x1 = temp;
        }

        if (y0 == y2) { // Handle awkward all-on-same-line case as its own thing
            a = b = x0;
            if (x1 < a) {
                a = x1;
            } else if (x1 > b) {
                b = x1;
            }
            if (x2 < a) {
                a = x2;
            } else if (x2 > b) {
                b = x2;
            }
            drawFastHLine(a, y0, b - a + 1, color);
            return;
        }

        int dx01 = x1 - x0, dy01 = y1 - y0, dx02 = x2 - x0, dy02 = y2 - y0, dx12 = x2 - x1, dy12 = y2 - y1, sa = 0,
                sb = 0;

        // For upper part of triangle, find scanline crossings for segments
        // 0-1 and 0-2. If y1=y2 (flat-bottomed triangle), the scanline y1
        // is included here (and second loop will be skipped, avoiding a /0
        // error there), otherwise scanline y1 is skipped here and handled
        // in the second loop...which also avoids a /0 error here if y0=y1
        // (flat-topped triangle).
        if (y1 == y2) {
            last = y1; // Include y1 scanline
        } else {
            last = y1 - 1; // Skip it
        }
        for (y = y0; y <= last; y++) {
            a = x0 + sa / dy01;
            b = x0 + sb / dy02;
            sa += dx01;
            sb += dx02;
            /*
             * longhand:
             * a = x0 + (x1 - x0) * (y - y0) / (y1 - y0);
             * b = x0 + (x2 - x0) * (y - y0) / (y2 - y0);
             */
            if (a > b) {
                // swap(a, b);
                temp = a;
                a = b;
                b = temp;
            }

            drawFastHLine(a, y, b - a + 1, color);
        }

        // For lower part of triangle, find scanline crossings for segments
        // 0-2 and 1-2. This loop is skipped if y1=y2.
        sa = dx12 * (y - y1);
        sb = dx02 * (y - y0);
        for (; y <= y2; y++) {
            a = x1 + sa / dy12;
            b = x0 + sb / dy02;
            sa += dx12;
            sb += dx02;
            /*
             * longhand:
             * a = x1 + (x2 - x1) * (y - y1) / (y2 - y1);
             * b = x0 + (x2 - x0) * (y - y0) / (y2 - y0);
             */
            if (a > b) {

                // swap(a, b);
                temp = a;
                a = b;
                b = temp;
            }

            drawFastHLine(a, y, b - a + 1, color);
        }
    }

    public void setCursor(int x, int y) {
        cursor_x = x;
        cursor_y = y;
    }

    public void setTextSize(int s) {
        textsize = (s > 0) ? s : 1;
    }

    public void setTextColor(int c) {
        textcolor = c;
        textbgcolor = c;
        // for 'transparent' background, we'll set the bg
        // to the same as fg instead of using a flag
    }

    public void setTextColor(int c, int b) {
        textcolor = c;
        textbgcolor = b;
    }

    public void setTextWrap(boolean w) {
        wrap = w;
    }

}
