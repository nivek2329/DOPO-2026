package tests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tower.Tower;

/**
 * Pruebas unitarias BDD para la clase Tower – Ciclo 4.
 * Verifica el comportamiento de tazas (opener, hierarchical, fragile)
 * y tapas (fearful, crazy).
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class TowerC4Test {

    private Tower t;

    /**
     * Inicializa una torre limpia antes de cada prueba.
     */
    @Before
    public void setUp() {
        t = new Tower(30, 500);
    }

    /**
     * Verifica que una OpenerCup elimine solo las tapas bloqueantes
     * hasta encontrar la primera taza al descender desde la cima.
     */
    @Test
    public void openerShouldRemoveOnlyTopLidsUntilFirstCup() {
        t.pushCup(4);
        t.pushLid(4);
        t.pushCup(3);
        t.pushLid(3);
        assertTrue(t.ok());

        t.pushCup("opener", 5);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(4, items.length);
        assertEquals("cup", items[0][0]);    assertEquals("4", items[0][1]);
        assertEquals("lid", items[1][0]);    assertEquals("4", items[1][1]);
        assertEquals("cup", items[2][0]);    assertEquals("3", items[2][1]);
        assertEquals("opener", items[3][0]); assertEquals("5", items[3][1]);
    }

    /**
     * Verifica que una FragileCup se rompa cuando se coloca encima
     * una taza de mayor tamaño, ignorando la presencia de tapas.
     */
    @Test
    public void fragileShouldBreakWhenBiggerCupIsPushedAboveIgnoringLids() {
        t.pushCup("fragile", 5);
        t.pushLid(1);
        assertTrue(t.ok());

        t.pushCup(8);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("lid", items[0][0]); assertEquals("1", items[0][1]);
        assertEquals("cup", items[1][0]); assertEquals("8", items[1][1]);
    }

    /**
     * Verifica que una HierarchicalCup se desplace por debajo de
     * elementos más pequeños y quede bloqueada al llegar al fondo.
     */
    @Test
    public void hierarchicalShouldBubbleDownPastSmallerItemsAndLockIfReachesBottom() {
        t.pushCup(6);
        t.pushCup(2);
        t.pushCup("hierarchical", 5);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("cup", items[0][0]);          assertEquals("6", items[0][1]);
        assertEquals("hierarchical", items[1][0]); assertEquals("5", items[1][1]);
        assertEquals("cup", items[2][0]);          assertEquals("2", items[2][1]);

        Tower bottom = new Tower(30, 500);
        bottom.pushCup("hierarchical", 3);
        assertTrue(bottom.ok());

        bottom.popCup();
        assertFalse(bottom.ok());
        assertEquals(1, bottom.stackingItems().length);
        assertEquals("hierarchical", bottom.stackingItems()[0][0]);
    }

    /**
     * Verifica que una FearfulLid requiera su taza compañera para entrar
     * y no pueda salir mientras esté cubriéndola.
     */
    @Test
    public void fearfulLidShouldRequireCompanionCupAndCannotLeaveIfCovering() {
        t.pushCup(3);
        t.pushLid("fearful", 3);
        assertTrue(t.ok());

        t.popLid();
        assertFalse(t.ok());
        assertEquals(2, t.stackingItems().length);

        Tower t2 = new Tower(30, 500);
        t2.pushLid("fearful", 5);
        assertFalse(t2.ok());
        assertEquals(0, t2.stackingItems().length);
    }

    /**
     * Verifica que una CrazyLid se coloque en la base de la torre
     * y que la operacion cover no la mueva.
     */
    @Test
    public void crazyLidShouldBePlacedAtBaseAndCoverShouldNotMoveIt() {
        t.pushCup(3);
        t.pushCup(1);
        t.pushLid("crazy", 3);
        assertTrue(t.ok());

        String[][] before = t.stackingItems();
        assertEquals("crazy", before[0][0]);
        assertEquals("3", before[0][1]);

        t.cover();
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertArrayEquals(flatten(before), flatten(after));
    }

    /**
     * Limpia el estado luego de cada prueba.
     */
    @After
    public void tearDown() {
        t = null;
    }

    /**
     * Convierte una matriz bidimensional en un arreglo plano.
     *
     * @param a matriz original.
     * @return arreglo plano con todos los valores.
     */
    private static String[] flatten(String[][] a) {
        int n = 0;
        for (String[] r : a) n += r.length;

        String[] out = new String[n];
        int k = 0;
        for (String[] r : a)
            for (String v : r)
                out[k++] = v;

        return out;
    }
}