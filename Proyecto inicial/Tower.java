import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Torre de tazas y tapas (Stacking Cups). Gestiona Cup y Lid; usa Canvas de shapes.
 * @author stackingitems
 */
public class Tower {
    private static final int CANVAS_W = 300;
    private static final int CANVAS_H = 300;
    private static final int MARGIN = 25;

    private final int width;
    private final int maxHeight;
    private ArrayList<Elemento> items;
    private int nextCupNumber;
    private boolean lastOk;
    private boolean visible;
    private int scale;
    private ArrayList<Rectangle> markRects;

    /**
     * Crea una torre con ancho y altura máxima en cm.
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.items = new ArrayList<Elemento>();
        this.nextCupNumber = 1;
        this.lastOk = true;
        this.visible = false;
        this.markRects = new ArrayList<Rectangle>();
        this.scale = Math.min((CANVAS_W - 2 * MARGIN) / width,
                              (CANVAS_H - 2 * MARGIN) / Math.max(1, maxHeight));
        if (this.scale < 2) this.scale = 2;
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
        Elemento top = items.get(items.size() - 1);
        if (!"cup".equals(top.getType())) {
            setOk(false, "La cima no es una taza.");
            return;
        }
        top.makeInvisible();
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
            Elemento it = items.get(k);
            if ("cup".equals(it.getType()) && it.getNumber() == i) {
                it.makeInvisible();
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
        Elemento top = items.get(items.size() - 1);
        if (!"lid".equals(top.getType())) {
            setOk(false, "La cima no es una tapa.");
            return;
        }
        top.makeInvisible();
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
            Elemento it = items.get(k);
            if ("lid".equals(it.getType()) && it.getNumber() == i) {
                it.makeInvisible();
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
        for (Elemento s : items) s.makeInvisible();
        ArrayList<Elemento> ordered = new ArrayList<Elemento>();
        ArrayList<Integer> cupNums = new ArrayList<Integer>();
        for (Elemento it : items) {
            if ("cup".equals(it.getType())) cupNums.add(it.getNumber());
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
     * @return altura actual
     */
    public int height() {
        return currentHeightCm();
    }

    /**
     * Números de tazas tapadas por su tapa (ordenados de menor a mayor).
     * @return arreglo con números de tazas tapadas
     */
    public int[] lidedCups() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < items.size() - 1; i++) {
            Elemento curr = items.get(i);
            Elemento next = items.get(i + 1);
            if ("cup".equals(curr.getType()) && "lid".equals(next.getType())
                && curr.getNumber() == next.getNumber()) {
                list.add(curr.getNumber());
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
            r[i][0] = items.get(i).getType();
            r[i][1] = String.valueOf(items.get(i).getNumber());
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
        for (Elemento s : items) s.makeInvisible();
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
     * @return true si fue exitosa, false en caso contrario
     */
    public boolean ok() {
        return lastOk;
    }

    // --- Métodos privados auxiliares (no se ven desde fuera) ---

    private int currentHeightCm() {
        int h = 0;
        for (Elemento it : items) h += it.getHeightCm();
        return h;
    }

    private boolean hasCupNumber(int n) {
        for (Elemento it : items)
            if ("cup".equals(it.getType()) && it.getNumber() == n) return true;
        return false;
    }

    private boolean hasLidNumber(int n) {
        for (Elemento it : items)
            if ("lid".equals(it.getType()) && it.getNumber() == n) return true;
        return false;
    }

    private boolean hasLidInItems(int n) {
        for (Elemento it : items)
            if ("lid".equals(it.getType()) && it.getNumber() == n) return true;
        return false;
    }

    private void setOk(boolean ok, String message) {
        lastOk = ok;
        if (visible && !ok) JOptionPane.showMessageDialog(null, message);
    }

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

    private void eraseMarks() {
        for (Rectangle r : markRects) r.makeInvisible();
        markRects.clear();
    }

    private static final int INSET_PER_LEVEL = 3;

    private void redraw() {
        if (!visible) return;
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_W - totalWidthPx) / 2;
        int baseY = CANVAS_H - MARGIN;
        for (int i = 0; i < items.size(); i++) {
            Elemento it = items.get(i);
            int heightCm = it.getHeightCm();
            int hPx = heightCm * scale;
            int widthCm = Math.max(2, width - INSET_PER_LEVEL * i);
            int wPx = widthCm * scale;
            int x = baseX + (totalWidthPx - wPx) / 2;
            int baseLevelCm = i;
            int yTopPx = baseY - (baseLevelCm + heightCm) * scale;
            it.makeVisibleAt(x, yTopPx, wPx, hPx);
        }
    }
}