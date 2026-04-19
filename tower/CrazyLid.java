package tower;

import shapes.Rectangle;

public class CrazyLid extends Lid {
    private Rectangle rect;

    public CrazyLid(int number) {
        super(number);
        this.rect = new Rectangle();
    }

    @Override
    public String getType() { 
        return "crazy"; 
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