package tower;

import java.util.ArrayList;
/**
 * Taza opener.
 * Al entrar a la torre elimina las tapas que le bloquean el paso.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class OpenerCup extends Cup {
    /**
     * Crea una taza opener con el numero indicado.
     *
     * @param number numero de la taza.
     */
    public OpenerCup(int number) {
        super(number);
    }

    /**
     * Devuelve el tipo de taza.
     *
     * @return tipo de taza como cadena.
     */
    @Override
    public String getType() {
        return "opener";
    }

    /**
     * Elimina de la lista las tapas que bloquean el paso a esta taza.
     *
     * @param items lista actual de elementos en la torre.
     */
    public void removeBlockingLids(ArrayList<TowerItem> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            TowerItem it = items.get(i);
            if (it.isCup()) {
                break;
            }
            if (!it.isCup() && it.getNumber() < this.number) {
                it.makeInvisible();
                items.remove(i);
            }
        }
    }
}