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
public static ParserVal yylval;//the 'lval' (result) I got from yylex()
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
    5,    5,    9,   17,   17,   16,   16,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   18,   18,   18,
   18,   18,   18,   20,   20,   20,   20,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   22,   22,   13,   13,   13,   13,   13,   13,   13,   14,
   14,   14,   14,   15,   15,   15,   15,   15,   15,   15,
   24,   24,   24,   24,   24,   24,   24,   25,   21,   21,
   21,   21,   27,   27,   27,   27,   27,   27,   23,   23,
   23,   23,   23,   23,   23,   28,   28,   28,   28,   28,
   28,   28,   26,   26,   19,   19,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    1,    2,    1,    2,    1,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    1,    1,   11,   10,   11,
   10,   10,    9,   11,   10,   11,   10,   11,   10,   11,
   10,   11,   10,   11,   10,   11,   10,    5,    3,    1,
    5,    5,    3,    2,    3,    2,    3,    8,   10,    8,
   10,    8,   10,    7,    9,    8,   10,    8,   10,    8,
   10,    7,    9,    8,   10,    8,    8,    7,    9,   10,
   10,   10,    9,    8,   14,   12,   14,   12,   14,   12,
   14,   12,   14,   12,   14,   12,   14,   12,   14,   12,
   14,   12,   14,   12,   14,   12,   14,   12,   14,   12,
    1,    1,    5,    5,    5,    4,    5,    5,    5,    4,
    4,    4,    4,    5,    4,    5,    4,    4,    4,    3,
    5,    3,    1,    7,    3,    3,    2,    1,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    3,    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   26,   27,    0,    0,    0,    1,
    0,    0,    7,    9,   16,   17,   18,   19,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    6,
    0,    8,    0,    0,    0,    0,    0,  165,    0,    0,
  164,    0,    0,  158,    0,    0,  163,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  138,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   23,    0,    0,
  147,  143,  144,  148,  145,  146,    0,    0,    0,    0,
    0,    0,    0,  166,    0,    0,    0,    0,    0,    0,
    0,  121,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  116,  128,  127,    0,  125,
    0,    0,    0,  122,  123,  120,    0,    0,    0,    0,
    0,    0,    0,   56,    0,    0,    0,   54,    0,    0,
    0,   24,    0,    0,    0,    0,    0,  162,  161,    0,
    0,  114,    0,    0,    0,    0,    0,    0,    0,    0,
  159,  156,  160,  157,    0,    0,    0,   15,   14,    0,
   11,    0,    0,    0,    0,    0,    0,    0,    0,  115,
  117,  118,  119,  113,  126,  124,  135,    0,  136,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   57,   55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,    0,    0,    0,
    0,    0,    0,   72,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   51,   52,   48,    0,    0,    0,    0,   60,    0,   62,
    0,   66,   10,    0,    0,   68,    0,   70,    0,   74,
   84,    0,   76,    0,    0,    0,   77,   58,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  111,  112,    0,    0,
    0,    0,   65,    0,    0,    0,   73,    0,   79,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  134,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    0,   31,
    0,    0,   61,   63,   67,   69,   71,   75,   80,   81,
   82,   59,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   35,    0,   37,    0,   39,    0,   41,   43,
   45,   47,   29,    0,    0,    0,    0,    0,   32,   30,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   34,   36,   38,   40,   42,   44,   46,   28,
    0,   88,    0,   90,    0,   92,    0,   94,    0,   96,
    0,   98,    0,  100,    0,  102,    0,  104,    0,  106,
    0,  108,    0,    0,   86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   87,   89,
   91,   93,   95,   97,   99,  101,  103,  105,  107,  109,
   85,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,  180,  221,  181,   15,   16,
   17,   18,   19,   20,   21,   22,   44,   85,   51,   86,
   52,  359,   53,   73,   74,   54,  101,   55,
};
final static short yysindex[] = {                       370,
  -23,  -34,  -33,   -3,    0,    0,  -30,    5,    0,    0,
  641,  155,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -247,   19,  -43,   76,   82,   32, -188, -159, -184,
  -38,   93,   68,   85,   88,   -1,  -36,  -23,  155,    0,
   17,    0,   73,  102,  -32,  505,  114,    0,  138,  -78,
    0,  154,  353,    0,   53,  101,    0,   45,  166,  144,
  -52,   50,  153,  156,   15,  180,  275,   78,  287,  296,
   23,  315,  323,   57,    0,   51,    3,  -29,   67,   75,
  107, -131,  129,  119,  344,  -31, -247,    0,  136,  350,
    0,    0,    0,    0,    0,    0,   87,   87,   -9,   -9,
   76,  125,  340,    0,  158,   76,   95,  104,  109,  115,
  117,    0,  160,  161,  523,  179, -202,  145,  146,  176,
 -185,  391,  396,  398,  -51,    0,    0,    0,  399,    0,
  400,  121,   -9,    0,    0,    0,  188,  424,  190,  428,
  -40, -213,  430,    0,  201,  202,  449,    0,  235, -164,
 -164,    0,  455,  238,   84,   53,   53,    0,    0,   90,
  464,    0,  523,   90,   84,   53,   84,   53,  101,   90,
    0,    0,    0,    0,  523,  523,  641,    0,    0, -113,
    0,  523,  523,  305,  468,  470,  474,  476,  -48,    0,
    0,    0,    0,    0,    0,    0,    0,  493,    0,  485,
  268,  494,  281,  498,  284,  501,   96, -204,    0,    0,
  -20,  502,  528,  -24,  -20,  516,   82,  -89,  -85,  -73,
  641,    0,  523,  521,  -64,  -63,  -21,  417,  522, -141,
   82,   82,   82,   82,   82,  123,   -9,  -20,  517,  -20,
  525,  -20,  532,  -20,  -20,   39,  535,  116,  462,  -20,
 -164, -164, -164,  477,  -20,  543,  523,  544,  523,  547,
  523,  548,  457,  343,    0,  523,  560,  523,  561,  523,
  568,  372,  364,    0,  571,  512,  -44,  573,  574,  578,
  583,  584,  483,  -41,  594,  526,  -20,  527,  -20,  529,
  -20,  530,  539,  551, -109,  -20,  -20,  128,  370,  552,
    0,    0,    0,  370,  554,  -95,  380,    0,  383,    0,
  386,    0,    0,  612,  414,    0,  415,    0,  419,    0,
    0,  631,    0,  -14,  633, -207,    0,    0,  -95,  -95,
  -95,  -95,  -95,  -95,  -95, -203,  437,  370,  572,  370,
  576,  370,  577,  370,  370,  370,  370,  370,  585,  599,
  600, -100,  569,  370,  601,  370,    0,    0,  425,  637,
  665,  670,    0,  671,  672,  673,    0,  674,    0,  685,
  -37,  429,  480,  481,  486,  488,  497,  503,  531, -149,
    0,  602,  370,  630,  370,  632,  370,  649,  677,  682,
  683,  696,  370,  370,  370,  370,  370,    0,  709,    0,
  717,  684,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  757,  758,  803,  807,  808,  813,  815,  819,
  825,   79,    0,  726,    0,  746,    0,  747,    0,    0,
    0,    0,    0,  751,  752,  753,  754,  759,    0,    0,
  -68,  495,  524,  540,  549,  553,  562,  566,  575,  579,
  591,  597,    0,    0,    0,    0,    0,    0,    0,    0,
  155,    0,  155,    0,  155,    0,  155,    0,  155,    0,
  155,    0,  155,    0,  155,    0,  155,    0,  155,    0,
  155,    0,   17,  653,    0,  603,  609,  625,  629,  642,
  666,  675,  687,  691,  700,  704,  -11,  713,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
   30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   33,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   42,    0,
    0,    0,  823,    0,    0,    0,  489,    0,    0,    0,
    0,    0,    0,    0,  365,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    4,    0,  842,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  846,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   16,    0,  847,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  401,  408,    0,    0,   10,
    0,    0,    0,   27,  447,  453,  459,  479,   40,   77,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  851,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  853,  858,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  765,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  323,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  668,    0,   24,  186,  -12,  810,    0, -136,    0,    0,
    0,    0,    0,    0,    0,  519,  814,  -13,  837,  403,
  356,  667,   36,    0,  -53,  484,  863,  507,
};
final static int YYTABLESIZE=1135;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
  205,   50,   69,  130,   83,   27,   29,  194,   89,   33,
  236,  137,  151,  348,  328,  129,   24,  336,   24,  253,
  133,  412,  397,   43,   50,   24,   42,   78,   24,    5,
   34,   90,    2,    3,   39,   50,   31,   25,   80,   25,
  222,    4,  206,   83,   37,  107,   25,  108,  370,   25,
  142,  247,  379,  183,  461,  110,   24,  371,   45,  184,
   58,  136,  207,  129,  138,  140,  143,  140,  142,   77,
  188,  248,   61,  357,  358,  121,   50,   25,  198,  199,
  141,  128,   63,   50,  263,  140,  189,  107,   66,  108,
  117,   81,   99,   97,  110,   98,   64,  100,  141,  111,
  132,   50,  179,  112,    5,    6,  421,  139,   72,  134,
   82,   65,   50,  510,  275,  142,   87,  139,  125,  452,
   50,  276,  422,  277,  145,   99,   50,   78,  130,   50,
  100,   50,  107,   70,  108,  139,  160,    5,    6,   50,
  129,  164,   99,   97,  170,   98,  347,  100,   50,  223,
  179,  224,   78,   50,    5,  396,  246,    2,    3,   50,
   88,   50,  179,  179,  179,   50,    4,   50,   83,  179,
  179,  179,   50,  257,  102,  258,  298,  259,  103,  260,
  110,  357,  358,  285,  114,   99,   97,   41,   98,  261,
  100,  262,    2,  104,  105,    3,   40,    4,  266,  268,
  267,  269,    7,   96,  193,   95,  113,  235,  179,  115,
  179,  327,   46,  118,  335,  179,  119,   67,  411,   81,
  122,   26,   28,   81,  150,   32,   81,   47,   48,   49,
  144,  252,    5,    6,   68,  204,    5,    6,   82,    5,
    6,  270,   82,  271,  179,   82,  179,   23,  179,   23,
  368,   48,   30,  179,   79,  179,   23,  179,  135,  130,
   35,   57,   48,  179,  130,  142,  130,  130,  130,  130,
  120,  129,  130,  130,  130,   36,  129,  130,  129,  129,
  129,  129,  140,   78,  129,  129,  129,   60,   78,  129,
   78,   78,   78,   78,  294,  141,   78,   78,   78,   83,
  178,   78,   57,   48,   83,  116,   83,   83,   83,   83,
   48,  110,   83,   83,   83,  123,  110,   83,  110,  110,
  110,  110,   81,   71,  110,  110,  110,   57,   48,  110,
  141,   56,  139,  124,  451,    5,    6,   46,   57,   48,
   76,   82,  155,    5,    6,  126,   57,   48,  178,   82,
  165,  245,   57,   48,  127,   57,   48,   57,   48,  167,
  178,  178,  178,  131,  169,   57,   48,  178,  178,  178,
  171,  297,  173,  130,   57,   48,  197,  144,  283,   57,
   48,   59,   62,  351,  149,   57,   48,   57,   48,  148,
  154,   57,   48,   57,   48,  107,  161,  108,  162,   48,
   91,   92,   93,   94,  147,  151,  178,  151,  178,  151,
   41,  153,   96,  178,   95,    2,  185,  186,    3,  163,
    4,  175,  176,  151,  151,    7,  151,  177,  462,  464,
  466,  468,  470,  472,  474,  476,  478,  480,  482,  485,
  182,  154,  178,  154,  178,  154,  178,  187,  155,  190,
  155,  178,  155,  178,  191,  178,  192,  195,  196,  154,
  154,  178,  154,  200,  201,  202,  155,  155,  203,  155,
  208,  209,  210,   42,   42,   42,   42,   42,   42,   42,
   42,   42,   42,   42,  486,   42,  487,  152,  488,  152,
  489,  152,  490,  149,  491,  149,  492,  149,  493,  153,
  494,  153,  495,  153,  496,  152,  152,  498,  152,  211,
  212,  149,  149,  216,  149,  215,   75,  153,  153,  150,
  153,  150,  217,  150,   99,   97,  231,   98,  232,  100,
  163,  163,  233,  163,  234,  163,  237,  150,  150,  177,
  150,  334,   96,  239,   95,  238,   99,   97,  163,   98,
  163,  100,  213,  214,  240,   84,  241,   75,  242,  243,
  227,  244,  250,   84,   96,    2,   95,  228,    3,  229,
    4,  251,  256,    5,    6,    7,  255,  287,    8,  265,
  274,  313,  158,  159,  299,  289,  278,  279,  280,  281,
  282,  284,  291,  172,  174,  296,   84,   84,   84,  304,
  146,  306,  308,  156,  157,  310,  312,  314,  106,   91,
   92,   93,   94,  166,  168,   75,   75,  463,  316,  318,
  151,  151,  151,  151,  151,    1,  320,  321,  322,  323,
    2,  329,  330,    3,  177,    4,  331,  337,    5,    6,
    7,  332,  333,    8,  360,  177,  465,  361,  338,  340,
  362,  342,  344,  301,  302,  303,  154,  154,  154,  154,
  154,  345,  467,  155,  155,  155,  155,  155,   84,   84,
  363,  469,   38,  346,  354,  471,  356,    2,  364,  365,
    3,  272,    4,  366,  473,    5,    6,    7,  475,  367,
    8,  369,  381,  398,  383,  403,  402,  477,  385,  387,
  413,  479,  152,  152,  152,  152,  152,  393,  149,  149,
  149,  149,  149,  481,  153,  153,  153,  153,  153,  484,
   75,  394,  395,  404,  441,  400,  423,  499,  405,  406,
  407,  408,  409,  500,  150,  150,  150,  150,  150,   91,
   92,   93,   94,  410,  163,  163,  163,  163,  163,  501,
   41,  414,  415,  502,  425,    2,  427,  416,    3,  417,
    4,   91,   92,   93,   94,    7,  503,  324,  418,   84,
   84,   84,    2,  429,  419,    3,  325,    4,   38,   41,
    5,    6,    7,    2,    2,    8,    3,    3,    4,    4,
  504,    5,    6,    7,    7,   41,    8,  442,  443,  505,
    2,  430,  420,    3,   41,    4,  431,  432,   41,    2,
    7,  506,    3,    2,    4,  507,    3,   41,    4,    7,
  433,   41,    2,    7,  508,    3,    2,    4,  509,    3,
   41,    4,    7,  439,   41,    2,    7,  511,    3,    2,
    4,  440,    3,  444,    4,    7,   41,  445,  446,    7,
  453,    2,  483,  447,    3,  448,    4,    2,   41,  449,
    3,    7,    4,    2,   41,  450,    3,    7,    4,    2,
  454,  455,    3,    7,    4,  456,  457,  458,  459,    7,
   41,   25,  133,  460,   41,    2,   50,  137,    3,    2,
    4,  132,    3,   53,    4,    7,   38,   41,   49,    7,
  152,    2,    2,    0,    3,    3,    4,    4,  497,    5,
    6,    7,    7,    2,    8,  109,    3,    0,    4,    0,
    0,   41,    0,    7,    0,    0,    2,    0,    0,    3,
   41,    4,    0,    0,    0,    2,    7,    0,    3,    0,
    4,    0,   41,    0,    0,    7,   41,    2,    0,    0,
    3,    2,    4,    0,    3,   41,    4,    7,    0,   41,
    2,    7,    0,    3,    2,    4,  353,    3,   41,    4,
    7,  355,  218,    2,    7,    0,    3,    0,    4,    0,
    0,    0,    0,    7,  219,  220,    0,    0,    0,    0,
    0,  225,  226,  230,    0,  372,  373,  374,  375,  376,
  377,  378,  380,    0,    0,  382,    0,  384,    0,  386,
    0,  388,  389,  390,  391,  392,    0,    0,    0,    0,
   12,  399,    0,  401,    0,   12,    0,    0,   12,    0,
   12,    0,  264,   12,   12,   12,    0,  273,   12,    0,
    0,    0,    0,    0,    0,    0,    0,  249,    0,    0,
  424,  254,  426,    0,  428,    0,    0,    0,    0,    0,
  434,  435,  436,  437,  438,    0,  307,    0,  309,    0,
  311,    0,    0,    0,  286,  315,  288,  317,  290,  319,
  292,  293,  295,    0,    0,  326,  300,    0,    0,    0,
    0,  305,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  339,    0,  341,    0,  343,    0,    0,
    0,    0,  349,  350,  352,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   41,   45,   41,    0,   41,   40,   40,   59,   41,   40,
   59,   41,   44,  123,   59,    0,   40,   59,   40,   44,
   74,   59,  123,  271,   45,   40,   39,    0,   40,    0,
   61,   45,    0,    0,   11,   45,   40,   61,   40,   61,
  177,    0,  256,    0,   40,   43,   61,   45,  256,   61,
   41,  256,  256,  256,  123,    0,   40,  265,   40,  262,
   25,   59,  276,   41,   78,   79,   80,   41,   59,   34,
  256,  276,   41,  277,  278,   61,   45,   61,  132,  133,
   41,   59,  271,   45,  221,   59,  272,   43,  273,   45,
   41,  256,   42,   43,   42,   45,  256,   47,   59,   47,
   44,   45,  115,   59,  269,  270,  256,   41,   41,   59,
  275,  271,   45,  125,  256,   41,   44,   41,   41,   41,
   45,  263,  272,  265,  256,   42,   45,   40,  125,   45,
   47,   45,   43,   41,   45,   59,  101,  269,  270,   45,
  125,  106,   42,   43,  109,   45,  256,   47,   45,  263,
  163,  265,  125,   45,  125,  256,   61,  125,  125,   45,
   59,   45,  175,  176,  177,   45,  125,   45,  125,  182,
  183,  184,   45,  263,   61,  265,   61,  263,   41,  265,
  125,  277,  278,  237,   41,   42,   43,  256,   45,  263,
   47,  265,  261,  272,   41,  264,   11,  266,  263,  263,
  265,  265,  271,   60,  256,   62,   41,  256,  221,  262,
  223,  256,  256,   61,  256,  228,   61,  256,  256,  256,
   41,  256,  256,  256,  256,  256,  256,  271,  272,  273,
  271,  256,  269,  270,  273,  276,  269,  270,  275,  269,
  270,  263,  275,  265,  257,  275,  259,  271,  261,  271,
  265,  272,  256,  266,  256,  268,  271,  270,  256,  256,
  256,  271,  272,  276,  261,  256,  263,  264,  265,  266,
  256,  256,  269,  270,  271,  271,  261,  274,  263,  264,
  265,  266,  256,  256,  269,  270,  271,  256,  261,  274,
  263,  264,  265,  266,  256,  256,  269,  270,  271,  256,
  115,  274,  271,  272,  261,  256,  263,  264,  265,  266,
  272,  256,  269,  270,  271,   41,  261,  274,  263,  264,
  265,  266,  256,  256,  269,  270,  271,  271,  272,  274,
  256,  256,  256,  256,  256,  269,  270,  256,  271,  272,
  256,  275,  256,  269,  270,   59,  271,  272,  163,  275,
  256,  256,  271,  272,   59,  271,  272,  271,  272,  256,
  175,  176,  177,   41,  256,  271,  272,  182,  183,  184,
  256,  256,  256,   59,  271,  272,  256,  271,  256,  271,
  272,   26,   27,  256,   41,  271,  272,  271,  272,  271,
   41,  271,  272,  271,  272,   43,  272,   45,   59,  272,
  257,  258,  259,  260,  276,   41,  221,   43,  223,   45,
  256,  276,   60,  228,   62,  261,  272,  272,  264,  262,
  266,  262,  262,   59,   60,  271,   62,  123,  441,  442,
  443,  444,  445,  446,  447,  448,  449,  450,  451,  452,
  262,   41,  257,   43,  259,   45,  261,  272,   41,   59,
   43,  266,   45,  268,   59,  270,   59,   59,   59,   59,
   60,  276,   62,  276,   41,  276,   59,   60,   41,   62,
   41,  271,  271,  486,  487,  488,  489,  490,  491,  492,
  493,  494,  495,  496,  461,  498,  463,   41,  465,   43,
  467,   45,  469,   41,  471,   43,  473,   45,  475,   41,
  477,   43,  479,   45,  481,   59,   60,  484,   62,   61,
  276,   59,   60,  276,   62,   61,   33,   59,   60,   41,
   62,   43,   59,   45,   42,   43,   59,   45,   59,   47,
   42,   43,   59,   45,   59,   47,   44,   59,   60,  123,
   62,   59,   60,  276,   62,   61,   42,   43,   60,   45,
   62,   47,  150,  151,   61,   37,  276,   74,   61,  276,
  256,   61,   61,   45,   60,  261,   62,  263,  264,  265,
  266,   44,  217,  269,  270,  271,   61,   61,  274,   59,
   59,  125,   99,  100,  123,   61,  231,  232,  233,  234,
  235,  236,   61,  110,  111,   61,   78,   79,   80,  123,
   82,   59,   59,   97,   98,   59,   59,  265,  256,  257,
  258,  259,  260,  107,  108,  132,  133,  123,   59,   59,
  256,  257,  258,  259,  260,  256,   59,  256,  265,   59,
  261,   59,   59,  264,  123,  266,   59,   44,  269,  270,
  271,   59,   59,  274,  265,  123,  123,  265,  123,  123,
  265,  123,  123,  251,  252,  253,  256,  257,  258,  259,
  260,  123,  123,  256,  257,  258,  259,  260,  150,  151,
   59,  123,  256,  123,  123,  123,  123,  261,  265,  265,
  264,  265,  266,  265,  123,  269,  270,  271,  123,   59,
  274,   59,  256,  125,  123,   59,  272,  123,  123,  123,
  272,  123,  256,  257,  258,  259,  260,  123,  256,  257,
  258,  259,  260,  123,  256,  257,  258,  259,  260,  123,
  237,  123,  123,   59,   41,  125,  125,  125,   59,   59,
   59,   59,   59,  125,  256,  257,  258,  259,  260,  257,
  258,  259,  260,   59,  256,  257,  258,  259,  260,  125,
  256,  272,  272,  125,  125,  261,  125,  272,  264,  272,
  266,  257,  258,  259,  260,  271,  125,  256,  272,  251,
  252,  253,  261,  125,  272,  264,  265,  266,  256,  256,
  269,  270,  271,  261,  261,  274,  264,  264,  266,  266,
  125,  269,  270,  271,  271,  256,  274,   41,   41,  125,
  261,  125,  272,  264,  256,  266,  125,  125,  256,  261,
  271,  125,  264,  261,  266,  125,  264,  256,  266,  271,
  125,  256,  261,  271,  125,  264,  261,  266,  125,  264,
  256,  266,  271,  125,  256,  261,  271,  125,  264,  261,
  266,  125,  264,   41,  266,  271,  256,   41,   41,  271,
  125,  261,  256,   41,  264,   41,  266,  261,  256,   41,
  264,  271,  266,  261,  256,   41,  264,  271,  266,  261,
  125,  125,  264,  271,  266,  125,  125,  125,  125,  271,
  256,   59,   41,  125,  256,  261,   41,   41,  264,  261,
  266,   41,  264,   41,  266,  271,  256,  256,   41,  271,
   87,  261,  261,   -1,  264,  264,  266,  266,  256,  269,
  270,  271,  271,  261,  274,   53,  264,   -1,  266,   -1,
   -1,  256,   -1,  271,   -1,   -1,  261,   -1,   -1,  264,
  256,  266,   -1,   -1,   -1,  261,  271,   -1,  264,   -1,
  266,   -1,  256,   -1,   -1,  271,  256,  261,   -1,   -1,
  264,  261,  266,   -1,  264,  256,  266,  271,   -1,  256,
  261,  271,   -1,  264,  261,  266,  299,  264,  256,  266,
  271,  304,  163,  261,  271,   -1,  264,   -1,  266,   -1,
   -1,   -1,   -1,  271,  175,  176,   -1,   -1,   -1,   -1,
   -1,  182,  183,  184,   -1,  329,  330,  331,  332,  333,
  334,  335,  336,   -1,   -1,  338,   -1,  340,   -1,  342,
   -1,  344,  345,  346,  347,  348,   -1,   -1,   -1,   -1,
  256,  354,   -1,  356,   -1,  261,   -1,   -1,  264,   -1,
  266,   -1,  223,  269,  270,  271,   -1,  228,  274,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  211,   -1,   -1,
  383,  215,  385,   -1,  387,   -1,   -1,   -1,   -1,   -1,
  393,  394,  395,  396,  397,   -1,  257,   -1,  259,   -1,
  261,   -1,   -1,   -1,  238,  266,  240,  268,  242,  270,
  244,  245,  246,   -1,   -1,  276,  250,   -1,   -1,   -1,
   -1,  255,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  287,   -1,  289,   -1,  291,   -1,   -1,
   -1,   -1,  296,  297,  298,
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
"declaracion_procedimiento : error ID '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : error ID '(' ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC '(' ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC error '(' lista_parametros_formales ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC error '(' ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID error lista_parametros_formales ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID error ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' error ')' NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' error NI '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' error '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' ')' error '=' cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI error cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' ')' NI error cte '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI '=' error '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' ')' NI '=' error '{' cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI '=' cte error cuerpo '}'",
"declaracion_procedimiento : PROC ID '(' ')' NI '=' cte error cuerpo '}'",
"lista_parametros_formales : parametro_formal ',' parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal",
"lista_parametros_formales : parametro_formal error parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal error parametro_formal",
"lista_parametros_formales : parametro_formal error parametro_formal",
"parametro_formal : tipo ID",
"parametro_formal : VAR tipo ID",
"parametro_formal : error ID",
"parametro_formal : VAR error ID",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : error '(' condicion ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : error '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF error condicion ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF error condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' error ')' THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' error ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion error THEN bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion error THEN bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' error bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' error bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN error END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN error ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias error ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias END_IF error",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias error",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE error END_IF ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error ';'",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias END_IF error",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error",
"sentencia_seleccion : IF '(' condicion ')' THEN ELSE END_IF error",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : error '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : error '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion error incr_decr CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion error incr_decr CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error '{' bloque_ejecutable '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error sentencia_ejecutable",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' '{' error '}'",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' error",
"incr_decr : UP",
"incr_decr : DOWN",
"sentencia_salida : OUT '(' CADENA_MULT ')' ';'",
"sentencia_salida : error '(' CADENA_MULT ')' ';'",
"sentencia_salida : OUT error CADENA_MULT ')' ';'",
"sentencia_salida : OUT '(' ')' ';'",
"sentencia_salida : OUT '(' error ')' ';'",
"sentencia_salida : OUT '(' CADENA_MULT error ';'",
"sentencia_salida : OUT '(' CADENA_MULT ')' error",
"sentencia_asignacion : ID '=' expresion ';'",
"sentencia_asignacion : error '=' expresion ';'",
"sentencia_asignacion : ID '=' error ';'",
"sentencia_asignacion : ID '=' expresion error",
"sentencia_invocacion : ID '(' lista_parametros ')' ';'",
"sentencia_invocacion : ID '(' ')' ';'",
"sentencia_invocacion : ID '(' error ')' ';'",
"sentencia_invocacion : ID '(' error ';'",
"sentencia_invocacion : ID error ')' ';'",
"sentencia_invocacion : ID '(' lista_parametros ')'",
"sentencia_invocacion : ID '(' ')'",
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

//#line 264 "gramatica.y"

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
		int cont = Integer.parseInt(TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		if (cont == 0)
		  TablaSimbolos.remove(cte);
		else
		  TablaSimbolos.getToken(cte).addAtributo("contador", String.valueOf(cont));
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "LONGINT", nuevoLexema);
		    nuevoToken.addAtributo("contador", "1");
		    TablaSimbolos.add(nuevoToken);
		}
		else {
                     cont = Integer.parseInt(TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
                     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", String.valueOf(cont));
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
		int cont = Integer.parseInt(TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		if (cont == 0)
		  TablaSimbolos.remove(cte);
		else
		  TablaSimbolos.getToken(cte).addAtributo("contador", String.valueOf(cont));
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "FLOAT", nuevoLexema);
		    nuevoToken.addAtributo("contador", "1");
		    TablaSimbolos.add(nuevoToken);
		}
		else {
                      cont = Integer.parseInt(TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
                      TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", String.valueOf(cont));
                }
	    }
	}
}
//#line 832 "Parser.java"
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
case 23:
//#line 69 "gramatica.y"
{System.out.println("Declaración variables");}
break;
case 28:
//#line 80 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 29:
//#line 81 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 30:
//#line 82 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 31:
//#line 83 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 32:
//#line 84 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 33:
//#line 85 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 34:
//#line 86 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 35:
//#line 87 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 36:
//#line 88 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 37:
//#line 89 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 38:
//#line 90 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 39:
//#line 92 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 40:
//#line 93 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 41:
//#line 94 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 42:
//#line 95 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 43:
//#line 96 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 44:
//#line 97 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 45:
//#line 98 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 46:
//#line 99 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 47:
//#line 100 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '{' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 48:
//#line 104 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 49:
//#line 105 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 50:
//#line 106 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 51:
//#line 107 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 52:
//#line 108 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 53:
//#line 109 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 54:
//#line 112 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 55:
//#line 113 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 56:
//#line 114 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 57:
//#line 115 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 58:
//#line 118 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 59:
//#line 119 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 60:
//#line 120 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 61:
//#line 121 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 62:
//#line 122 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 63:
//#line 123 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 64:
//#line 124 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 65:
//#line 125 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 66:
//#line 126 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 67:
//#line 127 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 68:
//#line 128 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 69:
//#line 129 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 70:
//#line 130 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 71:
//#line 131 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 72:
//#line 132 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 73:
//#line 133 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 74:
//#line 134 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 75:
//#line 135 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 76:
//#line 136 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 77:
//#line 137 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 78:
//#line 138 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 79:
//#line 139 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 80:
//#line 140 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 81:
//#line 141 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 82:
//#line 142 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 83:
//#line 143 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 84:
//#line 144 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan bloques de sentencias luego de THEN y ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 85:
//#line 147 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 86:
//#line 148 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 87:
//#line 149 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 88:
//#line 150 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 89:
//#line 151 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 90:
//#line 152 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 91:
//#line 153 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 92:
//#line 154 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 93:
//#line 155 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 94:
//#line 156 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 95:
//#line 157 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 96:
//#line 158 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 97:
//#line 159 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 98:
//#line 160 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 99:
//#line 161 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 100:
//#line 162 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 101:
//#line 163 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 102:
//#line 164 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 103:
//#line 165 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 104:
//#line 166 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 105:
//#line 167 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 106:
//#line 168 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 107:
//#line 169 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 108:
//#line 170 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 109:
//#line 171 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 110:
//#line 172 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 113:
//#line 179 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 114:
//#line 180 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 115:
//#line 181 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 116:
//#line 182 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 117:
//#line 183 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 118:
//#line 184 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 119:
//#line 185 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 120:
//#line 188 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 121:
//#line 189 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 122:
//#line 190 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 123:
//#line 191 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 124:
//#line 194 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocacion con lista de parametros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 125:
//#line 195 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocacion sin parametros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 126:
//#line 196 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parámetros inválidos %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 127:
//#line 197 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 128:
//#line 198 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 129:
//#line 199 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 130:
//#line 200 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 131:
//#line 203 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parametros: 3 %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 132:
//#line 204 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parametros: 2 %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 133:
//#line 205 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parametros: 1 %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 134:
//#line 206 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Numero de parametros excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 135:
//#line 207 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parametro incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 136:
//#line 208 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 137:
//#line 209 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 139:
//#line 215 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Comparacion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 140:
//#line 216 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta comparador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 141:
//#line 217 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la segunda expresion de la condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 142:
//#line 218 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la primera expresion de la condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 143:
//#line 221 "gramatica.y"
{yyval = val_peek(0);}
break;
case 144:
//#line 222 "gramatica.y"
{yyval = val_peek(0);}
break;
case 145:
//#line 223 "gramatica.y"
{yyval = new ParserVal('>');}
break;
case 146:
//#line 224 "gramatica.y"
{yyval = new ParserVal('<');}
break;
case 147:
//#line 225 "gramatica.y"
{yyval = val_peek(0);}
break;
case 148:
//#line 226 "gramatica.y"
{yyval = val_peek(0);}
break;
case 149:
//#line 229 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 150:
//#line 230 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 152:
//#line 232 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 153:
//#line 233 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 154:
//#line 234 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 155:
//#line 235 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 156:
//#line 238 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Multiplicacion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 157:
//#line 239 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Division %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 159:
//#line 241 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la multiplicacion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 160:
//#line 242 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la division %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 161:
//#line 243 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la division %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 162:
//#line 244 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Error en la multiplicacion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 163:
//#line 247 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 165:
//#line 251 "gramatica.y"
{String cte = val_peek(0).sval;
           checkRango(cte, false);
           yyval = val_peek(0);
           }
break;
case 166:
//#line 255 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  checkRango(cte, true);
      		  yyval = new ParserVal("-" + cte);
      		  String cte_nueva= "-"+cte;
      		  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte_nueva);
     	 	}
break;
//#line 1525 "Parser.java"
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
