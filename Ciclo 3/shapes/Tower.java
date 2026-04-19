import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Gestiona una torre de tazas y tapas apilables.
 * Permite agregar, quitar, ordenar, invertir, cubrir y dibujar los elementos.
 * @author Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class Tower {

    private static final int CANVAS_W        = 300;
    private static final int CANVAS_H        = 300;
    private static final int MARGIN          = 25;
    private static final int INSET_PER_LEVEL = 3;

    private final int width;
    private final int maxHeight;
    private int scale;
    private ArrayList<TowerItem> items = new ArrayList<>();
    private boolean lastOk  = true;
    private boolean visible = false;
    private ArrayList<Rectangle> markRects = new ArrayList<>();

    /**
     * Crea una torre vacía con ancho base y altura máxima.
     *
     * @param width     ancho base en centímetros.
     * @param maxHeight altura máxima permitida en centímetros.
     */
    public Tower(int width, int maxHeight) {
        this.width     = width;
        this.maxHeight = maxHeight;
        this.scale     = 4;
    }

    /**
     * Crea una torre con tazas numeradas de 1 a {cups}.
     * El ancho se fija en {cups * 2 + 2} y la altura máxima en {cups^2 + cups}.
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
     * Agrega una taza con número { i} en la cima de la torre.
     * Falla si ya existe una taza con ese número o si se excede la altura máxima.
     *
     * @param i número de la taza a agregar.
     */
    public void pushCup(int i) {
        if (hasCupNumber(i)) {
            setOk(false, "Ya existe una taza con ese número."); return;
        }
        if (currentHeightCm() + (2 * i - 1) > maxHeight) {
            setOk(false, "La torre excedería la altura máxima."); return;
        }
        items.add(new Cup(i));
        lastOk = true;
        redraw();
    }

    /**
     * Quita la taza de la cima de la torre.
     * Falla si la torre está vacía o si el elemento en la cima no es una taza.
     */
    public void popCup() {
        if (items.isEmpty()) { setOk(false, "La torre está vacía."); return; }
        TowerItem top = items.get(items.size() - 1);
        if (!top.isCup()) { setOk(false, "La cima no es una taza."); return; }
        top.makeInvisible();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la taza con número {i} en cualquier posición de la torre.
     * Falla si no existe una taza con ese número.
     *
     * @param i número de la taza a eliminar.
     */
    public void removeCup(int i) {
        if (!hasCupNumber(i)) {
            setOk(false, "No existe taza con número " + i + "."); return;
        }
        for (int k = items.size() - 1; k >= 0; k--) {
            TowerItem it = items.get(k);
            if (it.isCup() && it.getNumber() == i) {
                it.makeInvisible();
                items.remove(k);
                lastOk = true;
                redraw();
                return;
            }
        }
    }

    /**
     * Agrega una tapa con número {i} en la cima de la torre.
     * Falla si ya existe una tapa con ese número o si se excede la altura máxima.
     *
     * @param i número de la tapa a agregar.
     */
    public void pushLid(int i) {
        if (hasLidNumber(i)) {
            setOk(false, "Ya existe la tapa " + i + "."); return;
        }
        if (currentHeightCm() + 1 > maxHeight) {
            setOk(false, "La torre excedería la altura máxima."); return;
        }
        items.add(new Lid(i));
        lastOk = true;
        redraw();
    }

    /**
     * Quita la tapa de la cima de la torre.
     * Falla si la torre está vacía o si el elemento en la cima no es una tapa.
     */
    public void popLid() {
        if (items.isEmpty()) { setOk(false, "La torre está vacía."); return; }
        TowerItem top = items.get(items.size() - 1);
        if (top.isCup()) { setOk(false, "La cima no es una tapa."); return; }
        top.makeInvisible();
        items.remove(items.size() - 1);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la tapa con número {i} en cualquier posición de la torre.
     * Falla si no existe una tapa con ese número.
     *
     * @param i número de la tapa a eliminar.
     */
    public void removeLid(int i) {
        if (!hasLidNumber(i)) {
            setOk(false, "No existe tapa con número " + i + "."); return;
        }
        for (int k = items.size() - 1; k >= 0; k--) {
            TowerItem it = items.get(k);
            if (!it.isCup() && it.getNumber() == i) {
                it.makeInvisible();
                items.remove(k);
                lastOk = true;
                redraw();
                return;
            }
        }
    }

    /**
     * Ordena la torre siguiendo estas reglas:
     * las tazas quedan primero en orden descendente,
     * luego las tapas que tienen taza en orden ascendente,
     * y al final las tapas huérfanas en orden descendente.
     */
    public void orderTower() {
        makeAllInvisible();

        ArrayList<Integer> cupNums    = cupNumbers();
        ArrayList<Integer> lidNums    = lidNumbers();
        ArrayList<Integer> orphanLids = orphanLidNumbers(cupNums, lidNums);

        Collections.sort(cupNums,    Collections.reverseOrder());
        Collections.sort(lidNums);
        Collections.sort(orphanLids, Collections.reverseOrder());

        ArrayList<TowerItem> ordered = new ArrayList<>();
        for (int n : cupNums)    
            ordered.add(new Cup(n));
        for (int n : lidNums){ 
            if (cupNums.contains(n)) ordered.add(new Lid(n)); 
            }
        for (int n : orphanLids) 
            ordered.add(new Lid(n));

        items = ordered;
        lastOk = true;
        redraw();
    }

    /**
     * Invierte el orden de la torre por bloques.
     * Un bloque es una taza con todo su contenido hasta su tapa inclusive.
     * Las tapas huérfanas y las tazas sin cerrar son bloques individuales.
     */
    public void reverseTower() {
        makeAllInvisible();

        ArrayList<ArrayList<TowerItem>> blocks    = new ArrayList<>();
        ArrayList<Integer>              openStack = new ArrayList<>();

        for (TowerItem it : items) {
            if (it.isCup()) {
                ArrayList<TowerItem> block = new ArrayList<>();
                block.add(it);
                blocks.add(block);
                openStack.add(it.getNumber());
            } else {
                int n          = it.getNumber();
                boolean closes = !openStack.isEmpty()
                              && openStack.get(openStack.size() - 1) == n;
                if (closes) {
                    blocks.get(blocks.size() - 1).add(it);
                    openStack.remove(openStack.size() - 1);
                } else {
                    ArrayList<TowerItem> block = new ArrayList<>();
                    block.add(it);
                    blocks.add(block);
                }
            }
        }

        Collections.reverse(blocks);
        items = new ArrayList<>();
        for (ArrayList<TowerItem> block : blocks) 
            items.addAll(block);
        lastOk = true;
        redraw();
    }

    /**
     * Intercambia la posición de dos elementos identificados por tipo y número.
     * Falla si alguno de los elementos no existe en la torre.
     *
     * @param o1 identificador del primer elemento, en formato { {"cup"/"lid", "N"}}.
     * @param o2 identificador del segundo elemento, en formato { {"cup"/"lid", "N"}}.
     */
    public void swap(String[] o1, String[] o2) {
        int idx1 = findIndex(o1);
        int idx2 = findIndex(o2);
        if (idx1 == -1) {
            setOk(false, "No existe: " + o1[0] + " " + o1[1]); return; 
        }
        if (idx2 == -1) {
            setOk(false, "No existe: " + o2[0] + " " + o2[1]); return; 
        }
        if (idx1 == idx2) {
            lastOk = true; return; 
        }
        makeAllInvisible();
        TowerItem tmp = items.get(idx1);
        items.set(idx1, items.get(idx2));
        items.set(idx2, tmp);
        lastOk = true;
        redraw();
    }

    /**
     * Mueve cada tapa junto a su taza correspondiente, respetando el anidamiento.
     * Las tapas que ya están en posición correcta no se mueven.
     * Las tapas huérfanas permanecen en su posición.
     */
    public void cover() {
        makeAllInvisible();

        ArrayList<Integer> cups    = cupNumbers();
        ArrayList<Integer> covered = alreadyCoveredNumbers();
        ArrayList<Integer> toMove  = lidsNeedingMove(cups, covered);

        ArrayList<TowerItem> result = itemsWithoutMovingLids(toMove);
        for (int n : toMove) 
            insertLidAfterCup(n, result);

        items = result;
        lastOk = true;
        redraw();
    }

    /**
     * Busca un intercambio de dos elementos que reduzca la altura visual de la torre.
     * No modifica la torre; solo sugiere el intercambio.
     *
     * @return arreglo de dos identificadores { {{tipo,N},{tipo,M}}} si existe
     *         un intercambio que reduce la altura, o {null} si no hay mejora posible.
     */
    public String[][] swapToReduce() {
        int currentHeight = height();
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                TowerItem tmp = items.get(i);
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
                            "Sugerencia: intercambiar " + id1[0] + " " + id1[1]
                            + " con " + id2[0] + " " + id2[1]
                            + "\nAltura actual: "  + currentHeight
                            + "\nAltura después: " + newHeight);
                    }
                    return new String[][]{ id1, id2 };
                }
            }
        }
        lastOk = true;
        return null;
    }

    /**
     * Calcula la altura visual de la torre usando la fórmula secuencial:
     * { h = primero + Σ max(0, actual − anterior)}.
     * Cuando un elemento es más alto que el anterior, solo contribuye la diferencia.
     *
     * @return altura visual en centímetros.
     */
    public int height() {
        int total = 0;
        int prevH = 0;
        for (TowerItem it : items) {
            int h = it.getHeightCm();
            total += (prevH == 0) ? h : Math.max(0, h - prevH);
            prevH = h;
        }
        return total;
    }

    /**
     * Devuelve los números de las tazas que tienen su tapa inmediatamente encima.
     *
     * @return arreglo con los números de tazas tapadas, en orden ascendente.
     */
    public int[] lidedCups() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i++) {
            TowerItem a = items.get(i);
            TowerItem b = items.get(i + 1);
            if (a.isCup() && !b.isCup() && a.getNumber() == b.getNumber()) {
                list.add(a.getNumber());
            }
        }
        Collections.sort(list);
        int[] r = new int[list.size()];
        for (int i = 0; i < list.size(); i++) r[i] = list.get(i);
        return r;
    }

    /**
     * Devuelve la secuencia de elementos apilados como pares tipo-número.
     *
     * @return matriz de tamaño { [n][2]} donde cada fila es { {"cup"/"lid", "N"}}.
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
     * Hace visible la torre en el canvas, ajustando la escala para que quepa.
     */
    public void makeVisible() {
        recalcScale();
        visible = true;
        Canvas.getCanvas().setVisible(true);
        redraw();
        lastOk = true;
    }

    /**
     * Oculta la torre y el canvas.
     */
    public void makeInvisible() {
        visible = false;
        makeAllInvisible();
        eraseMarks();
        Canvas.getCanvas().setVisible(false);
    }

    /**
     * Oculta la torre y termina el programa.
     */
    public void exit() {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Indica si la última operación fue exitosa.
     *
     * @return { true} si la última operación fue exitosa, { false} en caso contrario.
     */
    public boolean ok() {
        return lastOk;
    }

    /**
     * Calcula la suma bruta de alturas de todos los elementos.
     * Se usa únicamente para validar que no se exceda { maxHeight} al agregar.
     *
     * @return suma de alturas en centímetros.
     */
    private int currentHeightCm() {
        int h = 0;
        for (TowerItem it : items) h += it.getHeightCm();
        return h;
    }

    /**
     * Verifica si ya existe una taza con el número dado.
     *
     * @param n número a buscar.
     * @return { true} si existe, { false} en caso contrario.
     */
    private boolean hasCupNumber(int n) {
        for (TowerItem it : items) {
            if (it.isCup() && it.getNumber() == n) return true;
        }
        return false;
    }

    /**
     * Verifica si ya existe una tapa con el número dado.
     *
     * @param n número a buscar.
     * @return { true} si existe, { false} en caso contrario.
     */
    private boolean hasLidNumber(int n) {
        for (TowerItem it : items) {
            if (!it.isCup() && it.getNumber() == n) return true;
        }
        return false;
    }

    /**
     * Busca el índice del elemento identificado por tipo y número.
     *
     * @param id arreglo { {tipo, número}} del elemento a buscar.
     * @return índice en la lista, o { -1} si no existe.
     */
    private int findIndex(String[] id) {
        String type = id[0];
        int num;
        try { num = Integer.parseInt(id[1]); }
        catch (NumberFormatException e) { return -1; }
        for (int i = 0; i < items.size(); i++) {
            TowerItem it = items.get(i);
            if (it.getType().equals(type) && it.getNumber() == num) return i;
        }
        return -1;
    }

    /**
     * Convierte un {TowerItem} a su identificador tipo-número.
     *
     * @param it elemento a convertir.
     * @return arreglo { {tipo, número}}.
     */
    private String[] itemToId(TowerItem it) {
        return new String[]{ it.getType(), String.valueOf(it.getNumber()) };
    }

    /**
     * Registra el resultado de la última operación.
     * Si hay error y la torre es visible, muestra el mensaje en pantalla.
     *
     * @param ok      {true} si la operación fue exitosa.
     * @param message mensaje a mostrar si { ok} es { false}.
     */
    private void setOk(boolean ok, String message) {
        lastOk = ok;
        if (visible && !ok) JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Devuelve los números de todas las tazas en la torre.
     *
     * @return lista de números de tazas.
     */
    private ArrayList<Integer> cupNumbers() {
        ArrayList<Integer> nums = new ArrayList<>();
        for (TowerItem it : items) { if (it.isCup()) nums.add(it.getNumber()); }
        return nums;
    }

    /**
     * Devuelve los números de todas las tapas en la torre.
     *
     * @return lista de números de tapas.
     */
    private ArrayList<Integer> lidNumbers() {
        ArrayList<Integer> nums = new ArrayList<>();
        for (TowerItem it : items) { if (!it.isCup()) nums.add(it.getNumber()); }
        return nums;
    }

    /**
     * Devuelve los números de las tapas cuyo número no coincide con ninguna taza.
     *
     * @param cups lista de números de tazas.
     * @param lids lista de números de tapas.
     * @return lista de números de tapas huérfanas.
     */
    private ArrayList<Integer> orphanLidNumbers(ArrayList<Integer> cups,
                                                  ArrayList<Integer> lids) {
        ArrayList<Integer> orphans = new ArrayList<>();
        for (int n : lids) { if (!cups.contains(n)) orphans.add(n); }
        return orphans;
    }

    /**
     * Devuelve los números de las tazas que ya tienen su tapa en posición inmediata.
     *
     * @return lista de números de tazas ya cubiertas.
     */
    private ArrayList<Integer> alreadyCoveredNumbers() {
        ArrayList<Integer> covered = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i++) {
            TowerItem a = items.get(i);
            TowerItem b = items.get(i + 1);
            if (a.isCup() && !b.isCup() && a.getNumber() == b.getNumber())
                covered.add(a.getNumber());
        }
        return covered;
    }

    /**
     * Devuelve los números de las tapas que tienen taza pero no están en posición correcta.
     *
     * @param cups    lista de números de tazas.
     * @param covered lista de números de tazas ya cubiertas.
     * @return lista de números de tapas que necesitan moverse.
     */
    private ArrayList<Integer> lidsNeedingMove(ArrayList<Integer> cups,
                                                ArrayList<Integer> covered) {
        ArrayList<Integer> toMove = new ArrayList<>();
        for (TowerItem it : items) {
            int n = it.getNumber();
            if (!it.isCup() && cups.contains(n) && !covered.contains(n))
                toMove.add(n);
        }
        return toMove;
    }

    /**
     * Devuelve una copia de la lista de elementos excluyendo las tapas que se van a mover.
     *
     * @param lidsToMove lista de números de tapas a excluir.
     * @return lista de elementos sin las tapas indicadas.
     */
    private ArrayList<TowerItem> itemsWithoutMovingLids(ArrayList<Integer> lidsToMove) {
        ArrayList<TowerItem> result = new ArrayList<>();
        for (TowerItem it : items) {
            if (!it.isCup() && lidsToMove.contains(it.getNumber())) continue;
            result.add(it);
        }
        return result;
    }

    /**
     * Inserta la tapa con número {n} justo después de su taza en la lista resultado.
     * Si la taza no existe en la lista, no realiza ningún cambio.
     *
     * @param n      número de la tapa a insertar.
     * @param result lista en la que se insertará la tapa.
     */
    private void insertLidAfterCup(int n, ArrayList<TowerItem> result) {
        int cupIdx = -1;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isCup() && result.get(i).getNumber() == n) {
                cupIdx = i; break;
            }
        }
        if (cupIdx == -1) return;

        int insertIdx = result.size();
        for (int i = cupIdx + 1; i < result.size(); i++) {
            if (result.get(i).isCup() && result.get(i).getNumber() >= n) {
                insertIdx = i; break;
            }
        }
        for (TowerItem it : items) {
            if (!it.isCup() && it.getNumber() == n) {
                result.add(insertIdx, it); return;
            }
        }
    }

    /**
     * Recalcula la escala para que la torre quepa dentro del canvas.
     */
    private void recalcScale() {
        int scaleH = (CANVAS_H - 2 * MARGIN) / Math.max(1, height());
        int scaleW = (CANVAS_W - 2 * MARGIN) / Math.max(1, width);
        this.scale = Math.max(2, Math.min(scaleH, scaleW));
    }

    /**
     * Redibuja toda la torre: recalcula escala, dibuja la regla y cada elemento.
     */
    private void redraw() {
        if (!visible) return;
        recalcScale();
        drawMarks();

        int totalWidthPx = width * scale;
        int baseX        = (CANVAS_W - totalWidthPx) / 2;
        int baseY        = CANVAS_H - MARGIN;
        DrawState state  = new DrawState();

        for (TowerItem it : items) {
            if (it.isCup()) drawCup((Cup) it, state, baseX, baseY, totalWidthPx);
            else            drawLid((Lid) it, state, baseX, baseY, totalWidthPx);
        }
    }

    /**
     * Dibuja una taza: desapila los contenedores que no pueden contenerla,
     * la posiciona en el canvas y registra su posición en el estado de dibujo.
     *
     * @param cup      taza a dibujar.
     * @param st       estado mutable del proceso de dibujo.
     * @param baseX    coordenada X base del canvas.
     * @param baseY    coordenada Y base del canvas.
     * @param totalWPx ancho total de la torre en píxeles.
     */
    private void drawCup(Cup cup, DrawState st, int baseX, int baseY, int totalWPx) {
        int num = cup.getNumber();
        int hCm = cup.getHeightCm();
        popUntilCanContain(num, st);

        int bottom = currentFloor(st);
        int depth  = st.stack.size();
        int wPx    = cupWidthPx(depth);
        int x      = centeredX(baseX, totalWPx, wPx);
        cup.makeVisibleAt(x, baseY - (bottom + hCm) * scale, wPx, hCm * scale);

        st.cupPos.put(num, new int[]{ bottom, hCm, depth });
        st.stack.add(new int[]{ num, bottom, hCm, bottom + 1 });
    }

    /**
     * Dibuja una tapa respetando el orden secuencial de colocación.
     * Se ubica en el máximo entre su posición natural y el tope de la última tapa.
     *
     * @param lid      tapa a dibujar.
     * @param st       estado mutable del proceso de dibujo.
     * @param baseX    coordenada X base del canvas.
     * @param baseY    coordenada Y base del canvas.
     * @param totalWPx ancho total de la torre en píxeles.
     */
    private void drawLid(Lid lid, DrawState st, int baseX, int baseY, int totalWPx) {
        int num        = lid.getNumber();
        int naturalTop = naturalLidTop(num, st);
        int depth      = lidDepth(num, st);
        int drawTop    = Math.max(naturalTop, st.lastLidTop);
        int wPx        = cupWidthPx(depth);
        int x          = centeredX(baseX, totalWPx, wPx);
        lid.makeVisibleAt(x, baseY - (drawTop + 1) * scale, wPx, scale);

        st.lastLidTop = drawTop + 1;
        updateFloor(st.lastLidTop, st);
    }

    /**
     * Desapila contenedores cuyo número es menor que {num}.
     * La nueva taza no cabe dentro de ellos, así que su tope efectivo
     * sube el piso del contenedor padre.
     *
     * @param num número de la taza que se va a insertar.
     * @param st  estado mutable del proceso de dibujo.
     */
    private void popUntilCanContain(int num, DrawState st) {
        while (!st.stack.isEmpty() && st.stack.get(st.stack.size() - 1)[0] < num) {
            int[] p    = st.stack.remove(st.stack.size() - 1);
            int effTop = Math.max(p[1] + p[2], p[3]);
            updateFloor(effTop, st);
        }
    }

    /**
     * Calcula la cima natural de una tapa: el máximo entre el borde superior
     * de su taza y la cima de todo lo apilado dentro de ella.
     *
     * @param num número de la tapa.
     * @param st  estado mutable del proceso de dibujo.
     * @return nivel de la cima natural en centímetros.
     */
    private int naturalLidTop(int num, DrawState st) {
        if (!st.cupPos.containsKey(num)) return currentFloor(st);
        int[] info     = st.cupPos.get(num);
        int naturalTop = info[0] + info[1];
        for (int s = stackIndexOf(num, st); s < st.stack.size(); s++) {
            int[] e = st.stack.get(s);
            naturalTop = Math.max(naturalTop, e[1] + e[2]);
        }
        return naturalTop;
    }

    /**
     * Devuelve la profundidad de anidamiento de la tapa, igual que la de su taza.
     * Si la taza no existe, devuelve el tamaño actual del stack.
     *
     * @param num número de la tapa.
     * @param st  estado mutable del proceso de dibujo.
     * @return profundidad de anidamiento.
     */
    private int lidDepth(int num, DrawState st) {
        return st.cupPos.containsKey(num) ? st.cupPos.get(num)[2] : st.stack.size();
    }

    /**
     * Devuelve el índice en el stack del contenedor con el número dado.
     *
     * @param num número del contenedor a buscar.
     * @param st  estado mutable del proceso de dibujo.
     * @return índice en el stack, o {st.stack.size()} si no existe.
     */
    private int stackIndexOf(int num, DrawState st) {
        for (int s = 0; s < st.stack.size(); s++) {
            if (st.stack.get(s)[0] == num) return s;
        }
        return st.stack.size();
    }

    /**
     * Devuelve el piso actual: el inner_top del contenedor activo,
     * o {globalFloor} si la pila está vacía.
     *
     * @param st estado mutable del proceso de dibujo.
     * @return nivel del piso actual en centímetros.
     */
    private int currentFloor(DrawState st) {
        return st.stack.isEmpty() ? st.globalFloor
                                  : st.stack.get(st.stack.size() - 1)[3];
    }

    /**
     * Actualiza el inner_top del contenedor activo, o {globalFloor}
     * si la pila está vacía.
     *
     * @param newTop nuevo nivel de tope en centímetros.
     * @param st     estado mutable del proceso de dibujo.
     */
    private void updateFloor(int newTop, DrawState st) {
        if (!st.stack.isEmpty()) {
            int[] top = st.stack.get(st.stack.size() - 1);
            top[3] = Math.max(top[3], newTop);
        } else {
            st.globalFloor = Math.max(st.globalFloor, newTop);
        }
    }

    /**
     * Calcula el ancho en píxeles de un elemento según su profundidad de anidamiento.
     *
     * @param depth profundidad de anidamiento.
     * @return ancho en píxeles.
     */
    private int cupWidthPx(int depth) {
        return Math.max(2, width - INSET_PER_LEVEL * depth) * scale;
    }

    /**
     * Calcula la coordenada X centrada para un elemento dado su ancho.
     *
     * @param baseX    coordenada X izquierda del área de dibujo.
     * @param totalWPx ancho total de la torre en píxeles.
     * @param wPx      ancho del elemento en píxeles.
     * @return coordenada X centrada en píxeles.
     */
    private int centeredX(int baseX, int totalWPx, int wPx) {
        return baseX + (totalWPx - wPx) / 2;
    }

    /**
     * Dibuja las marcas de la regla lateral en el canvas.
     */
    private void drawMarks() {
        if (!visible) return;
        eraseMarks();
        int baseY = CANVAS_H - MARGIN;
        for (int cm = 0; cm <= maxHeight; cm++) {
            Rectangle mark = new Rectangle();
            mark.changeSize(4, 2);
            mark.changeColor("black");
            mark.moveHorizontal(-70 + 8);
            mark.moveVertical(-15 + (baseY - cm * scale));
            mark.makeVisible();
            markRects.add(mark);
        }
    }

    /**
     * Borra todas las marcas de la regla del canvas.
     */
    private void eraseMarks() {
        for (Rectangle r : markRects) r.makeInvisible();
        markRects.clear();
    }

    /**
     * Oculta todos los elementos de la lista del canvas.
     */
    private void makeAllInvisible() {
        for (TowerItem it : items) it.makeInvisible();
    }

    /**
     * Agrupa el estado mutable compartido durante el proceso de {#redraw()}.
     *
     * <ul>
     *   <li>{stack}       – contenedores activos: {{cup_number, bottom_cm, h_cm, inner_top_cm}}</li>
     *   <li>{globalFloor} – piso global cuando la pila está vacía</li>
     *   <li>{cupPos}      – posición dibujada de cada taza: {num → {bottom, h, depth}}</li>
     *   <li>{lastLidTop}  – tope de la última tapa dibujada, para respetar el orden secuencial</li>
     * </ul>
     */
    private static class DrawState {
        ArrayList<int[]>                  stack       = new ArrayList<>();
        int                               globalFloor = 0;
        java.util.HashMap<Integer, int[]> cupPos      = new java.util.HashMap<>();
        int                               lastLidTop  = 0;
    }
}