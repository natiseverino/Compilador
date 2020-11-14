%{
package compilador;
import java.util.ArrayList;
import java.util.List;
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

lista_variables    : ID ',' lista_variables { if(!declaracionID($1.sval, "Variable", ultimoTipo))
                                                    System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable redeclarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                            }
        | ID { if(!declaracionID($1.sval, "Variable", ultimoTipo))
                    System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable redeclarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
             }
        | ID  lista_variables {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

tipo    : LONGINT { ultimoTipo = "LONGINT"; }
        | FLOAT { ultimoTipo = "FLOAT"; }
;

declaracion_procedimiento   : proc_encabezado proc_parametros proc_ni proc_cuerpo   {   System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                                                                        ambitos.eliminarAmbito();

                                                                                    }
;

proc_encabezado : PROC ID   {   ambitos.agregarAmbito($2.sval);
                                if(!declaracionID($2.sval, "Procedimiento", null))
                                System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Procedimiento redeclarado %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()); }
;

proc_parametros : '(' lista_parametros_formales ')' { TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametros_proc));
                                                  parametros_proc.clear(); }
                | '(' ')'
;

proc_ni : NI '=' CTE    {   String cte = $3.sval;
                            if(!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                                System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        }
;

proc_cuerpo : '{' lista_sentencias '}'
;

lista_parametros_formales   : parametro_formal ',' parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal parametro_formal ',' parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal parametro_formal {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' parametro_formal ',' parametro_formal ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | parametro_formal ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

parametro_formal    : tipo ID { System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                parametros_proc.add(ultimoTipo + " " + $2.sval);
                                declaracionID($2.sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken($2.sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
                    | VAR tipo ID { System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                    parametros_proc.add("VAR " + ultimoTipo + " " + $3.sval);
                                    declaracionID($3.sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken($3.sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_seleccion : IF condicion_if  THEN bloque_then END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  bloque_then END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  THEN bloque_then  bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then END_IF  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | IF error THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | IF THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

condicion_if  	: '(' condicion ')'
		| condicion ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis '(' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| '(' condicion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis ')' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| '(' ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| condicion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan parentesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;


bloque_then : bloque_sentencias
;

bloque_else: bloque_sentencias
;

sentencia_control   : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for {
							String cte = $10.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
							System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							else
							System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							}
                    | '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_declarativa {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | FOR '('';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

bloque_for	: '{' bloque_ejecutable sentencia_ejecutable '}'
		| sentencia_ejecutable
;

incr_decr   : UP
            | DOWN
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | error '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    |  '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT error CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | OUT '(' factor ')' ';'
;

sentencia_asignacion    : ID '=' expresion ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            if(!TablaSimbolos.existe($1.sval + ":" + ambitos.getAmbitos()))
                                System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                        }
                        | error '=' expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID error expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

sentencia_invocacion    : ID '(' lista_parametros ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '(' ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | '(' lista_parametros ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID lista_parametros ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Parámetros inválidos %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '(' lista_parametros  ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '('  ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '(' lista_parametros ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
                        | ID '(' ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

lista_parametros    : ID ',' ID ',' ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' ID ',' ID ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ID ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan literales ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' ID ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ID ',' ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;


condicion   : expresion comparador expresion {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Comparación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
	        | expresion error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta comparador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
	        | expresion comparador error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el segundo operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
	        | error comparador expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el primer operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

comparador  : MAYOR_IGUAL
            | MENOR_IGUAL
            | '>' {$$ = new ParserVal('>');}
            | '<' {$$ = new ParserVal('<');}
            | IGUAL
            | DISTINTO
;

expresion   : expresion '+' termino {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | expresion '-' termino {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | termino
            | expresion '+' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | expresion '-' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
            | error '+' termino {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d:| Falta el primer operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

termino : termino '*' factor {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | termino '/' factor {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: División %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | factor
        | termino '*' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | termino '/' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | error '*' factor {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
        | error '/' factor {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

factor  : ID    {   System.out.println(TablaSimbolos.getToken($1.sval).getAtributo("contador"));
                    if(!TablaSimbolos.existe($1.sval + ":" + ambitos.getAmbitos()))
                        System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
        | cte
;

cte : CTE {String cte = $1.sval;
           }
      | '-' CTE { String cte = $2.sval;
      		  checkRango(cte);
      		  $$ = new ParserVal("-" + cte);
      		  String cte_nueva= "-"+cte;
      		  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte_nueva);
     	 	}
;

%%

private AnalizadorLexico analizadorLexico;
private int nroUltimaLinea;
private Ambitos ambitos;

private String ultimoTipo;
private List<String> parametros_proc = new ArrayList<>();

public Parser(AnalizadorLexico analizadorLexico, boolean debug){
	this.analizadorLexico = analizadorLexico;
	this.yydebug = debug;
	this.ambitos = new Ambitos();
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
}

private int yylex(){
	if (!analizadorLexico.endOfFile()) {
            int id = analizadorLexico.yylex();
            if (id != -1) // caracter invalido
                return id;
            while (!analizadorLexico.endOfFile()) {
                id = analizadorLexico.yylex();
                if (id != -1)
                    return id;
            }
        }
        return 0;
}


public void checkRango(String cte){
	Token token = TablaSimbolos.getToken(cte);
	String tipo = token.getTipoToken();

	if (tipo.equals("LONGINT")){
	    long entero = 0;
		if (Long.parseLong(cte) <= Main.MAX_LONG-1) {
		    entero = Long.parseLong(cte);
		} else {
		    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: | Entero largo negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		    entero = Main.MAX_LONG-1;
		}
		String nuevoLexema = "-" + entero;
		int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", cont);
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "LONGINT", nuevoLexema);
		    nuevoToken.addAtributo("contador", 1);
		    TablaSimbolos.add(nuevoToken);
		}
		else {
		     cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
		     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
		}
	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0;
		if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
		    flotante =  Float.parseFloat(cte);
		} else {
		    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: | Flotante negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		    flotante = Main.MAX_FLOAT;
		}
		String nuevoLexema = "-" + flotante;
		int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", cont);
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "FLOAT", nuevoLexema);
		    nuevoToken.addAtributo("contador", 1);
		    TablaSimbolos.add(nuevoToken);
		}
		else {
		     cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
		     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
                      }

	}
}

public boolean declaracionID(String lexema, String uso, String tipo) {
    Token token = TablaSimbolos.getToken(lexema);
    int cont = (Integer) (token.getAtributo("contador")) - 1;
    if(cont == 0)
        TablaSimbolos.remove(lexema);
    else {
        TablaSimbolos.getToken(lexema).removeAtributo("contador");
        TablaSimbolos.getToken(lexema).addAtributo("contador", cont);
    }
    String nuevoLexema = lexema + ":" + ambitos.getAmbitos();
    if(!TablaSimbolos.existe(nuevoLexema)) {
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
        nuevoToken.addAtributo("uso", uso);
        nuevoToken.addAtributo("contador", 0);
        nuevoToken.addAtributo("tipo", tipo);
        TablaSimbolos.add(nuevoToken);
        return true;
    }
    else
        return false;
}

public String getLexemaID() {
    String ambitosActuales = ambitos.getAmbitos();
    return(ambitosActuales.split(":")[ambitosActuales.split(":").length-1] + ":" + ambitosActuales);
}
