package dev.maxbogomol.paw_paint;

public class Main {

    static void main(String[] args) {
        PawPaint.instance = new PawPaint();
        PawPaint.instance.runApplication(args);
    }
}
