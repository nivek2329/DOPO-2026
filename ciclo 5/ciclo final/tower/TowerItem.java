package tower;

/**
 * Contrato comun para todos los elementos apilables en la torre.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public interface TowerItem {
    /**
     * Devuelve el numero identificador del elemento.
     *
     * @return numero del elemento.
     */
    int getNumber();

    /**
     * Devuelve la altura del elemento en centimetros.
     *
     * @return altura del elemento.
     */
    int getHeightCm();

    /**
     * Devuelve el tipo del elemento.
     *
     * @return tipo del elemento como cadena.
     */
    String getType();

    /**
     * Indica si el elemento es una taza.
     *
     * @return true si es una taza, false si es una tapa.
     */
    boolean isCup();

    /**
     * Oculta visualmente el elemento del canvas.
     */
    void makeInvisible();

    /**
     * Hace visible el elemento en la posicion indicada del canvas.
     *
     * @param x posicion horizontal.
     * @param y posicion vertical.
     * @param w ancho en pixeles.
     * @param h altura en pixeles.
     */
    void makeVisibleAt(int x, int y, int w, int h);
}