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

sentencia_invocacion : ID '('lista_parametros')'
             | ID '(' ')'
;

lista_parametros     : parametro ',' parametro ',' parametro
             | parametro ',' parametro
            | parametro
;

parametro : factor
;

condicion    : expresion comparador expresion
;

comparador    : MAYOR_IGUAL
        | MENOR_IGUAL
        | '>'
        | '<'
        | IGUAL
        | DISTINTO
;

expresion       : expresion '+' termino
                | expresion '-' termino
                | termino
;

termino: termino '*' factor
        | termino '/' factor
        | factor
;

factor  : ID
        | cte
;

cte : CTE
      | '-' CTE
;

%%
