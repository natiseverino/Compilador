%{
package compilador;
import compilador.codigoIntermedio.*;
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
                    | sentencia_declarativa { error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea()); }
		    | bloque_ejecutable sentencia_declarativa { error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea()); }

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
                        | tipo lista_variables error {error("Falta literal ';'", nroUltimaLinea);}
                        | tipo error ';'  {error("Falta lista de variables", analizadorLexico.getNroLinea());}
;

lista_variables    : ID ',' lista_variables {  String id = $1.sval;
						    Token token = TablaSimbolos.getToken(id);
						    if (token!= null){
						      token.addAtributo("uso", "VARIABLE");
						      token.addAtributo("tipo", ultimoTipo);
						    }
					  }
        | ID {  String id = $1.sval;
                Token token = TablaSimbolos.getToken(id);
                if (token!= null){
                  token.addAtributo("uso", "VARIABLE");
                  token.addAtributo("tipo", ultimoTipo);
                }
              }
        | ID  lista_variables {error("Falta literal ',' ", analizadorLexico.getNroLinea());}
;

tipo    : LONGINT {ultimoTipo = "LONGINT";}
        | FLOAT{ultimoTipo = "FLOAT";}
;

declaracion_procedimiento   : PROC ID '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias {
							String id = $2.sval;
							Token token = TablaSimbolos.getToken(id);
							if (token!= null){
							  token.addAtributo("uso", "PROCEDIMIENTO");
							}

							String cte = $8.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
							System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							else
							error("Tipo incorrecto de CTE NI", analizadorLexico.getNroLinea());}
                            | PROC ID '(' ')' NI '=' CTE bloque_sentencias {
						        String id = $2.sval;
						        Token token = TablaSimbolos.getToken(id);
						        if (token!= null){
							  token.addAtributo("uso", "PROCEDIMIENTO");
						        }

							String cte = $7.sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                            				System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            				else
                                                        error("Tipo incorrecto de CTE NI", analizadorLexico.getNroLinea());}
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
                                    token.addAtributo("uso", "PARAMETRO");
				    token.addAtributo("PASAJE", "COPIA");
                                  }
				}
                    | VAR tipo ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
				  String param = $3.sval;
				  Token token = TablaSimbolos.getToken(param);
				  if (token!= null){
				    token.addAtributo("uso", "PARAMETRO");
				    token.addAtributo("PASAJE", "VAR");
				  }
                    		}
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
;

sentencia_seleccion : IF condicion_if  THEN bloque_then END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
									polaca.addElem(new ElemPos(polaca.size()), true);
									polaca.addElem(new EtiquetaElem(polaca.size()), false);
									}
                    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    									polaca.addElem(new ElemPos(polaca.size()), true);
                                                                                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    									}
                    | IF condicion_if  bloque_then END_IF ';' {error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());}
                    | IF condicion_if  bloque_then ELSE bloque_else END_IF ';' {error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());}
                    | IF condicion_if  THEN bloque_then  bloque_else END_IF ';' {error("Falta palabra reservada ELSE ", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN END_IF ';' {error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN ELSE bloque_else END_IF ';' {error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error END_IF ';' {error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN error ELSE bloque_else END_IF ';' {error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then END_IF  {error(" Falta literal ';'", nroUltimaLinea);}
		    | IF condicion_if  THEN bloque_then error  {error(" Falta palabra reservada END_IF y literal ';'", nroUltimaLinea);}
		    | IF condicion_if  THEN bloque_then ELSE END_IF ';' {error("Falta bloque de sentencias ELSE", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE error END_IF ';' {error("Error en bloque de sentencias ELSE", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else error {error("Falta palabra reservada END_IF", analizadorLexico.getNroLinea());}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF  {error(" Falta literal ';'", nroUltimaLinea);}
		    | IF THEN bloque_then END_IF ';' {error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);}
		    | IF THEN bloque_then ELSE bloque_else END_IF ';' {error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);}
;

condicion_if  	: '(' condicion ')' {polaca.pushPos(true); polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);}
		| condicion ')' {error("Falta literal '('", analizadorLexico.getNroLinea());}
		| '(' condicion {error("Falta literal ')'", analizadorLexico.getNroLinea());}
		| '(' ')' {error("Falta condicion", analizadorLexico.getNroLinea());}
		| condicion {error("Faltan parentesis", analizadorLexico.getNroLinea());}
		| '(' error ')' {error("Error en la condicion", analizadorLexico.getNroLinea());}
;


bloque_then : bloque_sentencias {polaca.addElem(new ElemPos(polaca.size()+2),true);
				 polaca.pushPos(true);
				 polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI),false);
				 polaca.addElem(new EtiquetaElem(polaca.size()), false);}
;

bloque_else: bloque_sentencias
;

sentencia_control   : FOR '(' inicio_for ';' condicion_for  incr_decr CTE ')' bloque_for {
				SA3($7.sval);
				SA4($3.sval , $5.sval);
				SA5($3.sval, $7.sval, $6.sval); // id cte incr_decr
				polaca.addElem(new ElemPos(polaca.size() +2 ),true);
				polaca.addElem(new ElemPos(polaca.popPos()),false);
				polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI),false);
				polaca.addElem(new EtiquetaElem(polaca.size()), false);
				}
                    | FOR  inicio_for ';' condicion_for  incr_decr CTE ')' bloque_for {error("Falta literal '('", analizadorLexico.getNroLinea());}
                    | FOR '(' error ';' condicion_for  incr_decr CTE ')' bloque_for {error("Error en el inicio de la variable de control", analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for {error("Falta condición de control en sentencia de control", analizadorLexico.getNroLinea());  }
                    | FOR '(' inicio_for ';' condicion_for  error CTE ')' bloque_for {error("Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea()); }
                    | FOR '(' inicio_for ';' condicion_for  incr_decr error ')' bloque_for { error("Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea());  }
                    | FOR '(' inicio_for ';' condicion_for  incr_decr CTE  bloque_for {error("Falta literal ')'", analizadorLexico.getNroLinea());}
                    | FOR '(' inicio_for ';' condicion_for  incr_decr CTE ')' sentencia_declarativa {error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());}
		    | FOR '('';' condicion_for  incr_decr CTE ')' bloque_for {error("Falta asignacion a la variable de control", analizadorLexico.getNroLinea());}
;

inicio_for	: ID '=' CTE { $$ = $1;
			SA3($3.sval);
			SA1($1.sval);
			SA1($3.sval);
			SA2($2.sval);
			polaca.pushPos(false);
			polaca.addElem(new EtiquetaElem(polaca.size()), false);
			}
		| error '=' CTE {error("Error en el identificador de control", analizadorLexico.getNroLinea());}
		| ID error CTE {error("Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea());}
		| ID '=' error {error("Error en la constante de la asignacion", analizadorLexico.getNroLinea());}
		| ID error {error("Error en la asignacion de control", analizadorLexico.getNroLinea());}

;

bloque_for	: '{' bloque_ejecutable '}'
		| sentencia_ejecutable
;

condicion_for : condicion ';' {$$ =$1; polaca.pushPos(true); polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF),false);}

;

incr_decr   : UP {$$ = new ParserVal("+");}
            | DOWN {$$ = new ParserVal("-");}
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
						   String cadena = $3.sval;
						   System.out.println(cadena);
						   SA1(cadena);
						   polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
						  }
                    | OUT error CADENA_MULT ')' ';' {error("Falta literal '('", analizadorLexico.getNroLinea());}
                    | OUT '(' ')' ';' {error("Falta elemento a imprimir", analizadorLexico.getNroLinea());}
                    | OUT '(' error ')' ';' {error("Error en la cadena multilínea a imprimir", analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT error ';' {error("Falta literal ')'", analizadorLexico.getNroLinea());}
                    | OUT '(' CADENA_MULT ')' {error("Falta literal ';'", nroUltimaLinea);}
		    | OUT '(' factor ')' ';' { 	String factor = $3.sval;
		    				out(factor);
                                               	polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);}
;

sentencia_asignacion    : ID '=' expresion ';' {  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
						  String id = $1.sval;
                                                  String cte = $3.sval;
                                                  Token token = TablaSimbolos.getToken(id);
                                                  if (token!= null){
                                                    token.addAtributo("VALOR", cte);
                                                    SA1(id);
                                                    SA2($2.sval);
                                                  }

						}
                        | error '=' expresion ';' {error("Falta lado izquierdo de la asignación", analizadorLexico.getNroLinea());}
                        | ID error expresion ';' {error("Falta literal '=' en sentencia de asignación", analizadorLexico.getNroLinea());}
                        | ID '=' error ';' {error("Falta lado derecho de la asignación", analizadorLexico.getNroLinea());}
                        | ID '=' expresion {error("Falta literal ';'", nroUltimaLinea);}
;

sentencia_invocacion    : ID '(' lista_parametros ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '(' ')' ';' {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID lista_parametros ')' ';' {error("Falta literal '('", analizadorLexico.getNroLinea());}
                        | ID ')' ';' {error("Falta literal '('", analizadorLexico.getNroLinea());}
                        | ID '(' error ')' ';' {error("Parametros invalidos", analizadorLexico.getNroLinea());}
                        | ID '(' lista_parametros  ';' {error("Falta literal ')'", analizadorLexico.getNroLinea());}
                        | ID '('  ';' {error("Falta literal ')'", analizadorLexico.getNroLinea());}
                        | ID '(' lista_parametros ')' {error("Falta literal ';'", nroUltimaLinea);}
                        | ID '(' ')' {error("Falta literal ';'", nroUltimaLinea);}
;

lista_parametros    : ID ',' ID ',' ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID {System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | ID ',' ID ',' ID ',' error {error("Número de parámetros permitidos excedido", analizadorLexico.getNroLinea());}
                    | ID ',' error {error("Parámetro incorrecto", analizadorLexico.getNroLinea());}
                    | ID ID ID {error("Faltan literales ',' entre parámetros", analizadorLexico.getNroLinea());}
                    | ID ',' ID ID {error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());}
                    | ID ID ',' ID {error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());}
                    | ID ID {error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());}
;


condicion   : expresion MAYOR_IGUAL expresion {$$ =$1; SA2(">=");}
		| expresion MENOR_IGUAL expresion{$$ =$1;SA2("<=");}
		| expresion '>' expresion {$$ =$1;SA2(">");}
		| expresion '<' expresion {$$ =$1;SA2("<");}
		| expresion IGUAL expresion {$$ =$1;SA2("==");}
		| expresion DISTINTO  expresion {$$ =$1;SA2("!=");}
;


expresion   : expresion '+' termino {SA2($2.sval);}
            | expresion '-' termino {SA2($2.sval);}
            | termino
            | expresion '+' error {error("Falta el segundo operando en la suma", analizadorLexico.getNroLinea());}
            | expresion '-' error {error("Falta el segundo operando en la resta", analizadorLexico.getNroLinea());}
            | error '+' termino {error("Falta el primer operando en la suma", analizadorLexico.getNroLinea());}
;

termino : termino '*' factor { SA2($2.sval);}
        | termino '/' factor { SA2($2.sval);}
        | factor
        | termino '*' error {error("Falta el segundo operando en la multiplicación", analizadorLexico.getNroLinea());}
        | termino '/' error {error("Falta el segundo operando en la division", analizadorLexico.getNroLinea());}
        | error '*' factor {error("Falta el primer operando en la multiplicación", analizadorLexico.getNroLinea());}
        | error '/' factor {error("Falta el primer operando en la division", analizadorLexico.getNroLinea());}
;

factor  : ID {$$ = $1; SA1($1.sval);}
        | cte {$$ = $1; SA1($1.sval);}
;

cte : CTE {String cte = $1.sval;
	   String nuevo = checkPositivo(cte);
	   if (nuevo != null)
	   	$$ = new ParserVal(nuevo);
	   else
	   	$$ = new ParserVal(cte);
           }
      | '-' CTE { String cte = $2.sval;
      		  String nuevo = checkRango(cte);
      		  if (nuevo != null){
      		  	$$ = new ParserVal(nuevo);
      		  	System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
      		  }
     	 	}
;

%%

private AnalizadorLexico analizadorLexico;
private int nroUltimaLinea;
private PolacaInversa polaca;
private boolean verbose;
private int yyerrores;
private String ultimoTipo = "";

public Parser(AnalizadorLexico analizadorLexico, boolean debug, PolacaInversa polaca, boolean verbose){
	this.analizadorLexico = analizadorLexico;
	this.yydebug = debug;
	this.polaca = polaca;
	this.verbose = verbose;
	this.yyerrores = 0;
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
}

public int getErrores(){
	return yyerrores;
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

public String checkPositivo(String cte){
	Token token = TablaSimbolos.getToken(cte);
        String tipo = token.getTipoToken();
        if (tipo.equals("LONGINT")) {
        	long entero = 0;
		if (Long.parseLong(cte) >= Main.MAX_LONG) {
		    entero = Main.MAX_LONG - 1;
		    warning("Entero largo positivo fuera de rango: " + cte + " - Se cambia por: " + entero, analizadorLexico.getNroLinea());
		    String nuevoLexema = String.valueOf(entero);
		    cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
		    return nuevoLexema;
		}
	}
	return cte;
}



public String checkRango(String cte) {
	Token token = TablaSimbolos.getToken(cte);
	String tipo = token.getTipoToken();

	if (tipo.equals("LONGINT")) {
	    long entero = 0;
	    String nuevoLexema = null;
		if (Long.parseLong(cte) <= Main.MAX_LONG) {
		    entero = Long.parseLong(cte);
		} else {
		    entero = Main.MAX_LONG;
		    warning("Entero largo negativo fuera de rango: " + cte + " - Se cambia por: " + entero, analizadorLexico.getNroLinea());
		}
		nuevoLexema = "-" + entero;
		cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
		return nuevoLexema;

	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0f;
	    if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
		flotante = Float.parseFloat(cte);
	    } else {
		flotante = Main.MAX_FLOAT-1;
		warning("Flotante negativo fuera de rango: " + cte + " - Se cambia por: " + flotante, analizadorLexico.getNroLinea());
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

public void error(String mensaje, int linea){
	yyerrores++;
	if (verbose)
		System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: " + mensaje +" %n" + Main.ANSI_RESET, linea);

}

public void warning(String mensaje, int linea){
	if (verbose)
		System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: "+ mensaje + "%n" + Main.ANSI_RESET, linea);
}

public void out(String lex){
       Token token = TablaSimbolos.getToken(lex);
               if (token != null) {
                   if (token.getTipoToken().equals("IDENTIFICADOR")) {
                       if (token.getAtributo("uso") != null) {
                           if (token.getAtributo("uso").equals("VARIABLE")) {
                               Object valor = token.getAtributo("VALOR");
                               if (valor != null)
                                   System.out.println(token.getAtributo("VALOR") + "\n");
                               else
                                   error("Variable " + lex + " no inicializada", analizadorLexico.getNroLinea());
                           }
                       } else
                           error("Variable " + lex + " no declarada", analizadorLexico.getNroLinea());
                   }else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                       System.out.println(token.getLexema() + "\n");
               }
}

public void SA1(String lex){  //añadir factor a la polaca
	Token token = TablaSimbolos.getToken(lex);
	ElemSimple elem = new ElemSimple(token, analizadorLexico.getNroLinea());
	polaca.addElem(elem, false);
}

public void SA2(String operador){ //añadir operador binario a la polaca
	OperadorBinario elem = new OperadorBinario(operador, analizadorLexico.getNroLinea());
	polaca.addElem(elem, false);
}

public void SA3(String cte){ //chequea que la constante sea LONGINT
	 if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
 		error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
}

public void SA4(String id1, String id2){ //reviso que la variable inicializada en el for sea la misma que la de la condicion
	Token token1 = TablaSimbolos.getToken(id1);
	Token token2 = TablaSimbolos.getToken(id2);
	if (!token1.equals(token2))
		error("En la sentencia for, la variable inicializada "+ id1 + "no es la misma que la variable utilizada en la condicion" ,analizadorLexico.getNroLinea());
}

public void SA5(String id, String cte, String op){ //incremento o decremento la variable del for
	Token id_t = TablaSimbolos.getToken(id);
	Token cte_t = TablaSimbolos.getToken(cte);

	polaca.addElem(new ElemSimple(id_t), false);
	polaca.addElem(new ElemSimple(cte_t), false);
        polaca.addElem(new OperadorBinario(op), false);
        polaca.addElem(new ElemSimple(id_t), false);
        polaca.addElem(new OperadorBinario("="), false);
}