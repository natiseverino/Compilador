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
            10, 10, 10, 22, 22, 22, 22, 22, 24, 24,
            23, 23, 11, 11, 11, 11, 11, 11, 11, 12,
            12, 12, 12, 12, 13, 13, 13, 13, 13, 13,
            13, 13, 13, 27, 27, 27, 27, 27, 27, 27,
            27, 27, 21, 21, 21, 21, 21, 21, 26, 26,
            26, 26, 26, 26, 28, 28, 28, 28, 28, 28,
            28, 25, 25, 29, 29,
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
            4, 2, 3, 3, 3, 3, 3, 3, 3, 3,
            1, 3, 3, 3, 3, 3, 1, 3, 3, 3,
            3, 1, 1, 1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            10, 11, 9, 12, 13, 14, 15, 16, 17, 18,
            0, 0, 0, 0, 152, 154, 0, 0, 0, 0,
            147, 0, 0, 153, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 8,
            0, 0, 0, 0, 0, 0, 0, 0, 83, 7,
            0, 0, 80, 0, 155, 0, 0, 78, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 121, 0, 0, 0, 0,
            0, 118, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 21, 0, 24, 20, 19, 111, 0, 0, 150,
            151, 0, 0, 0, 82, 77, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 148, 145, 149, 146, 95, 96, 97, 94,
            0, 0, 0, 0, 0, 0, 0, 0, 105, 0,
            112, 129, 0, 128, 0, 58, 0, 0, 0, 0,
            116, 56, 0, 0, 0, 120, 0, 113, 110, 117,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 22,
            6, 84, 0, 75, 0, 0, 0, 65, 0, 0,
            0, 0, 0, 62, 0, 0, 0, 0, 0, 104,
            106, 107, 103, 109, 131, 130, 0, 119, 59, 57,
            0, 0, 0, 0, 0, 115, 0, 0, 0, 0,
            0, 0, 0, 0, 46, 0, 0, 0, 0, 0,
            0, 67, 0, 0, 0, 0, 60, 0, 0, 0,
            101, 102, 0, 0, 0, 0, 0, 0, 0, 0,
            52, 51, 0, 0, 0, 0, 0, 0, 0, 47,
            0, 0, 0, 0, 0, 0, 0, 76, 0, 66,
            0, 71, 73, 0, 64, 63, 0, 0, 0, 0,
            0, 0, 0, 30, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            32, 0, 68, 72, 61, 0, 0, 0, 0, 0,
            0, 0, 127, 29, 0, 34, 0, 36, 0, 39,
            0, 41, 43, 45, 28, 38, 0, 0, 0, 0,
            31, 0, 0, 0, 100, 93, 0, 0, 0, 0,
            91, 86, 54, 33, 35, 37, 40, 42, 44, 27,
            87, 0, 0, 3, 4, 88, 89, 90, 92, 85,
            0, 0, 99, 2, 5,
    };
    final static short yydgoto[] = {9,
            10, 363, 11, 12, 192, 60, 14, 15, 16, 17,
            18, 19, 20, 21, 53, 99, 100, 29, 61, 193,
            30, 38, 253, 346, 31, 32, 46, 33, 34,
    };
    final static short yysindex[] = {758,
            -27, 41, -24, -28, 0, 0, -1, 34, 0, 758,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -229, 196, 504, 667, 0, 0, 216, -255, 564, 46,
            0, 584, 88, 0, 7, -5, -44, 79, -109, 38,
            196, -3, -4, 120, 240, 184, 140, -26, -6, 0,
            277, 22, 32, 91, 246, 24, 24, 758, 0, 0,
            -90, 543, 0, 320, 0, 593, -63, 0, 196, 196,
            196, 196, 196, 196, 294, 311, 337, 439, 114, 134,
            -210, 144, 196, 319, 196, 366, 368, -8, 363, 385,
            126, 27, -156, -12, 210, 0, -55, 156, 388, 175,
            44, 0, 416, 149, 390, 17, 104, 129, 182, 179,
            420, 0, 186, 0, 0, 0, 0, 113, 88, 0,
            0, 706, 667, 403, 0, 0, -48, 667, 411, 605,
            667, 414, 181, 181, 181, 181, 181, 181, 113, 88,
            113, 88, 0, 0, 0, 0, 0, 0, 0, 0,
            196, 418, 513, 423, 424, 427, 432, 434, 0, 435,
            0, 0, 230, 0, 28, 0, 447, 242, 244, 459,
            0, 0, 249, -108, 491, 0, 477, 0, 0, 0,
            262, 500, 276, 515, -38, 553, -36, 493, 281, 0,
            0, 0, 295, 0, 667, 503, 299, 0, -27, 617,
            510, 310, 314, 0, 521, -254, 422, 529, -254, 0,
            0, 0, 0, 0, 0, 0, 288, 0, 0, 0,
            317, 530, 182, 274, 270, 0, 538, 328, 545, 334,
            550, 358, 542, 6, 0, 552, -238, 365, 574, 579,
            374, 0, 582, -42, 583, -157, 0, 592, 597, -254,
            0, 0, 371, -254, -230, 380, 613, 667, 387, 270,
            0, 0, 389, 599, 391, 601, 394, 606, 516, 0,
            396, -199, 398, 623, 76, 667, 425, 0, 639, 0,
            643, 0, 0, 644, 0, 0, 433, 665, 436, 437,
            -195, 673, 462, 0, 667, 675, 667, 452, 667, 454,
            667, 455, 667, 667, 667, 667, 667, 458, 460, -174,
            0, 667, 0, 0, 0, 682, -60, 692, 693, 697,
            507, -60, 0, 0, 486, 0, 667, 0, 667, 0,
            667, 0, 0, 0, 0, 0, 667, 667, 667, 667,
            0, -60, 4, 758, 0, 0, -60, -60, -60, 678,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 81, 722, 0, 0, 0, 0, 0, 0, 0,
            702, 688, 0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 748,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 628,
            0, 0, 64, 0, 0, 0, 0, 0, 0, 0,
            0, 60, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 45, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 651, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 691,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 69, 0, 0, 0, 0, 234, 0, 0, -34,
            0, 0, 0, 258, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 84, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 362, 419, 430, 451, 475, 496, 106, 131,
            154, 177, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 307, 0, 0,
            0, 0, 0, 0, 98, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -33, 0, 331, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 351, 0,
            384, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -32, -31, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 100, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 408, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -30, 0, 0, 0, 0,
            0, 0, 139, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 234, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            695, 0, 668, -274, -23, 20, 0, 0, 0, 0,
            0, 0, 0, 686, -21, -13, -72, 0, -7, -17,
            5, 717, -134, 608, 40, -20, -43, 57, 0,
    };
    final static int YYTABLESIZE = 1032;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{101,
                59, 54, 232, 171, 237, 59, 50, 53, 55, 49,
                48, 40, 22, 108, 83, 37, 65, 274, 22, 13,
                91, 67, 251, 252, 104, 290, 51, 175, 167, 50,
                114, 64, 158, 22, 110, 111, 97, 275, 43, 44,
                93, 52, 59, 362, 44, 149, 251, 252, 133, 134,
                135, 136, 137, 138, 96, 81, 305, 181, 130, 45,
                320, 150, 344, 141, 45, 113, 272, 79, 28, 365,
                163, 217, 306, 49, 256, 369, 321, 13, 89, 90,
                27, 339, 28, 144, 177, 28, 68, 152, 375, 154,
                116, 190, 182, 184, 187, 120, 121, 340, 283, 164,
                126, 224, 176, 23, 141, 142, 141, 284, 141, 132,
                197, 119, 202, 203, 165, 287, 144, 146, 126, 289,
                291, 372, 141, 141, 144, 141, 144, 132, 144, 77,
                139, 140, 142, 75, 78, 76, 310, 85, 125, 96,
                124, 50, 144, 144, 183, 144, 142, 223, 142, 117,
                142, 261, 262, 143, 56, 205, 125, 208, 124, 57,
                5, 6, 235, 86, 142, 142, 95, 142, 75, 186,
                76, 139, 123, 139, 124, 139, 140, 241, 102, 106,
                154, 154, 246, 154, 161, 154, 141, 296, 141, 139,
                139, 75, 139, 76, 143, 1, 143, 154, 143, 131,
                2, 132, 151, 3, 79, 4, 144, 179, 144, 270,
                343, 82, 143, 143, 195, 143, 196, 140, 174, 140,
                170, 140, 281, 75, 105, 76, 36, 39, 142, 107,
                142, 35, 166, 123, 294, 140, 140, 231, 140, 236,
                28, 50, 53, 55, 49, 48, 36, 157, 54, 109,
                80, 94, 311, 139, 41, 139, 63, 114, 166, 41,
                28, 271, 5, 6, 5, 6, 42, 92, 95, 42,
                95, 324, 109, 326, 42, 328, 143, 330, 143, 332,
                333, 334, 335, 336, 28, 5, 6, 115, 341, 47,
                28, 95, 52, 87, 25, 26, 23, 162, 216, 140,
                23, 140, 24, 354, 48, 355, 108, 356, 25, 26,
                88, 25, 26, 357, 358, 359, 360, 260, 101, 141,
                141, 141, 141, 141, 141, 141, 141, 141, 141, 141,
                122, 309, 141, 141, 141, 112, 371, 141, 28, 144,
                144, 144, 144, 144, 144, 144, 144, 144, 144, 144,
                70, 42, 144, 144, 144, 28, 123, 144, 123, 109,
                126, 142, 142, 142, 142, 142, 142, 142, 142, 142,
                142, 142, 5, 6, 142, 142, 142, 153, 95, 142,
                114, 28, 114, 69, 185, 147, 139, 139, 139, 139,
                139, 139, 139, 139, 139, 139, 139, 5, 6, 139,
                139, 139, 137, 95, 139, 148, 155, 74, 156, 143,
                143, 143, 143, 143, 143, 143, 143, 143, 143, 143,
                137, 159, 143, 143, 143, 160, 172, 143, 173, 108,
                109, 108, 140, 140, 140, 140, 140, 140, 140, 140,
                140, 140, 140, 5, 6, 140, 140, 140, 180, 95,
                140, 23, 166, 122, 188, 122, 52, 56, 55, 133,
                189, 194, 57, 56, 55, 168, 25, 26, 57, 198,
                134, 62, 204, 70, 178, 70, 206, 133, 5, 6,
                254, 209, 210, 28, 137, 211, 25, 26, 134, 123,
                212, 138, 213, 214, 123, 103, 123, 123, 123, 123,
                215, 118, 123, 123, 123, 218, 69, 123, 69, 138,
                25, 26, 219, 114, 220, 135, 25, 26, 114, 221,
                114, 114, 114, 114, 222, 109, 114, 114, 114, 109,
                74, 114, 74, 135, 225, 226, 136, 227, 5, 6,
                228, 133, 5, 6, 95, 56, 55, 350, 95, 139,
                57, 229, 134, 238, 136, 230, 239, 28, 257, 240,
                28, 242, 108, 243, 25, 26, 141, 108, 247, 108,
                108, 108, 108, 138, 248, 108, 108, 108, 249, 250,
                108, 25, 26, 125, 56, 55, 122, 255, 258, 57,
                259, 122, 143, 122, 122, 122, 122, 135, 263, 122,
                122, 122, 269, 264, 122, 265, 70, 25, 26, 266,
                267, 70, 273, 70, 70, 70, 70, 137, 136, 70,
                70, 70, 137, 137, 70, 137, 75, 137, 76, 344,
                137, 137, 137, 268, 277, 137, 276, 278, 279, 69,
                280, 282, 288, 74, 69, 73, 69, 69, 69, 69,
                285, 292, 69, 69, 69, 286, 293, 69, 295, 298,
                297, 300, 299, 74, 58, 301, 302, 304, 74, 307,
                74, 74, 74, 74, 133, 58, 74, 74, 74, 133,
                133, 74, 133, 308, 133, 134, 58, 133, 133, 133,
                134, 134, 133, 134, 145, 134, 312, 313, 134, 134,
                134, 314, 315, 134, 316, 317, 138, 318, 319, 25,
                26, 138, 138, 322, 138, 58, 138, 323, 325, 138,
                138, 138, 342, 327, 138, 329, 331, 58, 98, 337,
                135, 338, 347, 348, 98, 135, 135, 349, 135, 58,
                135, 353, 167, 135, 135, 135, 171, 1, 135, 98,
                81, 136, 122, 84, 0, 0, 136, 136, 0, 136,
                0, 136, 1, 0, 136, 136, 136, 2, 207, 136,
                3, 23, 4, 79, 0, 0, 0, 343, 0, 0,
                169, 0, 0, 25, 26, 98, 25, 303, 0, 58,
                0, 98, 98, 98, 0, 0, 0, 1, 0, 0,
                344, 0, 2, 0, 0, 3, 0, 4, 233, 0,
                5, 6, 7, 2, 0, 8, 3, 0, 4, 1,
                0, 5, 6, 7, 2, 66, 8, 3, 234, 4,
                191, 0, 5, 6, 7, 0, 0, 8, 0, 0,
                69, 70, 71, 72, 0, 0, 373, 0, 127, 0,
                0, 0, 0, 2, 0, 128, 3, 129, 4, 98,
                199, 5, 6, 7, 0, 2, 8, 200, 3, 201,
                4, 0, 244, 5, 6, 7, 0, 2, 8, 0,
                3, 245, 4, 81, 0, 5, 6, 7, 81, 81,
                8, 81, 0, 81, 0, 0, 81, 81, 81, 0,
                0, 81, 0, 0, 0, 0, 79, 0, 0, 98,
                98, 79, 79, 0, 79, 0, 79, 0, 0, 79,
                79, 79, 1, 0, 79, 0, 0, 2, 351, 352,
                3, 0, 4, 1, 0, 5, 6, 7, 2, 0,
                8, 3, 0, 4, 0, 98, 5, 6, 7, 361,
                0, 8, 0, 0, 366, 367, 368, 370, 0, 0,
                0, 1, 0, 0, 0, 0, 2, 0, 0, 3,
                0, 4, 0, 0, 5, 6, 7, 1, 0, 8,
                0, 0, 2, 0, 345, 3, 0, 4, 345, 345,
                5, 6, 7, 0, 0, 8, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 345,
                0, 364, 0, 1, 345, 345, 345, 345, 2, 0,
                0, 3, 0, 4, 0, 0, 5, 6, 7, 0,
                374, 8,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{43,
                24, 22, 41, 59, 41, 29, 41, 41, 41, 41,
                41, 40, 61, 40, 59, 40, 272, 256, 61, 0,
                41, 29, 277, 278, 45, 256, 256, 100, 41, 10,
                52, 27, 41, 61, 41, 49, 41, 276, 40, 41,
                44, 271, 66, 40, 41, 256, 277, 278, 69, 70,
                71, 72, 73, 74, 59, 61, 256, 41, 66, 61,
                256, 272, 123, 0, 61, 44, 61, 61, 45, 344,
                44, 44, 272, 40, 209, 350, 272, 58, 41, 40,
                40, 256, 45, 0, 41, 45, 41, 83, 363, 85,
                59, 113, 106, 107, 108, 56, 57, 272, 256, 256,
                41, 174, 59, 59, 41, 0, 43, 265, 45, 41,
                128, 55, 130, 131, 271, 250, 77, 78, 59, 254,
                255, 41, 59, 60, 41, 62, 43, 59, 45, 42,
                0, 75, 76, 43, 47, 45, 61, 59, 41, 59,
                41, 122, 59, 60, 41, 62, 41, 256, 43, 59,
                45, 224, 225, 0, 42, 151, 59, 153, 59, 47,
                269, 270, 186, 273, 59, 60, 275, 62, 43, 41,
                45, 41, 263, 43, 265, 45, 0, 195, 59, 40,
                42, 43, 200, 45, 59, 47, 123, 260, 125, 59,
                60, 43, 62, 45, 41, 256, 43, 59, 45, 263,
                261, 265, 59, 264, 61, 266, 123, 59, 125, 233,
                271, 256, 59, 60, 263, 62, 265, 41, 44, 43,
                276, 45, 265, 43, 41, 45, 271, 256, 123, 256,
                125, 256, 271, 0, 258, 59, 60, 276, 62, 276,
                45, 276, 276, 276, 276, 276, 271, 256, 269, 256,
                256, 256, 276, 123, 256, 125, 41, 0, 271, 256,
                45, 256, 269, 270, 269, 270, 271, 271, 275, 271,
                275, 295, 256, 297, 271, 299, 123, 301, 125, 303,
                304, 305, 306, 307, 45, 269, 270, 256, 312, 256,
                45, 275, 271, 256, 271, 272, 256, 271, 271, 123,
                256, 125, 262, 327, 271, 329, 0, 331, 271, 272,
                273, 271, 272, 337, 338, 339, 340, 44, 362, 256,
                257, 258, 259, 260, 261, 262, 263, 264, 265, 266,
                0, 256, 269, 270, 271, 59, 256, 274, 45, 256,
                257, 258, 259, 260, 261, 262, 263, 264, 265, 266,
                0, 271, 269, 270, 271, 45, 123, 274, 125, 256,
                41, 256, 257, 258, 259, 260, 261, 262, 263, 264,
                265, 266, 269, 270, 269, 270, 271, 59, 275, 274,
                123, 45, 125, 0, 256, 272, 256, 257, 258, 259,
                260, 261, 262, 263, 264, 265, 266, 269, 270, 269,
                270, 271, 41, 275, 274, 272, 41, 0, 41, 256,
                257, 258, 259, 260, 261, 262, 263, 264, 265, 266,
                59, 59, 269, 270, 271, 41, 271, 274, 41, 123,
                256, 125, 256, 257, 258, 259, 260, 261, 262, 263,
                264, 265, 266, 269, 270, 269, 270, 271, 59, 275,
                274, 256, 271, 123, 276, 125, 271, 42, 43, 41,
                41, 59, 47, 42, 43, 256, 271, 272, 47, 59,
                41, 256, 59, 123, 59, 125, 59, 59, 269, 270,
                59, 59, 59, 45, 123, 59, 271, 272, 59, 256,
                59, 41, 59, 59, 261, 256, 263, 264, 265, 266,
                271, 256, 269, 270, 271, 59, 123, 274, 125, 59,
                271, 272, 271, 256, 271, 41, 271, 272, 261, 61,
                263, 264, 265, 266, 276, 256, 269, 270, 271, 256,
                123, 274, 125, 59, 44, 59, 41, 276, 269, 270,
                41, 123, 269, 270, 275, 42, 43, 41, 275, 256,
                47, 276, 123, 61, 59, 41, 276, 45, 271, 265,
                45, 59, 256, 265, 271, 272, 256, 261, 59, 263,
                264, 265, 266, 123, 265, 269, 270, 271, 265, 59,
                274, 271, 272, 41, 42, 43, 256, 59, 272, 47,
                61, 261, 256, 263, 264, 265, 266, 123, 61, 269,
                270, 271, 61, 276, 274, 61, 256, 271, 272, 276,
                61, 261, 61, 263, 264, 265, 266, 256, 123, 269,
                270, 271, 261, 262, 274, 264, 43, 266, 45, 123,
                269, 270, 271, 276, 61, 274, 272, 59, 265, 256,
                59, 59, 272, 60, 261, 62, 263, 264, 265, 266,
                59, 272, 269, 270, 271, 59, 44, 274, 272, 61,
                272, 61, 272, 256, 123, 272, 61, 272, 261, 272,
                263, 264, 265, 266, 256, 123, 269, 270, 271, 261,
                262, 274, 264, 61, 266, 256, 123, 269, 270, 271,
                261, 262, 274, 264, 256, 266, 272, 59, 269, 270,
                271, 59, 59, 274, 272, 41, 256, 272, 272, 271,
                272, 261, 262, 41, 264, 123, 266, 256, 44, 269,
                270, 271, 41, 272, 274, 272, 272, 123, 43, 272,
                256, 272, 41, 41, 49, 261, 262, 41, 264, 123,
                266, 256, 41, 269, 270, 271, 59, 0, 274, 59,
                123, 256, 58, 37, -1, -1, 261, 262, -1, 264,
                -1, 266, 256, -1, 269, 270, 271, 261, 256, 274,
                264, 256, 266, 123, -1, -1, -1, 271, -1, -1,
                95, -1, -1, 271, 272, 100, 271, 272, -1, 123,
                -1, 106, 107, 108, -1, -1, -1, 256, -1, -1,
                123, -1, 261, -1, -1, 264, -1, 266, 256, -1,
                269, 270, 271, 261, -1, 274, 264, -1, 266, 256,
                -1, 269, 270, 271, 261, 262, 274, 264, 276, 266,
                125, -1, 269, 270, 271, -1, -1, 274, -1, -1,
                257, 258, 259, 260, -1, -1, 125, -1, 256, -1,
                -1, -1, -1, 261, -1, 263, 264, 265, 266, 174,
                256, 269, 270, 271, -1, 261, 274, 263, 264, 265,
                266, -1, 256, 269, 270, 271, -1, 261, 274, -1,
                264, 265, 266, 256, -1, 269, 270, 271, 261, 262,
                274, 264, -1, 266, -1, -1, 269, 270, 271, -1,
                -1, 274, -1, -1, -1, -1, 256, -1, -1, 224,
                225, 261, 262, -1, 264, -1, 266, -1, -1, 269,
                270, 271, 256, -1, 274, -1, -1, 261, 321, 322,
                264, -1, 266, 256, -1, 269, 270, 271, 261, -1,
                274, 264, -1, 266, -1, 260, 269, 270, 271, 342,
                -1, 274, -1, -1, 347, 348, 349, 350, -1, -1,
                -1, 256, -1, -1, -1, -1, 261, -1, -1, 264,
                -1, 266, -1, -1, 269, 270, 271, 256, -1, 274,
                -1, -1, 261, -1, 317, 264, -1, 266, 321, 322,
                269, 270, 271, -1, -1, 274, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, 342,
                -1, 344, -1, 256, 347, 348, 349, 350, 261, -1,
                -1, 264, -1, 266, -1, -1, 269, 270, 271, -1,
                363, 274,
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

//#line 342 "gramatica.y"

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
        ElemSimple elem = new ElemSimple(token);
        polaca.addElem(elem, false);
    }

    public void SA2(String operador) { //añadir operador binario a la polaca
        OperadorBinario elem = new OperadorBinario(operador);
        polaca.addElem(elem, false);
    }

    //#line 823 "Parser.java"
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
                    //polaca.addElem(new EtiquetaElem(polaca.size()), false);
                }
                break;
                case 61:
//#line 161 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    polaca.addElem(new ElemPos(polaca.size()), true);
                    //polaca.addElem(new EtiquetaElem(polaca.size()), false);
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
                    String cte = val_peek(2).sval;
                    if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    else
                        error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
                }
                break;
                case 86:
//#line 207 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 87:
//#line 208 "gramatica.y"
                {
                    error("Error en el inicio de la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 88:
//#line 209 "gramatica.y"
                {
                    error("Falta condición de control en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 89:
//#line 210 "gramatica.y"
                {
                    error("Falta indicar incremento o decremento de la sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 90:
//#line 211 "gramatica.y"
                {
                    error("Falta indicar constante de paso para incremento/decremento en sentencia de control", analizadorLexico.getNroLinea());
                }
                break;
                case 91:
//#line 212 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 92:
//#line 213 "gramatica.y"
                {
                    error("Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa.", analizadorLexico.getNroLinea());
                }
                break;
                case 93:
//#line 214 "gramatica.y"
                {
                    error("Falta asignacion a la variable de control", analizadorLexico.getNroLinea());
                }
                break;
                case 94:
//#line 217 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        error("Constante no es del tipo entero", analizadorLexico.getNroLinea());
                }
                break;
                case 95:
//#line 222 "gramatica.y"
                {
                    error("Error en el identificador de control", analizadorLexico.getNroLinea());
                }
                break;
                case 96:
//#line 223 "gramatica.y"
                {
                    error("Error, el inicio del for debe ser una asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 97:
//#line 224 "gramatica.y"
                {
                    error("Error en la constante de la asignacion", analizadorLexico.getNroLinea());
                }
                break;
                case 98:
//#line 225 "gramatica.y"
                {
                    error("Error en la asignacion de control", analizadorLexico.getNroLinea());
                }
                break;
                case 103:
//#line 239 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String cadena = val_peek(2).sval;
                    System.out.println(cadena);
                    SA1(cadena);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 104:
//#line 245 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 105:
//#line 246 "gramatica.y"
                {
                    error("Falta elemento a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 106:
//#line 247 "gramatica.y"
                {
                    error("Error en la cadena multilínea a imprimir", analizadorLexico.getNroLinea());
                }
                break;
                case 107:
//#line 248 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 108:
//#line 249 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 109:
//#line 250 "gramatica.y"
                {
                    String factor = val_peek(2).sval;
                    out(factor);
                    polaca.addElem(new OperadorUnario(OperadorUnario.Tipo.OUT), false);
                }
                break;
                case 110:
//#line 255 "gramatica.y"
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
//#line 266 "gramatica.y"
                {
                    error("Falta lado izquierdo de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 112:
//#line 267 "gramatica.y"
                {
                    error("Falta literal '=' en sentencia de asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 113:
//#line 268 "gramatica.y"
                {
                    error("Falta lado derecho de la asignación", analizadorLexico.getNroLinea());
                }
                break;
                case 114:
//#line 269 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 115:
//#line 272 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 116:
//#line 273 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 117:
//#line 274 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 118:
//#line 275 "gramatica.y"
                {
                    error("Falta literal '('", analizadorLexico.getNroLinea());
                }
                break;
                case 119:
//#line 276 "gramatica.y"
                {
                    error("Parametros invalidos", analizadorLexico.getNroLinea());
                }
                break;
                case 120:
//#line 277 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 121:
//#line 278 "gramatica.y"
                {
                    error("Falta literal ')'", analizadorLexico.getNroLinea());
                }
                break;
                case 122:
//#line 279 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 123:
//#line 280 "gramatica.y"
                {
                    error("Falta literal ';'", nroUltimaLinea);
                }
                break;
                case 124:
//#line 283 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 125:
//#line 284 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 126:
//#line 285 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 127:
//#line 286 "gramatica.y"
                {
                    error("Número de parámetros permitidos excedido", analizadorLexico.getNroLinea());
                }
                break;
                case 128:
//#line 287 "gramatica.y"
                {
                    error("Parámetro incorrecto", analizadorLexico.getNroLinea());
                }
                break;
                case 129:
//#line 288 "gramatica.y"
                {
                    error("Faltan literales ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 130:
//#line 289 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 131:
//#line 290 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 132:
//#line 291 "gramatica.y"
                {
                    error("Falta literal ',' entre parámetros", analizadorLexico.getNroLinea());
                }
                break;
                case 133:
//#line 295 "gramatica.y"
                {
                    SA2(">=");
                }
                break;
                case 134:
//#line 296 "gramatica.y"
                {
                    SA2("<=");
                }
                break;
                case 135:
//#line 297 "gramatica.y"
                {
                    SA2(">");
                }
                break;
                case 136:
//#line 298 "gramatica.y"
                {
                    SA2("<");
                }
                break;
                case 137:
//#line 299 "gramatica.y"
                {
                    SA2("==");
                }
                break;
                case 138:
//#line 300 "gramatica.y"
                {
                    SA2("!=");
                }
                break;
                case 139:
//#line 304 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 140:
//#line 305 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 142:
//#line 307 "gramatica.y"
                {
                    error("Falta el segundo operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 143:
//#line 308 "gramatica.y"
                {
                    error("Falta el segundo operando en la resta", analizadorLexico.getNroLinea());
                }
                break;
                case 144:
//#line 309 "gramatica.y"
                {
                    error("Falta el primer operando en la suma", analizadorLexico.getNroLinea());
                }
                break;
                case 145:
//#line 312 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 146:
//#line 313 "gramatica.y"
                {
                    SA2(val_peek(1).sval);
                }
                break;
                case 148:
//#line 315 "gramatica.y"
                {
                    error("Falta el segundo operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 149:
//#line 316 "gramatica.y"
                {
                    error("Falta el segundo operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 150:
//#line 317 "gramatica.y"
                {
                    error("Falta el primer operando en la multiplicación", analizadorLexico.getNroLinea());
                }
                break;
                case 151:
//#line 318 "gramatica.y"
                {
                    error("Falta el primer operando en la division", analizadorLexico.getNroLinea());
                }
                break;
                case 152:
//#line 321 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 153:
//#line 322 "gramatica.y"
                {
                    yyval = val_peek(0);
                    SA1(val_peek(0).sval);
                }
                break;
                case 154:
//#line 325 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkPositivo(cte);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 155:
//#line 332 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
                    }
                }
                break;
//#line 1577 "Parser.java"
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
