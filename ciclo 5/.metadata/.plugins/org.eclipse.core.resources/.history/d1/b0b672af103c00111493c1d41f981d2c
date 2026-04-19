package tower;

import shapes.Rectangle;
import java.util.ArrayList;

public class OpenerCup extends Cup {
    private Rectangle base;
    private Rectangle leftArm;
    private Rectangle rightArm;

    public OpenerCup(int number) {
        super(number);
        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();
    }

    @Override
    public String getType() { 
        return "opener"; 
    }

    public void removeBlockingLids(ArrayList<TowerItem> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            TowerItem it = items.get(i);
            if (it.isCup()) break;
            if (!it.isCup() && it.getNumber() < this.number) {
                it.makeInvisible();
                items.remove(i);
            }
        }
    }

    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
        base.makeInvisible(); leftArm.makeInvisible(); rightArm.makeInvisible();
        base = new Rectangle(); leftArm = new Rectangle(); rightArm = new Rectangle();
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

    @Override
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}