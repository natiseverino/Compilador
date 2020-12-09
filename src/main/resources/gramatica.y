%{
package compilador;

import compilador.codigoIntermedio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                    | sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }
		    | bloque_ejecutable sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }

;

bloque_sentencias   : '{' lista_sentencias '}'
                    | sentencia
;

lista_sentencias    : lista_sentencias sentencia
                    | sentencia
;

sentencia   : sentencia_ejecutable
            | sentencia_declarativa
;

sentencia_declarativa   : declaracion_variables
                        | declaracion_procedimiento
;

sentencia_ejecutable    : sentencia_seleccion
                        | sentencia_control
                        | sentencia_salida
                        | sentencia_asignacion
                        | sentencia_invocacion
;

declaracion_variables   : tipo lista_variables ';'  { imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea()); }
                        | tipo lista_variables error {Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
                        | tipo error   {Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables %n",analizadorLexico.getNroLinea()));}
;

lista_variables : ID ',' lista_variables { declaracionID($1.sval, Main.VARIABLE, ultimoTipo); }
                | ID { declaracionID($1.sval, Main.VARIABLE, ultimoTipo); }
      		| ID  lista_variables {Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n",analizadorLexico.getNroLinea()));}
;

tipo    : LONGINT { ultimoTipo = "LONGINT"; }
        | FLOAT { ultimoTipo = "FLOAT"; }
;

declaracion_procedimiento   : proc_encabezado lista_parametros_formales proc_ni proc_cuerpo   { imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                                                                                                ambitos.eliminarAmbito(actualizarTablaSimbolos);
                                                                                    }
                            | proc_encabezado lista_parametros_formales proc_cuerpo { Errores.addError(String.format("[AS] | Linea %d: Falta declarar el número de invocaciones permitido en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                            | proc_encabezado error proc_ni proc_cuerpo { Errores.addError(String.format("[AS] | Linea %d: Faltan declarar los parametros en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;

proc_encabezado : PROC ID   {   ambitos.agregarAmbito($2.sval);
                                declaracionID($2.sval, Main.PROCEDIMIENTO, null);
                            }
               | PROC error { Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;


proc_ni : NI '=' CTE    {   String cte = $3.sval;
                            if(!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                                Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                            else{
                            String lexemaID = getLexemaID();
			    if (lexemaID != null)
				TablaSimbolos.getToken(lexemaID).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        }
                        }
        | '=' CTE { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
        | NI error CTE { Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
        | NI '=' error { Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
        | CTE { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI y literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;

proc_cuerpo : '{' lista_sentencias '}'
            | lista_sentencias '}' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
            | '{' error '}' { Errores.addError(String.format("[AS] | Linea %d: Error en las sentencias del cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
            | '{' lista_sentencias error { Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
;

lista_parametros_formales   : '(' parametro_formal ',' parametro_formal ',' parametro_formal ')'    {   imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                                                                                                        String lexemaID = getLexemaID();
												    	if (lexemaID != null)
														TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                                        parametrosFormales.clear();
                                                                                                    }
                            | '(' parametro_formal ',' parametro_formal ')' {   imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                                                                                String lexemaID = getLexemaID();
										    if (lexemaID != null)
											TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                parametrosFormales.clear();
                                                                            }
                            | '(' parametro_formal ')'  {   imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                                                            String lexemaID = getLexemaID();
								if (lexemaID != null)
								    TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                            parametrosFormales.clear();
                                                        }
                            | '(' ')' { String lexemaID = getLexemaID();
					    if (lexemaID != null)
						TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales)); }
                            | '(' parametro_formal parametro_formal ',' parametro_formal ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea())); }
                            | '(' parametro_formal ',' parametro_formal parametro_formal ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea())); }
                            | '(' parametro_formal parametro_formal ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea())); }
                            | '(' parametro_formal ',' parametro_formal ',' parametro_formal ',' lista_parametros_formales ')' { Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea())); }
                            | '(' parametro_formal ',' error ')' { Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea())); }
;

parametro_formal    : tipo ID { imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                parametrosFormales.add(ultimoTipo + " " + $2.sval);
                                declaracionID($2.sval, Main.PARAMETRO, ultimoTipo);
                                TablaSimbolos.getToken($2.sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
                    | VAR tipo ID { imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                    parametrosFormales.add("VAR " + ultimoTipo + " " + $3.sval);
                                    declaracionID($3.sval, Main.PARAMETRO, ultimoTipo);
                                    TablaSimbolos.getToken($3.sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
                    | error ID {  Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n",analizadorLexico.getNroLinea())); }
                    | VAR error ID { Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n",analizadorLexico.getNroLinea())); }
;

sentencia_seleccion : IF condicion_if THEN bloque_then END_IF ';'   {   imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
									                                        polaca.addElem(new ElemPos(polaca.size()), true);
									                                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                                                        }
                                                                        else {
                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                                                        }
									                                }
                    | IF condicion_if THEN bloque_then ELSE bloque_else END_IF ';'  {   imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                                                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                                                                            polaca.addElem(new ElemPos(polaca.size()), true);
                                                                                            polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                                                                        }
                                                                                        else {
                                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                                                                        }
                    									                            }
                    | IF condicion_if  bloque_then END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
                    | IF condicion_if  bloque_then ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
                    | IF condicion_if THEN bloque_then bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN error END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN error ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN bloque_then END_IF  { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
		    | IF condicion_if  THEN bloque_then error  { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n",nroUltimaLinea));}
		    | IF condicion_if  THEN bloque_then ELSE END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN bloque_then ELSE error END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else error { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n",analizadorLexico.getNroLinea()));}
		    | IF condicion_if  THEN bloque_then ELSE bloque_else END_IF  { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
		    | IF THEN bloque_then END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
		    | IF THEN bloque_then ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
;

condicion_if    : '(' condicion ')' {   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                            polaca.pushPos(true);
                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                                        }
                                        else {
                                            polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF), false);
                                        }
                                    }
		| condicion ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
		| '(' condicion { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
		| '(' ')' { Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n",analizadorLexico.getNroLinea()));}
		| condicion { Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n",analizadorLexico.getNroLinea()));}
		| '(' error ')' { Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n",analizadorLexico.getNroLinea()));}
;


bloque_then : bloque_sentencias {   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                        polaca.addElem(new ElemPos(polaca.size()+2),true);
                                        polaca.pushPos(true);
                                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI),false);
                                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                    }
                                    else {
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())+2),true);
                                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BI),false);
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                    }
                                }
;

bloque_else: bloque_sentencias
;

sentencia_control   : FOR '(' inicio_for condicion_for  incr_decr CTE ')' bloque_for    {   SA3($6.sval);
                                                                                                SA4($3.sval , $4.sval);
                                                                                                SA5($3.sval, $6.sval, $5.sval); // id cte incr_decr
                                                                                                if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                                                                                    polaca.addElem(new ElemPos(polaca.size() +2 ),true);
                                                                                                    polaca.addElem(new ElemPos(polaca.popPos()),false);
                                                                                                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI),false);
                                                                                                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                                                                                }
                                                                                                else {
                                                                                                    polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos()) +2 ),true);
                                                                                                    polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.popPos(ambitos.getAmbitos())),false);
                                                                                                    polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BI),false);
                                                                                                    polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                                                                                }
                                                                                            }
                    | FOR  inicio_for condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
                    | FOR '(' error condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n",analizadorLexico.getNroLinea()));}
                    | FOR '(' inicio_for error ';' incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n",analizadorLexico.getNroLinea()));  }
                    | FOR '(' inicio_for condicion_for  error CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n",analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for condicion_for  incr_decr error ')' bloque_for {  Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n",analizadorLexico.getNroLinea()));  }
                    | FOR '(' inicio_for condicion_for  incr_decr CTE  bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
                    | FOR '(' inicio_for condicion_for  incr_decr CTE ')' sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea()));}
		    | FOR '('';' condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));}
;

inicio_for	: ID '=' cte ';' { $$ = $1;
			SA3($3.sval);
			SA1($3.sval);
			SA1($1.sval);
			SA2($2.sval);
			SA1($1.sval);
			invocacionID($1.sval, Main.VARIABLE);
			if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
			    polaca.pushPos(false);
			    polaca.addElem(new EtiquetaElem(polaca.size()), false);
			}
			else {
			    polacaProcedimientos.pushPos(ambitos.getAmbitos(), false);
			    polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
			}
		    }
		| error '=' CTE ';'{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n",analizadorLexico.getNroLinea()));}
		| ID '=' error ';'{ Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n",analizadorLexico.getNroLinea()));}
		| ID error { Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n",analizadorLexico.getNroLinea()));}
		| '=' CTE ';' { Errores.addError(String.format("[AS] | Linea %d: Falta el identificador de control %n",analizadorLexico.getNroLinea()));}
		| ID CTE ';'{ Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n",analizadorLexico.getNroLinea()));}
		| ID '=' ';'{ Errores.addError(String.format("[AS] | Linea %d: Falta la constante de la asignacion %n",analizadorLexico.getNroLinea()));}
;

bloque_for	: '{' bloque_ejecutable '}'
		| sentencia_ejecutable
		| error bloque_ejecutable '}' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n",analizadorLexico.getNroLinea()));}
		| '{' bloque_ejecutable error { Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n",nroUltimaLinea));}
;

condicion_for : condicion ';'   { $$ =$1;
  				  if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                        polaca.pushPos(true);
                                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                    else {
                                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                }
;

incr_decr   : UP { $$ = new ParserVal("+"); }
            | DOWN { $$ = new ParserVal("-"); }
;

sentencia_salida    : OUT '(' CADENA_MULT ')' ';' {   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
						  SA1($3.sval);
						  if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
						      polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
						  else
						      polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
					      }
                    | OUT error CADENA_MULT ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
                    | OUT '(' ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n",analizadorLexico.getNroLinea()));}
                    | OUT '(' error ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n",analizadorLexico.getNroLinea()));}
                    | OUT '(' CADENA_MULT error ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
                    | OUT '(' CADENA_MULT ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
                    | OUT '(' factor ')' ';'            {   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
//                                                        invocacionID($3.sval, Main.VARIABLE);
//                                                        SA1($3.sval);
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }

		     | OUT '(' factor ')'  {Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}


;

sentencia_asignacion    : ID '=' expresion ';'  {   imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                                                    String id = $1.sval;
                                                    Token token = TablaSimbolos.getToken(id);
                                                    if(token != null) {
                                                        SA1(id);
                                                        SA2($2.sval);
                                                    }
                                                    invocacionID(id, Main.VARIABLE);
                                                }
                        | error '=' expresion ';' { Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n",analizadorLexico.getNroLinea()));}
                        | ID error expresion ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n",analizadorLexico.getNroLinea()));}
                        | ID '=' error ';' { Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n",analizadorLexico.getNroLinea()));}
                        | ID '=' expresion { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
;

sentencia_invocacion    : ID '(' lista_parametros ')' ';'   {   imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                                                                invocacionID($1.sval, Main.PROCEDIMIENTO);
                                                            }
                        | ID '(' ')' ';'    {   imprimirReglaReconocida("Sentencia de invocación sin parámetros", analizadorLexico.getNroLinea());
                                                invocacionID($1.sval, Main.PROCEDIMIENTO);
                                            }
                        | ID lista_parametros ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
                        | ID ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
                        | ID '(' error ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n",analizadorLexico.getNroLinea()));}
                        | ID '(' lista_parametros  ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
                        | ID '('  ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
                        | ID '(' lista_parametros ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
                        | ID '(' ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
;

lista_parametros    : ID ',' ID ',' ID  {   imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                                            invocacionID($1.sval, Main.PARAMETRO);
                                            invocacionID($3.sval, Main.PARAMETRO);
                                            invocacionID($5.sval, Main.PARAMETRO);
                                        }
                    | ID ',' ID {   imprimirReglaReconocida("Lista de parámetros (2)", analizadorLexico.getNroLinea());
                                    invocacionID($1.sval, Main.PARAMETRO);
                                    invocacionID($3.sval, Main.PARAMETRO);
                                }
                    | ID    {   imprimirReglaReconocida("Lista de parámetros (1)", analizadorLexico.getNroLinea());
                                invocacionID($1.sval, Main.PARAMETRO);
                            }
                    | ID ',' ID ',' ID ',' error { Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n",analizadorLexico.getNroLinea()));}
                    | ID ',' error { Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n",analizadorLexico.getNroLinea()));}
                    | ID ID ID { Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
                    | ID ',' ID ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
                    | ID ID ',' ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
                    | ID ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
;



condicion   : expresion MAYOR_IGUAL expresion {$$ = $1; SA2(">=");}
		| expresion MENOR_IGUAL expresion{$$ = $1;SA2("<=");}
		| expresion '>' expresion {$$ = $1;SA2(">");}
		| expresion '<' expresion {$$ = $1;SA2("<");}
		| expresion IGUAL expresion {$$ = $1;SA2("==");}
		| expresion DISTINTO  expresion {$$ = $1;SA2("!=");}
;

expresion   : expresion '+' termino {$$=$1;imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                                     SA2($2.sval);}
            | expresion '-' termino {$$=$1;imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                                     SA2($2.sval);}
            | termino {$$=$1;}
            | expresion '+' error { Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la suma %n",analizadorLexico.getNroLinea()));}
            | expresion '-' error { Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la resta %n",analizadorLexico.getNroLinea()));}
            | error '+' termino { Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la suma %n",analizadorLexico.getNroLinea()));}
;

termino : termino '*' factor { $$ = $1;
				imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
				SA2($2.sval);}
        | termino '/' factor { $$ = $1;
        			imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
        			SA2($2.sval);}
        | factor {$$ = $1;}
        | termino '*' error { Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
        | termino '/' error { Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la division %n",analizadorLexico.getNroLinea()));}
        | error '*' factor { Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
        | error '/' factor { Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la division %n",analizadorLexico.getNroLinea()));}
;

factor  : ID {$$ = $1; SA1($1.sval);
		invocacionID($1.sval, Main.VARIABLE);}
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
      		  	imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                              		        }
     	 	}
;

%%

private AnalizadorLexico analizadorLexico;
private int nroUltimaLinea;
private Ambitos ambitos;
private PolacaInversa polaca;
private boolean verbose;
private PolacaInversaProcedimientos polacaProcedimientos;

private String ultimoTipo;
private List<String> parametrosFormales = new ArrayList<>();
private List<String> parametrosReales = new ArrayList<>();
private int numberOfProcs = 0;

private boolean actualizarTablaSimbolos;

public Parser(AnalizadorLexico analizadorLexico, boolean actualizarTablaSimbolos, PolacaInversa polaca, PolacaInversaProcedimientos polacaProcedimientos, boolean verbose){
	this.analizadorLexico = analizadorLexico;
	this.ambitos = new Ambitos();
	this.actualizarTablaSimbolos = actualizarTablaSimbolos;
	this.polaca = polaca;
	this.polacaProcedimientos = polacaProcedimientos;
	this.verbose = verbose;
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
}

private void imprimirReglaReconocida(String descripcion, int lineaCodigo) {
    if(verbose)
        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: %s %n" + Main.ANSI_RESET, lineaCodigo, descripcion);
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

public String checkPositivo(String cte) {
	Token token = TablaSimbolos.getToken(cte);
    String tipo = (String) token.getAtributo("tipo");
    if(tipo.equals("LONGINT")) {
        long entero = 0;
		if (Long.parseLong(cte) >= Main.MAX_LONG) {
		    entero = Main.MAX_LONG - 1;
		     Errores.addWarning(String.format("[AS] | Linea %d: Entero largo positivo fuera de rango: %s - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
		    String nuevoLexema = String.valueOf(entero);
		    cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
		    return nuevoLexema;
		}
	}
	return cte;
}



public String checkRango(String cte) {
	Token token = TablaSimbolos.getToken(cte);
	String tipo = (String) token.getAtributo("tipo");

	if (tipo.equals("LONGINT")) {
	    long entero = 0;
	    String nuevoLexema = null;
		if (Long.parseLong(cte) <= Main.MAX_LONG) {
		    entero = Long.parseLong(cte);
		} else {
		    entero = Main.MAX_LONG;
		     Errores.addWarning(String.format("[AS] | Linea %d: Entero largo negativo fuera de rango: %d - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
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
		 Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: %s - Se cambia por: %f %n", analizadorLexico.getNroLinea(), cte, flotante));
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
	if (cont == 0)
	  TablaSimbolos.remove(cte);
	else
	  TablaSimbolos.getToken(cte).addAtributo("contador", cont);
	if (!TablaSimbolos.existe(nuevoLexema)) {
	    Token nuevoToken = new Token(token.getIdToken(), Main.CONSTANTE, nuevoLexema);
	    nuevoToken.addAtributo("contador", 1);
	    nuevoToken.addAtributo("tipo", tipo);
	    TablaSimbolos.add(nuevoToken);
	} else {
	    cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1;
	    TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
	}
}

public void actualizarContadorID(String lexema, boolean decremento) {
    Token token = TablaSimbolos.getToken(lexema);
    int cont = (decremento) ? (Integer)(token.getAtributo("contador")) - 1 : (Integer) (token.getAtributo("contador")) + 1;
    if(cont == 0)
        TablaSimbolos.remove(lexema);
    else {
        TablaSimbolos.getToken(lexema).removeAtributo("contador");
        TablaSimbolos.getToken(lexema).addAtributo("contador", cont);
    }
}

public void declaracionID(String lexema, String uso, String tipo) {
    Token token = TablaSimbolos.getToken(lexema);
    actualizarContadorID(lexema, true);
    String nuevoLexema = "";
    if(uso.equals(Main.PROCEDIMIENTO))
      nuevoLexema = lexema + "@" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length())-(lexema.length()+1));
    else
      nuevoLexema = lexema + "@" + ambitos.getAmbitos();

    if(!TablaSimbolos.existe(nuevoLexema)) {
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
        nuevoToken.addAtributo("uso", uso);
        nuevoToken.addAtributo("tipo", tipo);
        nuevoToken.addAtributo("contador", 0);
        nuevoToken.addAtributo("ambito", nuevoLexema.substring(lexema.length()+1, nuevoLexema.length()));
        if(uso.equals(Main.PROCEDIMIENTO)) {
            this.numberOfProcs++;
            nuevoToken.addAtributo("numeroProc", numberOfProcs);
        }
        TablaSimbolos.add(nuevoToken);
    }
    else {
        String usoTokenExistente = TablaSimbolos.getToken(nuevoLexema).getAtributo("uso")+"";
        if(uso.equals(Main.VARIABLE)) {
            if(usoTokenExistente.equals(Main.VARIABLE))
                Errores.addError(String.format("[ASem] | Linea %d: Variable redeclarada %n", analizadorLexico.getNroLinea()));
            else if(usoTokenExistente.equals(Main.PROCEDIMIENTO))
                Errores.addError(String.format("[ASem] | Linea %d: El nombre de la variable declarada pertenece a un procedimiento declarado en el mismo ambito %n", analizadorLexico.getNroLinea()));
        }
        else if(uso.equals(Main.PROCEDIMIENTO)) {
            if(usoTokenExistente.equals(Main.PROCEDIMIENTO))
                Errores.addError(String.format("[ASem] | Linea %d: Procedimiento redeclarado %n", analizadorLexico.getNroLinea()));
            else if(usoTokenExistente.equals(Main.VARIABLE))
                Errores.addError(String.format("[ASem] | Linea %d: El nombre del procedimiento declarado pertenece a una variable declarada en el mismo ambito %n", analizadorLexico.getNroLinea()));
        }
    }
}

public String getAmbitoDeclaracionID(String lexema, String uso) {
    // Se chequea que el id esté declarado en el ámbito actual o en los ámbitos que lo contienen
    String ambitosString = ambitos.getAmbitos();
    ArrayList<String> ambitosList = new ArrayList<>(Arrays.asList(ambitosString.split("@")));

//    if(uso.equals(Main.PROCEDIMIENTO) && !ambitosString.equals("main")) {
//        ambitosString = ambitosString.substring(0, (ambitosString.length()) - (ambitosList.get(ambitosList.size() - 1).length() + 1));
//        ambitosList.remove(ambitosList.size()-1);
//    }

    boolean declarado = false;
    while(!ambitosList.isEmpty()) {
      if(TablaSimbolos.existe(lexema + "@" + ambitosString)) {
        if((uso.equals(Main.PARAMETRO) && !TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals(Main.PROCEDIMIENTO)) || !uso.equals(Main.PARAMETRO)) {
          declarado = true;
          //if(!uso.equals(Main.PROCEDIMIENTO))
          //  actualizarContadorID(lexema + "@" + ambitosString, false);
          break;
        }
        else if (TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals(Main.PROCEDIMIENTO)) {
	    Errores.addError(String.format("[ASem] | Linea %d: No es posible pasar un procedimiento como parametro %n", analizadorLexico.getNroLinea()));
	    break;
	}
      }
      else {
        if(!ambitosString.equals("main")) {
          ambitosString = ambitosString.substring(0, (ambitosString.length()-(ambitosList.get(ambitosList.size()-1).length()+1)));
          ambitosList.remove(ambitosList.size()-1);
        }
        else break;
      }
    }

    if(!declarado) ambitosString = "";

    return ambitosString;
}

public void invocacionID(String lexema, String uso) {
    String ambitosString = getAmbitoDeclaracionID(lexema, uso);
    boolean declarado = (ambitosString.isEmpty()) ? false : true;

	if (!declarado) {
	    if (uso.equals(Main.VARIABLE))
		Errores.addError(String.format("[ASem] | Linea %d: Variable %s no declarada %n", analizadorLexico.getNroLinea(), lexema));
	    else if (uso.equals(Main.PROCEDIMIENTO))
        Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
        else if (uso.equals(Main.PARAMETRO))
        Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
	}

    if(uso.equals(Main.PARAMETRO))
        parametrosReales.add(lexema + "@" + ambitosString);

    if(uso.equals(Main.PROCEDIMIENTO) && declarado) {
        Token procedimiento = TablaSimbolos.getToken(lexema + "@" + ambitosString);

        Token padre = null;
        if(!ambitosString.equals("main")) {
            String lexemaPadre = ambitosString.split("@")[ambitosString.split("@").length-1];
            lexemaPadre = lexemaPadre + "@" + ambitosString.substring(0, (ambitosString.length())-(lexemaPadre.length()+1));
            padre = TablaSimbolos.getToken(lexemaPadre);
        }
        procedimiento.addAtributo("padre", (padre != null) ? padre.getAtributo("numeroProc") : 0);

        // Si se trata de un procedimiento que se encuentra declarado, se chequea además que la cantidad de parámetros reales correspondan a los formales
        List<String> parametrosFormales = (List) procedimiento.getAtributo("parametros");
        if(parametrosFormales == null) return;
        if (parametrosReales.size() != parametrosFormales.size())
            Errores.addError(String.format("[ASem] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
        else {
            // Se chequea, por último, los tipos entre parámetros reales y formales
            boolean tiposCompatibles = true;
            for(int i = 0; i < parametrosReales.size(); i++) {
                Token parametroReal = TablaSimbolos.getToken(parametrosReales.get(i));
                if(parametroReal != null) {
                  String tipoParametroReal = parametroReal.getAtributo("tipo")+"";
                  if(!parametrosFormales.get(i).contains(tipoParametroReal)) {
                    Errores.addError(String.format("[ASem] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i+1));
                    tiposCompatibles = false;
                    break;
                  }
                }
                else
                  tiposCompatibles = false;
            }

            if(tiposCompatibles) {
                SA6(lexema, parametrosFormales, parametrosReales);
                actualizarContadorID(lexema + "@" + ambitosString, false);
          }
        }
        parametrosReales.clear();
    }

    if(declarado && !uso.equals(Main.PROCEDIMIENTO))
      actualizarContadorID(lexema + "@" + ambitosString, false);
    // Se actualiza el contador de referencias
    actualizarContadorID(lexema, true);
}

public String getLexemaID() {
    String ambitosActuales = ambitos.getAmbitos();
    String id = ambitosActuales.split("@")[ambitosActuales.split("@").length-1];
    return(id + "@" + ambitosActuales.substring(0, (ambitosActuales.length())-(id.length()+1)));
}


public void SA1(String lexema) {  // Añadir factor a la polaca
    String ambitosActuales = ambitos.getAmbitos();

    Token token = TablaSimbolos.getToken(lexema + "@" + getAmbitoDeclaracionID(lexema, Main.VARIABLE));

    if (token == null)
        token = TablaSimbolos.getToken(lexema);

    String tipo = token.getTipoToken();
    ElemSimple elem;
    // Se añade a la polaca el token sin el ámbito en el lexema
    if (tipo.equals(Main.CONSTANTE) || tipo.equals(Main.CADENA)) //en constantes y cadenas me quedo con el token original, total no se modifica
	elem = new ElemSimple(token, analizadorLexico.getNroLinea());
    else{
      String lexemaToken = token.getLexema(false);
      if(token.getAtributo("uso") != null && token.getAtributo("uso").equals(Main.PROCEDIMIENTO)) {
        String id = lexemaToken.split("@")[0];
        lexemaToken = lexemaToken.replace(id+"@", "");
        lexemaToken += "@"+id;
      }
      Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), (lexemaToken.contains("@") && !token.getAtributo("uso").equals(Main.PROCEDIMIENTO)) ? lexemaToken.substring(0, lexemaToken.indexOf("@")) : lexemaToken);
      for (Map.Entry<String, Object> atributo : token.getAtributos().entrySet()) {
        nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
      }
      elem = new ElemSimple(nuevoToken, analizadorLexico.getNroLinea());
    }

    if(ambitosActuales.equals(Ambitos.ambitoGlobal))
        polaca.addElem(elem, false);
    else
        polacaProcedimientos.addElem(ambitosActuales, elem, false);
}

public void SA2(String operador){ // Añadir operador binario a la polaca
    String ambitosActuales = ambitos.getAmbitos();
    OperadorBinario elem = new OperadorBinario(operador, analizadorLexico.getNroLinea());

    if(ambitosActuales.equals(Ambitos.ambitoGlobal))
        polaca.addElem(elem, false);
    else
        polacaProcedimientos.addElem(ambitosActuales, elem, false);
}

public void SA3(String cte){ //chequea que la constante sea LONGINT
	Token cte_t = TablaSimbolos.getToken(cte);
        if (cte_t != null)
	 if (!cte_t.getAtributo("tipo").equals("LONGINT"))
 		 Errores.addError(String.format("[ASem] | Linea %d: Constante no es del tipo entero %n",analizadorLexico.getNroLinea()));
}

public void SA4(String id1, String id2){ //reviso que la variable inicializada en el for sea la misma que la de la condicion
	Token token1 = TablaSimbolos.getToken(id1 + "@" + getAmbitoDeclaracionID(id1, Main.VARIABLE));
        Token token2 = TablaSimbolos.getToken(id2 + "@" + getAmbitoDeclaracionID(id2, Main.VARIABLE));
        if (token1 != null && token2 != null)
	if (!token1.equals(token2))
		 Errores.addError(String.format("[AS] | Linea %d: En la sentencia for, la variable inicializada "+ id1 + " no es la misma que la variable utilizada en la condicion %n",analizadorLexico.getNroLinea()));
}

public void SA5(String id, String cte, String op){ //incremento o decremento la variable del for
	Token id_t = TablaSimbolos.getToken(id+"@" + getAmbitoDeclaracionID(id, Main.VARIABLE));
	Token cte_t = TablaSimbolos.getToken(cte);
	if (id_t != null) {
	String lexemaToken = id_t.getLexema(false);
	Token nuevoToken = new Token(id_t.getIdToken(), id_t.getTipoToken(), lexemaToken.substring(0, lexemaToken.indexOf("@")));
	for (Map.Entry<String, Object> atributo : id_t.getAtributos().entrySet()) {
	    nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
	}

	    if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
		polaca.addElem(new ElemSimple(nuevoToken), false);
		polaca.addElem(new ElemSimple(cte_t), false);
		polaca.addElem(new OperadorBinario(op), false);
		polaca.addElem(new ElemSimple(nuevoToken), false);
		polaca.addElem(new OperadorBinario("="), false);
	    }
	    else {
		polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(nuevoToken), false);
		polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(cte_t), false);
		polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorBinario(op), false);
		polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(nuevoToken), false);
		polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorBinario("="), false);
	    }
	   }

}

public void SA6(String lexema, List<String> parametrosFormales, List<String> parametrosReales) { // invocacion a procedimientos
    List<String> parametrosCVR = new ArrayList<>();

    for(int i = 0; i < parametrosFormales.size(); i++) {
        String parametroFormal = parametrosFormales.get(i);

        parametroFormal = parametroFormal.replace("LONGINT ", "");
        parametroFormal = parametroFormal.replace("FLOAT ", "");

        if(parametroFormal.contains("VAR ")) {
          parametroFormal = parametroFormal.replace("VAR ", "");
          parametrosCVR.add(parametroFormal + "@" + parametrosReales.get(i));
        }

        parametroFormal = parametroFormal + "@" + getAmbitoDeclaracionID(lexema, Main.PROCEDIMIENTO) + "@" + lexema;
        SA1(parametrosReales.get(i));
        SA1(parametroFormal);
        SA2("=");
    }

    SA1(lexema);
    if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.INV),false);
    else
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.INV),false);

    if(!parametrosCVR.isEmpty()) {
      for (String parametroCVR: parametrosCVR
           ) {
        String[] param = parametroCVR.split("@");
        SA1(param[0] + "@" + getAmbitoDeclaracionID(lexema, Main.PROCEDIMIENTO) + "@" + lexema);
        SA1(param[1]);
        SA2("=");
      }
    }
}
