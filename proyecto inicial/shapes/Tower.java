import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Torre de tazas y tapas (Stacking Cups). Gestiona Cup y Lid; usa Canvas de shapes.
 * Dibujo en modo "anidado": centros alineados y +1 cm por nivel (base).
 */
public class Tower {

    private static final int CANVAS_W = 300;
    private static final int CANVAS_H = 300;
    private static final int MARGIN   = 25;
    private final int width;
    private final int maxHeight;
    private ArrayList<Object> items;
    private int  nextCupNumber;
    private boolean lastOk;
    private boolean visible;
    private int  scale;                 
    private ArrayList<Rectangle> markRects;
    private static final int INSET_PER_LEVEL = 3; 
    private final boolean nestedMode = true;

    /**
     * Crea una torre con ancho (cm) y altura máxima (cm).
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;

        this.items = new ArrayList<Object>();
        this.nextCupNumber = 1;
        this.lastOk = true;
        this.visible = false;
        this.markRects = new ArrayList<Rectangle>();
        this.scale = Math.min(
            (CANVAS_W - 2 * MARGIN) / Math.max(1, width),
            (CANVAS_H - 2 * MARGIN) / Math.max(1, maxHeight)
        );
        if (this.scale < 2) this.scale = 2; // legibilidad mínima
    }

    /**
     * Añade una taza con el número especificado.
     * @param i número de la taza a añadir
     */
    public void pushCup(int i) {
        if (hasCupNumber(i)) {
            setOk(false, "Ya existe una taza con ese número.");
            return;
        }
        int h = currentHeightCm() + (2 * i - 1);
        if (h > maxHeight) {
            setOk(false, "La torre excedería la altura máxima.");
            return;
        }
        items.add(new Cup(i));
        if (i >= nextCupNumber) nextCupNumber = i + 1;
        lastOk = true;
        redraw();
    }

    /**
     * Quita la taza de la cima (si la cima es una taza).
     */
    public void popCup() {
        if (items.isEmpty()) {
            setOk(false, "La torre está vacía.");
            return;
        }
        Object top = items.get(items.size() - 1);
        if (!(top instanceof Cup)) {
            setOk(false, "La cima no es una taza.");
            return;
        }
        ((Cup) top).makeInvisible();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la taza con número i.
     * @param i número de la taza a eliminar
     */
    public void removeCup(int i) {
        if (!hasCupNumber(i)) {
            setOk(false, "No existe taza con número " + i + ".");
            return;
        }
        for (int k = items.size() - 1; k >= 0; k--) {
            Object it = items.get(k);
            if (it instanceof Cup && ((Cup) it).getNumber() == i) {
                ((Cup) it).makeInvisible();
                items.remove(k);
                lastOk = true;
                redraw();
                return;
            }
        }
    }

    /**
     * Añade la tapa del número i en la cima.
     * @param i número de la tapa a añadir
     */
    public void pushLid(int i) {
        if (hasLidNumber(i)) {
            setOk(false, "Ya existe la tapa " + i + ".");
            return;
        }
        int h = currentHeightCm() + 1;
        if (h > maxHeight) {
            setOk(false, "La torre excedería la altura máxima.");
            return;
        }
        items.add(new Lid(i));
        lastOk = true;
        redraw();
    }

    /**
     * Quita la tapa de la cima.
     */
    public void popLid() {
        if (items.isEmpty()) {
            setOk(false, "La torre está vacía.");
            return;
        }
        Object top = items.get(items.size() - 1);
        if (!(top instanceof Lid)) {
            setOk(false, "La cima no es una tapa.");
            return;
        }
        ((Lid) top).makeInvisible();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la tapa con número i.
     * @param i número de la tapa a eliminar
     */
    public void removeLid(int i) {
        if (!hasLidNumber(i)) {
            setOk(false, "No existe tapa con número " + i + ".");
            return;
        }
        for (int k = items.size() - 1; k >= 0; k--) {
            Object it = items.get(k);
            if (it instanceof Lid && ((Lid) it).getNumber() == i) {
                ((Lid) it).makeInvisible();
                items.remove(k);
                lastOk = true;
                redraw();
                return;
            }
        }
    }


    /**
     * Ordena de mayor a menor (número menor en la cima); tapa sobre su taza.
     */
    public void orderTower() {
        for (Object s : items) {
            if (s instanceof Cup) ((Cup) s).makeInvisible();
            else if (s instanceof Lid) ((Lid) s).makeInvisible();
        }

        ArrayList<Object> ordered = new ArrayList<Object>();
        ArrayList<Integer> cupNums = new ArrayList<Integer>();

        for (Object it : items) {
            if (it instanceof Cup) cupNums.add(((Cup) it).getNumber());
        }

        Collections.sort(cupNums, Collections.reverseOrder());
        for (Integer n : cupNums) {
            ordered.add(new Cup(n));
            if (hasLidInItems(n)) ordered.add(new Lid(n));
        }

        items = ordered;
        lastOk = true;
        redraw();
    }

    /**
     * Invierte el orden de los elementos (base y cima intercambiados).
     */
    public void reverseTower() {
        Collections.reverse(items);
        lastOk = true;
        redraw();
    }

    /**
     * Altura total apilada en cm.
     */
    public int height() {
        return currentHeightCm();
    }

    /**
     * Números de tazas tapadas por su tapa (ordenados de menor a mayor).
     */
    public int[] lidedCups() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < items.size() - 1; i++) {
            Object curr = items.get(i);
            Object next = items.get(i + 1);
            if (curr instanceof Cup && next instanceof Lid
                && ((Cup) curr).getNumber() == ((Lid) next).getNumber()) {
                list.add(((Cup) curr).getNumber());
            }
        }
        Collections.sort(list);
        int[] r = new int[list.size()];
        for (int j = 0; j < list.size(); j++) r[j] = list.get(j);
        return r;
    }

    /**
     * Elementos apilados de base a cima.
     * @return arreglo con {tipo, número} de cada elemento
     */
    public String[][] stackingItems() {
        String[][] r = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            if (obj instanceof Cup) {
                r[i][0] = "cup";
                r[i][1] = String.valueOf(((Cup)obj).getNumber());
            } else {
                r[i][0] = "lid";
                r[i][1] = String.valueOf(((Lid)obj).getNumber());
            }
        }
        return r;
    }


    /**
     * Hace visible el simulador.
     */
    public void makeVisible() {
        int needW = width * scale + 2 * MARGIN;
        int needH = maxHeight * scale + 2 * MARGIN;
        if (needW > CANVAS_W || needH > CANVAS_H) {
            lastOk = false;
            return;
        }
        visible = true;
        Canvas c = Canvas.getCanvas();
        c.setVisible(true);
        drawMarks();
        redraw();
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        for (Object s : items) {
            if (s instanceof Cup) ((Cup) s).makeInvisible();
            else if (s instanceof Lid) ((Lid) s).makeInvisible();
        }
        eraseMarks();
        Canvas c = Canvas.getCanvas();
        c.setVisible(false);
    }

    /**
     * Termina el simulador.
     */
    public void exit() {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Indica si la última operación fue exitosa.
     */
    public boolean ok() {
        return lastOk;
    }

    /**
     * Calcula la altura total actual de la torre en centímetros.
     * Recorre la lista de elementos; si el objeto es una {@code Cup}, suma
     * su altura ({@code 2*n-1}); si es una {@code Lid}, suma {@code 1}.
     *
     * @return altura acumulada de todos los elementos
     */
    private int currentHeightCm() {
        int h = 0;
        for (Object it : items) {
            if (it instanceof Cup) h += ((Cup) it).getHeightCm();
            else h += 1; 
        }
        return h;
    }
    
    /**
     * Indica si ya existe una taza con el número especificado.
     *
     * @param n número de taza a buscar
     * @return {@code true} si hay una {@code Cup} con ese número; {@code false} en caso contrario
     */
    private boolean hasCupNumber(int n) {
        for (Object it : items)
            if (it instanceof Cup && ((Cup) it).getNumber() == n) return true;
        return false;
    }
    
    /**
     * Indica si ya existe una tapa con el número especificado.
     *
     * @param n número de tapa a buscar
     * @return {@code true} si hay una {@code Lid} con ese número; {@code false} en caso contrario
     */
    private boolean hasLidNumber(int n) {
        for (Object it : items)
            if (it instanceof Lid && ((Lid) it).getNumber() == n) return true;
        return false;
    }
    
    /**
     * Indica si entre los elementos actuales hay una tapa con el número dado.
     *
     */
    private boolean hasLidInItems(int n) {
        for (Object it : items)
            if (it instanceof Lid && ((Lid) it).getNumber() == n) return true;
        return false;
    }
    
    /**
     * Registra el resultado de la última operación y, si la torre es visible y hubo error,
     * muestra un mensaje emergente.
     */
    private void setOk(boolean ok, String message) {
        lastOk = ok;
        if (visible && !ok) JOptionPane.showMessageDialog(null, message);
    }
    
    /**
     * Dibuja la regla lateral con marcas de centímetro a la izquierda del lienzo.
     * Limpia marcas anteriores, crea nuevos rectángulos (cortos y estrechos) para cada cm
     * desde 0 hasta {@code maxHeight}, los posiciona en el eje Y según la escala
     * y los hace visibles. Guarda referencias para poder ocultarlos después.
     */
    private void drawMarks() {
        if (!visible) return;
        eraseMarks();
        int baseY = CANVAS_H - MARGIN; 
        int markX = 8;                 
    
        for (int cm = 0; cm <= maxHeight; cm++) {
            Rectangle mark = new Rectangle();
            mark.changeSize(4, 2);                     
            mark.changeColor("black");
            mark.moveHorizontal(-70 + markX);
            mark.moveVertical(-15 + (baseY - cm * scale));
            mark.makeVisible();
            markRects.add(mark);
        }
    }
    
    /**
     * Oculta todas las marcas de la regla lateral y limpia la lista interna.
     */
    private void eraseMarks() {
        for (Rectangle r : markRects) r.makeInvisible();
        markRects.clear();
    }
    
    /**
     * Redibuja todos los elementos de la torre en el lienzo.
     * Calcula la zona disponible y, para cada nivel de la pila (de base a cima),
     * determina el ancho visual (con un "inset" por nivel) y la posición horizontal
     * centrada. La posición vertical depende del modo:
     */
    private void redraw() {
        if (!visible) return;
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_W - totalWidthPx) / 2;
        int baseY = CANVAS_H - MARGIN;
        int accumulated = 0;
    
        for (int i = 0; i < items.size(); i++) {
            Object it = items.get(i);

            int heightCm = (it instanceof Cup)
                ? ((Cup) it).getHeightCm()
                : ((Lid) it).getHeightCm();
    
            int hPx = heightCm * scale;
            int widthCm = Math.max(2, width - INSET_PER_LEVEL * i);
            int wPx = widthCm * scale;
            int x = baseX + (totalWidthPx - wPx) / 2;

            final int yTopPx;
            if (nestedMode) {
                int depthCm = i;
                yTopPx = baseY - (depthCm + heightCm) * scale;
            } else {
                yTopPx = baseY - (accumulated + heightCm) * scale;
            }

            if (it instanceof Cup) {
                ((Cup) it).makeVisibleAt(x, yTopPx, wPx, hPx);
            } else {
                ((Lid) it).makeVisibleAt(x, yTopPx, wPx, hPx);
            }
            accumulated += heightCm;
        }
    }
}