package tower;

/**
 * Tapa crazy.
 * En lugar de tapar a su taza, se ubica en la base de la torre al ser insertada.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class CrazyLid extends Lid {
    /**
     * Crea una tapa crazy con el numero indicado.
     *
     * @param number numero de la tapa.
     */
    public CrazyLid(int number) {
        super(number);
    }

    /**
     * Devuelve el tipo de tapa.
     *
     * @return tipo de tapa como cadena.
     */
    @Override
    public String getType() {
        return "crazy";
    }
}