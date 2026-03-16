import javax.swing.JOptionPane;

/**
 * Resuelve y simula el problema de la maratón Stacking Cups (ICPC 2025, Problema J).
 *
 * <p>Dado un número de tazas { n} con alturas {1, 3, 5, ..., 2n-1}
 * y una altura objetivo {h}, encuentra un orden de apilamiento cuya
 * altura visual sea exactamente { h}.</p>
 *
 * <p>Fórmula de altura para una secuencia {a[0], a[1], ..., a[n-1]}:</p>
 * <pre>h = a[0] + Σ max(0, a[i] - a[i-1])</pre>
 *
 * <p>Alturas alcanzables para {n} tazas:
 * todos los valores impares en {[2n-1, 4n-5]}.</p>
 *
 * @author Angel-Garcia
 * @version 3.0 (March 2026)
 */
public class TowerContest {

    /**
     * Resuelve el problema Stacking Cups: encuentra una permutación de las alturas
     * { {1, 3, 5, ..., 2n-1}} cuya altura de apilamiento sea exactamente { h}.
     *
     * <p>Algoritmo: sea {k = (h - (2n-1)) / 2}. Se construye la secuencia
     * {[2n-1, valley, resto_decreciente]}, donde { valley = 2n-3-2k}.
     * El valle crea exactamente una subida de {2k}, dando altura total { h}.</p>
     *
     * @param n número de tazas (1 ≤ n ≤ 200000).
     * @param h altura de apilamiento deseada (1 ≤ h ≤ 4·10^10).
     * @return cadena con las alturas separadas por espacios en el orden de apilamiento,
     *         o {"impossible"} si no existe ninguna permutación válida.
     */
    public static String solve(int n, int h) {
        if (n == 1) {
            return (h == 1) ? "1" : "impossible";
        }

        int minH = 2 * n - 1;
        int maxH = 4 * n - 5;

        if (h < minH || h > maxH || (h % 2 == 0)) {
            return "impossible";
        }

        int k       = (h - minH) / 2;
        int valleyH = 2 * n - 3 - 2 * k;

        int[] result = new int[n];
        result[0] = 2 * n - 1;
        result[1] = valleyH;

        int pos = 2;
        for (int i = n - 1; i >= 1; i--) {
            int hi = 2 * i - 1;
            if (hi != valleyH) {
                result[pos++] = hi;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        return sb.toString();
    }

    /**
     * Simula visualmente la solución del problema usando la clase {Tower}.
     * Apila las tazas en el orden calculado por { #solve(int, int)} y muestra
     * el resultado en el canvas. Si no existe solución, muestra un mensaje informativo.
     *
     * <p>La clase { Tower} se usa únicamente para la simulación visual;
     * el cálculo de la solución se realiza de forma independiente en {#solve(int, int)}.</p>
     *
     * @param n número de tazas.
     * @param h altura de apilamiento deseada.
     */
    public static void simulate(int n, int h) {
        String solution = solve(n, h);

        if ("impossible".equals(solution)) {
            int minH = (n == 1) ? 1 : 2 * n - 1;
            int maxH = (n <= 2) ? minH : 4 * n - 5;
            JOptionPane.showMessageDialog(null,
                "No existe solución para n=" + n + ", h=" + h + ".\n" +
                "Alturas válidas: valores impares en [" + minH + ", " + maxH + "].");
            return;
        }

        String[] parts      = solution.split(" ");
        int[]    cupNumbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int heightVal  = Integer.parseInt(parts[i]);
            cupNumbers[i]  = (heightVal + 1) / 2;
        }

        Tower tower = new Tower(n * 4 + 4, h + 10);
        tower.makeVisible();

        for (int cupNum : cupNumbers) {
            tower.pushCup(cupNum);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        JOptionPane.showMessageDialog(null,
            "Solución: " + solution + "\n" +
            "Altura del concurso: " + h + "\n" +
            "Altura visual Tower: " + tower.height());
    }
}