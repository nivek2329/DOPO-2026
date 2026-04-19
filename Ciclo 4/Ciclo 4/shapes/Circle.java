package shapes;

import java.awt.geom.Ellipse2D;

/**
 * Circulo que puede manipularse y dibujarse en el canvas.
 * Hereda posicion, color y movimientos de la clase Shape.
 *
 * @author Michael Kolling and David J. Barnes
 * @author Angel-Garcia
 * @version 4.0
 */
public class Circle extends Shape {
    public static final double PI = 3.1416;
    private int diameter;

    /**
     * Crea un circulo con diametro y posicion por defecto.
     */
    public Circle() {
        super(20, DEFAULT_Y, "blue");
        this.diameter = 30;
    }

    /**
     * Cambia el diametro del circulo.
     *
     * @param newDiameter nuevo diametro en pixeles.
     */
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * Dibuja el circulo en el canvas.
     */
    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(
                this,
                color,
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter)
            );
            canvas.wait(10);
        }
    }
}