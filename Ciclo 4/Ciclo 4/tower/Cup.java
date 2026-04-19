package tower;

import shapes.Rectangle;

/**
 * Clase abstracta que representa una taza en la torre.
 * Las subclases definen el comportamiento especifico de cada tipo.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Cup implements TowerItem {

    protected static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};
    protected static final int RECT_DEFAULT_X = 70;
    protected static final int RECT_DEFAULT_Y = 15;
    protected final int number;
    protected Rectangle base;
    protected Rectangle leftArm;
    protected Rectangle rightArm;

    /**
     * Crea una taza con el numero indicado.
     *
     * @param number numero de la taza.
     */
    public Cup(int number) {
        this.number   = number;
        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();
    }

    /**
     * Devuelve la altura de la taza en centimetros.
     *
     * @return altura de la taza.
     */
    @Override
    public int getHeightCm() {
        return 2 * number - 1;
    }

    /**
     * Devuelve el numero identificador de la taza.
     *
     * @return numero de la taza.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Indica que este elemento es una taza.
     *
     * @return true.
     */
    @Override
    public boolean isCup() {
        return true;
    }

    /**
     * Devuelve el tipo generico del elemento.
     *
     * @return tipo "cup".
     */
    @Override
    public String getType() {
        return "cup";
    }

    /**
     * Devuelve el color asociado a la taza.
     *
     * @return color de la taza.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Hace visible la taza en la posicion indicada del canvas.
     *
     * @param x posicion horizontal.
     * @param y posicion vertical.
     * @param w ancho en pixeles.
     * @param h altura en pixeles.
     */
    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();

        base     = new Rectangle();
        leftArm  = new Rectangle();
        rightArm = new Rectangle();

        String color = getColor();
        int heightCm = getHeightCm();

        int oneCmPx;
        if (heightCm > 0) {
            oneCmPx = Math.max(1, h / heightCm);
        } else {
            oneCmPx = 1;
        }

        int baseHeightPx = Math.max(1, oneCmPx);
        int baseY        = y + h - baseHeightPx;

        base.changeSize(baseHeightPx, w);
        base.changeColor(color);
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + baseY);
        base.makeVisible();

        int armHeight  = Math.max(0, h - baseHeightPx);
        int armWidthPx = Math.max(1, baseHeightPx);

        if (armHeight > 0) {
            leftArm.changeSize(armHeight, armWidthPx);
            leftArm.changeColor(color);
            leftArm.moveHorizontal(-RECT_DEFAULT_X + x);
            leftArm.moveVertical(-RECT_DEFAULT_Y + y);
            leftArm.makeVisible();

            rightArm.changeSize(armHeight, armWidthPx);
            rightArm.changeColor(color);
            rightArm.moveHorizontal(-RECT_DEFAULT_X + (x + w - armWidthPx));
            rightArm.moveVertical(-RECT_DEFAULT_Y + y);
            rightArm.makeVisible();
        }
    }

    /**
     * Oculta visualmente la taza del canvas.
     */
    @Override
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}