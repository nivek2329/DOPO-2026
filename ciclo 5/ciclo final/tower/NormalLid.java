package tower;

import shapes.Rectangle;

/**
 * Tapa normal. Implementa el dibujo como un rectangulo simple.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class NormalLid extends Lid {
    private Rectangle rect;

    public NormalLid(int number) {
        super(number);
        this.rect = new Rectangle();
    }

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

    @Override
    public void makeInvisible() {
        rect.makeInvisible();
    }
}
