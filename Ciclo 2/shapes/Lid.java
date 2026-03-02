/**
 * A lid (tapa) that can be placed on top of a cup in the stacking tower.
 * Each lid has an associated number and a fixed height of 1 cm.
 * Drawn as a solid rectangle on the BlueJ canvas.
 * @version 1.0 (February 2026)
 * @Author: Angel-Garcia
 */
public class Lid {
    private static final String[] COLORS =
        {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    private final int number;
    private Rectangle rect;

    /**
     * Creates a lid identified by a number.
     *
     * @param number number assigned to the lid; determines its color.
     * @return nothing.
     */
    public Lid(int number) {
        this.number = number;
        this.rect = new Rectangle();
    }

    /**
     * Returns the fixed height of the lid in centimeters.
     *
     * @return height of the lid (always 1 cm).
     */
    public int getHeightCm() { return 1; }

    /**
     * Returns the identifying number of the lid.
     *
     * @return number of the lid.
     */
    public int getNumber()   { return number; }

    /**
     * Returns the type associated with this figure.
     *
     * @return the string literal "lid".
     */
    public String getType()  { return "lid"; }

    /**
     * Computes the color associated with this lid.
     * Uses {@code Math.floorMod(number - 1, COLORS.length)} for robust indexing.
     *
     * @return name of the color in lowercase.
     */
    public String getColor() {
        int idx = Math.floorMod(number - 1, COLORS.length);
        return COLORS[idx];
    }

    /**
     * Makes the lid visible at the specified absolute position and size.
     * Hides the existing rectangle before re‑creating it to avoid duplicates.
     *
     * @param x leftmost X coordinate in pixels.
     * @param y top Y coordinate in pixels.
     * @param w width of the lid in pixels.
     * @param h height of the lid in pixels.
     * @return nothing.
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        if (rect != null) {
            rect.makeInvisible();
        }
        this.rect = new Rectangle();
        rect.changeSize(h, w);
        rect.changeColor(getColor());
        rect.moveHorizontal(-RECT_DEFAULT_X + x);
        rect.moveVertical(-RECT_DEFAULT_Y + y);
        rect.makeVisible();
    }

    /**
     * Hides the lid if it is currently visible.
     *
     * @return nothing.
     */
    public void makeInvisible() {
        rect.makeInvisible();
    }
}