package tower;

import shapes.Rectangle;

public class HierarchicalCup extends Cup {
    private Rectangle base;
    private Rectangle leftArm;
    private Rectangle rightArm;
    private boolean reachedBottom;

    public HierarchicalCup(int number) {
        super(number);
        this.base          = new Rectangle();
        this.leftArm       = new Rectangle();
        this.rightArm      = new Rectangle();
        this.reachedBottom = false;
    }

    @Override
    public String getType() { 
        return "hierarchical"; 
    }

    public void setReachedBottom() { 
        this.reachedBottom = true; 
    
        }
    public boolean isLocked()      { 
        return reachedBottom; 
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