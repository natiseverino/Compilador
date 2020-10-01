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
   10,   10,   10,   10,   10,   10,   10,   10,   18,   18,
   18,   18,   18,   18,   20,   20,   20,   20,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   22,   22,   13,   13,   13,   13,   13,   13,   13,
   14,   14,   14,   14,   15,   15,   15,   15,   15,   15,
   15,   24,   24,   24,   24,   24,   24,   24,   25,   21,
   21,   21,   21,   27,   27,   27,   27,   27,   27,   26,
   26,   26,   26,   26,   26,   26,   28,   28,   28,   28,
   28,   28,   28,   23,   23,   19,   19,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    1,    2,    1,    2,    1,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    1,    1,   11,   10,   11,
   10,   10,    9,   11,   10,   11,   10,   11,   10,   11,
   10,   11,   10,   11,   10,   11,   10,   10,    5,    3,
    1,    5,    5,    3,    2,    3,    2,    3,    8,   10,
    8,   10,    8,   10,    7,    9,    8,   10,    8,   10,
    8,   10,    7,    9,    8,   10,    8,    8,    7,    9,
   10,   10,   10,    9,    8,   14,   12,   14,   12,   14,
   12,   14,   12,   14,   12,   14,   12,   14,   12,   14,
   12,   14,   12,   14,   12,   14,   12,   14,   12,   14,
   12,    1,    1,    5,    5,    5,    4,    5,    5,    5,
    4,    4,    4,    4,    5,    4,    5,    4,    4,    5,
    4,    5,    3,    1,    7,    3,    3,    2,    1,    3,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    3,
    3,    1,    3,    3,    3,    3,    3,    3,    1,    3,
    3,    3,    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   26,   27,    0,    0,    0,    1,
    0,    0,    7,    9,   16,   17,   18,   19,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    6,
    0,    8,    0,    0,    0,    0,    0,  166,    0,    0,
  165,    0,  159,    0,    0,  164,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  139,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   23,    0,    0,  148,
  144,  145,  149,  146,  147,    0,    0,    0,    0,    0,
    0,    0,  167,    0,    0,    0,    0,    0,    0,    0,
  122,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  117,  129,  128,    0,  131,  126,
    0,    0,    0,  123,  124,  121,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,    0,   55,    0,    0,
    0,   24,    0,    0,    0,    0,    0,  163,  162,    0,
    0,    0,  115,    0,    0,    0,    0,    0,    0,    0,
    0,  160,  157,  161,  158,    0,    0,    0,   15,   14,
    0,   11,    0,    0,    0,    0,    0,    0,    0,    0,
  116,  118,  119,  120,  114,  127,  130,  125,  136,    0,
  137,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   58,   56,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,    0,    0,   73,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   52,   53,   49,    0,    0,    0,    0,   61,
    0,   63,    0,   67,   10,    0,    0,   69,    0,   71,
    0,   75,   85,    0,   77,    0,    0,    0,   78,   59,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  112,  113,
    0,    0,    0,    0,   66,    0,    0,    0,   74,    0,
   80,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  135,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   33,    0,   31,    0,    0,   62,   64,   68,   70,   72,
   76,   81,   82,   83,   60,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,   37,    0,   39,
    0,   41,   43,   45,   47,   48,   29,    0,    0,    0,
    0,    0,   32,   30,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   34,   36,   38,   40,
   42,   44,   46,   28,    0,   89,    0,   91,    0,   93,
    0,   95,    0,   97,    0,   99,    0,  101,    0,  103,
    0,  105,    0,  107,    0,  109,    0,    0,   87,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   88,   90,   92,   94,   96,   98,  100,  102,
  104,  106,  108,  110,   86,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,  181,  223,  182,   15,   16,
   17,   18,   19,   20,   21,   22,   44,   84,   51,   85,
   52,  361,   53,   73,   74,   54,  100,   55,
};
final static short yysindex[] = {                       721,
  -17,    3,   12,   17,    0,    0,  -32,   34,    0,    0,
  792, -146,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -247,   55,   58,   35,  -42,  -29, -190, -200, -181,
  -39,   23,  -19,   64,   93,   22,  -37,  -17, -146,    0,
    8,    0,   73,   49,  -30,  489,   92,    0,   97, -124,
    0,  122,    0,  343,   65,    0,  109,  128,  141,  -82,
   41,  120,  137,  -56,  148,  158,   78,  146,  150,   14,
  -49,    0,  175,   -7,  176,   87,   46,   53,   57,  118,
 -173,  131,  125,  344,   47, -247,    0,  138,  346,    0,
    0,    0,    0,    0,    0,   68,   68,   35,   35,   77,
   72,  332,    0,  151,   77,   81,   89,   91,   94,   99,
    0,  153,  154,  498,  163, -177,  160,  181,  183, -209,
  351,  371,  400,   90,    0,    0,    0,  402,    0,    0,
   95,  102,   35,    0,    0,    0,  187,  423,  191,  428,
  -40, -215,  435,    0,  201,  233,  447,    0,  239,  -79,
  -79,    0,  461,  242,   74,   65,   65,    0,    0,  381,
   56,  465,    0,  498,   56,   74,   65,   74,   65,  381,
   56,    0,    0,    0,    0,  498,  498,  792,    0,    0,
 -103,    0,  498,  498,  299,  467,  468,  470,  471,   98,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  484,
    0,  472,  259,  476,  263,  480,  266,  495,  -44, -198,
    0,    0,  -11,  496,  501,   86,  -11,  497,  -42,  -78,
  -68,  -55,  792,    0,  498,  500,  114,  115,   -8,  420,
  507, -179,  -42,  -42,  -42,  -42,  -42,  111,   35,  -11,
  506,  -11,  511,  -11,  513,  -11,  -11,  -31,  521,  -41,
  448,  -11,  -79,  -79,  -79,  460,  -11,  525,  498,  526,
  498,  528,  498,  529,  473,  327,    0,  498,  531,  498,
  535,  498,  538,  339,  345,    0,  545,  431,  100,  552,
  553,  556,  557,  564,  415,  116,  569,  491,  -11,  502,
  -11,  503,  -11,  504,  508,  509,  -95,  -11,  -11,   69,
  721,  527,    0,    0,    0,  721,  532, -149,  359,    0,
  365,    0,  383,    0,    0,  590,  389,    0,  391,    0,
  392,    0,    0,  599,    0,  -21,  601, -206,    0,    0,
 -149, -149, -149, -149, -149, -149, -149, -202,  405,  721,
  559,  721,  560,  721,  565,  721,  721,  721,  721,  803,
  570,  580,  581,  -77,  594,  721,  596,  721,    0,    0,
  407,  621,  647,  661,    0,  666,  667,  669,    0,  671,
    0,  672,  119,  455,  479,  481,  512,  515,  537,  540,
  541, -207,    0,  608,  721,  614,  721,  615,  721,  616,
  617,  620,  627,  -25,  636,  721,  721,  721,  721,  721,
    0,  650,    0,  653,  709,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  738,  741,  742,  757,  758,
  762,  763,  767,  773,   82,    0,  692,    0,  697,    0,
  698,    0,    0,    0,    0,    0,    0,  699,  702,  703,
  704,  705,    0,    0,  499,  510,  524,  530,  536,  554,
  555,  575,  576,  589,  595,  606,    0,    0,    0,    0,
    0,    0,    0,    0, -146,    0, -146,    0, -146,    0,
 -146,    0, -146,    0, -146,    0, -146,    0, -146,    0,
 -146,    0, -146,    0, -146,    0,    8, -129,    0,  325,
  607,  618,  619,  631,  632,  648,  652,  664,  668,  680,
   -1,  681,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
   33,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   42,   45,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   51,    0,
    0,    0,  774,    0,    0,    0,  478,    0,    0,    0,
    0,    0,    0,    0,  161,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  793,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  794,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  797,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  349,  386,    0,    0,    0,
  -34,    0,    0,    0,  -28,  406,  411,  451,  457,   29,
   52,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  802,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  807,  808,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  829,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  811,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,   30,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  625,    0,   24,  134,  -12,  779,    0, -157,    0,    0,
    0,    0,    0,    0,    0,  469,  768,   27,  789,  -61,
  342,  303,  519,    0,  -65,  276,  790,   44,
};
final static int YYTABLESIZE=1103;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
  207,   68,   50,   82,  120,   79,  143,   33,  133,  130,
   88,   60,  141,   50,   24,   50,  248,   84,   24,  300,
  224,   71,   24,   43,  143,   50,   42,  350,   34,  111,
  141,   24,    5,   50,   39,   25,  132,   50,   24,   25,
  208,    2,   27,   25,    3,  400,  189,   24,  424,  372,
    4,   29,   25,  381,  128,   63,   31,  249,  373,   25,
  209,   79,  190,   69,  425,  265,  200,  201,   25,  142,
   64,   89,  127,   37,  359,  360,  277,  250,  184,   50,
   62,  116,  145,  278,  185,  279,  137,  142,  215,  216,
  151,   65,  140,  139,   45,    5,    6,  142,  106,  436,
  107,  180,   50,  138,  140,  143,  109,   87,   50,   41,
  140,  110,   50,   50,    2,   98,   86,    3,  124,    4,
   99,   50,  456,  514,    7,   50,  501,  359,  360,  255,
   79,    2,   77,   50,    3,   50,    4,  102,   50,  156,
  157,    7,   84,   50,   40,  136,   50,  103,  195,  167,
  169,  180,  101,  198,  111,   50,  238,    5,  330,  225,
  349,  226,  104,  180,  180,  180,    2,  111,  112,    3,
  180,  180,  180,  287,  338,    4,   80,  415,  399,  114,
  117,  113,   98,   96,  259,   97,  260,   99,  121,    5,
    6,  303,  304,  305,  261,   81,  262,  118,  122,  119,
   95,  152,   94,  152,  125,  152,  129,  263,  126,  264,
  180,  247,  180,   46,  299,  131,   66,  180,   80,  152,
  152,  143,  152,   32,  296,   80,   59,  141,   56,   48,
  144,    5,    6,   67,  134,  206,   70,   81,    5,    6,
   48,   56,   48,  370,   81,   23,  180,  179,  180,   23,
  180,   56,   48,   23,  272,  180,  273,  180,   26,  180,
   48,   79,   23,   56,   48,  180,   79,   28,   79,   79,
   79,   79,   30,   84,   79,   79,   79,   78,   84,   79,
   84,   84,   84,   84,  142,  111,   84,   84,   84,   35,
  111,   84,  111,  111,  111,  111,  115,  179,  111,  111,
  111,   80,  150,  111,   36,   56,   48,  140,   80,  179,
  179,  179,  141,   46,    5,    6,  179,  179,  179,   75,
   81,    5,    6,  155,  353,    5,    6,   81,   47,   48,
   49,   81,  160,  123,   56,   48,  166,  455,   56,   48,
   48,  254,  135,  162,  168,  194,  170,   56,   48,  172,
  197,   56,   48,  237,  174,  329,  179,  199,  179,   56,
   48,   56,   48,  179,   56,   48,  285,   58,   61,   56,
   48,  337,   56,   48,  414,  161,  268,  270,  269,  271,
  165,   56,   48,  171,  149,  106,  154,  107,  144,  155,
  163,  155,  179,  155,  179,  148,  179,   90,   91,   92,
   93,  179,   95,  179,   94,  179,  147,  155,  155,  191,
  155,  179,  164,  153,  176,  177,  152,  152,  152,  152,
  152,  178,   98,   96,  183,   97,  156,   99,  156,  192,
  156,  186,  466,  468,  470,  472,  474,  476,  478,  480,
  482,  484,  486,  489,  156,  156,  153,  156,  153,  503,
  153,  150,  187,  150,  188,  150,   98,   96,  193,   97,
  196,   99,  202,  203,  153,  153,  204,  153,  205,  150,
  150,  211,  150,  336,   95,  210,   94,   42,   42,   42,
   42,   42,   42,   42,   42,   42,   42,   42,  490,   42,
  491,  154,  492,  154,  493,  154,  494,  151,  495,  151,
  496,  151,  497,  212,  498,   83,  499,  213,  500,  154,
  154,  502,  154,   83,  214,  151,  151,  218,  151,  164,
  164,  217,  164,  219,  164,  233,  234,  239,  235,  236,
   98,   96,  240,   97,  241,   99,  242,  164,  243,  164,
  244,  245,  178,   57,  253,   83,   83,   83,   95,  146,
   94,   72,   76,  178,  229,  246,  252,  257,  267,    2,
  258,  230,    3,  231,    4,  276,  289,    5,    6,    7,
  301,  291,    8,  293,  280,  281,  282,  283,  284,  286,
   41,  298,  306,  308,  310,    2,  312,  314,    3,  318,
    4,  316,   72,  320,  323,    7,  322,  315,  105,   90,
   91,   92,   93,  325,  155,  155,  155,  155,  155,  324,
  331,  332,  339,  340,  333,  334,  158,  159,   83,   83,
  178,  465,  335,  362,  342,  344,  346,  173,  175,  363,
  347,  348,  467,  374,  375,  376,  377,  378,  379,  380,
  382,  156,  156,  156,  156,  156,  469,  364,  365,  356,
   72,   72,  471,  366,  358,  367,  368,  369,  473,  371,
  383,  153,  153,  153,  153,  153,  150,  150,  150,  150,
  150,   90,   91,   92,   93,   38,  475,  477,  405,  406,
    2,  385,  387,    3,  274,    4,  326,  389,    5,    6,
    7,    2,  396,    8,    3,  327,    4,  479,  481,    5,
    6,    7,  397,  398,    8,  407,  154,  154,  154,  154,
  154,  483,  151,  151,  151,  151,  151,  485,  401,  408,
  403,   83,   83,   83,  409,  410,  416,  411,  488,  412,
  413,  504,  426,  164,  164,  164,  164,  164,  428,  430,
  432,  433,  505,  506,  434,   90,   91,   92,   93,  445,
  417,  435,  418,   38,   41,  507,  508,   72,    2,    2,
  437,    3,    3,    4,    4,   41,    5,    6,    7,    7,
    2,    8,  509,    3,  443,    4,  510,  444,  446,   41,
    7,  447,  448,  419,    2,   41,  420,    3,  511,    4,
    2,   41,  512,    3,    7,    4,    2,  449,  450,    3,
    7,    4,  451,  452,  513,  515,    7,  453,  421,   41,
   41,  422,  423,  454,    2,    2,  457,    3,    3,    4,
    4,  458,  459,  460,    7,    7,  461,  462,  463,  464,
   41,   41,   25,  134,   51,    2,    2,  138,    3,    3,
    4,    4,  133,  108,   41,    7,    7,   54,   50,    2,
   41,  132,    3,  152,    4,    2,    0,    0,    3,    7,
    4,  487,   41,    0,    0,    7,    2,    2,    0,    3,
    3,    4,    4,   41,   41,    0,    7,    7,    2,    2,
    0,    3,    3,    4,    4,    0,   41,   41,    7,    7,
    0,    2,    2,    0,    3,    3,    4,    4,    0,    0,
    0,    7,    7,   41,    0,    0,    0,   41,    2,    0,
    0,    3,    2,    4,    0,    3,    0,    4,    7,   41,
    0,    0,    7,   41,    2,  355,    0,    3,    2,    4,
  357,    3,    0,    4,    7,   41,   41,    0,    7,    0,
    2,    2,  220,    3,    3,    4,    4,    0,    0,    0,
    7,    7,    0,    0,  221,  222,    0,    0,    0,    0,
    0,  227,  228,  232,  384,    0,  386,    0,  388,    0,
  390,  391,  392,  393,  395,    0,    1,    0,    0,    0,
  402,    2,  404,    0,    3,    0,    4,    0,    0,    5,
    6,    7,    0,    0,    8,    0,    0,    0,    0,    0,
    0,  251,    0,  266,    0,  256,    0,    0,  275,  427,
    0,  429,    0,  431,    0,    0,    0,    0,    0,    0,
  438,  439,  440,  441,  442,    0,    0,    0,  288,    0,
  290,    0,  292,    0,  294,  295,  297,  309,    0,  311,
  302,  313,    0,    0,    0,  307,  317,   38,  319,    0,
  321,    0,    2,    0,    0,    3,  328,    4,  394,    0,
    5,    6,    7,    2,    0,    8,    3,    0,    4,    0,
    0,    5,    6,    7,    0,    0,    8,  341,    0,  343,
    0,  345,    0,    0,   12,    0,  351,  352,  354,   12,
    0,    0,   12,    0,   12,    0,    0,   12,   12,   12,
    0,    0,   12,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   41,   41,   45,   41,   61,    0,   41,   40,   74,   59,
   41,   41,   41,   45,   40,   45,   61,    0,   40,   61,
  178,   41,   40,  271,   59,   45,   39,  123,   61,    0,
   59,   40,    0,   45,   11,   61,   44,   45,   40,   61,
  256,    0,   40,   61,    0,  123,  256,   40,  256,  256,
    0,   40,   61,  256,   41,  256,   40,  256,  265,   61,
  276,   40,  272,   41,  272,  223,  132,  133,   61,   41,
  271,   45,   59,   40,  277,  278,  256,  276,  256,   45,
  271,   41,  256,  263,  262,  265,   41,   59,  150,  151,
   44,  273,   41,   41,   40,  269,  270,   41,   43,  125,
   45,  114,   45,   77,   78,   79,   42,   59,   45,  256,
   59,   47,   45,   45,  261,   42,   44,  264,   41,  266,
   47,   45,   41,  125,  271,   45,  256,  277,  278,   44,
  125,  261,   40,   45,  264,   45,  266,   41,   45,   96,
   97,  271,  125,   45,   11,   59,   45,  272,   59,  106,
  107,  164,   61,   59,  125,   45,   59,  125,   59,  263,
  256,  265,   41,  176,  177,  178,  125,   59,   41,  125,
  183,  184,  185,  239,   59,  125,  256,   59,  256,  262,
   61,   41,   42,   43,  263,   45,  265,   47,   41,  269,
  270,  253,  254,  255,  263,  275,  265,   61,   41,  256,
   60,   41,   62,   43,   59,   45,  256,  263,   59,  265,
  223,  256,  225,  256,  256,   41,  256,  230,  256,   59,
   60,  256,   62,  256,  256,  256,  256,  256,  271,  272,
  271,  269,  270,  273,   59,  276,  256,  275,  269,  270,
  272,  271,  272,  265,  275,  271,  259,  114,  261,  271,
  263,  271,  272,  271,  263,  268,  265,  270,  256,  272,
  272,  256,  271,  271,  272,  278,  261,  256,  263,  264,
  265,  266,  256,  256,  269,  270,  271,  256,  261,  274,
  263,  264,  265,  266,  256,  256,  269,  270,  271,  256,
  261,  274,  263,  264,  265,  266,  256,  164,  269,  270,
  271,  256,  256,  274,  271,  271,  272,  256,  256,  176,
  177,  178,  256,  256,  269,  270,  183,  184,  185,  256,
  275,  269,  270,  256,  256,  269,  270,  275,  271,  272,
  273,  275,  256,  256,  271,  272,  256,  256,  271,  272,
  272,  256,  256,  272,  256,  256,  256,  271,  272,  256,
  256,  271,  272,  256,  256,  256,  223,  256,  225,  271,
  272,  271,  272,  230,  271,  272,  256,   26,   27,  271,
  272,  256,  271,  272,  256,  100,  263,  263,  265,  265,
  105,  271,  272,  108,   41,   43,   41,   45,  271,   41,
   59,   43,  259,   45,  261,  271,  263,  257,  258,  259,
  260,  268,   60,  270,   62,  272,  276,   59,   60,   59,
   62,  278,  262,  276,  262,  262,  256,  257,  258,  259,
  260,  123,   42,   43,  262,   45,   41,   47,   43,   59,
   45,  272,  445,  446,  447,  448,  449,  450,  451,  452,
  453,  454,  455,  456,   59,   60,   41,   62,   43,  125,
   45,   41,  272,   43,  272,   45,   42,   43,   59,   45,
   59,   47,  276,   41,   59,   60,  276,   62,   41,   59,
   60,  271,   62,   59,   60,   41,   62,  490,  491,  492,
  493,  494,  495,  496,  497,  498,  499,  500,  465,  502,
  467,   41,  469,   43,  471,   45,  473,   41,  475,   43,
  477,   45,  479,  271,  481,   37,  483,   61,  485,   59,
   60,  488,   62,   45,  276,   59,   60,  276,   62,   42,
   43,   61,   45,   59,   47,   59,   59,   44,   59,   59,
   42,   43,   61,   45,  276,   47,   61,   60,  276,   62,
   61,  276,  123,   25,   44,   77,   78,   79,   60,   81,
   62,   33,   34,  123,  256,   61,   61,   61,   59,  261,
  219,  263,  264,  265,  266,   59,   61,  269,  270,  271,
  123,   61,  274,   61,  233,  234,  235,  236,  237,  238,
  256,   61,  123,   59,   59,  261,   59,   59,  264,   59,
  266,  265,   74,   59,  256,  271,   59,  125,  256,  257,
  258,  259,  260,   59,  256,  257,  258,  259,  260,  265,
   59,   59,   44,  123,   59,   59,   98,   99,  150,  151,
  123,  123,   59,  265,  123,  123,  123,  109,  110,  265,
  123,  123,  123,  331,  332,  333,  334,  335,  336,  337,
  338,  256,  257,  258,  259,  260,  123,  265,   59,  123,
  132,  133,  123,  265,  123,  265,  265,   59,  123,   59,
  256,  256,  257,  258,  259,  260,  256,  257,  258,  259,
  260,  257,  258,  259,  260,  256,  123,  123,  272,   59,
  261,  123,  123,  264,  265,  266,  256,  123,  269,  270,
  271,  261,  123,  274,  264,  265,  266,  123,  123,  269,
  270,  271,  123,  123,  274,   59,  256,  257,  258,  259,
  260,  123,  256,  257,  258,  259,  260,  123,  125,   59,
  125,  253,  254,  255,   59,   59,  272,   59,  123,   59,
   59,  125,  125,  256,  257,  258,  259,  260,  125,  125,
  125,  125,  125,  125,  125,  257,  258,  259,  260,   41,
  272,  125,  272,  256,  256,  125,  125,  239,  261,  261,
  125,  264,  264,  266,  266,  256,  269,  270,  271,  271,
  261,  274,  125,  264,  125,  266,  125,  125,   41,  256,
  271,   41,   41,  272,  261,  256,  272,  264,  125,  266,
  261,  256,  125,  264,  271,  266,  261,   41,   41,  264,
  271,  266,   41,   41,  125,  125,  271,   41,  272,  256,
  256,  272,  272,   41,  261,  261,  125,  264,  264,  266,
  266,  125,  125,  125,  271,  271,  125,  125,  125,  125,
  256,  256,   59,   41,   41,  261,  261,   41,  264,  264,
  266,  266,   41,   54,  256,  271,  271,   41,   41,  261,
  256,   41,  264,   86,  266,  261,   -1,   -1,  264,  271,
  266,  256,  256,   -1,   -1,  271,  261,  261,   -1,  264,
  264,  266,  266,  256,  256,   -1,  271,  271,  261,  261,
   -1,  264,  264,  266,  266,   -1,  256,  256,  271,  271,
   -1,  261,  261,   -1,  264,  264,  266,  266,   -1,   -1,
   -1,  271,  271,  256,   -1,   -1,   -1,  256,  261,   -1,
   -1,  264,  261,  266,   -1,  264,   -1,  266,  271,  256,
   -1,   -1,  271,  256,  261,  301,   -1,  264,  261,  266,
  306,  264,   -1,  266,  271,  256,  256,   -1,  271,   -1,
  261,  261,  164,  264,  264,  266,  266,   -1,   -1,   -1,
  271,  271,   -1,   -1,  176,  177,   -1,   -1,   -1,   -1,
   -1,  183,  184,  185,  340,   -1,  342,   -1,  344,   -1,
  346,  347,  348,  349,  350,   -1,  256,   -1,   -1,   -1,
  356,  261,  358,   -1,  264,   -1,  266,   -1,   -1,  269,
  270,  271,   -1,   -1,  274,   -1,   -1,   -1,   -1,   -1,
   -1,  213,   -1,  225,   -1,  217,   -1,   -1,  230,  385,
   -1,  387,   -1,  389,   -1,   -1,   -1,   -1,   -1,   -1,
  396,  397,  398,  399,  400,   -1,   -1,   -1,  240,   -1,
  242,   -1,  244,   -1,  246,  247,  248,  259,   -1,  261,
  252,  263,   -1,   -1,   -1,  257,  268,  256,  270,   -1,
  272,   -1,  261,   -1,   -1,  264,  278,  266,  256,   -1,
  269,  270,  271,  261,   -1,  274,  264,   -1,  266,   -1,
   -1,  269,  270,  271,   -1,   -1,  274,  289,   -1,  291,
   -1,  293,   -1,   -1,  256,   -1,  298,  299,  300,  261,
   -1,   -1,  264,   -1,  266,   -1,   -1,  269,  270,  271,
   -1,   -1,  274,
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
"declaracion_procedimiento : PROC ID '(' ')' NI '=' cte '{' error '}'",
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
"sentencia_asignacion : ID '=' factor ';'",
"sentencia_asignacion : error '=' factor ';'",
"sentencia_asignacion : ID '=' error ';'",
"sentencia_asignacion : ID '=' factor error",
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

//#line 263 "gramatica.y"

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
//#line 807 "Parser.java"
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
//#line 102 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el bloque de sentencias en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
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
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 52:
//#line 108 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 53:
//#line 109 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 54:
//#line 110 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 55:
//#line 113 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 56:
//#line 114 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 57:
//#line 115 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 58:
//#line 116 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta definir el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 59:
//#line 119 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 60:
//#line 120 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 61:
//#line 121 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 62:
//#line 122 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 63:
//#line 123 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 64:
//#line 124 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 65:
//#line 125 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 66:
//#line 126 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 67:
//#line 127 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 68:
//#line 128 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en condición en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 69:
//#line 129 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 70:
//#line 130 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 71:
//#line 131 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 72:
//#line 132 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 73:
//#line 133 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 74:
//#line 134 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 75:
//#line 135 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 76:
//#line 136 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 77:
//#line 137 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 78:
//#line 138 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 79:
//#line 139 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 80:
//#line 140 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 81:
//#line 141 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 82:
//#line 142 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 83:
//#line 143 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 84:
//#line 144 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 85:
//#line 145 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan bloques de sentencias luego de THEN y ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 86:
//#line 148 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 87:
//#line 149 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 88:
//#line 150 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 89:
//#line 151 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 90:
//#line 152 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 91:
//#line 153 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 92:
//#line 154 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 93:
//#line 155 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 94:
//#line 156 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 95:
//#line 157 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 96:
//#line 158 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 97:
//#line 159 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 98:
//#line 160 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 99:
//#line 161 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 100:
//#line 162 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 101:
//#line 163 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 102:
//#line 164 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 103:
//#line 165 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 104:
//#line 166 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 105:
//#line 167 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 106:
//#line 168 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 107:
//#line 169 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 108:
//#line 170 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 109:
//#line 171 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 110:
//#line 172 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 111:
//#line 173 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 114:
//#line 180 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 115:
//#line 181 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 116:
//#line 182 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 117:
//#line 183 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 118:
//#line 184 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 119:
//#line 185 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 120:
//#line 186 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 121:
//#line 189 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 122:
//#line 190 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 123:
//#line 191 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 124:
//#line 192 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 125:
//#line 195 "gramatica.y"
{System.out.println("Sentencia de invocacion con lista de parametros");}
break;
case 126:
//#line 196 "gramatica.y"
{System.out.println("Sentencia de invocacion sin parametros");}
break;
case 127:
//#line 197 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parámetros inválidos " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 128:
//#line 198 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 129:
//#line 199 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta paréntesis " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 130:
//#line 200 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 131:
//#line 201 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ';' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 132:
//#line 204 "gramatica.y"
{System.out.println("Lista de parametros: 3 parametros");}
break;
case 133:
//#line 205 "gramatica.y"
{System.out.println("Lista de parametros: 2 parametros");}
break;
case 134:
//#line 206 "gramatica.y"
{System.out.println("Lista de parametros: 1 parametro");}
break;
case 135:
//#line 207 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Numero de parametros excedido " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 136:
//#line 208 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Parametro incorrecto " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 137:
//#line 209 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 138:
//#line 210 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 141:
//#line 217 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta comparador " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 142:
//#line 218 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la segunda expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 143:
//#line 219 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta la primera expresion de la condicion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 144:
//#line 222 "gramatica.y"
{yyval = val_peek(0);}
break;
case 145:
//#line 223 "gramatica.y"
{yyval = val_peek(0);}
break;
case 146:
//#line 224 "gramatica.y"
{yyval = new ParserVal('>');}
break;
case 147:
//#line 225 "gramatica.y"
{yyval = new ParserVal('<');}
break;
case 148:
//#line 226 "gramatica.y"
{yyval = val_peek(0);}
break;
case 149:
//#line 227 "gramatica.y"
{yyval = val_peek(0);}
break;
case 153:
//#line 233 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 154:
//#line 234 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 155:
//#line 235 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la suma " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 156:
//#line 236 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la resta " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 160:
//#line 242 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 161:
//#line 243 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 162:
//#line 244 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la division " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 163:
//#line 245 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[Linea %d]- ERROR | Falta un termino en la multiplicacion " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 164:
//#line 248 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 166:
//#line 252 "gramatica.y"
{String cte = val_peek(0).sval;
           checkRango(cte, false);
           yyval = val_peek(0);
           }
break;
case 167:
//#line 256 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  checkRango(cte, true);
      		  yyval = new ParserVal("-" + cte);
     	 	}
break;
//#line 1482 "Parser.java"
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
