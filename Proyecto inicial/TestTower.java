/**
 * Clase para probar que Tower, Cup y Lid funcionan correctamente.
 * En BlueJ: crear objeto TestTower, luego llamar run().
 */
public class TestTower {

    /**
     * Ejecuta pruebas básicas e imprime resultados en la consola.
     */
    public void run() {
        System.out.println("=== Pruebas del simulador Stacking Cups ===\n");

        Tower t = new Tower(20, 30);

        // 1. Torre vacía
        System.out.println("1. Torre vacía:");
        System.out.println("   height() = " + t.height() + " (esperado: 0)");
        System.out.println("   ok() = " + t.ok());

        // 2. pushCup(int i)
        t.pushCup(1);  // Antes era pushCup() sin parámetros
        System.out.println("\n2. Después de pushCup(1):");
        System.out.println("   height() = " + t.height() + " (esperado: 1, taza 1)");
        t.pushCup(2);  // Antes era otro pushCup()
        System.out.println("   Después de pushCup(2), height() = " + t.height() + " (esperado: 4, tazas 1 y 2)");

        // 3. stackingItems()
        String[][] items = t.stackingItems();
        System.out.println("\n3. stackingItems() (base a cima):");
        for (int i = 0; i < items.length; i++) {
            System.out.println("   [" + i + "] " + items[i][0] + " " + items[i][1]);
        }

        // 4. pushLid(int i)
        t.pushLid(1);
        System.out.println("\n4. Después de pushLid(1):");
        System.out.println("   height() = " + t.height() + " (esperado: 5)");
        int[] lided = t.lidedCups();
        System.out.println("   lidedCups() = " + formatArray(lided) + " (esperado: [1])");

        // 5. makeVisible() (opcional: ver la torre)
        System.out.println("\n5. Haciendo visible la torre...");
        t.makeVisible();
        System.out.println("   makeVisible() ejecutado. ok() = " + t.ok());

        // 6. orderTower()
        t.orderTower();
        System.out.println("\n6. Después de orderTower() (mayor a menor, menor en cima):");
        items = t.stackingItems();
        for (int i = 0; i < items.length; i++) {
            System.out.println("   [" + i + "] " + items[i][0] + " " + items[i][1]);
        }

        // 7. reverseTower()
        t.reverseTower();
        System.out.println("\n7. Después de reverseTower():");
        items = t.stackingItems();
        for (int i = 0; i < items.length; i++) {
            System.out.println("   [" + i + "] " + items[i][0] + " " + items[i][1]);
        }

        // 8. popLid()
        t.popLid();
        System.out.println("\n8. Después de popLid(), height() = " + t.height());

        // 9. removeCup(int i)
        t.removeCup(1);
        System.out.println("9. Después de removeCup(1), height() = " + t.height());
        items = t.stackingItems();
        System.out.println("   stackingItems(): " + items.length + " elemento(s)");

        System.out.println("\n=== Pruebas terminadas. Revisa la ventana del Canvas. ===");
        System.out.println("Para cerrar: t.makeInvisible() o t.exit()");
    }

    /**
     * Ejemplo ICPC: Input 4 9, Output 7 3 5 1.
     * Construye la torre con las 4 tazas en ese orden (base: 7, luego 3, 5, 1)
     * y muestra el simulador.
     */
    public void runSampleICPC() {
        System.out.println("=== Ejemplo ICPC: n=4, h=9 ===");
        System.out.println("Output esperado: 7 3 5 1 (alturas en orden de colocación)\n");

        Tower t = new Tower(20, 15);
        
        // Usando pushCup(int i) en lugar de pushCupNumber()
        t.pushCup(4);  // Taza 4 = 7 cm
        t.pushCup(2);  // Taza 2 = 3 cm
        t.pushCup(3);  // Taza 3 = 5 cm
        t.pushCup(1);  // Taza 1 = 1 cm

        System.out.println("Orden en la torre (base a cima): 7, 3, 5, 1 cm");
        System.out.println("(Taza 4=7cm, Taza 2=3cm, Taza 3=5cm, Taza 1=1cm)");
        String[][] items = t.stackingItems();
        for (int i = 0; i < items.length; i++) {
            int altura = "cup".equals(items[i][0]) ? 2 * Integer.parseInt(items[i][1]) - 1 : 1;
            System.out.println("  " + items[i][0] + " " + items[i][1] + " -> " + altura + " cm");
        }
        System.out.println("\nAltura total (suma en simulador): " + t.height() + " cm");
        t.makeVisible();
        System.out.println("Ventana abierta. Cierra el Canvas o usa el menú de BlueJ.");
    }

    /**
     * Ejemplo visual: torre como en la figura (una U dentro de otra, anchura tipo 0-9).
     * Orden base a cima: 7 cm, 3 cm, 5 cm, 1 cm (tazas 4, 2, 3, 1).
     */
    public void runEjemploVisual() {
        Tower t = new Tower(9, 16);
        
        // Usando pushCup(int i) en lugar de pushCupNumber()
        t.pushCup(4);  // Taza 4 = 7 cm
        t.pushCup(2);  // Taza 2 = 3 cm
        t.pushCup(3);  // Taza 3 = 5 cm
        t.pushCup(1);  // Taza 1 = 1 cm
        
        t.makeVisible();
        System.out.println("Ejemplo visual: U dentro de U, anchura 9. Cierra el Canvas cuando termines.");
    }

    /**
     * Prueba de manejo de errores con ok()
     */
    public void runErrorTests() {
        System.out.println("=== Pruebas de manejo de errores ===\n");
        
        Tower t = new Tower(10, 5); // Torre pequeña (max 5 cm)
        
        // Intentar agregar taza que cabe
        t.pushCup(1); // Taza 1 = 1 cm
        System.out.println("pushCup(1): ok() = " + t.ok() + " (esperado: true)");
        
        // Intentar agregar taza que NO cabe (taza 3 = 5 cm, pero ya hay 1 cm)
        t.pushCup(3); // Debería fallar porque 1 + 5 = 6 > 5
        System.out.println("pushCup(3) con espacio insuficiente: ok() = " + t.ok() + " (esperado: false)");
        
        // Intentar agregar taza repetida
        t.pushCup(1); // Taza 1 ya existe
        System.out.println("pushCup(1) repetida: ok() = " + t.ok() + " (esperado: false)");
        
        // Agregar tapa para taza existente
        t.pushLid(1);
        System.out.println("pushLid(1) para taza existente: ok() = " + t.ok() + " (esperado: true)");
        
        // Intentar agregar tapa para taza que no existe
        t.pushLid(5);
        System.out.println("pushLid(5) para taza inexistente: ok() = " + t.ok() + " (esperado: false)");
        
        System.out.println("\nAltura final: " + t.height() + " cm");
    }

    private String formatArray(int[] a) {
        if (a == null || a.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(a[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}