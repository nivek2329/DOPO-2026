/**
 * Representa una tapa para la torre apilable.
 * Se dibuja como un rectángulo sólido y tiene altura fija de 1 cm.
 *
 * @author Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class Lid implements TowerItem {

    private static final String[] COLORS =
        {"red", "blue", "green", "yellow", "magenta", "black"};
    private static final int RECT_DEFAULT_X = 70;
    private static final int RECT_DEFAULT_Y = 15;

    private final int number;
    private Rectangle rect;

    /**
     * Crea una tapa identificada por un número.
     *
     * @param number número asignado a la tapa; determina su color.
     */
    public Lid(int number) {
        this.number = number;
        this.rect   = new Rectangle();
    }

    /**
     * Devuelve la altura fija de la tapa en centímetros.
     *
     * @return 
     */
    @Override
    public int getHeightCm() {
        return 1;
    }

    /**
     * Devuelve el número identificador de la tapa.
     *
     * @return número de la tapa.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Devuelve el tipo del elemento.
     *
     * @return la cadena { "lid"}.
     */
    @Override
    public String getType() {
        return "lid";
    }

    /**
     * Indica que este elemento no es una taza.
     *
     * @return { false} siempre.
     */
    @Override
    public boolean isCup() {
        return false;
    }

    /**
     * Devuelve el color asociado a la tapa según su número.
     * Usa { Math.floorMod} para evitar índices negativos.
     *
     * @return nombre del color en minúsculas.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Dibuja la tapa en el canvas en la posición y tamaño indicados.
     * Oculta el rectángulo anterior antes de crear uno nuevo.
     *
     * @param x coordenada X izquierda en píxeles.
     * @param y coordenada Y superior en píxeles.
     * @param w ancho en píxeles.
     * @param h alto en píxeles.
     */
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

    /**
     * Oculta la tapa del canvas.
     */
    @Override
    public void makeInvisible() {
        rect.makeInvisible();
    }
}