/**
 * A cup (taza) that forms part of the stacking tower simulation.
 * This class follows the structure and style of the Shapes example provided in BlueJ,
 * encapsulating drawing, visibility control, size adjustment, and positioning internally.
 *
 * @author  
 * @version 1.0 (February 2026)
 */
public class Cup {
    private static final String[] COLORS =
        {"red", "blue", "green", "yellow", "magenta", "black"};

    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;
    private int number;
    private Rectangle base;
    private Rectangle leftArm;
    private Rectangle rightArm;

    /**
     * Create a new cup with the given number.
     *
     * @param number the numerical identifier of this cup
     */
    public Cup(int number) {
        this.number = number;
        this.base = new Rectangle();
        this.leftArm = new Rectangle();
        this.rightArm = new Rectangle();
    }

    /**
     * Returns the height of this cup in centimeters.
     * In this simulation, the height is defined as {@code 2*number - 1}.
     *
     * @return height of this cup in centimeters
     */
    public int getHeightCm() {
        return 2 * number - 1;
    }

    /**
     * Returns the identifying number of this cup.
     *
     * @return cup number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the type of this element.
     *
     * @return the string "cup"
     */
    public String getType() {
        return "cup";
    }

    /**
     * Computes the drawing color associated with this cup.
     *
     * @return a string representing the drawing color
     */
    public String getColor() {
        return COLORS[(number - 1) % COLORS.length];
    }

    /**
     * Makes the cup visible at a given position and size within the canvas.
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        String c = getColor();
        int heightCm = getHeightCm();

        int oneCmPx = (heightCm > 0) ? h / heightCm : 1;
        if (oneCmPx <= 0) oneCmPx = 1;

        int baseHeightPx = Math.max(1, oneCmPx);
        int armHeight = Math.max(0, h - baseHeightPx);
        int baseY = y + h - baseHeightPx;

        base.changeSize(baseHeightPx, w);
        base.changeColor(c);
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + baseY);
        base.makeVisible();

        int armWidthPx = Math.max(1, baseHeightPx);
        if (armHeight > 0) {
            leftArm.changeSize(armHeight, armWidthPx);
            leftArm.changeColor(c);
            leftArm.moveHorizontal(-RECT_DEFAULT_X + x);
            leftArm.moveVertical(-RECT_DEFAULT_Y + y);
            leftArm.makeVisible();

            rightArm.changeSize(armHeight, armWidthPx);
            rightArm.changeColor(c);
            rightArm.moveHorizontal(-RECT_DEFAULT_X + (x + w - armWidthPx));
            rightArm.moveVertical(-RECT_DEFAULT_Y + y);
            rightArm.makeVisible();
        } else {
            leftArm.makeInvisible();
            rightArm.makeInvisible();
        }
    }

    /**
     * Makes the cup invisible by hiding all its internal rectangles.
     */
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}