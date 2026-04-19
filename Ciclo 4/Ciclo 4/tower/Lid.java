package tower;

import shapes.Rectangle;

/**
 * Clase abstracta que representa una tapa en la torre.
 * Las subclases definen el comportamiento especifico de cada tipo.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Lid implements TowerItem {

    protected static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};
    protected static final int RECT_DEFAULT_X = 70;
    protected static final int RECT_DEFAULT_Y = 15;

    protected final int number;
    protected Rectangle rect;

    /**
     * Crea una tapa con el numero indicado.
     *
     * @param number numero de la tapa.
     */
    public Lid(int number) {
        this.number = number;
        this.rect   = new Rectangle();
    }

    /**
     * Devuelve la altura de la tapa en centimetros.
     *
     * @return altura de la tapa.
     */
    @Override
    public int getHeightCm() {
        return 1;
    }

    /**
     * Devuelve el numero identificador de la tapa.
     *
     * @return numero de la tapa.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Devuelve el tipo generico del elemento.
     *
     * @return tipo "lid".
     */
    @Override
    public String getType() {
        return "lid";
    }

    /**
     * Indica que este elemento no es una taza.
     *
     * @return false.
     */
    @Override
    public boolean isCup() {
        return false;
    }

    /**
     * Devuelve el color asociado a la tapa.
     *
     * @return color de la tapa.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Hace visible la tapa en la posicion indicada del canvas.
     *
     * @param x posicion horizontal.
     * @param y posicion vertical.
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