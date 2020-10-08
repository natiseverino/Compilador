%{
package compilador;
%}

%token IGUAL
%token MAYOR_IGUAL
%token MENOR_IGUAL
%token DISTINTO
%token IF
%token THEN
%token ELSE
%token FOR
%token END_IF
%token OUT
%token FUNC
%token RETURN
%token LONGINT
%token FLOAT
%token ID
%token CTE
%token CADENA_MULT
%token PROC
%token VAR
%token NI
%token UP
%token DOWN

%%
programa : lista_sentencias
;

bloque_ejecutable   : bloque_ejecutable sentencia_ejecutable
                    | sentencia_ejecutable
;

bloque_sentencias   : '{' lista_sentencias sentencia '}'
                    | sentencia
;

lista_sentencias    : lista_sentencias sentencia
            | sentencia
;

sentencia :     sentencia_ejecutable
        | sentencia_declarativa
;

sentencia_declarativa    : declaracion_variables
            | declaracion_procedimiento
;

sentencia_ejecutable    : sentencia_seleccion
            | sentencia_control
            | sentencia_salida
            | sentencia_asignacion
            | sentencia_invocacion
;

declaracion_variables    : tipo lista_variables ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Declaración de variables %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | tipo lista_variables error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' " + Main.ANSI_RESET, nroUltimaLinea);}
                        | tipo error ';'  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lista de variables " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

lista_variables    : ID ',' lista_variables
        | ID
        | ID  lista_variables {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

tipo    : LONGINT
        | FLOAT
;

declaracion_procedimiento   : PROC ID '(' lista_parametros_formales ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | ID '(' lista_parametros_formales ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | ID '(' ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' lista_parametros_formales ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '(' lista_parametros_formales ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '('  ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error lista_parametros_formales ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' error ')' NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' error NI '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' error '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' error '=' cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI error cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI error cte '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' error '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' error '{' lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' cte lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' cte lista_sentencias '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' cte '{' '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias en declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' cte '{' '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias en declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

lista_parametros_formales   : parametro_formal ',' parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal ',' parametro_formal ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, nroUltimaLinea);}
                            | parametro_formal ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

parametro_formal    : tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | VAR tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' error ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' error ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' error bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' error bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN error ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | IF '(' condicion ')' THEN bloque_sentencias error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | IF '(' condicion ')' THEN ELSE END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan bloques de sentencias luego de THEN y ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_control   : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')'  sentencia_ejecutable {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' sentencia_ejecutable  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' sentencia_declarativa bloque_ejecutable'}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable sentencia_declarativa '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' sentencia_declarativa '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' sentencia_declarativa error '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable sentencia_declarativa error '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable error sentencia_declarativa '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_declarativa {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}

;

incr_decr   : UP
            | DOWN
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | error '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | OUT error CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | OUT '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | OUT '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | OUT '(' CADENA_MULT error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | OUT '(' CADENA_MULT ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

sentencia_asignacion    : ID '=' expresion ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | error '=' expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID error expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '=' error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '=' expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

sentencia_invocacion    : ID '(' lista_parametros ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | '(' lista_parametros ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID lista_parametros ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parámetros inválidos %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' lista_parametros error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' error ';' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' lista_parametros ')' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' ')' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

lista_parametros    : parametro ',' parametro ',' parametro {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro ',' parametro {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro ',' parametro ',' parametro ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros permitidos excedido %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro incorrecto %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro parametro parametro {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan literales ',' entre parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro ',' parametro parametro {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro parametro ',' parametro {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
                    | parametro parametro {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

parametro : factor
;

condicion   : expresion comparador expresion {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Comparación %n" + Main.ANSI_RESET, nroUltimaLinea);}
	        | expresion error expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta comparador %n" + Main.ANSI_RESET, nroUltimaLinea);}
	        | expresion comparador error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el segundo operando de la condición %n" + Main.ANSI_RESET, nroUltimaLinea);}
	        | error comparador expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el primer operando de la condición %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

comparador  : MAYOR_IGUAL
            | MENOR_IGUAL
            | '>' {$$ = new ParserVal('>');}
            | '<' {$$ = new ParserVal('<');}
            | IGUAL
            | DISTINTO
;

expresion   : expresion '+' termino {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Suma %n" + Main.ANSI_RESET, nroUltimaLinea);}
            | expresion '-' termino {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Resta %n" + Main.ANSI_RESET, nroUltimaLinea);}
            | termino
            | expresion '+' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el segundo operando en la suma %n" + Main.ANSI_RESET, nroUltimaLinea);}
            | expresion '-' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el segundo operando en la resta %n" + Main.ANSI_RESET, nroUltimaLinea);}
            | error '+' termino {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el primer operando en la suma %n" + Main.ANSI_RESET, nroUltimaLinea);}
            | error '-' termino {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el primer operando en la resta %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

termino : termino '*' factor {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Multiplicación %n" + Main.ANSI_RESET, nroUltimaLinea);}
        | termino '/' factor {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: División %n" + Main.ANSI_RESET, nroUltimaLinea);}
        | factor
        | termino '*' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el segundo operando en la multiplicación %n" + Main.ANSI_RESET, nroUltimaLinea);}
        | termino '/' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el segundo operando en la división %n" + Main.ANSI_RESET, nroUltimaLinea);}
        | error '*' factor {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el primer operando en la multiplicación %n" + Main.ANSI_RESET, nroUltimaLinea);}
        | error '/' factor {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta el primer operando en la división %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

factor  : ID
        | cte
;

cte : CTE {String cte = $1.sval;
           checkRango(cte, false);
           }
      | '-' CTE { String cte = $2.sval;
      		  checkRango(cte, true);
      		  $$ = new ParserVal("-" + cte);
      		  String cte_nueva= "-"+cte;
      		  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, nroUltimaLinea, cte_nueva);
     	 	}
;

%%

private AnalizadorLexico analizadorLexico;
private int nroUltimaLinea;

public Parser(AnalizadorLexico analizadorLexico, boolean debug){
	this.analizadorLexico = analizadorLexico;
	this.yydebug = debug;
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
}

private int yylex(){
	return analizadorLexico.yylex();
}


public void checkRango(String cte, boolean negativo){
	Token token = TablaSimbolos.getToken(cte);
	String tipo = token.getTipoToken();

	if (tipo.equals("LONGINT")){
	    long entero = 0;
	    if (negativo) {
		if (Long.parseLong(cte) <= 2147483648L) {
		    entero = Long.parseLong(cte);
		} else {
		    System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Entero largo negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
		String nuevoLexema = "-" + entero;
		int cont = Integer.parseInt(TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", String.valueOf(cont));
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "LONGINT", nuevoLexema);
		    nuevoToken.addAtributo("contador", "1");
		    TablaSimbolos.add(nuevoToken);
		}
		else {
                     cont = Integer.parseInt(TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
                     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", String.valueOf(cont));
                }
	    }
	    else{
		if (Long.parseLong(cte) > 2147483647L){
		    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Entero largo positivo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
	    }
	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0;
	    if (negativo) {
		if ((1.17549435e-38f < Float.parseFloat(cte) && Float.parseFloat(cte) < 3.40282347e+38f)) {
		    flotante =  Float.parseFloat(cte);
		} else {
		    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Flotante negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
		String nuevoLexema = "-" + flotante;
		int cont = Integer.parseInt(TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", String.valueOf(cont));
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "FLOAT", nuevoLexema);
		    nuevoToken.addAtributo("contador", "1");
		    TablaSimbolos.add(nuevoToken);
		}
		else {
                      cont = Integer.parseInt(TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
                      TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", String.valueOf(cont));
                }
	    }
	}
}