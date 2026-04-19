# DOPO-2026 — Stacking Cups Simulator

Simulador del problema **Stacking Cups** (Problem J, ICPC 2025), desarrollado como proyecto de la asignatura Programación Orientada a Objetos de la Escuela Colombiana de Ingeniería Julio Garavito.

**Autores:** Kevin Angel · Santiago Garcia  
**Versión:** 4.0 — final 2026-1

---

## Descripción del proyecto

El simulador permite gestionar una torre de tazas y tapas apilables con distintos comportamientos especiales, y resolver el problema de altura óptima de apilamiento definido en el concurso ICPC 2025.

---

---

## Tipos de tazas y tapas

| Tipo | Comportamiento |
|---|---|
| `NormalCup` | Taza estándar |
| `FragileCup` | Se rompe si se apila encima una taza mayor |
| `OpenerCup` | Elimina tapas bloqueantes al entrar |
| `HierarchicalCup` | Se desplaza hacia abajo hasta su posición jerárquica |
| `NormalLid` | Tapa estándar |
| `FearfulLid` | Solo entra si su taza está presente; no sale si la está cubriendo |
| `CrazyLid` | Se ubica en la base de la torre al ser insertada |

---

## pasos seguidos para iniciar el codigo en Eclipse

1. Abrir el proyecto en **Eclipse IDE**
2. Asegurarse de tener **JUnit 4** en el Build Path
3. Ejecutar las pruebas: click derecho sobre el paquete `tests` → **Coverage As → JUnit Test**

---

## Análisis de calidad

- **Análisis dinámico:** ver [`Analisis-Dinamico.md`](./Analisis-Dinamico.md)
- **Análisis estático:** ver [`Analisis-Estatico.md`](./Analisis-Estatico.md)

---

---

## Retrospectivas anteriores por ciclo

- [Retrospectiva Ciclo 1](./proyecto%20inicial/Retrospectivas.docx)
- [Retrospectiva Ciclo 2](./Ciclo%202/Retrospectiva_C2.docx)
- [Retrospectiva Ciclo 3](./Ciclo%203/Retrospectiva_C3.docx)
- [Retrospectiva Ciclo 4](./Ciclo%204/Retrospectiva_C4.docx)

---

## Retrospectiva — Ciclo 5 (Cierre)

### 1. ¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.

**Primer mini-ciclo: Refactorización de la jerarquía Cup y Lid**  
Se convirtieron `Cup` y `Lid` en clases abstractas con contrato mínimo debido a que en el ciclo 4 estaban mal estructuradas. Se crearon `NormalCup` y `NormalLid` como implementaciones base del dibujo. Las demás subclases (`FragileCup`, `OpenerCup`, `HierarchicalCup`, `FearfulLid`, `CrazyLid`) pasaron a heredar directamente de `Cup` y `Lid`, implementando `makeVisibleAt` y `makeInvisible` de forma propia. Esto corrigió el uso incorrecto de la abstracción y el polimorfismo.

**Segundo mini-ciclo: Análisis dinámico con JUnit y Coverage**  
Se ejecutaron todas las pruebas existentes desde los cilos anteriores y se midió el cubrimiento con la herramienta de coverage de Eclipse. Se identificó que `TowerContest.simulate` tenía 0% de cobertura por depender de GUI. Se extrajo el método `parseCupNumbers` para cubrir la lógica testeable, subiendo `TowerContest` de 49,8% a 61,3% y el paquete `tower` a 93,8%.

**Tercer mini-ciclo: Análisis estático con PMD**  
Se instaló PMD 7.23.0 y se corrió sobre el proyecto con 449 reglas activas. Se identificaron 5 violaciones de prioridad alta. Se corrigió `DrawState` agregando `final`, se refactorizó `swapToReduce` para retornar array vacío en lugar de `null`, y se suprimieron con `// NOPMD` las violaciones que no podían corregirse sin romper el diseño del sistema.

**Cuarto mini-ciclo: Documentación y publicación en GitHub**  
Se completaron los informes de análisis dinámico y estático en formato Markdown, se actualizó el `README.md` y se realizaron los commits correspondientes al repositorio.

### 2. ¿Cuál es el estado actual del proyecto?

El proyecto está completo. Se completo cada uno de los ciclos exitosamente, se superó el 75% de cobertura de código de dominio con un 93,8%, y se cumplió la meta de cero violaciones de prioridad alta en PMD.

### 3. ¿Cuál fue el tiempo total invertido?

 10 horas Kevin Angel y 9 horas Santiago Garcia.

### 4. ¿Cuál fue el mayor logro?

El mayor logro fue corregir el uso del polimorfismo y la abstracción en la jerarquía `Cup`/`Lid`. Antes, las clases abstractas contenían toda la lógica de dibujo y las subclases no sobreescribían nada relevante. Después de la refactorización, cada subclase implementa sus propios métodos `makeVisibleAt` y `makeInvisible`, haciendo que el polimorfismo sea real y justificado ademas de tener las pruebas unitarias con un bajo porcentaje de error y de mal uso.

### 5. ¿Cuál fue el mayor problema técnico?

El mayor problema técnico fue el análisis estático con PMD — el plugin no generaba resultados en Eclipse por incompatibilidad con Java 21. Se resolvió instalando `pmd-eclipse-plugin 7.23.0` y desinstalando la versión anterior, lo que permitió ver las violaciones en la vista Violations Overview.

### 6. ¿Qué hicieron bien como equipo?

Mantuvimos la división de trabajo clara: uno se encargó de la refactorización de la jerarquía mientras el otro trabajaba en las pruebas y el análisis. Nos comprometemos a iniciar el análisis estático y dinámico desde el comienzo de cada ciclo para no dejarlo al final.

### 7. ¿Cuál fue la práctica XP más útil?

La práctica más útil fue el **refactoring continuo**: en lugar de reescribir todo, identificamos exactamente qué estaba mal en la jerarquía de abstracción y aplicamos cambios quirúrgicos que preservaron el comportamiento existente sin romper las pruebas.

### 8. Referencias

- Barnes, D. J., & Kölling, M. (2016). *Objects First with Java: A Practical Introduction Using BlueJ* (6th ed.). Pearson.
- Oracle. (2024). *Java SE 21 Documentation*. https://docs.oracle.com/en/java/javase/21/
- ICPC. (2025). *Problem J: Stacking Cups — ICPC World Finals 2025*.
- Escuela Colombiana de Ingeniería. (2026). *Enunciado DOPO-I04-2026-01*.
- PMD. (2024). *PMD for Eclipse Plugin*. https://github.com/pmd/pmd-eclipse-plugin

---
