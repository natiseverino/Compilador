//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;
//#line 19 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IGUAL=257;
public final static short MAYOR_IGUAL=258;
public final static short MENOR_IGUAL=259;
public final static short DISTINTO=260;
public final static short IF=261;
public final static short THEN=262;
public final static short ELSE=263;
public final static short FOR=264;
public final static short END_IF=265;
public final static short OUT=266;
public final static short FUNC=267;
public final static short RETURN=268;
public final static short LONGINT=269;
public final static short FLOAT=270;
public final static short ID=271;
public final static short CTE=272;
public final static short CADENA_MULT=273;
public final static short PROC=274;
public final static short VAR=275;
public final static short NI=276;
public final static short UP=277;
public final static short DOWN=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    3,    3,    6,
    6,    7,    7,    8,    8,    4,    4,    5,    5,    5,
    5,    5,    9,   17,   17,   16,   16,   10,   10,   18,
   18,   18,   20,   20,   11,   11,   12,   12,   22,   22,
   13,   14,   15,   15,   15,   15,   15,   15,   15,   24,
   24,   24,   24,   24,   24,   24,   25,   21,   21,   21,
   21,   27,   27,   27,   27,   27,   27,   26,   26,   26,
   26,   26,   26,   26,   28,   28,   28,   28,   28,   28,
   28,   23,   23,   19,   19,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    1,    2,    1,    2,    1,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    1,    1,   11,   10,    5,
    3,    1,    2,    3,    8,   10,   14,   12,    1,    1,
    4,    3,    5,    4,    5,    4,    4,    5,    4,    5,
    3,    1,    7,    3,    3,    2,    1,    3,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,   26,   27,    0,    0,    0,    1,
    0,    0,    7,    9,   16,   17,   18,   19,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    6,    8,    0,    0,    0,   82,   84,    0,   83,    0,
   77,    0,    0,    0,    0,    0,    0,    0,   57,    0,
    0,   42,    0,    0,   23,   66,   62,   63,   67,   64,
   65,    0,    0,    0,    0,    0,   85,    0,    0,    0,
    0,    0,    0,    0,    0,   41,   47,   46,    0,   49,
   44,    0,    0,    0,    0,    0,    0,    0,    0,   24,
    0,    0,    0,   81,   80,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   78,   75,   79,   76,    0,
   45,   48,   43,   54,    0,   55,    0,    0,   33,    0,
    0,    0,   15,   14,    0,   11,    0,    0,   34,    0,
    0,    0,    0,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   35,    0,    0,    0,    0,   30,   10,
    0,   39,   40,    0,   53,    0,    0,   36,    0,   29,
    0,    0,   28,    0,   38,    0,   37,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,  123,   14,  125,  133,  126,   15,   16,
   17,   18,   19,   20,   21,   22,   34,   88,   39,   89,
   40,  154,   41,   50,   51,   42,   66,   43,
};
final static short yysindex[] = {                      -124,
    0,    5,   17,   30,    0,    0,  -33, -229,    0,    0,
  -72, -190,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -192,  -34, -181, -178,   73,  -40,  -18,   78, -190,
    0,    0,   87,   63,   74,    0,    0, -139,    0,   94,
    0,   66,   50,   92,  107,   97,  -11,  -56,    0,  117,
  -36,    0,  -41, -192,    0,    0,    0,    0,    0,    0,
    0,  -32,  -32,  -18,  -18,  -30,    0,  -96,  -30,  -26,
  -23,  -13,   -9,   -6, -102,    0,    0,    0,  109,    0,
    0,  -49,   -4,  -18, -169,  -94,  -76,  150,  152,    0,
   57,   50,   50,    0,    0,  242,   84,  -97,   84,   57,
   50,   57,   50,  242,   84,    0,    0,    0,    0,  144,
    0,    0,    0,    0,  161,    0,  -63,  151,    0,  -61,
 -145,  -72,    0,    0, -196,    0,  -34,  -18,    0,  -28,
  164,  169,  -72,    0,  -97,  168,  192,  211,  133,  -28,
 -145,  132,   -5,    0, -115,    8, -124,  138,    0,    0,
  227,    0,    0,   20,    0,  163, -124,    0,  252,    0,
  170,  -86,    0, -190,    0,  -85,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   16,   24,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   29,
    0,    0,  235,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,    0,
  255,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  256,    0,    0,    0,    0,  257,    0,
    0,   13,   18,    0,    0,    0,  -10,    0,   -8,   23,
   43,   48,   53,    3,    6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  274,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  275,    0,    0,    0,    0,    0,  276,    0,    0,
    0,  -60,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
 -104,    0,    1,   14,   22,  183,    0,  -87,    0,    0,
    0,    0,    0,    0,    0,    2,  265,    0,  -80, -103,
  193,    0,  263,    0,   55,  121,  279,   89,
};
final static int YYTABLESIZE=391;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         86,
   48,   70,   81,   70,   38,   70,   27,   83,   38,  113,
   38,   30,   38,   13,   38,    2,   38,  132,   38,   70,
   70,   38,   70,    3,   31,  122,   38,   28,    4,   79,
   61,   38,   59,   32,  134,   38,  164,  149,   38,  167,
   38,   29,  156,   60,   23,  142,   58,   78,   61,  139,
   59,   32,  161,   73,   87,   73,   24,   73,   74,  148,
   74,   60,   74,   71,   58,   71,  135,   71,  136,   25,
    2,   73,   73,    3,   73,    4,   74,   74,   33,   74,
    7,   71,   71,   68,   71,   68,  117,   68,   72,   44,
   72,   73,   72,   69,   45,   69,   74,   69,   64,    5,
    6,   68,   68,   65,   68,   84,   72,   72,   70,   72,
   71,   69,   69,   46,   69,   64,   62,   53,   63,  124,
   65,   55,   87,    5,    6,   61,   70,   60,   71,   85,
   54,    1,   67,   61,   68,   60,    2,  115,  116,    3,
    2,    4,   87,  124,    5,    6,    7,   76,    3,    8,
   92,   93,   75,    4,  124,   77,  124,   82,  101,  103,
   13,  152,  153,    2,  166,   98,    3,  111,    4,  110,
   13,    5,    6,    7,    2,    2,    8,    3,    3,    4,
    4,  118,  138,  165,    7,    7,   97,   32,    2,   99,
  120,    3,  105,    4,  119,  121,    5,    6,    7,   80,
   12,    8,  127,   12,  128,   12,  112,  129,   12,   12,
   12,  130,  141,   12,  131,   47,   70,   70,   70,   70,
   70,   35,   26,   91,  140,   96,  144,    5,    6,  100,
   36,   37,  102,   85,   36,   37,   36,   37,   36,   37,
   36,   37,  104,   37,   36,   37,  106,   36,   37,  108,
  145,  114,   36,   37,  146,  147,  150,   36,   37,  151,
  157,   36,   37,  155,   36,   37,   36,   37,   73,   73,
   73,   73,   73,   74,   74,   74,   74,   74,   71,   71,
   71,   71,   71,   64,   62,  158,   63,  160,   65,   49,
   52,  159,  162,   25,  163,   52,   56,   32,   68,   68,
   68,   68,   68,   72,   72,   72,   72,   72,   69,   69,
   69,   69,   69,   49,   51,   31,   50,  143,   90,  137,
   72,   69,   56,   57,   58,   59,   94,   95,    0,    0,
   56,   57,   58,   59,    0,  107,  109,    0,    0,    0,
    0,    0,    0,    0,    0,   49,   49,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   49,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   41,   59,   43,   45,   45,   40,   44,   45,   59,
   45,   11,   45,    0,   45,    0,   45,  121,   45,   59,
   60,   45,   62,    0,   11,  123,   45,   61,    0,   41,
   41,   45,   41,   12,  122,   45,  123,  141,   45,  125,
   45,  271,  147,   41,   40,  133,   41,   59,   59,  130,
   59,   30,  157,   41,   53,   43,   40,   45,   41,  140,
   43,   59,   45,   41,   59,   43,  263,   45,  265,   40,
  261,   59,   60,  264,   62,  266,   59,   60,  271,   62,
  271,   59,   60,   41,   62,   43,   85,   45,   41,  271,
   43,   42,   45,   41,  273,   43,   47,   45,   42,  269,
  270,   59,   60,   47,   62,   51,   59,   60,   43,   62,
   45,   59,   60,   41,   62,   42,   43,   40,   45,   98,
   47,   59,  121,  269,  270,   60,   43,   62,   45,  275,
   44,  256,  272,   60,   41,   62,  261,   83,   84,  264,
  125,  266,  141,  122,  269,  270,  271,   41,  125,  274,
   62,   63,   61,  125,  133,   59,  135,   41,   70,   71,
  147,  277,  278,  261,  164,  262,  264,   59,  266,  272,
  157,  269,  270,  271,  261,  261,  274,  264,  264,  266,
  266,  276,  128,  162,  271,  271,   66,  166,  261,   69,
   41,  264,   72,  266,  271,   44,  269,  270,  271,  256,
  261,  274,   59,  264,   44,  266,  256,  271,  269,  270,
  271,   61,   44,  274,  276,  256,  256,  257,  258,  259,
  260,  256,  256,  256,   61,  256,   59,  269,  270,  256,
  271,  272,  256,  275,  271,  272,  271,  272,  271,  272,
  271,  272,  256,  272,  271,  272,  256,  271,  272,  256,
   59,  256,  271,  272,   44,  123,  125,  271,  272,  265,
  123,  271,  272,  256,  271,  272,  271,  272,  256,  257,
  258,  259,  260,  256,  257,  258,  259,  260,  256,  257,
  258,  259,  260,   42,   43,   59,   45,  125,   47,   27,
   28,  272,   41,   59,  125,   41,   41,   41,  256,  257,
  258,  259,  260,  256,  257,  258,  259,  260,  256,  257,
  258,  259,  260,   51,   41,   41,   41,  135,   54,  127,
   42,  256,  257,  258,  259,  260,   64,   65,   -1,   -1,
  257,  258,  259,  260,   -1,   73,   74,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   83,   84,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  128,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IGUAL","MAYOR_IGUAL","MENOR_IGUAL",
"DISTINTO","IF","THEN","ELSE","FOR","END_IF","OUT","FUNC","RETURN","LONGINT",
"FLOAT","ID","CTE","CADENA_MULT","PROC","VAR","NI","UP","DOWN",
};
final static String yyrule[] = {
"$accept : programa",
"programa : cuerpo",
"cuerpo : bloque_declarativo",
"cuerpo : bloque_ejecutable",
"cuerpo : bloque_declarativo bloque_ejecutable",
"cuerpo : error",
"bloque_declarativo : bloque_declarativo sentencia_declarativa",
"bloque_declarativo : sentencia_declarativa",
"bloque_ejecutable : bloque_ejecutable sentencia_ejecutable",
"bloque_ejecutable : sentencia_ejecutable",
"bloque_sentencias : '{' lista_sentencias sentencia '}'",
"bloque_sentencias : sentencia",
"lista_sentencias : lista_sentencias sentencia",
"lista_sentencias : sentencia",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : declaracion_variables",
"sentencia_declarativa : declaracion_procedimiento",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_salida",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_ejecutable : sentencia_invocacion",
"declaracion_variables : tipo lista_variables ';'",
"lista_variables : ID ',' lista_variables",
"lista_variables : ID",
"tipo : LONGINT",
"tipo : FLOAT",
"declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' ')' NI '=' cte '{' cuerpo '}'",
"lista_parametros_formales : parametro_formal ',' parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal",
"parametro_formal : tipo ID",
"parametro_formal : VAR tipo ID",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"incr_decr : UP",
"incr_decr : DOWN",
"sentencia_salida : OUT '(' CADENA_MULT ')'",
"sentencia_asignacion : ID '=' factor",
"sentencia_invocacion : ID '(' lista_parametros ')' ';'",
"sentencia_invocacion : ID '(' ')' ';'",
"sentencia_invocacion : ID '(' error ')' ';'",
"sentencia_invocacion : ID '(' error ';'",
"sentencia_invocacion : ID error ')' ';'",
"sentencia_invocacion : ID '(' lista_parametros ')' error",
"sentencia_invocacion : ID '(' ')' error",
"lista_parametros : parametro ',' parametro ',' parametro",
"lista_parametros : parametro ',' parametro",
"lista_parametros : parametro",
"lista_parametros : parametro ',' parametro ',' parametro ',' error",
"lista_parametros : parametro ',' error",
"lista_parametros : parametro parametro parametro",
"lista_parametros : parametro parametro",
"parametro : factor",
"condicion : expresion comparador expresion",
"condicion : expresion error expresion",
"condicion : expresion comparador error",
"condicion : error comparador expresion",
"comparador : MAYOR_IGUAL",
"comparador : MENOR_IGUAL",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : DISTINTO",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : expresion '+' error",
"expresion : expresion '-' error",
"expresion : error '+' termino",
"expresion : error '-' termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : termino '*' error",
"termino : termino '/' error",
"termino : error '/' factor",
"termino : error '*' factor",
"factor : ID",
"factor : cte",
"cte : CTE",
"cte : '-' CTE",
};

//#line 180 "gramatica.y"

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
//#line 462 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 43:
//#line 112 "gramatica.y"
{System.out.println("Sentencia de invocacion con lista de parametros");}
break;
case 44:
//#line 113 "gramatica.y"
{System.out.println("Sentencia de invocacion sin parametros");}
break;
case 45:
//#line 114 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parámetros inválidos " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 46:
//#line 115 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 47:
//#line 116 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 48:
//#line 117 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 49:
//#line 118 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 50:
//#line 121 "gramatica.y"
{System.out.println("Lista de parametros: 3 parametros");}
break;
case 51:
//#line 122 "gramatica.y"
{System.out.println("Lista de parametros: 2 parametros");}
break;
case 52:
//#line 123 "gramatica.y"
{System.out.println("Lista de parametros: 1 parametro");}
break;
case 53:
//#line 124 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Numero de parametros excedido " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 54:
//#line 125 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parametro incorrecto " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 55:
//#line 126 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 56:
//#line 127 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 59:
//#line 134 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta comparador " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 60:
//#line 135 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la segunda expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 61:
//#line 136 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la primera expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 62:
//#line 139 "gramatica.y"
{yyval = val_peek(0);}
break;
case 63:
//#line 140 "gramatica.y"
{yyval = val_peek(0);}
break;
case 64:
//#line 141 "gramatica.y"
{yyval = new ParserVal('>');}
break;
case 65:
//#line 142 "gramatica.y"
{yyval = new ParserVal('<');}
break;
case 66:
//#line 143 "gramatica.y"
{yyval = val_peek(0);}
break;
case 67:
//#line 144 "gramatica.y"
{yyval = val_peek(0);}
break;
case 71:
//#line 150 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 72:
//#line 151 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 73:
//#line 152 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 74:
//#line 153 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 78:
//#line 159 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 79:
//#line 160 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 80:
//#line 161 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 81:
//#line 162 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 82:
//#line 165 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 84:
//#line 169 "gramatica.y"
{String cte = val_peek(0).sval;
           checkRango(cte, false);
           yyval = val_peek(0);
           }
break;
case 85:
//#line 173 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  checkRango(cte, true);
      		  yyval = new ParserVal("-" + cte);
     	 	}
break;
//#line 753 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
