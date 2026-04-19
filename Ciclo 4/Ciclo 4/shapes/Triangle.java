package shapes;

import java.awt.Polygon;

/**
 * Triangulo que puede manipularse y dibujarse en el canvas.
 * Hereda posicion, color y movimientos de la clase Shape.
 *
 * @author Michael Kolling and David J. Barnes
 * @author Angel-Garcia
 * @version 4.0 (March 2026)
 */
public class Triangle extends Shape {
    public static final int VERTICES = 3;
    private int height;
    private int width;

    /**
     * Crea un triangulo con dimensiones y posicion por defecto.
     */
    public Triangle() {
        super(140, DEFAULT_Y, "green");
        this.height = 30;
        this.width  = 40;
    }

    /**
     * Cambia el tamaño del triangulo.
     *
     * @param newHeight nueva altura en pixeles.
     * @param newWidth nuevo ancho en pixeles.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width  = newWidth;
        draw();
    }

    /**
     * Dibuja el triangulo en el canvas.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = {
                xPosition,
                xPosition + (width / 2),
                xPosition - (width / 2)
            };
            int[] ypoints = {
                yPosition,
                yPosition + height,
                yPosition + height
            };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }
}