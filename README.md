# Trabajo Práctico Especial
## Diseño de compiladores 1

Hasta el momento se cuenta con las etapas de Análisis Léxico y Análisis Sintáctico. Ambas acceden a la clase Tabla de Simbolos, quien mantiene el registro de cada identificador, constante y cadena de caracteres utilizados en el código fuente.

Las reglas gramaticales se encuetran en resources/gramatica.y, archivo a partir del cual la herramienta Byacc genera la clase Parser.
