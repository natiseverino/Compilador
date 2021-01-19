# Trabajo Práctico Especial
## Diseño de Compiladores 1

En un primer lugar se realizaron las etapas de **_Análisis Léxico_** y **Análisis Sintáctico**. Ambas acceden a la clase _Tabla de Simbolos_, quien mantiene el registro de cada identificador, constante y cadena de caracteres utilizados en el código fuente.

Las reglas gramaticales se encuetran en _resources/gramatica.y_, archivo a partir del cual la herramienta Byacc genera la clase Parser.

La generación de código intermedio se llevo a cabo con la notación **polaca inversa**.

Por ultimo, el código de salida es código _Assembler para Pentium de 32 bits_, a partir del código intermedio generado anteriormente.
Para las operaciones de tipo entero se generó código mediante el metodo de **seguimiento de registros**, utilizando los registros del procesador. Mientras que para las operaciones entre datos de punto flotante se utilizó el co-procesador 80X87, y el mecanismo para generar código fue el de **variables auxiliares**.

Adicionalmente, se cuenta con distintos controles de tiempo de ejecución:
* Compatibilidad de tipos
* Canitad de invocaciones de procedimientos
* Overflow en sumas y restas
* Recursión mutua entre invocaciones de procedimientos .
