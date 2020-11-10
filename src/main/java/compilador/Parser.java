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
//#line 19 "Parser.java"


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
            10, 10, 10, 10, 10, 22, 22, 22, 22, 22,
            24, 24, 23, 23, 11, 11, 11, 11, 11, 11,
            11, 12, 12, 12, 12, 12, 13, 13, 13, 13,
            13, 13, 13, 13, 13, 13, 13, 27, 27, 27,
            27, 27, 27, 27, 27, 27, 21, 21, 21, 21,
            28, 28, 28, 28, 28, 28, 26, 26, 26, 26,
            26, 26, 29, 29, 29, 29, 29, 29, 29, 25,
            25, 30, 30,
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
            1, 3, 1, 1, 10, 10, 10, 10, 10, 9,
            10, 10, 10, 10, 9, 3, 3, 3, 3, 2,
            3, 1, 1, 1, 5, 5, 4, 5, 5, 4,
            5, 4, 4, 4, 4, 3, 5, 4, 4, 3,
            4, 3, 5, 4, 3, 4, 3, 5, 3, 1,
            7, 3, 3, 4, 4, 2, 3, 2, 3, 3,
            1, 1, 1, 1, 1, 1, 3, 3, 1, 3,
            3, 3, 3, 3, 1, 3, 3, 3, 3, 1,
            1, 1, 2,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 25, 26, 0, 0, 0, 0,
            0, 10, 11, 9, 12, 13, 14, 15, 16, 17,
            18, 0, 0, 0, 0, 160, 162, 0, 0, 0,
            0, 155, 0, 0, 161, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            8, 0, 0, 0, 0, 0, 145, 141, 142, 146,
            143, 144, 0, 0, 0, 0, 0, 83, 7, 0,
            0, 80, 0, 163, 0, 0, 78, 138, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 125,
            0, 0, 0, 0, 0, 122, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 120, 0, 21, 0, 24,
            20, 19, 113, 0, 0, 158, 159, 0, 0, 0,
            0, 82, 77, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 156, 153, 157, 154, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 107, 0, 114, 133, 0, 132, 0, 58, 0,
            0, 0, 0, 118, 56, 0, 0, 0, 124, 0,
            115, 112, 121, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 119, 22, 6, 84, 0, 75, 0, 0,
            0, 65, 0, 0, 0, 0, 0, 62, 97, 98,
            99, 96, 0, 0, 0, 0, 0, 0, 106, 108,
            109, 105, 111, 135, 134, 0, 123, 59, 57, 0,
            0, 0, 0, 0, 117, 0, 0, 0, 0, 0,
            0, 0, 0, 46, 0, 0, 0, 0, 0, 0,
            67, 0, 0, 0, 0, 60, 0, 0, 0, 0,
            103, 104, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 52, 51, 0, 0, 0, 0, 0, 0, 0,
            47, 0, 0, 0, 0, 0, 0, 0, 76, 0,
            66, 0, 71, 73, 0, 64, 63, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 30, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 32, 0, 68, 72, 61, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 131, 29, 0,
            34, 0, 36, 0, 39, 0, 41, 43, 45, 28,
            38, 0, 0, 0, 0, 31, 0, 0, 0, 0,
            102, 95, 0, 0, 0, 0, 0, 0, 90, 54,
            33, 35, 37, 40, 42, 44, 27, 86, 87, 0,
            0, 3, 4, 88, 89, 91, 92, 93, 94, 85,
            0, 0, 101, 2, 5,
    };
    final static short yydgoto[] = {10,
            11, 381, 12, 13, 196, 69, 15, 16, 17, 18,
            19, 20, 21, 22, 54, 103, 104, 30, 70, 197,
            31, 86, 263, 362, 32, 33, 45, 66, 34, 35,
    };
    final static short yysindex[] = {549,
            -2, 44, 8, 58, 0, 0, -27, 171, -16, 0,
            549, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, -217, 274, 848, 610, 0, 0, 5, -240, 504,
            55, 0, 891, 107, 0, -185, 285, -238, 614, 274,
            31, 42, 19, 459, 62, 97, 71, 12, 110, 139,
            0, 125, 36, -28, 464, 145, 0, 0, 0, 0,
            0, 0, 482, -41, -41, 274, 549, 0, 0, -120,
            873, 0, 154, 0, 401, -90, 0, 0, 516, 565,
            570, 573, 578, 166, 96, 148, 133, 591, -19, 191,
            388, -33, 360, 405, 157, 37, -147, 14, 214, 0,
            -56, 176, 430, 35, 15, 0, 63, 443, 414, 283,
            492, 521, 203, 205, 452, 0, 435, 0, 237, 0,
            0, 0, 0, 120, 107, 0, 0, 167, 657, 610,
            469, 0, 0, 338, 610, 470, 437, 610, 472, 120,
            107, 120, 107, 464, 167, 0, 0, 0, 0, 262,
            263, -209, 591, 591, 478, 591, 601, 483, 484, 488,
            490, 0, 495, 0, 0, 280, 0, 38, 0, 498,
            292, 294, 509, 0, 0, 295, -144, 531, 0, 517,
            0, 0, 0, 304, 543, 309, 545, -36, 326, -34,
            518, 318, 0, 0, 0, 0, 340, 0, 610, 547,
            342, 0, -2, 515, 553, 343, 348, 0, 0, 0,
            0, 0, 555, 561, 144, 563, 896, 48, 0, 0,
            0, 0, 0, 0, 0, 357, 0, 0, 0, 359,
            574, 203, 210, -142, 0, 576, 364, 584, 375, 592,
            382, 538, 135, 0, 593, -218, 391, 599, 609, 415,
            0, 645, -44, 646, -115, 0, 650, 651, 144, 144,
            0, 0, 440, 144, 144, -234, 445, 641, 610, 449,
            -142, 0, 0, 451, 664, 454, 673, 467, 679, 611,
            0, 474, -159, 477, 680, 142, 610, 485, 0, 659,
            0, 685, 0, 0, 704, 0, 0, 497, 511, 723,
            520, 523, 525, -59, 752, 542, 0, 610, 756, 610,
            534, 610, 539, 610, 544, 610, 610, 610, 610, 610,
            550, 552, 260, 0, 610, 0, 0, 0, 762, 773,
            327, 776, 786, 787, 791, -31, 327, 0, 0, 597,
            0, 610, 0, 610, 0, 610, 0, 0, 0, 0,
            0, 610, 610, 610, 610, 0, 327, 327, -4, 549,
            0, 0, 327, 327, 327, 327, 327, 633, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 119,
            676, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            807, 796, 0, 0, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            858, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            569, 0, 0, 76, 0, 0, 0, 0, 0, 0,
            29, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 6, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 590, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 32, 0, 0, 0, 0,
            9, 0, 0, -29, 0, 0, 0, 226, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 99, 0, 0, 355, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 123,
            146, 174, 198, 458, 481, 0, 0, 0, 0, 0,
            92, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            249, 0, 0, 0, 0, 0, 0, 59, 0, 0,
            0, 0, 0, 0, 0, 0, 0, -26, 0, 303,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 378, 0, 413, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, -25, -23, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 61, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 425, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, -20, 0,
            0, 0, 0, 0, 0, 134, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 9, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            798, 0, 626, -296, -24, 41, 0, 0, 0, 0,
            0, 0, 0, 850, -42, -18, -62, 0, -6, -109,
            -1, 831, -198, 607, 65, -21, -9, 836, 446, 0,
    };
    final static int YYTABLESIZE = 1156;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{50,
                68, 56, 174, 29, 241, 68, 246, 161, 127, 368,
                120, 50, 42, 43, 53, 55, 23, 49, 95, 267,
                48, 303, 108, 76, 49, 201, 73, 206, 207, 115,
                122, 74, 105, 44, 90, 380, 43, 285, 52, 157,
                14, 178, 261, 262, 128, 72, 211, 37, 127, 29,
                68, 51, 114, 53, 170, 180, 44, 286, 23, 145,
                298, 299, 212, 383, 23, 301, 302, 304, 137, 130,
                84, 389, 136, 179, 97, 149, 194, 106, 177, 119,
                166, 226, 101, 28, 395, 85, 155, 130, 29, 250,
                136, 185, 187, 190, 255, 77, 318, 39, 152, 129,
                100, 128, 109, 94, 64, 63, 266, 14, 167, 65,
                112, 232, 319, 113, 233, 149, 149, 129, 149, 128,
                149, 181, 150, 168, 5, 6, 5, 6, 126, 127,
                99, 127, 99, 127, 149, 149, 110, 149, 152, 152,
                294, 152, 130, 152, 131, 147, 147, 149, 82, 295,
                100, 213, 214, 83, 216, 218, 152, 152, 152, 392,
                152, 64, 150, 150, 244, 150, 65, 150, 116, 51,
                272, 273, 138, 151, 139, 162, 162, 100, 162, 117,
                162, 150, 150, 118, 150, 147, 147, 79, 147, 80,
                147, 154, 162, 150, 133, 283, 335, 148, 149, 79,
                149, 80, 323, 123, 147, 147, 153, 147, 309, 79,
                48, 80, 336, 151, 151, 164, 151, 281, 151, 173,
                292, 152, 160, 152, 367, 116, 150, 121, 40, 26,
                27, 158, 151, 151, 169, 151, 156, 148, 148, 240,
                148, 245, 148, 41, 307, 150, 50, 150, 110, 53,
                55, 40, 49, 271, 41, 48, 148, 148, 56, 148,
                71, 23, 324, 36, 127, 116, 41, 113, 147, 127,
                147, 127, 127, 127, 127, 26, 27, 127, 127, 127,
                5, 6, 127, 339, 169, 341, 99, 343, 110, 345,
                113, 347, 348, 349, 350, 351, 151, 98, 151, 24,
                356, 96, 126, 5, 6, 25, 53, 165, 225, 99,
                5, 6, 41, 38, 26, 27, 99, 371, 29, 372,
                148, 373, 148, 184, 261, 262, 111, 374, 375, 376,
                377, 149, 149, 149, 149, 149, 149, 149, 149, 149,
                149, 149, 126, 88, 149, 149, 149, 100, 116, 149,
                116, 151, 149, 149, 152, 152, 152, 152, 152, 152,
                152, 152, 152, 152, 152, 9, 9, 152, 152, 152,
                105, 110, 152, 110, 391, 152, 152, 70, 150, 150,
                150, 150, 150, 150, 150, 150, 150, 150, 150, 41,
                282, 150, 150, 150, 140, 140, 150, 322, 23, 150,
                150, 147, 147, 147, 147, 147, 147, 147, 147, 147,
                147, 147, 69, 140, 147, 147, 147, 70, 162, 147,
                261, 262, 147, 147, 74, 126, 46, 126, 159, 151,
                151, 151, 151, 151, 151, 151, 151, 151, 151, 151,
                9, 47, 151, 151, 151, 163, 175, 151, 67, 360,
                151, 151, 69, 148, 148, 148, 148, 148, 148, 148,
                148, 148, 148, 148, 74, 113, 148, 148, 148, 171,
                176, 148, 183, 169, 148, 148, 9, 140, 5, 6,
                191, 116, 5, 6, 99, 79, 116, 80, 116, 116,
                116, 116, 192, 193, 116, 116, 116, 139, 139, 116,
                70, 182, 70, 29, 110, 64, 63, 53, 125, 110,
                65, 110, 110, 110, 110, 354, 139, 110, 110, 110,
                137, 137, 110, 67, 141, 143, 29, 198, 202, 55,
                208, 355, 186, 209, 210, 69, 215, 69, 113, 137,
                87, 219, 220, 9, 26, 27, 221, 74, 222, 74,
                224, 5, 6, 223, 9, 85, 227, 99, 126, 67,
                29, 189, 228, 126, 229, 126, 126, 126, 126, 230,
                231, 126, 126, 126, 234, 235, 126, 9, 247, 236,
                139, 242, 1, 237, 238, 239, 2, 2, 9, 3,
                3, 4, 4, 248, 5, 6, 7, 359, 280, 8,
                199, 243, 200, 137, 249, 251, 252, 257, 81, 29,
                140, 256, 258, 259, 29, 140, 140, 29, 140, 260,
                140, 264, 29, 140, 140, 140, 67, 268, 140, 79,
                269, 140, 140, 70, 270, 29, 274, 67, 70, 275,
                70, 70, 70, 70, 276, 29, 70, 70, 70, 9,
                277, 70, 278, 284, 93, 29, 134, 279, 29, 288,
                67, 2, 287, 135, 3, 136, 4, 289, 69, 5,
                6, 7, 9, 69, 8, 69, 69, 69, 69, 290,
                74, 69, 69, 69, 306, 74, 69, 74, 74, 74,
                74, 81, 203, 74, 74, 74, 9, 2, 74, 204,
                3, 205, 4, 291, 293, 5, 6, 7, 296, 297,
                8, 300, 79, 139, 107, 9, 305, 326, 139, 139,
                308, 139, 310, 139, 311, 312, 139, 139, 139, 26,
                27, 139, 67, 313, 139, 139, 137, 124, 314, 315,
                321, 137, 137, 327, 137, 317, 137, 113, 320, 137,
                137, 137, 26, 27, 137, 360, 325, 137, 137, 1,
                5, 6, 328, 331, 2, 75, 99, 3, 329, 4,
                253, 140, 5, 6, 7, 2, 188, 8, 3, 254,
                4, 195, 330, 5, 6, 7, 26, 27, 8, 5,
                6, 332, 337, 1, 333, 99, 334, 338, 2, 340,
                393, 3, 357, 4, 1, 342, 5, 6, 7, 2,
                344, 8, 3, 358, 4, 346, 363, 5, 6, 7,
                142, 352, 8, 353, 81, 144, 364, 365, 146, 81,
                81, 366, 81, 148, 81, 26, 27, 81, 81, 81,
                26, 27, 81, 26, 27, 79, 24, 170, 26, 27,
                79, 79, 370, 79, 174, 79, 217, 1, 79, 79,
                79, 26, 27, 79, 129, 1, 55, 89, 81, 91,
                2, 26, 27, 3, 0, 4, 0, 0, 5, 6,
                7, 26, 316, 8, 26, 27, 92, 0, 1, 64,
                63, 102, 0, 2, 65, 0, 3, 102, 4, 0,
                0, 5, 6, 7, 0, 0, 8, 62, 0, 61,
                0, 0, 1, 132, 64, 63, 0, 2, 0, 65,
                3, 0, 4, 0, 0, 5, 6, 7, 0, 0,
                8, 1, 62, 79, 61, 80, 2, 64, 63, 3,
                0, 4, 65, 369, 5, 6, 7, 0, 172, 8,
                62, 0, 61, 102, 265, 62, 361, 61, 0, 102,
                102, 102, 361, 378, 379, 0, 0, 0, 0, 384,
                385, 386, 387, 388, 390, 0, 0, 0, 0, 0,
                0, 0, 361, 361, 0, 382, 0, 0, 361, 361,
                361, 361, 361, 361, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 394, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 102, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 102, 102, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 57, 58, 59, 60, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                102, 0, 0, 0, 0, 0, 0, 0, 0, 57,
                58, 59, 60, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 78, 57, 58, 59,
                60, 0, 57, 58, 59, 60,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{9,
                25, 23, 59, 45, 41, 30, 41, 41, 0, 41,
                53, 41, 40, 41, 41, 41, 61, 41, 40, 218,
                41, 256, 44, 30, 41, 135, 28, 137, 138, 48,
                59, 272, 42, 61, 273, 40, 41, 256, 256, 59,
                0, 104, 277, 278, 66, 41, 256, 40, 40, 45,
                75, 11, 41, 271, 41, 41, 61, 276, 61, 81,
                259, 260, 272, 360, 59, 264, 265, 266, 75, 41,
                256, 368, 41, 59, 44, 0, 119, 59, 44, 44,
                44, 44, 41, 40, 381, 271, 88, 59, 45, 199,
                59, 110, 111, 112, 204, 41, 256, 40, 0, 41,
                59, 41, 41, 39, 42, 43, 59, 67, 256, 47,
                40, 256, 272, 256, 177, 40, 41, 59, 43, 59,
                45, 59, 0, 271, 269, 270, 269, 270, 64, 65,
                275, 123, 275, 125, 59, 60, 40, 62, 40, 41,
                256, 43, 263, 45, 265, 0, 82, 83, 42, 265,
                59, 153, 154, 47, 156, 157, 61, 59, 60, 41,
                62, 42, 40, 41, 189, 43, 47, 45, 59, 129,
                233, 234, 263, 0, 265, 42, 43, 59, 45, 41,
                47, 59, 60, 59, 62, 40, 41, 43, 43, 45,
                45, 59, 59, 61, 41, 61, 256, 0, 123, 43,
                125, 45, 61, 59, 59, 60, 59, 62, 271, 43,
                40, 45, 272, 40, 41, 59, 43, 242, 45, 276,
                265, 123, 256, 125, 256, 0, 61, 256, 256, 271,
                272, 41, 59, 60, 271, 62, 256, 40, 41, 276,
                43, 276, 45, 271, 269, 123, 276, 125, 0, 276,
                276, 256, 276, 44, 271, 276, 59, 60, 280, 62,
                256, 256, 287, 256, 256, 40, 271, 256, 123, 261,
                125, 263, 264, 265, 266, 271, 272, 269, 270, 271,
                269, 270, 274, 308, 271, 310, 275, 312, 40, 314,
                256, 316, 317, 318, 319, 320, 123, 256, 125, 256,
                325, 271, 0, 269, 270, 262, 271, 271, 271, 275,
                269, 270, 271, 256, 271, 272, 275, 342, 45, 344,
                123, 346, 125, 41, 277, 278, 256, 352, 353, 354,
                355, 256, 257, 258, 259, 260, 261, 262, 263, 264,
                265, 266, 40, 59, 269, 270, 271, 256, 123, 274,
                125, 256, 277, 278, 256, 257, 258, 259, 260, 261,
                262, 263, 264, 265, 266, 40, 40, 269, 270, 271,
                380, 123, 274, 125, 256, 277, 278, 0, 256, 257,
                258, 259, 260, 261, 262, 263, 264, 265, 266, 271,
                256, 269, 270, 271, 40, 41, 274, 256, 61, 277,
                278, 256, 257, 258, 259, 260, 261, 262, 263, 264,
                265, 266, 0, 59, 269, 270, 271, 40, 59, 274,
                277, 278, 277, 278, 0, 123, 256, 125, 41, 256,
                257, 258, 259, 260, 261, 262, 263, 264, 265, 266,
                40, 271, 269, 270, 271, 41, 271, 274, 123, 123,
                277, 278, 40, 256, 257, 258, 259, 260, 261, 262,
                263, 264, 265, 266, 40, 256, 269, 270, 271, 256,
                41, 274, 59, 271, 277, 278, 40, 123, 269, 270,
                276, 256, 269, 270, 275, 43, 261, 45, 263, 264,
                265, 266, 41, 59, 269, 270, 271, 40, 41, 274,
                123, 59, 125, 45, 256, 42, 43, 271, 63, 261,
                47, 263, 264, 265, 266, 256, 59, 269, 270, 271,
                40, 41, 274, 123, 79, 80, 45, 59, 59, 256,
                59, 272, 41, 272, 272, 123, 59, 125, 256, 59,
                256, 59, 59, 40, 271, 272, 59, 123, 59, 125,
                271, 269, 270, 59, 40, 271, 59, 275, 256, 123,
                45, 41, 271, 261, 271, 263, 264, 265, 266, 61,
                276, 269, 270, 271, 44, 59, 274, 40, 61, 276,
                123, 256, 256, 41, 276, 41, 261, 261, 40, 264,
                264, 266, 266, 276, 269, 270, 271, 271, 61, 274,
                263, 276, 265, 123, 265, 59, 265, 265, 40, 45,
                256, 59, 265, 59, 45, 261, 262, 45, 264, 59,
                266, 59, 45, 269, 270, 271, 123, 271, 274, 40,
                272, 277, 278, 256, 61, 45, 61, 123, 261, 276,
                263, 264, 265, 266, 61, 45, 269, 270, 271, 40,
                276, 274, 61, 61, 41, 45, 256, 276, 45, 61,
                123, 261, 272, 263, 264, 265, 266, 59, 256, 269,
                270, 271, 40, 261, 274, 263, 264, 265, 266, 265,
                256, 269, 270, 271, 44, 261, 274, 263, 264, 265,
                266, 123, 256, 269, 270, 271, 40, 261, 274, 263,
                264, 265, 266, 59, 59, 269, 270, 271, 59, 59,
                274, 272, 123, 256, 256, 40, 272, 59, 261, 262,
                272, 264, 272, 266, 61, 272, 269, 270, 271, 271,
                272, 274, 123, 61, 277, 278, 256, 256, 272, 61,
                61, 261, 262, 59, 264, 272, 266, 256, 272, 269,
                270, 271, 271, 272, 274, 123, 272, 277, 278, 256,
                269, 270, 59, 41, 261, 262, 275, 264, 272, 266,
                256, 256, 269, 270, 271, 261, 256, 274, 264, 265,
                266, 125, 272, 269, 270, 271, 271, 272, 274, 269,
                270, 272, 41, 256, 272, 275, 272, 256, 261, 44,
                125, 264, 41, 266, 256, 272, 269, 270, 271, 261,
                272, 274, 264, 41, 266, 272, 41, 269, 270, 271,
                256, 272, 274, 272, 256, 256, 41, 41, 256, 261,
                262, 41, 264, 256, 266, 271, 272, 269, 270, 271,
                271, 272, 274, 271, 272, 256, 256, 41, 271, 272,
                261, 262, 256, 264, 59, 266, 256, 0, 269, 270,
                271, 271, 272, 274, 67, 256, 256, 37, 33, 256,
                261, 271, 272, 264, -1, 266, -1, -1, 269, 270,
                271, 271, 272, 274, 271, 272, 273, -1, 256, 42,
                43, 42, -1, 261, 47, -1, 264, 48, 266, -1,
                -1, 269, 270, 271, -1, -1, 274, 60, -1, 62,
                -1, -1, 256, 41, 42, 43, -1, 261, -1, 47,
                264, -1, 266, -1, -1, 269, 270, 271, -1, -1,
                274, 256, 60, 43, 62, 45, 261, 42, 43, 264,
                -1, 266, 47, 337, 269, 270, 271, -1, 99, 274,
                60, -1, 62, 104, 59, 60, 331, 62, -1, 110,
                111, 112, 337, 357, 358, -1, -1, -1, -1, 363,
                364, 365, 366, 367, 368, -1, -1, -1, -1, -1,
                -1, -1, 357, 358, -1, 360, -1, -1, 363, 364,
                365, 366, 367, 368, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 381, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 177, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, 233, 234, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, 257, 258, 259, 260, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                271, -1, -1, -1, -1, -1, -1, -1, -1, 257,
                258, 259, 260, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 256, 257, 258, 259,
                260, -1, 257, 258, 259, 260,
        };
    }

    final static short YYFINAL = 10;
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
            "sentencia_control : FOR error inicio_for ';' condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' error ';' condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for error condicion ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' error ';' incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion incr_decr CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' error CTE ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr error ')' bloque_for",
            "sentencia_control : FOR '(' inicio_for ';' condicion ';' incr_decr CTE error bloque_for",
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

//#line 347 "gramatica.y"

    private AnalizadorLexico analizadorLexico;
    private int nroUltimaLinea;

    public Parser(AnalizadorLexico analizadorLexico, boolean debug) {
        this.analizadorLexico = analizadorLexico;
        this.yydebug = debug;
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


    public String checkRango(String cte, boolean negativo) {
        Token token = TablaSimbolos.getToken(cte);
        String tipo = token.getTipoToken();

        if (tipo.equals("LONGINT")) {
            long entero = 0;
            String nuevoLexema = null;
            if (negativo) {
                if (Long.parseLong(cte) <= Main.MAX_LONG) {
                    entero = Long.parseLong(cte);
                } else {
                    entero = Main.MAX_LONG;
                    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Entero largo negativo fuera de rango: %s - Se cambia por: %d%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, entero);
                }
                nuevoLexema = "-" + entero;

            } else {
                if (Long.parseLong(cte) >= Main.MAX_LONG) {
                    entero = Main.MAX_LONG - 1;
                    System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Entero largo positivo fuera de rango: %s - Se cambia por: %d %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, entero);
                    nuevoLexema = String.valueOf(entero);
                }
            }
            if (nuevoLexema != null) {
                cambiarSimbolo(token, cte, nuevoLexema, "LONGINT");
                return nuevoLexema;
            }
        }
        if (tipo.equals("FLOAT")) {
            float flotante = 0f;
            if ((Main.MIN_FLOAT < Float.parseFloat(cte) && Float.parseFloat(cte) < Main.MAX_FLOAT)) {
                flotante = Float.parseFloat(cte);
            } else {
                flotante = Main.MAX_FLOAT - 1;
                System.out.printf(Main.ANSI_YELLOW + "[AS] | Linea %d: Flotante negativo fuera de rango: %s - Se cambia por: %d%n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), cte, flotante);
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

    //#line 810 "Parser.java"
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
//#line 34 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 5:
//#line 35 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 19:
//#line 62 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Declaración de variables %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 20:
//#line 63 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' " + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 21:
//#line 64 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta lista de variables " + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 23:
//#line 68 "gramatica.y"
                {
                    String id = val_peek(0).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("USO", "VARIABLE");
                    }
                }
                break;
                case 24:
//#line 73 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' " + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 27:
//#line 80 "gramatica.y"
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
                        System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 28:
//#line 92 "gramatica.y"
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
                        System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Tipo incorrecto de CTE NI %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 29:
//#line 104 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 30:
//#line 105 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada PROC en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 31:
//#line 106 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 32:
//#line 107 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta definir el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 33:
//#line 108 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 34:
//#line 109 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el identificador en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 35:
//#line 110 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 36:
//#line 111 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 37:
//#line 112 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en la lista de parámetros formales en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 38:
//#line 113 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 39:
//#line 114 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 40:
//#line 115 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 41:
//#line 116 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 42:
//#line 117 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 43:
//#line 118 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 44:
//#line 119 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 45:
//#line 120 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta constante NI en sentencia de declaración de procedimiento %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 46:
//#line 121 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 47:
//#line 122 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta control de invocaciones %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 48:
//#line 125 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 49:
//#line 126 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 50:
//#line 127 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros formales (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 51:
//#line 128 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los primeros dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 52:
//#line 129 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los últimos dos parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 53:
//#line 130 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre los parámetros formales %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 54:
//#line 131 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros formales permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 55:
//#line 132 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Parámetro formal incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 56:
//#line 135 "gramatica.y"
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
//#line 143 "gramatica.y"
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
//#line 151 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 59:
//#line 152 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el tipo del parámetro formal %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 60:
//#line 155 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 61:
//#line 156 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 62:
//#line 157 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 63:
//#line 158 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 64:
//#line 159 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 65:
//#line 160 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 66:
//#line 161 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 67:
//#line 162 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias THEN  %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 68:
//#line 163 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias THEN %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 69:
//#line 164 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 70:
//#line 165 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF y literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 71:
//#line 166 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta bloque de sentencias ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 72:
//#line 167 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en bloque de sentencias ELSE %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 73:
//#line 168 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta palabra reservada END_IF %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 74:
//#line 169 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de selección IF %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 75:
//#line 170 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 76:
//#line 171 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta la condicion del IF %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 78:
//#line 175 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis '(' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 79:
//#line 176 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta parentesis ')' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 80:
//#line 177 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 81:
//#line 178 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Faltan parentesis %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 82:
//#line 179 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en la condicion %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 85:
//#line 189 "gramatica.y"
                {
                    String cte = val_peek(2).sval;
                    if (TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de control FOR %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    else
                        System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 86:
//#line 196 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 87:
//#line 197 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el inicio de la variable de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 88:
//#line 198 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de expresión de inicialización en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 89:
//#line 199 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 90:
//#line 200 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' luego de condición de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 91:
//#line 201 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta indicar incremento o decremento de la sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 92:
//#line 202 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta indicar constante de paso para incremento/decremento en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 93:
//#line 203 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 94:
//#line 204 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en el cuerpo de la sentencia de control. Se encontró referencia a sentencia declarativa. %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 95:
//#line 205 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta identificador de control en sentencia de control %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 96:
//#line 208 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    if (!TablaSimbolos.getToken(cte).getTipoToken().equals("LONGINT"))
                        System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Constante no es del tipo entero %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 97:
//#line 213 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: error inicio for en el identificador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 98:
//#line 214 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: error inicio for en el '=' %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 99:
//#line 215 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: error inicio for en la constante %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 100:
//#line 216 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: error inicio for %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 105:
//#line 228 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de salida OUT %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String cadena = val_peek(2).sval;
                    System.out.printf(cadena);
                }
                break;
                case 106:
//#line 232 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 107:
//#line 233 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 108:
//#line 234 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Error en la cadena multilínea a imprimir en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 109:
//#line 235 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de salida %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 110:
//#line 236 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' en sentencia de salida %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 111:
//#line 237 "gramatica.y"
                {
                    String lex = val_peek(2).sval;
                    Token token = TablaSimbolos.getToken(lex);
                    if (token != null) {
                        if (token.getAtributo("USO") != null) {
                            if (token.getTipoToken().equals("IDENTIFICADOR") && token.getAtributo("USO").equals("VARIABLE")) {
                                Object valor = token.getAtributo("VALOR");
                                if (valor != null)
                                    System.out.println((String) token.getAtributo("VALOR"));
                                else
                                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Variable '%s' no inicializada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), lex);
                            } else if (token.getTipoToken().equals("LONGINT") || token.getTipoToken().equals("FLOAT"))
                                System.out.println(token.getLexema());
                        } else
                            System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Variable '%s' no declarada %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), lex);
                    }
                }
                break;
                case 112:
//#line 256 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                    String id = val_peek(3).sval;
                    String cte = val_peek(1).sval;
                    Token token = TablaSimbolos.getToken(id);
                    if (token != null) {
                        token.addAtributo("VALOR", cte);
                    }
                }
                break;
                case 113:
//#line 264 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta lado izquierdo de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 114:
//#line 265 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '=' en sentencia de asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 115:
//#line 266 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta lado derecho de la asignación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 116:
//#line 267 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ';' al final de la asignación %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 117:
//#line 270 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación con lista de parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 118:
//#line 271 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Sentencia de invocación sin parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 119:
//#line 272 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 120:
//#line 273 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta el identificador del procedimiento a invocar %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 121:
//#line 274 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 122:
//#line 275 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal '(' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 123:
//#line 276 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Parámetros inválidos %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 124:
//#line 277 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 125:
//#line 278 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ')' en sentencia de invocación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 126:
//#line 279 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 127:
//#line 280 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta literal ';' en sentencia de invocación %n" + Main.ANSI_RESET, nroUltimaLinea);
                }
                break;
                case 128:
//#line 283 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (3) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 129:
//#line 284 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (2) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 130:
//#line 285 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Lista de parámetros (1) %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 131:
//#line 286 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Número de parámetros permitidos excedido %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 132:
//#line 287 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Parámetro incorrecto %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 133:
//#line 288 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Faltan literales ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 134:
//#line 289 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 135:
//#line 290 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 136:
//#line 291 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta literal ',' entre parámetros %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 137:
//#line 295 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Comparación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 138:
//#line 296 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta comparador %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 139:
//#line 297 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta el segundo operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 140:
//#line 298 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: Falta el primer operando de la condición %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 143:
//#line 303 "gramatica.y"
                {
                    yyval = new ParserVal('>');
                }
                break;
                case 144:
//#line 304 "gramatica.y"
                {
                    yyval = new ParserVal('<');
                }
                break;
                case 147:
//#line 309 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 148:
//#line 310 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 150:
//#line 312 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 151:
//#line 313 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la resta %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 152:
//#line 314 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d:| Falta el primer operando en la suma %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 153:
//#line 317 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 154:
//#line 318 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: División %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 156:
//#line 320 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 157:
//#line 321 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el segundo operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 158:
//#line 322 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la multiplicación %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 159:
//#line 323 "gramatica.y"
                {
                    System.out.printf(Main.ANSI_RED + "[AS] | Linea %d: | Falta el primer operando en la división %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea());
                }
                break;
                case 162:
//#line 330 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte, false);
                    if (nuevo != null)
                        yyval = new ParserVal(nuevo);
                    else
                        yyval = new ParserVal(cte);
                }
                break;
                case 163:
//#line 337 "gramatica.y"
                {
                    String cte = val_peek(0).sval;
                    String nuevo = checkRango(cte, true);
                    if (nuevo != null) {
                        yyval = new ParserVal(nuevo);
                        System.out.printf(Main.ANSI_GREEN + "[AS] | Linea %d: Constante negativa %s %n" + Main.ANSI_RESET, analizadorLexico.getNroLinea(), nuevo);
                    }
                }
                break;
//#line 1563 "Parser.java"
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
