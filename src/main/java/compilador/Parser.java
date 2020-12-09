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
            7, 15, 15, 15, 14, 14, 8, 8, 16, 16,
            18, 18, 18, 18, 19, 19, 19, 17, 17, 17,
            17, 17, 17, 17, 17, 17, 20, 20, 20, 20,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 21, 21, 21,
            21, 21, 21, 22, 23, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 25, 25, 25, 25, 25, 25,
            25, 28, 28, 28, 28, 26, 27, 27, 11, 11,
            11, 11, 11, 11, 11, 11, 12, 12, 12, 12,
            12, 13, 13, 13, 13, 13, 13, 13, 13, 13,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 24,
            24, 24, 24, 24, 24, 31, 31, 31, 31, 31,
            31, 33, 33, 33, 33, 33, 33, 33, 30, 30,
            29, 29,
    };
    final static short yylen[] = {2,
            1, 2, 1, 1, 2, 3, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 3, 3,
            2, 3, 1, 2, 1, 1, 4, 3, 2, 2,
            3, 2, 3, 3, 3, 3, 3, 7, 5, 3,
            2, 6, 6, 4, 9, 5, 2, 3, 2, 3,
            6, 8, 5, 7, 7, 5, 7, 6, 8, 5,
            5, 7, 8, 7, 7, 5, 7, 3, 2, 2,
            2, 1, 3, 1, 1, 8, 7, 8, 9, 8,
            8, 7, 8, 8, 4, 4, 4, 2, 3, 3,
            3, 3, 1, 3, 3, 2, 1, 1, 5, 5,
            4, 5, 5, 4, 5, 4, 4, 4, 4, 4,
            3, 5, 4, 4, 3, 5, 4, 3, 4, 3,
            5, 3, 1, 7, 3, 3, 4, 4, 2, 3,
            3, 3, 3, 3, 3, 3, 3, 1, 3, 3,
            3, 3, 3, 1, 3, 3, 3, 3, 1, 1,
            1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 0, 149, 151, 0, 0, 0,
            0, 150, 144, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 30, 29,
            8, 21, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 74, 7, 0, 0, 71, 0, 152, 0, 0,
            69, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 88, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 118, 0, 0, 115, 0, 0, 24, 20,
            19, 0, 0, 41, 0, 0, 0, 0, 0, 0,
            0, 28, 108, 0, 0, 147, 148, 0, 0, 0,
            73, 68, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 145, 142,
            146, 143, 0, 90, 0, 91, 0, 89, 0, 0,
            0, 0, 96, 97, 98, 0, 0, 0, 0, 0,
            101, 0, 109, 126, 0, 125, 0, 110, 107, 0,
            113, 117, 0, 114, 22, 49, 0, 0, 47, 0,
            40, 0, 0, 0, 0, 0, 0, 32, 27, 6,
            75, 0, 66, 0, 0, 0, 56, 0, 0, 0,
            0, 0, 53, 86, 87, 85, 0, 0, 0, 0,
            0, 0, 100, 102, 103, 99, 105, 128, 127, 0,
            116, 112, 50, 48, 0, 0, 0, 44, 36, 33,
            34, 31, 37, 35, 0, 0, 58, 0, 0, 0,
            0, 51, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 46, 0, 39, 0, 0, 67, 0, 57,
            0, 62, 64, 0, 55, 54, 0, 0, 0, 0,
            0, 0, 0, 0, 93, 82, 77, 0, 0, 43,
            42, 59, 63, 52, 78, 84, 0, 80, 81, 0,
            3, 4, 0, 83, 76, 124, 0, 38, 79, 94,
            2, 5, 0, 92, 0, 45,
    };
    final static short yydgoto[] = {9,
            10, 300, 11, 12, 201, 63, 14, 15, 16, 17,
            18, 19, 20, 21, 54, 22, 56, 121, 122, 116,
            30, 64, 202, 90, 40, 91, 166, 286, 32, 33,
            34, 48, 35,
    };
    final static short yysindex[] = {712,
            -26, -32, -24, -36, 0, 0, -29, -152, 0, 712,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -116, -15, 269, 396, 614, 0, 0, 154, -229, 560,
            36, 0, 0, 525, 158, 6, -46, -202, 356, 269,
            -171, 147, 269, -25, 290, -3, 51, 80, 0, 0,
            0, 0, -16, -53, -19, -7, 538, 295, 49, 49,
            712, 0, 0, 53, 430, 0, 91, 0, 531, 141,
            0, 269, 269, 269, 269, 269, 269, 330, 335, 343,
            404, -120, 0, 106, 157, 110, 151, 269, 407, 117,
            45, 146, 156, -39, 149, 173, 587, 3, -114, 116,
            596, 207, 0, 193, -14, 0, 250, 42, 0, 0,
            0, 65, -96, 0, 85, -41, 712, -31, 723, 127,
            -100, 0, 0, 246, 158, 0, 0, 483, 614, 301,
            0, 0, -56, 614, 357, 543, 614, 362, 366, 366,
            366, 366, 366, 366, 246, 158, 246, 158, 0, 0,
            0, 0, 365, 0, 371, 0, 388, 0, 45, 45,
            473, -187, 0, 0, 0, 176, 392, 394, 395, 402,
            0, 417, 0, 0, 195, 0, 5, 0, 0, 427,
            0, 0, 432, 0, 0, 0, 227, 229, 0, 200,
            0, 136, 637, 234, -150, -30, 648, 0, 0, 0,
            0, 252, 0, 614, 462, 257, 0, -26, 571, 464,
            262, 263, 0, 0, 0, 0, 258, 261, 45, 276,
            -131, 494, 0, 0, 0, 0, 0, 0, 0, 286,
            0, 0, 0, 0, -27, -34, 249, 0, 0, 0,
            0, 0, 0, 0, 506, 311, 0, 521, -44, 537,
            -232, 0, 539, 541, 549, 562, 312, 563, 564, 367,
            665, 565, 0, 249, 0, 576, 581, 0, 577, 0,
            578, 0, 0, 583, 0, 0, 665, 665, 599, 665,
            665, 495, 712, 625, 0, 0, 0, 389, 145, 0,
            0, 0, 0, 0, 0, 0, 665, 0, 0, 664,
            0, 0, 685, 0, 0, 0, -15, 0, 0, 0,
            0, 0, -26, 0, 603, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 652,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            582, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 30, 0, 0, 0, 0, 0, 0,
            0, 0, -50, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 602, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 55, 0, 0,
            120, 0, 0, 171, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 21, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 131, 387,
            403, 426, 452, 472, 41, 68, 88, 108, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 194,
            0, 218, 0, 0, 0, 0, 56, 0, 0, 0,
            0, 0, 238, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 273, 0, 289,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 58, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 308, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 355, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            -43, 385, -99, 310, 4, 26, 0, 0, 0, 0,
            0, 0, 0, 534, -33, 0, 363, 0, 550, 398,
            0, 29, -86, 37, 641, 408, -107, 683, 600, -2,
            62, 635, 14,
    };
    final static int YYTABLESIZE = 997;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{191,
                138, 170, 190, 42, 23, 111, 265, 28, 23, 264,
                46, 47, 29, 263, 85, 39, 23, 128, 99, 109,
                141, 114, 119, 273, 55, 13, 183, 108, 62, 195,
                23, 45, 274, 62, 23, 51, 38, 104, 31, 96,
                139, 138, 68, 138, 182, 138, 175, 206, 230, 211,
                212, 217, 218, 120, 221, 103, 126, 127, 70, 138,
                138, 141, 138, 141, 67, 141, 82, 136, 220, 86,
                123, 125, 62, 193, 185, 197, 71, 150, 152, 141,
                141, 139, 141, 139, 57, 139, 13, 140, 123, 164,
                165, 146, 148, 29, 243, 129, 122, 136, 121, 139,
                139, 92, 139, 49, 97, 241, 101, 137, 136, 106,
                136, 257, 136, 129, 122, 119, 121, 246, 50, 111,
                107, 242, 251, 138, 259, 138, 136, 136, 140, 136,
                140, 132, 140, 139, 140, 141, 142, 143, 144, 52,
                260, 176, 13, 141, 13, 141, 140, 140, 137, 140,
                137, 153, 137, 51, 53, 117, 177, 59, 58, 187,
                285, 285, 60, 139, 154, 139, 137, 137, 158, 137,
                120, 134, 5, 6, 178, 163, 238, 285, 285, 237,
                285, 285, 301, 301, 285, 308, 167, 95, 307, 134,
                136, 29, 136, 104, 66, 29, 168, 285, 29, 80,
                311, 29, 110, 311, 81, 23, 204, 171, 205, 83,
                140, 82, 140, 172, 112, 156, 169, 106, 51, 41,
                271, 112, 51, 24, 194, 84, 43, 5, 6, 25,
                137, 36, 137, 113, 5, 6, 112, 119, 26, 27,
                113, 44, 111, 186, 111, 98, 37, 180, 117, 5,
                6, 181, 102, 134, 53, 113, 138, 138, 138, 138,
                138, 138, 138, 138, 138, 138, 138, 44, 118, 138,
                138, 138, 61, 174, 138, 229, 141, 141, 141, 141,
                141, 141, 141, 141, 141, 141, 141, 59, 60, 141,
                141, 141, 60, 120, 141, 120, 139, 139, 139, 139,
                139, 139, 139, 139, 139, 139, 139, 65, 184, 139,
                139, 139, 53, 29, 139, 129, 104, 130, 104, 26,
                27, 164, 165, 136, 136, 136, 136, 136, 136, 136,
                136, 136, 136, 136, 29, 186, 136, 136, 136, 29,
                106, 136, 106, 140, 140, 140, 140, 140, 140, 140,
                140, 140, 140, 140, 95, 189, 140, 140, 140, 203,
                119, 140, 119, 137, 137, 137, 137, 137, 137, 137,
                137, 137, 137, 137, 29, 111, 137, 137, 137, 29,
                111, 137, 111, 111, 111, 111, 134, 29, 111, 111,
                111, 134, 134, 111, 134, 61, 134, 61, 198, 134,
                134, 134, 93, 137, 134, 138, 24, 284, 78, 65,
                79, 60, 155, 60, 88, 207, 38, 26, 27, 94,
                213, 26, 27, 214, 26, 27, 120, 130, 27, 215,
                65, 120, 65, 120, 120, 120, 120, 59, 58, 120,
                120, 120, 60, 131, 120, 130, 216, 222, 29, 104,
                223, 29, 224, 225, 104, 235, 104, 104, 104, 104,
                226, 131, 104, 104, 104, 228, 135, 104, 5, 6,
                131, 59, 58, 106, 113, 227, 60, 95, 106, 95,
                106, 106, 106, 106, 135, 231, 106, 106, 106, 283,
                232, 106, 132, 119, 159, 160, 162, 233, 119, 234,
                119, 119, 119, 119, 112, 240, 119, 119, 119, 130,
                132, 119, 133, 192, 59, 58, 245, 5, 6, 60,
                247, 248, 252, 113, 24, 131, 253, 254, 61, 255,
                133, 219, 256, 61, 261, 61, 61, 61, 61, 26,
                27, 61, 61, 61, 60, 100, 61, 258, 135, 60,
                124, 60, 60, 60, 60, 23, 262, 60, 60, 60,
                26, 27, 60, 65, 268, 26, 27, 78, 65, 79,
                65, 65, 65, 65, 132, 269, 65, 65, 65, 270,
                78, 65, 79, 279, 77, 145, 76, 236, 115, 277,
                147, 302, 302, 304, 133, 272, 123, 275, 149, 276,
                26, 27, 278, 280, 281, 26, 27, 200, 288, 312,
                95, 87, 312, 26, 27, 95, 290, 95, 95, 95,
                95, 291, 282, 95, 95, 95, 37, 2, 95, 78,
                3, 79, 4, 266, 267, 292, 293, 7, 78, 297,
                79, 294, 130, 316, 306, 173, 188, 130, 130, 115,
                130, 1, 130, 61, 179, 130, 130, 130, 131, 151,
                130, 289, 161, 131, 131, 61, 131, 303, 131, 315,
                199, 131, 131, 131, 26, 27, 131, 26, 27, 89,
                105, 135, 61, 0, 157, 0, 135, 135, 0, 135,
                0, 135, 0, 61, 135, 135, 135, 0, 0, 135,
                0, 0, 0, 0, 72, 0, 0, 132, 0, 0,
                0, 0, 132, 132, 0, 132, 0, 132, 0, 0,
                132, 132, 132, 115, 70, 132, 0, 133, 0, 0,
                0, 0, 133, 133, 0, 133, 61, 133, 1, 0,
                133, 133, 133, 2, 0, 133, 3, 283, 4, 0,
                1, 5, 6, 7, 0, 2, 8, 0, 3, 0,
                4, 239, 0, 5, 6, 7, 0, 0, 8, 115,
                115, 0, 244, 0, 0, 0, 0, 0, 0, 0,
                0, 72, 73, 74, 75, 0, 133, 283, 310, 0,
                0, 2, 0, 134, 3, 135, 4, 115, 208, 5,
                6, 7, 0, 2, 8, 209, 3, 210, 4, 314,
                0, 5, 6, 7, 0, 1, 8, 0, 0, 0,
                2, 69, 0, 3, 0, 4, 249, 0, 5, 6,
                7, 2, 0, 8, 3, 250, 4, 72, 0, 5,
                6, 7, 72, 72, 8, 72, 0, 72, 0, 0,
                72, 72, 72, 0, 0, 72, 0, 70, 0, 0,
                0, 0, 70, 70, 0, 70, 0, 70, 0, 1,
                70, 70, 70, 0, 2, 70, 0, 3, 0, 4,
                282, 0, 5, 6, 7, 2, 0, 8, 3, 0,
                4, 0, 1, 5, 6, 7, 0, 2, 8, 0,
                3, 0, 4, 1, 0, 5, 6, 7, 2, 0,
                8, 3, 0, 4, 0, 0, 5, 6, 7, 1,
                282, 8, 0, 0, 2, 2, 0, 3, 3, 4,
                4, 0, 5, 6, 7, 7, 0, 8, 0, 0,
                313, 0, 0, 287, 0, 2, 0, 0, 3, 0,
                4, 0, 0, 5, 6, 7, 0, 0, 8, 295,
                296, 0, 298, 299, 0, 0, 305, 1, 0, 0,
                0, 0, 2, 0, 0, 3, 0, 4, 196, 309,
                5, 6, 7, 2, 0, 8, 3, 0, 4, 0,
                0, 5, 6, 7, 0, 0, 8,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{41,
                0, 41, 44, 40, 61, 59, 41, 40, 59, 44,
                40, 41, 45, 41, 61, 40, 61, 61, 44, 53,
                0, 41, 123, 256, 40, 0, 41, 44, 25, 61,
                61, 61, 265, 30, 61, 10, 61, 41, 2, 42,
                0, 41, 272, 43, 59, 45, 44, 134, 44, 136,
                137, 159, 160, 61, 162, 59, 59, 60, 30, 59,
                60, 41, 62, 43, 28, 45, 61, 0, 256, 272,
                41, 58, 69, 117, 108, 119, 41, 80, 81, 59,
                60, 41, 62, 43, 23, 45, 61, 0, 59, 277,
                278, 78, 79, 45, 125, 41, 41, 69, 41, 59,
                60, 273, 62, 256, 43, 256, 45, 0, 41, 59,
                43, 219, 45, 59, 59, 123, 59, 204, 271, 0,
                41, 272, 209, 123, 256, 125, 59, 60, 41, 62,
                43, 41, 45, 72, 73, 74, 75, 76, 77, 256,
                272, 256, 117, 123, 119, 125, 59, 60, 41, 62,
                43, 272, 45, 128, 271, 256, 271, 42, 43, 256,
                260, 261, 47, 123, 59, 125, 59, 60, 59, 62,
                0, 41, 269, 270, 59, 59, 41, 277, 278, 44,
                280, 281, 282, 283, 284, 41, 41, 41, 44, 59,
                123, 45, 125, 0, 41, 45, 41, 297, 45, 42,
                300, 45, 256, 303, 47, 256, 263, 59, 265, 256,
                123, 61, 125, 41, 256, 59, 256, 0, 193, 256,
                265, 256, 197, 256, 256, 272, 256, 269, 270, 262,
                123, 256, 125, 275, 269, 270, 256, 0, 271, 272,
                275, 271, 123, 271, 125, 271, 271, 41, 256, 269,
                270, 59, 256, 123, 271, 275, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 271, 276, 269,
                270, 271, 0, 271, 274, 271, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 42, 0, 269,
                270, 271, 47, 123, 274, 125, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 0, 59, 269,
                270, 271, 271, 45, 274, 263, 123, 265, 125, 271,
                272, 277, 278, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 45, 271, 269, 270, 271, 45,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 0, 271, 269, 270, 271, 59,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 45, 256, 269, 270, 271, 45,
                261, 274, 263, 264, 265, 266, 256, 45, 269, 270,
                271, 261, 262, 274, 264, 123, 266, 125, 272, 269,
                270, 271, 256, 263, 274, 265, 256, 41, 43, 256,
                45, 123, 256, 125, 59, 59, 61, 271, 272, 273,
                59, 271, 272, 59, 271, 272, 256, 41, 272, 59,
                123, 261, 125, 263, 264, 265, 266, 42, 43, 269,
                270, 271, 47, 41, 274, 59, 59, 272, 45, 256,
                59, 45, 59, 59, 261, 256, 263, 264, 265, 266,
                59, 59, 269, 270, 271, 271, 41, 274, 269, 270,
                41, 42, 43, 256, 275, 59, 47, 123, 261, 125,
                263, 264, 265, 266, 59, 59, 269, 270, 271, 123,
                59, 274, 41, 256, 87, 88, 89, 271, 261, 271,
                263, 264, 265, 266, 256, 272, 269, 270, 271, 123,
                59, 274, 41, 116, 42, 43, 265, 269, 270, 47,
                59, 265, 59, 275, 256, 123, 265, 265, 256, 272,
                59, 59, 272, 261, 41, 263, 264, 265, 266, 271,
                272, 269, 270, 271, 256, 256, 274, 272, 123, 261,
                256, 263, 264, 265, 266, 61, 271, 269, 270, 271,
                271, 272, 274, 256, 59, 271, 272, 43, 261, 45,
                263, 264, 265, 266, 123, 265, 269, 270, 271, 59,
                43, 274, 45, 272, 60, 256, 62, 190, 55, 41,
                256, 282, 283, 284, 123, 59, 59, 59, 256, 59,
                271, 272, 41, 41, 41, 271, 272, 125, 44, 300,
                256, 256, 303, 271, 272, 261, 41, 263, 264, 265,
                266, 41, 256, 269, 270, 271, 271, 261, 274, 43,
                264, 45, 266, 236, 237, 59, 59, 271, 43, 41,
                45, 59, 256, 41, 256, 59, 113, 261, 262, 116,
                264, 0, 266, 123, 59, 269, 270, 271, 256, 256,
                274, 264, 256, 261, 262, 123, 264, 283, 266, 307,
                121, 269, 270, 271, 271, 272, 274, 271, 272, 39,
                46, 256, 123, -1, 85, -1, 261, 262, -1, 264,
                -1, 266, -1, 123, 269, 270, 271, -1, -1, 274,
                -1, -1, -1, -1, 123, -1, -1, 256, -1, -1,
                -1, -1, 261, 262, -1, 264, -1, 266, -1, -1,
                269, 270, 271, 190, 123, 274, -1, 256, -1, -1,
                -1, -1, 261, 262, -1, 264, 123, 266, 256, -1,
                269, 270, 271, 261, -1, 274, 264, 123, 266, -1,
                256, 269, 270, 271, -1, 261, 274, -1, 264, -1,
                266, 125, -1, 269, 270, 271, -1, -1, 274, 236,
                237, -1, 125, -1, -1, -1, -1, -1, -1, -1,
                -1, 257, 258, 259, 260, -1, 256, 123, 125, -1,
                -1, 261, -1, 263, 264, 265, 266, 264, 256, 269,
                270, 271, -1, 261, 274, 263, 264, 265, 266, 125,
                -1, 269, 270, 271, -1, 256, 274, -1, -1, -1,
                261, 262, -1, 264, -1, 266, 256, -1, 269, 270,
                271, 261, -1, 274, 264, 265, 266, 256, -1, 269,
                270, 271, 261, 262, 274, 264, -1, 266, -1, -1,
                269, 270, 271, -1, -1, 274, -1, 256, -1, -1,
                -1, -1, 261, 262, -1, 264, -1, 266, -1, 256,
                269, 270, 271, -1, 261, 274, -1, 264, -1, 266,
                256, -1, 269, 270, 271, 261, -1, 274, 264, -1,
                266, -1, 256, 269, 270, 271, -1, 261, 274, -1,
                264, -1, 266, 256, -1, 269, 270, 271, 261, -1,
                274, 264, -1, 266, -1, -1, 269, 270, 271, 256,
                256, 274, -1, -1, 261, 261, -1, 264, 264, 266,
                266, -1, 269, 270, 271, 271, -1, 274, -1, -1,
                256, -1, -1, 261, -1, 261, -1, -1, 264, -1,
                266, -1, -1, 269, 270, 271, -1, -1, 274, 277,
                278, -1, 280, 281, -1, -1, 284, 256, -1, -1,
                -1, -1, 261, -1, -1, 264, -1, 266, 256, 297,
                269, 270, 271, 261, -1, 274, 264, -1, 266, -1,
                -1, 269, 270, 271, -1, -1, 274,
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
            "proc_encabezado : PROC ID",
            "proc_encabezado : PROC error",
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

//#line 421 "gramatica.y"

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
                flotante = Main.MAX_FLOAT - 1;
                Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: %s - Se cambia por: %f %n", analizadorLexico.getNroLinea(), cte, flotante));
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
                }
                else if (TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals(Main.PROCEDIMIENTO)) {
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
        boolean declarado = !ambitosString.isEmpty();

        if (!declarado) {
            if (uso.equals(Main.VARIABLE))
                Errores.addError(String.format("[ASem] | Linea %d: Variable %s no declarada %n", analizadorLexico.getNroLinea(), lexema));
            else if (uso.equals(Main.PROCEDIMIENTO))
                Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
            else if (uso.equals(Main.PARAMETRO))
                Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
        }

        if (uso.equals(Main.PARAMETRO) && declarado)
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
            if (parametrosReales.size() != parametrosFormales.size())
                Errores.addError(String.format("[ASem] | Linea %d: La cantidad de parámetros reales no coincide con la cantidad de parámetros formales del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
                // Se chequea, por último, los tipos entre parámetros reales y formales
                boolean tiposCompatibles = true;
                for (int i = 0; i < parametrosReales.size(); i++) {
                    String tipoParametroReal = TablaSimbolos.getToken(parametrosReales.get(i)).getAtributo("tipo") + "";
                    if (!parametrosFormales.get(i).contains(tipoParametroReal)) {
                        Errores.addError(String.format("[ASem] | Linea %d: El tipo del parámetro real n° %d no corresponde con el formal %n", analizadorLexico.getNroLinea(), i + 1));
                        tiposCompatibles = false;
                        break;
                    }
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
        if (!id.equals("main"))
            return (id + "@" + ambitosActuales.substring(0, (ambitosActuales.length()) - (id.length() + 1)));
        else
            return null;
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
                Errores.addError(String.format("[AS] | Linea %d: Constante no es del tipo entero %n", analizadorLexico.getNroLinea()));
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

    //#line 1047 "Parser.java"
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
//#line 88 "gramatica.y"
                {
                    ambitos.agregarAmbito(val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PROCEDIMIENTO, null);
                }
                break;
                case 30:
//#line 91 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 31:
//#line 95 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getAtributo("tipo").equals("LONGINT"))
                        Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                    else{
                        String lexemaID = getLexemaID();
                        if (lexemaID != null)
                            TablaSimbolos.getToken(lexemaID).addAtributo("max. invocaciones", Integer.parseInt(cte));
                    }
                }
                break;
                case 32:
//#line 101 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 33:
//#line 102 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 34:
//#line 103 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 38:
//#line 112 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 39:
//#line 116 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 40:
//#line 120 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                    parametrosFormales.clear();
                }
                break;
                case 41:
//#line 124 "gramatica.y"
                {
                    String lexemaID = getLexemaID();
                    if (lexemaID != null)
                        TablaSimbolos.getToken(lexemaID).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                }
                break;
                case 42:
//#line 125 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 43:
//#line 126 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 44:
//#line 127 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 45:
//#line 128 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 46:
//#line 129 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 47:
//#line 132 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PARAMETRO, ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                }
                break;
                case 48:
//#line 137 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, Main.PARAMETRO, ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                }
                break;
                case 49:
//#line 142 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 50:
//#line 143 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 51:
//#line 146 "gramatica.y"
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
                case 52:
//#line 156 "gramatica.y"
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
                case 53:
//#line 166 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 54:
//#line 167 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 55:
//#line 168 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 56:
//#line 169 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 57:
//#line 170 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 58:
//#line 171 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 59:
//#line 172 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 60:
//#line 173 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 61:
//#line 174 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", nroUltimaLinea));
                }
                break;
                case 62:
//#line 175 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 63:
//#line 176 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 64:
//#line 177 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 65:
//#line 178 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 66:
//#line 179 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 67:
//#line 180 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 68:
//#line 183 "gramatica.y"
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
                case 69:
//#line 192 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 70:
//#line 193 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 71:
//#line 194 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 72:
//#line 195 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 73:
//#line 196 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 74:
//#line 200 "gramatica.y"
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
                case 76:
//#line 218 "gramatica.y"
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
                case 77:
//#line 234 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 78:
//#line 235 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 79:
//#line 236 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 80:
//#line 237 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 81:
//#line 238 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 82:
//#line 239 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 83:
//#line 240 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 84:
//#line 241 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 85:
//#line 244 "gramatica.y"
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
                case 86:
//#line 260 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 87:
//#line 261 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 88:
//#line 262 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 89:
//#line 263 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 90:
//#line 264 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 91:
//#line 265 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 94:
//#line 270 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 95:
//#line 271 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n", nroUltimaLinea));
                }
                break;
                case 96:
//#line 274 "gramatica.y"
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
                case 97:
//#line 286 "gramatica.y"
                {
                    yyval = new ParserVal("+");
                }
                break;
                case 98:
//#line 287 "gramatica.y"
                {
                    yyval = new ParserVal("-");
                }
                break;
                case 99:
//#line 290 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de salida OUT", analizadorLexico.getNroLinea());
                    SA1(val_peek(2).sval);
                    if (ambitos.getAmbitos().equals(Ambitos.ambitoGlobal))
                        polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                    else
                        polacaProcedimientos.addElem(ambitos.getAmbitos(), new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 100:
//#line 297 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 101:
//#line 298 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 102:
//#line 299 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 103:
//#line 300 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 104:
//#line 301 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 105:
//#line 302 "gramatica.y"
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
                case 106:
//#line 311 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 107:
//#line 316 "gramatica.y"
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
                case 108:
//#line 325 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 109:
//#line 326 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 110:
//#line 327 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 111:
//#line 328 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 112:
//#line 331 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, Main.PROCEDIMIENTO);
                }
                break;
                case 113:
//#line 334 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(3).sval, Main.PROCEDIMIENTO);
                }
                break;
                case 114:
//#line 337 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 115:
//#line 338 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 116:
//#line 339 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 117:
//#line 340 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 118:
//#line 341 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 119:
//#line 342 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 120:
//#line 343 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 121:
//#line 346 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, Main.PARAMETRO);
                    invocacionID(val_peek(2).sval, Main.PARAMETRO);
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 122:
//#line 351 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(2).sval, Main.PARAMETRO);
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 123:
//#line 355 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(0).sval, Main.PARAMETRO);
                }
                break;
                case 124:
//#line 358 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 125:
//#line 359 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 126:
//#line 360 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 127:
//#line 361 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 128:
//#line 362 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 129:
//#line 363 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 130:
//#line 368 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">=");
                }
                break;
                case 131:
//#line 369 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<=");
                }
                break;
                case 132:
//#line 370 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">");
                }
                break;
                case 133:
//#line 371 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<");
                }
                break;
                case 134:
//#line 372 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("==");
                }
                break;
                case 135:
//#line 373 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("!=");
                }
                break;
                case 136:
//#line 376 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 137:
//#line 378 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 138:
//#line 380 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 139:
//#line 381 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  segundo operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 140:
//#line 382 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  segundo operando en la resta %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 141:
//#line 383 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  primer operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 142:
//#line 386 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 143:
//#line 389 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 144:
//#line 392 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 145:
//#line 393 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  segundo operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 146:
//#line 394 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  segundo operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 147:
//#line 395 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  primer operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 148:
//#line 396 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el  primer operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 149:
//#line 399 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                    invocacionID(val_peek(0).sval, Main.VARIABLE);
                }
                break;
                case 150:
//#line 401 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 151:
//#line 404 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 152:
//#line 411 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                    }
                }
                break;
//#line 1872 "Parser.java"
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
