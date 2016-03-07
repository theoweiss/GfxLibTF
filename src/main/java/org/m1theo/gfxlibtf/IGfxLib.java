package org.m1theo.gfxlibtf;

public interface IGfxLib {

    // the most basic function, set a single pixel
    void drawPixel(int x, int y, int color);

    void clear();

    void flush();

    public int write(char c);

    public void print(String cad);

    // bresenham's algorithm - thx wikpedia
    void drawLine(int x0, int y0, int x1, int y1, int color);

    // draw a character
    void drawChar(int x, int y, byte c, int color, int bg, int size);

    // draw a circle outline
    void drawCircle(int x0, int y0, int r, int color);

    // draw a rectangle
    void drawRect(int x, int y, int w, int h, int color);

    // draw a vertical bargraph and fill it with percent value (0%..100%)
    void drawVerticalBargraph(int x, int y, int w, int h, int color, int percent);

    // draw a horizontal bargraph and fill it with percent value (0%..100%)
    void drawHorizontalBargraph(int x, int y, int w, int h, int color, int percent);

    // draw a rounded rectangle!
    void drawRoundRect(int x, int y, int w, int h, int r, int color);

    // draw a triangle!
    void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color);

}