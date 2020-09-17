package compilador;

public class AnalizadorLexico {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    private StringBuilder codigoFuente;
    private int nroLinea = 1;

    private int[][] estados = {
              //  l    L    d    _   "l"   .   "f"   %    +    -    =     * / { } ( ) , ;    "   > <   !   \n   " " \t   $  otro
                { 1,   2,   3,  16,   1,   5,   1,   9,  16,  16,  14,          16,         11,  14,  15,   0,    0,    16,  16},
                { 1,  16,   1,   1,   1,  16,   1,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,   2,  16,   2,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,   3,   4,  16,   6,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,  16,  16,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,   6,  16,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,   6,  16,  16,  16,   7,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,  16,  16,  16,  16,  16,  16,   8,   8,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,   8,  16,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,  16,  16,  16,  16,  16,  10,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,          10,         10,  10,  10,   0,   10,    16,  10},
                {11,  11,  11,  11,  11,  11,  11,  11,  11,  12,  11,          11,         16,  11,  11,  16,   11,    16,  11},
                {11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11,          11,         11,  11,  11,  13,   11,    16,  11},
                {11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11,          11,         16,  11,  11,  11,   11,    16,  11},
                {16,  16,  16,  16,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16},
                {16,  16,  16,  16,  16,  16,  16,  16,  16,  16,  16,          16,         16,  16,  16,  16,   16,    16,  16}
    };

    private AccionSemantica[][] accionesSemanticas;



    public AnalizadorLexico() {
        this.codigoFuente = FileManager.loadCodigoFuente("CasosDePrueba/Test3.txt");
        this.asignarAS();
        while(!endOfFile())
            getSigToken();
    }

    private void asignarAS() {
        AccionSemantica AS1 = new AS1();
        AccionSemantica AS2 = new AS2();
        AccionSemantica AS3 = new AS3();
        AccionSemantica AS4 = new AS4();
        AccionSemantica AS5 = new AS5();
        AccionSemantica AS6 = new AS6();
        AccionSemantica AS7 = new AS7();
        AccionSemantica AS8 = new AS8();

        accionesSemanticas = new AccionSemantica[][] {
              //  l    L    d    _   "l"   .   "f"   %    +    -    =     * / { } ( ) , ;    "   > <   !   \n   " " \t   $   otro
                {AS2, AS2, AS2, AS4, AS2, AS2, AS2, AS6, AS7, AS7, AS2,         AS7,        AS2, AS2, AS2, AS8,  AS6  , AS6, AS4},
                {AS2, AS1, AS2, AS2, AS2, AS1, AS2, AS1, AS1, AS1, AS1,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
                {AS1, AS2, AS1, AS2, AS1, AS1, AS1, AS1, AS1, AS1, AS1,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
                {AS4, AS4, AS2, AS2, AS4, AS2, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
                {AS4, AS4, AS4, AS4, AS3, AS4, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
                {AS4, AS4, AS2, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
                {AS5, AS5, AS2, AS5, AS5, AS5, AS2, AS5, AS5, AS5, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
                {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS2, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
                {AS5, AS5, AS2, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
                {AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS6, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
                {AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,         AS6,        AS6, AS6, AS6, AS8,  AS6  , AS6, AS6},
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS6, AS2,         AS2,        AS7, AS2, AS2, AS4,  AS2  , AS2, AS2},
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS2, AS2, AS2, AS8,  AS2  , AS2, AS2},
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS7, AS2, AS2, AS2,  AS2  , AS2, AS2},
                {AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS7,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
                {AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS7,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4}
        };
    }

    public int getIndiceCaracterMatrizEstados(char caracter) {
        int asciiCode = (int) caracter;
        if (asciiCode == 108) return 4;
        if (asciiCode == 102) return 6;
        if (asciiCode >= 97 && asciiCode <= 122) return 0;
        if (asciiCode >= 65 && asciiCode <= 90) return 1;
        if (asciiCode >= 48 && asciiCode <= 57) return 2;
        switch (caracter) {
            case '_': return 3;
            case '.': return 5;
            case '%': return 7;
            case '+': return 8;
            case '-': return 9;
            case '=': return 10;
            case '*': return 11;
            case '/': return 11;
            case '{': return 11;
            case '}': return 11;
            case '(': return 11;
            case ')': return 11;
            case ',': return 11;
            case ';': return 11;
            case '"': return 12;
            case '>': return 13;
            case '<': return 13;
            case '!': return 14;
            case '\n': return 15;
            case ' ': return 16;
            case '\t': return 16;
            case '$': return 17;
            default: return 18;
        }
    }

    public boolean endOfFile() {return codigoFuente.length() == 0;}

    public Token getSigToken() {
        StringBuilder lexemaActual = new StringBuilder();
        int estado = 0;
        Token token = null;

        while (estado != 16) {
            char caracter = '$';
            if(!endOfFile()) {
                caracter = codigoFuente.charAt(0);
                codigoFuente.deleteCharAt(0);
            }

            int indiceCaracter = getIndiceCaracterMatrizEstados(caracter);
            token = accionesSemanticas[estado][indiceCaracter].execute(lexemaActual, caracter);
            estado = estados[estado][indiceCaracter];
        }

        if(token != null) {
            //yylval = new ParserVal(token.getLexema());
            if (!token.getTipoToken().isEmpty())
                System.out.printf("%s %s%n", token.getTipoToken(), token.getLexema());
            else
                System.out.printf("%s%n", token.getLexema());
        }
        return token;
    }


    //ACCIONES SEMANTICAS

    public interface AccionSemantica {
        Token execute(StringBuilder lexema, char ultimo);
    }

    public class AS1 implements AccionSemantica {
        // Identificadores - Palabras reservadas

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            codigoFuente.insert(0, ultimo);

            if(lexema.length() > 20) {
                String lexemaAnt = lexema.toString();
                lexema = new StringBuilder(lexema.substring(0, 20));
                //TODO Anunciar warning al truncar
                System.out.printf(ANSI_YELLOW + "[Linea %d]- WARNING | Identificador con más de 20 caracteres: %s. Se trunca a: %s%n"  + ANSI_RESET, nroLinea, lexemaAnt, lexema);
            }


            Token token = null;
            if(TablaSimbolos.reservada(lexema.toString()) == -1) {
                token = new Token(TablaSimbolos.getId("id"), "IDENTIFICADOR", lexema.toString());
                TablaSimbolos.add(token);
            }
            else
                if(TablaSimbolos.reservada(lexema.toString()) == 0)
                    token = new Token(TablaSimbolos.getId(lexema.toString()), "", lexema.toString());
                else
                    token = new Token(TablaSimbolos.getId(lexema.toString()), "PALABRA RESERVADA", lexema.toString());

            return token;
        }
    }

    public class AS2 implements AccionSemantica {
        // Agrega caracter a lexema
        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            lexema.append(ultimo);

            return null;
        }
    }

    public class AS3 implements AccionSemantica {
        // Enteros Largos

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            lexema.append(ultimo);

            long enteroLargo = Long.parseLong(lexema.substring(0, lexema.length()-2));


            //verificar rango
            Token token = null;
            if(enteroLargo >= 0 && enteroLargo <= 2147483648L) {
                //verificar si esta en TS y agregar
                token = new Token(TablaSimbolos.getId("cte"), "LONGINT", lexema.substring(0, lexema.length()-2));
                //token.addAtributo("TIPO","LONGINT");
                TablaSimbolos.add(token);
            } else {
                System.out.printf( ANSI_RED + "[Linea %d]- ERROR | Entero largo fuera de rango: %s%n"  + ANSI_RESET, nroLinea, lexema.substring(0, lexema.length()-2) );
            }

            return token;
        }
    }

    public class AS4 implements AccionSemantica {
        // Caracter invalido

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            //TODO print error
            System.out.printf(ANSI_RED + "[Linea %d]- ERROR | Caracter inválido: %c%n" + ANSI_RESET, nroLinea, ultimo);
            return null;
        }
    }

    public class AS5 implements AccionSemantica {
        //Flotantes

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            codigoFuente.insert(0, ultimo);

            float flotante, real = -1, exponente = 1;

            if(lexema.toString().contains("f"))
                real = Float.parseFloat(lexema.toString().split("f")[0]);
            else
                real = Float.parseFloat(lexema.toString());

            if(lexema.toString().contains("f"))
                exponente = Float.parseFloat(lexema.toString().split("f")[1]);


            flotante = (float)Math.pow(real, exponente);

            //verificar rango
            Token token = null;
            if(((float)Math.pow(1.17549435,-38) < flotante && flotante < (float)Math.pow(3.40282347,38)) || flotante == 0 || ((float)Math.pow(-3.40282347,38) < flotante && flotante < (float)Math.pow(-1.17549435,-38))) {
                //verificar si esta en TS y agregar
                token = new Token(TablaSimbolos.getId("cte"), "FLOAT", String.valueOf(flotante));
                //token.addAtributo("TIPO","FLOAT");
                TablaSimbolos.add(token);
            } else {
                System.err.printf(ANSI_RED +"[Linea %d]- ERROR | Flotante fuera de rango: %s%n" + ANSI_RESET, nroLinea, lexema);
            }

            return token;
        }
    }

    public class AS6 implements AccionSemantica{
        // Acción semántica vacía
        // Utilizada para los comentarios

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            return null;
        }
    }

    public class AS7 implements AccionSemantica {
        // Agregar caracter y retornar token

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            lexema.append(ultimo);

            //TODO buscar en TS
            Token token = null;
            if(TablaSimbolos.reservada(lexema.toString()) == -1) {
                token = new Token(TablaSimbolos.getId("cadenaMult"), "CADENA MULT", lexema.toString());
                TablaSimbolos.add(token);
            }
            else if(TablaSimbolos.reservada(lexema.toString()) == 0)
                token = new Token(TablaSimbolos.getId(lexema.toString()), "", lexema.toString());

            return token;
        }
    }

    public class AS8 implements AccionSemantica{
        // Actualizar número de línea de código

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            nroLinea++;
            return null;
        }
    }

}