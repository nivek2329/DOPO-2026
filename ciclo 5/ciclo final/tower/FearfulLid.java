package tower;

import shapes.Rectangle;
import java.util.ArrayList;

/**
 * Tapa fearful.
 * No entra a la torre si su taza companiera no esta presente.
 * No puede ser removida si esta cubriendo directamente a su taza.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class FearfulLid extends Lid {

    /** Rectangulo que representa visualmente la tapa. */
    private Rectangle rect;

    /**
     * Crea una tapa fearful con el numero indicado.
     *
     * @param number numero identificador de la tapa.
     */
    public FearfulLid(int number) {
        super(number);
        this.rect = new Rectangle();
    }

    /**
     * Devuelve el tipo de tapa.
     *
     * @return cadena "fearful".
     */
    @Override
    public String getType() {
        return "fearful";
    }

    /**
     * Verifica si la taza companiera de esta tapa esta presente en la torre.
     *
     * @param items lista actual de elementos en la torre.
     * @return true si la taza companiera existe, false en caso contrario.
     */
    public boolean companionIsPresent(ArrayList<TowerItem> items) {
        for (TowerItem item : items) {
            if (item.isCup() && item.getNumber() == this.number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si esta tapa esta cubriendo directamente a su taza.
     * Ocurre cuando la taza esta inmediatamente debajo de la tapa.
     *
     * @param items lista actual de elementos en la torre.
     * @return true si la tapa esta cubriendo a su taza, false en caso contrario.
     */
    public boolean isCoveringCup(ArrayList<TowerItem> items) {
        for (int i = 1; i < items.size(); i++) {
            TowerItem current  = items.get(i);
            TowerItem previous = items.get(i - 1);
            if (!current.isCup()
                    && current.getNumber() == this.number
                    && previous.isCup()
                    && previous.getNumber() == this.number) {
                return true;
            }
        }
        return false;
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