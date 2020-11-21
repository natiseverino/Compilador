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
import compilador.codigoIntermedio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
//#line 23 "Parser.java"




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
    0,    2,    2,    2,    2,    5,    5,    1,    1,    6,
    6,    4,    4,    3,    3,    3,    3,    3,    7,    7,
    7,   15,   15,   15,   14,   14,    8,   16,   16,   16,
   17,   17,   17,   17,   17,   17,   17,   18,   18,   18,
   18,   19,   19,   19,   20,   20,   20,   20,   20,   20,
   20,   20,   21,   21,   21,   21,    9,    9,   22,   22,
   22,   22,   22,   22,   23,   24,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   26,   26,   26,   26,   26,
   29,   29,   27,   28,   28,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   12,   12,   12,   12,   12,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   31,   31,   31,   31,   31,   31,   31,   31,   31,
   25,   25,   25,   25,   32,   32,   32,   32,   32,   32,
   30,   30,   30,   30,   30,   30,   30,   33,   33,   33,
   33,   33,   33,   33,   34,   34,   35,   35,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    3,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    3,    1,    2,    1,    1,    4,    2,    2,    2,
    3,    2,    3,    2,    3,    2,    2,    3,    2,    3,
    3,    3,    3,    3,    5,    3,    1,    4,    4,    2,
    7,    3,    2,    3,    2,    3,    6,    8,    3,    2,
    2,    2,    1,    3,    1,    1,    9,    8,    9,   10,
    9,    9,    8,    9,    8,    3,    3,    3,    3,    2,
    3,    1,    2,    1,    1,    5,    5,    5,    5,    4,
    5,    4,    5,    5,    4,    4,    4,    4,    4,    3,
    5,    4,    4,    3,    4,    3,    5,    4,    3,    4,
    3,    5,    3,    1,    7,    3,    3,    4,    4,    2,
    3,    2,    3,    3,    1,    1,    1,    1,    1,    1,
    3,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   25,   26,    0,    0,    0,    0,
    0,   10,   11,    9,   12,   13,   14,   15,   16,   17,
   18,    0,    0,   29,    0,    0,    0,  145,  147,    0,
    0,    0,    0,    0,    0,  140,  146,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   30,
   28,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  129,  125,  126,  130,  127,  128,
    0,    0,    0,    0,    0,    0,   62,    0,  148,    0,
   60,  122,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  109,    0,    0,  106,    0,    0,
    0,    0,  104,    0,   21,    0,   24,   20,   19,    0,
    0,   34,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,   97,    0,    0,    0,  143,  144,    0,   64,
   59,    0,   65,    7,    0,    0,    0,    0,    0,    0,
    0,  141,  138,  142,  139,   77,   78,   79,   76,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   92,   98,  117,    0,  116,    0,    0,  102,  108,    0,
   99,   96,  105,   90,  103,   22,   55,    0,    0,   53,
   33,    0,    0,   35,   31,    0,    0,   39,    0,    0,
   27,   89,    0,    0,    0,    0,   83,   84,   85,    0,
    0,    0,    0,   91,   93,   87,   88,   94,   86,  119,
  118,    0,  107,  101,   56,   54,    0,    0,    0,   40,
   41,   38,    0,    0,    0,    6,   66,    0,   57,    0,
    0,    0,    0,    0,    0,    0,    0,   49,   48,   43,
   44,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   58,    0,    0,    0,   82,   75,    0,    0,
    0,    0,   73,   68,  115,    0,   69,    0,    3,    4,
    0,   71,   72,   74,   67,   51,   81,    2,    5,   70,
};
final static short yydgoto[] = {                         10,
   11,  278,   12,   13,  143,   14,   15,   16,   17,   18,
   19,   20,   21,   22,   58,   23,   61,  131,  201,  124,
  125,   32,  145,  238,  161,   41,  162,  210,  268,   34,
   49,   75,   35,   36,   37,
};
final static short yysindex[] = {                       298,
   74,  279,  186,   17,    0,    0,  -32, -125,  -23,    0,
  298,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -79,   36,    0, -236,  302,  495,    0,    0,  255,
 -224, -211,   37,  586,   -8,    0,    0,   16,  -42,  112,
   22, -136,  193,  302,  -40,  163,   91,  350,  126,    0,
    0,  133,  140,  167,    0,  156,    8,  -45,  -26,  -24,
  -55,  175,  442,  142,    0,    0,    0,    0,    0,    0,
  394,  394,  -17,  -17,  302,  598,    0,  178,    0,  154,
    0,    0,  399,  416,  422,  424,  427,  -52,  -49, -200,
  139,  429,  168,  429,  206,  284,  301,  324,  -31,  307,
  148,   30,  -74,  328,    0,  311,   39,    0,  614,  453,
  333,  337,    0,  338,    0,   72,    0,    0,    0,  145,
  -60,    0,  150,  385,  234,  -15,    0,  421,   64,  198,
 -112,  432,    0,   26,   -8,   -8,    0,    0,  303,    0,
    0,  298,    0,    0,  128,   26,   -8,   26,   -8,  442,
  303,    0,    0,    0,    0,    0,    0,    0,    0,  429,
  434, -160,  435, -160,  436,  449,  454,  458,  459,  460,
    0,    0,    0,  217,    0,   52,  462,    0,    0,  463,
    0,    0,    0,    0,    0,    0,    0,  243,  253,    0,
    0, -128,  488,    0,    0,  271, -177,    0,  298,  320,
    0,    0,  177,  154,  493, -160,    0,    0,    0,  294,
  518, -223,  316,    0,    0,    0,    0,    0,    0,    0,
    0,  285,    0,    0,    0,    0,  145,  278, -105,    0,
    0,    0,  259,   -2,  331,    0,    0,  310,    0,  321,
  538, -160,  326, -148,  555,  559, -105,    0,    0,    0,
    0,    0,  540,  566,  371,  336,  570,  572,  348,  371,
  360,  573,    0,  371,    5,  298,    0,    0,  585,  371,
  371,  275,    0,    0,    0,  374,    0,  354,    0,    0,
  371,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  633,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  372,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -43,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  376,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  577,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   61,    0,    0,    0,  166,    0,    0,    0,  189,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,  -48,    0,   21,    0,    0,
    0,    0,    0,    0,   24,   47,    0,    0,  -29,    0,
    0,    0,    0,    0,    0,   70,   93,  116,  143,   -9,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  212,
    0,    0,    0,    0,    0,   63,    0,    0,    0,  236,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -38,  -36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -37,    0,  455, -118,  440,  505,    0,    0,    0,    0,
    0,    0,    0,  592,  -22,    0,    0,    0,    0,  587,
  -89,    0,    0,    0,   19,  609,  -63, -121,  -91,   14,
   66,  619,  499,  589,    0,
};
final static int YYTABLESIZE=858;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
  133,   50,   52,  103,   46,  130,   45,   46,   47,  170,
  200,  124,   37,  119,  122,   23,  127,   53,   90,   47,
   33,   50,   52,  136,   46,  194,   45,   31,   48,  124,
  164,  123,  243,   86,  117,  193,   62,   25,   87,   64,
  133,  133,  213,  133,   25,  133,  137,   79,   78,  123,
   80,  116,  121,  208,  209,  158,   43,  101,   26,  133,
  133,  110,  133,  136,  136,   26,  136,   73,  136,  134,
  121,  159,   74,  174,   54,   60,   88,   81,  231,  180,
   94,   36,  136,  136,  240,  136,  137,  137,  139,  137,
  244,  137,  131,  186,  232,  222,  206,  179,  151,  212,
  114,  120,  228,  113,  203,  137,  137,  258,  137,  134,
  134,  107,  134,   25,  134,  135,  208,  209,  114,  120,
  256,  113,  251,  259,  197,  133,  112,  227,  134,  134,
   50,  134,  131,  131,   26,  131,   95,  131,  248,  249,
    5,    6,  132,  199,  112,   51,  121,  280,  136,  108,
  120,  131,  131,  284,  131,  135,  135,  262,  135,  289,
  135,  233,  235,    5,    6,  111,  111,  273,  274,  121,
   92,  137,  277,  112,  135,  135,   56,  135,  282,  283,
  285,  175,  132,  132,   83,  132,   84,  132,  100,  290,
   83,   57,   84,    9,  134,  188,  176,  160,  113,   88,
  133,  132,  132,  106,  132,  111,  172,  114,    5,    6,
  118,   95,   23,   89,  115,  132,    9,  131,  141,  156,
  129,  105,  157,   44,  169,   40,  163,   37,  100,  120,
  102,  126,  124,  100,   47,  110,   50,   52,   45,   46,
  135,   45,    5,    6,    5,    6,  165,   45,  121,   52,
  121,   95,  123,   28,   29,  187,  133,  133,  133,  133,
  133,  133,  133,  133,  133,  133,  133,  132,   24,  133,
  133,  133,   42,  121,  133,  110,  142,  192,   57,  136,
  136,  136,  136,  136,  136,  136,  136,  136,  136,  136,
  111,   59,  136,  136,  136,   77,   36,  136,    9,   31,
  173,  236,  137,  137,  137,  137,  137,  137,  137,  137,
  137,  137,  137,  100,    9,  137,  137,  137,   30,  196,
  137,  247,  221,   31,  166,  134,  134,  134,  134,  134,
  134,  134,  134,  134,  134,  134,   95,    9,  134,  134,
  134,  167,   57,  134,   24,   83,   31,   84,  131,  131,
  131,  131,  131,  131,  131,  131,  131,  131,  131,    9,
  110,  131,  131,  131,  168,  171,  131,   91,  177,  178,
    9,  135,  135,  135,  135,  135,  135,  135,  135,  135,
  135,  135,   39,  250,  135,  135,  135,    9,  272,  135,
  204,  183,  205,    9,   31,  184,  185,  266,  132,  132,
  132,  132,  132,  132,  132,  132,  132,  132,  132,    1,
    9,  132,  132,  132,    2,  187,  132,    3,  104,    4,
  190,  111,    5,    6,    7,  191,  111,    8,  111,  111,
  111,  111,    1,   45,  111,  111,  111,    2,   31,  111,
    3,   38,    4,   31,  100,    5,    6,    7,   96,  100,
    8,  100,  100,  100,  100,  252,   39,  100,  100,  100,
   31,  195,  100,   97,   98,   99,   31,   95,   31,  198,
  266,   31,   95,   31,   95,   95,   95,   95,  287,   31,
   95,   95,   95,   73,   71,   95,   72,  220,   74,  120,
  202,  110,  207,  266,  214,   83,  110,   84,  110,  110,
  110,  110,    5,    6,  110,  110,  110,  215,  121,  110,
   76,  182,  216,  225,    1,   55,  217,  218,  219,    2,
  223,  224,    3,  226,    4,   28,   29,    5,    6,    7,
    1,  229,    8,  120,   27,    2,   73,   71,    3,   72,
    4,   74,  230,    5,    6,    7,    5,    6,    8,   28,
   29,  239,  121,    1,   70,  246,   69,   63,    2,   73,
   71,    3,   72,    4,   74,  241,    5,    6,    7,  135,
  136,    8,   28,   29,  253,  234,  242,   70,  255,   69,
    2,  147,  149,    3,  144,    4,    1,  245,    5,    6,
    7,    2,  254,    8,    3,  260,    4,  257,  263,    5,
    6,    7,  261,  265,    8,  109,  264,  269,    2,    1,
  270,    3,  271,    4,    2,  275,  276,    3,    7,    4,
   28,   29,    5,    6,    7,  281,  265,    8,   83,  286,
   84,    2,    1,   63,    3,   80,    4,   61,  140,   73,
   71,    7,   72,  237,   74,   70,  128,   69,   93,  134,
  123,  123,   85,    0,  146,   73,   71,   70,   72,   69,
   74,  137,  138,    0,   28,   29,    0,    0,    0,   28,
   29,  148,  181,    0,  153,  155,    0,  150,    0,  152,
    0,    0,  154,    0,   27,    0,   28,   29,    0,    0,
  211,    0,   28,   29,   28,   29,    0,   28,   29,   28,
   29,    0,    0,    0,    0,   28,   29,   55,  144,  267,
    0,    0,  189,  267,  267,    0,  123,    0,  267,    0,
  279,    0,    0,    0,  267,  267,  267,    0,    0,    0,
    0,    0,  288,    0,    0,  267,    0,   55,    0,   55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   65,   66,   67,   68,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   65,   66,   67,   68,    0,    0,
    0,    0,    0,  123,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  123,
  123,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  123,    0,
    0,   82,   65,   66,   67,   68,    0,    0,    0,    0,
    0,    0,    0,    0,   65,   66,   67,   68,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   41,   44,   41,   61,   41,   40,   41,   41,
  123,   41,   61,   59,   41,   59,   41,   41,   61,   61,
    2,   61,   61,    0,   61,   41,   61,   45,   61,   59,
   94,   41,  256,   42,   57,  125,  273,   40,   47,   26,
   40,   41,  164,   43,   40,   45,    0,  272,   30,   59,
  262,   44,   41,  277,  278,  256,   40,   44,   61,   59,
   60,   48,   62,   40,   41,   61,   43,   42,   45,    0,
   59,  272,   47,   44,    9,   40,   61,   41,  256,   41,
   59,   61,   59,   60,  206,   62,   40,   41,   75,   43,
  212,   45,    0,  116,  272,   44,  160,   59,   85,  163,
   41,   41,  192,   41,  142,   59,   60,  256,   62,   40,
   41,   46,   43,   40,   45,    0,  277,  278,   59,   59,
  242,   59,  125,  272,   61,  125,   41,  256,   59,   60,
  256,   62,   40,   41,   61,   43,  273,   45,  228,  229,
  269,  270,    0,  256,   59,  271,  275,  266,  125,   59,
  256,   59,   60,  272,   62,   40,   41,  247,   43,  278,
   45,  199,  200,  269,  270,    0,   41,  259,  260,  275,
   59,  125,  264,   41,   59,   60,  256,   62,  270,  271,
  272,  256,   40,   41,   43,   43,   45,   45,    0,  281,
   43,  271,   45,   40,  125,  256,  271,   59,   59,   61,
   59,   59,   60,   41,   62,   40,   59,   41,  269,  270,
  256,    0,  256,  256,   59,   41,   40,  125,   41,  272,
  276,   59,  272,  256,  256,   40,   59,  276,   40,  256,
  271,  256,  262,   41,  276,    0,  276,  276,  271,  276,
  125,  276,  269,  270,  269,  270,   41,  271,  275,  273,
  275,   40,  262,  271,  272,  271,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  125,  271,  269,
  270,  271,  256,  262,  274,   40,  123,   44,  271,  256,
  257,  258,  259,  260,  261,  262,  263,  264,  265,  266,
  125,  256,  269,  270,  271,   41,  276,  274,   40,   45,
  271,  125,  256,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  125,   40,  269,  270,  271,   40,  256,
  274,   44,  271,   45,   41,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  125,   40,  269,  270,
  271,   41,  271,  274,  271,   43,   45,   45,  256,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,   40,
  125,  269,  270,  271,   41,   59,  274,  256,   41,   59,
   40,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  266,  271,  125,  269,  270,  271,   40,   41,  274,
  263,   59,  265,   40,   45,   59,   59,  123,  256,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  256,
   40,  269,  270,  271,  261,  271,  274,  264,  256,  266,
  271,  256,  269,  270,  271,   41,  261,  274,  263,  264,
  265,  266,  256,  271,  269,  270,  271,  261,   45,  274,
  264,  256,  266,   45,  256,  269,  270,  271,  256,  261,
  274,  263,  264,  265,  266,  125,  271,  269,  270,  271,
   45,   41,  274,  271,  272,  273,   45,  256,   45,  272,
  123,   45,  261,   45,  263,  264,  265,  266,  125,   45,
  269,  270,  271,   42,   43,  274,   45,  271,   47,  256,
   59,  256,   59,  123,   59,   43,  261,   45,  263,  264,
  265,  266,  269,  270,  269,  270,  271,   59,  275,  274,
  256,   59,   59,  271,  256,   11,   59,   59,   59,  261,
   59,   59,  264,  271,  266,  271,  272,  269,  270,  271,
  256,   44,  274,  256,  256,  261,   42,   43,  264,   45,
  266,   47,  272,  269,  270,  271,  269,  270,  274,  271,
  272,   59,  275,  256,   60,  271,   62,  256,  261,   42,
   43,  264,   45,  266,   47,  272,  269,  270,  271,   71,
   72,  274,  271,  272,  265,  256,   59,   60,   41,   62,
  261,   83,   84,  264,   80,  266,  256,  272,  269,  270,
  271,  261,  272,  274,  264,   41,  266,  272,   59,  269,
  270,  271,   44,  256,  274,  256,   41,  272,  261,  256,
   41,  264,   41,  266,  261,  256,   44,  264,  271,  266,
  271,  272,  269,  270,  271,   41,  256,  274,   43,  256,
   45,  261,    0,  262,  264,   59,  266,  262,   41,   42,
   43,  271,   45,  204,   47,   60,   60,   62,   40,  256,
   59,   60,   34,   -1,  256,   42,   43,   60,   45,   62,
   47,   73,   74,   -1,  271,  272,   -1,   -1,   -1,  271,
  272,  256,   59,   -1,   86,   87,   -1,  256,   -1,  256,
   -1,   -1,  256,   -1,  256,   -1,  271,  272,   -1,   -1,
  256,   -1,  271,  272,  271,  272,   -1,  271,  272,  271,
  272,   -1,   -1,   -1,   -1,  271,  272,  203,  204,  255,
   -1,   -1,  121,  259,  260,   -1,  125,   -1,  264,   -1,
  266,   -1,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,
   -1,   -1,  278,   -1,   -1,  281,   -1,  233,   -1,  235,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,  259,  260,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,   -1,   -1,
   -1,   -1,   -1,  192,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  228,
  229,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  247,   -1,
   -1,  256,  257,  258,  259,  260,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,
};
}
final static short YYFINAL=10;
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
"declaracion_procedimiento : proc_encabezado proc_parametros proc_ni proc_cuerpo",
"proc_encabezado : PROC ID",
"proc_encabezado : error ID",
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
"lista_parametros_formales : parametro_formal ',' parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal",
"lista_parametros_formales : parametro_formal parametro_formal ',' parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal parametro_formal",
"lista_parametros_formales : parametro_formal parametro_formal",
"lista_parametros_formales : parametro_formal ',' parametro_formal ',' parametro_formal ',' error",
"lista_parametros_formales : parametro_formal ',' error",
"parametro_formal : tipo ID",
"parametro_formal : VAR tipo ID",
"parametro_formal : error ID",
"parametro_formal : VAR error ID",
"sentencia_seleccion : IF condicion_if THEN bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else END_IF ';'",
"condicion_if : '(' condicion ')'",
"condicion_if : condicion ')'",
"condicion_if : '(' condicion",
"condicion_if : '(' ')'",
"condicion_if : condicion",
"condicion_if : '(' error ')'",
"bloque_then : bloque_sentencias",
"bloque_else : bloque_sentencias",
"sentencia_control : FOR '(' inicio_for ';' condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR inicio_for ';' condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' error ';' condicion_for incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for ';' condicion_for error CTE ')' bloque_for",
"sentencia_control : FOR '(' inicio_for ';' condicion_for incr_decr error ')' bloque_for",
"sentencia_control : FOR '(' inicio_for ';' condicion_for incr_decr CTE bloque_for",
"sentencia_control : FOR '(' inicio_for ';' condicion_for incr_decr CTE ')' sentencia_declarativa",
"sentencia_control : FOR '(' ';' condicion_for incr_decr CTE ')' bloque_for",
"inicio_for : ID '=' CTE",
"inicio_for : error '=' CTE",
"inicio_for : ID error CTE",
"inicio_for : ID '=' error",
"inicio_for : ID error",
"bloque_for : '{' bloque_ejecutable '}'",
"bloque_for : sentencia_ejecutable",
"condicion_for : condicion ';'",
"incr_decr : UP",
"incr_decr : DOWN",
"sentencia_salida : OUT '(' CADENA_MULT ')' ';'",
"sentencia_salida : OUT '(' ID ')' ';'",
"sentencia_salida : OUT '(' CTE ')' ';'",
"sentencia_salida : error '(' CADENA_MULT ')' ';'",
"sentencia_salida : '(' CADENA_MULT ')' ';'",
"sentencia_salida : OUT error CADENA_MULT ')' ';'",
"sentencia_salida : OUT '(' ')' ';'",
"sentencia_salida : OUT '(' error ')' ';'",
"sentencia_salida : OUT '(' CADENA_MULT error ';'",
"sentencia_salida : OUT '(' CADENA_MULT ')'",
"sentencia_asignacion : ID '=' expresion ';'",
"sentencia_asignacion : error '=' expresion ';'",
"sentencia_asignacion : ID error expresion ';'",
"sentencia_asignacion : ID '=' error ';'",
"sentencia_asignacion : ID '=' expresion",
"sentencia_invocacion : ID '(' lista_parametros ')' ';'",
"sentencia_invocacion : ID '(' ')' ';'",
"sentencia_invocacion : '(' lista_parametros ')' ';'",
"sentencia_invocacion : '(' ')' ';'",
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
"condicion : expresion error",
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
"termino : error '*' factor",
"termino : error '/' factor",
"factor : ID",
"factor : cte",
"cte : CTE",
"cte : '-' CTE",
};

//#line 436 "gramatica.y"

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
//#line 922 "Parser.java"
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
case 4:
//#line 38 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa", analizadorLexico.getNroLinea())); }
break;
case 5:
//#line 39 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa", analizadorLexico.getNroLinea())); }
break;
case 19:
//#line 65 "gramatica.y"
{ imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea()); }
break;
case 20:
//#line 66 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';'", nroUltimaLinea)); }
break;
case 21:
//#line 67 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables", analizadorLexico.getNroLinea())); }
break;
case 22:
//#line 70 "gramatica.y"
{ declaracionID(val_peek(2).sval, "Variable", ultimoTipo); }
break;
case 23:
//#line 71 "gramatica.y"
{ declaracionID(val_peek(0).sval, "Variable", ultimoTipo); }
break;
case 24:
//#line 72 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ','" + Main.ANSI_RESET, analizadorLexico.getNroLinea())); }
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
{   imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                                                                                        ambitos.eliminarAmbito(actualizarTablaSimbolos);
                                                                                    }
break;
case 28:
//#line 84 "gramatica.y"
{   ambitos.agregarAmbito(val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Procedimiento", null);
                            }
break;
case 29:
//#line 87 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
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
                                System.out.printf( Main.ANSI_RED + "[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                int cteInt = Integer.parseInt(cte);
                                TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                                if(cteInt < 1 || cteInt > 4)
                                    Errores.addError(String.format("[ASem] | Linea %d: NI no se encuentra en el intervalo [1,4] %n", analizadorLexico.getNroLinea()));
                        }
break;
case 39:
//#line 110 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 40:
//#line 111 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 41:
//#line 112 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 45:
//#line 121 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 46:
//#line 122 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 47:
//#line 123 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 48:
//#line 124 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 49:
//#line 125 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 50:
//#line 126 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 51:
//#line 127 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 52:
//#line 128 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 53:
//#line 131 "gramatica.y"
{ System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
break;
case 54:
//#line 136 "gramatica.y"
{ System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
break;
case 55:
//#line 141 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 56:
//#line 142 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 57:
//#line 145 "gramatica.y"
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
case 58:
//#line 155 "gramatica.y"
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
//#line 182 "gramatica.y"
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
case 60:
//#line 191 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '('", analizadorLexico.getNroLinea())); }
break;
case 61:
//#line 192 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')'", analizadorLexico.getNroLinea())); }
break;
case 62:
//#line 193 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condicion", analizadorLexico.getNroLinea())); }
break;
case 63:
//#line 194 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis", analizadorLexico.getNroLinea())); }
break;
case 64:
//#line 195 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la condicion", analizadorLexico.getNroLinea())); }
break;
case 65:
//#line 199 "gramatica.y"
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
case 67:
//#line 217 "gramatica.y"
{   SA3(val_peek(2).sval);
                                                                                                SA4(val_peek(6).sval , val_peek(4).sval);
                                                                                                SA5(val_peek(6).sval, val_peek(2).sval, val_peek(3).sval); /* id cte incr_decr*/
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
case 68:
//#line 233 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '('", analizadorLexico.getNroLinea())); }
break;
case 69:
//#line 234 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control", analizadorLexico.getNroLinea())); }
break;
case 70:
//#line 235 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control", analizadorLexico.getNroLinea())); }
break;
case 71:
//#line 236 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea())); }
break;
case 72:
//#line 237 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea())); }
break;
case 73:
//#line 238 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')'", analizadorLexico.getNroLinea())); }
break;
case 74:
//#line 239 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea())); }
break;
case 75:
//#line 240 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control", analizadorLexico.getNroLinea())); }
break;
case 76:
//#line 243 "gramatica.y"
{   SA3(val_peek(0).sval);
                                SA1(val_peek(2).sval);
                                SA1(val_peek(0).sval);
                                SA2(val_peek(1).sval);
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
case 77:
//#line 256 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control", analizadorLexico.getNroLinea())); }
break;
case 78:
//#line 257 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea())); }
break;
case 79:
//#line 258 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion", analizadorLexico.getNroLinea())); }
break;
case 80:
//#line 259 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control", analizadorLexico.getNroLinea())); }
break;
case 83:
//#line 267 "gramatica.y"
{   if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                                        polaca.pushPos(true);
                                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                    else {
                                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF),false);
                                    }
                                }
break;
case 84:
//#line 278 "gramatica.y"
{ yyval = new ParserVal("+"); }
break;
case 85:
//#line 279 "gramatica.y"
{ yyval = new ParserVal("-"); }
break;
case 86:
//#line 282 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        SA1(val_peek(2).sval);
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
break;
case 87:
//#line 289 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        /* TODO: ver como hacer SA1 cuando el id no está declarado o cuando está renombrado con el ámbito*/
                                                        SA1(val_peek(2).sval);
                                                        invocacionID(val_peek(2).sval, "Variable");
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
break;
case 88:
//#line 298 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                                                        SA1(val_peek(2).sval);
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
break;
case 89:
//#line 305 "gramatica.y"
{ Errores.addError(String.format("Falta palabra reservada OUT en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 90:
//#line 306 "gramatica.y"
{ Errores.addError(String.format("Falta palabra reservada OUT en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 91:
//#line 307 "gramatica.y"
{ Errores.addError(String.format("Falta literal '(' en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 92:
//#line 308 "gramatica.y"
{ Errores.addError(String.format("Falta cadena multilínea a imprimir en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 93:
//#line 309 "gramatica.y"
{ Errores.addError(String.format("Error en la cadena multilínea a imprimir en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 94:
//#line 310 "gramatica.y"
{ Errores.addError(String.format("Falta literal ')' en sentencia de salida %n", analizadorLexico.getNroLinea())); }
break;
case 95:
//#line 311 "gramatica.y"
{ Errores.addError(String.format("Falta literal ';' en sentencia de salida %n", nroUltimaLinea)); }
break;
case 96:
//#line 314 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                                                    String id = val_peek(3).sval;
                                                    String cte = val_peek(1).sval;
                                                    Token token = TablaSimbolos.getToken(id);
                                                    if(token != null) {
                                                        /* TODO: hay que agregar el valor en la TS?*/
                                                        token.addAtributo("VALOR", cte);
                                                        SA1(id);
                                                        SA2(val_peek(2).sval);
                                                    }
                                                    invocacionID(id, "Variable");
                                                }
break;
case 97:
//#line 326 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 98:
//#line 327 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 99:
//#line 328 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 100:
//#line 329 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 101:
//#line 332 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación con lista de parámetros %n", analizadorLexico.getNroLinea());
                                                                invocacionID(val_peek(4).sval, "Procedimiento");
                                                            }
break;
case 102:
//#line 335 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                                                invocacionID(val_peek(3).sval, "Procedimiento");
                                            }
break;
case 103:
//#line 338 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n", analizadorLexico.getNroLinea())); }
break;
case 104:
//#line 339 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n", analizadorLexico.getNroLinea())); }
break;
case 105:
//#line 340 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
break;
case 106:
//#line 341 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
break;
case 107:
//#line 342 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: | Parámetros inválidos %n", analizadorLexico.getNroLinea())); }
break;
case 108:
//#line 343 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
break;
case 109:
//#line 344 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: | Falta literal ')' en sentencia de invocación %n", analizadorLexico.getNroLinea())); }
break;
case 110:
//#line 345 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n", nroUltimaLinea)); }
break;
case 111:
//#line 346 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n", nroUltimaLinea)); }
break;
case 112:
//#line 349 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                                            invocacionID(val_peek(4).sval, "Parametro");
                                            invocacionID(val_peek(2).sval, "Parametro");
                                            invocacionID(val_peek(0).sval, "Parametro");
                                        }
break;
case 113:
//#line 354 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                                    invocacionID(val_peek(2).sval, "Parametro");
                                    invocacionID(val_peek(0).sval, "Parametro");
                                }
break;
case 114:
//#line 358 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                                invocacionID(val_peek(0).sval, "Parametro");
                            }
break;
case 115:
//#line 361 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea())); }
break;
case 116:
//#line 362 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea())); }
break;
case 117:
//#line 363 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
break;
case 118:
//#line 364 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
break;
case 119:
//#line 365 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
break;
case 120:
//#line 366 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea())); }
break;
case 121:
//#line 370 "gramatica.y"
{   imprimirReglaReconocida("Comparación", analizadorLexico.getNroLinea());
                                                    SA2(val_peek(1).sval);
                                                }
break;
case 122:
//#line 373 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta comparador %n", analizadorLexico.getNroLinea())); }
break;
case 123:
//#line 374 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando de la condición %n", analizadorLexico.getNroLinea())); }
break;
case 124:
//#line 375 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando de la condición %n", analizadorLexico.getNroLinea())); }
break;
case 131:
//#line 386 "gramatica.y"
{   imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                                        SA2(val_peek(1).sval);
                                    }
break;
case 132:
//#line 389 "gramatica.y"
{   imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                                        SA2(val_peek(1).sval);
                                    }
break;
case 134:
//#line 393 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n", analizadorLexico.getNroLinea())); }
break;
case 135:
//#line 394 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n", analizadorLexico.getNroLinea())); }
break;
case 136:
//#line 395 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n", analizadorLexico.getNroLinea())); }
break;
case 137:
//#line 396 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la resta %n", analizadorLexico.getNroLinea())); }
break;
case 138:
//#line 399 "gramatica.y"
{   imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                                    SA2(val_peek(1).sval);
                                }
break;
case 139:
//#line 402 "gramatica.y"
{   imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
                                    SA2(val_peek(1).sval);
                                }
break;
case 141:
//#line 406 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n", analizadorLexico.getNroLinea())); }
break;
case 142:
//#line 407 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la división %n", analizadorLexico.getNroLinea())); }
break;
case 143:
//#line 408 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n", analizadorLexico.getNroLinea())); }
break;
case 144:
//#line 409 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: | Falta el primer operando en la división %n", analizadorLexico.getNroLinea())); }
break;
case 145:
//#line 412 "gramatica.y"
{   SA1(val_peek(0).sval);
                    invocacionID(val_peek(0).sval, "Variable");
                }
break;
case 146:
//#line 415 "gramatica.y"
{ SA1(val_peek(0).sval); }
break;
case 147:
//#line 418 "gramatica.y"
{   String cte = val_peek(0).sval;
	            String nuevo = checkPositivo(cte);
	            if (nuevo != null)
	   	            yyval = new ParserVal(nuevo);
	            else
	   	            yyval = new ParserVal(cte);
            }
break;
case 148:
//#line 426 "gramatica.y"
{   String cte = val_peek(0).sval;
      		        String nuevo = checkRango(cte);
      		        if (nuevo != null) {
      		  	        yyval = new ParserVal(nuevo);
                        imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
      		        }
     	 	    }
break;
//#line 1705 "Parser.java"
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
