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
//#line 25 "Parser.java"


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
            7, 15, 15, 15, 14, 14, 8, 8, 8, 16,
            16, 18, 18, 18, 18, 18, 19, 19, 19, 19,
            17, 17, 17, 17, 17, 17, 17, 17, 17, 20,
            20, 20, 20, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            21, 21, 21, 21, 21, 21, 22, 23, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 25, 25, 25,
            25, 25, 25, 25, 28, 28, 28, 28, 26, 27,
            27, 11, 11, 11, 11, 11, 11, 11, 11, 12,
            12, 12, 12, 12, 13, 13, 13, 13, 13, 13,
            13, 13, 13, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 24, 24, 24, 33, 33, 33, 33, 33,
            33, 31, 31, 31, 31, 31, 31, 34, 34, 34,
            34, 34, 34, 34, 30, 30, 29, 29,
    };
    final static short yylen[] = {2,
            1, 2, 1, 1, 2, 3, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 3,
            2, 3, 1, 2, 1, 1, 4, 3, 4, 2,
            2, 3, 2, 3, 3, 1, 3, 2, 3, 3,
            7, 5, 3, 2, 6, 6, 4, 9, 5, 2,
            3, 2, 3, 6, 8, 5, 7, 7, 5, 7,
            6, 8, 5, 5, 7, 8, 7, 7, 5, 7,
            3, 2, 2, 2, 1, 3, 1, 1, 8, 7,
            8, 9, 8, 8, 7, 8, 8, 4, 4, 4,
            2, 3, 3, 3, 3, 1, 3, 3, 2, 1,
            1, 5, 5, 4, 5, 5, 4, 5, 4, 4,
            4, 4, 4, 3, 5, 4, 4, 3, 5, 4,
            3, 4, 3, 5, 3, 1, 7, 3, 3, 4,
            4, 2, 3, 3, 3, 1, 1, 1, 1, 1,
            1, 3, 3, 1, 3, 3, 3, 3, 3, 1,
            3, 3, 3, 3, 1, 1, 1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 0, 155, 157, 0, 0, 0,
            0, 156, 150, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 31, 30,
            8, 21, 0, 0, 0, 0, 0, 0, 0, 140,
            136, 137, 141, 138, 139, 0, 0, 0, 0, 0,
            77, 7, 0, 0, 74, 0, 158, 0, 0, 72,
            0, 0, 0, 0, 0, 0, 91, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 121, 0, 0, 118,
            0, 0, 24, 20, 19, 36, 0, 0, 0, 0,
            0, 44, 0, 0, 0, 0, 0, 28, 111, 0,
            0, 153, 154, 0, 0, 0, 0, 76, 71, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 151, 148, 152, 149, 0, 93, 0, 94, 0,
            92, 0, 0, 0, 0, 99, 100, 101, 0, 0,
            0, 0, 0, 104, 0, 112, 129, 0, 128, 0,
            113, 110, 0, 116, 120, 0, 117, 22, 0, 0,
            33, 29, 52, 0, 0, 50, 0, 43, 0, 0,
            0, 38, 27, 6, 78, 0, 69, 0, 0, 0,
            59, 0, 0, 0, 0, 0, 56, 89, 90, 88,
            0, 0, 0, 0, 0, 0, 103, 105, 106, 102,
            108, 131, 130, 0, 119, 115, 34, 35, 32, 53,
            51, 0, 0, 0, 47, 39, 0, 37, 0, 0,
            61, 0, 0, 0, 0, 54, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 49, 0, 42, 0,
            0, 70, 0, 60, 0, 65, 67, 0, 58, 57,
            0, 0, 0, 0, 0, 0, 0, 0, 96, 85,
            80, 0, 0, 46, 45, 62, 66, 55, 81, 87,
            0, 83, 84, 0, 3, 4, 0, 86, 79, 127,
            0, 41, 82, 97, 2, 5, 0, 95, 0, 48,
    };
    final static short yydgoto[] = {9,
            126, 304, 11, 12, 205, 72, 14, 15, 16, 17,
            18, 19, 20, 21, 54, 22, 57, 119, 128, 124,
            30, 73, 206, 94, 40, 95, 169, 290, 32, 33,
            34, 48, 69, 35,
    };
    final static short yysindex[] = {701,
            -18, -32, -24, -36, 0, 0, -29, -201, 0, 701,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -161, -35, 157, 613, 619, 0, 0, 154, -243, 485,
            61, 0, 0, 547, 96, 17, -46, -166, 458, 269,
            -148, 147, 157, -25, 277, -3, 86, 132, 0, 0,
            0, 0, -16, -53, 37, -19, 434, 404, 76, 0,
            0, 0, 0, 0, 0, 290, 49, 49, 157, 701,
            0, 0, -131, 542, 0, 145, 0, -87, -49, 0,
            295, 343, 361, 371, 379, -118, 0, 146, 427, 149,
            151, 269, 409, 166, 198, 207, 208, -39, 210, 282,
            164, 3, -67, 116, 426, 315, 0, 301, -2, 0,
            340, 65, 0, 0, 0, 0, -37, 137, 630, 133,
            174, 0, 144, -41, 765, 658, 630, 0, 0, 246,
            96, 0, 0, 22, 678, 619, 358, 0, 0, 53,
            619, 362, 553, 619, 380, 246, 96, 246, 96, 404,
            22, 0, 0, 0, 0, 389, 0, 393, 0, 402,
            0, 198, 198, 468, -202, 0, 0, 0, 201, 407,
            411, 438, 446, 0, 447, 0, 0, 222, 0, 5,
            0, 0, 454, 0, 0, 455, 0, 0, 244, -238,
            0, 0, 0, 247, 253, 0, -198, 0, 121, -38,
            690, 0, 0, 0, 0, 261, 0, 619, 473, 291,
            0, -18, 524, 506, 303, 305, 0, 0, 0, 0,
            304, 309, 198, 316, -157, 534, 0, 0, 0, 0,
            0, 0, 0, 322, 0, 0, 0, 0, 0, 0,
            0, -27, -34, 221, 0, 0, -18, 0, 521, 332,
            0, 528, -13, 539, -230, 0, 554, 563, 565, 571,
            356, 589, 593, 339, 330, 604, 0, 221, 0, 596,
            616, 0, 595, 0, 625, 0, 0, 626, 0, 0,
            330, 330, 645, 330, 330, 474, 701, 642, 0, 0,
            0, 431, 367, 0, 0, 0, 0, 0, 0, 0,
            330, 0, 0, 712, 0, 0, 723, 0, 0, 0,
            649, 0, 0, 0, 0, 0, -18, 0, 651, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 694,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            570, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 10, 0, 0, 0, 0, 0, 0,
            0, 0, -50, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 590, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 33, 0, 0, 120, 0, 0, 171, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            21, 0, 0, 131, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 41, 68, 88, 108, 397,
            408, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 194, 0, 218, 0, 0, 0, 0, 48,
            0, 0, 0, 0, 0, 238, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 273, 0, 289, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 308, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 64, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 355, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 375, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            27, 410, 726, -147, 15, 652, 0, 0, 0, 0,
            0, 0, 0, 567, -33, 0, 385, 650, -15, -107,
            0, -5, -91, 57, 663, 430, -132, 436, 622, 113,
            73, 666, 681, 119,
    };
    final static int YYTABLESIZE = 1039;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{198,
                144, 173, 197, 42, 56, 115, 269, 28, 23, 268,
                46, 47, 29, 267, 89, 39, 199, 238, 103, 113,
                147, 122, 23, 190, 79, 277, 10, 112, 77, 221,
                222, 45, 225, 239, 278, 70, 38, 108, 186, 71,
                145, 144, 23, 144, 71, 144, 178, 23, 234, 210,
                126, 215, 216, 224, 49, 107, 185, 242, 31, 144,
                144, 147, 144, 147, 81, 147, 82, 142, 126, 50,
                5, 6, 143, 132, 167, 168, 121, 86, 188, 147,
                147, 145, 147, 145, 76, 145, 246, 146, 125, 243,
                261, 132, 71, 29, 52, 59, 135, 118, 263, 145,
                145, 80, 145, 192, 124, 90, 125, 143, 142, 53,
                142, 203, 142, 23, 264, 101, 250, 105, 81, 114,
                82, 255, 124, 144, 96, 144, 142, 142, 146, 142,
                146, 136, 146, 137, 129, 270, 271, 84, 306, 306,
                308, 134, 85, 147, 110, 147, 146, 146, 143, 146,
                143, 201, 143, 156, 100, 151, 316, 67, 66, 316,
                293, 245, 68, 145, 244, 145, 143, 143, 140, 143,
                123, 135, 111, 2, 181, 141, 3, 142, 4, 132,
                133, 5, 6, 7, 131, 139, 8, 99, 179, 135,
                142, 29, 142, 107, 75, 29, 153, 155, 29, 147,
                149, 29, 114, 180, 157, 23, 81, 161, 82, 87,
                146, 86, 146, 144, 120, 145, 172, 109, 189, 41,
                55, 120, 176, 24, 166, 88, 43, 5, 6, 25,
                143, 36, 143, 121, 5, 6, 120, 122, 26, 27,
                121, 44, 114, 193, 114, 102, 37, 170, 171, 5,
                6, 275, 106, 135, 53, 121, 144, 144, 144, 144,
                144, 144, 144, 144, 144, 144, 144, 44, 174, 144,
                144, 144, 64, 177, 144, 233, 147, 147, 147, 147,
                147, 147, 147, 147, 147, 147, 147, 67, 63, 147,
                147, 147, 68, 123, 147, 123, 145, 145, 145, 145,
                145, 145, 145, 145, 145, 145, 145, 40, 116, 145,
                145, 145, 117, 29, 145, 208, 107, 209, 107, 26,
                27, 29, 175, 142, 142, 142, 142, 142, 142, 142,
                142, 142, 142, 142, 29, 53, 142, 142, 142, 29,
                109, 142, 109, 146, 146, 146, 146, 146, 146, 146,
                146, 146, 146, 146, 68, 183, 146, 146, 146, 184,
                122, 146, 122, 143, 143, 143, 143, 143, 143, 143,
                143, 143, 143, 143, 98, 114, 143, 143, 143, 288,
                114, 143, 114, 114, 114, 114, 135, 29, 114, 114,
                114, 135, 135, 114, 135, 64, 135, 64, 187, 135,
                135, 135, 97, 193, 135, 29, 24, 312, 191, 74,
                311, 63, 58, 63, 196, 29, 207, 26, 27, 98,
                211, 26, 27, 29, 26, 27, 123, 26, 27, 194,
                40, 123, 40, 123, 123, 123, 123, 134, 217, 123,
                123, 123, 5, 6, 123, 67, 66, 218, 133, 107,
                68, 219, 287, 29, 107, 134, 107, 107, 107, 107,
                220, 287, 107, 107, 107, 227, 133, 107, 81, 228,
                82, 29, 226, 109, 167, 168, 120, 68, 109, 68,
                109, 109, 109, 109, 182, 159, 109, 109, 109, 5,
                6, 109, 232, 122, 118, 121, 229, 98, 122, 98,
                122, 122, 122, 122, 230, 231, 122, 122, 122, 67,
                66, 122, 235, 236, 68, 237, 92, 240, 38, 134,
                162, 163, 165, 241, 24, 249, 223, 65, 64, 64,
                133, 251, 104, 64, 23, 64, 64, 64, 64, 26,
                27, 64, 64, 64, 63, 130, 64, 26, 27, 63,
                146, 63, 63, 63, 63, 252, 125, 63, 63, 63,
                26, 27, 63, 40, 256, 26, 27, 257, 40, 258,
                40, 40, 40, 40, 265, 259, 40, 40, 40, 272,
                260, 40, 138, 67, 66, 286, 274, 262, 68, 81,
                2, 82, 266, 3, 286, 4, 273, 276, 148, 2,
                7, 65, 3, 64, 4, 281, 65, 70, 64, 7,
                68, 282, 279, 26, 27, 68, 150, 68, 68, 68,
                68, 280, 123, 68, 68, 68, 152, 283, 68, 284,
                98, 26, 27, 285, 154, 98, 294, 98, 98, 98,
                98, 26, 27, 98, 98, 98, 70, 292, 98, 26,
                27, 13, 134, 296, 67, 66, 295, 134, 134, 68,
                134, 51, 134, 133, 164, 134, 134, 134, 133, 133,
                134, 133, 65, 133, 64, 70, 133, 133, 133, 26,
                27, 133, 158, 297, 298, 301, 310, 195, 56, 1,
                123, 320, 75, 1, 2, 319, 307, 3, 27, 4,
                291, 93, 5, 6, 7, 116, 127, 8, 13, 117,
                160, 109, 73, 91, 83, 0, 299, 300, 0, 302,
                303, 13, 0, 309, 60, 61, 62, 63, 37, 1,
                0, 0, 0, 0, 2, 0, 313, 3, 0, 4,
                1, 70, 5, 6, 7, 2, 78, 8, 3, 0,
                4, 0, 125, 5, 6, 7, 0, 0, 8, 0,
                0, 0, 0, 123, 287, 0, 0, 0, 0, 0,
                13, 0, 0, 0, 0, 0, 13, 51, 13, 253,
                0, 0, 202, 0, 2, 0, 51, 3, 254, 4,
                0, 0, 5, 6, 7, 0, 0, 8, 60, 61,
                62, 63, 204, 60, 61, 62, 63, 0, 212, 123,
                123, 0, 0, 2, 248, 213, 3, 214, 4, 0,
                0, 5, 6, 7, 0, 75, 8, 0, 0, 0,
                75, 75, 0, 75, 123, 75, 314, 0, 75, 75,
                75, 0, 0, 75, 0, 73, 0, 318, 0, 0,
                73, 73, 51, 73, 0, 73, 0, 0, 73, 73,
                73, 0, 0, 73, 0, 0, 0, 0, 0, 60,
                61, 62, 63, 0, 1, 0, 0, 0, 0, 2,
                0, 0, 3, 0, 4, 1, 0, 5, 6, 7,
                2, 0, 8, 3, 0, 4, 0, 286, 5, 6,
                7, 0, 2, 8, 0, 3, 0, 4, 0, 0,
                5, 6, 7, 1, 0, 8, 0, 0, 2, 0,
                0, 3, 0, 4, 0, 0, 5, 6, 7, 0,
                0, 8, 0, 1, 0, 0, 0, 0, 2, 0,
                0, 3, 0, 4, 0, 247, 5, 6, 7, 0,
                2, 8, 0, 3, 0, 4, 1, 0, 5, 6,
                7, 2, 0, 8, 3, 0, 4, 1, 0, 5,
                6, 7, 2, 0, 8, 3, 0, 4, 317, 0,
                5, 6, 7, 2, 0, 8, 3, 0, 4, 289,
                289, 5, 6, 7, 0, 0, 8, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 289, 289, 0, 289,
                289, 305, 305, 289, 0, 0, 0, 0, 0, 0,
                200, 0, 0, 0, 0, 2, 289, 0, 3, 315,
                4, 0, 315, 5, 6, 7, 0, 0, 8,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{41,
                0, 41, 44, 40, 40, 59, 41, 40, 59, 44,
                40, 41, 45, 41, 61, 40, 124, 256, 44, 53,
                0, 41, 61, 61, 30, 256, 0, 44, 272, 162,
                163, 61, 165, 272, 265, 123, 61, 41, 41, 25,
                0, 41, 61, 43, 30, 45, 44, 61, 44, 141,
                41, 143, 144, 256, 256, 59, 59, 256, 2, 59,
                60, 41, 62, 43, 43, 45, 45, 0, 59, 271,
                269, 270, 78, 41, 277, 278, 275, 61, 112, 59,
                60, 41, 62, 43, 28, 45, 125, 0, 41, 197,
                223, 59, 78, 45, 256, 23, 70, 61, 256, 59,
                60, 41, 62, 119, 41, 272, 59, 0, 41, 271,
                43, 127, 45, 61, 272, 43, 208, 45, 43, 0,
                45, 213, 59, 123, 273, 125, 59, 60, 41, 62,
                43, 263, 45, 265, 59, 243, 244, 42, 286, 287,
                288, 69, 47, 123, 59, 125, 59, 60, 41, 62,
                43, 125, 45, 272, 42, 83, 304, 42, 43, 307,
                268, 41, 47, 123, 44, 125, 59, 60, 256, 62,
                0, 41, 41, 261, 59, 263, 264, 265, 266, 67,
                68, 269, 270, 271, 66, 41, 274, 41, 256, 59,
                123, 45, 125, 0, 41, 45, 84, 85, 45, 81,
                82, 45, 256, 271, 59, 256, 43, 59, 45, 256,
                123, 61, 125, 263, 256, 265, 256, 0, 256, 256,
                256, 256, 59, 256, 59, 272, 256, 269, 270, 262,
                123, 256, 125, 275, 269, 270, 256, 0, 271, 272,
                275, 271, 123, 271, 125, 271, 271, 41, 41, 269,
                270, 265, 256, 123, 271, 275, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 271, 59, 269,
                270, 271, 0, 271, 274, 271, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 42, 0, 269,
                270, 271, 47, 123, 274, 125, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 0, 272, 269,
                270, 271, 276, 45, 274, 263, 123, 265, 125, 271,
                272, 45, 41, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 45, 271, 269, 270, 271, 45,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 0, 41, 269, 270, 271, 59,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 0, 256, 269, 270, 271, 41,
                261, 274, 263, 264, 265, 266, 256, 45, 269, 270,
                271, 261, 262, 274, 264, 123, 266, 125, 59, 269,
                270, 271, 256, 271, 274, 45, 256, 41, 272, 256,
                44, 123, 256, 125, 271, 45, 59, 271, 272, 273,
                59, 271, 272, 45, 271, 272, 256, 271, 272, 256,
                123, 261, 125, 263, 264, 265, 266, 41, 59, 269,
                270, 271, 269, 270, 274, 42, 43, 59, 41, 256,
                47, 59, 123, 45, 261, 59, 263, 264, 265, 266,
                59, 123, 269, 270, 271, 59, 59, 274, 43, 59,
                45, 45, 272, 256, 277, 278, 256, 123, 261, 125,
                263, 264, 265, 266, 59, 59, 269, 270, 271, 269,
                270, 274, 271, 256, 61, 275, 59, 123, 261, 125,
                263, 264, 265, 266, 59, 59, 269, 270, 271, 42,
                43, 274, 59, 59, 47, 272, 59, 271, 61, 123,
                91, 92, 93, 271, 256, 265, 59, 60, 256, 62,
                123, 59, 256, 261, 61, 263, 264, 265, 266, 271,
                272, 269, 270, 271, 256, 256, 274, 271, 272, 261,
                256, 263, 264, 265, 266, 265, 123, 269, 270, 271,
                271, 272, 274, 256, 59, 271, 272, 265, 261, 265,
                263, 264, 265, 266, 41, 272, 269, 270, 271, 59,
                272, 274, 41, 42, 43, 256, 59, 272, 47, 43,
                261, 45, 271, 264, 256, 266, 265, 59, 256, 261,
                271, 60, 264, 62, 266, 41, 60, 123, 62, 271,
                256, 41, 59, 271, 272, 261, 256, 263, 264, 265,
                266, 59, 56, 269, 270, 271, 256, 272, 274, 41,
                256, 271, 272, 41, 256, 261, 41, 263, 264, 265,
                266, 271, 272, 269, 270, 271, 123, 44, 274, 271,
                272, 0, 256, 59, 42, 43, 41, 261, 262, 47,
                264, 10, 266, 256, 256, 269, 270, 271, 261, 262,
                274, 264, 60, 266, 62, 123, 269, 270, 271, 271,
                272, 274, 256, 59, 59, 41, 256, 121, 40, 256,
                124, 41, 123, 0, 261, 311, 287, 264, 272, 266,
                265, 39, 269, 270, 271, 272, 57, 274, 57, 276,
                89, 46, 123, 256, 34, -1, 281, 282, -1, 284,
                285, 70, -1, 288, 257, 258, 259, 260, 271, 256,
                -1, -1, -1, -1, 261, -1, 301, 264, -1, 266,
                256, 123, 269, 270, 271, 261, 262, 274, 264, -1,
                266, -1, 123, 269, 270, 271, -1, -1, 274, -1,
                -1, -1, -1, 197, 123, -1, -1, -1, -1, -1,
                119, -1, -1, -1, -1, -1, 125, 126, 127, 256,
                -1, -1, 125, -1, 261, -1, 135, 264, 265, 266,
                -1, -1, 269, 270, 271, -1, -1, 274, 257, 258,
                259, 260, 125, 257, 258, 259, 260, -1, 256, 243,
                244, -1, -1, 261, 125, 263, 264, 265, 266, -1,
                -1, 269, 270, 271, -1, 256, 274, -1, -1, -1,
                261, 262, -1, 264, 268, 266, 125, -1, 269, 270,
                271, -1, -1, 274, -1, 256, -1, 125, -1, -1,
                261, 262, 201, 264, -1, 266, -1, -1, 269, 270,
                271, -1, -1, 274, -1, -1, -1, -1, -1, 257,
                258, 259, 260, -1, 256, -1, -1, -1, -1, 261,
                -1, -1, 264, -1, 266, 256, -1, 269, 270, 271,
                261, -1, 274, 264, -1, 266, -1, 256, 269, 270,
                271, -1, 261, 274, -1, 264, -1, 266, -1, -1,
                269, 270, 271, 256, -1, 274, -1, -1, 261, -1,
                -1, 264, -1, 266, -1, -1, 269, 270, 271, -1,
                -1, 274, -1, 256, -1, -1, -1, -1, 261, -1,
                -1, 264, -1, 266, -1, 256, 269, 270, 271, -1,
                261, 274, -1, 264, -1, 266, 256, -1, 269, 270,
                271, 261, -1, 274, 264, -1, 266, 256, -1, 269,
                270, 271, 261, -1, 274, 264, -1, 266, 256, -1,
                269, 270, 271, 261, -1, 274, 264, -1, 266, 264,
                265, 269, 270, 271, -1, -1, 274, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 281, 282, -1, 284,
                285, 286, 287, 288, -1, -1, -1, -1, -1, -1,
                256, -1, -1, -1, -1, 261, 301, -1, 264, 304,
                266, -1, 307, 269, 270, 271, -1, -1, 274,
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
            "condicion : expresion comparador expresion",
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

//#line 439 "gramatica.y"

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
            try {
                if (Long.parseLong(cte) >= Main.MAX_LONG) {
                    entero = Main.MAX_LONG - 1;
                    Errores.addWarning(String.format("[AS] | Linea %d: Entero largo positivo fuera de rango: %s - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
                    String nuevoLexema = String.valueOf(entero);
                    cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
                    return nuevoLexema;
                }
            } catch (NumberFormatException e) {
                Errores.addError(String.format("[AS] | Linea %d: Error en la constante numerica", analizadorLexico.getNroLinea()));
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
            try {
                if (Long.parseLong(cte) <= Main.MAX_LONG) {
                    entero = Long.parseLong(cte);
                } else {
                    entero = Main.MAX_LONG;
                    Errores.addWarning(String.format("[AS] | Linea %d: Entero largo negativo fuera de rango: %d - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
                }
            } catch (NumberFormatException e) {
                Errores.addError(String.format("[AS] | Linea %d: Error en la constante numerica", analizadorLexico.getNroLinea()));
            }
            nuevoLexema = "-" + entero;
            cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
            return nuevoLexema;

        }
        if (tipo.equals("FLOAT")) {
            float flotante = 0f;
            try {
                if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
                    flotante = Float.parseFloat(cte);
                } else {
                    flotante = Main.MAX_FLOAT - 1;
                    Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: %s - Se cambia por: %f %n", analizadorLexico.getNroLinea(), cte, flotante));
                }
            } catch (NumberFormatException e) {
                Errores.addError(String.format("[AS] | Linea %d: Error en la constante numerica", analizadorLexico.getNroLinea()));
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
        if (uso.equals(Main.PROCEDIMIENTO))
            nuevoLexema = lexema + "@" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length()) - (lexema.length() + 1));
        else
            nuevoLexema = lexema + "@" + ambitos.getAmbitos();

        if (!TablaSimbolos.existe(nuevoLexema)) {
            Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
            nuevoToken.addAtributo("uso", uso);
            nuevoToken.addAtributo("tipo", tipo);
            nuevoToken.addAtributo("contador", 0);
            nuevoToken.addAtributo("ambito", nuevoLexema.substring(lexema.length() + 1, nuevoLexema.length()));
            if (uso.equals(Main.PROCEDIMIENTO)) {
                this.numberOfProcs++;
                nuevoToken.addAtributo("numeroProc", numberOfProcs);
            }
            TablaSimbolos.add(nuevoToken);
        } else {
            String usoTokenExistente = TablaSimbolos.getToken(nuevoLexema).getAtributo("uso") + "";
            if (uso.equals(Main.VARIABLE)) {
                if (usoTokenExistente.equals(Main.VARIABLE))
                    Errores.addError(String.format("[ASem] | Linea %d: Variable redeclarada %n", analizadorLexico.getNroLinea()));
                else if (usoTokenExistente.equals(Main.PROCEDIMIENTO))
                    Errores.addError(String.format("[ASem] | Linea %d: El nombre de la variable declarada pertenece a un procedimiento declarado en el mismo ambito %n", analizadorLexico.getNroLinea()));
            } else if (uso.equals(Main.PROCEDIMIENTO)) {
                if (usoTokenExistente.equals(Main.PROCEDIMIENTO))
                    Errores.addError(String.format("[ASem] | Linea %d: Procedimiento redeclarado %n", analizadorLexico.getNroLinea()));
                else if (usoTokenExistente.equals(Main.VARIABLE))
                    Errores.addError(String.format("[ASem] | Linea %d: El nombre del procedimiento declarado pertenece a una variable declarada en el mismo ambito %n", analizadorLexico.getNroLinea()));
            }
        }
    }

    public String getAmbitoDeclaracionID(String lexema, String uso) {
        // Se chequea que el id esté declarado en el ámbito actual o en los ámbitos que lo contienen
        String ambitosString = ambitos.getAmbitos();
        ArrayList<String> ambitosList = new ArrayList<>(Arrays.asList(ambitosString.split("@")));

//    if(uso.equals(Main.PROCEDIMIENTO) && !ambitosString.equals("main")) {
//        ambitosString = ambitosString.substring(0, (ambitosString.length()) - (ambitosList.get(ambitosList.size() - 1).length() + 1));
//        ambitosList.remove(ambitosList.size()-1);
//    }

        boolean declarado = false;
        while (!ambitosList.isEmpty()) {
            if (TablaSimbolos.existe(lexema + "@" + ambitosString)) {
                if ((uso.equals(Main.PARAMETRO) && !TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals(Main.PROCEDIMIENTO)) || !uso.equals(Main.PARAMETRO)) {
                    declarado = true;
                    //if(!uso.equals(Main.PROCEDIMIENTO))
                    //  actualizarContadorID(lexema + "@" + ambitosString, false);
                    break;
                } else if (TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals(Main.PROCEDIMIENTO)) {
                    Errores.addError(String.format("[ASem] | Linea %d: No es posible pasar un procedimiento como parametro %n", analizadorLexico.getNroLinea()));
                    break;
                }
            } else {
                if (!ambitosString.equals("main")) {
                    ambitosString = ambitosString.substring(0, (ambitosString.length() - (ambitosList.get(ambitosList.size() - 1).length() + 1)));
                    ambitosList.remove(ambitosList.size() - 1);
                } else break;
            }
        }

        if (!declarado) ambitosString = "";

        return ambitosString;
    }

    public void invocacionID(String lexema, String uso) {
        String ambitosString = getAmbitoDeclaracionID(lexema, uso);
        boolean declarado = (ambitosString.isEmpty()) ? false : true;

        if (!declarado) {
            if (uso.equals(Main.VARIABLE))
                Errores.addError(String.format("[ASem] | Linea %d: Variable %s no declarada %n", analizadorLexico.getNroLinea(), lexema));
            else if (uso.equals(Main.PROCEDIMIENTO))
                Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
            else if (uso.equals(Main.PARAMETRO))
                Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
        }

        Token token = TablaSimbolos.getToken(lexema + "@" + ambitosString);
        if (token != null) {
            String uso_real = token.getAtributo("uso") != null ? token.getAtributo("uso").toString() : "";
            if (uso_real.equals(Main.VARIABLE) && uso.equals(Main.PROCEDIMIENTO)) {
                Errores.addError(String.format("[ASem] | Linea %d: La variable \"%s\" no es un procedimiento %n", analizadorLexico.getNroLinea(), lexema));
                TablaSimbolos.remove(lexema);
            }
        }

        if (uso.equals(Main.PARAMETRO))
            parametrosReales.add(lexema + "@" + ambitosString);

        if (uso.equals(Main.PROCEDIMIENTO) && declarado) {
            Token procedimiento = TablaSimbolos.getToken(lexema + "@" + ambitosString);

            Token padre = null;
            if (!ambitosString.equals("main")) {
                String lexemaPadre = ambitosString.split("@")[ambitosString.split("@").length - 1];
                lexemaPadre = lexemaPadre + "@" + ambitosString.substring(0, (ambitosString.length()) - (lexemaPadre.length() + 1));
                padre = TablaSimbolos.getToken(lexemaPadre);
            }
            procedimiento.addAtributo("padre", (padre != null) ? padre.getAtributo("numeroProc") : 0);

            // Si se trata de un procedimiento que se encuentra declarado, se chequea además que la cantidad de parámetros reales correspondan a los formales
            List<String> parametrosFormales = (List) procedimiento.getAtributo("parametros");
            if (parametrosFormales == null) return;
            if (parametrosReales.size() != parametrosFormales.size())
                Errores.addError(String.format("[ASem] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
                // Se chequea, por último, los tipos entre parámetros reales y formales
                boolean tiposCompatibles = true;
                for (int i = 0; i < parametrosReales.size(); i++) {
                    Token parametroReal = TablaSimbolos.getToken(parametrosReales.get(i));
                    if (parametroReal != null) {
                        String tipoParametroReal = parametroReal.getAtributo("tipo") + "";
                        if (!parametrosFormales.get(i).contains(tipoParametroReal)) {
                            Errores.addError(String.format("[ASem] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i + 1));
                            tiposCompatibles = false;
                            break;
                        }
                    } else
                        tiposCompatibles = false;
                }

                if (tiposCompatibles) {
                    SA6(lexema, parametrosFormales, parametrosReales);
                    actualizarContadorID(lexema + "@" + ambitosString, false);
                }
            }
            parametrosReales.clear();
        }

        if (declarado && !uso.equals(Main.PROCEDIMIENTO))
            actualizarContadorID(lexema + "@" + ambitosString, false);
        // Se actualiza el contador de referencias
        actualizarContadorID(lexema, true);
    }

    public String getLexemaID() {
        String ambitosActuales = ambitos.getAmbitos();
        String id = ambitosActuales.split("@")[ambitosActuales.split("@").length - 1];
        return (id + "@" + ambitosActuales.substring(0, (ambitosActuales.length()) - (id.length() + 1)));
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
        else {
            String lexemaToken = token.getLexema(false);
            if (token.getAtributo("uso") != null && token.getAtributo("uso").equals(Main.PROCEDIMIENTO)) {
                String id = lexemaToken.split("@")[0];
                lexemaToken = lexemaToken.replace(id + "@", "");
                lexemaToken += "@" + id;
            }
            Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), (lexemaToken.contains("@") && !token.getAtributo("uso").equals(Main.PROCEDIMIENTO)) ? lexemaToken.substring(0, lexemaToken.indexOf("@")) : lexemaToken);
            for (Map.Entry<String, Object> atributo : token.getAtributos().entrySet()) {
                nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
            }
            elem = new ElemSimple(nuevoToken, analizadorLexico.getNroLinea());
        }

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
        Token cte_t = TablaSimbolos.getToken(cte);
        if (cte_t != null)
            if (!cte_t.getAtributo("tipo").equals("LONGINT"))
                Errores.addError(String.format("[ASem] | Linea %d: Constante no es del tipo entero %n", analizadorLexico.getNroLinea()));
    }

    public void SA4(String id1, String id2) { //reviso que la variable inicializada en el for sea la misma que la de la condicion
        Token token1 = TablaSimbolos.getToken(id1 + "@" + getAmbitoDeclaracionID(id1, Main.VARIABLE));
        Token token2 = TablaSimbolos.getToken(id2 + "@" + getAmbitoDeclaracionID(id2, Main.VARIABLE));
        if (token1 != null && token2 != null)
            if (!token1.equals(token2))
                Errores.addError(String.format("[AS] | Linea %d: En la sentencia for, la variable inicializada " + id1 + " no es la misma que la variable utilizada en la condicion %n", analizadorLexico.getNroLinea()));
    }

    public void SA5(String id, String cte, String op) { //incremento o decremento la variable del for
        Token id_t = TablaSimbolos.getToken(id + "@" + getAmbitoDeclaracionID(id, Main.VARIABLE));
        Token cte_t = TablaSimbolos.getToken(cte);
        if (id_t != null) {
            String lexemaToken = id_t.getLexema(false);
            Token nuevoToken = new Token(id_t.getIdToken(), id_t.getTipoToken(), lexemaToken.substring(0, lexemaToken.indexOf("@")));
            for (Map.Entry<String, Object> atributo : id_t.getAtributos().entrySet()) {
                nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
            }

            if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                polaca.addElem(new ElemSimple(nuevoToken), false);
                polaca.addElem(new ElemSimple(cte_t), false);
                polaca.addElem(new OperadorBinario(op), false);
                polaca.addElem(new ElemSimple(nuevoToken), false);
                polaca.addElem(new OperadorBinario("="), false);
            } else {
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

        for (int i = 0; i < parametrosFormales.size(); i++) {
            String parametroFormal = parametrosFormales.get(i);

            parametroFormal = parametroFormal.replace("LONGINT ", "");
            parametroFormal = parametroFormal.replace("FLOAT ", "");

            if (parametroFormal.contains("VAR ")) {
                parametroFormal = parametroFormal.replace("VAR ", "");
                parametrosCVR.add(parametroFormal + "@" + parametrosReales.get(i));
            }

            parametroFormal = parametroFormal + "@" + getAmbitoDeclaracionID(lexema, Main.PROCEDIMIENTO) + "@" + lexema;
            SA1(parametrosReales.get(i));
            SA1(parametroFormal);
            SA2("=");
        }

        SA1(lexema);
        if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
            polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.INV), false);
        else
            polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.INV), false);

        if (!parametrosCVR.isEmpty()) {
            for (String parametroCVR : parametrosCVR
            ) {
                String[] param = parametroCVR.split("@");
                SA1(param[0] + "@" + getAmbitoDeclaracionID(lexema, Main.PROCEDIMIENTO) + "@" + lexema);
                SA1(param[1]);
                SA2("=");
            }
        }
    }

    //#line 1071 "Parser.java"
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
//#line 40 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 5:
//#line 41 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 19:
//#line 68 "gramatica.y"
                {
                    imprimirReglaReconocida("Declaración de variables", analizadorLexico.getNroLinea());
                }
                break;
                case 20:
//#line 69 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 21:
//#line 70 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lista de variables %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 22:
//#line 73 "gramatica.y"
                {
                    declaracionID(val_peek(2).sval, Main.VARIABLE, ultimoTipo);
                }
                break;
                case 23:
//#line 74 "gramatica.y"
                {
                    declaracionID(val_peek(0).sval, Main.VARIABLE, ultimoTipo);
                }
                break;
                case 24:
//#line 75 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 25:
//#line 78 "gramatica.y"
                {
                    ultimoTipo = "LONGINT";
                }
                break;
                case 26:
//#line 79 "gramatica.y"
                {
                    ultimoTipo = "FLOAT";
                }
                break;
                case 27:
//#line 82 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                    ambitos.eliminarAmbito(actualizarTablaSimbolos);
                }
                break;
                case 28:
//#line 85 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta declarar el número de invocaciones permitido en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 29:
//#line 86 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan declarar los parametros en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 30:
//#line 89 "gramatica.y"
                {
                    ambitos.agregarAmbito(val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PROCEDIMIENTO, null);
                }
                break;
                case 31:
//#line 92 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 32:
//#line 96 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                        Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                    else {
                        String lexemaID = getLexemaID();
                        try {
                            if (lexemaID != null)
                                TablaSimbolos.getToken(lexemaID).addAtributo("max. invocaciones", Integer.parseInt(cte));
                        } catch (NumberFormatException e) {
                            Errores.addError(String.format("[AS] | Linea %d: Error en la constante numerica", analizadorLexico.getNroLinea()));
                        }
                    }
                }
                break;
                case 33:
//#line 105 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 34:
//#line 106 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 35:
//#line 107 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 36:
//#line 108 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI y literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 38:
//#line 112 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 39:
//#line 113 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en las sentencias del cuerpo del procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 40:
//#line 114 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el cuerpo del procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 41:
//#line 117 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 42:
//#line 123 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 43:
//#line 129 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 44:
//#line 135 "gramatica.y"
                {
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                }
                break;
                case 45:
//#line 138 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 46:
//#line 139 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 47:
//#line 140 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 48:
//#line 141 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 49:
//#line 142 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 50:
//#line 145 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PARAMETRO, ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                }
                break;
                case 51:
//#line 150 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PARAMETRO, ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                }
                break;
                case 52:
//#line 155 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 53:
//#line 156 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 54:
//#line 159 "gramatica.y"
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
                case 55:
//#line 169 "gramatica.y"
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
                case 56:
//#line 179 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 57:
//#line 180 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 58:
//#line 181 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 59:
//#line 182 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 60:
//#line 183 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 61:
//#line 184 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 62:
//#line 185 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 63:
//#line 186 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 64:
//#line 187 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", nroUltimaLinea));
                }
                break;
                case 65:
//#line 188 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 66:
//#line 189 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 67:
//#line 190 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 68:
//#line 191 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 69:
//#line 192 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 70:
//#line 193 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 71:
//#line 196 "gramatica.y"
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
                case 72:
//#line 205 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 73:
//#line 206 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 74:
//#line 207 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 75:
//#line 208 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 76:
//#line 209 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 77:
//#line 213 "gramatica.y"
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
                case 79:
//#line 231 "gramatica.y"
                {
                    SA3(val_peek(2).sval);
                    SA4(val_peek(5).sval, val_peek(4).sval);
                    SA5(val_peek(5).sval, val_peek(2).sval, val_peek(3).sval); /* id cte incr_decr*/
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
                case 80:
//#line 247 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 81:
//#line 248 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 82:
//#line 249 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 83:
//#line 250 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 84:
//#line 251 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 85:
//#line 252 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 86:
//#line 253 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 87:
//#line 254 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 88:
//#line 257 "gramatica.y"
                {
                    yyval = val_peek(3);
                    SA3(val_peek(1).sval);
                    SA1(val_peek(1).sval);
                    SA1(val_peek(3).sval);
                    SA2(val_peek(2).sval);
                    SA1(val_peek(3).sval);
                    invocacionID(val_peek(3).sval, Main.VARIABLE);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal)) {
                        polaca.pushPos(false);
                        polaca.addElem(new EtiquetaElem(polaca.size()), false);
                    } else {
                        polacaProcedimientos.pushPos(ambitos.getAmbitos(), false);
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new EtiquetaElem(polacaProcedimientos.getPolacaSize(ambitos.getAmbitos())), false);
                    }
                }
                break;
                case 89:
//#line 273 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 90:
//#line 274 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 91:
//#line 275 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 92:
//#line 276 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 93:
//#line 277 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 94:
//#line 278 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 97:
//#line 283 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 98:
//#line 284 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n", nroUltimaLinea));
                }
                break;
                case 99:
//#line 287 "gramatica.y"
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
                case 100:
//#line 299 "gramatica.y"
                {
                    yyval = new ParserVal("+");
                }
                break;
                case 101:
//#line 300 "gramatica.y"
                {
                    yyval = new ParserVal("-");
                }
                break;
                case 102:
//#line 303 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                    SA1(val_peek(2).sval);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                    else
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 103:
//#line 310 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 104:
//#line 311 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 105:
//#line 312 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 106:
//#line 313 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 107:
//#line 314 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 108:
//#line 315 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                    /*                                                        invocacionID($3.sval, Main.VARIABLE);*/
                    /*                                                        SA1($3.sval);*/
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                    else
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 109:
//#line 324 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 110:
//#line 329 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de asignación", analizadorLexico.getNroLinea());
                    String id = val_peek(3).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        SA1(id);
                        SA2(val_peek(2).sval);
                    }
                    invocacionID(id, Main.VARIABLE);
                }
                break;
                case 111:
//#line 338 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 112:
//#line 339 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 113:
//#line 340 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 114:
//#line 341 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 115:
//#line 344 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, Main.PROCEDIMIENTO);
                }
                break;
                case 116:
//#line 347 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación sin parámetros", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(3).sval, Main.PROCEDIMIENTO);
                }
                break;
                case 117:
//#line 350 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 118:
//#line 351 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 119:
//#line 352 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 120:
//#line 353 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 121:
//#line 354 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 122:
//#line 355 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 123:
//#line 356 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 124:
//#line 359 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, Main.PARAMETRO);
                    invocacionID(val_peek(2).sval, Main.PARAMETRO);
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 125:
//#line 364 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (2)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(2).sval, Main.PARAMETRO);
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 126:
//#line 368 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (1)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 127:
//#line 371 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 128:
//#line 372 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 129:
//#line 373 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 130:
//#line 374 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 131:
//#line 375 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 132:
//#line 376 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 133:
//#line 381 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(val_peek(1).sval);
                }
                break;
                case 134:
//#line 382 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta segundo operando de la comparacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 135:
//#line 383 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta primer operando de la comparacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 136:
//#line 387 "gramatica.y"
                {
                    yyval = new ParserVal(">=");
                }
                break;
                case 137:
//#line 388 "gramatica.y"
                {
                    yyval = new ParserVal("<=");
                }
                break;
                case 138:
//#line 389 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 139:
//#line 390 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 140:
//#line 391 "gramatica.y"
                {
                    yyval = new ParserVal("==");
                }
                break;
                case 141:
//#line 392 "gramatica.y"
                {
                    yyval = new ParserVal("!=");
                }
                break;
                case 142:
//#line 394 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 143:
//#line 396 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 144:
//#line 398 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 145:
//#line 399 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 146:
//#line 400 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la resta %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 147:
//#line 401 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 148:
//#line 404 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 149:
//#line 407 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 150:
//#line 410 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 151:
//#line 411 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 152:
//#line 412 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el segundo operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 153:
//#line 413 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 154:
//#line 414 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el primer operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 155:
//#line 417 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                    invocacionID(val_peek(0).sval, Main.VARIABLE);
                }
                break;
                case 156:
//#line 419 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 157:
//#line 422 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 158:
//#line 429 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                    }
                }
                break;
//#line 1939 "Parser.java"
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
