import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Gestión completa de una torre de tazas y tapas: permite agregar, quitar,
 * ordenar, invertir, cubrir y dibujar los elementos en un canvas. Mantiene
 * coherencia lógica (bloques taza‑contenido‑tapa), calcula alturas reales,
 * ajusta la escala para el dibujo, valida operaciones y ofrece consultas
 * sobre el estado actual de la torre.
 * @Author: Angel-Garcia
 */

public class Tower {
    private static final int CANVAS_W = 300;
    private static final int CANVAS_H = 300;
    private static final int MARGIN = 25;
    private static final int INSET_PER_LEVEL = 3;
    private final int width;
    private final int maxHeight;
    private int scale;
    private ArrayList<Object> items = new ArrayList<>();
    private int nextCupNumber = 1;
    private boolean lastOk  = true;
    private boolean visible = false;
    private ArrayList<Rectangle> markRects = new ArrayList<>();
 
    
    /**
     * Crea una torre con ancho base y altura máxima determinados.
     *
     * @param width ancho base en centímetros.
     * @param maxHeight altura máxima en centímetros.
     */

    public Tower(int width, int maxHeight) {
        this.width     = width;
        this.maxHeight = maxHeight;
        this.scale     = 4;
    }
 
    /**
     * Crea una torre con tazas numeradas de 1 a {@code cups} (sin tapas iniciales).
     * Las tazas tienen alturas 1, 3, 5, … (2*i - 1 cm). La altura total es cups^2.
     * {@code maxHeight} se fija en cups^2 + margen, y {@code width} en 2*cups + 2.
     *
     * @param cups número de tazas a crear.
     */

    public Tower(int cups) {
        this(cups * 2 + 2, cups * cups + cups);
        for (int i = 1; i <= cups; i++) {
            pushCup(i);
        }
    }
 

    /**
     * Agrega una taza con número {@code i} en la cima de la torre.
     * Falla si ya existe una taza con ese número o si excede la altura máxima.
     *
     * @param i número de la taza a agregar.
     * @return nada.
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
        if (i >= nextCupNumber) {
            nextCupNumber = i + 1;
        }
        lastOk = true;
        redraw();
    }
 
    
    /**
     * Quita la taza superior si la cima es una taza.
     *
     * @return nada.
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
     * Elimina la taza con número {@code i} (si existe) en cualquier posición.
     *
     * @param i número de la taza a eliminar.
     * @return nada.
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
     * Agrega una tapa con número {@code i} en la cima de la torre.
     * Falla si ya existe una tapa con ese número o si excede la altura máxima.
     *
     * @param i número de la tapa a agregar.
     * @return nada.
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
     * Quita la tapa superior si la cima es una tapa.
     *
     * @return nada.
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
     * Elimina la tapa con número {@code i} (si existe) en cualquier posición.
     *
     * @param i número de la tapa a eliminar.
     * @return nada.
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
     * Ordena la torre:
     *  - Tazas: descendente (grande→pequeña) para anidar.
     *  - Tapas con taza existente: ascendente (pequeña→grande), cierran de dentro→fuera.
     *  - Tapas huérfanas: al final, en orden descendente.
     *
     * @return nada.
     */

    public void orderTower() {
        makeAllInvisible();
 
        HashSet<Integer> cupNums = new HashSet<>();
        HashSet<Integer> lidNums = new HashSet<>();
        for (Object it : items) {
            if (it instanceof Cup) {
                cupNums.add(((Cup) it).getNumber());
            } else {
                lidNums.add(((Lid) it).getNumber());
            }
        }
 
        ArrayList<Integer> cupsDesc = new ArrayList<>(cupNums);
        Collections.sort(cupsDesc, Collections.reverseOrder());
 
        ArrayList<Integer> lidsAsc = new ArrayList<>(lidNums);
        Collections.sort(lidsAsc);
 
        ArrayList<Integer> orphanLidsDesc = new ArrayList<>();
        for (Integer n : lidNums) {
            if (!cupNums.contains(n)) {
                orphanLidsDesc.add(n);
            }
        }
        Collections.sort(orphanLidsDesc, Collections.reverseOrder());
 
        ArrayList<Object> ordered = new ArrayList<>();
        for (Integer n : cupsDesc) {
            ordered.add(new Cup(n));
        }
        for (Integer n : lidsAsc) {
            if (cupNums.contains(n)) {
                ordered.add(new Lid(n));
            }
        }
        for (Integer n : orphanLidsDesc) {
            ordered.add(new Lid(n));
        }
 
        items = ordered;
        lastOk = true;
        redraw();
    }
 
    
    /**
     * Invierte la torre por bloques. Un bloque es una taza con todo
     * su contenido hasta su tapa (inclusive). Las tapas huérfanas y
     * tazas sin cerrar se tratan como bloques individuales.
     *
     * @return nada.
     */
    
    public void reverseTower() {
        makeAllInvisible();
 
        ArrayList<ArrayList<Object>> blocks = new ArrayList<>();
        ArrayList<Integer> openStack = new ArrayList<>();
 
        for (Object it : items) {
            if (it instanceof Cup) {
                ArrayList<Object> block = new ArrayList<>();
                block.add(it);
                blocks.add(block);
                openStack.add(((Cup) it).getNumber());
            } else {
                int n = ((Lid) it).getNumber();
                if (!openStack.isEmpty() && openStack.get(openStack.size() - 1) == n) {
                    blocks.get(blocks.size() - 1).add(it);
                    openStack.remove(openStack.size() - 1);
                } else {
                    ArrayList<Object> block = new ArrayList<>();
                    block.add(it);
                    blocks.add(block);
                }
            }
        }
 
        Collections.reverse(blocks);
        items = new ArrayList<>();
        for (ArrayList<Object> block : blocks) {
            items.addAll(block);
        }
        lastOk = true;
        redraw();
    }
 

    /**
     * Intercambia la posición de dos objetos identificados por tipo y número.
     * Ejemplo: {@code swap(new String[]{"cup","4"}, new String[]{"lid","4"})}.
     * Si alguno no existe, no realiza cambios y marca {@code ok() = false}.
     *
     * @param o1 identificador del primer objeto {tipo, número}.
     * @param o2 identificador del segundo objeto {tipo, número}.
     * @return nada.
     */

    public void swap(String[] o1, String[] o2) {
        int idx1 = findIndex(o1);
        int idx2 = findIndex(o2);
        if (idx1 == -1) {
            setOk(false, "No existe el objeto: " + o1[0] + " " + o1[1]);
            return;
        }
        if (idx2 == -1) {
            setOk(false, "No existe el objeto: " + o2[0] + " " + o2[1]);
            return;
        }
        if (idx1 == idx2) {
            lastOk = true;
            return;
        }
        makeAllInvisible();
        Object tmp = items.get(idx1);
        items.set(idx1, items.get(idx2));
        items.set(idx2, tmp);
        lastOk = true;
        redraw();
    }
 
    /**
     * Tapa todas las tazas que tienen su tapa en la torre,
     * respetando el anidamiento: las tazas internas (número menor)
     * se cierran antes que las externas (número mayor).
     *
     * @return nada.
     */
    public void cover() {
        makeAllInvisible();
        HashSet<Integer> cupNums = new HashSet<>();
        for (Object it : items) {
            if (it instanceof Cup) {
                cupNums.add(((Cup) it).getNumber());
            }
        }
        HashSet<Integer> alreadyCovered = new HashSet<>();
        for (int i = 0; i < items.size() - 1; i++) {
            Object a = items.get(i);
            Object b = items.get(i + 1);
            if (a instanceof Cup && b instanceof Lid && ((Cup) a).getNumber() == ((Lid) b).getNumber()) {
                alreadyCovered.add(((Cup) a).getNumber());
            }
        }
        HashSet<Integer> lidsToMove = new HashSet<>();
        for (Object it : items) {
            if (it instanceof Lid) {
                int n = ((Lid) it).getNumber();
                if (cupNums.contains(n) && !alreadyCovered.contains(n)) {
                    lidsToMove.add(n);
                }
            }
        }
        ArrayList<Object> base = new ArrayList<>();
        for (Object it : items) {
            if (it instanceof Lid && lidsToMove.contains(((Lid) it).getNumber())) {
                continue;
            }
            base.add(it);
        }
        ArrayList<Object> result = new ArrayList<>(base);
        for (int n : lidsToMove) {
            int cupIdx = -1;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) instanceof Cup && ((Cup) result.get(i)).getNumber() == n) {
                    cupIdx = i;
                    break;
                }
            }
            if (cupIdx == -1) continue;
            int insertIdx = result.size();
            for (int i = cupIdx + 1; i < result.size(); i++) {
                if (result.get(i) instanceof Cup && ((Cup) result.get(i)).getNumber() >= n) {
                    insertIdx = i;
                    break;
                }
            }
            Object lidObj = null;
            for (Object it : items) {
                if (it instanceof Lid && ((Lid) it).getNumber() == n) {
                    lidObj = it;
                    break;
                }
            }
            result.add(insertIdx, lidObj);
        }
        items = result;
        lastOk = true;
        redraw();
    }
 
    /**
     * Sugiere un intercambio de dos objetos que reduciría la altura de la torre.
     * Retorna un arreglo con los dos identificadores de objeto a intercambiar,
     * o null si no existe tal intercambio posible.
     * Formato de retorno: {{"cup"/"lid", "N"}, {"cup"/"lid", "M"}}
     * @return par de identificadores a intercambiar, o null si no hay mejora posible
     * @return par {{tipo,N},{tipo,M}} a intercambiar para reducir altura visual, o null si no existe
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        int n = items.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                Object tmp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, tmp);
                int newHeight = height();
                items.set(j, items.get(i));
                items.set(i, tmp);
                if (newHeight < currentHeight) {
                    String[] id1 = itemToId(tmp);
                    String[] id2 = itemToId(items.get(j));
                    lastOk = true;
                    if (visible) {
                        JOptionPane.showMessageDialog(null,
                            "Sugerencia: intercambiar " + id1[0] + " " + id1[1] +
                            " con " + id2[0] + " " + id2[1] +
                            "\nAltura actual: " + currentHeight +
                            "\nAltura después del intercambio: " + newHeight);
                    }
                    return new String[][] { id1, id2 };
                }
            }
        }
        lastOk = true;
        return null;
    }

    /**
     * Calcula la altura visual de la torre en cm
     * @return altura visual de la torre en cm
     */
    public int height() {
        int total = 0;
            ArrayList<Integer> stack = new ArrayList<>();
            for (Object it : items) {
                if (it instanceof Cup) {
                    int num   = ((Cup) it).getNumber();
                    int cupCm = ((Cup) it).getHeightCm();
                    while (!stack.isEmpty() && stack.get(stack.size() - 1) <= num) {
                        stack.remove(stack.size() - 1);
                    }
                    if (stack.isEmpty()) {
                        total += cupCm;
                    }
                    stack.add(num);
                } else {
                    int num = ((Lid) it).getNumber();
                    for (int i = stack.size() - 1; i >= 0; i--) {
                        if (stack.get(i) == num) {
                            stack.remove(i);
                            break;
                        }
                    }
                    if (stack.isEmpty()) {
                        total += 1;
                    }
                }
            }
            return total;
        }
 
    /**
     * Obtiene los números de tazas que están tapadas (taza seguida de su tapa),
     * ordenados ascendentemente.
     *
     * @return arreglo con los números de tazas tapadas.
     */

    public int[] lidedCups() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i++) {
            Object a = items.get(i);
            Object b = items.get(i + 1);
            if (a instanceof Cup && b instanceof Lid && ((Cup) a).getNumber() == ((Lid) b).getNumber()) {
                list.add(((Cup) a).getNumber());
            }
        }
        Collections.sort(list);
        int[] r = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            r[i] = list.get(i);
        }
        return r;
    }
 

    /**
     * Devuelve la secuencia de elementos apilados como pares {tipo, número}.
     *
     * @return matriz [n][2] donde n es la cantidad de elementos; cada fila es {"cup"/"lid", "N"}.
     */

    public String[][] stackingItems() {
        String[][] r = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) {
            Object it = items.get(i);
            if (it instanceof Cup) {
                r[i][0] = "cup";
                r[i][1] = String.valueOf(((Cup) it).getNumber());
            } else {
                r[i][0] = "lid";
                r[i][1] = String.valueOf(((Lid) it).getNumber());
            }
        }
        return r;
    }
    
    /**
     * Hace visible la torre y dibuja su estado actual ajustando la escala para caber en el canvas.
     *
     * @return nada.
     */

    public void makeVisible() {
        int drawHeight = Math.max(1, currentHeightCm());
        int scaleH = (CANVAS_H - 2 * MARGIN) / drawHeight;
        int scaleW = (CANVAS_W - 2 * MARGIN) / Math.max(1, width);
        this.scale = Math.max(2, Math.min(scaleH, scaleW));
        visible = true;
        Canvas c = Canvas.getCanvas();
        c.setVisible(true);
        redraw();
        lastOk = true;
    }
 
    
    /**
     * Oculta la torre y borra todos los elementos dibujados.
     *
     * @return nada.
     */
    public void makeInvisible() {
        visible = false;
        makeAllInvisible();
        eraseMarks();
        Canvas.getCanvas().setVisible(false);
    }
 
    /**
     * Oculta la torre y termina el programa.
     *
     * @return nada.
     */

    public void exit() {
        makeInvisible();
        System.exit(0);
    }
 

    /**
     * Indica si la última operación fue exitosa.
     *
     * @return {@code true} si fue exitosa, {@code false} en caso contrario.
     */

    public boolean ok() {
        return lastOk;
    }
 
    /**
     * Calcula la altura acumulada actual de la torre en centímetros.
     *
     * @return altura total en cm (privado).
     */

    private int currentHeightCm() {
        int h = 0;
        for (Object it : items) {
            h += (it instanceof Cup) ? ((Cup) it).getHeightCm() : 1;
        }
        return h;
    }
 

    /**
     * Verifica si existe una taza con número {@code n}.
     *
     * @param n número de taza a buscar.
     * @return {@code true} si existe, en caso contrario {@code false}.
     */

    private boolean hasCupNumber(int n) {
        for (Object it : items) {
            if (it instanceof Cup && ((Cup) it).getNumber() == n) {
                return true;
            }
        }
        return false;
    }
 

    /**
     * Verifica si existe una tapa con número {@code n}.
     *
     * @param n número de tapa a buscar.
     * @return {@code true} si existe, en caso contrario {@code false}.
     */

    private boolean hasLidNumber(int n) {
        for (Object it : items) {
            if (it instanceof Lid && ((Lid) it).getNumber() == n) {
                return true;
            }
        }
        return false;
    }
 
    /**
     * Busca el índice de un elemento identificado por {tipo, número}.
     *
     * @param id identificador en el formato {tipo, número}.
     * @return índice en {@code items} o {@code -1} si no existe.
     */

    private int findIndex(String[] id) {
        String type = id[0];
        int num;
        try {
            num = Integer.parseInt(id[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
        for (int i = 0; i < items.size(); i++) {
            Object it = items.get(i);
            if ("cup".equals(type) && it instanceof Cup && ((Cup) it).getNumber() == num) {
                return i;
            }
            if ("lid".equals(type) && it instanceof Lid && ((Lid) it).getNumber() == num) {
                return i;
            }
        }
        return -1;
    }
 

    /**
     * Convierte un objeto de la torre a su identificador {tipo, número}.
     *
     * @param it elemento a convertir.
     * @return arreglo {tipo, número}.
     */

    private String[] itemToId(Object it) {
        if (it instanceof Cup) {
            return new String[] { "cup", String.valueOf(((Cup) it).getNumber()) };
        }
        return new String[] { "lid", String.valueOf(((Lid) it).getNumber()) };
    }

    /**
     * Establece el estado de la última operación y muestra un mensaje si corresponde.
     *
     * @param ok estado a fijar.
     * @param message mensaje a mostrar si {@code ok} es falso y la torre es visible.
     * @return nada.
     */

    private void setOk(boolean ok, String message) {
        lastOk = ok;
        if (visible && !ok) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
 
    
    /**
     * Dibuja las marcas laterales de la regla en el canvas.
     *
     * @return nada.
     */

    private void drawMarks() {
        if (!visible) {
            return;
        }
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
     * Borra todas las marcas laterales dibujadas.
     *
     * @return nada.
     */

    private void eraseMarks() {
        for (Rectangle r : markRects) {
            r.makeInvisible();
        }
        markRects.clear();
    }
 
    
    /**
     * Oculta todos los elementos (tazas y tapas) actualmente dibujados.
     *
     * @return nada.
     */
    private void makeAllInvisible() {
        for (Object s : items) {
            if (s instanceof Cup) {
                ((Cup) s).makeInvisible();
            } else if (s instanceof Lid) {
                ((Lid) s).makeInvisible();
            }
        }
    }
 
    
    /**
     * Redibuja la torre completa en el canvas, recalculando la escala
     * para que siempre quepa en el área visible.
     *
     * @return nada.
     */
    private void redraw() {
        if (!visible) {
            return;
        }
 
        int scaleH = (CANVAS_H - 2 * MARGIN) / Math.max(1, currentHeightCm());
        int scaleW = (CANVAS_W - 2 * MARGIN) / Math.max(1, width);
        this.scale = Math.max(2, Math.min(scaleH, scaleW));
        drawMarks();
 
        int totalWidthPx = width * scale;
        int baseX = (CANVAS_W - totalWidthPx) / 2;
        int baseY = CANVAS_H - MARGIN;
 
        ArrayList<Integer> stackNum      = new ArrayList<>();
        ArrayList<Integer> stackWpx      = new ArrayList<>();
        ArrayList<Integer> stackBlockMax = new ArrayList<>();
        int accumulatedCm = 0;
 
        for (Object it : items) {
            if (it instanceof Cup) {
                Cup cup   = (Cup) it;
                int num   = cup.getNumber();
                int cupCm = cup.getHeightCm();
 
                while (!stackNum.isEmpty() && stackNum.get(stackNum.size() - 1) <= num) {
                    int topMax = stackBlockMax.remove(stackBlockMax.size() - 1);
                    stackNum.remove(stackNum.size() - 1);
                    stackWpx.remove(stackWpx.size() - 1);
                    if (stackBlockMax.isEmpty()) {
                        accumulatedCm += topMax;
                    } else {
                        stackBlockMax.set(
                            stackBlockMax.size() - 1,
                            Math.max(stackBlockMax.get(stackBlockMax.size() - 1), topMax)
                        );
                    }
                }
 
                int depth = stackNum.size();
                int wPx   = Math.max(2, width - INSET_PER_LEVEL * depth) * scale;
                int x     = baseX + (totalWidthPx - wPx) / 2;
                int yTop  = (baseY - accumulatedCm * scale) - (depth + cupCm) * scale;
 
                cup.makeVisibleAt(x, yTop, wPx, cupCm * scale);
                stackNum.add(num);
                stackWpx.add(wPx);
                stackBlockMax.add(cupCm);
 
            } else {
                Lid lid  = (Lid) it;
                int num  = lid.getNumber();
                int hPx  = lid.getHeightCm() * scale;
 
                int matchIdx = -1;
                for (int s = stackNum.size() - 1; s >= 0; s--) {
                    if (stackNum.get(s) == num) {
                        matchIdx = s;
                        break;
                    }
                }
 
                if (matchIdx != -1) {
                    int wPx    = stackWpx.get(matchIdx);
                    int x      = baseX + (totalWidthPx - wPx) / 2;
                    int blockH = stackBlockMax.get(matchIdx);
                    int yTop   = (baseY - accumulatedCm * scale) - (matchIdx + blockH) * scale;
                    lid.makeVisibleAt(x, yTop, wPx, hPx);
 
                    int closed = stackBlockMax.get(matchIdx);
                    while (stackNum.size() - 1 >= matchIdx) {
                        stackNum.remove(stackNum.size() - 1);
                        stackWpx.remove(stackWpx.size() - 1);
                        stackBlockMax.remove(stackBlockMax.size() - 1);
                    }
                    if (stackBlockMax.isEmpty()) {
                        accumulatedCm += closed;
                    } else {
                        stackBlockMax.set(
                            stackBlockMax.size() - 1,
                            Math.max(stackBlockMax.get(stackBlockMax.size() - 1), closed)
                        );
                    }
 
                } else {
                    int depth = stackNum.size();
                    int wPx   = Math.max(2, width - INSET_PER_LEVEL * Math.max(0, depth - 1)) * scale;
                    int x     = baseX + (totalWidthPx - wPx) / 2;
                    int yTop  = (baseY - accumulatedCm * scale) - (depth + 1) * scale;
                    lid.makeVisibleAt(x, yTop, wPx, hPx);
                    if (stackBlockMax.isEmpty()) {
                        accumulatedCm += 1;
                    } else {
                        stackBlockMax.set(
                            stackBlockMax.size() - 1,
                            Math.max(stackBlockMax.get(stackBlockMax.size() - 1), depth + 1)
                        );
                    }
                }
            }
        }
    }
}