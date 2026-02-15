/**
 * Clase base para elementos apilables en la torre.
 * @author stackingitems
 */
public class Elemento {
    protected int number;
    
    public Elemento() {
        this.number = 0;
    }
    
    public int getHeightCm() {
        return 0;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getType() {
        return "elemento";
    }
    
    public String getColor() {
        return "black";
    }
    
    public void makeVisibleAt(int x, int y, int w, int h) {
        // Método vacío - será sobrescrito
    }
    
    public void makeInvisible() {
        // Método vacío - será sobrescrito
    }
}
