package tower;

import javax.swing.JOptionPane;

/**
 * Resuelve y simula el problema Stacking Cups del ICPC.
 *
 * @author Angel-Garcia
 * @version 4.0
 */
public class TowerContest {

    /**
     * Encuentra un orden de apilamiento de n tazas con una altura visual exacta.
     *
     * @param n numero de tazas.
     * @param h altura de apilamiento deseada.
     * @return una cadena con las alturas separadas por espacios o "impossible" si no existe solucion.
     */
    public static String solve(int n, int h) {
        if (n == 1) {
            if (h == 1) {
                return "1";
            } else {
                return "impossible";
            }
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
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(result[i]);
        }
        return sb.toString();
    }

    /**
     * Convierte una cadena de solución en un arreglo de numeros de tazas.
     * Cada altura h corresponde a la taza numero (h + 1) / 2.
     *
     * @param solution cadena con las alturas separadas por espacios.
     * @return arreglo con los numeros de tazas en el orden de apilamiento.
     */
    public static int[] parseCupNumbers(String solution) {
        String[] parts    = solution.split(" ");
        int[] cupNumbers  = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int heightVal    = Integer.parseInt(parts[i]);
            cupNumbers[i]    = (heightVal + 1) / 2;
        }
        return cupNumbers;
    }

    /**
     * Simula visualmente el apilamiento de tazas para una solucion dada.
     *
     * @param n numero de tazas.
     * @param h altura de apilamiento deseada.
     */
    public static void simulate(int n, int h) {
        String solution = solve(n, h);
        if ("impossible".equals(solution)) {
            int minH;
            if (n == 1) {
                minH = 1;
            } else {
                minH = 2 * n - 1;
            }
            int maxH;
            if (n <= 2) {
                maxH = minH;
            } else {
                maxH = 4 * n - 5;
            }
            JOptionPane.showMessageDialog(null,
                "No existe solución para n=" + n + ", h=" + h + ".\n" +
                "Alturas válidas: valores impares en [" + minH + ", " + maxH + "].");
            return;
        }
        int[] cupNumbers = parseCupNumbers(solution);
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