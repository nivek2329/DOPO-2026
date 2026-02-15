/**
 * Taza con número. Altura en cm: 2*number-1. Se dibuja como forma "U"
 * (base + dos brazos) usando 3 Rectangle de shapes.
 * @author stackingitems
 */
public class Cup extends Elemento {
    private static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    private Rectangle base;
    private Rectangle leftArm;
    private Rectangle rightArm;

    public Cup(int number) {
        this.number = number;
        this.base = new Rectangle();
        this.leftArm = new Rectangle();
        this.rightArm = new Rectangle();
    }

    @Override
    public int getHeightCm() {
        return 2 * number - 1;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getType() {
        return "cup";
    }

    @Override
    public String getColor() {
        return COLORS[(number - 1) % COLORS.length];
    }

    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
        String c = getColor();
        int heightCm = getHeightCm();
       int oneCmPx = 1;
        if (heightCm > 0) {
            oneCmPx = h / heightCm;
        }
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

    @Override
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}