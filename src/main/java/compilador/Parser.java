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
import java.util.ArrayList;
import java.util.List;
//#line 21 "Parser.java"




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
    0,    2,    2,    4,    4,    1,    1,    5,    5,    6,
    6,    3,    3,    3,    3,    3,    7,    7,    7,   15,
   15,   15,   14,   14,    8,   16,   17,   17,   18,   19,
   20,   20,   20,   20,   20,   20,   20,   20,   21,   21,
   21,   21,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,   22,
   22,   22,   22,   22,   23,   24,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   27,   27,   26,   26,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   12,   12,   12,   12,   12,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   30,
   30,   30,   30,   30,   30,   30,   30,   30,   25,   25,
   25,   25,   31,   31,   31,   31,   31,   31,   29,   29,
   29,   29,   29,   29,   32,   32,   32,   32,   32,   32,
   32,   28,   28,   33,   33,
};
final static short yylen[] = {                            2,
    1,    2,    1,    4,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    3,    3,    3,
    1,    2,    1,    1,    4,    2,    3,    2,    3,    3,
    5,    3,    1,    4,    4,    2,    7,    3,    2,    3,
    2,    3,    6,    8,    5,    7,    7,    5,    7,    6,
    8,    5,    6,    7,    8,    8,    7,    8,    7,    3,
    2,    2,    2,    1,    1,    1,   12,   11,   12,   12,
   12,   12,   12,   12,   11,   12,   12,   12,   12,    9,
    4,    1,    1,    1,    5,    5,    4,    5,    4,    5,
    5,    4,    5,    4,    4,    4,    4,    3,    5,    4,
    4,    3,    4,    3,    5,    4,    3,    4,    3,    5,
    3,    1,    7,    3,    3,    4,    4,    2,    3,    2,
    3,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    3,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   23,   24,    0,    0,    0,    0,
    0,    8,    7,    9,   10,   11,   12,   13,   14,   15,
   16,    0,    0,    0,    0,    0,    0,  142,  144,    0,
    0,    0,    0,  137,    0,    0,  143,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   26,    0,    0,
    0,    0,    6,    0,    0,    0,    0,    0,    0,    0,
    0,  127,  123,  124,  128,    0,  125,  126,    0,    0,
    0,    0,    0,   65,    5,    0,    0,   63,    0,  145,
    0,    0,   61,  120,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  107,    0,    0,  104,    0,    0,    0,    0,
    0,  102,    0,   19,    0,   22,   18,   17,    0,    0,
   28,    0,    0,    0,    0,    0,    0,   95,    0,    0,
    0,  140,  141,    0,    0,    0,   60,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  138,
  135,  139,  136,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   89,    0,   96,  115,    0,  114,    0,    0,
  100,  106,    0,   97,   94,  103,    0,   87,  101,   20,
   41,    0,    0,   39,   27,    0,    0,    0,    0,   25,
   86,    0,    0,   66,    0,    0,    0,    0,   48,    0,
    0,    0,    0,    0,   45,    0,    0,    0,    0,    0,
    0,   88,   90,   91,   85,   93,  117,  116,    0,  105,
   99,    0,   42,   40,    0,    0,    0,   29,    0,    0,
    4,    0,    0,   50,    0,   53,    0,    0,    0,   43,
    0,    0,    0,    0,    0,    0,    0,    0,   83,   84,
    0,    0,    0,    0,   35,   34,   30,    0,   59,    0,
   49,    0,   54,    0,    0,   47,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,   51,
   55,   56,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  113,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   82,   80,    0,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    3,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   75,    0,   68,   69,   70,   71,   72,   73,
   74,   76,   77,   78,   79,   67,   81,
};
final static short yydgoto[] = {                         10,
   11,  320,   12,  194,   75,   14,   15,   16,   17,   18,
   19,   20,   21,   22,   56,   23,   58,  126,  190,  123,
  124,   32,   76,  195,   33,  251,  307,   34,   35,   47,
   72,   36,   37,
};
final static short yysindex[] = {                       553,
  -12,  147,  -37,  -30,    0,    0,   -3, -245,  -24,    0,
  553,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -203,   -9, -194,  157,  -35,  510,    0,    0,  -39,
 -177,  326,   67,    0,  672,  154,    0, -170,  139, -142,
  201,  157,  -21,   -2,   85,  228,  113,    0,  -40,  117,
  109,  136,    0,  130,    6,  -50,  -41,  -73,  169,  162,
  436,    0,    0,    0,    0,  510,    0,    0,  347,  -16,
  -16,  157,  553,    0,    0,  -52,  632,    0,  278,    0,
  274, -151,    0,    0,  430,  453,  456,  487,  497,  254,
  281,  105,  517,  305,  324,  -36,  309,  328,  437,   29,
 -190,  343,    0,  329,  -23,    0,  120,  462,  330,  132,
  346,    0,  349,    0,  135,    0,    0,    0,  153,  -70,
    0,  156,  390,  177,  373,  313,  382,    0,  188,  165,
  154,    0,    0,  356,  553,  510,    0,  133,  510,  395,
  303,  510,  397,  165,  154,  165,  154,  162,  356,    0,
    0,    0,    0,  160,  187,  192, -213,  406,  417,  418,
  419,  427,    0,  428,    0,    0,  219,    0,   52,  432,
    0,    0,  438,    0,    0,    0,  443,    0,    0,    0,
    0,  238,  239,    0,    0,  -87,  468,  241,  553,    0,
    0,  510,  389,    0,  253,  510,  460,  255,    0,   78,
  371,  464,  259,  263,    0,  470,  472,  474,  477,  -43,
 -173,    0,    0,    0,    0,    0,    0,    0,  270,    0,
    0,  517,    0,    0,  153,  247,  -85,    0,  530,  269,
    0,  488,  284,    0,  492,    0,   80,  494, -211,    0,
  495,  496,  517,  517,  517,  517,  517,  583,    0,    0,
  285,  512,  499,  -85,    0,    0,    0,  501,    0,  502,
    0,  504,    0,  519,  520,    0,    0,  521,  524,  525,
  526,  527,  798,   60,  535,  315, -173,  545,    0,    0,
    0,    0,    0, -173, -173, -173, -173, -173, -173, -226,
  293,  565,    0,  319,  338,  327,  334,  336,  339,  341,
  342,  348, -181,  560,  351,    0,    0,  561,    0,  569,
  597,  602,  609,  610,  618,  619,  620,   36,  565,  351,
    0,  565,  565,  565,  565,  565,  565,  565,  565,  565,
  565,  541,    0,  537,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  669,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  475,    0,    0,    1,    0,    0,    0,    0,
    0,    0,   21,    0,    0,    0,    0,    0,  629,    0,
    0,    0,    0,    0,   40,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  486,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   62,
    0,    0,    0,  151,    0,    0,    0,  174,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  636,    0,    0,    0,    0,    0,    0,
   24,    0,    0,  -26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   47,   70,   93,  116,  429,  452,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  197,    0,    0,    0,    0,    0,    0,   84,    0,
    0,    0,  360,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  639,    0,    0,    0,
    0,    0,  564,    0,    0,    0,    0,    0,    0,    0,
    0,  383,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  640,  641,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  402,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  642,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  506,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -54,    0,  574,  423,  678,  352,    0,    0,    0,    0,
    0,    0,    0,  -46,  -15,    0,    0,    0,    0,    0,
 -104,    0,  -10, -107,  626,  634,  614,   57,   30,    4,
  652,   79,    0,
};
final static int YYTABLESIZE=1058;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        121,
  131,   78,   39,  101,  162,   31,   70,   69,  118,   41,
  122,   71,   52,  122,  122,  248,   51,  173,  135,  187,
  110,   82,  101,  134,   68,   48,   67,   24,   31,  302,
   57,  198,  122,  203,  204,  172,   44,   45,  104,  116,
  131,  131,  209,  131,  264,  131,  132,  105,   25,  115,
  249,  250,   54,  265,   61,  129,  103,   46,  210,  131,
  131,  112,  131,  134,  134,  168,  134,   55,  134,  129,
  141,   99,  167,  183,  317,  108,  332,  122,   59,  112,
  169,  226,  134,  134,  230,  134,  132,  132,  233,  132,
  318,  132,  133,  239,   80,  219,  122,   98,   21,  180,
   90,  134,  118,  249,  250,  132,  132,   83,  132,  129,
  129,  142,  129,  143,  129,  130,  149,   24,  290,   24,
  118,  255,  256,  131,  111,  131,  132,  133,  129,  129,
   94,  129,  133,  133,  229,  133,  236,  133,   25,  122,
   25,  110,  111,  106,  151,  153,  134,  131,  134,  278,
  109,  133,  133,  109,  133,  130,  130,  111,  130,  110,
  130,   70,   69,  145,  147,  157,   71,  112,  225,  132,
  119,  132,   24,   98,  130,  130,  113,  130,  174,  122,
  122,    5,    6,    5,    6,  182,   30,  120,  114,  120,
  109,   31,  129,   25,  129,   88,   92,   93,    5,    6,
   89,   31,  125,   70,   69,  117,   70,  122,   71,  127,
  136,   71,  247,   98,  119,  133,   77,  133,   38,  161,
  186,   62,   63,   64,   65,   40,   66,    5,    6,  122,
  100,   28,   29,  120,  122,  122,   92,  122,  130,  122,
  130,   97,  122,  122,  122,   31,   49,  122,   50,  100,
  122,  122,   42,  102,   28,   29,  131,  131,  131,  131,
  131,  131,  131,  131,  131,  131,  131,   43,   43,  131,
  131,  131,   31,  109,  131,  109,   55,  131,  131,  134,
  134,  134,  134,  134,  134,  134,  134,  134,  134,  134,
  254,  331,  134,  134,  134,   21,   98,  134,   98,  166,
  134,  134,  132,  132,  132,  132,  132,  132,  132,  132,
  132,  132,  132,    9,  154,  132,  132,  132,  137,   92,
  132,   92,  218,  132,  132,  129,  129,  129,  129,  129,
  129,  129,  129,  129,  129,  129,  249,  250,  129,  129,
  129,  155,    9,  129,  262,  159,  129,  129,  133,  133,
  133,  133,  133,  133,  133,  133,  133,  133,  133,  108,
  156,  133,  133,  133,  160,    9,  133,  163,  164,  133,
  133,  130,  130,  130,  130,  130,  130,  130,  130,  130,
  130,  130,   52,  170,  130,  130,  130,  171,  176,  130,
    9,   31,  130,  130,   91,  196,   73,  197,   85,  108,
   86,   57,   26,  177,  178,   55,  109,  179,   27,   92,
    9,  109,   60,  109,  109,  109,  109,   28,   29,  109,
  109,  109,   52,  181,  109,   73,  184,   28,   29,   98,
  185,  206,  119,  188,   98,  189,   98,   98,   98,   98,
  191,   57,   98,   98,   98,    5,    6,   98,   73,   74,
  192,  120,   92,  199,   74,  205,   95,   92,  207,   92,
   92,   92,   92,  208,  211,   92,   92,   92,  121,  121,
   92,   28,   29,   96,   31,  212,  213,  214,   85,   85,
   86,   86,  108,  107,  108,  215,  216,  121,   74,  217,
  220,  119,  119,   73,  128,  165,  221,   31,   28,   29,
   31,  222,  119,   74,   85,   52,   86,   52,  223,  224,
  119,  227,  228,  231,   64,    5,    6,  232,  234,  235,
  175,  120,  240,  241,   57,   62,   57,  242,  243,  138,
  244,   31,  245,  258,    2,  246,  139,    3,  140,    4,
  252,   31,    5,    6,    7,    2,  259,    8,  260,    9,
  261,  121,  263,  266,  267,  276,  275,  277,  200,  279,
  280,   31,  281,    2,  304,  201,    3,  202,    4,    9,
  293,    5,    6,    7,  119,  292,    8,  282,  283,  284,
    9,    1,  285,  286,  287,  288,    2,   81,  295,    3,
  308,    4,    9,  309,    5,    6,    7,   64,  310,    8,
  319,  322,  130,    6,    9,  311,    1,  312,   62,  323,
  313,    2,  314,  315,    3,  108,    4,   28,   29,  316,
  108,    7,  108,  108,  108,  108,  237,   31,  108,  108,
  108,    2,   73,  108,    3,  238,    4,  324,   52,    5,
    6,    7,  325,   52,    8,   52,   52,   52,   52,  326,
  327,   52,   52,   52,  257,   79,   52,   57,  328,  329,
  330,  347,   57,  305,   57,   57,   57,   57,    1,  112,
   57,   57,   57,   70,   69,   57,   33,   13,   71,   36,
   38,   32,   31,  345,  121,  144,   87,  305,   53,  121,
  121,   68,  121,   67,  121,    0,    0,  121,  121,  121,
   28,   29,  121,    0,    0,  121,  121,  119,  146,    0,
    0,  148,  119,  119,   85,  119,   86,  119,  158,    0,
  119,  119,  119,   28,   29,  119,   28,   29,  119,  119,
   64,   68,    0,   67,    0,   64,   64,    0,   64,    0,
   64,   62,  150,   64,   64,   64,   62,   62,   64,   62,
   13,   62,  152,    0,   62,   62,   62,   28,   29,   62,
    0,    2,    0,    0,    0,    1,    2,   28,   29,    2,
    2,    2,   77,    3,    0,    4,    2,    0,    5,    6,
    7,    0,    0,    8,    0,    1,    0,   28,   29,    0,
    2,    0,    0,    3,    0,    4,    1,    0,    5,    6,
    7,    2,    0,    8,    3,    0,    4,    0,    1,    5,
    6,    7,  193,    2,    8,    0,    3,    0,    4,    6,
    1,    5,    6,    7,    6,    2,    8,    6,    3,    6,
    4,    0,    6,    6,    6,    7,    0,    6,  273,   70,
   69,    0,    0,    0,   71,    0,    0,  253,    0,    0,
    0,    0,    0,   28,   29,    0,  289,   68,    0,   67,
    0,    0,    0,    0,    0,  306,   13,    0,  268,  269,
  270,  271,  272,  274,    0,    0,    0,    0,  321,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   62,   63,
   64,   65,  306,  334,    0,  306,  306,  306,  306,  306,
  306,  306,  306,  306,  306,  306,   53,  291,    0,    0,
  294,    0,    0,    0,    0,    0,    0,  296,  297,  298,
  299,  300,  301,  303,    0,    0,    0,   84,   62,   63,
   64,   65,  333,    0,    0,  335,  336,  337,  338,  339,
  340,  341,  342,  343,  344,  346,    0,    0,    0,    0,
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
    0,    0,    0,    0,   62,   63,   64,   65,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   40,   44,   41,   45,   42,   43,   59,   40,
   57,   47,    9,   40,   41,   59,   41,   41,   73,  124,
   61,   32,   44,    0,   60,  271,   62,   40,   45,  256,
   40,  139,   59,  141,  142,   59,   40,   41,   41,   55,
   40,   41,  256,   43,  256,   45,    0,   44,   61,   44,
  277,  278,  256,  265,   25,   66,   59,   61,  272,   59,
   60,   41,   62,   40,   41,  256,   43,  271,   45,    0,
   81,   42,   44,  120,  256,   46,   41,  124,  273,   59,
  271,  186,   59,   60,  192,   62,   40,   41,  196,   43,
  272,   45,    0,  201,  272,   44,  123,   41,   59,  115,
  271,   72,   41,  277,  278,   59,   60,   41,   62,   40,
   41,  263,   43,  265,   45,    0,   87,   40,   59,   40,
   59,  226,  227,  123,   41,  125,   70,   71,   59,   60,
  273,   62,   40,   41,  189,   43,   59,   45,   61,  186,
   61,   41,   59,   59,   88,   89,  123,   69,  125,  254,
    0,   59,   60,   41,   62,   40,   41,   41,   43,   59,
   45,   42,   43,   85,   86,   61,   47,   59,  256,  123,
  256,  125,   40,    0,   59,   60,   41,   62,   59,  226,
  227,  269,  270,  269,  270,  256,   40,  275,   59,  275,
   40,   45,  123,   61,  125,   42,    0,   59,  269,  270,
   47,   45,  276,   42,   43,  256,   42,  254,   47,   41,
  263,   47,  256,   40,  256,  123,  256,  125,  256,  256,
   44,  257,  258,  259,  260,  256,  262,  269,  270,  256,
  271,  271,  272,  275,  261,  262,   40,  264,  123,  266,
  125,   41,  269,  270,  271,   45,  271,  274,  273,  271,
  277,  278,  256,  256,  271,  272,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  271,  271,  269,
  270,  271,   45,  123,  274,  125,  271,  277,  278,  256,
  257,  258,  259,  260,  261,  262,  263,  264,  265,  266,
   44,  256,  269,  270,  271,  256,  123,  274,  125,  271,
  277,  278,  256,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  266,   40,   61,  269,  270,  271,   41,  123,
  274,  125,  271,  277,  278,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  277,  278,  269,  270,
  271,   61,   40,  274,  265,   41,  277,  278,  256,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,    0,
  256,  269,  270,  271,   41,   40,  274,   59,   41,  277,
  278,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  266,    0,   41,  269,  270,  271,   59,   59,  274,
   40,   45,  277,  278,  256,  263,  123,  265,   43,   40,
   45,    0,  256,  272,   59,  271,  256,   59,  262,  271,
   40,  261,  256,  263,  264,  265,  266,  271,  272,  269,
  270,  271,   40,  271,  274,  123,  271,  271,  272,  256,
   41,  272,  256,   61,  261,  123,  263,  264,  265,  266,
   59,   40,  269,  270,  271,  269,  270,  274,  123,   27,
  263,  275,  256,   59,   32,   59,  256,  261,  272,  263,
  264,  265,  266,  272,   59,  269,  270,  271,   40,   41,
  274,  271,  272,  273,   45,   59,   59,   59,   43,   43,
   45,   45,  123,  256,  125,   59,   59,   59,   66,  271,
   59,   40,   41,  123,   59,   59,   59,   45,  271,  272,
   45,   59,  256,   81,   43,  123,   45,  125,  271,  271,
   59,   44,  272,  125,   40,  269,  270,  265,   59,  265,
   59,  275,   59,  265,  123,   40,  125,  265,   59,  256,
   59,   45,   59,  265,  261,   59,  263,  264,  265,  266,
  271,   45,  269,  270,  271,   40,   59,  274,  265,   40,
   59,  123,   59,   59,   59,   44,  272,   59,  256,   59,
   59,   45,   59,  261,  272,  263,  264,  265,  266,   40,
  256,  269,  270,  271,  123,   41,  274,   59,   59,   59,
   40,  256,   59,   59,   59,   59,  261,  262,   44,  264,
  272,  266,   40,  256,  269,  270,  271,  123,  272,  274,
   41,   41,  256,   40,   40,  272,  256,  272,  123,   41,
  272,  261,  272,  272,  264,  256,  266,  271,  272,  272,
  261,  271,  263,  264,  265,  266,  256,   45,  269,  270,
  271,  261,  123,  274,  264,  265,  266,   41,  256,  269,
  270,  271,   41,  261,  274,  263,  264,  265,  266,   41,
   41,  269,  270,  271,  125,   30,  274,  256,   41,   41,
   41,  125,  261,  123,  263,  264,  265,  266,    0,   41,
  269,  270,  271,   42,   43,  274,   41,    0,   47,   41,
   41,   41,   41,  332,  256,  256,   35,  123,   11,  261,
  262,   60,  264,   62,  266,   -1,   -1,  269,  270,  271,
  271,  272,  274,   -1,   -1,  277,  278,  256,  256,   -1,
   -1,  256,  261,  262,   43,  264,   45,  266,   93,   -1,
  269,  270,  271,  271,  272,  274,  271,  272,  277,  278,
  256,   60,   -1,   62,   -1,  261,  262,   -1,  264,   -1,
  266,  256,  256,  269,  270,  271,  261,  262,  274,  264,
   73,  266,  256,   -1,  269,  270,  271,  271,  272,  274,
   -1,  256,   -1,   -1,   -1,  256,  261,  271,  272,  264,
  261,  266,  256,  264,   -1,  266,  271,   -1,  269,  270,
  271,   -1,   -1,  274,   -1,  256,   -1,  271,  272,   -1,
  261,   -1,   -1,  264,   -1,  266,  256,   -1,  269,  270,
  271,  261,   -1,  274,  264,   -1,  266,   -1,  256,  269,
  270,  271,  135,  261,  274,   -1,  264,   -1,  266,  256,
  256,  269,  270,  271,  261,  261,  274,  264,  264,  266,
  266,   -1,  269,  270,  271,  271,   -1,  274,  256,   42,
   43,   -1,   -1,   -1,   47,   -1,   -1,  222,   -1,   -1,
   -1,   -1,   -1,  271,  272,   -1,   59,   60,   -1,   62,
   -1,   -1,   -1,   -1,   -1,  292,  189,   -1,  243,  244,
  245,  246,  247,  248,   -1,   -1,   -1,   -1,  305,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,  319,  320,   -1,  322,  323,  324,  325,  326,
  327,  328,  329,  330,  331,  332,  229,  274,   -1,   -1,
  277,   -1,   -1,   -1,   -1,   -1,   -1,  284,  285,  286,
  287,  288,  289,  290,   -1,   -1,   -1,  256,  257,  258,
  259,  260,  319,   -1,   -1,  322,  323,  324,  325,  326,
  327,  328,  329,  330,  331,  332,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
"declaracion_variables : tipo lista_variables error",
"declaracion_variables : tipo error ';'",
"lista_variables : ID ',' lista_variables",
"lista_variables : ID",
"lista_variables : ID lista_variables",
"tipo : LONGINT",
"tipo : FLOAT",
"declaracion_procedimiento : proc_encabezado proc_parametros proc_ni proc_cuerpo",
"proc_encabezado : PROC ID",
"proc_parametros : '(' lista_parametros_formales ')'",
"proc_parametros : '(' ')'",
"proc_ni : NI '=' CTE",
"proc_cuerpo : '{' lista_sentencias '}'",
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
"sentencia_seleccion : IF condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN error END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN error ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then END_IF",
"sentencia_seleccion : IF condicion_if THEN bloque_then error ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE error END_IF ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else error ';'",
"sentencia_seleccion : IF condicion_if THEN bloque_then ELSE bloque_else END_IF",
"sentencia_seleccion : IF error THEN bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF THEN bloque_then ELSE bloque_else END_IF ';'",
"condicion_if : '(' condicion ')'",
"condicion_if : condicion ')'",
"condicion_if : '(' condicion",
"condicion_if : '(' ')'",
"condicion_if : condicion",
"bloque_then : bloque_sentencias",
"bloque_else : bloque_sentencias",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR error ID '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' error '=' CTE ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID error CTE ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' error ';' condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE error condicion ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' error ';' incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion incr_decr CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' error CTE ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr error ')' bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE error bloque_for",
"sentencia_control : FOR '(' ID '=' CTE ';' condicion ';' incr_decr CTE ')' sentencia_declarativa",
"sentencia_control : FOR '(' ';' condicion ';' incr_decr CTE ')' bloque_for",
"bloque_for : '{' bloque_ejecutable sentencia_ejecutable '}'",
"bloque_for : sentencia_ejecutable",
"incr_decr : UP",
"incr_decr : DOWN",
"sentencia_salida : OUT '(' CADENA_MULT ')' ';'",
"sentencia_salida : error '(' CADENA_MULT ')' ';'",
"sentencia_salida : '(' CADENA_MULT ')' ';'",
"sentencia_salida : OUT error CADENA_MULT ')' ';'",
"sentencia_salida : OUT '(' ')' ';'",
"sentencia_salida : OUT '(' error ')' ';'",
"sentencia_salida : OUT '(' CADENA_MULT error ';'",
"sentencia_salida : OUT '(' CADENA_MULT ')'",
"sentencia_salida : OUT '(' factor ')' ';'",
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

//#line 287 "gramatica.y"

private AnalizadorLexico analizadorLexico;
private int nroUltimaLinea;
private Ambitos ambitos;

private String ultimoTipo;
private List<String> parametros_proc = new ArrayList<>();

public Parser(AnalizadorLexico analizadorLexico, boolean debug){
	this.analizadorLexico = analizadorLexico;
	this.yydebug = debug;
	this.ambitos = new Ambitos();
}

private void yyerror(String mensaje){
	//System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
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


public void checkRango(String cte){
	Token token = TablaSimbolos.getToken(cte);
	String tipo = token.getTipoToken();

	if (tipo.equals("LONGINT")){
	    long entero = 0;
		if (Long.parseLong(cte) <= Main.MAX_LONG-1) {
		    entero = Long.parseLong(cte);
		} else {
		    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: | Entero largo negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		    entero = Main.MAX_LONG-1;
		}
		String nuevoLexema = "-" + entero;
		int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", cont);
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "LONGINT", nuevoLexema);
		    nuevoToken.addAtributo("contador", 1);
		    TablaSimbolos.add(nuevoToken);
		}
		else {
		     cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
		     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
		}
	}
	if (tipo.equals("FLOAT")) {
	    float flotante = 0;
		if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
		    flotante =  Float.parseFloat(cte);
		} else {
		    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: | Flotante negativo fuera de rango: %s%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte);
		    flotante = Main.MAX_FLOAT;
		}
		String nuevoLexema = "-" + flotante;
		int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
		//if (cont == 0)
		//  TablaSimbolos.remove(cte);
		//else
		  TablaSimbolos.getToken(cte).addAtributo("contador", cont);
		if (!TablaSimbolos.existe(nuevoLexema)){
		    Token nuevoToken = new Token(token.getIdToken(), "FLOAT", nuevoLexema);
		    nuevoToken.addAtributo("contador", 1);
		    TablaSimbolos.add(nuevoToken);
		}
		else {
		     cont = (Integer) (TablaSimbolos.getToken(nuevoLexema).getAtributo("contador")) + 1 ;
		     TablaSimbolos.getToken(nuevoLexema).addAtributo("contador", cont);
                      }

	}
}

public boolean declaracionID(String lexema, String uso, String tipo) {
    Token token = TablaSimbolos.getToken(lexema);
    int cont = (Integer) (token.getAtributo("contador")) - 1;
    if(cont == 0)
        TablaSimbolos.remove(lexema);
    else {
        TablaSimbolos.getToken(lexema).removeAtributo("contador");
        TablaSimbolos.getToken(lexema).addAtributo("contador", cont);
    }
    String nuevoLexema = lexema + ":" + ambitos.getAmbitos();
    if(!TablaSimbolos.existe(nuevoLexema)) {
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
        nuevoToken.addAtributo("uso", uso);
        nuevoToken.addAtributo("contador", 0);
        nuevoToken.addAtributo("tipo", tipo);
        TablaSimbolos.add(nuevoToken);
        return true;
    }
    else
        return false;
}

public String getLexemaID() {
    String ambitosActuales = ambitos.getAmbitos();
    return(ambitosActuales.split(":")[ambitosActuales.split(":").length-1] + ":" + ambitosActuales);
}
//#line 782 "Parser.java"
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
case 17:
//#line 61 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Declaración de variables %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 18:
//#line 62 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' " + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 19:
//#line 63 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lista de variables " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 20:
//#line 66 "gramatica.y"
{ if(!declaracionID(val_peek(2).sval, "Variable", ultimoTipo))
                                                    System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable redeclarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                            }
break;
case 21:
//#line 69 "gramatica.y"
{ if(!declaracionID(val_peek(0).sval, "Variable", ultimoTipo))
                    System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable redeclarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
             }
break;
case 22:
//#line 72 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 23:
//#line 75 "gramatica.y"
{ ultimoTipo = "LONGINT"; }
break;
case 24:
//#line 76 "gramatica.y"
{ ultimoTipo = "FLOAT"; }
break;
case 25:
//#line 79 "gramatica.y"
{   System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                                                                        ambitos.eliminarAmbito();

                                                                                    }
break;
case 26:
//#line 85 "gramatica.y"
{   ambitos.agregarAmbito(val_peek(0).sval);
                                if(!declaracionID(val_peek(0).sval, "Procedimiento", null))
                                System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Procedimiento redeclarado %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()); }
break;
case 27:
//#line 90 "gramatica.y"
{ TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametros_proc));
                                                  parametros_proc.clear(); }
break;
case 29:
//#line 95 "gramatica.y"
{   String cte = val_peek(0).sval;
                            if(!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                                System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        }
break;
case 31:
//#line 105 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 32:
//#line 106 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 33:
//#line 107 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 34:
//#line 108 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 35:
//#line 109 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 36:
//#line 110 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 37:
//#line 111 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 38:
//#line 112 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 39:
//#line 115 "gramatica.y"
{ System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                parametros_proc.add(ultimoTipo + " " + val_peek(0).sval);
                                declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                              }
break;
case 40:
//#line 120 "gramatica.y"
{ System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                                    parametros_proc.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                                    TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                                  }
break;
case 41:
//#line 125 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 42:
//#line 126 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 43:
//#line 129 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 44:
//#line 130 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 45:
//#line 131 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 46:
//#line 132 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 47:
//#line 133 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 48:
//#line 134 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 49:
//#line 135 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 50:
//#line 136 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 51:
//#line 137 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de THEN en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 52:
//#line 138 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 53:
//#line 139 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 54:
//#line 140 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 55:
//#line 141 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias luego de ELSE en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 56:
//#line 142 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 57:
//#line 143 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 58:
//#line 144 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 59:
//#line 145 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 61:
//#line 149 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis '(' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 62:
//#line 150 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis ')' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 63:
//#line 151 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 64:
//#line 152 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan parentesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 67:
//#line 162 "gramatica.y"
{
							String cte = val_peek(2).sval;
							if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
							System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							else
							System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
							}
break;
case 68:
//#line 169 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada FOR en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 69:
//#line 170 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 70:
//#line 171 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 71:
//#line 172 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 72:
//#line 173 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta constante para inicializar identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 73:
//#line 174 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 74:
//#line 175 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 75:
//#line 176 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 76:
//#line 177 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 77:
//#line 178 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 78:
//#line 179 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 79:
//#line 180 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 80:
//#line 181 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 85:
//#line 192 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 86:
//#line 193 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 87:
//#line 194 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada OUT en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 88:
//#line 195 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 89:
//#line 196 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 90:
//#line 197 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 91:
//#line 198 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 92:
//#line 199 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 94:
//#line 203 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                            if(!TablaSimbolos.existe(val_peek(3).sval + ":" + ambitos.getAmbitos()))
                                System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                        }
break;
case 95:
//#line 207 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 96:
//#line 208 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 97:
//#line 209 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 98:
//#line 210 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 99:
//#line 213 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 100:
//#line 214 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 101:
//#line 215 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 102:
//#line 216 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 103:
//#line 217 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 104:
//#line 218 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 105:
//#line 219 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Parámetros inválidos %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 106:
//#line 220 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 107:
//#line 221 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 108:
//#line 222 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 109:
//#line 223 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);}
break;
case 110:
//#line 226 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 111:
//#line 227 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 112:
//#line 228 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 113:
//#line 229 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 114:
//#line 230 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Parámetro incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 115:
//#line 231 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Faltan literales ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 116:
//#line 232 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 117:
//#line 233 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 118:
//#line 234 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 119:
//#line 238 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Comparación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 120:
//#line 239 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta comparador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 121:
//#line 240 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el segundo operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 122:
//#line 241 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: Falta el primer operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 125:
//#line 246 "gramatica.y"
{yyval = new ParserVal('>');}
break;
case 126:
//#line 247 "gramatica.y"
{yyval = new ParserVal('<');}
break;
case 129:
//#line 252 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 130:
//#line 253 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 132:
//#line 255 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 133:
//#line 256 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 134:
//#line 257 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d:| Falta el primer operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 135:
//#line 260 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 136:
//#line 261 "gramatica.y"
{System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: División %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 138:
//#line 263 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 139:
//#line 264 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 140:
//#line 265 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 141:
//#line 266 "gramatica.y"
{System.out.printf( Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());}
break;
case 142:
//#line 269 "gramatica.y"
{   System.out.println(TablaSimbolos.getToken(val_peek(0).sval).getAtributo("contador"));
                    if(!TablaSimbolos.existe(val_peek(0).sval + ":" + ambitos.getAmbitos()))
                        System.out.printf(Main.ANSI_RED + "[GD] | Linea %d: Variable no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
break;
case 144:
//#line 276 "gramatica.y"
{String cte = val_peek(0).sval;
           }
break;
case 145:
//#line 278 "gramatica.y"
{ String cte = val_peek(0).sval;
      		  checkRango(cte);
      		  yyval = new ParserVal("-" + cte);
      		  String cte_nueva= "-"+cte;
      		  System.out.printf( Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte_nueva);
     	 	}
break;
//#line 1419 "Parser.java"
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
