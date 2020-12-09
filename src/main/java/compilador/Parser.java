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
//#line 25 "Parser.java"




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
    7,   15,   15,   15,   14,   14,    8,    8,    8,   16,
   16,   18,   18,   18,   18,   18,   19,   19,   19,   19,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   20,
   20,   20,   20,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
   21,   21,   21,   21,   21,   21,   22,   23,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   25,   25,   25,
   25,   25,   25,   25,   28,   28,   28,   28,   26,   27,
   27,   11,   11,   11,   11,   11,   11,   11,   11,   12,
   12,   12,   12,   12,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   32,   32,   32,   32,   32,   32,   32,
   32,   32,   24,   24,   24,   24,   24,   24,   31,   31,
   31,   31,   31,   31,   33,   33,   33,   33,   33,   33,
   33,   30,   30,   29,   29,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    2,    3,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    2,    3,    1,    2,    1,    1,    4,    3,    4,    2,
    2,    3,    2,    3,    3,    1,    3,    2,    3,    3,
    7,    5,    3,    2,    6,    6,    4,    9,    5,    2,
    3,    2,    3,    6,    8,    5,    7,    7,    5,    7,
    6,    8,    5,    5,    7,    8,    7,    7,    5,    7,
    3,    2,    2,    2,    1,    3,    1,    1,    8,    7,
    8,    9,    8,    8,    7,    8,    8,    4,    4,    4,
    2,    3,    3,    3,    3,    1,    3,    3,    2,    1,
    1,    5,    5,    4,    5,    5,    4,    5,    4,    4,
    4,    4,    4,    3,    5,    4,    4,    3,    5,    4,
    3,    4,    3,    5,    3,    1,    7,    3,    3,    4,
    4,    2,    3,    3,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   25,   26,    0,    0,    0,    0,
   10,   11,    9,   12,   13,   14,   15,   16,   17,   18,
    0,    0,    0,    0,    0,  152,  154,    0,    0,    0,
    0,  153,  147,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   31,   30,
    8,   21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,    7,    0,    0,   74,    0,  155,    0,
    0,   72,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  121,    0,    0,  118,    0,    0,   24,
   20,   19,   36,    0,    0,    0,    0,    0,   44,    0,
    0,    0,    0,    0,   28,  111,    0,    0,  150,  151,
    0,    0,    0,   76,   71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  148,  145,  149,  146,    0,   93,    0,   94,    0,
   92,    0,    0,    0,    0,   99,  100,  101,    0,    0,
    0,    0,    0,  104,    0,  112,  129,    0,  128,    0,
  113,  110,    0,  116,  120,    0,  117,   22,    0,    0,
   33,   29,   52,    0,    0,   50,    0,   43,    0,    0,
    0,   38,   27,    6,   78,    0,   69,    0,    0,    0,
   59,    0,    0,    0,    0,    0,   56,   89,   90,   88,
    0,    0,    0,    0,    0,    0,  103,  105,  106,  102,
  108,  131,  130,    0,  119,  115,   34,   35,   32,   53,
   51,    0,    0,    0,   47,   39,    0,   37,    0,    0,
   61,    0,    0,    0,    0,   54,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   49,    0,   42,    0,
    0,   70,    0,   60,    0,   65,   67,    0,   58,   57,
    0,    0,    0,    0,    0,    0,    0,    0,   96,   85,
   80,    0,    0,   46,   45,   62,   66,   55,   81,   87,
    0,   83,   84,    0,    3,    4,    0,   86,   79,  127,
    0,   41,   82,   97,    2,    5,    0,   95,    0,   48,
};
final static short yydgoto[] = {                          9,
  123,  304,   11,   12,  205,   64,   14,   15,   16,   17,
   18,   19,   20,   21,   54,   22,   57,  116,  125,  121,
   30,   65,  206,   91,   40,   92,  169,  290,   32,   33,
   34,   48,   35,
};
final static short yysindex[] = {                       767,
  -18,  -32,  -24,  -36,    0,    0,  -29, -133,    0,  767,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -98,  -35,  157,  474,  633,    0,    0,  154, -246,  574,
    9,    0,    0,  546,  246,   13,  -46, -176,  436,  157,
 -163,  147,  157,  -25,  277,   -3,   73,   94,    0,    0,
    0,    0,  -16,  -53,   44,  -19,  445,  155,  290,   42,
   42,  767,    0,    0,  146,  405,    0,  111,    0,  -87,
  152,    0,  157,  157,  157,  157,  157,  157,  295,  343,
  361,  371,  -83,    0,  145,  427,  149,  151,  157,  379,
  166, -118,  175,  207,  -39,  190,  228,  164,    3,  -70,
  138,  426,  268,    0,  259,   36,    0,  297,  -66,    0,
    0,    0,    0,  -37,   64,  644,   89,  174,    0,  137,
  -41,  778,  667,  644,    0,    0,  357,  246,    0,    0,
  683,  633,  362,    0,    0,   58,  633,  380,  557,  633,
  392,  483,  483,  483,  483,  483,  483,  357,  246,  357,
  246,    0,    0,    0,    0,  394,    0,  395,    0,  402,
    0, -118, -118,  468, -187,    0,    0,    0,  198,  407,
  414,  417,  454,    0,  459,    0,    0,  248,    0,    5,
    0,    0,  465,    0,    0,  466,    0,    0,  258, -233,
    0,    0,    0,  261,  264,    0, -114,    0,  -14,  -26,
  694,    0,    0,    0,    0,  276,    0,  633,  497,  305,
    0,  -18,  590,  516,  311,  315,    0,    0,    0,    0,
  309,  314, -118,  316, -183,  549,    0,    0,    0,    0,
    0,    0,    0,  321,    0,    0,    0,    0,    0,    0,
    0,  -27,  -34,  221,    0,    0,  -18,    0,  534,  329,
    0,  537,  -13,  538, -159,    0,  539,  542,  561,  568,
  335,  571,  572,  339,  391,  579,    0,  221,    0,  587,
  589,    0,  575,    0,  578,    0,    0,  595,    0,    0,
  391,  391,  607,  391,  391,  504,  767,  656,    0,    0,
    0,  400,   26,    0,    0,    0,    0,    0,    0,    0,
  391,    0,    0,  714,    0,    0,  725,    0,    0,    0,
  620,    0,    0,    0,    0,    0,  -18,    0,  632,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  676,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  601,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,   63,    0,    0,    0,    0,    0,    0,
    0,    0,  -50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  617,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   84,    0,
    0,  120,    0,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   21,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,  397,  408,  434,  464,  481,   41,   68,   88,
  108,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  194,    0,  218,    0,    0,    0,    0,  104,
    0,    0,    0,    0,    0,  238,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  273,    0,  289,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  308,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  116,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  355,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  375,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   17,  398,  728, -229,   15,  665,    0,    0,    0,    0,
    0,    0,    0,  566,  -33,    0,  370,  629,   38, -103,
    0,   -5, -106,   27,  649,  495, -111, -167,  603,   11,
  928,  645,   57,
};
final static int YYTABLESIZE=1052;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        198,
  141,  173,  197,   42,   56,  112,  269,   28,   23,  268,
   46,   47,   29,  267,   86,   39,   10,  199,  100,  110,
  144,  119,  238,  190,   71,   69,  245,  109,   31,  244,
  210,   45,  215,  216,   23,   62,   38,  105,  239,   63,
  142,  141,   23,  141,   63,  141,  178,   23,  234,   72,
  221,  222,   97,  225,   68,  104,  306,  306,  308,  141,
  141,  144,  141,  144,  139,  144,  312,  139,  224,  311,
  129,  130,  263,   83,  316,  188,  186,  316,  131,  144,
  144,  142,  144,  142,   63,  142,   29,  143,  264,  167,
  168,  153,  155,  243,  185,   87,  277,  291,  246,  142,
  142,  250,  142,  126,  115,  278,  255,  140,  139,   93,
  139,  261,  139,  299,  300,  128,  302,  303,   23,  114,
  309,  126,   49,  141,  132,  141,  139,  139,  143,  139,
  143,  107,  143,  313,  108,  149,  151,   50,  201,  270,
  271,  242,  132,  144,  125,  144,  143,  143,  140,  143,
  140,  135,  140,  192,    5,    6,  124,   52,  167,  168,
  118,  203,  125,  142,  293,  142,  140,  140,  136,  140,
  123,  137,   53,    2,  124,  137,    3,  138,    4,   60,
   59,    5,    6,    7,   61,  179,    8,   96,  156,  137,
  139,   29,  139,  107,   67,   29,  181,   79,   29,   80,
  180,   29,  111,  157,   53,   23,   79,  161,   80,   84,
  143,   83,  143,  126,  117,  170,  172,  109,  189,   41,
   55,  117,  176,   24,  166,   85,   43,    5,    6,   25,
  140,   36,  140,  118,    5,    6,  117,  122,   26,   27,
  118,   44,  114,  193,  114,   99,   37,  171,  174,    5,
    6,  275,  103,  137,   53,  118,  141,  141,  141,  141,
  141,  141,  141,  141,  141,  141,  141,   44,  175,  141,
  141,  141,   64,  177,  141,  233,  144,  144,  144,  144,
  144,  144,  144,  144,  144,  144,  144,   81,   63,  144,
  144,  144,   82,  123,  144,  123,  142,  142,  142,  142,
  142,  142,  142,  142,  142,  142,  142,   40,  183,  142,
  142,  142,   26,   27,  142,  113,  107,  184,  107,  114,
  208,   29,  209,  139,  139,  139,  139,  139,  139,  139,
  139,  139,  139,  139,   29,  191,  139,  139,  139,   29,
  109,  139,  109,  143,  143,  143,  143,  143,  143,  143,
  143,  143,  143,  143,   68,  187,  143,  143,  143,  193,
  122,  143,  122,  140,  140,  140,  140,  140,  140,  140,
  140,  140,  140,  140,   98,  114,  140,  140,  140,  288,
  114,  140,  114,  114,  114,  114,  137,   29,  114,  114,
  114,  137,  137,  114,  137,   64,  137,   64,   60,  137,
  137,  137,   94,   61,  137,   29,   24,  196,  132,   66,
  133,   63,   24,   63,  140,   29,  141,   26,   27,   95,
  207,   26,   27,   29,   26,   27,  123,   26,   27,  194,
   40,  123,   40,  123,  123,  123,  123,  133,  211,  123,
  123,  123,    5,    6,  123,  134,   60,   59,  134,  107,
  217,   61,  218,  219,  107,  133,  107,  107,  107,  107,
  220,  287,  107,  107,  107,  227,  134,  107,   79,  226,
   80,   29,  228,  109,  138,  229,  117,   68,  109,   68,
  109,  109,  109,  109,  182,  159,  109,  109,  109,    5,
    6,  109,  138,  122,   89,  118,   38,   98,  122,   98,
  122,  122,  122,  122,  135,  115,  122,  122,  122,   60,
   59,  122,  230,  287,   61,   60,   59,  231,  232,  133,
   61,  136,  135,  235,  236,   79,  223,   80,   64,  237,
  134,  240,  101,   64,  241,   64,   64,   64,   64,  136,
  249,   64,   64,   64,   63,  127,   64,   26,   27,   63,
  148,   63,   63,   63,   63,  251,  138,   63,   63,   63,
   26,   27,   63,   40,   23,   26,   27,  122,   40,  252,
   40,   40,   40,   40,  256,  257,   40,   40,   40,  258,
  259,   40,  162,  163,  165,  260,  135,  262,   79,  265,
   80,  266,  272,  273,  286,  274,  276,  279,  150,    2,
  280,  281,    3,  136,    4,   78,  283,   77,  282,    7,
   68,  284,  285,   26,   27,   68,  152,   68,   68,   68,
   68,  120,  292,   68,   68,   68,  154,  294,   68,  295,
   98,   26,   27,  296,  164,   98,  297,   98,   98,   98,
   98,   26,   27,   98,   98,   98,  286,  301,   98,   26,
   27,    2,  133,  298,    3,  310,    4,  133,  133,   56,
  133,    7,  133,  134,   13,  133,  133,  133,  134,  134,
  133,  134,  320,  134,   51,    1,  134,  134,  134,   62,
  319,  134,  158,  195,  307,  124,  120,   90,  160,  138,
  106,   88,    0,    0,  138,  138,   62,  138,   27,  138,
    1,    0,  138,  138,  138,    2,   37,  138,    3,    0,
    4,    0,   62,    5,    6,    7,  113,    0,    8,  135,
  114,   13,    0,   75,  135,  135,   13,  135,    0,  135,
    0,    0,  135,  135,  135,    0,  136,  135,    0,   73,
    0,  136,  136,    0,  136,    0,  136,    0,    0,  136,
  136,  136,    0,    0,  136,   62,    0,    0,    0,    1,
    0,    0,  120,    0,    2,    0,  122,    3,    0,    4,
    0,    0,    5,    6,    7,    0,    0,    8,  287,    0,
   13,    0,    0,    0,    0,    0,   13,   51,   13,    0,
    0,  202,    0,    0,    0,   51,    0,    0,    0,    0,
    0,    0,   73,   74,   75,   76,    0,  204,  120,  120,
    0,    0,  212,    0,    0,    0,    0,    2,  248,  213,
    3,  214,    4,    0,    0,    5,    6,    7,    0,    1,
    8,    0,    0,  120,    2,   70,    0,    3,  314,    4,
    0,    0,    5,    6,    7,  253,    0,    8,    0,  318,
    2,    0,    0,    3,  254,    4,   75,    0,    5,    6,
    7,   75,   75,    8,   75,   51,   75,    0,    0,   75,
   75,   75,   73,    0,   75,    0,    0,   73,   73,    0,
   73,    0,   73,    0,    0,   73,   73,   73,    1,    0,
   73,    0,    0,    2,    0,    0,    3,    0,    4,    1,
    0,    5,    6,    7,    2,    0,    8,    3,    0,    4,
    0,  286,    5,    6,    7,    0,    2,    8,    0,    3,
    0,    4,    1,    0,    5,    6,    7,    2,    0,    8,
    3,    0,    4,    0,    0,    5,    6,    7,    1,    0,
    8,    0,    0,    2,    0,    0,    3,    0,    4,  247,
   58,    5,    6,    7,    2,    0,    8,    3,    0,    4,
    0,    0,    5,    6,    7,    0,    0,    8,    0,    1,
   98,    0,  102,    0,    2,    0,    0,    3,    0,    4,
  317,    0,    5,    6,    7,    2,    0,    8,    3,    0,
    4,  289,  289,    5,    6,    7,    0,    0,    8,    0,
  142,  143,  144,  145,  146,  147,    0,    0,  289,  289,
    0,  289,  289,  305,  305,  289,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    2,  289,    0,
    3,  315,    4,  200,  315,    5,    6,    7,    2,    0,
    8,    3,    0,    4,    0,    0,    5,    6,    7,    0,
    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   44,   40,   40,   59,   41,   40,   59,   44,
   40,   41,   45,   41,   61,   40,    0,  121,   44,   53,
    0,   41,  256,   61,   30,  272,   41,   44,    2,   44,
  137,   61,  139,  140,   61,  123,   61,   41,  272,   25,
    0,   41,   61,   43,   30,   45,   44,   61,   44,   41,
  162,  163,   42,  165,   28,   59,  286,  287,  288,   59,
   60,   41,   62,   43,   70,   45,   41,    0,  256,   44,
   60,   61,  256,   61,  304,  109,   41,  307,   62,   59,
   60,   41,   62,   43,   70,   45,   45,    0,  272,  277,
  278,   81,   82,  197,   59,  272,  256,  265,  125,   59,
   60,  208,   62,   41,   61,  265,  213,    0,   41,  273,
   43,  223,   45,  281,  282,   59,  284,  285,   61,    0,
  288,   59,  256,  123,   41,  125,   59,   60,   41,   62,
   43,   59,   45,  301,   41,   79,   80,  271,  122,  243,
  244,  256,   59,  123,   41,  125,   59,   60,   41,   62,
   43,   41,   45,  116,  269,  270,   41,  256,  277,  278,
  275,  124,   59,  123,  268,  125,   59,   60,  256,   62,
    0,   41,  271,  261,   59,  263,  264,  265,  266,   42,
   43,  269,  270,  271,   47,  256,  274,   41,  272,   59,
  123,   45,  125,    0,   41,   45,   59,   43,   45,   45,
  271,   45,  256,   59,  271,  256,   43,   59,   45,  256,
  123,   61,  125,   59,  256,   41,  256,    0,  256,  256,
  256,  256,   59,  256,   59,  272,  256,  269,  270,  262,
  123,  256,  125,  275,  269,  270,  256,    0,  271,  272,
  275,  271,  123,  271,  125,  271,  271,   41,   59,  269,
  270,  265,  256,  123,  271,  275,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  271,   41,  269,
  270,  271,    0,  271,  274,  271,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,   42,    0,  269,
  270,  271,   47,  123,  274,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,    0,   41,  269,
  270,  271,  271,  272,  274,  272,  123,   59,  125,  276,
  263,   45,  265,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,   45,  272,  269,  270,  271,   45,
  123,  274,  125,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,    0,   59,  269,  270,  271,  271,
  123,  274,  125,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,    0,  256,  269,  270,  271,   41,
  261,  274,  263,  264,  265,  266,  256,   45,  269,  270,
  271,  261,  262,  274,  264,  123,  266,  125,   42,  269,
  270,  271,  256,   47,  274,   45,  256,  271,  263,  256,
  265,  123,  256,  125,  263,   45,  265,  271,  272,  273,
   59,  271,  272,   45,  271,  272,  256,  271,  272,  256,
  123,  261,  125,  263,  264,  265,  266,   41,   59,  269,
  270,  271,  269,  270,  274,   41,   42,   43,   41,  256,
   59,   47,   59,   59,  261,   59,  263,  264,  265,  266,
   59,  123,  269,  270,  271,   59,   59,  274,   43,  272,
   45,   45,   59,  256,   41,   59,  256,  123,  261,  125,
  263,  264,  265,  266,   59,   59,  269,  270,  271,  269,
  270,  274,   59,  256,   59,  275,   61,  123,  261,  125,
  263,  264,  265,  266,   41,   61,  269,  270,  271,   42,
   43,  274,   59,  123,   47,   42,   43,   59,  271,  123,
   47,   41,   59,   59,   59,   43,   59,   45,  256,  272,
  123,  271,  256,  261,  271,  263,  264,  265,  266,   59,
  265,  269,  270,  271,  256,  256,  274,  271,  272,  261,
  256,  263,  264,  265,  266,   59,  123,  269,  270,  271,
  271,  272,  274,  256,   61,  271,  272,  123,  261,  265,
  263,  264,  265,  266,   59,  265,  269,  270,  271,  265,
  272,  274,   88,   89,   90,  272,  123,  272,   43,   41,
   45,  271,   59,  265,  256,   59,   59,   59,  256,  261,
   59,   41,  264,  123,  266,   60,  272,   62,   41,  271,
  256,   41,   41,  271,  272,  261,  256,  263,  264,  265,
  266,   56,   44,  269,  270,  271,  256,   41,  274,   41,
  256,  271,  272,   59,  256,  261,   59,  263,  264,  265,
  266,  271,  272,  269,  270,  271,  256,   41,  274,  271,
  272,  261,  256,   59,  264,  256,  266,  261,  262,   40,
  264,  271,  266,  256,    0,  269,  270,  271,  261,  262,
  274,  264,   41,  266,   10,    0,  269,  270,  271,  123,
  311,  274,  256,  118,  287,   57,  121,   39,   86,  256,
   46,  256,   -1,   -1,  261,  262,  123,  264,  272,  266,
  256,   -1,  269,  270,  271,  261,  271,  274,  264,   -1,
  266,   -1,  123,  269,  270,  271,  272,   -1,  274,  256,
  276,   57,   -1,  123,  261,  262,   62,  264,   -1,  266,
   -1,   -1,  269,  270,  271,   -1,  256,  274,   -1,  123,
   -1,  261,  262,   -1,  264,   -1,  266,   -1,   -1,  269,
  270,  271,   -1,   -1,  274,  123,   -1,   -1,   -1,  256,
   -1,   -1,  197,   -1,  261,   -1,  123,  264,   -1,  266,
   -1,   -1,  269,  270,  271,   -1,   -1,  274,  123,   -1,
  116,   -1,   -1,   -1,   -1,   -1,  122,  123,  124,   -1,
   -1,  125,   -1,   -1,   -1,  131,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  259,  260,   -1,  125,  243,  244,
   -1,   -1,  256,   -1,   -1,   -1,   -1,  261,  125,  263,
  264,  265,  266,   -1,   -1,  269,  270,  271,   -1,  256,
  274,   -1,   -1,  268,  261,  262,   -1,  264,  125,  266,
   -1,   -1,  269,  270,  271,  256,   -1,  274,   -1,  125,
  261,   -1,   -1,  264,  265,  266,  256,   -1,  269,  270,
  271,  261,  262,  274,  264,  201,  266,   -1,   -1,  269,
  270,  271,  256,   -1,  274,   -1,   -1,  261,  262,   -1,
  264,   -1,  266,   -1,   -1,  269,  270,  271,  256,   -1,
  274,   -1,   -1,  261,   -1,   -1,  264,   -1,  266,  256,
   -1,  269,  270,  271,  261,   -1,  274,  264,   -1,  266,
   -1,  256,  269,  270,  271,   -1,  261,  274,   -1,  264,
   -1,  266,  256,   -1,  269,  270,  271,  261,   -1,  274,
  264,   -1,  266,   -1,   -1,  269,  270,  271,  256,   -1,
  274,   -1,   -1,  261,   -1,   -1,  264,   -1,  266,  256,
   23,  269,  270,  271,  261,   -1,  274,  264,   -1,  266,
   -1,   -1,  269,  270,  271,   -1,   -1,  274,   -1,  256,
   43,   -1,   45,   -1,  261,   -1,   -1,  264,   -1,  266,
  256,   -1,  269,  270,  271,  261,   -1,  274,  264,   -1,
  266,  264,  265,  269,  270,  271,   -1,   -1,  274,   -1,
   73,   74,   75,   76,   77,   78,   -1,   -1,  281,  282,
   -1,  284,  285,  286,  287,  288,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,  261,  301,   -1,
  264,  304,  266,  256,  307,  269,  270,  271,  261,   -1,
  274,  264,   -1,  266,   -1,   -1,  269,  270,  271,   -1,
   -1,  274,
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
"declaracion_variables : tipo error",
"lista_variables : ID ',' lista_variables",
"lista_variables : ID",
"lista_variables : ID lista_variables",
"tipo : LONGINT",
"tipo : FLOAT",
"declaracion_procedimiento : proc_encabezado lista_parametros_formales proc_ni proc_cuerpo",
"declaracion_procedimiento : proc_encabezado lista_parametros_formales proc_cuerpo",
"declaracion_procedimiento : proc_encabezado error proc_ni proc_cuerpo",
"proc_encabezado : PROC ID",
"proc_encabezado : PROC error",
"proc_ni : NI '=' CTE",
"proc_ni : '=' CTE",
"proc_ni : NI error CTE",
"proc_ni : NI '=' error",
"proc_ni : CTE",
"proc_cuerpo : '{' lista_sentencias '}'",
"proc_cuerpo : lista_sentencias '}'",
"proc_cuerpo : '{' error '}'",
"proc_cuerpo : '{' lista_sentencias error",
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
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else END_IF",
"sentencia_seleccion : IF THEN bloque_then END_IF ';'",
"sentencia_seleccion : IF THEN bloque_then ELSE bloque_else END_IF ';'",
"condicion_if : '(' condicion ')'",
"condicion_if : condicion ')'",
"condicion_if : '(' condicion",
"condicion_if : '(' ')'",
"condicion_if : condicion",
"condicion_if : '(' error ')'",
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
"inicio_for : ID '=' error ';'",
"inicio_for : ID error",
"inicio_for : '=' CTE ';'",
"inicio_for : ID CTE ';'",
"inicio_for : ID '=' ';'",
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
"condicion : expresion MAYOR_IGUAL expresion",
"condicion : expresion MENOR_IGUAL expresion",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"condicion : expresion IGUAL expresion",
"condicion : expresion DISTINTO expresion",
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

//#line 423 "gramatica.y"

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
	    else if (uso.equals("Procedimiento"))
		Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
	    else if (uso.equals("Parametro"))
		Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
	}

    if(uso.equals("Parametro") && declarado)
        parametrosReales.add(lexema + "@" + ambitosString);

    if(uso.equals("Procedimiento") && declarado) {
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
//#line 1067 "Parser.java"
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
//#line 40 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }
break;
case 5:
//#line 41 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea())); }
break;
case 19:
//#line 68 "gramatica.y"
{ imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea()); }
break;
case 20:
//#line 69 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 21:
//#line 70 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables %n",analizadorLexico.getNroLinea()));}
break;
case 22:
//#line 73 "gramatica.y"
{ declaracionID(val_peek(2).sval, Main.VARIABLE, ultimoTipo); }
break;
case 23:
//#line 74 "gramatica.y"
{ declaracionID(val_peek(0).sval, Main.VARIABLE, ultimoTipo); }
break;
case 24:
//#line 75 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n",analizadorLexico.getNroLinea()));}
break;
case 25:
//#line 78 "gramatica.y"
{ ultimoTipo = "LONGINT"; }
break;
case 26:
//#line 79 "gramatica.y"
{ ultimoTipo = "FLOAT"; }
break;
case 27:
//#line 82 "gramatica.y"
{ imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                                                                                                ambitos.eliminarAmbito(actualizarTablaSimbolos);
                                                                                    }
break;
case 28:
//#line 85 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta declarar el número de invocaciones permitido en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 29:
//#line 86 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan declarar los parametros en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 30:
//#line 89 "gramatica.y"
{   ambitos.agregarAmbito(val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Procedimiento", null);
                            }
break;
case 31:
//#line 92 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 32:
//#line 96 "gramatica.y"
{   String cte = val_peek(0).sval;
                            if(!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                                Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                            else
                                TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        }
break;
case 33:
//#line 102 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 34:
//#line 103 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 35:
//#line 104 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 36:
//#line 105 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI y literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 38:
//#line 109 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 39:
//#line 110 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en las sentencias del cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 40:
//#line 111 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea())); }
break;
case 41:
//#line 114 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                                                                                                        TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                                        parametrosFormales.clear();
                                                                                                    }
break;
case 42:
//#line 118 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                                                                                TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                                                parametrosFormales.clear();
                                                                            }
break;
case 43:
//#line 122 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                                                            TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                                                            parametrosFormales.clear();
                                                        }
break;
case 44:
//#line 126 "gramatica.y"
{ TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales)); }
break;
case 45:
//#line 127 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 46:
//#line 128 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 47:
//#line 129 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea())); }
break;
case 48:
//#line 130 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea())); }
break;
case 49:
//#line 131 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea())); }
break;
case 50:
//#line 134 "gramatica.y"
{ imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
break;
case 51:
//#line 139 "gramatica.y"
{ imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
break;
case 52:
//#line 144 "gramatica.y"
{  Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n",analizadorLexico.getNroLinea())); }
break;
case 53:
//#line 145 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n",analizadorLexico.getNroLinea())); }
break;
case 54:
//#line 148 "gramatica.y"
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
case 55:
//#line 158 "gramatica.y"
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
case 56:
//#line 168 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
break;
case 57:
//#line 169 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n",analizadorLexico.getNroLinea()));}
break;
case 58:
//#line 170 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n",analizadorLexico.getNroLinea()));}
break;
case 59:
//#line 171 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 60:
//#line 172 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 61:
//#line 173 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 62:
//#line 174 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n",analizadorLexico.getNroLinea()));}
break;
case 63:
//#line 175 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 64:
//#line 176 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n",nroUltimaLinea));}
break;
case 65:
//#line 177 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
break;
case 66:
//#line 178 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n",analizadorLexico.getNroLinea()));}
break;
case 67:
//#line 179 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n",analizadorLexico.getNroLinea()));}
break;
case 68:
//#line 180 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 69:
//#line 181 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
break;
case 70:
//#line 182 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n",nroUltimaLinea));}
break;
case 71:
//#line 185 "gramatica.y"
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
case 72:
//#line 194 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 73:
//#line 195 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 74:
//#line 196 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n",analizadorLexico.getNroLinea()));}
break;
case 75:
//#line 197 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n",analizadorLexico.getNroLinea()));}
break;
case 76:
//#line 198 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n",analizadorLexico.getNroLinea()));}
break;
case 77:
//#line 202 "gramatica.y"
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
case 79:
//#line 220 "gramatica.y"
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
case 80:
//#line 236 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 81:
//#line 237 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n",analizadorLexico.getNroLinea()));}
break;
case 82:
//#line 238 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n",analizadorLexico.getNroLinea()));  }
break;
case 83:
//#line 239 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n",analizadorLexico.getNroLinea())); }
break;
case 84:
//#line 240 "gramatica.y"
{  Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n",analizadorLexico.getNroLinea()));  }
break;
case 85:
//#line 241 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 86:
//#line 242 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n",analizadorLexico.getNroLinea()));}
break;
case 87:
//#line 243 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));}
break;
case 88:
//#line 246 "gramatica.y"
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
case 89:
//#line 262 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n",analizadorLexico.getNroLinea()));}
break;
case 90:
//#line 263 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n",analizadorLexico.getNroLinea()));}
break;
case 91:
//#line 264 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n",analizadorLexico.getNroLinea()));}
break;
case 92:
//#line 265 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el identificador de control %n",analizadorLexico.getNroLinea()));}
break;
case 93:
//#line 266 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n",analizadorLexico.getNroLinea()));}
break;
case 94:
//#line 267 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta la constante de la asignacion %n",analizadorLexico.getNroLinea()));}
break;
case 97:
//#line 272 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n",analizadorLexico.getNroLinea()));}
break;
case 98:
//#line 273 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n",nroUltimaLinea));}
break;
case 99:
//#line 276 "gramatica.y"
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
case 100:
//#line 288 "gramatica.y"
{ yyval = new ParserVal("+"); }
break;
case 101:
//#line 289 "gramatica.y"
{ yyval = new ParserVal("-"); }
break;
case 102:
//#line 292 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
						  SA1(val_peek(2).sval);
						  if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
						      polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
						  else
						      polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
					      }
break;
case 103:
//#line 299 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 104:
//#line 300 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n",analizadorLexico.getNroLinea()));}
break;
case 105:
//#line 301 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n",analizadorLexico.getNroLinea()));}
break;
case 106:
//#line 302 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 107:
//#line 303 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 108:
//#line 304 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
/*                                                        invocacionID($3.sval, Main.VARIABLE);*/
/*                                                        SA1($3.sval);*/
                                                        if(ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                                                            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                        else
                                                            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                                                    }
break;
case 109:
//#line 313 "gramatica.y"
{Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 110:
//#line 318 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                                                    String id = val_peek(3).sval;
                                                    Token token = TablaSimbolos.getToken(id);
                                                    if(token != null) {
                                                        SA1(id);
                                                        SA2(val_peek(2).sval);
                                                    }
                                                    invocacionID(id, Main.VARIABLE);
                                                }
break;
case 111:
//#line 327 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n",analizadorLexico.getNroLinea()));}
break;
case 112:
//#line 328 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n",analizadorLexico.getNroLinea()));}
break;
case 113:
//#line 329 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n",analizadorLexico.getNroLinea()));}
break;
case 114:
//#line 330 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 115:
//#line 333 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                                                                invocacionID(val_peek(4).sval, "Procedimiento");
                                                            }
break;
case 116:
//#line 336 "gramatica.y"
{   imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                                                invocacionID(val_peek(3).sval, "Procedimiento");
                                            }
break;
case 117:
//#line 339 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 118:
//#line 340 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n",analizadorLexico.getNroLinea()));}
break;
case 119:
//#line 341 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n",analizadorLexico.getNroLinea()));}
break;
case 120:
//#line 342 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 121:
//#line 343 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n",analizadorLexico.getNroLinea()));}
break;
case 122:
//#line 344 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 123:
//#line 345 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n",nroUltimaLinea));}
break;
case 124:
//#line 348 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                                            invocacionID(val_peek(4).sval, "Parametro");
                                            invocacionID(val_peek(2).sval, "Parametro");
                                            invocacionID(val_peek(0).sval, "Parametro");
                                        }
break;
case 125:
//#line 353 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                                    invocacionID(val_peek(2).sval, "Parametro");
                                    invocacionID(val_peek(0).sval, "Parametro");
                                }
break;
case 126:
//#line 357 "gramatica.y"
{   imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                                invocacionID(val_peek(0).sval, "Parametro");
                            }
break;
case 127:
//#line 360 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n",analizadorLexico.getNroLinea()));}
break;
case 128:
//#line 361 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n",analizadorLexico.getNroLinea()));}
break;
case 129:
//#line 362 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 130:
//#line 363 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 131:
//#line 364 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 132:
//#line 365 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n",analizadorLexico.getNroLinea()));}
break;
case 133:
//#line 370 "gramatica.y"
{yyval = val_peek(2); SA2(">=");}
break;
case 134:
//#line 371 "gramatica.y"
{yyval = val_peek(2);SA2("<=");}
break;
case 135:
//#line 372 "gramatica.y"
{yyval = val_peek(2);SA2(">");}
break;
case 136:
//#line 373 "gramatica.y"
{yyval = val_peek(2);SA2("<");}
break;
case 137:
//#line 374 "gramatica.y"
{yyval = val_peek(2);SA2("==");}
break;
case 138:
//#line 375 "gramatica.y"
{yyval = val_peek(2);SA2("!=");}
break;
case 139:
//#line 378 "gramatica.y"
{yyval=val_peek(2);imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                                     SA2(val_peek(1).sval);}
break;
case 140:
//#line 380 "gramatica.y"
{yyval=val_peek(2);imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                                     SA2(val_peek(1).sval);}
break;
case 141:
//#line 382 "gramatica.y"
{yyval=val_peek(0);}
break;
case 142:
//#line 383 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n",analizadorLexico.getNroLinea()));}
break;
case 143:
//#line 384 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n",analizadorLexico.getNroLinea()));}
break;
case 144:
//#line 385 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n",analizadorLexico.getNroLinea()));}
break;
case 145:
//#line 388 "gramatica.y"
{ yyval = val_peek(2);
				imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
				SA2(val_peek(1).sval);}
break;
case 146:
//#line 391 "gramatica.y"
{ yyval = val_peek(2);
        			imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
        			SA2(val_peek(1).sval);}
break;
case 147:
//#line 394 "gramatica.y"
{yyval = val_peek(0);}
break;
case 148:
//#line 395 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
break;
case 149:
//#line 396 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la division %n",analizadorLexico.getNroLinea()));}
break;
case 150:
//#line 397 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n",analizadorLexico.getNroLinea()));}
break;
case 151:
//#line 398 "gramatica.y"
{ Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la division %n",analizadorLexico.getNroLinea()));}
break;
case 152:
//#line 401 "gramatica.y"
{yyval = val_peek(0); SA1(val_peek(0).sval);
		invocacionID(val_peek(0).sval, Main.VARIABLE);}
break;
case 153:
//#line 403 "gramatica.y"
{yyval = val_peek(0); SA1(val_peek(0).sval);}
break;
case 154:
//#line 406 "gramatica.y"
{String cte = val_peek(0).sval;
	   String nuevo = checkPositivo(cte);
	   if (nuevo != null)
	   	yyval = new ParserVal(nuevo);
	   else
	   	yyval = new ParserVal(cte);
           }
break;
case 155:
//#line 413 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  String nuevo = checkRango(cte);
      		  if (nuevo != null){
      		  	yyval = new ParserVal(nuevo);
      		  	imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                              		        }
     	 	}
break;
//#line 1912 "Parser.java"
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
