package tower;

import shapes.Rectangle;

/**
 * Tapa crazy.
 * En lugar de ubicarse en la cima, se coloca en la base
 * de la torre al ser insertada.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class CrazyLid extends Lid {

    /** Rectangulo que representa visualmente la tapa. */
    private Rectangle rect;

    /**
     * Crea una tapa crazy con el numero indicado.
     *
     * @param number numero identificador de la tapa.
     */
    public CrazyLid(int number) {
        super(number);
        this.rect = new Rectangle();
    }

    /**
     * Devuelve el tipo de tapa.
     *
     * @return cadena "crazy".
     */
    @Override
    public String getType() {
        return "crazy";
    }

    /**
     * Hace visible la tapa en la posicion indicada del canvas.
     *
     * @param x posicion horizontal en pixeles.
     * @param y posicion vertical en pixeles.
     * @param w ancho en pixeles.
     * @param h altura en pixeles.
     */
    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
        rect.makeInvisible();
        rect = new Rectangle();
        rect.changeSize(h, w);
        rect.changeColor(getColor());
        rect.moveHorizontal(-RECT_DEFAULT_X + x);
        rect.moveVertical(-RECT_DEFAULT_Y + y);
        rect.makeVisible();
    }

    /**
     * Oculta visualmente la tapa del canvas.
     */
    @Override
    public void makeInvisible() {
        rect.makeInvisible();
    }
}