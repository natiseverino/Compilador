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
            3, 3, 1, 2, 1, 1, 4, 3, 2, 2,
            3, 2, 3, 3, 3, 3, 3, 7, 5, 3,
            6, 6, 4, 9, 5, 2, 2, 3, 2, 3,
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
            8, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 74, 7, 0, 0, 71, 0, 152, 0, 0,
            69, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 88, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 118, 0, 0, 115, 0, 21, 0, 24,
            20, 19, 0, 0, 46, 0, 0, 0, 0, 0,
            0, 0, 28, 108, 0, 0, 147, 148, 0, 0,
            0, 73, 68, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 145,
            142, 146, 143, 0, 90, 0, 91, 0, 89, 0,
            0, 0, 0, 96, 97, 98, 0, 0, 0, 0,
            0, 101, 0, 109, 126, 0, 125, 0, 110, 107,
            0, 113, 117, 0, 114, 22, 49, 0, 0, 47,
            0, 40, 0, 0, 0, 0, 0, 0, 32, 27,
            6, 75, 0, 66, 0, 0, 0, 56, 0, 0,
            0, 0, 0, 53, 86, 87, 85, 0, 0, 0,
            0, 0, 0, 100, 102, 103, 99, 105, 128, 127,
            0, 116, 112, 50, 48, 0, 0, 0, 43, 36,
            33, 34, 31, 37, 35, 0, 0, 58, 0, 0,
            0, 0, 51, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 45, 0, 39, 0, 0, 67, 0,
            57, 0, 62, 64, 0, 55, 54, 0, 0, 0,
            0, 0, 0, 0, 0, 93, 82, 77, 0, 0,
            42, 41, 59, 63, 52, 78, 84, 0, 80, 81,
            0, 3, 4, 0, 83, 76, 124, 0, 38, 79,
            94, 2, 5, 0, 92, 0, 44,
    };
    final static short yydgoto[] = {9,
            10, 301, 11, 12, 202, 63, 14, 15, 16, 17,
            18, 19, 20, 21, 54, 22, 56, 122, 123, 117,
            30, 64, 203, 90, 40, 91, 167, 287, 32, 33,
            34, 48, 35,
    };
    final static short yysindex[] = {701,
            -6, -32, -24, -36, 0, 0, -29, -204, 0, 701,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -165, -15, 269, 396, 597, 0, 0, 154, -227, 547,
            24, 0, 0, 525, 110, 26, -46, -157, 356, 269,
            -141, 147, 269, -25, 290, -3, 86, 124, 0, 0,
            0, 114, -16, -53, -19, -7, 155, 295, 49, 49,
            701, 0, 0, -230, 430, 0, 148, 0, -87, -170,
            0, 269, 269, 269, 269, 269, 269, 330, 335, 343,
            404, -68, 0, 189, 157, 193, 151, 269, 407, 259,
            -123, 315, 319, -39, 277, 358, 455, 3, -70, 116,
            551, 363, 0, 347, 55, 0, 350, 0, 140, 0,
            0, 0, 145, 53, 0, 150, -41, 701, -37, 713,
            152, -100, 0, 0, 246, 110, 0, 0, 483, 597,
            371, 0, 0, -56, 597, 388, 531, 597, 389, 180,
            180, 180, 180, 180, 180, 246, 110, 246, 110, 0,
            0, 0, 0, 392, 0, 394, 0, 395, 0, -123,
            -123, 138, -208, 0, 0, 0, 204, 402, 427, 432,
            447, 0, 456, 0, 0, 195, 0, 5, 0, 0,
            457, 0, 0, 458, 0, 0, 0, 250, 251, 0,
            200, 0, 164, 620, 248, -213, -35, 637, 0, 0,
            0, 0, 258, 0, 597, 468, 263, 0, -6, 558,
            471, 267, 268, 0, 0, 0, 0, 276, 285, -123,
            293, -198, 494, 0, 0, 0, 0, 0, 0, 0,
            305, 0, 0, 0, 0, -27, -34, 249, 0, 0,
            0, 0, 0, 0, 0, 521, 316, 0, 530, -44,
            538, -144, 0, 539, 541, 543, 549, 320, 562, 563,
            367, 609, 561, 0, 249, 0, 568, 572, 0, 571,
            0, 573, 0, 0, 576, 0, 0, 609, 609, 581,
            609, 609, 495, 701, 608, 0, 0, 0, 361, 272,
            0, 0, 0, 0, 0, 0, 0, 609, 0, 0,
            648, 0, 0, 664, 0, 0, 0, -15, 0, 0,
            0, 0, 0, -6, 0, 593, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 636,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            575, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 58, 0, 0, 0, 0, 0, 0,
            0, 0, -50, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 586, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 63, 0, 0,
            120, 0, 0, 171, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 21, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 131,
            387, 403, 426, 452, 472, 41, 68, 88, 108, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            194, 0, 218, 0, 0, 0, 0, 84, 0, 0,
            0, 0, 0, 238, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 273, 0,
            289, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 101, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 308, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 355, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            -43, 353, 662, -212, 9, 583, 0, 0, 0, 0,
            0, 0, 0, 533, -33, 0, 331, 0, 518, 569,
            0, 10, -108, 29, 602, 408, -110, 670, 557, 81,
            62, 599, 40,
    };
    final static int YYTABLESIZE = 987;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{192,
                138, 171, 191, 42, 23, 112, 266, 28, 23, 265,
                46, 47, 29, 264, 85, 39, 23, 129, 99, 110,
                141, 115, 120, 196, 55, 23, 207, 109, 212, 213,
                31, 45, 130, 62, 131, 61, 38, 104, 62, 70,
                139, 138, 242, 138, 68, 138, 176, 221, 231, 218,
                219, 49, 222, 121, 23, 103, 67, 260, 243, 138,
                138, 141, 138, 141, 71, 141, 50, 136, 165, 166,
                303, 303, 305, 261, 194, 186, 198, 62, 137, 141,
                141, 139, 141, 139, 57, 139, 82, 140, 313, 244,
                52, 313, 138, 29, 139, 184, 247, 126, 123, 139,
                139, 252, 139, 129, 97, 53, 101, 137, 136, 258,
                136, 274, 136, 183, 86, 120, 123, 147, 149, 111,
                275, 129, 96, 138, 122, 138, 136, 136, 140, 136,
                140, 92, 140, 140, 141, 142, 143, 144, 145, 127,
                128, 121, 122, 141, 106, 141, 140, 140, 137, 140,
                137, 80, 137, 165, 166, 118, 81, 59, 58, 121,
                151, 153, 60, 139, 107, 139, 137, 137, 134, 137,
                120, 134, 108, 2, 179, 135, 3, 136, 4, 59,
                58, 5, 6, 7, 60, 177, 8, 95, 133, 134,
                136, 29, 136, 104, 66, 29, 220, 78, 29, 79,
                178, 29, 111, 154, 239, 23, 205, 238, 206, 83,
                140, 82, 140, 124, 113, 157, 170, 106, 195, 41,
                272, 113, 78, 24, 79, 84, 43, 5, 6, 25,
                137, 36, 137, 114, 5, 6, 113, 119, 26, 27,
                114, 44, 111, 187, 111, 98, 37, 155, 118, 5,
                6, 159, 102, 134, 53, 114, 138, 138, 138, 138,
                138, 138, 138, 138, 138, 138, 138, 44, 119, 138,
                138, 138, 61, 175, 138, 230, 141, 141, 141, 141,
                141, 141, 141, 141, 141, 141, 141, 59, 60, 141,
                141, 141, 60, 120, 141, 120, 139, 139, 139, 139,
                139, 139, 139, 139, 139, 139, 139, 65, 188, 139,
                139, 139, 309, 29, 139, 308, 104, 164, 104, 26,
                27, 5, 6, 136, 136, 136, 136, 136, 136, 136,
                136, 136, 136, 136, 29, 172, 136, 136, 136, 29,
                106, 136, 106, 140, 140, 140, 140, 140, 140, 140,
                140, 140, 140, 140, 95, 168, 140, 140, 140, 169,
                119, 140, 119, 137, 137, 137, 137, 137, 137, 137,
                137, 137, 137, 137, 29, 111, 137, 137, 137, 29,
                111, 137, 111, 111, 111, 111, 134, 29, 111, 111,
                111, 134, 134, 111, 134, 61, 134, 61, 173, 134,
                134, 134, 93, 181, 134, 182, 24, 285, 185, 65,
                53, 60, 156, 60, 88, 187, 38, 26, 27, 94,
                190, 26, 27, 199, 26, 27, 120, 130, 27, 204,
                65, 120, 65, 120, 120, 120, 120, 59, 58, 120,
                120, 120, 60, 131, 120, 130, 208, 214, 29, 104,
                215, 29, 216, 217, 104, 236, 104, 104, 104, 104,
                224, 131, 104, 104, 104, 229, 135, 104, 5, 6,
                132, 59, 58, 106, 114, 223, 60, 95, 106, 95,
                106, 106, 106, 106, 135, 225, 106, 106, 106, 284,
                226, 106, 132, 119, 160, 161, 163, 78, 119, 79,
                119, 119, 119, 119, 113, 227, 119, 119, 119, 130,
                132, 119, 133, 174, 228, 232, 233, 5, 6, 241,
                234, 235, 246, 114, 24, 131, 248, 249, 61, 253,
                133, 254, 255, 61, 262, 61, 61, 61, 61, 26,
                27, 61, 61, 61, 60, 100, 61, 256, 135, 60,
                125, 60, 60, 60, 60, 23, 257, 60, 60, 60,
                26, 27, 60, 65, 259, 26, 27, 78, 65, 79,
                65, 65, 65, 65, 132, 263, 65, 65, 65, 269,
                270, 65, 13, 278, 77, 146, 76, 116, 271, 279,
                148, 280, 51, 78, 133, 79, 273, 276, 150, 277,
                26, 27, 281, 282, 289, 26, 27, 201, 291, 180,
                95, 87, 292, 26, 27, 95, 307, 95, 95, 95,
                95, 298, 283, 95, 95, 95, 37, 2, 95, 293,
                3, 294, 4, 317, 295, 1, 304, 7, 316, 200,
                89, 158, 130, 13, 105, 0, 189, 130, 130, 116,
                130, 0, 130, 61, 0, 130, 130, 130, 131, 152,
                130, 0, 162, 131, 131, 0, 131, 0, 131, 61,
                0, 131, 131, 131, 26, 27, 131, 26, 27, 0,
                61, 135, 0, 0, 0, 193, 135, 135, 0, 135,
                0, 135, 0, 0, 135, 135, 135, 72, 0, 135,
                13, 0, 13, 0, 0, 0, 0, 132, 70, 0,
                0, 51, 132, 132, 0, 132, 0, 132, 0, 61,
                132, 132, 132, 116, 0, 132, 0, 133, 0, 0,
                284, 284, 133, 133, 0, 133, 0, 133, 1, 0,
                133, 133, 133, 2, 240, 133, 3, 0, 4, 0,
                1, 5, 6, 7, 0, 2, 8, 0, 3, 237,
                4, 245, 0, 5, 6, 7, 0, 0, 8, 116,
                116, 0, 311, 0, 0, 0, 51, 0, 0, 0,
                51, 72, 73, 74, 75, 0, 209, 0, 315, 0,
                0, 2, 0, 210, 3, 211, 4, 116, 0, 5,
                6, 7, 1, 0, 8, 267, 268, 2, 69, 0,
                3, 0, 4, 250, 0, 5, 6, 7, 2, 0,
                8, 3, 251, 4, 0, 0, 5, 6, 7, 0,
                72, 8, 0, 290, 0, 72, 72, 0, 72, 0,
                72, 70, 0, 72, 72, 72, 70, 70, 72, 70,
                0, 70, 1, 0, 70, 70, 70, 2, 0, 70,
                3, 0, 4, 283, 283, 5, 6, 7, 2, 2,
                8, 3, 3, 4, 4, 1, 5, 6, 7, 7,
                2, 8, 0, 3, 0, 4, 0, 0, 5, 6,
                7, 0, 1, 8, 0, 0, 0, 2, 0, 0,
                3, 0, 4, 1, 0, 5, 6, 7, 2, 0,
                8, 3, 0, 4, 0, 0, 5, 6, 7, 314,
                0, 8, 286, 286, 2, 0, 0, 3, 0, 4,
                0, 288, 5, 6, 7, 0, 0, 8, 0, 286,
                286, 0, 286, 286, 302, 302, 286, 296, 297, 0,
                299, 300, 0, 0, 306, 0, 1, 0, 0, 286,
                0, 2, 312, 0, 3, 312, 4, 310, 197, 5,
                6, 7, 0, 2, 8, 0, 3, 0, 4, 0,
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
                0, 41, 123, 61, 40, 61, 135, 44, 137, 138,
                2, 61, 263, 25, 265, 123, 61, 41, 30, 30,
                0, 41, 256, 43, 272, 45, 44, 256, 44, 160,
                161, 256, 163, 61, 61, 59, 28, 256, 272, 59,
                60, 41, 62, 43, 41, 45, 271, 0, 277, 278,
                283, 284, 285, 272, 118, 109, 120, 69, 69, 59,
                60, 41, 62, 43, 23, 45, 61, 0, 301, 125,
                256, 304, 263, 45, 265, 41, 205, 58, 41, 59,
                60, 210, 62, 41, 43, 271, 45, 0, 41, 220,
                43, 256, 45, 59, 272, 123, 59, 78, 79, 0,
                265, 59, 42, 123, 41, 125, 59, 60, 41, 62,
                43, 273, 45, 72, 73, 74, 75, 76, 77, 59,
                60, 41, 59, 123, 59, 125, 59, 60, 41, 62,
                43, 42, 45, 277, 278, 256, 47, 42, 43, 59,
                80, 81, 47, 123, 41, 125, 59, 60, 256, 62,
                0, 41, 59, 261, 59, 263, 264, 265, 266, 42,
                43, 269, 270, 271, 47, 256, 274, 41, 41, 59,
                123, 45, 125, 0, 41, 45, 59, 43, 45, 45,
                271, 45, 256, 272, 41, 256, 263, 44, 265, 256,
                123, 61, 125, 59, 256, 59, 256, 0, 256, 256,
                265, 256, 43, 256, 45, 272, 256, 269, 270, 262,
                123, 256, 125, 275, 269, 270, 256, 0, 271, 272,
                275, 271, 123, 271, 125, 271, 271, 59, 256, 269,
                270, 59, 256, 123, 271, 275, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 271, 276, 269,
                270, 271, 0, 271, 274, 271, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 42, 0, 269,
                270, 271, 47, 123, 274, 125, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 0, 256, 269,
                270, 271, 41, 45, 274, 44, 123, 59, 125, 271,
                272, 269, 270, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 45, 59, 269, 270, 271, 45,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 0, 41, 269, 270, 271, 41,
                123, 274, 125, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 45, 256, 269, 270, 271, 45,
                261, 274, 263, 264, 265, 266, 256, 45, 269, 270,
                271, 261, 262, 274, 264, 123, 266, 125, 41, 269,
                270, 271, 256, 41, 274, 59, 256, 41, 59, 256,
                271, 123, 256, 125, 59, 271, 61, 271, 272, 273,
                271, 271, 272, 272, 271, 272, 256, 41, 272, 59,
                123, 261, 125, 263, 264, 265, 266, 42, 43, 269,
                270, 271, 47, 41, 274, 59, 59, 59, 45, 256,
                59, 45, 59, 59, 261, 256, 263, 264, 265, 266,
                59, 59, 269, 270, 271, 271, 41, 274, 269, 270,
                41, 42, 43, 256, 275, 272, 47, 123, 261, 125,
                263, 264, 265, 266, 59, 59, 269, 270, 271, 123,
                59, 274, 41, 256, 87, 88, 89, 43, 261, 45,
                263, 264, 265, 266, 256, 59, 269, 270, 271, 123,
                59, 274, 41, 59, 59, 59, 59, 269, 270, 272,
                271, 271, 265, 275, 256, 123, 59, 265, 256, 59,
                59, 265, 265, 261, 41, 263, 264, 265, 266, 271,
                272, 269, 270, 271, 256, 256, 274, 272, 123, 261,
                256, 263, 264, 265, 266, 61, 272, 269, 270, 271,
                271, 272, 274, 256, 272, 271, 272, 43, 261, 45,
                263, 264, 265, 266, 123, 271, 269, 270, 271, 59,
                265, 274, 0, 41, 60, 256, 62, 55, 59, 41,
                256, 272, 10, 43, 123, 45, 59, 59, 256, 59,
                271, 272, 41, 41, 44, 271, 272, 125, 41, 59,
                256, 256, 41, 271, 272, 261, 256, 263, 264, 265,
                266, 41, 256, 269, 270, 271, 271, 261, 274, 59,
                264, 59, 266, 41, 59, 0, 284, 271, 308, 122,
                39, 85, 256, 61, 46, -1, 114, 261, 262, 117,
                264, -1, 266, 123, -1, 269, 270, 271, 256, 256,
                274, -1, 256, 261, 262, -1, 264, -1, 266, 123,
                -1, 269, 270, 271, 271, 272, 274, 271, 272, -1,
                123, 256, -1, -1, -1, 117, 261, 262, -1, 264,
                -1, 266, -1, -1, 269, 270, 271, 123, -1, 274,
                118, -1, 120, -1, -1, -1, -1, 256, 123, -1,
                -1, 129, 261, 262, -1, 264, -1, 266, -1, 123,
                269, 270, 271, 191, -1, 274, -1, 256, -1, -1,
                123, 123, 261, 262, -1, 264, -1, 266, 256, -1,
                269, 270, 271, 261, 125, 274, 264, -1, 266, -1,
                256, 269, 270, 271, -1, 261, 274, -1, 264, 191,
                266, 125, -1, 269, 270, 271, -1, -1, 274, 237,
                238, -1, 125, -1, -1, -1, 194, -1, -1, -1,
                198, 257, 258, 259, 260, -1, 256, -1, 125, -1,
                -1, 261, -1, 263, 264, 265, 266, 265, -1, 269,
                270, 271, 256, -1, 274, 237, 238, 261, 262, -1,
                264, -1, 266, 256, -1, 269, 270, 271, 261, -1,
                274, 264, 265, 266, -1, -1, 269, 270, 271, -1,
                256, 274, -1, 265, -1, 261, 262, -1, 264, -1,
                266, 256, -1, 269, 270, 271, 261, 262, 274, 264,
                -1, 266, 256, -1, 269, 270, 271, 261, -1, 274,
                264, -1, 266, 256, 256, 269, 270, 271, 261, 261,
                274, 264, 264, 266, 266, 256, 269, 270, 271, 271,
                261, 274, -1, 264, -1, 266, -1, -1, 269, 270,
                271, -1, 256, 274, -1, -1, -1, 261, -1, -1,
                264, -1, 266, 256, -1, 269, 270, 271, 261, -1,
                274, 264, -1, 266, -1, -1, 269, 270, 271, 256,
                -1, 274, 261, 262, 261, -1, -1, 264, -1, 266,
                -1, 262, 269, 270, 271, -1, -1, 274, -1, 278,
                279, -1, 281, 282, 283, 284, 285, 278, 279, -1,
                281, 282, -1, -1, 285, -1, 256, -1, -1, 298,
                -1, 261, 301, -1, 264, 304, 266, 298, 256, 269,
                270, 271, -1, 261, 274, -1, 264, -1, 266, -1,
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
            "lista_parametros_formales : '(' parametro_formal parametro_formal ',' parametro_formal ')'",
            "lista_parametros_formales : '(' parametro_formal ',' parametro_formal parametro_formal ')'",
            "lista_parametros_formales : '(' parametro_formal parametro_formal ')'",
            "lista_parametros_formales : '(' parametro_formal ',' parametro_formal ',' parametro_formal ',' lista_parametros_formales ')'",
            "lista_parametros_formales : '(' parametro_formal ',' error ')'",
            "lista_parametros_formales : '(' ')'",
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

//#line 427 "gramatica.y"

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
        if (tipo.equals(Main.LONGINT)) {
            long entero = 0;
            if (Long.parseLong(cte) >= Main.MAX_LONG) {
                entero = Main.MAX_LONG - 1;
                Errores.addWarning(String.format("[AS] | Linea %d: Entero largo positivo fuera de rango: %s - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
                String nuevoLexema = String.valueOf(entero);
                cambiarSimbolo(token, cte, nuevoLexema, Main.LONGINT);
                return nuevoLexema;
            }
        }
        return cte;
    }


    public String checkRango(String cte) {
        Token token = TablaSimbolos.getToken(cte);
        String tipo = (String) token.getAtributo("tipo");

        if (tipo.equals(Main.LONGINT)) {
            long entero = 0;
            String nuevoLexema = null;
            if (Long.parseLong(cte) <= Main.MAX_LONG) {
                entero = Long.parseLong(cte);
            } else {
                entero = Main.MAX_LONG;
                Errores.addWarning(String.format("[AS] | Linea %d: Entero largo negativo fuera de rango: %d - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, entero));
            }
            nuevoLexema = "-" + entero;
            cambiarSimbolo(token, cte, nuevoLexema, Main.LONGINT);
            return nuevoLexema;

        }
        if (tipo.equals(Main.FLOAT)) {
            float flotante = 0f;
            if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
                flotante = Float.parseFloat(cte);
            } else {
                flotante = Main.MAX_FLOAT - 1;
                Errores.addWarning(String.format("[AS] | Linea %d: Flotante negativo fuera de rango: %d - Se cambia por: %d %n", analizadorLexico.getNroLinea(), cte, flotante));
            }
            if (flotante != 0f) {
                String nuevoLexema = "-" + flotante;
                cambiarSimbolo(token, cte, nuevoLexema, Main.FLOAT);
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
        if (uso.equals("Procedimiento"))
            nuevoLexema = lexema + "@" + ambitos.getAmbitos().substring(0, (ambitos.getAmbitos().length()) - (lexema.length() + 1));
        else
            nuevoLexema = lexema + "@" + ambitos.getAmbitos();

        if (!TablaSimbolos.existe(nuevoLexema)) {
            Token nuevoToken = new Token(token.getIdToken(), token.getTipoToken(), nuevoLexema);
            nuevoToken.addAtributo("uso", uso);
            nuevoToken.addAtributo("tipo", tipo);
            nuevoToken.addAtributo("contador", 0);
            nuevoToken.addAtributo("ambito", nuevoLexema.substring(lexema.length() + 1, nuevoLexema.length()));
            TablaSimbolos.add(nuevoToken);
        } else {
            if (uso.equals(Main.VARIABLE))
                Errores.addError(String.format("[ASem] | Linea %d: Variable redeclarada %n", analizadorLexico.getNroLinea()));
            else if (uso.equals("Procedimiento"))
                Errores.addError(String.format("[ASem] | Linea %d: Procedimiento redeclarado %n", analizadorLexico.getNroLinea()));
        }
    }

    public String getAmbitoDeclaracionID(String lexema, String uso) {
        // Se chequea que el id esté declarado en el ámbito actual o en los ámbitos que lo contienen
        String ambitosString = ambitos.getAmbitos();
        ArrayList<String> ambitosList = new ArrayList<>(Arrays.asList(ambitosString.split("@")));

        // Para el caso de la invocación a procedimientos se acota el ámbito actual para excluirlo como parte del ámbito al tener que estar declarado en un ámbito padre
        if (uso.equals("Procedimiento") && !ambitosString.equals("main"))
            ambitosString = ambitosString.substring(0, (ambitosString.length()) - (ambitosList.get(ambitosList.size() - 1).length() + 1));

        boolean declarado = false;
        while (!ambitosList.isEmpty()) {
            if (TablaSimbolos.existe(lexema + "@" + ambitosString)) {
                if ((uso.equals("Parametro") && !TablaSimbolos.getToken(lexema + "@" + ambitosString).getAtributo("uso").equals("Procedimiento")) || !uso.equals("Parametro")) {
                    declarado = true;
                    //if(!uso.equals("Procedimiento"))
                    //  actualizarContadorID(lexema + "@" + ambitosString, false);
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
            else if (uso.equals("Procedimiento"))
                Errores.addError(String.format("[ASem] | Linea %d: Procedimiento %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
            else if (uso.equals("Parametro"))
                Errores.addError(String.format("[ASem] | Linea %d: Parametro real %s no declarado %n", analizadorLexico.getNroLinea(), lexema));
        }

        if (uso.equals("Parametro") && declarado)
            parametrosReales.add(lexema + "@" + ambitosString);

        if (uso.equals("Procedimiento") && declarado) {
            Token procedimiento = TablaSimbolos.getToken(lexema + "@" + ambitosString);

            // Si se trata de un procedimiento que se encuentra declarado, se chequea el número de invocaciones respecto del máximo permitido
            if (((Integer) procedimiento.getAtributo("contador") + 1) > (Integer) procedimiento.getAtributo("max. invocaciones"))
                Errores.addError(String.format("[ASem] | Linea %d: Se supera el máximo de invocaciones del procedimiento %n", analizadorLexico.getNroLinea()));
            else {
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
            }
            parametrosReales.clear();
        }

        if (declarado && !uso.equals("Procedimiento"))
            actualizarContadorID(lexema + "@" + ambitosString, false);
        // Se actualiza el contador de referencias
        actualizarContadorID(lexema, true);
    }

    public String getLexemaID() {
        String ambitosActuales = ambitos.getAmbitos();
        String id = ambitosActuales.split("@")[ambitosActuales.split("@").length - 1];
        return (id + "@" + ambitosActuales.substring(0, (ambitosActuales.length()) - (id.length() + 1)));
    }


    public void out(String lex) {
        Token token = TablaSimbolos.getToken(lex);
        if (token != null) {
            if (token.getTipoToken().equals(Main.IDENTIFICADOR)) {
                if (token.getAtributo("uso") != null) {
                    if (token.getAtributo("uso").equals(Main.VARIABLE)) {
                        Object valor = token.getAtributo("VALOR");
                        if (valor != null)
                            System.out.println(token.getAtributo("VALOR") + "\n");
                        else
                            Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no inicializada %n", analizadorLexico.getNroLinea()));
                    }
                } else
                    Errores.addError(String.format("[AS] | Linea %d: Variable " + lex + " no declarada %n", analizadorLexico.getNroLinea()));
            } else if (token.getTipoToken().equals(Main.LONGINT) || token.getTipoToken().equals(Main.FLOAT))
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
            if (!cte_t.getAtributo("tipo").equals(Main.LONGINT))
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
            SA1(parametroFormal);
            SA1(parametrosReales.get(i));
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
                SA1(param[1]);
                SA1(param[0] + "@" + getAmbitoDeclaracionID(lexema, Main.PROCEDIMIENTO) + "@" + lexema);
                SA2("=");
            }
        }
    }

    //#line 1051 "Parser.java"
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
//#line 72 "gramatica.y"
                {
                    declaracionID(val_peek(2).sval, Main.VARIABLE, ultimoTipo);
                }
                break;
                case 23:
//#line 73 "gramatica.y"
                {
                    declaracionID(val_peek(0).sval, Main.VARIABLE, ultimoTipo);
                }
                break;
                case 24:
//#line 74 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ','  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 25:
//#line 77 "gramatica.y"
                {
                    ultimoTipo = "LONGINT";
                }
                break;
                case 26:
//#line 78 "gramatica.y"
                {
                    ultimoTipo = "FLOAT";
                }
                break;
                case 27:
//#line 81 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de declaración de procedimiento", analizadorLexico.getNroLinea());
                    ambitos.eliminarAmbito(actualizarTablaSimbolos);
                }
                break;
                case 28:
//#line 84 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta declarar el número de invocaciones permitido en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 29:
//#line 87 "gramatica.y"
                {
                    ambitos.agregarAmbito(val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Procedimiento", null);
                }
                break;
                case 30:
//#line 90 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 31:
//#line 104 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getAtributo("tipo").equals(Main.LONGINT))
                        Errores.addError(String.format("[ASem] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea()));
                    else {
                        int cteInt = Integer.parseInt(cte);
                        if (cteInt < 1 || cteInt > 4)
                            Errores.addError(String.format("[ASem] | Linea %d: NI no se encuentra en el intervalo [1,4] %n", analizadorLexico.getNroLinea()));
                        else
                            TablaSimbolos.getToken(getLexemaID()).addAtributo("max. invocaciones", Integer.parseInt(cte));
                    }

                }
                break;
                case 32:
//#line 116 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 33:
//#line 117 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 34:
//#line 118 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 38:
//#line 127 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (3)", analizadorLexico.getNroLinea());
                }
                break;
                case 39:
//#line 128 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (2)", analizadorLexico.getNroLinea());
                }
                break;
                case 40:
//#line 129 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros formales (1)", analizadorLexico.getNroLinea());
                }
                break;
                case 41:
//#line 130 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 42:
//#line 131 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 43:
//#line 132 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 44:
//#line 133 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros formales permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 45:
//#line 134 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro formal incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 46:
//#line 135 "gramatica.y"
                {
                    TablaSimbolos.getToken(getLexemaID()).addAtributo("parametros", new ArrayList<>(parametrosFormales));
                }
                break;
                case 47:
//#line 138 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add(ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CV");
                }
                break;
                case 48:
//#line 143 "gramatica.y"
                {
                    imprimirReglaReconocida("Parámetro formal", analizadorLexico.getNroLinea());
                    parametrosFormales.add("VAR " + ultimoTipo + " " + val_peek(0).sval);
                    declaracionID(val_peek(0).sval, "Parametro", ultimoTipo);
                    TablaSimbolos.getToken(val_peek(0).sval + "@" + ambitos.getAmbitos()).addAtributo("tipo pasaje", "CVR");
                }
                break;
                case 49:
//#line 148 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 50:
//#line 149 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el tipo del parámetro formal %n %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 51:
//#line 152 "gramatica.y"
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
//#line 162 "gramatica.y"
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
//#line 172 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 54:
//#line 173 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada THEN  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 55:
//#line 174 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada ELSE  %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 56:
//#line 175 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 57:
//#line 176 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 58:
//#line 177 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 59:
//#line 178 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias THEN %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 60:
//#line 179 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 61:
//#line 180 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", nroUltimaLinea));
                }
                break;
                case 62:
//#line 181 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 63:
//#line 182 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en bloque de sentencias ELSE %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 64:
//#line 183 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta palabra reservada END_IF %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 65:
//#line 184 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 66:
//#line 185 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 67:
//#line 186 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la condicion de la sentencia IF  %n", nroUltimaLinea));
                }
                break;
                case 68:
//#line 189 "gramatica.y"
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
//#line 198 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 70:
//#line 199 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 71:
//#line 200 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 72:
//#line 201 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan parentesis %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 73:
//#line 202 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la condicion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 74:
//#line 206 "gramatica.y"
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
//#line 224 "gramatica.y"
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
//#line 240 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 78:
//#line 241 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el inicio de la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 79:
//#line 242 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta condición de control en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 80:
//#line 243 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 81:
//#line 244 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 82:
//#line 245 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 83:
//#line 246 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 84:
//#line 247 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta asignacion a la variable de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 85:
//#line 250 "gramatica.y"
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
//#line 266 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 87:
//#line 267 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 88:
//#line 268 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 89:
//#line 269 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el identificador de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 90:
//#line 270 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la asignacion de control %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 91:
//#line 271 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta la constante de la asignacion %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 94:
//#line 276 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '{' en el bloque de sentencias de la sentencia FOR %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 95:
//#line 277 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '}' en el bloque de sentencias de la sentencia FOR %n", nroUltimaLinea));
                }
                break;
                case 96:
//#line 280 "gramatica.y"
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
//#line 292 "gramatica.y"
                {
                    yyval = new ParserVal("+");
                }
                break;
                case 98:
//#line 293 "gramatica.y"
                {
                    yyval = new ParserVal("-");
                }
                break;
                case 99:
//#line 296 "gramatica.y"
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
//#line 303 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 101:
//#line 304 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta elemento a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 102:
//#line 305 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Error en la cadena multilínea a imprimir %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 103:
//#line 306 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 104:
//#line 307 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 105:
//#line 308 "gramatica.y"
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
//#line 317 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 107:
//#line 322 "gramatica.y"
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
//#line 331 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado izquierdo de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 109:
//#line 332 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 110:
//#line 333 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta lado derecho de la asignación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 111:
//#line 334 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 112:
//#line 337 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación con lista de parámetros", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, "Procedimiento");
                }
                break;
                case 113:
//#line 340 "gramatica.y"
                {
                    imprimirReglaReconocida("Sentencia de invocación sin parámetros %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(3).sval, "Procedimiento");
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
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal '(' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 116:
//#line 345 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parametros invalidos %n", analizadorLexico.getNroLinea()));
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
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ')' %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 119:
//#line 348 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 120:
//#line 349 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ';' %n", nroUltimaLinea));
                }
                break;
                case 121:
//#line 352 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (3)", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(4).sval, "Parametro");
                    invocacionID(val_peek(2).sval, "Parametro");
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 122:
//#line 357 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (2) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(2).sval, "Parametro");
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 123:
//#line 361 "gramatica.y"
                {
                    imprimirReglaReconocida("Lista de parámetros (1) %n", analizadorLexico.getNroLinea());
                    invocacionID(val_peek(0).sval, "Parametro");
                }
                break;
                case 124:
//#line 364 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Número de parámetros permitidos excedido %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 125:
//#line 365 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Parámetro incorrecto %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 126:
//#line 366 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Faltan literales ',' entre parámetros %n", analizadorLexico.getNroLinea()));
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
//#line 369 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta literal ',' entre parámetros %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 130:
//#line 374 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">=");
                }
                break;
                case 131:
//#line 375 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<=");
                }
                break;
                case 132:
//#line 376 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2(">");
                }
                break;
                case 133:
//#line 377 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("<");
                }
                break;
                case 134:
//#line 378 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("==");
                }
                break;
                case 135:
//#line 379 "gramatica.y"
                {
                    yyval = val_peek(2);
                    SA2("!=");
                }
                break;
                case 136:
//#line 382 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Suma", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 137:
//#line 384 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Resta", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 138:
//#line 386 "gramatica.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 139:
//#line 387 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 140:
//#line 388 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la resta %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 141:
//#line 389 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la suma %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 142:
//#line 392 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("Multiplicación", analizadorLexico.getNroLinea());
                    SA2(val_peek(1).sval);
                }
                break;
                case 143:
//#line 395 "gramatica.y"
                {
                    yyval = val_peek(2);
                    imprimirReglaReconocida("División", analizadorLexico.getNroLinea());
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
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 146:
//#line 400 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el segundo operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 147:
//#line 401 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la multiplicación %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 148:
//#line 402 "gramatica.y"
                {
                    Errores.addError(String.format("[AS] | Linea %d: Falta el primer operando en la division %n", analizadorLexico.getNroLinea()));
                }
                break;
                case 149:
//#line 405 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                    invocacionID(val_peek(0).sval, Main.VARIABLE);
                }
                break;
                case 150:
//#line 407 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 151:
//#line 410 "gramatica.y"
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
//#line 417 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        imprimirReglaReconocida(String.format("Constante negativa %s %n", nuevo), analizadorLexico.getNroLinea());
                    }
                }
                break;
//#line 1873 "Parser.java"
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
