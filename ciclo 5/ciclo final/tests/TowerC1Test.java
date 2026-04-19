package tests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tower.Tower;

/**
 * Pruebas unitarias BDD para Tower.
 * height() usa la formula secuencial:
 *   h = primero + Σ max(0, actual - anterior)
 *
 * @author Angel-Garcia
 * @version 4.0 (March 2026)
 */
public class TowerC1Test {

    private Tower t;

    @Before
    public void setUp() {
        t = new Tower(30, 200);
    }

    @Test
    public void shouldStartEmptyAndOk() {
        assertEquals(0, t.height());
        assertTrue(t.ok());
        assertEquals(0, t.stackingItems().length);
    }

    @Test
    public void shouldIncreaseHeightWhenPushingCups() {
        t.pushCup(1);
        t.pushCup(2);
        assertTrue(t.ok());
        assertEquals(3, t.height());
        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("1", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("2", items[1][1]);
    }

    @Test
    public void shouldCountLidAsOneCm() {
        t.pushCup(2);
        t.pushLid(2);
        assertEquals(3, t.height());
        assertArrayEquals(new int[]{2}, t.lidedCups());
        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]); assertEquals("2", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("2", items[1][1]);
    }

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

    @Test
    public void shouldRemoveCupLeavingOrphanLid() {
        t.pushCup(3); t.pushCup(5); t.pushLid(3);
        assertEquals(3, t.stackingItems().length);
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

    @Test
    public void shouldOrderCupsDescAndLidsAsc() {
        t.pushLid(4); t.pushCup(2); t.pushCup(6);
        t.pushLid(6); t.pushCup(4); t.pushLid(2);
        t.orderTower();
        String[][] items = t.stackingItems();
        assertEquals(6, items.length);
        assertEquals("cup", items[0][0]); assertEquals("6", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("4", items[1][1]);
        assertEquals("cup", items[2][0]); assertEquals("2", items[2][1]);
        assertEquals("lid", items[3][0]); assertEquals("2", items[3][1]);
        assertEquals("lid", items[4][0]); assertEquals("4", items[4][1]);
        assertEquals("lid", items[5][0]); assertEquals("6", items[5][1]);
        assertArrayEquals(new int[]{2}, t.lidedCups());
        assertEquals(11, t.height());
    }

    @Test
    public void shouldKeepSameAfterOrderingTwice() {
        t.pushCup(6); t.pushCup(4); t.pushCup(2);
        t.pushLid(2); t.pushLid(4); t.pushLid(6);
        t.orderTower();
        String[][] a = t.stackingItems();
        t.orderTower();
        assertArrayEquals(flatten(a), flatten(t.stackingItems()));
    }

    @Test
    public void shouldOrderOnlyLidsAsOrphansDesc() {
        t.pushLid(3); t.pushLid(1); t.pushLid(5);
        t.orderTower();
        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("lid", items[0][0]); assertEquals("5", items[0][1]);
        assertEquals("lid", items[1][0]); assertEquals("3", items[1][1]);
        assertEquals("lid", items[2][0]); assertEquals("1", items[2][1]);
    }

    @Test
    public void shouldOrderOnlyCupsDesc() {
        t.pushCup(1); t.pushCup(5); t.pushCup(3);
        t.orderTower();
        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("cup", items[0][0]); assertEquals("5", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("3", items[1][1]);
        assertEquals("cup", items[2][0]); assertEquals("1", items[2][1]);
    }

    @Test
    public void shouldPlaceOrphanLidsAtEnd() {
        t.pushLid(10); t.pushCup(6); t.pushLid(6); t.pushCup(4);
        t.orderTower();
        String[][] items = t.stackingItems();
        assertEquals(4, items.length);
        assertEquals("cup", items[0][0]); assertEquals("6", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("4", items[1][1]);
        assertEquals("lid", items[2][0]); assertEquals("6", items[2][1]);
        assertEquals("lid", items[3][0]); assertEquals("10", items[3][1]);
    }

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

    @Test
    public void shouldComputeHeightAfterMixedOps() {
        t.pushCup(5);
        t.pushCup(2);
        t.pushLid(5);
        assertEquals(9, t.height());
    }

    @Test
    public void shouldReturnLidedCupsInAscendingOrder() {
        t.pushCup(6); t.pushLid(6);
        t.pushCup(2); t.pushLid(2);
        t.pushCup(4); t.pushLid(4);
        assertArrayEquals(new int[]{2, 4, 6}, t.lidedCups());
    }

    @After
    public void tearDown() {
        t = null;
    }

    private static String[] flatten(String[][] a) {
        int n = 0;
        for (String[] r : a) n += r.length;
        String[] out = new String[n];
        int k = 0;
        for (String[] r : a) for (String v : r) out[k++] = v;
        return out;
    }
}