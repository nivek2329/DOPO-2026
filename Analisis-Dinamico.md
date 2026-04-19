---
# Reporte de Análisis Dinámico (Cobertura de Código)

A continuación se detalla el proceso de evaluación de cobertura del proyecto mediante JUnit 4 y EclEmma, cumpliendo con los estándares de calidad del ciclo final.

## 1. Resultado Inicial

Se ejecutaron todas las pruebas JUnit existentes (TowerC1Test, TowerC2Test, TowerC4Test, TowerCC4Test, TowerContestTest, TowerContestCTest, TowerATest), obteniendo un total de **91 pruebas pasando, 0 errores y 0 fallos**.

El cubrimiento inicial fue el siguiente:

![Reporte Inicial de Cobertura](https://drive.google.com/file/d/1ccROj4zWG1q9gddD8ow3JDNhmq-ir0RF/view?usp=sharing)

## 2. Decisiones Tomadas

Al analizar el reporte inicial, se identificaron los siguientes puntos críticos:

* **Baja cobertura en TowerContest:** La clase `TowerContest` presentaba solo un 49.8% de cobertura general.
* **Problema en método `simulate`:** Aunque el método `solve` estaba al 100%, el método `simulate` tenía **0% de cobertura**. Esto se debe a que mezcla lógica de negocio con llamadas a la interfaz gráfica (`JOptionPane`, `Thread.sleep`, `tower.makeVisible`), lo que impide su automatización con JUnit.
* **Clases no utilizadas:** Las clases `Triangle` y `Circle` del paquete `shapes` quedaron con 0% de cobertura porque no son utilizadas por el simulador de torre en este ciclo, por lo que no forman parte del código de dominio evaluado.

### Acciones de Refactorización:

Se decidió refactorizar `TowerContest`, extrayendo la lógica de conversión de alturas a números de taza en un nuevo método público llamado `parseCupNumbers`. Se agregaron pruebas específicas para este nuevo método en `TowerContestTest`, lo que permitió cubrir la lógica que antes quedaba "enterrada" dentro de `simulate`.

## 3. Resultado Final

Tras la refactorización y la adición de pruebas, se lograron los siguientes resultados:

* **Meta superada:** La meta de más del 75% de cubrimiento del código de dominio fue ampliamente superada, logrando un **93.8%** en el paquete `tower`.
* **Limitación conocida:** El método `simulate` permanece sin cobertura automática debido a su dependencia inherente de la interfaz gráfica.

![Reporte Final de Cobertura (93.8%)]()

### Conclusión

La refactorización estratégica permitió validar la lógica de negocio crítica sin necesidad de automatizar la interfaz gráfica, asegurando la robustez del componente de dominio del simulador.
---
