package shapes;

/**
 * Superclase abstracta para todas las figuras geométricas del paquete shapes.
 * Centraliza el estado comun y el comportamiento basico de movimiento y visibilidad.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Shape {
    protected static final int DEFAULT_X = 70;
    protected static final int DEFAULT_Y = 15;
    protected int     xPosition;
    protected int     yPosition;
    protected String  color;
    protected boolean isVisible;

    /**
     * Inicializa la figura en la posicion y color indicados, invisible.
     *
     * @param x posicion X inicial en pixeles.
     * @param y posicion Y inicial en pixeles.
     * @param color color inicial de la figura.
     */
    protected Shape(int x, int y, String color) {
        this.xPosition = x;
        this.yPosition = y;
        this.color     = color;
        this.isVisible = false;
    }

    /**
     * Hace visible la figura en el canvas.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la figura en el canvas.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Mueve la figura hacia la derecha.
     */
    public void moveRight() {
        moveHorizontal(20);
    }

    /**
     * Mueve la figura hacia la izquierda.
     */
    public void moveLeft() {
        moveHorizontal(-20);
    }

    /**
     * Mueve la figura hacia arriba.
     */
    public void moveUp() {
        moveVertical(-20);
    }

    /**
     * Mueve la figura hacia abajo.
     */
    public void moveDown() {
        moveVertical(20);
    }

    /**
     * Mueve la figura horizontalmente una distancia determinada.
     *
     * @param distance distancia horizontal en pixeles.
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve la figura verticalmente una distancia determinada.
     *
     * @param distance distancia vertical en pixeles.
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve lentamente la figura de forma horizontal.
     *
     * @param distance distancia horizontal en pixeles.
     */
    public void slowMoveHorizontal(int distance) {
        int delta;
        if (distance < 0) {
            delta = -1;
        } else {
            delta = 1;
        }

        distance = Math.abs(distance);

        for (int i = 0; i < distance; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve lentamente la figura de forma vertical.
     *
     * @param distance distancia vertical en pixeles.
     */
    public void slowMoveVertical(int distance) {
        int delta;
        if (distance < 0) {
            delta = -1;
        } else {
            delta = 1;
        }

        distance = Math.abs(distance);

        for (int i = 0; i < distance; i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el color de la figura.
     *
     * @param newColor nuevo color de la figura.
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /**
     * Dibuja la figura en el canvas.
     * Cada subclase implementa su forma concreta.
     */
    protected abstract void draw();

    /**
     * Borra la figura del canvas si es visible.
     */
    protected void erase() {
        if (isVisible) {
            Canvas.getCanvas().erase(this);
        }
    }
}