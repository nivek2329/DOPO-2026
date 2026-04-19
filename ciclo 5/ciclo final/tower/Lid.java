package tower;

/**
 * Clase abstracta que representa una tapa en la torre.
 * Define el contrato minimo comun a todos los tipos de tapa.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Lid implements TowerItem {

    /** Colores disponibles para las tapas. */
    protected static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};

    /** Desplazamiento horizontal por defecto para el dibujo. */
    protected static final int RECT_DEFAULT_X = 70;

    /** Desplazamiento vertical por defecto para el dibujo. */
    protected static final int RECT_DEFAULT_Y = 15;

    /** Numero identificador de la tapa. */
    protected final int number;

    /**
     * Crea una tapa con el numero indicado.
     *
     * @param number numero identificador de la tapa.
     */
    public Lid(int number) {
        this.number = number;
    }

    /**
     * Devuelve la altura de la tapa en centimetros.
     *
     * @return siempre 1.
     */
    @Override
    public int getHeightCm() {
        return 1;
    }

    /**
     * Devuelve el numero identificador de la tapa.
     *
     * @return numero de la tapa.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Devuelve el tipo generico del elemento.
     *
     * @return cadena "lid".
     */
    @Override
    public String getType() {
        return "lid";
    }

    /**
     * Indica que este elemento no es una taza.
     *
     * @return false siempre.
     */
    @Override
    public boolean isCup() {
        return false;
    }

    /**
     * Devuelve el color asociado a la tapa segun su numero.
     *
     * @return color de la tapa.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Hace visible la tapa en la posicion indicada del canvas.
     *
     * @param x posicion horizontal en pixeles.
     * @param y posicion vertical en pixeles.
     * @param w ancho en pixeles.
     * @param h altura en pixeles.
     */
    @Override
    public abstract void makeVisibleAt(int x, int y, int w, int h);

    /**
     * Oculta visualmente la tapa del canvas.
     */
    @Override
    public abstract void makeInvisible();
}