package tower;

/**
 * Taza fragil.
 * Si se intenta colocar encima una taza de mayor tamaño,
 * la taza fragil se elimina automaticamente de la torre.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class FragileCup extends Cup {
    /**
     * Crea una taza fragil con el numero indicado.
     *
     * @param number numero de la taza.
     */
    public FragileCup(int number) {
        super(number);
    }

    /**
     * Devuelve el tipo de taza.
     *
     * @return tipo de taza como cadena.
     */
    @Override
    public String getType() {
        return "fragile";
    }

    /**
     * Indica si esta taza se rompe al colocar encima una taza de mayor tamanio.
     *
     * @param newCupNumber numero de la taza que se quiere apilar encima.
     * @return true si la taza fragil debe eliminarse, false en caso contrario.
     */
    public boolean breaksUnder(int newCupNumber) {
        return newCupNumber > this.number;
    }
}