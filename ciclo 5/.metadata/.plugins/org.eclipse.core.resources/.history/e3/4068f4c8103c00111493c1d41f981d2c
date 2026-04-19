package tower;

import shapes.Rectangle;
import java.util.ArrayList;

public class FearfulLid extends Lid {
    private Rectangle rect;

    public FearfulLid(int number) {
        super(number);
        this.rect = new Rectangle();
    }

    @Override
    public String getType() { 
        return "fearful"; 
        }

    public boolean companionIsPresent(ArrayList<TowerItem> items) {
        for (TowerItem item : items)
            if (item.isCup() && item.getNumber() == this.number) return true;
        return false;
    }

    public boolean isCoveringCup(ArrayList<TowerItem> items) {
        for (int i = 1; i < items.size(); i++) {
            TowerItem current  = items.get(i);
            TowerItem previous = items.get(i - 1);
            if (!current.isCup() && current.getNumber() == this.number
                    && previous.isCup() && previous.getNumber() == this.number)
                return true;
        }
        return false;
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