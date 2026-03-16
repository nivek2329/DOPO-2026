import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias BDD para Tower (sin helpers).
 * Mantiene @Before/@After y nombres should... en cada @Test.
 *
 * height() usa la fórmula secuencial:
 *   h = primero + Σ max(0, actual - anterior)
 * Cuando un elemento es más alto que el anterior, solo suma la diferencia.
 */
public class TowerC1Test {

    private Tower t;

    @Before
    public void setUp() {
        t = new Tower(30, 200);
    }

    // ===========================================================
    // CICLO 1: ESTADO INICIAL Y ALTURA
    // ===========================================================

    @Test
    public void shouldStartEmptyAndOk() {
        assertEquals(0, t.height());
        assertTrue(t.ok());
        String[][] items = t.stackingItems();
        assertEquals(0, items.length);
    }

    @Test
    public void shouldIncreaseHeightWhenPushingCups() {
        t.pushCup(1);  // h=1
        t.pushCup(2);  // h=3, sube 2 sobre la cup1 → 1 + (3-1) = 3
        assertTrue(t.ok());
        // Fórmula: cup1(1) + max(0, cup2(3)-cup1(1)) = 1 + 2 = 3
        assertEquals(3, t.height());

        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("1", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("2", items[1][1]);
    }

    @Test
    public void shouldCountLidAsOneCm() {
        t.pushCup(2);   // h=3
        t.pushLid(2);   // lid(1) < cup2(3) → no sube, queda dentro
        // Fórmula: cup2(3) + max(0, lid(1)-cup2(3)) = 3 + 0 = 3
        assertEquals(3, t.height());

        int[] l = t.lidedCups();
        assertArrayEquals(new int[]{2}, l);

        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("2", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("2", items[1][1]);
    }

    // ===========================================================
    // CICLO 2: VALIDACIONES BÁSICAS Y ERRORES
    // ===========================================================

    @Test
    public void shouldRejectDuplicateCup() {
        t.pushCup(4);
        assertTrue(t.ok());
        t.pushCup(4);
        assertFalse(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(1, items.length);
        assertEquals("cup", items[0][0]); assertEquals("4", items[0][1]);
    }

    @Test
    public void shouldRejectDuplicateLid() {
        t.pushCup(4);
        t.pushLid(4);
        assertTrue(t.ok());
        t.pushLid(4);
        assertFalse(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("4", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("4", items[1][1]);
    }

    @Test
    public void shouldFailPopCupIfTopIsNotCup() {
        t.pushLid(2);
        String[][] before = t.stackingItems();
        t.popCup();
        assertFalse(t.ok());
        assertArrayEquals(flatten(before), flatten(t.stackingItems()));
    }

    @Test
    public void shouldFailPopLidIfTopIsNotLid() {
        t.pushCup(3);
        String[][] before = t.stackingItems();
        t.popLid();
        assertFalse(t.ok());
        assertArrayEquals(flatten(before), flatten(t.stackingItems()));
    }

    @Test
    public void shouldRespectMaxHeight() {
        Tower small = new Tower(10, 5);
        small.pushCup(1);
        assertTrue(small.ok());
        small.pushCup(3);
        assertFalse(small.ok());

        String[][] items = small.stackingItems();
        assertEquals(1, items.length);
        assertEquals("cup", items[0][0]); assertEquals("1", items[0][1]);
    }

    // ===========================================================
    // CICLO 3: ELIMINACIONES Y TAPAS HUÉRFANAS
    // ===========================================================

    @Test
    public void shouldRemoveCupLeavingOrphanLid() {
        t.pushCup(3); t.pushCup(5); t.pushLid(3);
        String[][] items0 = t.stackingItems();
        assertEquals(3, items0.length);

        t.removeCup(3);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("5", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("3", items[1][1]);
    }

    @Test
    public void shouldFailRemovingNonExistingLid() {
        t.pushCup(4);
        String[][] before = t.stackingItems();
        t.removeLid(7);
        assertFalse(t.ok());
        assertArrayEquals(flatten(before), flatten(t.stackingItems()));
    }

    @Test
    public void shouldRemoveExistingLid() {
        t.pushCup(3); t.pushLid(3);
        t.removeLid(3);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(1, items.length);
        assertEquals("cup", items[0][0]); assertEquals("3", items[0][1]);
    }

    // ===========================================================
    // CICLO 4: ORDENAMIENTO (orderTower)
    // ===========================================================

    @Test
    public void shouldOrderCupsDescAndLidsAsc() {
        t.pushLid(4);
        t.pushCup(2);
        t.pushCup(6);
        t.pushLid(6);
        t.pushCup(4);
        t.pushLid(2);
     
        t.orderTower();
     
        // Orden resultado: [cup6(11), cup4(7), cup2(3), lid2(1), lid4(1), lid6(1)]
        String[][] items = t.stackingItems();
        assertEquals(6, items.length);
        assertEquals("cup", items[0][0]); assertEquals("6", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("4", items[1][1]);
        assertEquals("cup", items[2][0]); assertEquals("2", items[2][1]);
        assertEquals("lid", items[3][0]); assertEquals("2", items[3][1]);
        assertEquals("lid", items[4][0]); assertEquals("4", items[4][1]);
        assertEquals("lid", items[5][0]); assertEquals("6", items[5][1]);
     
        assertArrayEquals(new int[]{2}, t.lidedCups());
        // Fórmula: cup6(11) + max(0,7-11) + max(0,3-7) + max(0,1-3)
        //                    + max(0,1-1) + max(0,1-1) = 11
        assertEquals(11, t.height());
    }

    @Test
    public void shouldKeepSameAfterOrderingTwice() {
        t.pushCup(6); t.pushCup(4); t.pushCup(2);
        t.pushLid(2); t.pushLid(4); t.pushLid(6);

        t.orderTower();
        String[][] a = t.stackingItems();
        t.orderTower();
        String[][] b = t.stackingItems();

        assertArrayEquals(flatten(a), flatten(b));
    }

    @Test
    public void shouldOrderOnlyLidsAsOrphansDesc() {
        t.pushLid(3);
        t.pushLid(1);
        t.pushLid(5);

        t.orderTower();

        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("lid", items[0][0]); assertEquals("5", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("3", items[1][1]);
        assertEquals("lid", items[2][0]); assertEquals("1", items[2][1]);
    }

    @Test
    public void shouldOrderOnlyCupsDesc() {
        t.pushCup(1);
        t.pushCup(5);
        t.pushCup(3);

        t.orderTower();

        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("cup", items[0][0]); assertEquals("5", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("3", items[1][1]);
        assertEquals("cup", items[2][0]); assertEquals("1", items[2][1]);
    }

    @Test
    public void shouldAppendNewCupOnTopAfterOrdering() {
        t.pushCup(6); t.pushCup(4); t.pushCup(2);
        t.pushLid(2); t.pushLid(4); t.pushLid(6);
        t.orderTower();

        int prevLen = t.stackingItems().length;
        int[] prevLided = t.lidedCups();

        t.pushCup(3);

        String[][] items = t.stackingItems();
        assertEquals(prevLen + 1, items.length);
        assertEquals("cup", items[items.length - 1][0]);
        assertEquals("3", items[items.length - 1][1]);

        assertArrayEquals(prevLided, t.lidedCups());
    }

    @Test
    public void shouldPlaceOrphanLidsAtEnd() {
        t.pushLid(10);
        t.pushCup(6);  t.pushLid(6);
        t.pushCup(4);

        t.orderTower();

        String[][] items = t.stackingItems();
        assertEquals(4, items.length);
        assertEquals("cup", items[0][0]); assertEquals("6", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("4", items[1][1]);
        assertEquals("lid", items[2][0]); assertEquals("6", items[2][1]);
        assertEquals("lid", items[3][0]); assertEquals("10", items[3][1]);
    }

    // ===========================================================
    // CICLO 5: REVERSE 
    // ===========================================================

    @Test
    public void shouldReturnToOriginalAfterDoubleReverse() {
        t.pushCup(8); t.pushLid(8);
        t.pushCup(4); t.pushLid(4);
        String[][] original = t.stackingItems();

        t.reverseTower();
        t.reverseTower();

        assertArrayEquals(flatten(original), flatten(t.stackingItems()));
    }

    @Test
    public void shouldReverseWithOrphanLids() {
        t.pushCup(6); t.pushLid(6);
        t.pushLid(9);
        t.pushCup(4); t.pushLid(4);

        t.reverseTower();

        String[][] items = t.stackingItems();
        assertEquals(5, items.length);
        assertEquals("cup", items[0][0]); assertEquals("4", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("4", items[1][1]);
        assertEquals("lid", items[2][0]); assertEquals("9", items[2][1]);
        assertEquals("cup", items[3][0]); assertEquals("6", items[3][1]);
        assertEquals("lid", items[4][0]); assertEquals("6", items[4][1]);
    }

    // ===========================================================
    // CICLO 6: PROPIEDADES Y COMBINADAS
    // ===========================================================

    @Test
    public void shouldComputeHeightAfterMixedOps() {
        t.pushCup(5);   // h=9
        t.pushCup(2);   // h=3, 3<9 → queda dentro, suma 0
        t.pushLid(5);   // h=1, 1<3 → queda dentro, suma 0
        // Fórmula: 9 + max(0,3-9) + max(0,1-3) = 9
        assertEquals(9, t.height());
    }

    @Test
    public void shouldReturnLidedCupsInAscendingOrder() {
        t.pushCup(6); t.pushLid(6);
        t.pushCup(2); t.pushLid(2);
        t.pushCup(4); t.pushLid(4);
        int[] got = t.lidedCups();
        assertArrayEquals(new int[]{2,4,6}, got);
    }

    // ===========================================================
    // LIMPIEZA
    // ===========================================================

    @After
    public void tearDown() {
        t = null;
    }

    private static String[] flatten(String[][] a) {
        int n = 0; for (String[] r : a) n += r.length;
        String[] out = new String[n];
        int k = 0;
        for (String[] r : a) for (String v : r) out[k++] = v;
        return out;
    }
}