package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import tower.Tower;

import javax.swing.JOptionPane;

/**
 * Pruebas de aceptacion manuales para la clase Tower.
 * Incluyen pausas y confirmacion explicita del usuario.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class TowerATest {

    /**
     * Prueba de aceptacion que valida el comportamiento de
     * OpenerCup, HierarchicalCup y CrazyLid.
     *
     * @throws Exception si ocurre un error durante la ejecucion.
     */
    @Test
    public void acceptanceDemoOpenerHierarchicalCrazy() throws Exception {
        Tower t = new Tower(30, 200);
        t.makeVisible();

        t.pushCup(4);
        t.pushLid(4);
        t.pushCup(3);
        t.pushLid(3);

        Thread.sleep(800);
        t.pushCup("opener", 5);
        Thread.sleep(800);
        t.pushLid("crazy", 2);
        Thread.sleep(800);
        t.pushCup("hierarchical", 6);
        Thread.sleep(800);

        int ans = JOptionPane.showConfirmDialog(
            null,
            "Prueba de aceptación 1:\n"
                + "- Opener solo quitó la tapa superior bloqueante\n"
                + "- Crazy lid quedó en la base\n"
                + "- Hierarchical se desplazó hacia abajo\n\n"
                + "¿Aceptas el resultado visual?",
            "TowerATest - Aceptación 1",
            JOptionPane.YES_NO_OPTION
        );

        assertEquals(JOptionPane.YES_OPTION, ans);
        t.makeInvisible();
    }

    /**
     * Prueba de aceptacion que valida el comportamiento de
     * FearfulLid y FragileCup.
     *
     * @throws Exception si ocurre un error durante la ejecucion.
     */
    @Test
    public void acceptanceDemoFearfulAndFragile() throws Exception {
        Tower t = new Tower(30, 200);
        t.makeVisible();

        t.pushCup("fragile", 5);
        Thread.sleep(700);
        t.pushCup(8);
        Thread.sleep(700);
        t.pushCup(3);
        t.pushLid("fearful", 3);
        Thread.sleep(700);
        t.popLid();
        Thread.sleep(700);

        int ans = JOptionPane.showConfirmDialog(
            null,
            "Prueba de aceptación 2:\n"
                + "- Fragile 5 se rompió al poner cup 8\n"
                + "- Fearful lid 3 entró solo cuando cup 3 existe\n"
                + "- Fearful lid 3 no salió si estaba cubriendo\n\n"
                + "¿Aceptas el resultado visual y los mensajes?",
            "TowerATest - Aceptación 2",
            JOptionPane.YES_NO_OPTION
        );

        assertEquals(JOptionPane.YES_OPTION, ans);
        t.makeInvisible();
    }
}