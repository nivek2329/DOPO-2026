import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Casos de prueba COMUNES para TowerContest – Ciclo 3.
 * Estas pruebas corresponden a los casos de entrada/salida
 * definidos en el problema de la maratón (ICPC 2025 Problem J).
 *
 * Todos los estudiantes del curso deben pasar estas pruebas.
 *
 * @Author: Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class TowerContestCTest {

    // ===================================================================
    // Caso de muestra oficial del enunciado
    // ===================================================================

    @Test
    public void shouldSolveSampleInput1() {
        // Sample Input 1:  4 9
        // Sample Output 1: 7 3 5 1
        assertEquals("7 3 5 1", TowerContest.solve(4, 9));
    }

    // ===================================================================
    // Casos adicionales derivados de la especificación
    // ===================================================================

    @Test
    public void shouldSolveMinimumHeightForN1() {
        assertEquals("1", TowerContest.solve(1, 1));
    }

    @Test
    public void shouldSolveMinimumHeightForN2() {
        // n=2: only valid height is 3
        assertEquals("3 1", TowerContest.solve(2, 3));
    }

    @Test
    public void shouldSolveMinimumHeightForN3() {
        // Minimum for n=3 is 2*3-1=5, decreasing: 5 3 1
        assertEquals("5 3 1", TowerContest.solve(3, 5));
    }

    @Test
    public void shouldSolveMinimumHeightForN4() {
        // Minimum for n=4 is 7, decreasing: 7 5 3 1
        assertEquals("7 5 3 1", TowerContest.solve(4, 7));
    }

    @Test
    public void shouldSolveMaximumHeightForN3() {
        // Maximum for n=3 is 4*3-5=7  →  5 1 3
        assertEquals("5 1 3", TowerContest.solve(3, 7));
    }

    @Test
    public void shouldSolveMaximumHeightForN4() {
        // Maximum for n=4 is 4*4-5=11  →  7 1 5 3
        assertEquals("7 1 5 3", TowerContest.solve(4, 11));
    }

    @Test
    public void shouldReturnImpossibleWhenHIsBelowMinimum() {
        // n=4 minimum is 7; h=5 is unreachable
        assertEquals("impossible", TowerContest.solve(4, 5));
    }

    @Test
    public void shouldReturnImpossibleWhenHIsAboveMaximum() {
        // n=4 maximum is 11; h=13 is unreachable
        assertEquals("impossible", TowerContest.solve(4, 13));
    }

    @Test
    public void shouldReturnImpossibleWhenHIsEven() {
        // All cup heights are odd  →  any valid h must be odd
        assertEquals("impossible", TowerContest.solve(4, 8));
        assertEquals("impossible", TowerContest.solve(3, 6));
    }

    @Test
    public void shouldReturnImpossibleForN1InvalidH() {
        assertEquals("impossible", TowerContest.solve(1, 3));
    }

    // ===================================================================
    // Verificación de altura computada (acepta cualquier permutación válida)
    // ===================================================================

    @Test
    public void shouldProduceCorrectContestHeightForSampleCase() {
        String sol = TowerContest.solve(4, 9);
        assertEquals(9, computeContestHeight(sol));
    }

    @Test
    public void shouldProduceCorrectContestHeightForN5AllValidH() {
        // n=5: valid heights are 9, 11, 13, 15
        int[] validH = {9, 11, 13, 15};
        for (int h : validH) {
            String sol = TowerContest.solve(5, h);
            assertNotEquals("solve(5," + h + ") must not be impossible", "impossible", sol);
            assertEquals("Height mismatch for h=" + h, h, computeContestHeight(sol));
        }
    }

    // ── Helper ──────────────────────────────────────────────────────────

    /**
     * Computes the contest height formula:
     * h = a[0] + Σ max(0, a[i] - a[i-1])
     */
    private static int computeContestHeight(String solution) {
        String[] parts = solution.split(" ");
        int[] a = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            a[i] = Integer.parseInt(parts[i]);
        }
        int h = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > a[i - 1]) h += a[i] - a[i - 1];
        }
        return h;
    }
}