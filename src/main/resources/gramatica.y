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

declaracion_variables    : tipo lista_variables ';'
;

lista_variables    : ID ',' lista_variables
        | ID
;

tipo    : LONGINT
    | FLOAT
;

declaracion_procedimiento : PROC ID '(' lista_parametros_formales')' NI '=' cte '{' cuerpo '}'
                      | PROC ID '(' ')' NI '=' cte '{' cuerpo '}'
;

lista_parametros_formales     : parametro_formal ',' parametro_formal ',' parametro_formal
                 | parametro_formal ',' parametro_formal
                | parametro_formal
;

parametro_formal    : tipo ID
            | VAR tipo ID



sentencia_seleccion    : IF '(' condicion ')' THEN bloque_sentencias END_IF ';'
            | IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'
;

sentencia_control    : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'
            | FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')'  sentencia_ejecutable
;

incr_decr    : UP
        | DOWN
;

sentencia_salida : OUT'('CADENA_MULT')'
;

sentencia_asignacion : ID '=' factor
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
	System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
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