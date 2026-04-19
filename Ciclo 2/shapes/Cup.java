/**
 * Representa una taza para una simulación de torre apilable.
 * Se dibuja como una "U" usando tres rectángulos (base y dos brazos).
 * La altura en centímetros es 2*number - 1.
 * @version 1.0 (February 2026)
 * @Author: Angel-Garcia
 */
public class Cup {
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
     * @param number número de la taza; determina color y altura lógica (en cm).
     */
    public Cup(int number) {
        this.number = number;
        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();
    }

    /**
     * Calcula la altura lógica de la taza en centímetros.
     * Fórmula: 2 * number - 1.
     *
     * @return altura en centímetros.
     */
    public int getHeightCm() { 
        return 2 * number - 1; 
    }

    /**
     * Devuelve el número identificador de la taza.
     *
     * @return número de la taza.
     */
    public int getNumber()   { 
        return number; 
    }

    /**
     * Devuelve el tipo de figura.
     *
     * @return la cadena literal "cup".
     */
    public String getType()  { 
        return "cup"; 
    }

    /**
     * Obtiene el color asociado a la taza de forma robusta.
     * Usa {@code Math.floorMod(number - 1, COLORS.length)} para evitar índices negativos.
     *
     * @return nombre del color en minúsculas (por ejemplo, "red", "blue").
     */
    public String getColor() {
        int idx = Math.floorMod(number - 1, COLORS.length);
        return COLORS[idx];
    }

    /**
     * Dibuja la taza en el lienzo en la posición y tamaño indicados.
     * Siempre oculta y re-crea los rectángulos antes de mostrarlos
     * para evitar duplicados y derivas de posición.
     *
     * @param x coordenada X del extremo izquierdo (en píxeles) donde iniciar el dibujo.
     * @param y coordenada Y del borde superior (en píxeles) donde iniciar el dibujo.
     * @param w ancho total de la taza (en píxeles).
     * @param h alto total de la taza (en píxeles).
     * @return no retorna valor.
     */
    public void makeVisibleAt(int x, int y, int w, int h) {
        if (base != null)     base.makeInvisible();
        if (leftArm != null)  leftArm.makeInvisible();
        if (rightArm != null) rightArm.makeInvisible();

        this.base     = new Rectangle();
        this.leftArm  = new Rectangle();
        this.rightArm = new Rectangle();

        String c = getColor();

        int heightCm = getHeightCm();
        int oneCmPx  = (heightCm > 0) ? Math.max(1, h / heightCm) : 1;

        int baseHeightPx = Math.max(1, oneCmPx);
        int baseY        = y + h - baseHeightPx;

        base.changeSize(baseHeightPx, w);
        base.changeColor(c);
        base.moveHorizontal(-RECT_DEFAULT_X + x);
        base.moveVertical(-RECT_DEFAULT_Y + baseY);
        base.makeVisible();

        int armHeight  = Math.max(0, h - baseHeightPx);
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
     * Oculta todos los rectángulos que componen la taza.
     *
     * @return no retorna valor.
     */
    public void makeInvisible() {
        base.makeInvisible();
        leftArm.makeInvisible();
        rightArm.makeInvisible();
    }
}