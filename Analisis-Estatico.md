---
# Reporte de Análisis Estático (PMD)

Se realizó el análisis estático del código utilizando **PMD 7.23.0** con 449 reglas activas, orientado a mejorar la mantenibilidad y robustez del sistema.

## 1. Resultado Inicial

El escaneo inicial arrojó diversas violaciones de alta prioridad. Las más críticas fueron:

* **En `Canvas.java`:** Violación `ClassWithOnlyPrivateConstructors`. PMD detectó que la clase y sus clases internas (`CanvasPane`, `ShapeDescription`) no estaban declaradas como `final`.
* **En `Tower.java`:**
    * `ClassWithOnlyPrivateConstructors` en la clase interna `DrawState`.
    * `ConstructorCallsOverridableMethod`: El constructor `Tower(int cups)` llamaba a `pushCup` durante la inicialización.
    * `ReturnEmptyCollectionRatherThanNull`: El método `swapToReduce` retornaba `null` en lugar de una colección vacía.

![Reporte Inicial PMD]((https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/7.png?raw=true))
![Reporte Inicial PMD]((https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/8.png?raw=true))
![Reporte Inicial PMD](https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/9.png?raw=true)
![Reporte Inicial PMD](https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/10.png?raw=true)
)
## 2. Decisiones Tomadas

Tras analizar los resultados, se determinó que algunas violaciones eran parte del diseño intencional planteado desde el comienzo, mientras que otras sí requerían corrección:

* **Singleton y Clases Internas en `Canvas.java`:** Se decidió suprimir las violaciones mediante `// NOPMD`. La clase `Canvas` implementa un patrón Singleton necesario, y Las clases internas CanvasPane y ShapeDescription acceden a campos de instancia de la clase externa, por lo que no pueden ser `Static` `final` sin romper el funcionamiento .
* **Refactorización en `Tower.java`:** Se corrigió la violación en `DrawState` declarándola explícitamente como `final`, mejorando la seguridad del diseño sin afectar el estado.
* **Supresiones por diseño:** Se suprimió `ConstructorCallsOverridableMethod` mediante `// NOPMD` ya que la llamada a `pushCup` es una dependencia lógica indispensable durante la construcción de la torre.
* **Optimización en `swapToReduce`:** Se refactorizó el método para retornar una colección vacía en lugar de `null`, cumpliendo con la regla `ReturnEmptyCollectionRatherThanNull` y mejorando la seguridad ante posibles `NullPointerException`.

## 3. Resultado Final

Tras aplicar las correcciones y gestionar adecuadamente las supresiones técnicas, el proyecto cumple con los estándares de calidad definidos por la PMD, manteniendo la integridad del diseño que ya teniamos sin bajar la calidad de el analisis dinamico.

![Reporte Final PMD](https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/11.png?raw=true)
![Reporte Final PMD](https://github.com/nivek2329/DOPO-2026/blob/main/imagenes/12.png?raw=true)

---
