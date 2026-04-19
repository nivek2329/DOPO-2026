package tower;

/**
 * Clase abstracta que representa una taza en la torre.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Cup implements TowerItem {
    protected static final String[] COLORS = {"red","blue","green","yellow","magenta","black"};
    protected static final int RECT_DEFAULT_X = 70;
    protected static final int RECT_DEFAULT_Y = 15;
    protected final int number;

    public Cup(int number) {
        this.number = number;
        }

    @Override public int getHeightCm() { 
        return 2 * number - 1; 
        }
    @Override public int getNumber() { 
        return number; 
        }
    @Override public boolean isCup() { 
        return true; 
        }
    @Override public String getType() { 
        return "cup"; 
        }

    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
        }

    @Override public abstract void makeVisibleAt(int x, int y, int w, int h);
    @Override public abstract void makeInvisible();
}