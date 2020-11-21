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
import java.util.Map;
//#line 24 "Parser.java"


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
            7, 15, 15, 15, 14, 14, 8, 16, 16, 17,
            17, 17, 17, 17, 17, 17, 18, 18, 18, 18,
            19, 19, 19, 20, 20, 20, 20, 20, 20, 20,
            20, 21, 21, 21, 21, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 22, 22, 22, 22, 22, 22, 23, 24,
            10, 10, 10, 10, 10, 10, 10, 10, 10, 26,
            26, 26, 26, 26, 29, 29, 27, 28, 28, 11,
            11, 11, 11, 11, 11, 12, 12, 12, 12, 12,
            13, 13, 13, 13, 13, 13, 13, 13, 13, 31,
            31, 31, 31, 31, 31, 31, 31, 31, 25, 25,
            25, 25, 25, 25, 30, 30, 30, 30, 30, 30,
            32, 32, 32, 32, 32, 32, 32, 33, 33, 34,
            34,
    };
    final static short yylen[] = {2,
            1, 2, 1, 1, 2, 3, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 3,
            3, 3, 1, 2, 1, 1, 4, 2, 2, 3,
            2, 3, 2, 3, 2, 2, 3, 2, 3, 3,
            3, 3, 3, 5, 3, 1, 4, 4, 2, 7,
            3, 2, 3, 2, 3, 6, 8, 5, 7, 7,
            5, 7, 6, 8, 5, 5, 7, 8, 7, 7,
            5, 7, 3, 2, 2, 2, 1, 3, 1, 1,
            9, 8, 9, 10, 9, 9, 8, 9, 8, 3,
            3, 3, 3, 2, 3, 1, 2, 1, 1, 5,
            5, 4, 5, 5, 4, 4, 4, 4, 4, 3,
            5, 4, 4, 3, 5, 4, 3, 4, 3, 5,
            3, 1, 7, 3, 3, 4, 4, 2, 3, 3,
            3, 3, 3, 3, 3, 3, 1, 3, 3, 3,
            3, 3, 1, 3, 3, 3, 3, 1, 1, 1,
            2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 0, 148, 150, 0, 0, 0,
            0, 0, 0, 143, 149, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 29, 28, 8,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 79, 7, 0, 0, 76, 0, 151, 0, 0,
            74, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 117, 0, 0, 114,
            0, 0, 0, 21, 0, 24, 20, 19, 0, 0,
            33, 0, 0, 0, 0, 31, 0, 0, 0, 0,
            107, 0, 0, 146, 147, 0, 0, 0, 78, 73,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 144, 141, 145, 142,
            91, 92, 93, 90, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 102, 108, 125, 0, 124, 0, 0,
            112, 116, 0, 109, 106, 113, 22, 54, 0, 0,
            52, 32, 0, 0, 34, 30, 0, 0, 38, 0,
            0, 27, 6, 80, 0, 71, 0, 0, 0, 61,
            0, 0, 0, 0, 0, 58, 0, 97, 98, 99,
            0, 0, 0, 0, 101, 103, 104, 100, 127, 126,
            0, 115, 111, 55, 53, 0, 0, 0, 39, 40,
            37, 0, 0, 0, 0, 0, 63, 0, 0, 0,
            0, 56, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 48, 47, 42, 43, 41, 72, 0, 62,
            0, 67, 69, 0, 60, 59, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 64, 68, 57, 0, 0,
            96, 89, 0, 0, 0, 0, 87, 82, 123, 0,
            83, 0, 3, 4, 0, 85, 86, 88, 81, 50,
            95, 2, 5, 84,
    };
    final static short yydgoto[] = {9,
            10, 292, 11, 12, 194, 63, 14, 15, 16, 17,
            18, 19, 20, 21, 53, 22, 56, 120, 192, 113,
            114, 30, 64, 195, 156, 39, 157, 211, 282, 32,
            47, 33, 34, 35,
    };
    final static short yysindex[] = {531,
            -26, 147, 52, -33, 0, 0, -27, -93, 0, 531,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -91, -32, 152, 163, 519, 0, 0, 154, -227, 476,
            26, 455, 167, 0, 0, 29, -52, -56, 40, -131,
            -31, 152, -15, 64, 43, 157, 104, 0, 0, 0,
            93, 5, -28, -22, -1, -50, 14, 205, 50, 50,
            531, 0, 0, -44, 657, 0, 118, 0, 423, 181,
            0, 152, 152, 152, 152, 152, 152, 269, 335, 394,
            411, -110, -96, -198, 392, 152, 126, 152, 191, 268,
            -29, 129, 130, 45, -70, 272, 0, 316, 6, 0,
            523, 153, 340, 0, 136, 0, 0, 0, 140, -53,
            0, 149, 380, 249, -25, 0, 389, 32, 175, -95,
            0, 313, 167, 0, 0, 566, 519, 384, 0, 0,
            -43, 519, 393, 460, 519, 403, 428, 428, 428, 428,
            428, 428, 313, 167, 313, 167, 0, 0, 0, 0,
            0, 0, 0, 0, 152, 408, 139, 424, 139, 413,
            416, 419, 421, 0, 0, 0, 214, 0, 47, 431,
            0, 0, 432, 0, 0, 0, 0, 0, 222, 224,
            0, 0, -115, 452, 0, 0, 234, -162, 0, 531,
            664, 0, 0, 0, 246, 0, 519, 461, 256, 0,
            -26, 331, 464, 261, 266, 0, 139, 0, 0, 0,
            260, 526, -171, 263, 0, 0, 0, 0, 0, 0,
            278, 0, 0, 0, 0, 140, 292, 258, 0, 0,
            0, 577, -38, 593, 492, 291, 0, 498, 75, 505,
            -186, 0, 513, 515, 303, 536, 139, 306, -140, 543,
            544, 258, 0, 0, 0, 0, 0, 0, 521, 0,
            530, 0, 0, 545, 0, 0, 549, -87, 326, 571,
            573, 315, -87, 359, 576, 0, 0, 0, -87, 531,
            0, 0, 581, -87, -87, 547, 0, 0, 0, 367,
            0, 610, 0, 0, -87, 0, 0, 0, 0, 0,
            0, 0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 626,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            487, 0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 10, 0, 0, 0, 0, 0, 0, 0,
            0, -20, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 508, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 569, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 78, 0, 0, 0, 120, 0, 0,
            0, 171, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -41, -46, 0, -24, 0, 0, 0,
            0, 0, 21, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 131, 347, 363, 374,
            390, 407, 41, 68, 88, 108, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 194, 0, 0, 0, 0, 0, 80, 0,
            0, 0, 218, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -39, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            238, 0, 273, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -37, -36, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            99, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 289, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -35, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            -8, 0, 629, 230, 8, 17, 0, 0, 0, 0,
            0, 0, 0, 539, 20, 0, 0, 0, 0, 584,
            -71, 0, 2, -80, 28, 603, -40, -109, 632, 819,
            611, 56, 16, 0,
    };
    final static int YYTABLESIZE = 938;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{46,
                137, 49, 86, 51, 45, 44, 41, 55, 84, 92,
                119, 163, 44, 45, 36, 185, 13, 23, 111, 46,
                140, 49, 23, 51, 45, 44, 50, 191, 95, 31,
                108, 70, 62, 46, 23, 280, 35, 62, 23, 116,
                138, 137, 184, 137, 68, 137, 173, 159, 105, 214,
                122, 199, 126, 204, 205, 67, 78, 153, 79, 137,
                137, 140, 137, 140, 172, 140, 71, 135, 122, 263,
                134, 106, 121, 154, 124, 125, 62, 13, 264, 140,
                140, 138, 140, 138, 248, 138, 256, 139, 167, 82,
                221, 38, 188, 230, 29, 148, 150, 245, 88, 138,
                138, 100, 138, 249, 98, 209, 210, 136, 135, 231,
                135, 227, 135, 123, 207, 271, 236, 213, 128, 119,
                121, 241, 97, 137, 177, 137, 135, 135, 139, 135,
                139, 272, 139, 144, 146, 23, 128, 269, 121, 120,
                226, 89, 50, 140, 103, 140, 139, 139, 136, 139,
                136, 104, 136, 5, 6, 253, 254, 120, 130, 110,
                190, 151, 48, 138, 51, 138, 136, 136, 1, 136,
                110, 133, 78, 2, 79, 152, 3, 49, 4, 52,
                275, 232, 234, 7, 158, 168, 28, 164, 165, 133,
                135, 29, 135, 105, 66, 78, 29, 79, 29, 85,
                169, 29, 179, 83, 59, 58, 13, 13, 80, 60,
                139, 175, 139, 81, 37, 5, 6, 118, 127, 197,
                128, 198, 40, 54, 90, 118, 162, 107, 42, 36,
                136, 160, 136, 109, 46, 23, 49, 66, 51, 45,
                44, 91, 119, 43, 119, 178, 5, 6, 50, 29,
                50, 35, 110, 133, 115, 94, 137, 137, 137, 137,
                137, 137, 137, 137, 137, 137, 137, 5, 6, 137,
                137, 137, 65, 110, 137, 52, 140, 140, 140, 140,
                140, 140, 140, 140, 140, 140, 140, 187, 70, 140,
                140, 140, 183, 110, 140, 110, 138, 138, 138, 138,
                138, 138, 138, 138, 138, 138, 138, 36, 161, 138,
                138, 138, 170, 29, 138, 166, 105, 220, 105, 96,
                26, 27, 37, 135, 135, 135, 135, 135, 135, 135,
                135, 135, 135, 135, 43, 252, 135, 135, 135, 261,
                118, 135, 118, 139, 139, 139, 139, 139, 139, 139,
                139, 139, 139, 139, 59, 286, 139, 139, 139, 60,
                66, 139, 66, 136, 136, 136, 136, 136, 136, 136,
                136, 136, 136, 136, 171, 119, 136, 136, 136, 29,
                119, 136, 119, 119, 119, 119, 133, 129, 119, 119,
                119, 133, 133, 119, 133, 65, 133, 65, 176, 133,
                133, 133, 24, 130, 133, 129, 52, 24, 25, 65,
                178, 70, 101, 70, 134, 209, 210, 26, 27, 181,
                182, 130, 26, 27, 26, 27, 110, 26, 27, 186,
                131, 110, 134, 110, 110, 110, 110, 280, 29, 110,
                110, 110, 196, 135, 110, 136, 189, 132, 131, 105,
                155, 200, 82, 61, 105, 29, 105, 105, 105, 105,
                122, 206, 105, 105, 105, 132, 208, 105, 29, 129,
                78, 215, 79, 118, 216, 26, 27, 217, 118, 218,
                118, 118, 118, 118, 219, 130, 118, 118, 118, 222,
                223, 118, 224, 66, 225, 228, 134, 78, 66, 79,
                66, 66, 66, 66, 109, 229, 66, 66, 66, 294,
                235, 66, 131, 109, 77, 298, 76, 5, 6, 237,
                238, 303, 242, 110, 143, 243, 5, 6, 65, 132,
                244, 246, 110, 65, 250, 65, 65, 65, 65, 26,
                27, 65, 65, 65, 70, 61, 65, 109, 251, 70,
                258, 70, 70, 70, 70, 259, 260, 70, 70, 70,
                5, 6, 70, 262, 59, 58, 110, 59, 58, 60,
                1, 265, 60, 266, 267, 2, 268, 270, 3, 276,
                4, 174, 61, 273, 247, 7, 239, 274, 277, 279,
                145, 2, 112, 112, 3, 240, 4, 283, 61, 5,
                6, 7, 129, 278, 8, 26, 27, 129, 129, 77,
                129, 284, 129, 285, 289, 129, 129, 129, 130, 290,
                129, 295, 300, 130, 130, 1, 130, 94, 130, 134,
                75, 130, 130, 130, 134, 134, 130, 134, 117, 134,
                87, 61, 134, 134, 134, 131, 0, 134, 180, 147,
                131, 131, 112, 131, 99, 131, 0, 0, 131, 131,
                131, 0, 132, 131, 26, 27, 149, 132, 132, 280,
                132, 0, 132, 0, 0, 132, 132, 132, 131, 212,
                132, 26, 27, 2, 0, 132, 3, 133, 4, 0,
                193, 5, 6, 7, 26, 27, 8, 129, 59, 58,
                0, 255, 0, 60, 0, 0, 0, 0, 0, 0,
                0, 72, 73, 74, 75, 201, 0, 257, 0, 0,
                2, 112, 202, 3, 203, 4, 0, 0, 5, 6,
                7, 1, 0, 8, 301, 0, 2, 69, 0, 3,
                0, 4, 77, 0, 5, 6, 7, 77, 77, 8,
                77, 0, 77, 0, 0, 77, 77, 77, 0, 0,
                77, 0, 0, 75, 0, 112, 112, 0, 75, 75,
                0, 75, 0, 75, 1, 0, 75, 75, 75, 2,
                0, 75, 3, 0, 4, 0, 1, 5, 6, 7,
                112, 2, 8, 0, 3, 0, 4, 0, 0, 5,
                6, 7, 1, 0, 8, 0, 0, 2, 0, 0,
                3, 0, 4, 0, 0, 5, 6, 7, 0, 0,
                8, 1, 0, 0, 0, 0, 2, 0, 0, 3,
                0, 4, 1, 0, 5, 6, 7, 2, 0, 8,
                3, 57, 4, 0, 0, 5, 6, 7, 1, 0,
                8, 0, 0, 2, 0, 0, 3, 0, 4, 0,
                93, 5, 6, 7, 102, 1, 8, 0, 0, 0,
                2, 0, 0, 3, 0, 4, 0, 0, 5, 6,
                7, 0, 0, 8, 0, 0, 0, 0, 0, 0,
                137, 138, 139, 140, 141, 142, 281, 0, 0, 0,
                281, 281, 0, 287, 288, 0, 0, 281, 293, 0,
                291, 0, 281, 281, 281, 296, 297, 299, 0, 233,
                302, 0, 0, 281, 2, 0, 304, 3, 0, 4,
                0, 0, 5, 6, 7, 0, 0, 8,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{41,
                0, 41, 59, 41, 41, 41, 40, 40, 61, 41,
                61, 41, 40, 41, 61, 41, 0, 61, 41, 61,
                0, 61, 61, 61, 61, 61, 10, 123, 44, 2,
                59, 30, 25, 61, 61, 123, 61, 30, 59, 41,
                0, 41, 114, 43, 272, 45, 41, 88, 44, 159,
                41, 132, 61, 134, 135, 28, 43, 256, 45, 59,
                60, 41, 62, 43, 59, 45, 41, 0, 59, 256,
                69, 52, 59, 272, 59, 60, 69, 61, 265, 59,
                60, 41, 62, 43, 256, 45, 125, 0, 44, 61,
                44, 40, 61, 256, 45, 80, 81, 207, 59, 59,
                60, 59, 62, 213, 41, 277, 278, 0, 41, 272,
                43, 183, 45, 58, 155, 256, 197, 158, 41, 0,
                41, 202, 59, 123, 105, 125, 59, 60, 41, 62,
                43, 272, 45, 78, 79, 61, 59, 247, 59, 41,
                256, 273, 126, 123, 41, 125, 59, 60, 41, 62,
                43, 59, 45, 269, 270, 227, 228, 59, 41, 275,
                256, 272, 256, 123, 256, 125, 59, 60, 256, 62,
                0, 41, 43, 261, 45, 272, 264, 271, 266, 271,
                252, 190, 191, 271, 59, 256, 40, 59, 59, 59,
                123, 45, 125, 0, 41, 43, 45, 45, 45, 256,
                271, 45, 256, 256, 42, 43, 190, 191, 42, 47,
                123, 59, 125, 47, 271, 269, 270, 0, 263, 263,
                265, 265, 256, 256, 256, 276, 256, 256, 256, 276,
                123, 41, 125, 256, 276, 256, 276, 0, 276, 276,
                276, 273, 123, 271, 125, 271, 269, 270, 232, 45,
                234, 276, 275, 123, 256, 271, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 269, 270, 269,
                270, 271, 0, 275, 274, 271, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 256, 0, 269,
                270, 271, 44, 123, 274, 125, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 256, 41, 269,
                270, 271, 41, 45, 274, 271, 123, 271, 125, 256,
                271, 272, 271, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 271, 44, 269, 270, 271, 265,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 42, 41, 269, 270, 271, 47,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 59, 256, 269, 270, 271, 45,
                261, 274, 263, 264, 265, 266, 256, 41, 269, 270,
                271, 261, 262, 274, 264, 123, 266, 125, 59, 269,
                270, 271, 256, 41, 274, 59, 271, 256, 262, 256,
                271, 123, 256, 125, 41, 277, 278, 271, 272, 271,
                41, 59, 271, 272, 271, 272, 256, 271, 272, 41,
                41, 261, 59, 263, 264, 265, 266, 123, 45, 269,
                270, 271, 59, 263, 274, 265, 272, 41, 59, 256,
                59, 59, 61, 123, 261, 45, 263, 264, 265, 266,
                256, 59, 269, 270, 271, 59, 59, 274, 45, 123,
                43, 59, 45, 256, 59, 271, 272, 59, 261, 59,
                263, 264, 265, 266, 271, 123, 269, 270, 271, 59,
                59, 274, 271, 256, 271, 44, 123, 43, 261, 45,
                263, 264, 265, 266, 256, 272, 269, 270, 271, 280,
                265, 274, 123, 256, 60, 286, 62, 269, 270, 59,
                265, 292, 59, 275, 256, 265, 269, 270, 256, 123,
                265, 272, 275, 261, 272, 263, 264, 265, 266, 271,
                272, 269, 270, 271, 256, 123, 274, 256, 271, 261,
                59, 263, 264, 265, 266, 265, 59, 269, 270, 271,
                269, 270, 274, 59, 42, 43, 275, 42, 43, 47,
                256, 59, 47, 59, 272, 261, 41, 272, 264, 59,
                266, 59, 123, 41, 59, 271, 256, 44, 59, 41,
                256, 261, 54, 55, 264, 265, 266, 272, 123, 269,
                270, 271, 256, 59, 274, 271, 272, 261, 262, 123,
                264, 41, 266, 41, 256, 269, 270, 271, 256, 44,
                274, 41, 256, 261, 262, 0, 264, 59, 266, 256,
                123, 269, 270, 271, 261, 262, 274, 264, 55, 266,
                38, 123, 269, 270, 271, 256, -1, 274, 110, 256,
                261, 262, 114, 264, 44, 266, -1, -1, 269, 270,
                271, -1, 256, 274, 271, 272, 256, 261, 262, 123,
                264, -1, 266, -1, -1, 269, 270, 271, 256, 256,
                274, 271, 272, 261, -1, 263, 264, 265, 266, -1,
                125, 269, 270, 271, 271, 272, 274, 41, 42, 43,
                -1, 125, -1, 47, -1, -1, -1, -1, -1, -1,
                -1, 257, 258, 259, 260, 256, -1, 125, -1, -1,
                261, 183, 263, 264, 265, 266, -1, -1, 269, 270,
                271, 256, -1, 274, 125, -1, 261, 262, -1, 264,
                -1, 266, 256, -1, 269, 270, 271, 261, 262, 274,
                264, -1, 266, -1, -1, 269, 270, 271, -1, -1,
                274, -1, -1, 256, -1, 227, 228, -1, 261, 262,
                -1, 264, -1, 266, 256, -1, 269, 270, 271, 261,
                -1, 274, 264, -1, 266, -1, 256, 269, 270, 271,
                252, 261, 274, -1, 264, -1, 266, -1, -1, 269,
                270, 271, 256, -1, 274, -1, -1, 261, -1, -1,
                264, -1, 266, -1, -1, 269, 270, 271, -1, -1,
                274, 256, -1, -1, -1, -1, 261, -1, -1, 264,
                -1, 266, 256, -1, 269, 270, 271, 261, -1, 274,
                264, 23, 266, -1, -1, 269, 270, 271, 256, -1,
                274, -1, -1, 261, -1, -1, 264, -1, 266, -1,
                42, 269, 270, 271, 46, 256, 274, -1, -1, -1,
                261, -1, -1, 264, -1, 266, -1, -1, 269, 270,
                271, -1, -1, 274, -1, -1, -1, -1, -1, -1,
                72, 73, 74, 75, 76, 77, 268, -1, -1, -1,
                272, 273, -1, 272, 273, -1, -1, 279, 280, -1,
                279, -1, 284, 285, 286, 284, 285, 286, -1, 256,
                292, -1, -1, 295, 261, -1, 295, 264, -1, 266,
                -1, -1, 269, 270, 271, -1, -1, 274,
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
            "declaracion_procedimiento : proc_encabezado proc_parametros proc_ni proc_cuerpo",
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

//#line 426 "gramatica.y"

    private AnalizadorLexico analizadorLexico;
    private int nroUltimaLinea;
    private Ambitos ambitos;
    private PolacaInversa polaca;
    private boolean verbose;
    private PolacaInversaProcedimientos polacaProcedimientos;

    private String ultimoTipo;
    private List<String> parametrosFormales = new ArrayList<>();
    private List<String> parametrosReales = new ArrayList<>();

    private boolean actualizarTablaSimbolos;

    public Parser(AnalizadorLexico analizadorLexico, boolean actualizarTablaSimbolos, PolacaInversa polaca, PolacaInversaProcedimientos polacaProcedimientos, boolean verbose) {
        this.analizadorLexico = analizadorLexico;
        this.ambitos = new Ambitos();
        this.actualizarTablaSimbolos = actualizarTablaSimbolos;
        this.polaca = polaca;
        this.polacaProcedimientos = polacaProcedimientos;
        this.verbose = verbose;
    }

    private void yyerror(String mensaje) {
        //System.out.println(Main.ANSI_RED + "ERROR | " + mensaje + Main.ANSI_RESET);
    }

    private void imprimirReglaReconocida(String descripcion, int lineaCodigo) {
        if (verbose)
            System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: %s %n" + Main.ANSI_RESET, lineaCodigo, descripcion);
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
        String tipo = (String) token.getAtributo("tipo");
        if (tipo.equals("LONGINT")) {
            long entero = 0;
            if (Long.parseLong(cte) >= Main.MAX_LONG) {
                entero = Main.MAX_LONG - 1;
                Errores.addWarning(String.format("[AS] | Linea %d: Entero largo positivo fuera de rango: " + cte + " - Se cambia por: " + entero, analizadorLexico.getNroLinea()));
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
                flotante = Main.MAX_FLOAT - 1;
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

    public void actualizarContadorID(String lexema, boolean decremento) {
        Token token = TablaSimbolos.getToken(lexema);
        int cont = (decremento) ? (Integer) (token.getAtributo("contador")) - 1 : (Integer) (token.getAtributo("contador")) + 1;
        if (cont == 0)
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
        if (uso.equals("Procedimiento"))
            nuevoLexema = lexema + ":" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length()) - (lexema.length() + 1));
        else
            nuevoLexema = lexema + ":" + ambitos.getAmbitos();

        if (!TablaSimbolos.existe(nuevoLexema)) {
            Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
            nuevoToken.addAtributo("uso", uso);
            nuevoToken.addAtributo("tipo", tipo);
            nuevoToken.addAtributo("contador", 0);
            TablaSimbolos.add(nuevoToken);
        } else {
            if (uso.equals("Variable"))
                Errores.addError(String.format("[GD] | Linea %d: Variable redeclarada %n", analizadorLexico.getNroLinea()));
            else if (uso.equals("Procedimiento"))
                Errores.addError(String.format("[GD] | Linea %d: Procedimiento redeclarado %n", analizadorLexico.getNroLinea()));
        }
    }

    public String getAmbitoDeclaracionID(String lexema, String uso) {
        // Se chequea que el id esté declarado en el ámbito actual o en los ámbitos que lo contienen
        String ambitosString = ambitos.getAmbitos();
        ArrayList<String> ambitosList = new ArrayList<>(Arrays.asList(ambitosString.split(":")));

        // Para el caso de la invocación a procedimientos se acota el ámbito actual para excluirlo como parte del ámbito al tener que estar declarado en un ámbito padre
        if (uso.equals("Procedimiento") && !ambitosString.equals("main"))
            ambitosString = ambitosString.substring(0, (ambitosString.length()) - (ambitosList.get(ambitosList.size() - 1).length() + 1));

        boolean declarado = false;
        for (int i = 0; i < ambitosList.size(); i++) {
            if (TablaSimbolos.existe(lexema + ":" + ambitosString)) {
                if ((uso.equals("Parametro") && !TablaSimbolos.getToken(lexema + ":" + ambitosString).getAtributo("uso").equals("Procedimiento")) || !uso.equals("Parametro")) {
                    declarado = true;
                    //if(!uso.equals("Procedimiento"))
                    //  actualizarContadorID(lexema + ":" + ambitosString, false);
                    break;
                }
            } else if (!ambitosString.equals("main")) {
                ambitosString = ambitosString.substring(0, (ambitosString.length() - (ambitosList.get(ambitosList.size() - 1).length() + 1)));
                ambitosList.remove(ambitosList.size() - 1);
            }
        }

        if (!declarado) ambitosString = "";

        return ambitosString;
    }

    public void invocacionID(String lexema, String uso) {
        String ambitosString = getAmbitoDeclaracionID(lexema, uso);
        boolean declarado = (ambitosString.isEmpty()) ? false : true;

        if (!declarado) {
            if (uso.equals("Variable"))
                Errores.addError(String.format("[GD] | Linea %d: Variable no declarada %n", analizadorLexico.getNroLinea()));
            else if (uso.equals("Procedimiento"))
                Errores.addError(String.format("[GD] | Linea %d: Procedimiento no declarado %n", analizadorLexico.getNroLinea()));
            else if (uso.equals("Parametro"))
                Errores.addError(String.format("[GD] | Linea %d: Parametro real no declarado %n", analizadorLexico.getNroLinea()));
        }

        if (uso.equals("Parametro") && declarado)
            parametrosReales.add(lexema + ":" + ambitosString);

        if (uso.equals("Procedimiento") && declarado) {
            Token procedimiento = TablaSimbolos.getToken(lexema + ":" + ambitosString);

            // Si se trata de un procedimiento que se encuentra declarado, se chequea el número de invocaciones respecto del máximo permitido
            if (uso.equals("Procedimiento") && (((Integer) procedimiento.getAtributo("contador") + 1) > (Integer) procedimiento.getAtributo("max. invocaciones")))
                Errores.addError(String.format("[GD] | Linea %d: Se supera el máximo de invocaciones del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
                // Si se trata de un procedimiento que se encuentra declarado, se chequea además que la cantidad de parámetros reales correspondan a los formales
                List<String> parametrosFormales = (List) procedimiento.getAtributo("parametros");
                if (parametrosReales.size() != parametrosFormales.size())
                    Errores.addError(String.format("[GD] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
                else {
                    // Se chequea, por último, los tipos entre parámetros reales y formales
                    boolean tiposCompatibles = true;
                    for (int i = 0; i < parametrosReales.size(); i++) {
                        String tipoParametroReal = TablaSimbolos.getToken(parametrosReales.get(i)).getAtributo("tipo") + "";
                        if (!parametrosFormales.get(i).contains(tipoParametroReal)) {
                            Errores.addError(String.format("[GD] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i + 1));
                            tiposCompatibles = false;
                            break;
                        }
                    }

                    if (tiposCompatibles) {
                        SA6(lexema);
                        actualizarContadorID(lexema + ":" + ambitosString, false);
                    }
                }
            }
            parametrosReales.clear();
        }

        if (declarado && !uso.equals("Procedimiento"))
            actualizarContadorID(lexema + ":" + ambitosString, false);
        // Se actualiza el contador de referencias
        actualizarContadorID(lexema, true);
    }

    public String getLexemaID() {
        String ambitosActuales = ambitos.getAmbitos();
        String id = ambitosActuales.split(":")[ambitosActuales.split(":").length - 1];
        return (id + ":" + ambitosActuales.substring(0, (ambitosActuales.length()) - (id.length() + 1)));
    }


    public void out(String lex) {
        Token token = TablaSimbolos.getToken(lex);
        if (token != null) {
            if (token.getTipoToken().equals("IDENTIFICADOR")) {
                if (token.getAtributo("uso") != null) {
                    if (token.getAtributo("uso").equals("VARIABLE")) {
                        Object valor = token.getAtributo("VALOR");
                        if (valor != null)
                            System.out.println(token.getAtributo("VALOR") + "\n");
                        else
                            Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no inicializada %n", analizadorLexico.getNroLinea()));
                    }
                } else
                    Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no declarada %n", analizadorLexico.getNroLinea()));
            } else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                System.out.println(token.getLexema(false) + "\n");
        }
    }

//public void SA1(String lex){  //añadir factor a la polaca
//	Token token = TablaSimbolos.getToken(lex);
//	ElemSimple elem = new ElemSimple(token, analizadorLexico.getNroLinea());
//	polaca.addElem(elem, false);
//}

    public void SA1(String lexema) {  // Añadir factor a la polaca
        String ambitosActuales = ambitos.getAmbitos();

        Token token = TablaSimbolos.getToken(lexema + ":" + getAmbitoDeclaracionID(lexema, "Variable"));

        // Se añade a la polaca el token sin el ámbito en el lexema
        String lexemaToken = token.getLexema(false);
        Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), (lexemaToken.contains(":")) ? lexemaToken.substring(0, lexemaToken.indexOf(":")) : lexemaToken);
        for (Map.Entry<String, Object> atributo : token.getAtributos().entrySet()) {
            nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
        }
        ElemSimple elem = new ElemSimple(nuevoToken, analizadorLexico.getNroLinea());
        if (ambitosActuales.equals(Ambitos.ambitoGlobal))
            polaca.addElem(elem, false);
        else
            polacaProcedimientos.addElem(ambitosActuales, elem, false);
    }

    public void SA2(String operador) { // Añadir operador binario a la polaca
        String ambitosActuales = ambitos.getAmbitos();
        OperadorBinario elem = new OperadorBinario(operador, analizadorLexico.getNroLinea());

        if (ambitosActuales.equals(Ambitos.ambitoGlobal))
            polaca.addElem(elem, false);
        else
            polacaProcedimientos.addElem(ambitosActuales, elem, false);
    }

    public void SA3(String cte) { //chequea que la constante sea LONGINT
        if (!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
            Errores.addError(String.format("[AS] | Linea %d: Constante no es del tipo entero %n", analizadorLexico.getNroLinea()));
    }

    public void SA4(String id1, String id2) { //reviso que la variable inicializada en el for sea la misma que la de la condicion
        Token token1 = TablaSimbolos.getToken(id1);
        Token token2 = TablaSimbolos.getToken(id2);
        if (!token1.equals(token2))
            Errores.addError(String.format("[AS] | Linea %d: En la sentencia for, la variable inicializada " + id1 + " no es la misma que la variable utilizada en la condicion %n", analizadorLexico.getNroLinea()));
    }

    public void SA5(String id, String cte, String op) { //incremento o decremento la variable del for
        Token id_t = TablaSimbolos.getToken(id);
        Token cte_t = TablaSimbolos.getToken(cte);

        if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
            polaca.addElem(new ElemSimple(id_t), false);
            polaca.addElem(new ElemSimple(cte_t), false);
            polaca.addElem(new OperadorBinario(op), false);
            polaca.addElem(new ElemSimple(id_t), false);
            polaca.addElem(new OperadorBinario("="), false);
        } else {
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
        if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.INV), false);
        else
            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.INV), false);
    }

    //#line 982 "Parser.java"
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
//#line 39 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 5:
//#line 40 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 19:
//#line 67 "gramatica.y"
                {
                    imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea());
                }
                break;
                case 20:
//#line 68 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 21:
//#line 69 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 22:
//#line 71 "gramatica.y"
                {
                    declaracionID(val_peek(2).sval, "Variable", ultimoTipo);
                }
                break;
                case 23:
//#line 72 "gramatica.y"
                {
                    declaracionID(val_peek(0).sval, "Variable", ultimoTipo);
                }
                break;
                case 24:
//#line 73 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 25:
//#line 76 "gramatica.y"
                {
                    ultimoTipo = "LONGINT";
                }
                break;
                case 26:
//#line 77 "gramatica.y"
                {
                    ultimoTipo = "FLOAT";
                }
                break;
                case 27:
//#line 80 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                    ambitos.eliminarAmbito(actualizarTablaSimbolos);
                }
                break;
                case 28:
//#line 85 "gramatica.y"
                {
                    ambitos.agregarAmbito(val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Procedimiento", null);
                }
                break;
                case 29:
//#line 88 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 30:
//#line 91 "gramatica.y"
                {
                    TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 31:
//#line 94 "gramatica.y"
                {
                    TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                }
                break;
                case 32:
//#line 95 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 33:
//#line 96 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 34:
//#line 97 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 35:
//#line 98 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 36:
//#line 99 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 37:
//#line 102 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                        Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                    int cteInt = Integer.parseInt(cte);
                    TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                    if (cteInt < 1 || cteInt > 4)
                        Errores.addError(String.format("[ASem] | Linea %d: NI no se encuentra en el intervalo [1,4] %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 38:
//#line 110 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 39:
//#line 111 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 40:
//#line 112 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 44:
//#line 121 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                }
                break;
                case 45:
//#line 122 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                }
                break;
                case 46:
//#line 123 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                }
                break;
                case 47:
//#line 124 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 48:
//#line 125 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 49:
//#line 126 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 50:
//#line 127 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 51:
//#line 128 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 52:
//#line 131 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                }
                break;
                case 53:
//#line 136 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + ":" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                }
                break;
                case 54:
//#line 141 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 55:
//#line 142 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 56:
//#line 145 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.addElem(new ElemPos(polaca.size()), true);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 57:
//#line 155 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de selección IF", analizadorLexico.getNroLinea());
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.addElem(new ElemPos(polaca.size()), true);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 58:
//#line 165 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 59:
//#line 166 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 60:
//#line 167 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 61:
//#line 168 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 62:
//#line 169 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 63:
//#line 170 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 64:
//#line 171 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 65:
//#line 172 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d:  Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 66:
//#line 173 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d:  Falta palabra reservada END_IF y literal ';' %n", nroUltimaLinea));
                }
                break;
                case 67:
//#line 174 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 68:
//#line 175 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 69:
//#line 176 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 70:
//#line 177 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d:  Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 71:
//#line 178 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d:  Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 72:
//#line 179 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d:  Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 73:
//#line 182 "gramatica.y"
                {
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.pushPos(true);
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                    } else {
                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF), false);
                    }
                }
                break;
                case 74:
//#line 191 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 75:
//#line 192 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 76:
//#line 193 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 77:
//#line 194 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 78:
//#line 195 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 79:
//#line 199 "gramatica.y"
                {
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.addElem(new ElemPos(polaca.size() + 2), true);
                        polaca.pushPos(true);
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI), false);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos()) + 2), true);
                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BI), false);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 81:
//#line 217 "gramatica.y"
                {
                    SA3(val_peek(2).sval);
                    SA4(val_peek(6).sval, val_peek(4).sval);
                    SA5(val_peek(6).sval, val_peek(2).sval, val_peek(3).sval); /* id cte incr_decr*/
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.addElem(new ElemPos(polaca.size() + 2), true);
                        polaca.addElem(new ElemPos(polaca.popPos()), false);
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BI), false);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos()) + 2), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new ElemPos(polacaProcedimientos.popPos(ambitos.getAmbitos())), false);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BI), false);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 82:
//#line 233 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 83:
//#line 234 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 84:
//#line 235 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 85:
//#line 236 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 86:
//#line 237 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 87:
//#line 238 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 88:
//#line 239 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 89:
//#line 240 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 90:
//#line 243 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA3(val_peek(0).sval);
                    SA1(val_peek(2).sval);
                    SA1(val_peek(0).sval);
                    SA2(val_peek(1).sval);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.pushPos(false);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), false);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 91:
//#line 257 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 92:
//#line 258 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error, el inicio del for debe ser una asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 93:
//#line 259 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 94:
//#line 260 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 97:
//#line 268 "gramatica.y"
                {
                    yyval = val_peek(1);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.pushPos(true);
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.BF), false);
                    } else {
                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), true);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.BF), false);
                    }
                }
                break;
                case 98:
//#line 280 "gramatica.y"
                {
                    yyval = new ParserVal("+");
                }
                break;
                case 99:
//#line 281 "gramatica.y"
                {
                    yyval = new ParserVal("-");
                }
                break;
                case 100:
//#line 284 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                    SA1(val_peek(2).sval);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                    else
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 101:
//#line 291 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 102:
//#line 292 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 103:
//#line 293 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 104:
//#line 294 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 105:
//#line 295 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 106:
//#line 318 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                    String id = val_peek(3).sval;
                    String cte = val_peek(1).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        /* TODO: hay que agregar el valor en la TS?*/
                        token.addAtributo("VALOR", cte);
                        SA1(id);
                        SA2(val_peek(2).sval);
                    }
                    invocacionID(id, "Variable");
                }
                break;
                case 107:
//#line 330 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 108:
//#line 331 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 109:
//#line 332 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 110:
//#line 333 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 111:
//#line 336 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación con lista de parámetros %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, "Procedimiento");
                }
                break;
                case 112:
//#line 339 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(3).sval, "Procedimiento");
                }
                break;
                case 113:
//#line 342 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 114:
//#line 343 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 115:
//#line 344 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 116:
//#line 345 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 117:
//#line 346 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 118:
//#line 347 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 119:
//#line 348 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 120:
//#line 351 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, "Parametro");
                    invocacionID(val_peek(2).sval, "Parametro");
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 121:
//#line 356 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(2).sval, "Parametro");
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 122:
//#line 360 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 123:
//#line 363 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 124:
//#line 364 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 125:
//#line 365 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 126:
//#line 366 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 127:
//#line 367 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 128:
//#line 368 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 129:
//#line 372 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">=");
                }
                break;
                case 130:
//#line 373 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<=");
                }
                break;
                case 131:
//#line 374 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">");
                }
                break;
                case 132:
//#line 375 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<");
                }
                break;
                case 133:
//#line 376 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("==");
                }
                break;
                case 134:
//#line 377 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("!=");
                }
                break;
                case 135:
//#line 381 "gramatica.y"
                {
                    imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 136:
//#line 383 "gramatica.y"
                {
                    imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 138:
//#line 386 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 139:
//#line 387 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 140:
//#line 388 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 141:
//#line 391 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 142:
//#line 394 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 143:
//#line 397 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 144:
//#line 398 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 145:
//#line 399 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 146:
//#line 400 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 147:
//#line 401 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 148:
//#line 404 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                    invocacionID(val_peek(0).sval, "Variable");
                }
                break;
                case 149:
//#line 406 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 150:
//#line 409 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 151:
//#line 416 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                    }
                }
                break;
//#line 1788 "Parser.java"
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
