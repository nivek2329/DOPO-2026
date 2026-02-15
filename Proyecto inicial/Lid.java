/**
 * Tapa con número. Altura en cm: 1. Mismo color que la taza del mismo número.
 * Usa Rectangle de shapes para dibujar.
 * @author stackingitems
 */
public class Lid extends Elemento {
    private static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    private Rectangle rect;

    public Lid(int number) {
        this.number = number;
        this.rect = new Rectangle();
    }

    @Override
    public int getHeightCm() {
        return 1;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getType() {
        return "lid";
    }

    @Override
    public String getColor() {
        return COLORS[(number - 1) % COLORS.length];
    }

    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
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