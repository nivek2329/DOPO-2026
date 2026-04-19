package tower;

import shapes.Rectangle;

/**
 * Taza hierarchical.
 * Al entrar desplaza todos los elementos de menor tamanio hacia arriba.
 * Si llega al fondo de la torre no puede ser removida.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class HierarchicalCup extends Cup {

    /** Rectangulo que representa la base de la taza. */
    private Rectangle base;

    /** Rectangulo que representa el brazo izquierdo de la taza. */
    private Rectangle leftArm;

    /** Rectangulo que representa el brazo derecho de la taza. */
    private Rectangle rightArm;

    /** Indica si la taza llego al fondo y no puede removerse. */
    private boolean reachedBottom;

    /**
     * Crea una taza hierarchical con el numero indicado.
     *
     * @param number numero identificador de la taza.
     */
    public HierarchicalCup(int number) {
        super(number);
        this.base          = new Rectangle();
        this.leftArm       = new Rectangle();
        this.rightArm      = new Rectangle();
        this.reachedBottom = false;
    }

    /**
     * Devuelve el tipo de taza.
     *
     * @return cadena "hierarchical".
     */
    @Override
    public String getType() {
        return "hierarchical";
    }

    /**
     * Marca que esta taza llego al fondo de la torre.
     */
    public void setReachedBottom() {
        this.reachedBottom = true;
    }

    /**
     * Indica si esta taza llego al fondo y no puede ser removida.
     *
     * @return true si la taza esta bloqueada, false en caso contrario.
     */
    public boolean isLocked() {
        return reachedBottom;
    }

    /**
     * Hace visible la taza en la posicion indicada del canvas.
     *
     * @param x posicion horizontal en pixeles.
     * @param y posicion vertical en pixeles.
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

        int heightCm     = getHeightCm();
        int oneCmPx      = heightCm > 0 ? Math.max(1, h / heightCm) : 1;
        int baseHeightPx = Math.max(1, oneCmPx);
        int baseY        = y + h - baseHeightPx;

        base.changeSize(baseHeightPx, w);
        base.changeColor(getColor());
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + baseY);
        base.makeVisible();

        int armHeight  = Math.max(0, h - baseHeightPx);
        int armWidthPx = Math.max(1, baseHeightPx);
        if (armHeight > 0) {
            leftArm.changeSize(armHeight, armWidthPx);
            leftArm.changeColor(getColor());
            leftArm.moveHorizontal(-RECT_DEFAULT_X + x);
            leftArm.moveVertical(-RECT_DEFAULT_Y + y);
            leftArm.makeVisible();

            rightArm.changeSize(armHeight, armWidthPx);
            rightArm.changeColor(getColor());
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