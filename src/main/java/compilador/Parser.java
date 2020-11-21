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
//#line 20 "Parser.java"


public class Parser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
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
    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


    String yytext;//user variable to return contextual strings
    ParserVal yyval; //used to return semantic vals from action routines
    public static ParserVal yylval;//the 'lval' (result) I got from yylex()
    ParserVal valstk[];
    int valptr;

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new ParserVal[YYSTACKSIZE];
        yyval = new ParserVal();
        yylval = new ParserVal();
        valptr = -1;
    }

    void val_push(ParserVal val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    ParserVal val_pop() {
        if (valptr < 0)
            return new ParserVal();
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    ParserVal val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return new ParserVal();
        return valstk[ptr];
    }

    final ParserVal dup_yyval(ParserVal val) {
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }

    //#### end semantic value section ####
    public final static short IGUAL = 257;
    public final static short MAYOR_IGUAL = 258;
    public final static short MENOR_IGUAL = 259;
    public final static short DISTINTO = 260;
    public final static short IF = 261;
    public final static short THEN = 262;
    public final static short ELSE = 263;
    public final static short FOR = 264;
    public final static short END_IF = 265;
    public final static short OUT = 266;
    public final static short FUNC = 267;
    public final static short RETURN = 268;
    public final static short LONGINT = 269;
    public final static short FLOAT = 270;
    public final static short ID = 271;
    public final static short CTE = 272;
    public final static short CADENA_MULT = 273;
    public final static short PROC = 274;
    public final static short VAR = 275;
    public final static short NI = 276;
    public final static short UP = 277;
    public final static short DOWN = 278;
    public final static short YYERRCODE = 256;
    final static short yylhs[] = {-1,
            0, 2, 2, 2, 2, 5, 5, 1, 1, 6,
            6, 4, 4, 3, 3, 3, 3, 3, 7, 7,
            7, 15, 15, 15, 14, 14, 8, 8, 8, 8,
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
            8, 8, 8, 8, 8, 8, 8, 16, 16, 16,
            16, 16, 16, 16, 16, 17, 17, 17, 17, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 18, 18, 18, 18,
            18, 18, 19, 20, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 22, 22, 22, 22, 22, 25, 25,
            23, 24, 24, 11, 11, 11, 11, 11, 11, 11,
            12, 12, 12, 12, 12, 13, 13, 13, 13, 13,
            13, 13, 13, 13, 28, 28, 28, 28, 28, 28,
            28, 28, 28, 21, 21, 21, 21, 21, 21, 27,
            27, 27, 27, 27, 27, 29, 29, 29, 29, 29,
            29, 29, 26, 26, 30, 30,
    };
    final static short yylen[] = {2,
            1, 2, 1, 1, 2, 3, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 3,
            3, 3, 1, 2, 1, 1, 9, 8, 8, 7,
            8, 7, 9, 8, 9, 8, 9, 8, 8, 9,
            8, 9, 8, 9, 8, 5, 6, 5, 3, 1,
            4, 4, 2, 7, 3, 2, 3, 2, 3, 6,
            8, 5, 7, 7, 5, 7, 6, 8, 5, 5,
            7, 8, 7, 7, 5, 7, 3, 2, 2, 2,
            1, 3, 1, 1, 9, 8, 9, 10, 9, 9,
            8, 9, 8, 3, 3, 3, 3, 2, 3, 1,
            2, 1, 1, 5, 5, 4, 5, 5, 4, 5,
            4, 4, 4, 4, 3, 5, 4, 4, 3, 5,
            4, 3, 4, 3, 5, 3, 1, 7, 3, 3,
            4, 4, 2, 3, 3, 3, 3, 3, 3, 3,
            3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
            3, 3, 1, 1, 1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 153, 155, 0, 0, 0, 0,
            148, 0, 0, 154, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 8,
            0, 0, 0, 0, 0, 0, 0, 0, 83, 7,
            0, 0, 80, 0, 156, 0, 0, 78, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 122, 0, 0, 0, 0,
            0, 119, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 21, 0, 24, 20, 19, 112, 0, 0, 151,
            152, 0, 0, 0, 82, 77, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 149, 146, 150, 147, 95, 96, 97, 94,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 106,
            0, 113, 130, 0, 129, 0, 58, 0, 0, 0,
            0, 117, 56, 0, 0, 0, 121, 0, 114, 111,
            118, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            22, 6, 84, 0, 75, 0, 0, 0, 65, 0,
            0, 0, 0, 0, 62, 0, 101, 102, 103, 0,
            0, 0, 0, 105, 107, 108, 104, 110, 132, 131,
            0, 120, 59, 57, 0, 0, 0, 0, 0, 116,
            0, 0, 0, 0, 0, 0, 0, 0, 46, 0,
            0, 0, 0, 0, 0, 67, 0, 0, 0, 0,
            60, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 52, 51, 0, 0, 0, 0, 0,
            0, 0, 47, 0, 0, 0, 0, 0, 0, 0,
            76, 0, 66, 0, 71, 73, 0, 64, 63, 0,
            0, 0, 0, 0, 0, 0, 0, 30, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 32, 0, 68, 72, 61, 0,
            0, 0, 100, 93, 0, 0, 0, 0, 91, 86,
            128, 29, 0, 34, 0, 36, 0, 39, 0, 41,
            43, 45, 28, 38, 0, 0, 0, 0, 31, 87,
            0, 0, 3, 4, 0, 89, 90, 92, 85, 54,
            33, 35, 37, 40, 42, 44, 27, 0, 0, 99,
            2, 5, 88,
    };
    final static short yydgoto[] = {9,
            10, 352, 11, 12, 193, 60, 14, 15, 16, 17,
            18, 19, 20, 21, 53, 99, 100, 29, 61, 194,
            152, 38, 153, 210, 324, 31, 32, 46, 33, 34,
    };
    final static short yysindex[] = {729,
            20, 132, 37, -21, 0, 0, -20, 39, 0, 729,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -89, -41, 358, 677, 0, 0, 438, -237, 607, 62,
            0, 724, 159, 0, 50, -45, -48, 65, -140, 47,
            -41, -38, 137, 84, 311, 112, 134, -4, 215, 0,
            148, -14, 40, 117, 353, 27, 27, 729, 0, 0,
            -68, 162, 0, 169, 0, 574, 146, 0, -41, -41,
            -41, -41, -41, -41, 380, 430, 478, 502, -54, -47,
            -216, 408, -41, 211, -41, 187, 264, 33, 285, 320,
            466, 23, -72, -17, -83, 0, -50, 91, 340, 225,
            -13, 0, 617, 506, 330, 237, 247, 249, 125, 126,
            369, 0, 153, 0, 0, 0, 0, 295, 159, 0,
            0, -81, 677, 370, 0, 0, -36, 677, 375, 591,
            677, 399, 431, 431, 431, 431, 431, 431, 295, 159,
            295, 159, 0, 0, 0, 0, 0, 0, 0, 0,
            -41, 404, -112, 508, -112, 409, 413, 414, 419, 0,
            427, 0, 0, 216, 0, 29, 0, 432, 218, 227,
            441, 0, 0, 228, -114, 469, 0, 461, 0, 0,
            0, 255, 492, 258, 495, -31, 558, -34, 481, 270,
            0, 0, 0, 289, 0, 677, 498, 293, 0, 20,
            623, 500, 296, 297, 0, -112, 0, 0, 0, 288,
            799, -141, 292, 0, 0, 0, 0, 0, 0, 0,
            298, 0, 0, 0, 294, 514, 125, 470, -111, 0,
            519, 309, 525, 312, 530, 316, 547, 61, 0, 532,
            -186, 324, 534, 539, 334, 0, 541, -44, 542, -185,
            0, 544, 557, 345, 578, -112, 361, -188, 588, 586,
            677, 362, -111, 0, 0, 363, 577, 371, 583, 377,
            594, 510, 0, 385, -178, 393, 606, 80, 677, 396,
            0, 610, 0, 612, 0, 0, 613, 0, 0, 632,
            730, 403, 636, 637, 323, 730, 440, 0, 677, 639,
            677, 433, 677, 434, 677, 436, 677, 677, 677, 677,
            677, 439, 444, -149, 0, 677, 0, 0, 0, 730,
            35, 729, 0, 0, 663, 730, 730, 689, 0, 0,
            0, 0, 462, 0, 677, 0, 677, 0, 677, 0,
            0, 0, 0, 0, 677, 677, 677, 677, 0, 0,
            41, 700, 0, 0, 730, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 678, 665, 0,
            0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 727,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 640,
            0, 0, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 42, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 45, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 660, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 673,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 54, 0, 0, 0, 0, 121, 0, 0, -33,
            0, 0, 0, 191, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 69, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 392, 418, 429, 451, 467, 491, 89, 109,
            157, 179, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 274, 0,
            0, 0, 0, 0, 0, 79, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -29, 0, 307, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 341,
            0, 357, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -28, -27, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 85,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 376, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -26,
            0, 0, 0, 0, 0, 0, 745, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 121, 0,
            0, 0, 0,
    };
    final static short yygindex[] = {0,
            685, 0, 682, -295, -24, 48, 0, 0, 0, 0,
            0, 0, 0, 699, -15, 10, -82, 0, -6, -99,
            36, 698, -46, -121, 201, 9, -19, -42, 64, 0,
    };
    final static int YYTABLESIZE = 1037;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{59,
                101, 142, 54, 28, 59, 93, 241, 50, 172, 236,
                83, 53, 55, 49, 48, 81, 22, 176, 40, 43,
                44, 91, 67, 168, 22, 104, 354, 178, 198, 113,
                203, 204, 358, 213, 65, 108, 114, 30, 155, 149,
                45, 59, 142, 192, 142, 177, 142, 13, 90, 133,
                134, 135, 136, 137, 138, 150, 372, 50, 111, 130,
                142, 142, 64, 142, 120, 121, 164, 294, 145, 277,
                286, 28, 221, 159, 351, 44, 37, 309, 49, 287,
                22, 369, 127, 295, 254, 144, 146, 89, 143, 278,
                258, 28, 228, 310, 133, 45, 245, 191, 116, 96,
                127, 250, 68, 23, 206, 13, 347, 212, 140, 145,
                79, 145, 133, 145, 257, 183, 185, 188, 119, 126,
                124, 275, 348, 85, 142, 125, 142, 145, 145, 143,
                145, 143, 86, 143, 292, 208, 209, 126, 140, 142,
                314, 227, 102, 125, 109, 264, 265, 143, 143, 140,
                143, 140, 105, 140, 5, 6, 144, 5, 6, 75,
                95, 76, 239, 95, 208, 209, 51, 140, 140, 50,
                140, 27, 169, 106, 1, 117, 28, 97, 141, 2,
                300, 52, 3, 165, 4, 5, 6, 5, 6, 7,
                115, 145, 8, 145, 123, 96, 124, 144, 166, 144,
                77, 144, 125, 56, 55, 78, 112, 82, 57, 126,
                80, 143, 273, 143, 23, 144, 144, 147, 144, 141,
                284, 141, 36, 141, 148, 171, 196, 156, 197, 25,
                26, 140, 92, 140, 39, 41, 298, 141, 141, 167,
                141, 240, 50, 124, 235, 124, 53, 55, 49, 48,
                42, 107, 54, 167, 315, 110, 52, 142, 142, 142,
                142, 142, 142, 142, 142, 142, 142, 142, 175, 154,
                142, 142, 142, 109, 332, 142, 334, 182, 336, 144,
                338, 144, 340, 341, 342, 343, 344, 184, 158, 187,
                41, 349, 35, 163, 47, 115, 368, 25, 26, 220,
                23, 141, 87, 141, 157, 42, 123, 36, 101, 48,
                361, 42, 362, 115, 363, 115, 274, 25, 26, 88,
                364, 365, 366, 367, 145, 145, 145, 145, 145, 145,
                145, 145, 145, 145, 145, 313, 56, 145, 145, 145,
                70, 57, 145, 160, 143, 143, 143, 143, 143, 143,
                143, 143, 143, 143, 143, 28, 69, 143, 143, 143,
                161, 173, 143, 328, 140, 140, 140, 140, 140, 140,
                140, 140, 140, 140, 140, 74, 124, 140, 140, 140,
                174, 124, 140, 124, 124, 124, 124, 23, 181, 124,
                124, 124, 94, 24, 124, 167, 109, 28, 109, 56,
                55, 189, 25, 26, 57, 5, 6, 42, 131, 190,
                132, 95, 144, 144, 144, 144, 144, 144, 144, 144,
                144, 144, 144, 52, 28, 144, 144, 144, 195, 123,
                144, 123, 138, 199, 141, 141, 141, 141, 141, 141,
                141, 141, 141, 141, 141, 322, 115, 141, 141, 141,
                138, 115, 141, 115, 115, 115, 115, 205, 134, 115,
                115, 115, 207, 70, 115, 70, 151, 214, 79, 135,
                109, 215, 216, 75, 28, 76, 134, 217, 63, 69,
                109, 69, 28, 5, 6, 218, 219, 135, 223, 95,
                222, 139, 109, 5, 6, 329, 330, 224, 74, 95,
                74, 225, 109, 226, 186, 5, 6, 136, 75, 139,
                76, 95, 229, 263, 138, 5, 6, 5, 6, 230,
                350, 95, 28, 95, 162, 136, 356, 357, 359, 109,
                231, 137, 232, 233, 109, 234, 109, 109, 109, 109,
                134, 242, 109, 109, 109, 243, 28, 109, 75, 137,
                76, 135, 28, 244, 28, 373, 246, 247, 251, 255,
                252, 253, 123, 259, 180, 261, 103, 123, 260, 123,
                123, 123, 123, 139, 262, 123, 123, 123, 1, 266,
                123, 25, 26, 2, 267, 268, 3, 269, 4, 136,
                270, 271, 276, 321, 280, 279, 70, 281, 282, 283,
                285, 70, 288, 70, 70, 70, 70, 272, 118, 70,
                70, 70, 69, 137, 70, 289, 290, 69, 291, 69,
                69, 69, 69, 25, 26, 69, 69, 69, 296, 297,
                69, 74, 293, 299, 301, 139, 74, 302, 74, 74,
                74, 74, 303, 304, 74, 74, 74, 138, 305, 74,
                25, 26, 138, 138, 306, 138, 308, 138, 56, 55,
                138, 138, 138, 57, 311, 138, 312, 316, 317, 58,
                318, 319, 320, 134, 325, 179, 326, 327, 134, 134,
                58, 134, 333, 134, 135, 141, 134, 134, 134, 135,
                135, 134, 135, 62, 135, 331, 58, 135, 135, 135,
                25, 26, 135, 355, 335, 337, 139, 339, 25, 26,
                345, 139, 139, 58, 139, 346, 139, 360, 168, 139,
                139, 139, 136, 172, 139, 109, 1, 136, 136, 58,
                136, 98, 136, 143, 84, 136, 136, 136, 5, 6,
                136, 98, 122, 0, 95, 58, 137, 98, 25, 26,
                0, 137, 137, 0, 137, 0, 137, 145, 0, 137,
                137, 137, 81, 211, 137, 23, 75, 0, 76, 0,
                0, 0, 25, 26, 0, 0, 0, 0, 25, 26,
                25, 307, 79, 74, 0, 73, 155, 155, 0, 155,
                0, 155, 0, 170, 0, 0, 0, 0, 98, 58,
                0, 0, 1, 155, 98, 98, 98, 2, 0, 0,
                3, 322, 4, 237, 0, 5, 6, 7, 2, 0,
                8, 3, 0, 4, 370, 0, 5, 6, 7, 127,
                0, 8, 0, 238, 2, 0, 128, 3, 129, 4,
                56, 55, 5, 6, 7, 57, 200, 8, 0, 0,
                0, 2, 322, 201, 3, 202, 4, 256, 0, 5,
                6, 7, 1, 0, 8, 0, 0, 2, 66, 0,
                3, 0, 4, 98, 0, 5, 6, 7, 248, 0,
                8, 0, 0, 2, 0, 0, 3, 249, 4, 0,
                0, 5, 6, 7, 0, 81, 8, 0, 0, 0,
                81, 81, 0, 81, 0, 81, 0, 0, 81, 81,
                81, 0, 0, 81, 0, 79, 0, 0, 0, 0,
                79, 79, 0, 79, 0, 79, 98, 98, 79, 79,
                79, 0, 1, 79, 0, 0, 0, 2, 0, 0,
                3, 0, 4, 0, 1, 5, 6, 7, 0, 2,
                8, 0, 3, 0, 4, 1, 0, 5, 6, 7,
                2, 98, 8, 3, 0, 4, 0, 0, 5, 6,
                7, 0, 323, 8, 0, 0, 323, 323, 0, 0,
                69, 70, 71, 72, 1, 1, 0, 0, 0, 2,
                2, 0, 3, 3, 4, 4, 0, 5, 6, 7,
                321, 323, 8, 353, 0, 0, 0, 323, 323, 323,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 371, 0, 0, 323,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{24,
                43, 0, 22, 45, 29, 44, 41, 41, 59, 41,
                59, 41, 41, 41, 41, 61, 61, 100, 40, 40,
                41, 41, 29, 41, 61, 45, 322, 41, 128, 44,
                130, 131, 328, 155, 272, 40, 52, 2, 85, 256,
                61, 66, 41, 125, 43, 59, 45, 0, 40, 69,
                70, 71, 72, 73, 74, 272, 352, 10, 49, 66,
                59, 60, 27, 62, 56, 57, 44, 256, 0, 256,
                256, 45, 44, 41, 40, 41, 40, 256, 40, 265,
                61, 41, 41, 272, 206, 77, 78, 41, 0, 276,
                212, 45, 175, 272, 41, 61, 196, 113, 59, 59,
                59, 201, 41, 59, 151, 58, 256, 154, 0, 41,
                61, 43, 59, 45, 256, 106, 107, 108, 55, 41,
                0, 61, 272, 59, 123, 41, 125, 59, 60, 41,
                62, 43, 273, 45, 256, 277, 278, 59, 75, 76,
                61, 256, 59, 59, 256, 228, 229, 59, 60, 41,
                62, 43, 41, 45, 269, 270, 0, 269, 270, 43,
                275, 45, 187, 275, 277, 278, 256, 59, 60, 122,
                62, 40, 256, 40, 256, 59, 45, 41, 0, 261,
                263, 271, 264, 256, 266, 269, 270, 269, 270, 271,
                0, 123, 274, 125, 263, 59, 265, 41, 271, 43,
                42, 45, 41, 42, 43, 47, 59, 256, 47, 41,
                256, 123, 237, 125, 256, 59, 60, 272, 62, 41,
                265, 43, 271, 45, 272, 276, 263, 41, 265, 271,
                272, 123, 271, 125, 256, 256, 261, 59, 60, 271,
                62, 276, 276, 123, 276, 125, 276, 276, 276, 276,
                271, 256, 272, 271, 279, 41, 271, 256, 257, 258,
                259, 260, 261, 262, 263, 264, 265, 266, 44, 59,
                269, 270, 271, 0, 299, 274, 301, 41, 303, 123,
                305, 125, 307, 308, 309, 310, 311, 41, 256, 41,
                256, 316, 256, 271, 256, 256, 256, 271, 272, 271,
                256, 123, 256, 125, 41, 271, 0, 271, 351, 271,
                335, 271, 337, 123, 339, 125, 256, 271, 272, 273,
                345, 346, 347, 348, 256, 257, 258, 259, 260, 261,
                262, 263, 264, 265, 266, 256, 42, 269, 270, 271,
                0, 47, 274, 59, 256, 257, 258, 259, 260, 261,
                262, 263, 264, 265, 266, 45, 0, 269, 270, 271,
                41, 271, 274, 41, 256, 257, 258, 259, 260, 261,
                262, 263, 264, 265, 266, 0, 256, 269, 270, 271,
                41, 261, 274, 263, 264, 265, 266, 256, 59, 269,
                270, 271, 256, 262, 274, 271, 123, 45, 125, 42,
                43, 276, 271, 272, 47, 269, 270, 271, 263, 41,
                265, 275, 256, 257, 258, 259, 260, 261, 262, 263,
                264, 265, 266, 271, 45, 269, 270, 271, 59, 123,
                274, 125, 41, 59, 256, 257, 258, 259, 260, 261,
                262, 263, 264, 265, 266, 123, 256, 269, 270, 271,
                59, 261, 274, 263, 264, 265, 266, 59, 41, 269,
                270, 271, 59, 123, 274, 125, 59, 59, 61, 41,
                256, 59, 59, 43, 45, 45, 59, 59, 41, 123,
                256, 125, 45, 269, 270, 59, 271, 59, 271, 275,
                59, 41, 256, 269, 270, 295, 296, 271, 123, 275,
                125, 61, 256, 276, 256, 269, 270, 41, 43, 59,
                45, 275, 44, 44, 123, 269, 270, 269, 270, 59,
                320, 275, 45, 275, 59, 59, 326, 327, 328, 256,
                276, 41, 41, 276, 261, 41, 263, 264, 265, 266,
                123, 61, 269, 270, 271, 276, 45, 274, 43, 59,
                45, 123, 45, 265, 45, 355, 59, 265, 59, 272,
                265, 265, 256, 272, 59, 272, 256, 261, 271, 263,
                264, 265, 266, 123, 61, 269, 270, 271, 256, 61,
                274, 271, 272, 261, 276, 61, 264, 276, 266, 123,
                61, 276, 61, 271, 61, 272, 256, 59, 265, 59,
                59, 261, 59, 263, 264, 265, 266, 61, 256, 269,
                270, 271, 256, 123, 274, 59, 272, 261, 41, 263,
                264, 265, 266, 271, 272, 269, 270, 271, 41, 44,
                274, 256, 272, 272, 272, 256, 261, 61, 263, 264,
                265, 266, 272, 61, 269, 270, 271, 256, 272, 274,
                271, 272, 261, 262, 61, 264, 272, 266, 42, 43,
                269, 270, 271, 47, 272, 274, 61, 272, 59, 123,
                59, 59, 41, 256, 272, 59, 41, 41, 261, 262,
                123, 264, 44, 266, 256, 256, 269, 270, 271, 261,
                262, 274, 264, 256, 266, 256, 123, 269, 270, 271,
                271, 272, 274, 41, 272, 272, 256, 272, 271, 272,
                272, 261, 262, 123, 264, 272, 266, 256, 41, 269,
                270, 271, 256, 59, 274, 256, 0, 261, 262, 123,
                264, 59, 266, 256, 37, 269, 270, 271, 269, 270,
                274, 43, 58, -1, 275, 123, 256, 49, 271, 272,
                -1, 261, 262, -1, 264, -1, 266, 256, -1, 269,
                270, 271, 123, 256, 274, 256, 43, -1, 45, -1,
                -1, -1, 271, 272, -1, -1, -1, -1, 271, 272,
                271, 272, 123, 60, -1, 62, 42, 43, -1, 45,
                -1, 47, -1, 95, -1, -1, -1, -1, 100, 123,
                -1, -1, 256, 59, 106, 107, 108, 261, -1, -1,
                264, 123, 266, 256, -1, 269, 270, 271, 261, -1,
                274, 264, -1, 266, 125, -1, 269, 270, 271, 256,
                -1, 274, -1, 276, 261, -1, 263, 264, 265, 266,
                42, 43, 269, 270, 271, 47, 256, 274, -1, -1,
                -1, 261, 123, 263, 264, 265, 266, 59, -1, 269,
                270, 271, 256, -1, 274, -1, -1, 261, 262, -1,
                264, -1, 266, 175, -1, 269, 270, 271, 256, -1,
                274, -1, -1, 261, -1, -1, 264, 265, 266, -1,
                -1, 269, 270, 271, -1, 256, 274, -1, -1, -1,
                261, 262, -1, 264, -1, 266, -1, -1, 269, 270,
                271, -1, -1, 274, -1, 256, -1, -1, -1, -1,
                261, 262, -1, 264, -1, 266, 228, 229, 269, 270,
                271, -1, 256, 274, -1, -1, -1, 261, -1, -1,
                264, -1, 266, -1, 256, 269, 270, 271, -1, 261,
                274, -1, 264, -1, 266, 256, -1, 269, 270, 271,
                261, 263, 274, 264, -1, 266, -1, -1, 269, 270,
                271, -1, 291, 274, -1, -1, 295, 296, -1, -1,
                257, 258, 259, 260, 256, 256, -1, -1, -1, 261,
                261, -1, 264, 264, 266, 266, -1, 269, 270, 271,
                271, 320, 274, 322, -1, -1, -1, 326, 327, 328,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, 352, -1, -1, 355,
        };
    }

    final static short YYFINAL = 9;
    final static short YYMAXTOKEN = 278;
    final static String yyname[] = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
            "'-'", null, "'/'", null, null, null, null, null, null, null, null, null, null, null, "';'",
            "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, "IGUAL", "MAYOR_IGUAL", "MENOR_IGUAL",
            "DISTINTO", "IF", "THEN", "ELSE", "FOR", "END_IF", "OUT", "FUNC", "RETURN", "LONGINT",
            "FLOAT", "ID", "CTE", "CADENA_MULT", "PROC", "VAR", "NI", "UP", "DOWN",
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
            "declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : ID '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : ID '(' ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC '(' ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC error '(' lista_parametros_formales ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC error '(' ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID error lista_parametros_formales ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID error ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' error ')' NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' lista_parametros_formales NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' error NI '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' error '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' error '=' CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI error CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' NI error CTE bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' lista_parametros_formales ')' NI '=' error bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' NI '=' error bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' bloque_sentencias",
            "declaracion_procedimiento : PROC ID '(' ')' error bloque_sentencias",
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

//#line 348 "gramatica.y"

    private AnalizadorLexico analizadorLexico;
    private int nroUltimaLinea;
    private PolacaInversa polaca;
    private boolean verbose;
    private int yyerrores;

    public Parser(AnalizadorLexico analizadorLexico, boolean debug, PolacaInversa polaca, boolean verbose) {
        this.analizadorLexico = analizadorLexico;
        this.yydebug = debug;
        this.polaca = polaca;
        this.verbose = verbose;
        this.yyerrores = 0;
    }

    private void yyerror(String mensaje) {
        //System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
    }

    public int getErrores() {
        return yyerrores;
    }

    private int yylex() {
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
        String tipo = token.getTipoToken();
        if (tipo.equals("LONGINT")) {
            long entero = 0;
            if (Long.parseLong(cte) >= Main.MAX_LONG) {
                entero = Main.MAX_LONG - 1;
                warning("Entero largo positivo fuera de rango: " + cte + " - Se cambia por: " + entero, analizadorLexico.getNroLinea());
                String nuevoLexema = String.valueOf(entero);
                cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
                return nuevoLexema;
            }
        }
        return cte;
    }


    public String checkRango(String cte) {
        Token token = TablaSimbolos.getToken(cte);
        String tipo = token.getTipoToken();

        if (tipo.equals("LONGINT")) {
            long entero = 0;
            String nuevoLexema = null;
            if (Long.parseLong(cte) <= Main.MAX_LONG) {
                entero = Long.parseLong(cte);
            } else {
                entero = Main.MAX_LONG;
                warning("Entero largo negativo fuera de rango: " + cte + " - Se cambia por: " + entero, analizadorLexico.getNroLinea());
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
                flotante = Main.MAX_FLOAT - 1;
                warning("Flotante negativo fuera de rango: " + cte + " - Se cambia por: " + flotante, analizadorLexico.getNroLinea());
            }
            if (flotante != 0f) {
                String nuevoLexema = "-" + flotante;
                cambiarSimbolo(token, cte, nuevoLexema, "FLOAT");
                return nuevoLexema;
            }
        }
        return null;
    }

    public void cambiarSimbolo(Token token, String cte, String nuevoLexema, String tipo) {
        int cont = (Integer) (TablaSimbolos.getToken(cte).getAtributo("contador")) - 1;
        //if (cont == 0)
        //  TablaSimbolos.remove(cte);
        //else
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

    public void error(String mensaje, int linea) {
        yyerrores++;
        if (verbose)
            System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: " + mensaje + " %n" + Main.ANSI_RESET, linea);

    }

    public void warning(String mensaje, int linea) {
        if (verbose)
            System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: " + mensaje + "%n" + Main.ANSI_RESET, linea);
    }

    public void out(String lex) {
        Token token = TablaSimbolos.getToken(lex);
        if (token != null) {
            if (token.getTipoToken().equals("IDENTIFICADOR")) {
                if (token.getAtributo("USO") != null) {
                    if (token.getAtributo("USO").equals("VARIABLE")) {
                        Object valor = token.getAtributo("VALOR");
                        if (valor != null)
                            System.out.println(token.getAtributo("VALOR") + "\n");
                        else
                            error("Variable " + lex + " no inicializada", analizadorLexico.getNroLinea());
                    }
                } else
                    error("Variable " + lex + " no declarada", analizadorLexico.getNroLinea());
            } else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                System.out.println(token.getLexema() + "\n");
        }
    }

    public void SA1(String lex) {  //añadir factor a la polaca
        Token token = TablaSimbolos.getToken(lex);
        ElemSimple elem = new ElemSimple(token, analizadorLexico.getNroLinea());
        polaca.addElem(elem, false);
    }

    public void SA2(String operador) { //añadir operador binario a la polaca
        OperadorBinario elem = new OperadorBinario(operador, analizadorLexico.getNroLinea());
        polaca.addElem(elem, false);
    }

    public void SA3(String cte) { //chequea que la constante sea LONGINT
        if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
            error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
    }

    public void SA4(String id1, String id2) { //reviso que la variable inicializada en el for sea la misma que la de la condicion
        Token token1 = TablaSimbolos.getToken(id1);
        Token token2 = TablaSimbolos.getToken(id2);
        if (!token1.equals(token2))
            error("En la sentencia for, la variable inicializada " + id1 + "no es la misma que la variable utilizada en la condicion", analizadorLexico.getNroLinea());
    }

    public void SA5(String id, String cte, String op) { //incremento o decremento la variable del for
        Token id_t = TablaSimbolos.getToken(id);
        Token cte_t = TablaSimbolos.getToken(cte);

        polaca.addElem(new ElemSimple(id_t), false);
        polaca.addElem(new ElemSimple(cte_t), false);
        polaca.addElem(new OperadorBinario(op), false);
        polaca.addElem(new ElemSimple(id_t), false);
        polaca.addElem(new OperadorBinario("="), false);
    }

    //#line 853 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }


    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string


    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    nroUltimaLinea = analizadorLexico.getNroLinea();
                    yychar = yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
            switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                case 4:
//#line 35 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 5:
//#line 36 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 19:
//#line 63 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Declaración de variables %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 20:
//#line 64 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 21:
//#line 65 "gramatica.y"
                {
                    error("Falta lista de variables", analizadorLexico.getNroLinea());
                }
                break;
                case 23:
//#line 69 "gramatica.y"
                {
                    String id = val_peek(0).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("USO", "VARIABLE");
                    }
                }
                break;
                case 24:
//#line 75 "gramatica.y"
                {
                    error("Falta literal ',' ", analizadorLexico.getNroLinea());
                }
                break;
                case 27:
//#line 82 "gramatica.y"
                {
                    String id = val_peek(7).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("USO", "PROCEDIMIENTO");
                    }

                    String cte = val_peek(1).sval;
                    if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    else
                        error("Tipo incorrecto de CTE NI", analizadorLexico.getNroLinea());
                }
                break;
                case 28:
//#line 94 "gramatica.y"
                {
                    String id = val_peek(6).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("USO", "PROCEDIMIENTO");
                    }

                    String cte = val_peek(1).sval;
                    if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    else
                        error("Tipo incorrecto de CTE NI", analizadorLexico.getNroLinea());
                }
                break;
                case 29:
//#line 106 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 30:
//#line 107 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 31:
//#line 108 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 32:
//#line 109 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 33:
//#line 110 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 34:
//#line 111 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 35:
//#line 112 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 36:
//#line 113 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 37:
//#line 114 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 38:
//#line 115 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 39:
//#line 116 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 40:
//#line 117 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 41:
//#line 118 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 42:
//#line 119 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 43:
//#line 120 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 44:
//#line 121 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 45:
//#line 122 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 46:
//#line 123 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 47:
//#line 124 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 48:
//#line 127 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 49:
//#line 128 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 50:
//#line 129 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 51:
//#line 130 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 52:
//#line 131 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 53:
//#line 132 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 54:
//#line 133 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 55:
//#line 134 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 56:
//#line 137 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String param = val_peek(0).sval;
                    Token token = TablaSimbolos.getToken(param);
                    if (token != null) {
                        token.addAtributo("USO", "PARAMETRO");
                        token.addAtributo("PASAJE", "COPIA");
                    }
                }
                break;
                case 57:
//#line 145 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String param = val_peek(0).sval;
                    Token token = TablaSimbolos.getToken(param);
                    if (token != null) {
                        token.addAtributo("USO", "PARAMETRO");
                        token.addAtributo("PASAJE", "VAR");
                    }
                }
                break;
                case 58:
//#line 153 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 59:
//#line 154 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 60:
//#line 157 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    polaca.addElem(new ElemPos(polaca.size()), true);
                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 61:
//#line 161 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    polaca.addElem(new ElemPos(polaca.size()), true);
                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 62:
//#line 165 "gramatica.y"
                {
                    error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());
                }
                break;
                case 63:
//#line 166 "gramatica.y"
                {
                    error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());
                }
                break;
                case 64:
//#line 167 "gramatica.y"
                {
                    error("Falta palabra reservada ELSE ", analizadorLexico.getNroLinea());
                }
                break;
                case 65:
//#line 168 "gramatica.y"
                {
                    error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 66:
//#line 169 "gramatica.y"
                {
                    error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 67:
//#line 170 "gramatica.y"
                {
                    error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 68:
//#line 171 "gramatica.y"
                {
                    error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 69:
//#line 172 "gramatica.y"
                {
                    error(" Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 70:
//#line 173 "gramatica.y"
                {
                    error(" Falta palabra reservada END_IF y literal ';'", nroUltimaLinea);
                }
                break;
                case 71:
//#line 174 "gramatica.y"
                {
                    error("Falta bloque de sentencias ELSE", analizadorLexico.getNroLinea());
                }
                break;
                case 72:
//#line 175 "gramatica.y"
                {
                    error("Error en bloque de sentencias ELSE", analizadorLexico.getNroLinea());
                }
                break;
                case 73:
//#line 176 "gramatica.y"
                {
                    error("Falta palabra reservada END_IF", analizadorLexico.getNroLinea());
                }
                break;
                case 74:
//#line 177 "gramatica.y"
                {
                    error(" Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 75:
//#line 178 "gramatica.y"
                {
                    error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);
                }
                break;
                case 76:
//#line 179 "gramatica.y"
                {
                    error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);
                }
                break;
                case 77:
//#line 182 "gramatica.y"
                {
                    polaca.pushPos(true);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                }
                break;
                case 78:
//#line 183 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 79:
//#line 184 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 80:
//#line 185 "gramatica.y"
                {
                    error("Falta condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 81:
//#line 186 "gramatica.y"
                {
                    error("Faltan parentesis", analizadorLexico.getNroLinea());
                }
                break;
                case 82:
//#line 187 "gramatica.y"
                {
                    error("Error en la condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 83:
//#line 191 "gramatica.y"
                {
                    polaca.addElem(new ElemPos(polaca.size() + 2), true);
                    polaca.pushPos(true);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI), false);
                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 85:
//#line 200 "gramatica.y"
                {
                    SA3(val_peek(2).sval);
                    SA4(val_peek(6).sval, val_peek(4).sval);
                    SA5(val_peek(6).sval, val_peek(2).sval, val_peek(3).sval); /* id cte incr_decr*/
                    polaca.addElem(new ElemPos(polaca.size() + 2), true);
                    polaca.addElem(new ElemPos(polaca.popPos()), false);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI), false);
                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 86:
//#line 209 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 87:
//#line 210 "gramatica.y"
                {
                    error("Error en el inicio de la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 88:
//#line 211 "gramatica.y"
                {
                    error("Falta condición de control en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 89:
//#line 212 "gramatica.y"
                {
                    error("Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 90:
//#line 213 "gramatica.y"
                {
                    error("Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 91:
//#line 214 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 92:
//#line 215 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 93:
//#line 216 "gramatica.y"
                {
                    error("Falta asignacion a la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 94:
//#line 219 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA3(val_peek(0).sval);
                    SA1(val_peek(2).sval);
                    SA1(val_peek(0).sval);
                    SA2(val_peek(1).sval);
                    polaca.pushPos(false);
                    polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 95:
//#line 226 "gramatica.y"
                {
                    error("Error en el identificador de control", analizadorLexico.getNroLinea());
                }
                break;
                case 96:
//#line 227 "gramatica.y"
                {
                    error("Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 97:
//#line 228 "gramatica.y"
                {
                    error("Error en la constante de la asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 98:
//#line 229 "gramatica.y"
                {
                    error("Error en la asignacion de control", analizadorLexico.getNroLinea());
                }
                break;
                case 101:
//#line 237 "gramatica.y"
                {
                    yyval = val_peek(1);
                    polaca.pushPos(true);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                }
                break;
                case 102:
//#line 241 "gramatica.y"
                {
                    yyval = new ParserVal("+");
                }
                break;
                case 103:
//#line 242 "gramatica.y"
                {
                    yyval = new ParserVal("-");
                }
                break;
                case 104:
//#line 245 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String cadena = val_peek(2).sval;
                    System.out.println(cadena);
                    SA1(cadena);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 105:
//#line 251 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 106:
//#line 252 "gramatica.y"
                {
                    error("Falta elemento a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 107:
//#line 253 "gramatica.y"
                {
                    error("Error en la cadena multilínea a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 108:
//#line 254 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 109:
//#line 255 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 110:
//#line 256 "gramatica.y"
                {
                    String factor = val_peek(2).sval;
                    out(factor);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 111:
//#line 261 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String id = val_peek(3).sval;
                    String cte = val_peek(1).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("VALOR", cte);
                        SA1(id);
                        SA2(val_peek(2).sval);
                    }

                }
                break;
                case 112:
//#line 272 "gramatica.y"
                {
                    error("Falta lado izquierdo de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 113:
//#line 273 "gramatica.y"
                {
                    error("Falta literal '=' en sentencia de asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 114:
//#line 274 "gramatica.y"
                {
                    error("Falta lado derecho de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 115:
//#line 275 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 116:
//#line 278 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 117:
//#line 279 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 118:
//#line 280 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 119:
//#line 281 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 120:
//#line 282 "gramatica.y"
                {
                    error("Parametros invalidos", analizadorLexico.getNroLinea());
                }
                break;
                case 121:
//#line 283 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 122:
//#line 284 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 123:
//#line 285 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 124:
//#line 286 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 125:
//#line 289 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 126:
//#line 290 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 127:
//#line 291 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 128:
//#line 292 "gramatica.y"
                {
                    error("Número de parámetros permitidos excedido", analizadorLexico.getNroLinea());
                }
                break;
                case 129:
//#line 293 "gramatica.y"
                {
                    error("Parámetro incorrecto", analizadorLexico.getNroLinea());
                }
                break;
                case 130:
//#line 294 "gramatica.y"
                {
                    error("Faltan literales ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 131:
//#line 295 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 132:
//#line 296 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 133:
//#line 297 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 134:
//#line 301 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">=");
                }
                break;
                case 135:
//#line 302 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<=");
                }
                break;
                case 136:
//#line 303 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">");
                }
                break;
                case 137:
//#line 304 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<");
                }
                break;
                case 138:
//#line 305 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("==");
                }
                break;
                case 139:
//#line 306 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("!=");
                }
                break;
                case 140:
//#line 310 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 141:
//#line 311 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 143:
//#line 313 "gramatica.y"
                {
                    error("Falta el segundo operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 144:
//#line 314 "gramatica.y"
                {
                    error("Falta el segundo operando en la resta", analizadorLexico.getNroLinea());
                }
                break;
                case 145:
//#line 315 "gramatica.y"
                {
                    error("Falta el primer operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 146:
//#line 318 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 147:
//#line 319 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 149:
//#line 321 "gramatica.y"
                {
                    error("Falta el segundo operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 150:
//#line 322 "gramatica.y"
                {
                    error("Falta el segundo operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 151:
//#line 323 "gramatica.y"
                {
                    error("Falta el primer operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 152:
//#line 324 "gramatica.y"
                {
                    error("Falta el primer operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 153:
//#line 327 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 154:
//#line 328 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 155:
//#line 331 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 156:
//#line 338 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
                    }
                }
                break;
//#line 1623 "Parser.java"
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
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
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
                if (yydebug)
                    debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
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
    public void run() {
        yyparse();
    }
//## end of method run() ########################################


//## Constructors ###############################################

    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
    public Parser() {
        //nothing to do
    }


    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe) {
        yydebug = debugMe;
    }
//###############################################################


}
//################### END OF CLASS ##############################
