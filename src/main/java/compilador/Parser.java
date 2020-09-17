package compilador;//### This file created by BYACC 1.8(/Java extension  1.15)
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
   13,   14,   15,   15,   24,   24,   24,   25,   21,   27,
   27,   27,   27,   27,   27,   26,   26,   26,   28,   28,
   28,   23,   23,   19,   19,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    1,    2,    1,    2,    1,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    1,    1,   11,   10,    5,
    3,    1,    2,    3,    8,   10,   14,   12,    1,    1,
    4,    3,    4,    3,    5,    3,    1,    1,    3,    1,
    1,    1,    1,    1,    1,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,   26,   27,    0,    0,    0,    1,
    0,    0,    7,    9,   16,   17,   18,   19,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    6,
    8,    0,    0,   62,   64,    0,   63,    0,   61,    0,
    0,    0,    0,   44,   48,    0,    0,   42,    0,    0,
   23,   65,    0,   54,   50,   51,   55,   52,   53,    0,
    0,    0,    0,    0,    0,   41,   43,    0,    0,    0,
    0,    0,    0,   24,    0,    0,    0,    0,   59,   60,
    0,    0,    0,    0,   33,    0,    0,    0,   15,   14,
    0,   11,    0,    0,   34,    0,    0,    0,    0,   13,
    0,    0,    0,   45,    0,    0,    0,    0,    0,   35,
    0,    0,    0,   30,   10,    0,   39,   40,    0,    0,
    0,   36,    0,   29,    0,    0,   28,    0,   38,    0,
   37,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   89,   14,   91,   99,   92,   15,   16,
   17,   18,   19,   20,   21,   22,   33,   72,   37,   73,
   38,  119,   39,   46,   47,   40,   62,   41,
};
final static short yysindex[] = {                      -204,
    0,   34,   37,   41,    0,    0,  -21, -177,    0,    0,
 -182, -193,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -164,  -36, -163, -162,  -40,  -36,   69, -193,    0,
    0,   66,   53,    0,    0, -159,    0,   73,    0,   -9,
   17,   54,   75,    0,    0,   76,   77,    0,  -41, -164,
    0,    0, -143,    0,    0,    0,    0,    0,    0,  -36,
  -36,  -36,  -36,  -36, -152,    0,    0,  -36, -173, -154,
 -148,   83,   81,    0, -116,   17,   17,    5,    0,    0,
   67,   84, -144,   68,    0, -146, -184, -182,    0,    0,
 -209,    0,  -36,  -36,    0,  -42,   72,   91, -182,    0,
 -116,   78,   82,    0,   23,  -42, -184,   15, -118,    0,
 -176, -204,   26,    0,    0,   92,    0,    0, -120,   32,
 -204,    0,  121,    0,   39, -105,    0, -193,    0, -101,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   11,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
    0,  108,    0,    0,    0,    0,    0,    0,    0,    0,
  -39,    0,    0,    0,    0,    0,  127,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  130,    0,    0,  -33,  -13,    2,    0,    0,
    0,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  133,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -171,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  -77,    0,    4,   22,   43,   74,    0,  -30,    0,    0,
    0,    0,    0,    0,    0,  -24,  126,    0,  -65,  -70,
   85,    0,   12,    0,  -52,  115,    0,   44,
};
final static int YYTABLESIZE=251;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         70,
   44,   58,   36,   58,   36,   58,   88,   56,   36,   56,
    2,   56,    3,    4,   29,   82,   98,  128,   26,   58,
   58,   13,   58,  131,   71,   56,   56,   57,   56,   57,
  105,   57,   30,   60,  120,   61,  114,   45,   48,   27,
  113,  104,   49,  125,   83,   57,   57,   60,   57,   61,
   59,    1,   58,  101,   31,  102,    2,  100,   63,    3,
   49,    4,   71,   64,    5,    6,    7,    2,  108,    8,
    3,   31,    4,   23,   79,   80,   24,    7,    2,   45,
   25,    3,   71,    4,    5,    6,    5,    6,    7,   12,
   69,    8,   12,   28,   12,    5,    6,   12,   12,   12,
  117,  118,   12,   76,   77,   45,   32,   42,   49,   50,
   43,   51,   52,   53,   65,   66,   67,   90,   75,   81,
   68,   84,   85,   86,   87,   93,   95,   94,   96,   97,
   90,  130,  106,   13,  107,    2,  110,    3,    4,  115,
  111,   90,   13,   90,    2,  112,  116,    3,  121,    4,
  122,  123,    5,    6,    7,    2,  124,    8,    3,    2,
    4,  126,    3,  127,    4,    7,   25,   47,  129,    7,
   32,   46,   31,   31,  109,   74,   78,  103,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   58,   58,   58,
   58,    0,    0,   56,   56,   56,   56,    5,    6,   35,
   34,   35,    0,   69,   34,   35,    0,    0,    0,    0,
    0,    0,    0,   57,   57,   57,   57,   54,   55,   56,
   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   41,   45,   43,   45,   45,  123,   41,   45,   43,
    0,   45,    0,    0,   11,   68,   87,  123,   40,   59,
   60,    0,   62,  125,   49,   59,   60,   41,   62,   43,
   96,   45,   11,   43,  112,   45,  107,   26,   27,   61,
  106,   94,   41,  121,   69,   59,   60,   43,   62,   45,
   60,  256,   62,  263,   12,  265,  261,   88,   42,  264,
   59,  266,   87,   47,  269,  270,  271,  261,   99,  274,
  264,   29,  266,   40,   63,   64,   40,  271,  261,   68,
   40,  264,  107,  266,  269,  270,  269,  270,  271,  261,
  275,  274,  264,  271,  266,  269,  270,  269,  270,  271,
  277,  278,  274,   60,   61,   94,  271,  271,   40,   44,
  273,   59,  272,   41,   61,   41,   41,   75,  262,  272,
   44,  276,  271,   41,   44,   59,  271,   44,   61,  276,
   88,  128,   61,  112,   44,  125,   59,  125,  125,  125,
   59,   99,  121,  101,  261,  123,  265,  264,  123,  266,
   59,  272,  269,  270,  271,  261,  125,  274,  264,  261,
  266,   41,  264,  125,  266,  271,   59,   41,  126,  271,
   41,   41,  130,   41,  101,   50,   62,   93,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,   -1,   -1,  257,  258,  259,  260,  269,  270,  272,
  271,  272,   -1,  275,  271,  272,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  259,  260,  257,  258,  259,
  260,
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
"sentencia_invocacion : ID '(' lista_parametros ')'",
"sentencia_invocacion : ID '(' ')'",
"lista_parametros : parametro ',' parametro ',' parametro",
"lista_parametros : parametro ',' parametro",
"lista_parametros : parametro",
"parametro : factor",
"condicion : expresion comparador expresion",
"comparador : MAYOR_IGUAL",
"comparador : MENOR_IGUAL",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : DISTINTO",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : cte",
"cte : CTE",
"cte : '-' CTE",
};

//#line 150 "gramatica.y"

//#line 343 "Parser.java"
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
