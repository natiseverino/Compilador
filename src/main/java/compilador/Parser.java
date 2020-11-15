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

import compilador.codigoIntermedio.ElemSimple;
import compilador.codigoIntermedio.OperadorBinario;
import compilador.codigoIntermedio.PolacaInversa;
//#line 22 "Parser.java"


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
            10, 10, 10, 22, 22, 22, 22, 22, 24, 24,
            23, 23, 11, 11, 11, 11, 11, 11, 11, 12,
            12, 12, 12, 12, 13, 13, 13, 13, 13, 13,
            13, 13, 13, 27, 27, 27, 27, 27, 27, 27,
            27, 27, 21, 21, 21, 21, 28, 28, 28, 28,
            28, 28, 26, 26, 26, 26, 26, 26, 29, 29,
            29, 29, 29, 29, 29, 25, 25, 30, 30,
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
            1, 3, 1, 1, 10, 9, 10, 10, 10, 10,
            9, 10, 9, 3, 3, 3, 3, 2, 3, 1,
            1, 1, 5, 5, 4, 5, 5, 4, 5, 4,
            4, 4, 4, 3, 5, 4, 4, 3, 5, 4,
            3, 4, 3, 5, 3, 1, 7, 3, 3, 4,
            4, 2, 3, 2, 3, 3, 1, 1, 1, 1,
            1, 1, 3, 3, 1, 3, 3, 3, 3, 3,
            1, 3, 3, 3, 3, 1, 1, 1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 156, 158, 0, 0, 0, 0,
            151, 0, 0, 157, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 8,
            0, 0, 0, 0, 0, 141, 137, 138, 142, 139,
            140, 0, 0, 0, 0, 0, 83, 7, 0, 0,
            80, 0, 159, 0, 0, 78, 134, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 121,
            0, 0, 0, 0, 0, 118, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 24, 20, 19,
            111, 0, 0, 154, 155, 0, 0, 0, 0, 82,
            77, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 152, 149, 153, 150, 95, 96, 97,
            94, 0, 0, 0, 0, 0, 0, 0, 0, 105,
            0, 112, 129, 0, 128, 0, 58, 0, 0, 0,
            0, 116, 56, 0, 0, 0, 120, 0, 113, 110,
            117, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            22, 6, 84, 0, 75, 0, 0, 0, 65, 0,
            0, 0, 0, 0, 62, 0, 0, 0, 0, 0,
            104, 106, 107, 103, 109, 131, 130, 0, 119, 59,
            57, 0, 0, 0, 0, 0, 115, 0, 0, 0,
            0, 0, 0, 0, 0, 46, 0, 0, 0, 0,
            0, 0, 67, 0, 0, 0, 0, 60, 0, 0,
            0, 101, 102, 0, 0, 0, 0, 0, 0, 0,
            0, 52, 51, 0, 0, 0, 0, 0, 0, 0,
            47, 0, 0, 0, 0, 0, 0, 0, 76, 0,
            66, 0, 71, 73, 0, 64, 63, 0, 0, 0,
            0, 0, 0, 0, 30, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 32, 0, 68, 72, 61, 0, 0, 0, 0,
            0, 0, 0, 127, 29, 0, 34, 0, 36, 0,
            39, 0, 41, 43, 45, 28, 38, 0, 0, 0,
            0, 31, 0, 0, 0, 100, 93, 0, 0, 0,
            0, 91, 86, 54, 33, 35, 37, 40, 42, 44,
            27, 87, 0, 0, 3, 4, 88, 89, 90, 92,
            85, 0, 0, 99, 2, 5,
    };
    final static short yydgoto[] = {9,
            10, 364, 11, 12, 193, 68, 14, 15, 16, 17,
            18, 19, 20, 21, 53, 103, 104, 29, 69, 194,
            30, 38, 254, 347, 31, 32, 46, 65, 33, 34,
    };
    final static short yysindex[] = {625,
            -26, 42, 256, -27, 0, 0, 35, 297, 0, 625,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -173, 222, 713, 647, 0, 0, 231, -254, 545, 25,
            0, 585, 89, 0, -21, -44, 202, 32, -245, 39,
            222, -13, -7, 56, 295, 88, 37, -25, -4, 0,
            80, -11, 30, 162, 92, 0, 0, 0, 0, 0,
            0, 338, 28, 28, 222, 625, 0, 0, -66, 494,
            0, 101, 0, 562, 145, 0, 0, 437, 443, 445,
            447, 450, -115, -107, -202, 166, 468, 111, 468, 139,
            172, -3, 238, 261, 113, -1, -118, -38, -63, 0,
            -54, 86, 321, 176, -17, 0, 690, 142, 320, 18,
            105, 130, 116, 131, 382, 0, 156, 0, 0, 0,
            0, 121, 89, 0, 0, 183, 695, 647, 369, 0,
            0, -47, 647, 371, 590, 647, 394, 121, 89, 121,
            89, 162, 183, 0, 0, 0, 0, 0, 0, 0,
            0, 468, 395, 471, 397, 400, 401, 403, 406, 0,
            411, 0, 0, 197, 0, 23, 0, 424, 218, 226,
            446, 0, 0, 241, 194, 467, 0, 455, 0, 0,
            0, 243, 480, 250, 497, -37, 534, -34, 478, 264,
            0, 0, 0, 277, 0, 647, 488, 283, 0, -26,
            602, 493, 292, 293, 0, 496, -75, 688, 502, -75,
            0, 0, 0, 0, 0, 0, 0, 291, 0, 0,
            0, 298, 519, 116, 275, 290, 0, 522, 287, 523,
            309, 525, 311, 515, -8, 0, 528, -226, 318, 530,
            533, 334, 0, 541, -41, 546, -70, 0, 548, 552,
            -75, 0, 0, 340, -75, -220, 342, 560, 647, 348,
            290, 0, 0, 367, 574, 368, 591, 381, 597, 473,
            0, 388, -194, 389, 601, 36, 647, 391, 0, 605,
            0, 607, 0, 0, 608, 0, 0, 399, 628, 405,
            417, -193, 653, 442, 0, 647, 658, 647, 432, 647,
            433, 647, 438, 647, 647, 647, 647, 647, 439, 440,
            -151, 0, 647, 0, 0, 0, 679, -82, 682, 685,
            687, 420, -82, 0, 0, 482, 0, 647, 0, 647,
            0, 647, 0, 0, 0, 0, 0, 647, 647, 647,
            647, 0, -82, 82, 625, 0, 0, -82, -82, -82,
            659, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -15, 722, 0, 0, 0, 0, 0, 0,
            0, 693, 704, 0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 741,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 613,
            0, 0, 65, 0, 0, 0, 0, 0, 0, 0,
            0, -12, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 33, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 636, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 706, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 45, 0, 0, 0, 0,
            235, 0, 0, -33, 0, 0, 0, 259, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 85, 0, 0, 210, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 107, 132, 155,
            178, 363, 426, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 308, 0,
            0, 0, 0, 0, 0, 52, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -32, 0, 332, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 352,
            0, 385, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -31, -30, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 58, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 409, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -29, 0, 0, 0,
            0, 0, 0, 719, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 235, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            680, 0, 631, -290, -23, 22, 0, 0, 0, 0,
            0, 0, 0, 532, -36, -10, -85, 0, -2, -87,
            29, 730, -187, -189, 31, -20, -43, 736, 41, 0,
    };
    final static int YYTABLESIZE = 996;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{105,
                67, 55, 168, 233, 172, 67, 238, 50, 53, 55,
                49, 48, 40, 22, 112, 118, 85, 73, 176, 22,
                95, 13, 257, 178, 108, 373, 75, 90, 126, 275,
                97, 50, 117, 101, 22, 291, 114, 159, 115, 83,
                345, 177, 164, 100, 126, 198, 126, 203, 204, 276,
                67, 100, 273, 150, 366, 72, 252, 253, 182, 143,
                370, 306, 321, 288, 145, 76, 218, 290, 292, 151,
                94, 135, 28, 376, 43, 44, 110, 307, 322, 93,
                191, 27, 51, 28, 148, 132, 28, 13, 120, 225,
                89, 23, 125, 124, 125, 45, 311, 52, 124, 183,
                185, 188, 123, 132, 340, 145, 146, 145, 242, 145,
                125, 145, 147, 247, 106, 153, 124, 155, 139, 141,
                341, 363, 44, 145, 145, 148, 145, 148, 109, 148,
                81, 143, 352, 353, 78, 82, 79, 165, 116, 262,
                263, 131, 45, 148, 148, 184, 148, 146, 50, 146,
                121, 146, 166, 362, 147, 78, 148, 79, 367, 368,
                369, 371, 63, 236, 149, 146, 146, 64, 146, 154,
                187, 162, 143, 1, 143, 297, 143, 144, 2, 156,
                206, 3, 209, 4, 78, 284, 79, 145, 344, 145,
                143, 143, 169, 143, 285, 147, 128, 147, 129, 147,
                180, 252, 253, 63, 62, 5, 6, 148, 64, 148,
                271, 84, 157, 147, 147, 196, 147, 197, 144, 175,
                144, 171, 144, 282, 152, 78, 83, 79, 39, 146,
                111, 146, 167, 167, 123, 295, 144, 144, 232, 144,
                372, 237, 50, 53, 55, 49, 48, 272, 98, 55,
                136, 113, 158, 312, 143, 42, 143, 96, 114, 52,
                87, 5, 6, 42, 5, 6, 28, 99, 136, 163,
                99, 71, 325, 113, 327, 28, 329, 147, 331, 147,
                333, 334, 335, 336, 337, 119, 5, 6, 23, 342,
                41, 310, 99, 217, 91, 37, 160, 23, 25, 26,
                144, 161, 144, 24, 355, 42, 356, 108, 357, 25,
                26, 92, 25, 26, 358, 359, 360, 361, 261, 105,
                145, 145, 145, 145, 145, 145, 145, 145, 145, 145,
                145, 122, 136, 145, 145, 145, 49, 41, 145, 28,
                148, 148, 148, 148, 148, 148, 148, 148, 148, 148,
                148, 70, 42, 148, 148, 148, 173, 123, 148, 123,
                113, 174, 146, 146, 146, 146, 146, 146, 146, 146,
                146, 146, 146, 5, 6, 146, 146, 146, 181, 99,
                146, 114, 28, 114, 69, 186, 167, 143, 143, 143,
                143, 143, 143, 143, 143, 143, 143, 143, 5, 6,
                143, 143, 143, 135, 99, 143, 189, 136, 74, 137,
                147, 147, 147, 147, 147, 147, 147, 147, 147, 147,
                147, 135, 190, 147, 147, 147, 52, 195, 147, 199,
                108, 113, 108, 144, 144, 144, 144, 144, 144, 144,
                144, 144, 144, 144, 5, 6, 144, 144, 144, 224,
                99, 144, 205, 207, 122, 210, 122, 86, 211, 212,
                351, 213, 5, 6, 214, 136, 133, 216, 99, 215,
                136, 136, 36, 136, 70, 136, 70, 54, 136, 136,
                136, 28, 219, 136, 133, 135, 70, 28, 220, 28,
                123, 28, 25, 26, 28, 123, 221, 123, 123, 123,
                123, 25, 26, 123, 123, 123, 222, 69, 123, 69,
                226, 35, 28, 227, 114, 28, 223, 28, 228, 114,
                229, 114, 114, 114, 114, 230, 36, 114, 114, 114,
                113, 74, 114, 74, 130, 63, 62, 231, 239, 240,
                64, 241, 345, 5, 6, 113, 243, 244, 133, 99,
                107, 248, 47, 61, 251, 60, 249, 250, 5, 6,
                256, 258, 265, 108, 99, 25, 26, 48, 108, 259,
                108, 108, 108, 108, 102, 270, 108, 108, 108, 260,
                102, 108, 264, 266, 267, 268, 269, 122, 274, 277,
                278, 279, 122, 122, 122, 122, 122, 122, 280, 281,
                122, 122, 122, 294, 283, 122, 286, 70, 25, 26,
                287, 289, 70, 293, 70, 70, 70, 70, 135, 296,
                70, 70, 70, 135, 135, 70, 135, 78, 135, 79,
                170, 135, 135, 135, 299, 102, 135, 66, 298, 300,
                69, 102, 102, 102, 61, 69, 60, 69, 69, 69,
                69, 301, 302, 69, 69, 69, 66, 303, 69, 305,
                308, 309, 313, 314, 74, 315, 316, 66, 318, 74,
                317, 74, 74, 74, 74, 1, 319, 74, 74, 74,
                2, 133, 74, 3, 66, 4, 133, 133, 320, 133,
                344, 133, 138, 323, 133, 133, 133, 324, 140, 133,
                142, 326, 144, 328, 330, 146, 102, 25, 26, 332,
                338, 339, 66, 25, 26, 25, 26, 25, 26, 343,
                25, 26, 348, 23, 66, 349, 208, 350, 54, 63,
                62, 63, 62, 168, 64, 81, 64, 354, 25, 26,
                1, 25, 26, 25, 304, 127, 255, 61, 179, 60,
                56, 57, 58, 59, 63, 62, 102, 102, 79, 64,
                158, 158, 172, 158, 98, 158, 88, 80, 0, 66,
                1, 0, 61, 0, 60, 2, 0, 158, 3, 0,
                4, 345, 0, 5, 6, 7, 0, 0, 8, 234,
                0, 0, 102, 0, 2, 0, 0, 3, 0, 4,
                1, 0, 5, 6, 7, 2, 74, 8, 3, 235,
                4, 0, 0, 5, 6, 7, 0, 132, 8, 192,
                0, 0, 2, 0, 133, 3, 134, 4, 0, 0,
                5, 6, 7, 0, 0, 8, 0, 0, 0, 0,
                77, 56, 57, 58, 59, 200, 374, 0, 0, 0,
                2, 0, 201, 3, 202, 4, 0, 245, 5, 6,
                7, 0, 2, 8, 0, 3, 246, 4, 81, 0,
                5, 6, 7, 81, 81, 8, 81, 0, 81, 0,
                1, 81, 81, 81, 0, 2, 81, 0, 3, 0,
                4, 79, 0, 5, 6, 7, 79, 79, 8, 79,
                0, 79, 1, 0, 79, 79, 79, 2, 0, 79,
                3, 0, 4, 0, 1, 5, 6, 7, 0, 2,
                8, 0, 3, 0, 4, 0, 0, 5, 6, 7,
                0, 0, 8, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 56, 57, 58, 59, 346, 0,
                1, 0, 346, 346, 0, 2, 0, 0, 3, 0,
                4, 0, 0, 5, 6, 7, 0, 0, 8, 56,
                57, 58, 59, 346, 0, 365, 0, 1, 346, 346,
                346, 346, 2, 0, 0, 3, 0, 4, 0, 0,
                5, 6, 7, 0, 375, 8,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{43,
                24, 22, 41, 41, 59, 29, 41, 41, 41, 41,
                41, 41, 40, 61, 40, 52, 61, 272, 104, 61,
                41, 0, 210, 41, 45, 41, 29, 273, 41, 256,
                44, 10, 44, 41, 61, 256, 41, 41, 49, 61,
                123, 59, 44, 59, 65, 133, 59, 135, 136, 276,
                74, 59, 61, 256, 345, 27, 277, 278, 41, 80,
                351, 256, 256, 251, 0, 41, 44, 255, 256, 272,
                40, 74, 45, 364, 40, 41, 40, 272, 272, 41,
                117, 40, 256, 45, 0, 41, 45, 66, 59, 175,
                59, 59, 41, 63, 64, 61, 61, 271, 41, 110,
                111, 112, 62, 59, 256, 41, 0, 43, 196, 45,
                59, 81, 82, 201, 59, 87, 59, 89, 78, 79,
                272, 40, 41, 59, 60, 41, 62, 43, 41, 45,
                42, 0, 322, 323, 43, 47, 45, 256, 59, 225,
                226, 41, 61, 59, 60, 41, 62, 41, 127, 43,
                59, 45, 271, 343, 0, 43, 272, 45, 348, 349,
                350, 351, 42, 187, 272, 59, 60, 47, 62, 59,
                41, 59, 41, 256, 43, 261, 45, 0, 261, 41,
                152, 264, 154, 266, 43, 256, 45, 123, 271, 125,
                59, 60, 256, 62, 265, 41, 263, 43, 265, 45,
                59, 277, 278, 42, 43, 269, 270, 123, 47, 125,
                234, 256, 41, 59, 60, 263, 62, 265, 41, 44,
                43, 276, 45, 265, 59, 43, 61, 45, 256, 123,
                256, 125, 271, 271, 0, 259, 59, 60, 276, 62,
                256, 276, 276, 276, 276, 276, 276, 256, 256, 270,
                41, 256, 256, 277, 123, 271, 125, 271, 0, 271,
                59, 269, 270, 271, 269, 270, 45, 275, 59, 271,
                275, 41, 296, 256, 298, 45, 300, 123, 302, 125,
                304, 305, 306, 307, 308, 256, 269, 270, 256, 313,
                256, 256, 275, 271, 256, 40, 59, 256, 271, 272,
                123, 41, 125, 262, 328, 271, 330, 0, 332, 271,
                272, 273, 271, 272, 338, 339, 340, 341, 44, 363,
                256, 257, 258, 259, 260, 261, 262, 263, 264, 265,
                266, 0, 123, 269, 270, 271, 40, 256, 274, 45,
                256, 257, 258, 259, 260, 261, 262, 263, 264, 265,
                266, 0, 271, 269, 270, 271, 271, 123, 274, 125,
                256, 41, 256, 257, 258, 259, 260, 261, 262, 263,
                264, 265, 266, 269, 270, 269, 270, 271, 59, 275,
                274, 123, 45, 125, 0, 256, 271, 256, 257, 258,
                259, 260, 261, 262, 263, 264, 265, 266, 269, 270,
                269, 270, 271, 41, 275, 274, 276, 263, 0, 265,
                256, 257, 258, 259, 260, 261, 262, 263, 264, 265,
                266, 59, 41, 269, 270, 271, 271, 59, 274, 59,
                123, 256, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 269, 270, 269, 270, 271, 256,
                275, 274, 59, 59, 123, 59, 125, 256, 59, 59,
                41, 59, 269, 270, 59, 256, 41, 271, 275, 59,
                261, 262, 271, 264, 123, 266, 125, 256, 269, 270,
                271, 45, 59, 274, 59, 123, 256, 45, 271, 45,
                256, 45, 271, 272, 45, 261, 271, 263, 264, 265,
                266, 271, 272, 269, 270, 271, 61, 123, 274, 125,
                44, 256, 45, 59, 256, 45, 276, 45, 276, 261,
                41, 263, 264, 265, 266, 276, 271, 269, 270, 271,
                256, 123, 274, 125, 41, 42, 43, 41, 61, 276,
                47, 265, 123, 269, 270, 256, 59, 265, 123, 275,
                256, 59, 256, 60, 59, 62, 265, 265, 269, 270,
                59, 271, 276, 256, 275, 271, 272, 271, 261, 272,
                263, 264, 265, 266, 43, 61, 269, 270, 271, 61,
                49, 274, 61, 61, 276, 61, 276, 256, 61, 272,
                61, 59, 261, 256, 263, 264, 265, 266, 265, 59,
                269, 270, 271, 44, 59, 274, 59, 256, 271, 272,
                59, 272, 261, 272, 263, 264, 265, 266, 256, 272,
                269, 270, 271, 261, 262, 274, 264, 43, 266, 45,
                99, 269, 270, 271, 61, 104, 274, 123, 272, 272,
                256, 110, 111, 112, 60, 261, 62, 263, 264, 265,
                266, 61, 272, 269, 270, 271, 123, 61, 274, 272,
                272, 61, 272, 59, 256, 59, 59, 123, 41, 261,
                272, 263, 264, 265, 266, 256, 272, 269, 270, 271,
                261, 256, 274, 264, 123, 266, 261, 262, 272, 264,
                271, 266, 256, 41, 269, 270, 271, 256, 256, 274,
                256, 44, 256, 272, 272, 256, 175, 271, 272, 272,
                272, 272, 123, 271, 272, 271, 272, 271, 272, 41,
                271, 272, 41, 256, 123, 41, 256, 41, 256, 42,
                43, 42, 43, 41, 47, 123, 47, 256, 271, 272,
                0, 271, 272, 271, 272, 66, 59, 60, 59, 62,
                257, 258, 259, 260, 42, 43, 225, 226, 123, 47,
                42, 43, 59, 45, 59, 47, 37, 32, -1, 123,
                256, -1, 60, -1, 62, 261, -1, 59, 264, -1,
                266, 123, -1, 269, 270, 271, -1, -1, 274, 256,
                -1, -1, 261, -1, 261, -1, -1, 264, -1, 266,
                256, -1, 269, 270, 271, 261, 262, 274, 264, 276,
                266, -1, -1, 269, 270, 271, -1, 256, 274, 125,
                -1, -1, 261, -1, 263, 264, 265, 266, -1, -1,
                269, 270, 271, -1, -1, 274, -1, -1, -1, -1,
                256, 257, 258, 259, 260, 256, 125, -1, -1, -1,
                261, -1, 263, 264, 265, 266, -1, 256, 269, 270,
                271, -1, 261, 274, -1, 264, 265, 266, 256, -1,
                269, 270, 271, 261, 262, 274, 264, -1, 266, -1,
                256, 269, 270, 271, -1, 261, 274, -1, 264, -1,
                266, 256, -1, 269, 270, 271, 261, 262, 274, 264,
                -1, 266, 256, -1, 269, 270, 271, 261, -1, 274,
                264, -1, 266, -1, 256, 269, 270, 271, -1, 261,
                274, -1, 264, -1, 266, -1, -1, 269, 270, 271,
                -1, -1, 274, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, 257, 258, 259, 260, 318, -1,
                256, -1, 322, 323, -1, 261, -1, -1, 264, -1,
                266, -1, -1, 269, 270, 271, -1, -1, 274, 257,
                258, 259, 260, 343, -1, 345, -1, 256, 348, 349,
                350, 351, 261, -1, -1, 264, -1, 266, -1, -1,
                269, 270, 271, -1, 364, 274,
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
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR inicio_for ';' condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' error ';' condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' error CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr error ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr CTE bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr CTE ')' sentencia_declarativa",
            "sentencia_control : FOR '(' ';' condicion ';' incr_decr CTE ')' bloque_for",
            "inicio_for : ID '=' CTE",
            "inicio_for : error '=' CTE",
            "inicio_for : ID error CTE",
            "inicio_for : ID '=' error",
            "inicio_for : ID error",
            "bloque_for : '{' bloque_ejecutable '}'",
            "bloque_for : sentencia_ejecutable",
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

//#line 335 "gramatica.y"

    private AnalizadorLexico analizadorLexico;
    private int nroUltimaLinea;
    private PolacaInversa polaca;
    private boolean verbose;

    public Parser(AnalizadorLexico analizadorLexico, boolean debug, PolacaInversa polaca, boolean verbose) {
        this.analizadorLexico = analizadorLexico;
        this.yydebug = debug;
        this.polaca = polaca;
        this.verbose = verbose;
    }

    private void yyerror(String mensaje) {
        //System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
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
        if (verbose)
            System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | " + mensaje + " %n" + Main.ANSI_RESET, linea);

    }

    public void warning(String mensaje, int linea) {
        if (verbose)
            System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: " + mensaje + "%n" + Main.ANSI_RESET, linea);
    }

    public void out(String lex) {
        Token token = TablaSimbolos.getToken(lex);
        if (token != null) {
            if (token.getAtributo("USO") != null) {
                if (token.getTipoToken().equals("IDENTIFICADOR") && token.getAtributo("USO").equals("VARIABLE")) {
                    Object valor = token.getAtributo("VALOR");
                    if (valor != null)
                        System.out.println((String) token.getAtributo("VALOR"));
                    else
                        error("Variable + lex + no inicializada", analizadorLexico.getNroLinea());
                } else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                    System.out.println(token.getLexema());
            } else
                error("Variable + lex + no declarada", analizadorLexico.getNroLinea());
        }
    }

    public void SA1(String lex) {  //añadir factor a la polaca
        Token token = TablaSimbolos.getToken(lex);
        ElemSimple elem = new ElemSimple(token);
        polaca.addElem(elem, false);
    }

    public void SA2(String operador) { //añadir operador binario a la polaca
        OperadorBinario elem = new OperadorBinario(operador);
        polaca.addElem(elem, false);
    }

    //#line 820 "Parser.java"
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
//#line 37 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 5:
//#line 38 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 19:
//#line 65 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Declaración de variables %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 20:
//#line 66 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 21:
//#line 67 "gramatica.y"
                {
                    error("Falta lista de variables", analizadorLexico.getNroLinea());
                }
                break;
                case 23:
//#line 71 "gramatica.y"
                {
                    String id = val_peek(0).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("USO", "VARIABLE");
                    }
                }
                break;
                case 24:
//#line 76 "gramatica.y"
                {
                    error("Falta literal ',' ", analizadorLexico.getNroLinea());
                }
                break;
                case 27:
//#line 83 "gramatica.y"
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
//#line 95 "gramatica.y"
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
//#line 107 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 30:
//#line 108 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 31:
//#line 109 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 32:
//#line 110 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 33:
//#line 111 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 34:
//#line 112 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 35:
//#line 113 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 36:
//#line 114 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 37:
//#line 115 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 38:
//#line 116 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 39:
//#line 117 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 40:
//#line 118 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 41:
//#line 119 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 42:
//#line 120 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 43:
//#line 121 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 44:
//#line 122 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 45:
//#line 123 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 46:
//#line 124 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 47:
//#line 125 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 48:
//#line 128 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 49:
//#line 129 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 50:
//#line 130 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 51:
//#line 131 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 52:
//#line 132 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 53:
//#line 133 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 54:
//#line 134 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 55:
//#line 135 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 56:
//#line 138 "gramatica.y"
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
//#line 146 "gramatica.y"
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
//#line 154 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 59:
//#line 155 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 60:
//#line 158 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 61:
//#line 159 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 62:
//#line 160 "gramatica.y"
                {
                    error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());
                }
                break;
                case 63:
//#line 161 "gramatica.y"
                {
                    error("Falta palabra reservada THEN ", analizadorLexico.getNroLinea());
                }
                break;
                case 64:
//#line 162 "gramatica.y"
                {
                    error("Falta palabra reservada ELSE ", analizadorLexico.getNroLinea());
                }
                break;
                case 65:
//#line 163 "gramatica.y"
                {
                    error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 66:
//#line 164 "gramatica.y"
                {
                    error("Falta bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 67:
//#line 165 "gramatica.y"
                {
                    error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 68:
//#line 166 "gramatica.y"
                {
                    error("Error en bloque de sentencias THEN", analizadorLexico.getNroLinea());
                }
                break;
                case 69:
//#line 167 "gramatica.y"
                {
                    error(" Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 70:
//#line 168 "gramatica.y"
                {
                    error(" Falta palabra reservada END_IF y literal ';'", nroUltimaLinea);
                }
                break;
                case 71:
//#line 169 "gramatica.y"
                {
                    error("Falta bloque de sentencias ELSE", analizadorLexico.getNroLinea());
                }
                break;
                case 72:
//#line 170 "gramatica.y"
                {
                    error("Error en bloque de sentencias ELSE", analizadorLexico.getNroLinea());
                }
                break;
                case 73:
//#line 171 "gramatica.y"
                {
                    error("Falta palabra reservada END_IF", analizadorLexico.getNroLinea());
                }
                break;
                case 74:
//#line 172 "gramatica.y"
                {
                    error(" Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 75:
//#line 173 "gramatica.y"
                {
                    error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);
                }
                break;
                case 76:
//#line 174 "gramatica.y"
                {
                    error(" Falta la condicion de la sentencia IF ", nroUltimaLinea);
                }
                break;
                case 78:
//#line 178 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 79:
//#line 179 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 80:
//#line 180 "gramatica.y"
                {
                    error("Falta condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 81:
//#line 181 "gramatica.y"
                {
                    error("Faltan parentesis", analizadorLexico.getNroLinea());
                }
                break;
                case 82:
//#line 182 "gramatica.y"
                {
                    error("Error en la condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 85:
//#line 192 "gramatica.y"
                {
                    String cte = val_peek(2).sval;
                    if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    else
                        error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
                }
                break;
                case 86:
//#line 199 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 87:
//#line 200 "gramatica.y"
                {
                    error("Error en el inicio de la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 88:
//#line 201 "gramatica.y"
                {
                    error("Falta condición de control en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 89:
//#line 202 "gramatica.y"
                {
                    error("Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 90:
//#line 203 "gramatica.y"
                {
                    error("Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 91:
//#line 204 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 92:
//#line 205 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 93:
//#line 206 "gramatica.y"
                {
                    error("Falta asignacion a la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 94:
//#line 209 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
                }
                break;
                case 95:
//#line 214 "gramatica.y"
                {
                    error("Error en el identificador de control", analizadorLexico.getNroLinea());
                }
                break;
                case 96:
//#line 215 "gramatica.y"
                {
                    error("Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 97:
//#line 216 "gramatica.y"
                {
                    error("Error en la constante de la asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 98:
//#line 217 "gramatica.y"
                {
                    error("Error en la asignacion de control", analizadorLexico.getNroLinea());
                }
                break;
                case 103:
//#line 231 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String cadena = val_peek(2).sval;
                    System.out.printf(cadena);
                }
                break;
                case 104:
//#line 235 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 105:
//#line 236 "gramatica.y"
                {
                    error("Falta elemento a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 106:
//#line 237 "gramatica.y"
                {
                    error("Error en la cadena multilínea a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 107:
//#line 238 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 108:
//#line 239 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 109:
//#line 240 "gramatica.y"
                {
                    out(val_peek(2).sval);
                }
                break;
                case 110:
//#line 243 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String id = val_peek(3).sval;
                    String cte = val_peek(1).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("VALOR", cte);
                        SA1(val_peek(3).sval);
                        SA2(val_peek(2).sval);
                    }

                }
                break;
                case 111:
//#line 254 "gramatica.y"
                {
                    error("Falta lado izquierdo de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 112:
//#line 255 "gramatica.y"
                {
                    error("Falta literal '=' en sentencia de asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 113:
//#line 256 "gramatica.y"
                {
                    error("Falta lado derecho de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 114:
//#line 257 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 115:
//#line 260 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 116:
//#line 261 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 117:
//#line 262 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 118:
//#line 263 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 119:
//#line 264 "gramatica.y"
                {
                    error("Parametros invalidos", analizadorLexico.getNroLinea());
                }
                break;
                case 120:
//#line 265 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 121:
//#line 266 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 122:
//#line 267 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 123:
//#line 268 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 124:
//#line 271 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 125:
//#line 272 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 126:
//#line 273 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 127:
//#line 274 "gramatica.y"
                {
                    error("Número de parámetros permitidos excedido", analizadorLexico.getNroLinea());
                }
                break;
                case 128:
//#line 275 "gramatica.y"
                {
                    error("Parámetro incorrecto", analizadorLexico.getNroLinea());
                }
                break;
                case 129:
//#line 276 "gramatica.y"
                {
                    error("Faltan literales ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 130:
//#line 277 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 131:
//#line 278 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 132:
//#line 279 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 133:
//#line 283 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Comparación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 134:
//#line 284 "gramatica.y"
                {
                    error("Falta comparador", analizadorLexico.getNroLinea());
                }
                break;
                case 135:
//#line 285 "gramatica.y"
                {
                    error("Falta el segundo operando de la condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 136:
//#line 286 "gramatica.y"
                {
                    error("Falta el primer operando de la condicion", analizadorLexico.getNroLinea());
                }
                break;
                case 139:
//#line 291 "gramatica.y"
                {
                    yyval = new ParserVal('>');
                }
                break;
                case 140:
//#line 292 "gramatica.y"
                {
                    yyval = new ParserVal('<');
                }
                break;
                case 143:
//#line 297 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 144:
//#line 298 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 146:
//#line 300 "gramatica.y"
                {
                    error("Falta el segundo operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 147:
//#line 301 "gramatica.y"
                {
                    error("Falta el segundo operando en la resta", analizadorLexico.getNroLinea());
                }
                break;
                case 148:
//#line 302 "gramatica.y"
                {
                    error("Falta el primer operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 149:
//#line 305 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 150:
//#line 306 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 152:
//#line 308 "gramatica.y"
                {
                    error("Falta el segundo operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 153:
//#line 309 "gramatica.y"
                {
                    error("Falta el segundo operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 154:
//#line 310 "gramatica.y"
                {
                    error("Falta el primer operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 155:
//#line 311 "gramatica.y"
                {
                    error("Falta el primer operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 156:
//#line 314 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 157:
//#line 315 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 158:
//#line 318 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 159:
//#line 325 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
                    }
                }
                break;
//#line 1552 "Parser.java"
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
