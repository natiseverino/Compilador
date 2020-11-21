%{
package compilador;
import compilador.codigoIntermedio.*;
import java.util.Arrays;
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
                    | sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa", analizadorLexico.getNroLinea())); }
                    | bloque_ejecutable sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa", analizadorLexico.getNroLinea())); }
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
                        | tipo lista_variables error { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';'", nroUltimaLinea)); }
                        | tipo error ';' { Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables", analizadorLexico.getNroLinea())); }
;

lista_variables : ID ',' lista_variables { declaracionID($1.sval, "Variable", ultimoTipo); }
                | ID { declaracionID($1.sval, "Variable", ultimoTipo); }
                | ID lista_variables { Errores.addError(String.format("[AS] | Linea %d: Falta literal ','" + Main.ANSI_RESET, analizadorLexico.getNroLinea())); }
;

tipo    : LONGINT { ultimoTipo = "LONGINT"; }
        | FLOAT { ultimoTipo = "FLOAT"; }
;

declaracion_procedimiento   : proc_encabezado proc_parametros proc_ni proc_cuerpo   {   imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                                                                                        ambitos.eliminarAmbito(actualizarTablaSimbolos);
                                                                                    }
;

proc_encabezado : PROC ID   {   ambitos.agregarAmbito($2.sval);
                                declaracionID($2.sval, "Procedimiento", null);
                            }
                | error ID { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                | PROC error { Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;

proc_parametros : '(' lista_parametros_formales ')' {   TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                        parametrosFormales.clear();
                                                    }
                | '(' ')' { TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales)); }
                | error lista_parametros_formales ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                | error ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                | '(' error ')' { Errores.addError(String.format("[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                | '(' lista_parametros_formales { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
                | '(' error { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;

proc_ni : NI '=' CTE    {   String cte = $3.sval;
                            if(!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                                System.out.printf( Main.ANSI_RED + "[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                int cteInt = Integer.parseInt(cte);
                                TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                                if(cteInt < 1 || cteInt > 4)
                                    Errores.addError(String.format("[ASem] | Linea %d: NI no se encuentra en el intervalo [1,4] %n", analizadorLexico.getNroLinea()));
                        }
        | '=' CTE { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
        | NI error CTE { Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
        | NI '=' error { Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
;

proc_cuerpo : '{' lista_sentencias '}'
            | error lista_sentencias '}'
            | '{' error '}'
            //| '{' lista_sentencias error
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
                                parametrosFormales.add(ultimoTipo + " " + $2.sval);
                                declaracionID($2.sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken($2.sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
                    | VAR tipo ID { System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                    parametrosFormales.add("VAR " + ultimoTipo + " " + $3.sval);
                                    declaracionID($3.sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken($3.sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
                    | error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                    | VAR error ID {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
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
                    //| IF condicion_if bloque_then END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN %n", analizadorLexico.getNroLinea())); }
                    //| IF condicion_if bloque_then ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN %n", analizadorLexico.getNroLinea())); }
                    //| IF condicion_if THEN bloque_then  bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE ", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN error END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN error ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN bloque_then END_IF { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';'", nroUltimaLinea)); }
		            //| IF condicion_if THEN bloque_then error { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF y literal ';'", nroUltimaLinea)); }
		            //| IF condicion_if THEN bloque_then ELSE END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN bloque_then ELSE error END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN bloque_then ELSE bloque_else error { Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF", analizadorLexico.getNroLinea())); }
		            //| IF condicion_if THEN bloque_then ELSE bloque_else END_IF  { Errores.addError(String.format("[AS] | Linea %d: Falta literal ';'", nroUltimaLinea)); }
		            //| IF THEN bloque_then END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF ", nroUltimaLinea)); }
		            //| IF THEN bloque_then ELSE bloque_else END_IF ';' { Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF ", nroUltimaLinea)); }
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
		        | condicion ')' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '('", analizadorLexico.getNroLinea())); }
		        | '(' condicion { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')'", analizadorLexico.getNroLinea())); }
		        | '(' ')' { Errores.addError(String.format("[AS] | Linea %d: Falta condicion", analizadorLexico.getNroLinea())); }
		        | condicion { Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis", analizadorLexico.getNroLinea())); }
		        | '(' error ')' { Errores.addError(String.format("[AS] | Linea %d: Error en la condicion", analizadorLexico.getNroLinea())); }
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

sentencia_control   : FOR '(' inicio_for ';' condicion_for  incr_decr CTE ')' bloque_for    {   SA3($7.sval);
                                                                                                SA4($3.sval , $5.sval);
                                                                                                SA5($3.sval, $7.sval, $6.sval); // id cte incr_decr
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
                    | FOR  inicio_for ';' condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta literal '('", analizadorLexico.getNroLinea())); }
                    | FOR '(' error ';' condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control", analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control", analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for ';' condicion_for  error CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for ';' condicion_for  incr_decr error ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for ';' condicion_for  incr_decr CTE  bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')'", analizadorLexico.getNroLinea())); }
                    | FOR '(' inicio_for ';' condicion_for  incr_decr CTE ')' sentencia_declarativa { Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea())); }
		            | FOR '('';' condicion_for  incr_decr CTE ')' bloque_for { Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control", analizadorLexico.getNroLinea())); }
;

inicio_for  : ID '=' CTE    {   SA3($3.sval);
                                SA1($1.sval);
                                SA1($3.sval);
                                SA2($2.sval);
                                if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                    polaca.pushPos(false);
                                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                }
                                else {
                                    polacaProcedimientos.pushPos(ambitos.getAmbitos(), false);
                                    polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                }
                            }
		    | error '=' CTE { Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control", analizadorLexico.getNroLinea())); }
		    | ID error CTE { Errores.addError(String.format("[AS] | Linea %d: Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea())); }
		    | ID '=' error { Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion", analizadorLexico.getNroLinea())); }
		    | ID error { Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control", analizadorLexico.getNroLinea())); }

;

bloque_for	: '{' bloque_ejecutable '}'
		    | sentencia_ejecutable
;

condicion_for : condicion ';'   {   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
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

sentencia_salida    : OUT '(' CADENA_MULT ')' ';'   {   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        SA1($3.sval);
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
                    | OUT '(' ID ')' ';'            {   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        // TODO: ver como hacer SA1 cuando el id no está declarado o cuando está renombrado con el ámbito
                                                        SA1($3.sval);
                                                        invocacionID($3.sval, "Variable");
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
                    | OUT '(' CTE ')' ';'           {   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        SA1($3.sval);
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
                    | error '(' CADENA_MULT ')' ';' { Errores.addError(String.format("Falta palabra reservada OUT en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    |  '(' CADENA_MULT ')' ';' { Errores.addError(String.format("Falta palabra reservada OUT en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    | OUT error CADENA_MULT ')' ';' { Errores.addError(String.format("Falta literal '(' en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    | OUT '(' ')' ';' { Errores.addError(String.format("Falta cadena multilínea a imprimir en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    | OUT '(' error ')' ';' { Errores.addError(String.format("Error en la cadena multilínea a imprimir en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    | OUT '(' CADENA_MULT error ';' { Errores.addError(String.format("Falta literal ')' en sentencia de salida %n", analizadorLexico.getNroLinea())); }
                    | OUT '(' CADENA_MULT ')' { Errores.addError(String.format("Falta literal ';' en sentencia de salida %n", nroUltimaLinea)); }
;

sentencia_asignacion    : ID '=' expresion ';'  {   imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                                                    String id = $1.sval;
                                                    String cte = $3.sval;
                                                    Token token = TablaSimbolos.getToken(id);
                                                    if(token != null) {
                                                        // TODO: hay que agregar el valor en la TS?
                                                        token.addAtributo("VALOR", cte);
                                                        SA1(id);
                                                        SA2($2.sval);
                                                    }
                                                    invocacionID(id, "Variable");
                                                }
                        | error '=' expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID error expresion ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' error ';' {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
                        | ID '=' expresion {System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
;

sentencia_invocacion    : ID '(' lista_parametros ')' ';'   {   imprimirReglaReconocida("Sentencia de invocación con lista de parámetros %n", analizadorLexico.getNroLinea());
                                                                invocacionID($1.sval, "Procedimiento");
                                                            }
                        | ID '(' ')' ';'    {   imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                                                invocacionID($1.sval, "Procedimiento");
                                            }
                        | '(' lista_parametros ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n", analizadorLexico.getNroLinea())); }
                        | '(' ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n", analizadorLexico.getNroLinea())); }
                        | ID lista_parametros ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
                        | ID ')' ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
                        | ID '(' error ')' ';' { Errores.addError(String.format("[AS] | Linea %d: | Parámetros inválidos %n", analizadorLexico.getNroLinea())); }
                        | ID '(' lista_parametros  ';' { Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
                        | ID '('  ';' { Errores.addError(String.format("[AS] | Linea %d: | Falta literal ')' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
                        | ID '(' lista_parametros ')' { Errores.addError(String.format("[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n", nroUltimaLinea)); }
                        | ID '(' ')' { Errores.addError(String.format("[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n", nroUltimaLinea)); }
;

lista_parametros    : ID ',' ID ',' ID  {   imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                                            invocacionID($1.sval, "Parametro");
                                            invocacionID($3.sval, "Parametro");
                                            invocacionID($5.sval, "Parametro");
                                        }
                    | ID ',' ID {   imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                                    invocacionID($1.sval, "Parametro");
                                    invocacionID($3.sval, "Parametro");
                                }
                    | ID    {   imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                                invocacionID($1.sval, "Parametro");
                            }
                    | ID ',' ID ',' ID ',' error { Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea())); }
                    | ID ',' error { Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea())); }
                    | ID ID ID { Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
                    | ID ',' ID ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
                    | ID ID ',' ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
                    | ID ID { Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
;


condicion   : expresion comparador expresion    {   imprimirReglaReconocida("Comparación", analizadorLexico.getNroLinea());
                                                    SA2($2.sval);
                                                }
	        | expresion error { Errores.addError(String.format("[AS] | Linea %d: Falta comparador %n", analizadorLexico.getNroLinea())); }
	        | expresion comparador error { Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando de la condición %n", analizadorLexico.getNroLinea())); }
	        | error comparador expresion { Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando de la condición %n", analizadorLexico.getNroLinea())); }
;

comparador  : MAYOR_IGUAL
            | MENOR_IGUAL
            | '>'
            | '<'
            | IGUAL
            | DISTINTO
;

expresion   : expresion '+' termino {   imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                                        SA2($2.sval);
                                    }
            | expresion '-' termino {   imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                                        SA2($2.sval);
                                    }
            | termino
            | expresion '+' error { Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n", analizadorLexico.getNroLinea())); }
            | expresion '-' error { Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n", analizadorLexico.getNroLinea())); }
            | error '+' termino { Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n", analizadorLexico.getNroLinea())); }
            | error '-' termino { Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la resta %n", analizadorLexico.getNroLinea())); }
;

termino : termino '*' factor    {   imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                                    SA2($2.sval);
                                }
        | termino '/' factor    {   imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
                                    SA2($2.sval);
                                }
        | factor
        | termino '*' error { Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n", analizadorLexico.getNroLinea())); }
        | termino '/' error { Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la división %n", analizadorLexico.getNroLinea())); }
        | error '*' factor { Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n", analizadorLexico.getNroLinea())); }
        | error '/' factor { Errores.addError(String.format("[AS] | Linea %d: | Falta el primer operando en la división %n", analizadorLexico.getNroLinea())); }
;

factor  : ID    {   SA1($1.sval);
                    invocacionID($1.sval, "Variable");
                }
        | cte   { SA1($1.sval); }
;

cte : CTE   {   String cte = $1.sval;
	            String nuevo = checkPositivo(cte);
	            if (nuevo != null)
	   	            $$ = new ParserVal(nuevo);
	            else
	   	            $$ = new ParserVal(cte);
            }

    | '-' CTE   {   String cte = $2.sval;
      		        String nuevo = checkRango(cte);
      		        if (nuevo != null) {
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
private PolacaInversaProcedimientos polacaProcedimientos;

private String ultimoTipo;
private List<String> parametrosFormales = new ArrayList<>();
private List<String> parametrosReales = new ArrayList<>();

private boolean actualizarTablaSimbolos;
private boolean verboseSintactico;

public Parser(AnalizadorLexico analizadorLexico, boolean actualizarTablaSimbolos, PolacaInversa polaca, PolacaInversaProcedimientos polacaProcedimientos, boolean verboseSintactico){
	this.analizadorLexico = analizadorLexico;
	this.ambitos = new Ambitos();
	this.actualizarTablaSimbolos = actualizarTablaSimbolos;
	this.polaca = polaca;
	this.polacaProcedimientos = polacaProcedimientos;
	this.verboseSintactico = verboseSintactico;
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
}

private void imprimirReglaReconocida(String descripcion, int lineaCodigo) {
    if(verboseSintactico)
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
    String tipo = token.getAtributo("tipo")+"";
    if(tipo.equals("LONGINT")) {
        long entero = 0;
		if (Long.parseLong(cte) >= Main.MAX_LONG) {
		    entero = Main.MAX_LONG - 1;
		    Errores.addWarning(String.format("[AS] | Linea %d: Entero largo positivo fuera de rango: " + cte + " - Se cambia por: " + entero + "%n", analizadorLexico.getNroLinea()));
		    String nuevoLexema = String.valueOf(entero);
		    cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
		    return nuevoLexema;
		}
	}
	return cte;
}

public void cambiarSimbolo(Token token, String cte, String nuevoLexema, String tipo) {
	int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
	if (cont == 0)
	    TablaSimbolos.remove(cte);
	else
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

public String checkRango(String cte) {
	Token token = TablaSimbolos.getToken(cte);
	String tipo = token.getAtributo("tipo")+"";

	if(tipo.equals("LONGINT")) {
	    long entero = 0;
	    String nuevoLexema = null;
		if (Long.parseLong(cte) <= Main.MAX_LONG) {
		    entero = Long.parseLong(cte);
		} else {
		    entero = Main.MAX_LONG;
		    Errores.addWarning(String.format("[AS] | Linea %d: Entero largo negativo fuera de rango: " + cte + " - Se cambia por: " + entero + "%n", analizadorLexico.getNroLinea()));
		}
		nuevoLexema = "-" + entero;
		cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
		return nuevoLexema;

	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0f;
	    if((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
		    flotante = Float.parseFloat(cte);
	    } else {
		    flotante = Main.MAX_FLOAT-1;
		    Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: " + cte + " - Se cambia por: " + flotante + "%n", analizadorLexico.getNroLinea()));
	    }
	    if (flotante != 0f) {
		    String nuevoLexema = "-" + flotante;
		    cambiarSimbolo(token, cte, nuevoLexema, "FLOAT");
		    return nuevoLexema;
	    }
	}
	return null;
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
    if(uso.equals("Procedimiento"))
      nuevoLexema = lexema + ":" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length())-(lexema.length()+1));
    else
      nuevoLexema = lexema + ":" + ambitos.getAmbitos();

    if(!TablaSimbolos.existe(nuevoLexema)) {
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
        nuevoToken.addAtributo("uso", uso);
        nuevoToken.addAtributo("tipo", tipo);
        nuevoToken.addAtributo("contador", 0);
        TablaSimbolos.add(nuevoToken);
    }
    else {
        if(uso.equals("Variable"))
            Errores.addError(String.format("[GD] | Linea %d: Variable redeclarada %n", analizadorLexico.getNroLinea()));
        else if(uso.equals("Procedimiento"))
            Errores.addError(String.format("[GD] | Linea %d: Procedimiento redeclarado %n", analizadorLexico.getNroLinea()));
    }
}

public String getAmbitoDeclaracionID(String lexema, String uso) {
    // Se chequea que el id esté declarado en el ámbito actual o en los ámbitos que lo contienen
    String ambitosString = ambitos.getAmbitos();
    ArrayList<String> ambitosList = new ArrayList<>(Arrays.asList(ambitosString.split(":")));

    // Para el caso de la invocación a procedimientos se acota el ámbito actual para excluirlo como parte del ámbito al tener que estar declarado en un ámbito padre
    if(uso.equals("Procedimiento") && !ambitosString.equals("main"))
      ambitosString = ambitosString.substring(0, (ambitosString.length())-(ambitosList.get(ambitosList.size()-1).length()+1));

    boolean declarado = false;
    for(int i = 0; i < ambitosList.size(); i++) {
      if(TablaSimbolos.existe(lexema + ":" + ambitosString)) {
        if((uso.equals("Parametro") && !TablaSimbolos.getToken(lexema + ":" + ambitosString).getAtributo("uso").equals("Procedimiento")) || !uso.equals("Parametro")) {
          declarado = true;
          if(!uso.equals("Procedimiento"))
            actualizarContadorID(lexema + ":" + ambitosString, false);
          break;
        }
      }
      else if(!ambitosString.equals("main")) {
        ambitosString = ambitosString.substring(0, (ambitosString.length()-(ambitosList.get(ambitosList.size()-1).length()+1)));
        ambitosList.remove(ambitosList.size()-1);
      }
    }

    if(!declarado) ambitosString = "";

    return ambitosString;
}

public void invocacionID(String lexema, String uso) {
    String ambitosString = getAmbitoDeclaracionID(lexema, uso);
    boolean declarado = (ambitosString.isEmpty()) ? false : true;

    if(!declarado) {
        if(uso.equals("Variable"))
            Errores.addError(String.format("[GD] | Linea %d: Variable no declarada %n" , analizadorLexico.getNroLinea()));
        else if(uso.equals("Procedimiento"))
            Errores.addError(String.format("[GD] | Linea %d: Procedimiento no declarado %n", analizadorLexico.getNroLinea()));
            else if(uso.equals("Parametro"))
                Errores.addError(String.format("[GD] | Linea %d: Parametro real no declarado %n", analizadorLexico.getNroLinea()));
    }

    if(uso.equals("Parametro") && declarado)
        parametrosReales.add(lexema + ":" + ambitosString);

    if(uso.equals("Procedimiento") && declarado) {
        Token procedimiento = TablaSimbolos.getToken(lexema + ":" + ambitosString);

        // Si se trata de un procedimiento que se encuentra declarado, se chequea el número de invocaciones respecto del máximo permitido
        if(uso.equals("Procedimiento") && (((Integer) procedimiento.getAtributo("contador") + 1) > (Integer) procedimiento.getAtributo("max. invocaciones")))
            Errores.addError(String.format("[GD] | Linea %d: Se supera el máximo de invocaciones del procedimiento %n", analizadorLexico.getNroLinea()));
        else {
            // Si se trata de un procedimiento que se encuentra declarado, se chequea además que la cantidad de parámetros reales correspondan a los formales
            List<String> parametrosFormales = (List) procedimiento.getAtributo("parametros");
            if (parametrosReales.size() != parametrosFormales.size())
                Errores.addError(String.format("[GD] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
                // Se chequea, por último, los tipos entre parámetros reales y formales
                boolean tiposCompatibles = true;
                for(int i = 0; i < parametrosReales.size(); i++) {
                    String tipoParametroReal = TablaSimbolos.getToken(parametrosReales.get(i)).getAtributo("tipo")+"";
                    if(!parametrosFormales.get(i).contains(tipoParametroReal)) {
                        Errores.addError(String.format("[GD] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i+1));
                        tiposCompatibles = false;
                        break;
                    }
                }

                if(tiposCompatibles) {
                    SA6(lexema);
                    actualizarContadorID(lexema + ":" + ambitosString, false);
              }
            }
        }
        parametrosReales.clear();
    }

    // Se actualiza el contador de referencias
    actualizarContadorID(lexema, true);
}

public String getLexemaID() {
    String ambitosActuales = ambitos.getAmbitos();
    String id = ambitosActuales.split(":")[ambitosActuales.split(":").length-1];
    return(id + ":" + ambitosActuales.substring(0, (ambitosActuales.length())-(id.length()+1)));
}

public void SA1(String lexema) {  //añadir factor a la polaca
    String ambitosActuales = ambitos.getAmbitos();

    // Se obtiene el token para las constantes o cadenas
    Token token = TablaSimbolos.getToken(lexema);
    // Se obtiene el token para las variables
    /*if(token == null) {
        ambitosActuales = getAmbitoDeclaracionID(lexema, "Variable");
        token = TablaSimbolos.getToken(lexema + ":" + ambitosActuales);
    }*/

    // Se añade a la polaca el token sin el ámbito
    Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), (token.getLexema().contains(":")) ? token.getLexema().substring(0, lexema.indexOf(":")) : token.getLexema());
    ElemSimple elem = new ElemSimple(nuevoToken);
    if(ambitosActuales.equals(Ambitos.ambitoGlobal))
        polaca.addElem(elem, false);
    else
        polacaProcedimientos.addElem(ambitosActuales, elem, false);
}

public void SA2(String operador){ //añadir operador binario a la polaca
    String ambitosActuales = ambitos.getAmbitos();
    OperadorBinario elem = new OperadorBinario(operador);

    if(ambitosActuales.equals(Ambitos.ambitoGlobal))
        polaca.addElem(elem, false);
    else
        polacaProcedimientos.addElem(ambitosActuales, elem, false);
}

public void SA3(String cte){ //chequea que la constante sea LONGINT
	 if(!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
 		Errores.addError(String.format("[ASem] | Linea %d: Constante no es del tipo entero", analizadorLexico.getNroLinea()));
}

public void SA4(String id1, String id2){ //reviso que la variable inicializada en el for sea la misma que la de la condicion
	Token token1 = TablaSimbolos.getToken(id1);
	Token token2 = TablaSimbolos.getToken(id2);
	if (!token1.equals(token2))
		Errores.addError(String.format("[ASem] | Linea %d: En la sentencia for, la variable inicializada "+ id1 + "no es la misma que la variable utilizada en la condicion" ,analizadorLexico.getNroLinea()));
}

public void SA5(String id, String cte, String op){ //incremento o decremento la variable del for
	Token id_t = TablaSimbolos.getToken(id);
	Token cte_t = TablaSimbolos.getToken(cte);

    if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
        polaca.addElem(new ElemSimple(id_t), false);
        polaca.addElem(new ElemSimple(cte_t), false);
        polaca.addElem(new OperadorBinario(op), false);
        polaca.addElem(new ElemSimple(id_t), false);
        polaca.addElem(new OperadorBinario("="), false);
    }
    else {
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(id_t), false);
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(cte_t), false);
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorBinario(op), false);
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemSimple(id_t), false);
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorBinario("="), false);
    }

}

public void SA6(String lexema) { // invocacion a procedimientos
    // TODO: Falta acomodar el pasaje de parámetros cuando se invoca a un procedimiento
    SA1(lexema);
    if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.INV),false);
    else
        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.INV),false);
}
