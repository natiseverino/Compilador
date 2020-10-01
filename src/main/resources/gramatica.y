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
programa : cuerpo
;

cuerpo: bloque_declarativo
      | bloque_ejecutable
      | bloque_declarativo bloque_ejecutable
      | error
;

bloque_declarativo     : bloque_declarativo sentencia_declarativa
            | sentencia_declarativa
;

bloque_ejecutable     : bloque_ejecutable sentencia_ejecutable
            | sentencia_ejecutable
;

bloque_sentencias    : '{' lista_sentencias sentencia '}'
            | sentencia
            |'{' lista_sentencias sentencia error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal '}' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
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

declaracion_variables    : tipo lista_variables ';' {System.out.println("Declaración variables");}
                        | tipo lista_variables error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | tipo error ';'  {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta lista de variables " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

lista_variables    : ID ',' lista_variables
        | ID
        | ID  lista_variables {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

tipo    : LONGINT
        | FLOAT
;

declaracion_procedimiento   : PROC ID '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | error ID '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | error ID '(' ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '('  ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error lista_parametros_formales ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' error ')' NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}

                            | PROC ID '(' error NI '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' error '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' error '=' cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI error cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI error cte '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' error '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' error '{' cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' cte error cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' cte error cuerpo '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}

;

lista_parametros_formales   : parametro_formal ',' parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal error parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal error parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal error parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

parametro_formal    : tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' condicion ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF error condicion ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF error condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' error ')' THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' error ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion error THEN bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion error THEN bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' error bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' error bloque_sentencias ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN error ELSE bloque_sentencias END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF '(' condicion ')' THEN ELSE END_IF error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan bloques de sentencias luego de THEN y ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_control   : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')'  sentencia_ejecutable {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
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
                    | FOR '(' ID '=' CTE ';' condicion error incr_decr CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion error incr_decr CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error '{' bloque_ejecutable '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error sentencia_ejecutable {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' error '}' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

incr_decr   : UP
            | DOWN
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT error CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT ')' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_asignacion    : ID '=' factor ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | error '=' factor ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' factor error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_invocacion : ID '('lista_parametros')' ';' {System.out.println("Sentencia de invocacion con lista de parametros");}
             | ID '(' ')' ';' {System.out.println("Sentencia de invocacion sin parametros");}
             | ID '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parámetros inválidos " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
             | ID '(' error ';' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
             | ID error ')' ';' {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
             | ID '('lista_parametros')' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
             | ID '(' ')' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

lista_parametros     : parametro ',' parametro ',' parametro {System.out.println("Lista de parametros: 3 parametros");}
            | parametro ',' parametro {System.out.println("Lista de parametros: 2 parametros");}
            | parametro {System.out.println("Lista de parametros: 1 parametro");}
            | parametro ',' parametro ',' parametro ',' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Numero de parametros excedido " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | parametro ',' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parametro incorrecto " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | parametro parametro parametro {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | parametro parametro {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

parametro : factor
;

condicion    : expresion comparador expresion
	| expresion error expresion {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta comparador " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
	| expresion comparador error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la segunda expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
	| error comparador expresion {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la primera expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

comparador    : MAYOR_IGUAL {$$ = $1;}
        | MENOR_IGUAL {$$ = $1;}
        | '>' {$$ = new ParserVal('>');}
        | '<' {$$ = new ParserVal('<');}
        | IGUAL {$$ = $1;}
        | DISTINTO {$$ = $1;}
;

expresion       : expresion '+' termino
                | expresion '-' termino
                | termino
                | expresion '+' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                | expresion '-' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                | error '+' termino {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                | error '-' termino {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

termino: termino '*' factor
        | termino '/' factor
        | factor
        | termino '*' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | termino '/' error {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | error '/' factor {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | error '*' factor {System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

factor  : ID { $$ = $1;}
        | cte
;

cte : CTE {String cte = $1.sval;
           checkRango(cte, false);
           $$ = $1;
           }
      | '-' CTE { String cte = $2.sval;
      		  checkRango(cte, true);
      		  $$ = new ParserVal("-" + cte);
     	 	}
;

%%

private AnalizadorLexico analizadorLexico;

public Parser(AnalizadorLexico analizadorLexico){
	this.analizadorLexico = analizadorLexico;
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
		    System.out.printf(Main.ANSI_RED + "[Linea %d]- ERROR | Entero largo negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
		String nuevoLexema = "-" + entero;
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "LONGINT", nuevoLexema);
		    TablaSimbolos.add(nuevoToken);
		}
	    }
	    else{
		if (Long.parseLong(cte) > 2147483647L){
		    System.out.printf(Main.ANSI_RED + "[Linea %d]- ERROR | Entero largo positivo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
	    }
	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0;
	    if (negativo) {
		if ((1.17549435e-38f < Float.parseFloat(cte) && Float.parseFloat(cte) < 3.40282347e+38f)) {
		    flotante =  Float.parseFloat(cte);
		} else {
		    System.out.printf(Main.ANSI_RED + "[Linea %d]- ERROR | Flotante negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		}
		String nuevoLexema = "-" + flotante;
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "FLOAT", nuevoLexema);
		    TablaSimbolos.add(nuevoToken);
		}
	    }
	}
}