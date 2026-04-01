package tower;

/**
 * Taza hierarchical.
 * Al entrar desplaza todos los objetos de menor tamanio hacia arriba.
 * Si llega al fondo de la torre, no puede ser removida.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class HierarchicalCup extends Cup {

    private boolean reachedBottom;

    /**
     * Crea una taza hierarchical con el numero indicado.
     *
     * @param number numero de la taza.
     */
    public HierarchicalCup(int number) {
        super(number);
        this.reachedBottom = false;
    }

    /**
     * Devuelve el tipo de taza.
     *
     * @return tipo de taza como cadena.
     */
    @Override
    public String getType() {
        return "hierarchical";
    }

    /**
     * Marca que esta taza llego al fondo de la torre.
     */
    public void setReachedBottom() {
        this.reachedBottom = true;
    }

    /**
     * Indica si esta taza llego al fondo y no puede ser removida.
     *
     * @return true si la taza esta bloqueada, false en caso contrario.
     */
    public boolean isLocked() {
        return reachedBottom;
    }
}