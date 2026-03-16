import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas unitarias BDD para TowerContest - Ciclo 3.
 * Cubre: solve (todos los casos válidos e inválidos) con verificación
 * tanto de la cadena exacta como de la altura computada.
 *
 * Fórmula de altura del concurso:
 *   h = a[0] + Σ max(0, a[i] - a[i-1])
 *
 * Alturas alcanzables para n tazas: todos los valores impares en [2n-1, 4n-5].
 *
 * @Author: Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class TowerContestTest {

    // ===================================================================
    // REQUISITO 14: solve – caso de muestra oficial del concurso
    // ===================================================================

    @Test
    public void shouldSolveSampleInput4And9() {
        // Sample Input: 4 9  →  Sample Output: 7 3 5 1
        assertEquals("7 3 5 1", TowerContest.solve(4, 9));
    }

    // ===================================================================
    // REQUISITO 14: solve – n = 1
    // ===================================================================

    @Test
    public void shouldSolveN1ValidH() {
        assertEquals("1", TowerContest.solve(1, 1));
    }

    @Test
    public void shouldReturnImpossibleForN1InvalidH() {
        assertEquals("impossible", TowerContest.solve(1, 3));
    }

    @Test
    public void shouldReturnImpossibleForN1EvenH() {
        assertEquals("impossible", TowerContest.solve(1, 2));
    }

    // ===================================================================
    // REQUISITO 14: solve – n = 2
    // ===================================================================

    @Test
    public void shouldSolveN2OnlyValidHeight() {
        // n=2: min = max = 3, only valid h is 3
        assertEquals("3 1", TowerContest.solve(2, 3));
    }

    @Test
    public void shouldReturnImpossibleForN2HTooSmall() {
        assertEquals("impossible", TowerContest.solve(2, 1));
    }

    @Test
    public void shouldReturnImpossibleForN2HTooLarge() {
        assertEquals("impossible", TowerContest.solve(2, 5));
    }

    // ===================================================================
    // REQUISITO 14: solve – n = 3
    // ===================================================================

    @Test
    public void shouldSolveN3MinHeight() {
        // k=0: descending 5,3,1  →  h=5
        assertEquals("5 3 1", TowerContest.solve(3, 5));
    }

    @Test
    public void shouldSolveN3MaxHeight() {
        // k=1: valley=1  →  5,1,3  →  h=5+0+2=7
        assertEquals("5 1 3", TowerContest.solve(3, 7));
    }

    @Test
    public void shouldReturnImpossibleForN3HTooSmall() {
        assertEquals("impossible", TowerContest.solve(3, 3));
    }

    @Test
    public void shouldReturnImpossibleForN3HTooLarge() {
        assertEquals("impossible", TowerContest.solve(3, 9));
    }

    @Test
    public void shouldReturnImpossibleForN3EvenH() {
        assertEquals("impossible", TowerContest.solve(3, 6));
    }

    // ===================================================================
    // REQUISITO 14: solve – n = 4 (todos los valores válidos)
    // ===================================================================

    @Test
    public void shouldSolveN4MinHeight() {
        // k=0: descending 7,5,3,1  →  h=7
        assertEquals("7 5 3 1", TowerContest.solve(4, 7));
    }

    @Test
    public void shouldSolveN4MidHeight() {
        // k=1: valley=3  →  7,3,5,1  →  h=9
        assertEquals("7 3 5 1", TowerContest.solve(4, 9));
    }

    @Test
    public void shouldSolveN4MaxHeight() {
        // k=2: valley=1  →  7,1,5,3  →  h=11
        assertEquals("7 1 5 3", TowerContest.solve(4, 11));
    }

    @Test
    public void shouldReturnImpossibleForN4EvenH() {
        assertEquals("impossible", TowerContest.solve(4, 8));
    }

    @Test
    public void shouldReturnImpossibleForN4HBelowMin() {
        assertEquals("impossible", TowerContest.solve(4, 5));
    }

    @Test
    public void shouldReturnImpossibleForN4HAboveMax() {
        assertEquals("impossible", TowerContest.solve(4, 13));
    }

    // ===================================================================
    // REQUISITO 14: solve – n = 5 (todos los valores válidos)
    // ===================================================================

    @Test
    public void shouldSolveN5H9() {
        // k=0: 9,7,5,3,1  →  h=9
        assertEquals("9 7 5 3 1", TowerContest.solve(5, 9));
    }

    @Test
    public void shouldSolveN5H11() {
        // k=1: valley=5  →  9,5,7,3,1  →  h=11
        assertEquals("9 5 7 3 1", TowerContest.solve(5, 11));
    }

    @Test
    public void shouldSolveN5H13() {
        // k=2: valley=3  →  9,3,7,5,1  →  h=13
        assertEquals("9 3 7 5 1", TowerContest.solve(5, 13));
    }

    @Test
    public void shouldSolveN5H15() {
        // k=3: valley=1  →  9,1,7,5,3  →  h=15
        assertEquals("9 1 7 5 3", TowerContest.solve(5, 15));
    }

    @Test
    public void shouldReturnImpossibleForN5HTooLarge() {
        assertEquals("impossible", TowerContest.solve(5, 17));
    }

    @Test
    public void shouldReturnImpossibleForN5EvenH() {
        assertEquals("impossible", TowerContest.solve(5, 10));
    }

    // ===================================================================
    // REQUISITO 14: solve – propiedad: la altura computada debe coincidir
    // ===================================================================

    @Test
    public void shouldProduceCorrectHeightForAllN1ToN6() {
        // For every valid (n, h) pair in n=1..6, verify that solve()
        // returns a permutation whose contest-height equals h exactly.
        for (int n = 1; n <= 6; n++) {
            int minH = (n == 1) ? 1 : 2 * n - 1;
            int maxH = (n <= 2) ? minH : 4 * n - 5;
            for (int h = minH; h <= maxH; h += 2) {
                String sol = TowerContest.solve(n, h);
                assertNotEquals("solve(" + n + "," + h + ") should not be impossible",
                    "impossible", sol);
                int computed = computeContestHeight(sol);
                assertEquals(
                    "Height mismatch for n=" + n + ", h=" + h + ", sol=" + sol,
                    h, computed);
            }
        }
    }

    @Test
    public void shouldContainNDistinctOddHeightsInSolution() {
        // Each element 2i-1 for i=1..n must appear exactly once.
        int n = 5;
        for (int h = 9; h <= 15; h += 2) {
            String sol = TowerContest.solve(n, h);
            String[] parts = sol.split(" ");
            assertEquals(n, parts.length);
            boolean[] seen = new boolean[n + 1];
            for (String p : parts) {
                int val = Integer.parseInt(p);
                int cup = (val + 1) / 2;
                assertTrue("Value " + val + " out of range", cup >= 1 && cup <= n);
                assertFalse("Duplicate value " + val, seen[cup]);
                seen[cup] = true;
            }
        }
    }

    @Test
    public void shouldReturnImpossibleForAllEvenHFrom2To20() {
        for (int h = 2; h <= 20; h += 2) {
            assertEquals("h=" + h + " should be impossible for n=4",
                "impossible", TowerContest.solve(4, h));
        }
    }

    // ===================================================================
    // LIMPIEZA  (no hay @Before/@After ya que TowerContest tiene sólo
    // métodos estáticos y no requiere estado de instancia)
    // ===================================================================

    // ── Helper ──────────────────────────────────────────────────────────

    /**
     * Computes the contest-defined height of a given solution string.
     * h = a[0] + Σ_{i=1}^{n-1} max(0, a[i] - a[i-1])
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