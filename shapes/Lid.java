import java.awt.*;

/**
 * A lid (tapa) that can be placed on top of a cup in the stacking tower.
 * Each lid has an associated number and a fixed height of 1 cm.
 * This class uses a single {@link Rectangle} shape to draw the lid on the
 * BlueJ Shapes canvas. The lid is always drawn as a solid-colored rectangle,
 * where its color depends on its number, cycling through a predefined set 
 * of colors.
 * @authors Angel-Garcia  
 * @version 1.0 (February 2026)
 */
public class Lid {
    private static final String[] COLORS =
        {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;
    private int number;
    private Rectangle rect;

    /**
     * Create a new lid with the given number.
     * The lid is initially invisible until {@code makeVisibleAt} is called.
     *
     * @param number the numerical identifier of this lid
     */
    public Lid(int number) {
        this.number = number;
        this.rect = new Rectangle();
    }
    
    /**
     * Returns the height of this lid in centimeters.
     * Lids always have a height of 1 cm.
     *
     * @return 1 — the fixed height of a lid
     */
    public int getHeightCm() {
        return 1;
    }

    /**
     * Returns the number assigned to this lid.
     *
     * @return the lid number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the type of this element as a string.
     * Used by the {@code Tower} class to identify lids.
     *
     * @return the string "lid"
     */
    public String getType() {
        return "lid";
    }

    /**
     * Returns the display color of this lid based on its number.
     * The color repeats cyclically through the predefined list.
     *
     * @return a string representing the color name
     */
    public String getColor() {
        return COLORS[(number - 1) % COLORS.length];
    }

    /**
     * Makes the lid visible on the canvas at a specific position and size.
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        rect.changeSize(h, w);
        rect.changeColor(getColor());
        rect.moveHorizontal(-RECT_DEFAULT_X + x);
        rect.moveVertical(-RECT_DEFAULT_Y + y);
        rect.makeVisible();
    }

    /**
     * Makes the lid invisible on the canvas by hiding its underlying shape.
     */
    public void makeInvisible() {
        rect.makeInvisible();
    }
}