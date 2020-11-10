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
                    | sentencia_declarativa {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | bloque_ejecutable sentencia_declarativa {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}

;

bloque_sentencias   : '{' lista_sentencias '}'
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
        | ID {  String id = $1.sval;
                Token token = TablaSimbolos.getToken(id);
                if (token!= null){
                  token.addAtributo("USO", "VARIABLE");
                }}
        | ID  lista_variables {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

tipo    : LONGINT
        | FLOAT
;

declaracion_procedimiento   : PROC ID '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias {
							String id = $2.sval;
							Token token = TablaSimbolos.getToken(id);
							if (token!= null){
							  token.addAtributo("USO", "PROCEDIMIENTO");
							}

							String cte = $8.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
							System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							else
							System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' CTE bloque_sentencias {
						        String id = $2.sval;
						        Token token = TablaSimbolos.getToken(id);
						        if (token!= null){
							  token.addAtributo("USO", "PROCEDIMIENTO");
						        }

							String cte = $7.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                            				System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            				else
                                                        System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | ID '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | ID '(' ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC '(' ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC error '('  ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error lista_parametros_formales ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID error ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' error ')' NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' error NI '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' error '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' error '=' CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI error CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI error CTE bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' lista_parametros_formales ')' NI '=' error bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' error bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
			    | PROC ID '(' ')' error bloque_sentencias {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
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

parametro_formal    : tipo ID   {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
				  String param = $2.sval;
                                  Token token = TablaSimbolos.getToken(param);
                                  if (token!= null){
                                    token.addAtributo("USO", "PARAMETRO");
				    token.addAtributo("PASAJE", "COPIA");
                                  }
				}
                    | VAR tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
				  String param = $3.sval;
				  Token token = TablaSimbolos.getToken(param);
				  if (token!= null){
				    token.addAtributo("USO", "PARAMETRO");
				    token.addAtributo("PASAJE", "VAR");
				  }
                    		}
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_seleccion : IF condicion_if  THEN bloque_then END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  bloque_then END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | IF condicion_if  THEN bloque_then  bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then END_IF  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then error  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | IF condicion_if  THEN bloque_then ELSE END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE error END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF  {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | IF THEN bloque_then END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | IF THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

condicion_if  	: '(' condicion ')'
		| condicion ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis '(' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| '(' condicion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis ')' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| '(' ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| condicion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan parentesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| '(' error ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;


bloque_then : bloque_sentencias
;

bloque_else: bloque_sentencias
;

sentencia_control   : FOR '(' inicio_for ';' condicion ';' incr_decr CTE ')' bloque_for {
							String cte = $8.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
							System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							else
							System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							}
                    | FOR error inicio_for ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' error ';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el inicio de la variable de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for error condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion ';' error CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion ';' incr_decr error ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion ';' incr_decr CTE error bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion ';' incr_decr CTE ')' sentencia_declarativa {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		    | FOR '('';' condicion ';' incr_decr CTE ')' bloque_for {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

inicio_for	: ID '=' CTE {
			String cte = $3.sval;
			if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
			System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
			}
		| error '=' CTE {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: error inicio for en el identificador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| ID error CTE {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: error inicio for en el '=' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| ID '=' error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: error inicio for en la constante %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
		| ID error {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: error inicio for %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}

;

bloque_for	: '{' bloque_ejecutable '}'
		| sentencia_ejecutable
;

incr_decr   : UP
            | DOWN
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
						   String cadena = $3.sval;
						   System.out.printf(cadena);
						  }
                    | OUT error CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' error ')' ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT ')' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
		    | OUT '(' factor ')' ';' { String lex = $3.sval;
                                               Token token = TablaSimbolos.getToken(lex);
                                               if (token!= null) {
                                               	  if (token.getAtributo("USO") != null) {
						      if (token.getTipoToken().equals("IDENTIFICADOR") && token.getAtributo("USO").equals("VARIABLE")) {
							  Object valor = token.getAtributo("VALOR");
							  if (valor != null)
							      System.out.println((String) token.getAtributo("VALOR"));
							  else
							      System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Variable '%s' no inicializada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), lex);
						      } else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
							  System.out.println(token.getLexema());
						  }
						  else
						      System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Variable '%s' no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), lex);
                                               }
                                             }
;

sentencia_asignacion    : ID '=' expresion ';' {  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
						  String id = $1.sval;
                                                  String cte = $3.sval;
                                                  Token token = TablaSimbolos.getToken(id);
                                                  if (token!= null){
                                                    token.addAtributo("VALOR", cte);
                                                  }
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

factor  : ID
        | cte
;

cte : CTE {String cte = $1.sval;
	   String nuevo = checkRango(cte, false);
	   if (nuevo != null)
	   	$$ = new ParserVal(nuevo);
	   else
	   	$$ = new ParserVal(cte);
           }
      | '-' CTE { String cte = $2.sval;
      		  String nuevo = checkRango(cte, true);
      		  if (nuevo != null){
      		  	$$ = new ParserVal(nuevo);
      		  	System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
      		  }
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


    public String checkRango(String cte, boolean negativo) {
        Token token = TablaSimbolos.getToken(cte);
        String tipo = token.getTipoToken();

        if (tipo.equals("LONGINT")) {
            long entero = 0;
            String nuevoLexema = null;
            if (negativo) {
                if (Long.parseLong(cte) <= Main.MAX_LONG) {
                    entero = Long.parseLong(cte);
                } else {
                    entero = Main.MAX_LONG;
                    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Entero largo negativo fuera de rango: %s - Se cambia por: %d%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, entero);
                }
                nuevoLexema = "-" + entero;

            }
            else{
                if (Long.parseLong(cte) >= Main.MAX_LONG) {
                    entero = Main.MAX_LONG - 1;
                    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Entero largo positivo fuera de rango: %s - Se cambia por: %d %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, entero);
                    nuevoLexema = String.valueOf(entero);
                }
            }
            if (nuevoLexema != null) {
                cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
                return nuevoLexema;
            }
        }
        if (tipo.equals("FLOAT")) {
            float flotante = 0f;
            if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
                flotante = Float.parseFloat(cte);
            } else {
                flotante = Main.MAX_FLOAT-1;
                System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Flotante negativo fuera de rango: %s - Se cambia por: %d%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, flotante);
            }
            if (flotante != 0f) {
                String nuevoLexema = "-" + flotante;
                cambiarSimbolo(token, cte, nuevoLexema, "FLOAT");
                return nuevoLexema;
            }
        }
        return null;
    }

    public void cambiarSimbolo(Token token, String cte, String nuevoLexema, String tipo){
        int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
        //if (cont == 0)
        //  TablaSimbolos.remove(cte);
        //else
        TablaSimbolos.getToken(cte).addAtributo("contador", cont);
        if (!TablaSimbolos.existe(nuevoLexema)) {
            Token nuevoToken = new Token(token.getIdToken(), tipo, nuevoLexema);
            nuevoToken.addAtributo("contador", 1);
            TablaSimbolos.add(nuevoToken);
        } else {
            cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1;
            TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
        }
    }