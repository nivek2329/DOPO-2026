package tower;

import shapes.Rectangle;
import java.util.ArrayList;

/**
 * Taza opener.
 * Al entrar a la torre elimina las tapas que le bloquean el paso
 * hasta encontrar la primera taza desde la cima.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class OpenerCup extends Cup {

    /** Rectangulo que representa la base de la taza. */
    private Rectangle base;

    /** Rectangulo que representa el brazo izquierdo de la taza. */
    private Rectangle leftArm;

    /** Rectangulo que representa el brazo derecho de la taza. */
    private Rectangle rightArm;

    /**
     * Crea una taza opener con el numero indicado.
     *
     * @param number numero identificador de la taza.
     */
    public OpenerCup(int number) {
        super(number);
        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();
    }

    /**
     * Devuelve el tipo de taza.
     *
     * @return cadena "opener".
     */
    @Override
    public String getType() {
        return "opener";
    }

    /**
     * Elimina de la lista las tapas que bloquean el paso a esta taza.
     * Solo elimina tapas desde la cima hasta encontrar la primera taza.
     *
     * @param items lista actual de elementos en la torre.
     */
    public void removeBlockingLids(ArrayList<TowerItem> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            TowerItem it = items.get(i);
            if (it.isCup()) {
                break;
            }
            if (!it.isCup() && it.getNumber() < this.number) {
                it.makeInvisible();
                items.remove(i);
            }
        }
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