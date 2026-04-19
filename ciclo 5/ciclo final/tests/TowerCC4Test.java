package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import tower.Tower;

/**
 * Casos de prueba comunes para la clase Tower – Ciclo 4.
 * Estas pruebas verifican comportamientos base compartidos
 *
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class TowerCC4Test {

    /**
     * Verifica que una taza fragil solo pueda crearse mediante
     * el uso explicito del tipo "fragile".
     */
    @Test
    public void shouldAllowCreatingFragileOnlyByFragileKeyword() {
        Tower t = new Tower(30, 500);
        t.pushCup("fragile", 5);

        assertTrue(t.ok());
        assertEquals("fragile", t.stackingItems()[0][0]);
        assertEquals("5", t.stackingItems()[0][1]);
    }

    /**
     * Verifica que una OpenerCup no elimine tapas que se encuentren
     * por debajo de la primera taza al descender desde la cima.
     */
    @Test
    public void openerShouldNotRemoveLidsBelowFirstCupFromTop() {
        Tower t = new Tower(30, 500);
        t.pushCup(4);
        t.pushLid(4);
        t.pushCup(3);
        t.pushLid(3);

        assertTrue(t.ok());

        t.pushCup("opener", 5);
        assertTrue(t.ok());

        String[][] items = t.stackingItems();
        assertEquals(4, items.length);
        assertEquals("lid", items[1][0]);
        assertEquals("4", items[1][1]);
    }

    /**
     * Verifica que una FearfulLid no pueda entrar a la torre
     * si su taza compañera no está presente.
     */
    @Test
    public void fearfulLidShouldNotEnterWithoutCompanionCup() {
        Tower t = new Tower(30, 500);
        t.pushLid("fearful", 7);

        assertFalse(t.ok());
        assertEquals(0, t.stackingItems().length);
    }

    /**
     * Verifica que una CrazyLid se coloque en la base de la torre
     * y que la operación cover no modifique su posición.
     */
    @Test
    public void crazyLidShouldBeBaseAndStayAfterCover() {
        Tower t = new Tower(30, 500);
        t.pushCup(3);
        t.pushCup(1);
        t.pushLid("crazy", 3);

        assertTrue(t.ok());

        String[][] before = t.stackingItems();
        assertEquals("crazy", before[0][0]);

        t.cover();
        assertTrue(t.ok());

        String[][] after = t.stackingItems();
        assertEquals("crazy", after[0][0]);
        assertEquals(before.length, after.length);
    }
}