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



package compilador;



//#line 2 "gramatica.y"
import compilador.codigoIntermedio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug = true;        //do I want debug output?
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
    0,    2,    2,    2,    2,    5,    5,    1,    1,    6,
    6,    4,    4,    3,    3,    3,    3,    3,    7,    7,
    7,   15,   15,   15,   14,   14,    8,    8,   16,   16,
   20,   20,   20,   20,   20,   20,   20,   18,   18,   18,
   18,   19,   19,   19,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   21,   21,   21,   21,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,   22,   22,   22,   22,   22,
   23,   24,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   26,   26,   26,   26,   26,   29,   29,   29,   29,
   27,   28,   28,   11,   11,   11,   11,   11,   11,   11,
   11,   12,   12,   12,   12,   12,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   25,   25,   25,   25,   34,   34,
   34,   34,   34,   34,   32,   32,   32,   32,   32,   32,
   35,   35,   35,   35,   35,   35,   35,   31,   31,   30,
   30,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    3,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    3,    1,    2,    1,    1,    4,    3,    2,    2,
    3,    2,    3,    2,    3,    2,    2,    3,    2,    3,
    3,    3,    3,    3,    7,    5,    3,    2,    6,    6,
    4,    9,    5,    2,    3,    2,    3,    6,    8,    5,
    7,    7,    5,    7,    6,    8,    5,    5,    7,    8,
    7,    8,    7,    5,    7,    3,    2,    2,    2,    1,
    1,    1,    8,    7,    8,    9,    8,    8,    7,    8,
    8,    4,    4,    4,    4,    3,    3,    1,    3,    3,
    2,    1,    1,    5,    5,    4,    5,    5,    4,    5,
    4,    4,    4,    4,    4,    3,    5,    4,    4,    3,
    5,    4,    3,    4,    3,    5,    3,    1,    7,    3,
    3,    4,    4,    2,    3,    3,    3,    3,    1,    1,
    1,    1,    1,    1,    3,    3,    1,    3,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    1,    1,    1,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   25,   26,    0,    0,    0,    0,
   10,   11,    9,   12,   13,   14,   15,   16,   17,   18,
    0,    0,    0,    0,    0,  158,  160,    0,    0,    0,
    0,  159,  153,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   30,   29,    8,
    0,    0,    0,    0,    0,    0,    0,  143,  139,  140,
  144,  141,  142,    0,    0,    0,    0,    0,   81,    7,
    0,   79,    0,  161,    0,    0,   77,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  123,    0,    0,  120,    0,    0,    0,   21,    0,   24,
   20,   19,    0,    0,   48,    0,    0,    0,    0,    0,
    0,    0,   28,  113,    0,    0,  156,  157,    0,    0,
    0,    0,   76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  154,  151,  155,  152,
    0,    0,   96,    0,    0,    0,    0,    0,    0,  101,
  102,  103,    0,    0,    0,    0,    0,  106,    0,  114,
  131,    0,  130,    0,    0,  118,  122,    0,  115,  112,
  119,   22,   56,    0,    0,   54,    0,   47,    0,    0,
    0,    0,    0,    0,   39,   27,    6,   82,    0,   74,
    0,    0,    0,   63,    0,    0,    0,    0,    0,   60,
   93,   94,   95,   92,    0,    0,    0,    0,    0,    0,
  105,  107,  108,  104,  110,  133,  132,    0,  121,  117,
   57,   55,    0,    0,    0,   51,   43,   40,   41,   38,
   44,   42,    0,    0,   65,    0,    0,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   53,    0,   46,    0,    0,   75,    0,   64,    0,   69,
    0,    0,   62,   61,    0,    0,    0,    0,    0,    0,
    0,    0,   98,   89,   84,    0,    0,   50,   49,   66,
   70,   72,   59,   85,   91,    0,   87,   88,    0,    3,
    4,    0,   90,   83,  129,    0,   45,   86,   99,    2,
    5,    0,   97,    0,   52,
};
final static short yydgoto[] = {                          9,
   10,  299,   11,   12,  198,   70,   14,   15,   16,   17,
   18,   19,   20,   21,   53,   22,   55,  122,  123,    0,
  117,   30,   71,  199,   90,   39,   91,  163,  284,   32,
   33,   34,   47,   67,   35,
};
final static short yysindex[] = {                       718,
  -43,  -32,  -24,  -36,    0,    0,  -29, -206,    0,  718,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -204,   47,  157,  542,  598,    0,    0,  154, -227,  489,
   63,    0,    0,  547,   28,   51,  -31,  -50,  269, -141,
  147,  157,  -25,   -3,   64,  277,  102,    0,    0,    0,
   96,  -16,  -42,  -19,   -7,  158,   -8,    0,    0,    0,
    0,    0,    0,  290,   49,   49,  157,  718,    0,    0,
  258,    0,  122,    0,  -87,  305,    0,  157,  295,  343,
  361,  371,  379,  -86,  -20,  174,  151,  269,  409,  139,
  -74,  167,  175,  -39,  189,  247,  130,    3, -161,  282,
    0,  259,   17,    0,  138,  538,  297,    0,   65,    0,
    0,    0,   89,  183,    0,  133,  -41,  718,  -30,  729,
  137, -100,    0,    0,  115,   28,    0,    0,  543,  629,
  598,  358,    0,  -56,  598,  362,  473,  598,  384,  543,
  115,   28,  115,   28,  158,  543,    0,    0,    0,    0,
  385,  388,    0,  389,  392,  -74,  -74,  468, -179,    0,
    0,    0,  201,  402,  407,  417,  418,    0,  431,    0,
    0,  242,    0,    5,  457,    0,    0,  463,    0,    0,
    0,    0,    0,  255,  261,    0,  216,    0,  272,  640,
  263,   37,  -46,  656,    0,    0,    0,    0,  291,    0,
  598,  506,  310,    0,  -43,  561,  517,  315,  322,    0,
    0,    0,    0,    0,  319,  321,  -74,  326,  143,  553,
    0,    0,    0,    0,    0,    0,    0,  330,    0,    0,
    0,    0,  -27,  -34,  249,    0,    0,    0,    0,    0,
    0,    0,  549,  341,    0,  563,  -55,  564, -150,    0,
  571,  575,  596,  607,  383,  613,  615,  339,  524,  616,
    0,  249,    0,  634,  642,    0,  626,    0,  627,    0,
  628,  630,    0,    0,  524,  524,  647,  524,  524,  445,
  718,  609,    0,    0,    0,  401,  367,    0,    0,    0,
    0,    0,    0,    0,    0,  524,    0,    0,  667,    0,
    0,  683,    0,    0,    0,   47,    0,    0,    0,    0,
    0,  -43,    0,  650,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  692,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  505,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,   32,    0,    0,    0,    0,    0,    0,    0,
    0,  -33,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  587,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   55,    0,    0,
    0,  120,    0,    0,    0,  171,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   21,    0,    0,  131,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  397,
   41,   68,   88,  108,  408,  434,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  194,    0,  218,    0,
    0,    0,    0,   93,    0,    0,    0,  238,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  273,    0,  289,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   95,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  308,  355,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  375,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -13,  412,  684,  215,   18,  652,    0,    0,    0,    0,
    0,    0,    0,  559,  -12,    0,  391,    0,  572,    0,
  -97,    0,   -1,  -66,   31,  661,  382, -132, -157,  621,
   76,   11,  658,  678,   81,
};
final static int YYTABLESIZE=1003;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        188,
  147,  167,  187,   41,   23,   23,  263,   28,   88,  262,
   44,   45,   29,  261,   23,   38,  112,   23,   99,  189,
  150,  115,  120,  215,  216,   23,  219,  109,   76,   86,
  192,   46,   31,   57,   79,   68,   80,  102,  153,  110,
  148,  147,   69,  147,   74,  147,  172,   69,  228,   48,
  124,   51,   97,  121,  130,  101,  106,  178,   73,  147,
  147,  150,  147,  150,   49,  150,   52,  145,  203,   82,
  208,  209,  128,  137,   83,  177,  218,  129,  241,  150,
  150,  148,  150,  148,  255,  148,   54,  149,  140,  234,
  128,  146,   69,   29,  173,  134,  182,  161,  162,  148,
  148,  285,  148,   77,  190,  271,  194,  146,  145,  174,
  145,   84,  145,  134,  272,  120,   96,  294,  295,  125,
  297,  298,  104,  147,  304,  147,  145,  145,  149,  145,
  149,   92,  149,  127,  244,  126,  264,  265,  308,  249,
  127,  128,  107,  150,  126,  150,  149,  149,  146,  149,
  146,  127,  146,  126,  108,  118,   65,  148,  150,  142,
  144,   66,  133,  148,  287,  148,  146,  146,  134,  146,
  116,  137,   79,    2,   80,  135,    3,  136,    4,   65,
   64,    5,    6,    7,   66,  151,    8,   95,  170,  137,
  145,   29,  145,  109,   72,   29,  179,  160,   29,   65,
   64,   29,  161,  162,   66,   87,  201,  164,  202,  269,
  149,   84,  149,  111,  113,  165,  166,  111,   29,   40,
   37,  113,   23,   24,   85,  191,   42,    5,    6,   25,
  146,   36,  146,  114,    5,    6,  113,  124,   26,   27,
  114,   43,  125,  183,  125,   98,   37,  168,  118,    5,
    6,  152,  100,  137,   52,  114,  147,  147,  147,  147,
  147,  147,  147,  147,  147,  147,  147,   43,  119,  147,
  147,  147,   68,  171,  147,  227,  150,  150,  150,  150,
  150,  150,  150,  150,  150,  150,  150,  169,   67,  150,
  150,  150,  239,  116,  150,  116,  148,  148,  148,  148,
  148,  148,  148,  148,  148,  148,  148,   71,  240,  148,
  148,  148,  236,   29,  148,  235,  109,  176,  109,   26,
   27,   29,  175,  145,  145,  145,  145,  145,  145,  145,
  145,  145,  145,  145,   29,   52,  145,  145,  145,   29,
  111,  145,  111,  149,  149,  149,  149,  149,  149,  149,
  149,  149,  149,  149,   73,  181,  149,  149,  149,  183,
  124,  149,  124,  146,  146,  146,  146,  146,  146,  146,
  146,  146,  146,  146,  100,  125,  146,  146,  146,  282,
  125,  146,  125,  125,  125,  125,  137,   29,  125,  125,
  125,  137,  137,  125,  137,   68,  137,   68,  257,  137,
  137,  137,   93,  186,  137,   29,   24,  307,  195,   24,
  306,   67,   56,   67,  258,   29,  200,   26,   27,   94,
  204,   26,   27,   29,   26,   27,  116,   26,   27,  154,
   71,  116,   71,  116,  116,  116,  116,  136,  184,  116,
  116,  116,  210,  211,  116,   27,  212,  213,  138,  109,
  214,    5,    6,   29,  109,  136,  109,  109,  109,  109,
  221,  281,  109,  109,  109,  222,  138,  109,  156,  157,
  159,  233,  220,  111,  135,  223,  224,   73,  111,   73,
  111,  111,  111,  111,    5,    6,  111,  111,  111,  225,
  114,  111,  135,  124,  301,  301,  303,  100,  124,  100,
  124,  124,  124,  124,  113,   23,  124,  124,  124,   65,
   64,  124,  226,  311,   66,  229,  311,    5,    6,  136,
  131,  230,  132,  114,   24,  231,  217,   63,   68,   62,
  138,  232,  105,   68,  238,   68,   68,   68,   68,   26,
   27,   68,   68,   68,   67,  125,   68,   26,   27,   67,
  141,   67,   67,   67,   67,  243,  135,   67,   67,   67,
   26,   27,   67,   71,  245,   26,   27,  138,   71,  139,
   71,   71,   71,   71,  246,  250,   71,   71,   71,  251,
   79,   71,   80,   65,   64,   79,  252,   80,   66,   79,
  253,   80,  254,  259,  280,   68,  180,  256,  143,    2,
  260,   63,    3,   62,    4,  267,   63,  266,   62,    7,
   73,   68,  116,   26,   27,   73,  145,   73,   73,   73,
   73,  268,  270,   73,   73,   73,  147,   80,   73,  273,
  100,   26,   27,  274,  149,  100,  275,  100,  100,  100,
  100,   26,   27,  100,  100,  100,  281,  276,  100,   26,
   27,   13,  136,  278,  277,  279,  305,  136,  136,  286,
  136,   50,  136,  138,  158,  136,  136,  136,  138,  138,
  136,  138,  185,  138,  288,  116,  138,  138,  138,   26,
   27,  138,  289,   68,  290,  291,  292,  296,  293,  135,
  315,    1,  302,  196,  135,  135,  314,  135,   89,  135,
    1,  103,  135,  135,  135,    2,  155,  135,    3,   78,
    4,   81,    0,    5,    6,    7,    0,    0,    8,   13,
   68,    0,    0,    0,   58,   59,   60,   61,  205,    0,
    0,  281,    0,    2,    0,  206,    3,  207,    4,    0,
    0,    5,    6,    7,    1,  116,    8,    0,    0,    2,
   75,    0,    3,  197,    4,    0,    0,    5,    6,    7,
   80,    0,    8,    0,  237,   80,   80,    0,   80,   13,
   80,   13,    0,   80,   80,   80,    0,    0,   80,  280,
  242,   50,    0,    0,    2,    0,    0,    3,    0,    4,
    0,  309,  116,  116,    7,    0,    0,    0,   58,   59,
   60,   61,   78,   58,   59,   60,   61,  313,    0,    0,
    0,    0,    0,    0,    0,    0,  247,    0,    0,    0,
  116,    2,    0,    0,    3,  248,    4,    0,    0,    5,
    6,    7,    0,    0,    8,    0,    0,    0,    0,    0,
    0,   50,   78,    0,    0,   50,    0,   78,   78,    0,
   78,    0,   78,    1,    0,   78,   78,   78,    2,    0,
   78,    3,    0,    4,  280,    0,    5,    6,    7,    2,
    0,    8,    3,    0,    4,    0,    0,    5,    6,    7,
    0,    0,    8,    0,    1,    0,    0,    0,    0,    2,
    0,    0,    3,    0,    4,    1,    0,    5,    6,    7,
    2,    0,    8,    3,    0,    4,    0,    0,    5,    6,
    7,    1,    0,    8,    0,    0,    2,    0,    0,    3,
    0,    4,    1,    0,    5,    6,    7,    2,    0,    8,
    3,    0,    4,    0,    0,    5,    6,    7,  312,    0,
    8,  283,  283,    2,    0,    0,    3,    0,    4,    0,
    0,    5,    6,    7,    0,    0,    8,    0,  283,  283,
    0,  283,  283,  300,  300,  283,    0,    0,    0,    0,
    0,    0,    0,    1,    0,    0,    0,    0,    2,  283,
    0,    3,  310,    4,  193,  310,    5,    6,    7,    2,
    0,    8,    3,    0,    4,    0,    0,    5,    6,    7,
    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   44,   40,   61,   61,   41,   40,   59,   44,
   40,   41,   45,   41,   61,   40,   59,   61,   44,  117,
    0,   41,  123,  156,  157,   59,  159,   44,   30,   61,
   61,   61,    2,   23,   43,  123,   45,   41,   59,   52,
    0,   41,   25,   43,  272,   45,   44,   30,   44,  256,
   59,  256,   42,   61,   68,   59,   46,   41,   28,   59,
   60,   41,   62,   43,  271,   45,  271,    0,  135,   42,
  137,  138,   41,   75,   47,   59,  256,   67,  125,   59,
   60,   41,   62,   43,  217,   45,   40,    0,   78,  187,
   59,   81,   75,   45,  256,   41,  109,  277,  278,   59,
   60,  259,   62,   41,  118,  256,  120,    0,   41,  271,
   43,   61,   45,   59,  265,  123,   41,  275,  276,    0,
  278,  279,   59,  123,  282,  125,   59,   60,   41,   62,
   43,  273,   45,   41,  201,   41,  234,  235,  296,  206,
   65,   66,   41,  123,   64,  125,   59,   60,   41,   62,
   43,   59,   45,   59,   59,  256,   42,   82,   83,   79,
   80,   47,   41,  123,  262,  125,   59,   60,  256,   62,
    0,   41,   43,  261,   45,  263,  264,  265,  266,   42,
   43,  269,  270,  271,   47,  272,  274,   41,   59,   59,
  123,   45,  125,    0,   41,   45,   59,   59,   45,   42,
   43,   45,  277,  278,   47,  256,  263,   41,  265,  265,
  123,   61,  125,  256,  256,   41,  256,    0,   45,  256,
  271,  256,  256,  256,  256,  256,  256,  269,  270,  262,
  123,  256,  125,  275,  269,  270,  256,    0,  271,  272,
  275,  271,  123,  271,  125,  271,  271,   59,  256,  269,
  270,  272,  256,  123,  271,  275,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  271,  276,  269,
  270,  271,    0,  271,  274,  271,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,   41,    0,  269,
  270,  271,  256,  123,  274,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,    0,  272,  269,
  270,  271,   41,   45,  274,   44,  123,   59,  125,  271,
  272,   45,   41,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,   45,  271,  269,  270,  271,   45,
  123,  274,  125,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,    0,   59,  269,  270,  271,  271,
  123,  274,  125,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,    0,  256,  269,  270,  271,   41,
  261,  274,  263,  264,  265,  266,  256,   45,  269,  270,
  271,  261,  262,  274,  264,  123,  266,  125,  256,  269,
  270,  271,  256,  271,  274,   45,  256,   41,  272,  256,
   44,  123,  256,  125,  272,   45,   59,  271,  272,  273,
   59,  271,  272,   45,  271,  272,  256,  271,  272,  256,
  123,  261,  125,  263,  264,  265,  266,   41,  256,  269,
  270,  271,   59,   59,  274,  272,   59,   59,   41,  256,
   59,  269,  270,   45,  261,   59,  263,  264,  265,  266,
   59,  123,  269,  270,  271,   59,   59,  274,   87,   88,
   89,  256,  272,  256,   41,   59,   59,  123,  261,  125,
  263,  264,  265,  266,  269,  270,  269,  270,  271,   59,
  275,  274,   59,  256,  280,  281,  282,  123,  261,  125,
  263,  264,  265,  266,  256,   61,  269,  270,  271,   42,
   43,  274,  271,  299,   47,   59,  302,  269,  270,  123,
  263,   59,  265,  275,  256,  271,   59,   60,  256,   62,
  123,  271,  256,  261,  272,  263,  264,  265,  266,  271,
  272,  269,  270,  271,  256,  256,  274,  271,  272,  261,
  256,  263,  264,  265,  266,  265,  123,  269,  270,  271,
  271,  272,  274,  256,   59,  271,  272,  263,  261,  265,
  263,  264,  265,  266,  265,   59,  269,  270,  271,  265,
   43,  274,   45,   42,   43,   43,  265,   45,   47,   43,
  272,   45,  272,   41,  256,  123,   59,  272,  256,  261,
  271,   60,  264,   62,  266,  265,   60,   59,   62,  271,
  256,  123,   54,  271,  272,  261,  256,  263,  264,  265,
  266,   59,   59,  269,  270,  271,  256,  123,  274,   59,
  256,  271,  272,   59,  256,  261,   41,  263,  264,  265,
  266,  271,  272,  269,  270,  271,  123,   41,  274,  271,
  272,    0,  256,   41,  272,   41,  256,  261,  262,   44,
  264,   10,  266,  256,  256,  269,  270,  271,  261,  262,
  274,  264,  114,  266,   41,  117,  269,  270,  271,  271,
  272,  274,   41,  123,   59,   59,   59,   41,   59,  256,
   41,    0,  281,  122,  261,  262,  306,  264,   38,  266,
  256,   44,  269,  270,  271,  261,   86,  274,  264,  123,
  266,   34,   -1,  269,  270,  271,   -1,   -1,  274,   68,
  123,   -1,   -1,   -1,  257,  258,  259,  260,  256,   -1,
   -1,  123,   -1,  261,   -1,  263,  264,  265,  266,   -1,
   -1,  269,  270,  271,  256,  187,  274,   -1,   -1,  261,
  262,   -1,  264,  125,  266,   -1,   -1,  269,  270,  271,
  256,   -1,  274,   -1,  125,  261,  262,   -1,  264,  118,
  266,  120,   -1,  269,  270,  271,   -1,   -1,  274,  256,
  125,  130,   -1,   -1,  261,   -1,   -1,  264,   -1,  266,
   -1,  125,  234,  235,  271,   -1,   -1,   -1,  257,  258,
  259,  260,  256,  257,  258,  259,  260,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,
  262,  261,   -1,   -1,  264,  265,  266,   -1,   -1,  269,
  270,  271,   -1,   -1,  274,   -1,   -1,   -1,   -1,   -1,
   -1,  190,  256,   -1,   -1,  194,   -1,  261,  262,   -1,
  264,   -1,  266,  256,   -1,  269,  270,  271,  261,   -1,
  274,  264,   -1,  266,  256,   -1,  269,  270,  271,  261,
   -1,  274,  264,   -1,  266,   -1,   -1,  269,  270,  271,
   -1,   -1,  274,   -1,  256,   -1,   -1,   -1,   -1,  261,
   -1,   -1,  264,   -1,  266,  256,   -1,  269,  270,  271,
  261,   -1,  274,  264,   -1,  266,   -1,   -1,  269,  270,
  271,  256,   -1,  274,   -1,   -1,  261,   -1,   -1,  264,
   -1,  266,  256,   -1,  269,  270,  271,  261,   -1,  274,
  264,   -1,  266,   -1,   -1,  269,  270,  271,  256,   -1,
  274,  258,  259,  261,   -1,   -1,  264,   -1,  266,   -1,
   -1,  269,  270,  271,   -1,   -1,  274,   -1,  275,  276,
   -1,  278,  279,  280,  281,  282,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,  261,  296,
   -1,  264,  299,  266,  256,  302,  269,  270,  271,  261,
   -1,  274,  264,   -1,  266,   -1,   -1,  269,  270,  271,
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
"programa : lista_sentencias",
"bloque_ejecutable : bloque_ejecutable sentencia_ejecutable",
"bloque_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : sentencia_declarativa",
"bloque_ejecutable : bloque_ejecutable sentencia_declarativa",
"bloque_sentencias : '{' lista_sentencias '}'",
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
"declaracion_variables : tipo lista_variables error",
"declaracion_variables : tipo error ';'",
"lista_variables : ID ',' lista_variables",
"lista_variables : ID",
"lista_variables : ID lista_variables",
"tipo : LONGINT",
"tipo : FLOAT",
"declaracion_procedimiento : proc_encabezado lista_parametros_formales proc_ni proc_cuerpo",
"declaracion_procedimiento : proc_encabezado lista_parametros_formales proc_cuerpo",
"proc_encabezado : PROC ID",
"proc_encabezado : PROC error",
"proc_parametros : '(' lista_parametros_formales ')'",
"proc_parametros : '(' ')'",
"proc_parametros : error lista_parametros_formales ')'",
"proc_parametros : error ')'",
"proc_parametros : '(' error ')'",
"proc_parametros : '(' lista_parametros_formales",
"proc_parametros : '(' error",
"proc_ni : NI '=' CTE",
"proc_ni : '=' CTE",
"proc_ni : NI error CTE",
"proc_ni : NI '=' error",
"proc_cuerpo : '{' lista_sentencias '}'",
"proc_cuerpo : error lista_sentencias '}'",
"proc_cuerpo : '{' error '}'",
"lista_parametros_formales : '(' parametro_formal ',' parametro_formal ',' parametro_formal ')'",
"lista_parametros_formales : '(' parametro_formal ',' parametro_formal ')'",
"lista_parametros_formales : '(' parametro_formal ')'",
"lista_parametros_formales : '(' ')'",
"lista_parametros_formales : '(' parametro_formal parametro_formal ',' parametro_formal ')'",
"lista_parametros_formales : '(' parametro_formal ',' parametro_formal parametro_formal ')'",
"lista_parametros_formales : '(' parametro_formal parametro_formal ')'",
"lista_parametros_formales : '(' parametro_formal ',' parametro_formal ',' parametro_formal ',' lista_parametros_formales ')'",
"lista_parametros_formales : '(' parametro_formal ',' error ')'",
"parametro_formal : tipo ID",
"parametro_formal : VAR tipo ID",
"parametro_formal : error ID",
"parametro_formal : VAR error ID",
"sentencia_seleccion : IF condicion_if THEN bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN error END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN error ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then END_IF",
"sentencia_seleccion : IF condicion_if THEN bloque_then error",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE error END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else error",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else error ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else END_IF",
"sentencia_seleccion : IF THEN bloque_then END_IF ';'",
"sentencia_seleccion : IF THEN bloque_then ELSE bloque_else END_IF ';'",
"condicion_if : '(' condicion ')'",
"condicion_if : condicion ')'",
"condicion_if : '(' condicion",
"condicion_if : '(' ')'",
"condicion_if : condicion",
"bloque_then : bloque_sentencias",
"bloque_else : bloque_sentencias",
"sentencia_control : FOR '(' inicio_for condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR inicio_for condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' error condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for error ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for condicion_for error CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for condicion_for incr_decr error ')' bloque_for",
"sentencia_control : FOR '(' inicio_for condicion_for incr_decr CTE bloque_for",
"sentencia_control : FOR '(' inicio_for condicion_for incr_decr CTE ')' sentencia_declarativa",
"sentencia_control : FOR '(' ';' condicion_for incr_decr CTE ')' bloque_for",
"inicio_for : ID '=' cte ';'",
"inicio_for : error '=' CTE ';'",
"inicio_for : ID error CTE ';'",
"inicio_for : ID '=' error ';'",
"inicio_for : ID error ';'",
"bloque_for : '{' bloque_ejecutable '}'",
"bloque_for : sentencia_ejecutable",
"bloque_for : error bloque_ejecutable '}'",
"bloque_for : '{' bloque_ejecutable error",
"condicion_for : condicion ';'",
"incr_decr : UP",
"incr_decr : DOWN",
"sentencia_salida : OUT '(' CADENA_MULT ')' ';'",
"sentencia_salida : OUT error CADENA_MULT ')' ';'",
"sentencia_salida : OUT '(' ')' ';'",
"sentencia_salida : OUT '(' error ')' ';'",
"sentencia_salida : OUT '(' CADENA_MULT error ';'",
"sentencia_salida : OUT '(' CADENA_MULT ')'",
"sentencia_salida : OUT '(' factor ')' ';'",
"sentencia_salida : OUT '(' factor ')'",
"sentencia_asignacion : ID '=' expresion ';'",
"sentencia_asignacion : error '=' expresion ';'",
"sentencia_asignacion : ID error expresion ';'",
"sentencia_asignacion : ID '=' error ';'",
"sentencia_asignacion : ID '=' expresion",
"sentencia_invocacion : ID '(' lista_parametros ')' ';'",
"sentencia_invocacion : ID '(' ')' ';'",
"sentencia_invocacion : ID lista_parametros ')' ';'",
"sentencia_invocacion : ID ')' ';'",
"sentencia_invocacion : ID '(' error ')' ';'",
"sentencia_invocacion : ID '(' lista_parametros ';'",
"sentencia_invocacion : ID '(' ';'",
"sentencia_invocacion : ID '(' lista_parametros ')'",
"sentencia_invocacion : ID '(' ')'",
"lista_parametros : ID ',' ID ',' ID",
"lista_parametros : ID ',' ID",
"lista_parametros : ID",
"lista_parametros : ID ',' ID ',' ID ',' error",
"lista_parametros : ID ',' error",
"lista_parametros : ID ID ID",
"lista_parametros : ID ',' ID ID",
"lista_parametros : ID ID ',' ID",
"lista_parametros : ID ID",
"condicion : expresion comparador expresion",
"condicion : expresion error expresion",
"condicion : error comparador expresion",
"condicion : expresion comparador error",
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
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : termino '*' error",
"termino : termino '/' error",
"termino : error '*' factor",
"termino : error '/' factor",
"factor : ID",
"factor : cte",
"cte : CTE",
"cte : '-' CTE",
};

//#line 448 "gramatica.y"

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
		 Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: %d - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, flotante));
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
    if(uso.equals("Procedimiento"))
      nuevoLexema = lexema + "@" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length())-(lexema.length()+1));
    else
      nuevoLexema = lexema + "@" + ambitos.getAmbitos();

    if(!TablaSimbolos.existe(nuevoLexema)) {
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
        nuevoToken.addAtributo("uso", uso);
        nuevoToken.addAtributo("tipo", tipo);
        nuevoToken.addAtributo("contador", 0);
        nuevoToken.addAtributo("ambito", nuevoLexema.substring(lexema.length()+1, nuevoLexema.length()));
        if(uso.equals("Procedimiento")) {
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
        else if(uso.equals("Procedimiento")) {
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

//    if(uso.equals("Procedimiento") && !ambitosString.equals("main")) {
//        ambitosString = ambitosString.substring(0, (ambitosString.length()) - (ambitosList.get(ambitosList.size() - 1).length() + 1));
//        ambitosList.remove(ambitosList.size()-1);
//    }

    boolean declarado = false;
    while(!ambitosList.isEmpty()) {
      if(TablaSimbolos.existe(lexema + "@" + ambitosString)) {
        if((uso.equals("Parametro") && !TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals("Procedimiento")) || !uso.equals("Parametro")) {
          declarado = true;
          //if(!uso.equals("Procedimiento"))
          //  actualizarContadorID(lexema + "@" + ambitosString, false);
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
	    else if (uso.equals("Procedimiento"))
		Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
	    else if (uso.equals("Parametro"))
		Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
	}

    if(uso.equals("Parametro") && declarado)
        parametrosReales.add(lexema + "@" + ambitosString);

    if(uso.equals("Procedimiento") && declarado) {
        Token procedimiento = TablaSimbolos.getToken(lexema + "@" + ambitosString);

        // Si se trata de un procedimiento que se encuentra declarado, se chequea el número de invocaciones respecto del máximo permitido
        // TODO no hay que chequearlo acá
//        if(((Integer) procedimiento.getAtributo("contador") + 1) > (Integer) procedimiento.getAtributo("max. invocaciones"))
//            Errores.addError(String.format("[ASem] | Linea %d: Se supera el máximo de invocaciones del procedimiento %n", analizadorLexico.getNroLinea()));
//        else
          Token padre = null;
          if(!ambitosString.equals("main")) {
              String lexemaPadre = ambitosString.split("@")[ambitosString.split("@").length-1];
              lexemaPadre = lexemaPadre + "@" + ambitosString.substring(0, (ambitosString.length())-(lexemaPadre.length()+1));
              padre = TablaSimbolos.getToken(lexemaPadre);
          }
          procedimiento.addAtributo("padre", (padre != null) ? padre.getAtributo("numeroProc") : 0);


            // Si se trata de un procedimiento que se encuentra declarado, se chequea además que la cantidad de parámetros reales correspondan a los formales
            List<String> parametrosFormales = (List) procedimiento.getAtributo("parametros");
            if (parametrosReales.size() != parametrosFormales.size())
                Errores.addError(String.format("[ASem] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
                // Se chequea, por último, los tipos entre parámetros reales y formales
                boolean tiposCompatibles = true;
                for(int i = 0; i < parametrosReales.size(); i++) {
                    String tipoParametroReal = TablaSimbolos.getToken(parametrosReales.get(i)).getAtributo("tipo")+"";
                    if(!parametrosFormales.get(i).contains(tipoParametroReal)) {
                        Errores.addError(String.format("[ASem] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i+1));
                        tiposCompatibles = false;
                        break;
                    }
                }

                if(tiposCompatibles) {
                    SA6(lexema, parametrosFormales, parametrosReales);
                    actualizarContadorID(lexema + "@" + ambitosString, false);
              }
            }
        parametrosReales.clear();
    }

    if(declarado && !uso.equals("Procedimiento"))
      actualizarContadorID(lexema + "@" + ambitosString, false);
    // Se actualiza el contador de referencias
    actualizarContadorID(lexema, true);
}

public String getLexemaID() {
    String ambitosActuales = ambitos.getAmbitos();
    String id = ambitosActuales.split("@")[ambitosActuales.split("@").length-1];
    return(id + "@" + ambitosActuales.substring(0, (ambitosActuales.length())-(id.length()+1)));
}





public void out(String lex){
       Token token = TablaSimbolos.getToken(lex);
               if (token != null) {
                   if (token.getTipoToken().equals(Main.IDENTIFICADOR)) {
                       if (token.getAtributo("uso") != null) {
                           if (token.getAtributo("uso").equals(Main.VARIABLE)) {
                               Object valor = token.getAtributo("VALOR");
                               if (valor != null)
                                   System.out.println(token.getAtributo("VALOR") + "\n");
                               else
                                    Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no inicializada %n",analizadorLexico.getNroLinea()));
                           }
                       } else
                            Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no declarada %n",analizadorLexico.getNroLinea()));
                   }else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                       System.out.println(token.getLexema(false) + "\n");
               }
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
 		 Errores.addError(String.format("[AS] | Linea %d: Constante no es del tipo entero %n",analizadorLexico.getNroLinea()));
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

    // TODO revisar parametros VAR
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
//#line 1080 "Parser.java"
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
        nroUltimaLinea = analizadorLexico.getNroLinea();
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
case 4:
//#line 38 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }
break;
case 5:
//#line 39 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }
break;
case 19:
//#line 66 "gramatica.y"
{ imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea()); }
break;
case 20:
//#line 67 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 21:
//#line 68 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables %n",analizadorLexico.getNroLinea()));}
break;
case 22:
//#line 70 "gramatica.y"
{ declaracionID(val_peek(2).sval, Main.VARIABLE, ultimoTipo); }
break;
case 23:
//#line 71 "gramatica.y"
{ declaracionID(val_peek(0).sval, Main.VARIABLE, ultimoTipo); }
break;
case 24:
//#line 72 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n",analizadorLexico.getNroLinea()));}
break;
case 25:
//#line 75 "gramatica.y"
{ ultimoTipo = "LONGINT"; }
break;
case 26:
//#line 76 "gramatica.y"
{ ultimoTipo = "FLOAT"; }
break;
case 27:
//#line 79 "gramatica.y"
{ imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                                                                                                ambitos.eliminarAmbito(actualizarTablaSimbolos);
                                                                                    }
break;
case 28:
//#line 82 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta declarar el número de invocaciones permitido en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 29:
//#line 85 "gramatica.y"
{   ambitos.agregarAmbito(val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Procedimiento", null);
                            }
break;
case 30:
//#line 88 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 31:
//#line 91 "gramatica.y"
{   TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                        parametrosFormales.clear();
                                                    }
break;
case 32:
//#line 94 "gramatica.y"
{ TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales)); }
break;
case 33:
//#line 95 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 34:
//#line 96 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 35:
//#line 97 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 36:
//#line 98 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 37:
//#line 99 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 38:
//#line 102 "gramatica.y"
{   String cte = val_peek(0).sval;
                            if(!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                                Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                            else
                                TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        }
break;
case 39:
//#line 108 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 40:
//#line 109 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 41:
//#line 110 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 45:
//#line 119 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                                                                                                        TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                                        parametrosFormales.clear();
                                                                                                    }
break;
case 46:
//#line 123 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                                                                                TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                parametrosFormales.clear();
                                                                            }
break;
case 47:
//#line 127 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                                                            TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                            parametrosFormales.clear();
                                                        }
break;
case 48:
//#line 131 "gramatica.y"
{ TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales)); }
break;
case 49:
//#line 132 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 50:
//#line 133 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 51:
//#line 134 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 52:
//#line 135 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea())); }
break;
case 53:
//#line 136 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea())); }
break;
case 54:
//#line 139 "gramatica.y"
{ imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
break;
case 55:
//#line 144 "gramatica.y"
{ imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
break;
case 56:
//#line 149 "gramatica.y"
{  Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n",analizadorLexico.getNroLinea())); }
break;
case 57:
//#line 150 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n",analizadorLexico.getNroLinea())); }
break;
case 58:
//#line 153 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
									                                        polaca.addElem(new ElemPos(polaca.size()), true);
									                                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                                                        }
                                                                        else {
                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                                                        }
									                                }
break;
case 59:
//#line 163 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                                                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                                                                            polaca.addElem(new ElemPos(polaca.size()), true);
                                                                                            polaca.addElem(new EtiquetaElem(polaca.size()), false);
                                                                                        }
                                                                                        else {
                                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                                                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                                                                                        }
                    									                            }
break;
case 60:
//#line 173 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
break;
case 61:
//#line 174 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
break;
case 62:
//#line 175 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n",analizadorLexico.getNroLinea()));}
break;
case 63:
//#line 176 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 64:
//#line 177 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 65:
//#line 178 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 66:
//#line 179 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 67:
//#line 180 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d:  Falta literal ';' %n",nroUltimaLinea));}
break;
case 68:
//#line 181 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d:  Falta palabra reservada END_IF y literal ';' %n",nroUltimaLinea));}
break;
case 69:
//#line 183 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
break;
case 70:
//#line 184 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
break;
case 71:
//#line 185 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' %n",analizadorLexico.getNroLinea()));}
break;
case 72:
//#line 186 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n",analizadorLexico.getNroLinea()));}
break;
case 73:
//#line 187 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d:  Falta literal ';' %n",nroUltimaLinea));}
break;
case 74:
//#line 188 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d:  Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
break;
case 75:
//#line 189 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d:  Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
break;
case 76:
//#line 192 "gramatica.y"
{   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                            polaca.pushPos(true);
                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                                        }
                                        else {
                                            polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF), false);
                                        }
                                    }
break;
case 77:
//#line 201 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 78:
//#line 202 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 79:
//#line 203 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n",analizadorLexico.getNroLinea()));}
break;
case 80:
//#line 204 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n",analizadorLexico.getNroLinea()));}
break;
case 81:
//#line 209 "gramatica.y"
{   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
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
break;
case 83:
//#line 227 "gramatica.y"
{   SA3(val_peek(2).sval);
                                                                                                SA4(val_peek(5).sval , val_peek(4).sval);
                                                                                                SA5(val_peek(5).sval, val_peek(2).sval, val_peek(3).sval); /* id cte incr_decr*/
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
break;
case 84:
//#line 243 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 85:
//#line 244 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n",analizadorLexico.getNroLinea()));}
break;
case 86:
//#line 245 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n",analizadorLexico.getNroLinea()));  }
break;
case 87:
//#line 246 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n",analizadorLexico.getNroLinea())); }
break;
case 88:
//#line 247 "gramatica.y"
{  Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n",analizadorLexico.getNroLinea()));  }
break;
case 89:
//#line 248 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 90:
//#line 249 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea()));}
break;
case 91:
//#line 250 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));}
break;
case 92:
//#line 253 "gramatica.y"
{ yyval = val_peek(3);
			SA3(val_peek(1).sval);
			SA1(val_peek(1).sval);
			SA1(val_peek(3).sval);
			SA2(val_peek(2).sval);
			SA1(val_peek(3).sval);
			invocacionID(val_peek(3).sval, Main.VARIABLE);
			if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
			    polaca.pushPos(false);
			    polaca.addElem(new EtiquetaElem(polaca.size()), false);
			}
			else {
			    polacaProcedimientos.pushPos(ambitos.getAmbitos(), false);
			    polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
			}
		    }
break;
case 93:
//#line 269 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n",analizadorLexico.getNroLinea()));}
break;
case 94:
//#line 270 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error, el inicio del for debe ser una asignacion %n",analizadorLexico.getNroLinea()));}
break;
case 95:
//#line 271 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n",analizadorLexico.getNroLinea()));}
break;
case 96:
//#line 272 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n",analizadorLexico.getNroLinea()));}
break;
case 99:
//#line 285 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n",analizadorLexico.getNroLinea()));}
break;
case 100:
//#line 286 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n",nroUltimaLinea));}
break;
case 101:
//#line 289 "gramatica.y"
{ yyval =val_peek(1);
  				  if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                        polaca.pushPos(true);
                                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                    else {
                                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                }
break;
case 102:
//#line 301 "gramatica.y"
{ yyval = new ParserVal("+"); }
break;
case 103:
//#line 302 "gramatica.y"
{ yyval = new ParserVal("-"); }
break;
case 104:
//#line 305 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
						  SA1(val_peek(2).sval);
						  if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
						      polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
						  else
						      polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
					      }
break;
case 105:
//#line 312 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 106:
//#line 313 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n",analizadorLexico.getNroLinea()));}
break;
case 107:
//#line 314 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n",analizadorLexico.getNroLinea()));}
break;
case 108:
//#line 315 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 109:
//#line 316 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 110:
//#line 317 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
/*                                                        invocacionID($3.sval, Main.VARIABLE);*/
/*                                                        SA1($3.sval);*/
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
break;
case 111:
//#line 332 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 112:
//#line 337 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                                                    String id = val_peek(3).sval;
                                                    String cte = val_peek(1).sval;
                                                    Token token = TablaSimbolos.getToken(id);
                                                    if(token != null) {
                                                        SA1(id);
                                                        SA2(val_peek(2).sval);
                                                    }
                                                    invocacionID(id, Main.VARIABLE);
                                                }
break;
case 113:
//#line 347 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n",analizadorLexico.getNroLinea()));}
break;
case 114:
//#line 348 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n",analizadorLexico.getNroLinea()));}
break;
case 115:
//#line 349 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n",analizadorLexico.getNroLinea()));}
break;
case 116:
//#line 350 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 117:
//#line 353 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                                                                invocacionID(val_peek(4).sval, "Procedimiento");
                                                            }
break;
case 118:
//#line 356 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                                                invocacionID(val_peek(3).sval, "Procedimiento");
                                            }
break;
case 119:
//#line 359 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 120:
//#line 360 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 121:
//#line 361 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n",analizadorLexico.getNroLinea()));}
break;
case 122:
//#line 362 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 123:
//#line 363 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 124:
//#line 364 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 125:
//#line 365 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 126:
//#line 368 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                                            invocacionID(val_peek(4).sval, "Parametro");
                                            invocacionID(val_peek(2).sval, "Parametro");
                                            invocacionID(val_peek(0).sval, "Parametro");
                                        }
break;
case 127:
//#line 373 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                                    invocacionID(val_peek(2).sval, "Parametro");
                                    invocacionID(val_peek(0).sval, "Parametro");
                                }
break;
case 128:
//#line 377 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                                invocacionID(val_peek(0).sval, "Parametro");
                            }
break;
case 129:
//#line 380 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n",analizadorLexico.getNroLinea()));}
break;
case 130:
//#line 381 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n",analizadorLexico.getNroLinea()));}
break;
case 131:
//#line 382 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 132:
//#line 383 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 133:
//#line 384 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 134:
//#line 385 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 135:
//#line 389 "gramatica.y"
{ yyval = val_peek(2); }
break;
case 136:
//#line 390 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta comparador en condicion %n",analizadorLexico.getNroLinea()));}
break;
case 137:
//#line 391 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando de la comparacion %n",analizadorLexico.getNroLinea()));}
break;
case 138:
//#line 392 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando de la comparacion %n",nroUltimaLinea));}
break;
case 139:
//#line 395 "gramatica.y"
{ SA2(">="); }
break;
case 140:
//#line 396 "gramatica.y"
{ SA2("<="); }
break;
case 141:
//#line 397 "gramatica.y"
{ SA2(">");}
break;
case 142:
//#line 398 "gramatica.y"
{ SA2("<");}
break;
case 143:
//#line 399 "gramatica.y"
{ SA2("=="); }
break;
case 144:
//#line 400 "gramatica.y"
{ SA2("!="); }
break;
case 145:
//#line 403 "gramatica.y"
{yyval=val_peek(2);imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                                     SA2(val_peek(1).sval);}
break;
case 146:
//#line 405 "gramatica.y"
{yyval=val_peek(2);imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                                     SA2(val_peek(1).sval);}
break;
case 147:
//#line 407 "gramatica.y"
{yyval=val_peek(0);}
break;
case 148:
//#line 408 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n",analizadorLexico.getNroLinea()));}
break;
case 149:
//#line 409 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n",analizadorLexico.getNroLinea()));}
break;
case 150:
//#line 410 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n",analizadorLexico.getNroLinea()));}
break;
case 151:
//#line 413 "gramatica.y"
{ yyval = val_peek(2);
				imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
				SA2(val_peek(1).sval);}
break;
case 152:
//#line 416 "gramatica.y"
{ yyval = val_peek(2);
        			imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
        			SA2(val_peek(1).sval);}
break;
case 153:
//#line 419 "gramatica.y"
{yyval = val_peek(0);}
break;
case 154:
//#line 420 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
break;
case 155:
//#line 421 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la division %n",analizadorLexico.getNroLinea()));}
break;
case 156:
//#line 422 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
break;
case 157:
//#line 423 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la division %n",analizadorLexico.getNroLinea()));}
break;
case 158:
//#line 426 "gramatica.y"
{yyval = val_peek(0); SA1(val_peek(0).sval);
		invocacionID(val_peek(0).sval, Main.VARIABLE);}
break;
case 159:
//#line 428 "gramatica.y"
{yyval = val_peek(0); SA1(val_peek(0).sval);}
break;
case 160:
//#line 431 "gramatica.y"
{String cte = val_peek(0).sval;
	   String nuevo = checkPositivo(cte);
	   if (nuevo != null)
	   	yyval = new ParserVal(nuevo);
	   else
	   	yyval = new ParserVal(cte);
           }
break;
case 161:
//#line 438 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  String nuevo = checkRango(cte);
      		  if (nuevo != null){
      		  	yyval = new ParserVal(nuevo);
      		  	imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                              		        }
     	 	}
break;
//#line 1944 "Parser.java"
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
