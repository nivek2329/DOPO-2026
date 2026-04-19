package shapes;

/**
 * Rectangulo que puede manipularse y dibujarse en el canvas.
 * Hereda posicion, color y movimientos de la clase Shape.
 *
 * @author Michael Kolling and David J. Barnes
 * @author Angel-Garcia
 * @version 4.0
 */
public class Rectangle extends Shape {
    public static final int EDGES = 4;
    private int height;
    private int width;

    /**
     * Crea un rectangulo con dimensiones y posicion por defecto.
     */
    public Rectangle() {
        super(DEFAULT_X, DEFAULT_Y, "magenta");
        this.height = 30;
        this.width  = 40;
    }

    /**
     * Cambia el tamaño del rectangulo.
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
     * Dibuja el rectangulo en el canvas.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }
}