package tower;

import java.util.ArrayList;

/**
 * Tapa fearful.
 * No entra a la torre si su taza companiera no esta presente.
 * Si ya esta tapando a su taza, no puede ser removida.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class FearfulLid extends Lid {

    /**
     * Crea una tapa fearful con el numero indicado.
     *
     * @param number numero de la tapa.
     */
    public FearfulLid(int number) {
        super(number);
    }

    /**
     * Devuelve el tipo de tapa.
     *
     * @return tipo de tapa como cadena.
     */
    @Override
    public String getType() {
        return "fearful";
    }

    /**
     * Verifica si la taza companiera de esta tapa esta presente en la torre.
     *
     * @param items lista actual de elementos en la torre.
     * @return true si la taza companiera existe, false en caso contrario.
     */
    public boolean companionIsPresent(ArrayList<TowerItem> items) {
        for (TowerItem item : items) {
            if (item.isCup() && item.getNumber() == this.number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si esta tapa esta cubriendo directamente a su taza.
     * Esto ocurre cuando la taza esta inmediatamente debajo de la tapa.
     *
     * @param items lista actual de elementos en la torre.
     * @return true si la tapa esta cubriendo a su taza.
     */
    public boolean isCoveringCup(ArrayList<TowerItem> items) {
        for (int i = 1; i < items.size(); i++) {
            TowerItem current  = items.get(i);
            TowerItem previous = items.get(i - 1);
            if (!current.isCup()
                    && current.getNumber() == this.number
                    && previous.isCup()
                    && previous.getNumber() == this.number) {
                return true;
            }
        }
        return false;
    }
}