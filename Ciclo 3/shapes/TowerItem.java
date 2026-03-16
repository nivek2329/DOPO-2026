/**
 * Contrato común para todos los elementos que se pueden apilar en la torre.
 * Tanto Cup como Lid implementan esta interfaz.
 *
 * @Author: Angel-Garcia
 * @version 3.0 (March 2026)
 */
public interface TowerItem {

    /** Número identificador del elemento. */
    int getNumber();

    /** Altura del elemento en centímetros. */
    int getHeightCm();

    /** Tipo del elemento: "cup" o "lid". */
    String getType();

    /** Indica si el elemento es una taza. */
    boolean isCup();

    /** Oculta el elemento del canvas. */
    void makeInvisible();

    /**
     * Dibuja el elemento en el canvas en la posición y tamaño indicados.
     *
     * @param x coordenada X izquierda en píxeles.
     * @param y coordenada Y superior en píxeles.
     * @param w ancho en píxeles.
     * @param h alto en píxeles.
     */
    void makeVisibleAt(int x, int y, int w, int h);
}