package tower;

/**
 * Clase abstracta que representa una taza en la torre.
 * Define el contrato minimo comun a todos los tipos de taza.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public abstract class Cup implements TowerItem {

    /** Colores disponibles para las tazas. */
    protected static final String[] COLORS = {"red", "blue", "green", "yellow", "magenta", "black"};

    /** Desplazamiento horizontal por defecto para el dibujo. */
    protected static final int RECT_DEFAULT_X = 70;

    /** Desplazamiento vertical por defecto para el dibujo. */
    protected static final int RECT_DEFAULT_Y = 15;

    /** Numero identificador de la taza. */
    protected final int number;

    /**
     * Crea una taza con el numero indicado.
     *
     * @param number numero identificador de la taza.
     */
    public Cup(int number) {
        this.number = number;
    }

    /**
     * Devuelve la altura de la taza en centimetros.
     *
     * @return altura calculada como 2 * number - 1.
     */
    @Override
    public int getHeightCm() {
        return 2 * number - 1;
    }

    /**
     * Devuelve el numero identificador de la taza.
     *
     * @return numero de la taza.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Indica que este elemento es una taza.
     *
     * @return true siempre.
     */
    @Override
    public boolean isCup() {
        return true;
    }

    /**
     * Devuelve el tipo generico del elemento.
     *
     * @return cadena "cup".
     */
    @Override
    public String getType() {
        return "cup";
    }

    /**
     * Devuelve el color asociado a la taza segun su numero.
     *
     * @return color de la taza.
     */
    public String getColor() {
        return COLORS[Math.floorMod(number - 1, COLORS.length)];
    }

    /**
     * Hace visible la taza en la posicion indicada del canvas.
     *
     * @param x posicion horizontal en pixeles.
     * @param y posicion vertical en pixeles.
     * @param w ancho en pixeles.
     * @param h altura en pixeles.
     */
    @Override
    public abstract void makeVisibleAt(int x, int y, int w, int h);

    /**
     * Oculta visualmente la taza del canvas.
     */
    @Override
    public abstract void makeInvisible();
}