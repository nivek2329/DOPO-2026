/**
 * Representa una taza cilíndrica para la torre apilable.
 * Se dibuja como una "U" usando tres rectángulos: base y dos brazos laterales.
 * La altura lógica en centímetros es { 2 * number - 1}.
 *
 * @author Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class Cup implements TowerItem {

    private static final String[] COLORS =
        {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    private final int number;
    private Rectangle base;
    private Rectangle leftArm;
    private Rectangle rightArm;

    /**
     * Crea una taza identificada por un número.
     *
     * @param number número de la taza; determina su color y altura lógica.
     */
    public Cup(int number) {
        this.number   = number;
        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();
    }

    /**
     * Devuelve la altura lógica de la taza en centímetros.
     * La fórmula es { 2 * number - 1}.
     *
     * @return altura en centímetros.
     */
    @Override
    public int getHeightCm() {
        return 2 * number - 1;
    }

    /**
     * Devuelve el número identificador de la taza.
     *
     * @return número de la taza.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Devuelve el tipo del elemento.
     *
     * @return la cadena {"cup"}.
     */
    @Override
    public String getType() {
        return "cup";
    }

    /**
     * Indica que este elemento es una taza.
     *
     * @return { true} siempre.
     */
    @Override
    public boolean isCup() {
        return true;
    }

    /**
     * Devuelve el color asociado a la taza según su número.
     * Usa {Math.floorMod} para evitar índices negativos.
     *
     * @return nombre del color en minúsculas.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Dibuja la taza en el canvas en la posición y tamaño indicados.
     * Oculta y recrea los rectángulos antes de mostrarlos para evitar duplicados.
     *
     * @param x coordenada X del extremo izquierdo en píxeles.
     * @param y coordenada Y del borde superior en píxeles.
     * @param w ancho total de la taza en píxeles.
     * @param h alto total de la taza en píxeles.
     */
    @Override
    public void makeVisibleAt(int x, int y, int w, int h) {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();

        base     = new Rectangle();
        leftArm  = new Rectangle();
        rightArm = new Rectangle();

        String color     = getColor();
        int heightCm     = getHeightCm();
        int oneCmPx      = (heightCm > 0) ? Math.max(1, h / heightCm) : 1;
        int baseHeightPx = Math.max(1, oneCmPx);
        int baseY        = y + h - baseHeightPx;

        base.changeSize(baseHeightPx, w);
        base.changeColor(color);
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + baseY);
        base.makeVisible();

        int armHeight  = Math.max(0, h - baseHeightPx);
        int armWidthPx = Math.max(1, baseHeightPx);

        if (armHeight > 0) {
            leftArm.changeSize(armHeight, armWidthPx);
            leftArm.changeColor(color);
            leftArm.moveHorizontal(-RECT_DEFAULT_X + x);
            leftArm.moveVertical(-RECT_DEFAULT_Y + y);
            leftArm.makeVisible();

            rightArm.changeSize(armHeight, armWidthPx);
            rightArm.changeColor(color);
            rightArm.moveHorizontal(-RECT_DEFAULT_X + (x + w - armWidthPx));
            rightArm.moveVertical(-RECT_DEFAULT_Y + y);
            rightArm.makeVisible();
        }
    }

    /**
     * Oculta todos los rectángulos que componen la taza.
     */
    @Override
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}