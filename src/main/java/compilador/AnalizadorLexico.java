package compilador;

public class AnalizadorLexico {


    private StringBuilder codigoFuente;
    private int nroLinea = 1;
    private boolean verbose;

    //TODO recordar modificar el informe en estado 1 con _ ; estado 11 con -
    private final int[][] estados = {
              //  l    L    d    _   "l"   .   "f"   %    +    -    =     * / { } ( ) , ;    "   > <   !   \n   " " \t   $  otro
                { 1,   2,   3,  1,   1,   5,   1,   9,  16,  16,  14,          16,         11,  14,  15,   0,    0,    16,  16},
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



    public AnalizadorLexico(StringBuilder codigoFuente, boolean verbose) {
        this.codigoFuente = codigoFuente;
        this.asignarAS();
        this.verbose = verbose;
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
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS6, AS7, AS7, AS2,         AS7,        AS2, AS2, AS2, AS8,  AS6  , AS6, AS4},
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
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS7, AS2, AS2, AS4,  AS2  , AS2, AS2},
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
            case '*':
            case '/':
            case '{':
            case '}':
            case '(':
            case ')':
            case ',':
            case ';':
                return 11;
            case '"': return 12;
            case '>':
            case '<':
                return 13;
            case '!': return 14;
            case '\n': return 15;
            case ' ':
            case '\t':
                return 16;
            case '$': return 17;
            default: return 18;
        }
    }

    public boolean endOfFile() {return codigoFuente.length() == 0;}

    public int yylex() {
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
            Parser.yylval = new ParserVal(token.getLexema());
            if (!token.getTipoToken().isEmpty())
                out(token.getTipoToken() + token.getLexema(), nroLinea);
            else
                out(token.getLexema(), nroLinea);
        }
        return token != null ? token.getIdToken() : -1;
    }

    public int getNroLinea() {
        return nroLinea;
    }

    private Token addToken(String lex, String tipo){
        Token token;
        if (TablaSimbolos.existe(lex)){
            token = TablaSimbolos.getToken(lex);
            int cont = ((Integer) token.getAtributo("contador")) + 1;
            token.addAtributo("contador", cont);
        }
        else {
            int id = 0;
            if(tipo.equals("IDENTIFICADOR")) {
                id = TablaSimbolos.getId("id");
            } else {
                if(tipo.equals("CADENA MULT")) id = TablaSimbolos.getId("cadenaMult");
                else id = TablaSimbolos.getId("cte");
            }
            token = new Token(id, tipo, lex);
            token.addAtributo("contador", 1);
            TablaSimbolos.add(token);
        }
        return token;
    }

    public void out(String mensaje, int linea){
        if (verbose)
            System.out.printf( Main.ANSI_GRAY + "[AL] | Linea %d: | " + mensaje +" %n" + Main.ANSI_RESET, linea);

    }

    public void error(String mensaje, int linea){
        if (verbose)
            System.out.printf( Main.ANSI_RED + "[AL] | Linea %d: | " + mensaje +" %n" + Main.ANSI_RESET, linea);

    }

    public void warning(String mensaje, int linea){
        if (verbose)
            System.out.printf(Main.ANSI_YELLOW + "[AL] | Linea %d: "+ mensaje + "%n" + Main.ANSI_RESET, linea);
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
            boolean editado = false;


            while (lexema.charAt(0) == '_') {
                editado = true;
                lexema.deleteCharAt(0);
            }

            if (editado)
                warning("Identificador comezaba con '_'. Se modifica a: "+ lexema, nroLinea);


            if(lexema.length() > 20) {
                String lexemaAnt = lexema.toString();
                lexema = new StringBuilder(lexema.substring(0, 20));
                warning("Identificador con más de 20 caracteres: + " + lexemaAnt + "Se trunca a: "+ lexema,nroLinea);
             }


            Token token;
            if(TablaSimbolos.reservada(lexema.toString()) == -1) {
                token = addToken(lexema.toString(), "IDENTIFICADOR");
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
            if(enteroLargo >= 0 && enteroLargo <= Main.MAX_LONG) {
                //verificar si
                // esta en TS y agregar
                token = addToken(lexema.substring(0, lexema.length()-2), "LONGINT");

            } else {
                warning("Entero largo fuera de rango: " + lexema.substring(0, lexema.length()-2) + " - Se cambia por: " + Main.MAX_LONG, nroLinea);
                token = addToken(String.valueOf(Main.MAX_LONG), "LONGINT");
            }

            return token;
        }
    }

    public class AS4 implements AccionSemantica {
        // Caracter invalido

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            error("Caracter inválido: " + ultimo, nroLinea);
           lexema.setLength(0);
            return null;
        }
    }

    public class AS5 implements AccionSemantica {
        //Flotantes

        @Override
        public Token execute(StringBuilder lexema, char ultimo) {
            codigoFuente.insert(0, ultimo);
            String lex;
            if(lexema.toString().contains("f"))
                lex = lexema.toString().replace("f", "e");
            else
                lex = lexema.toString();

//            float flotante, real = -1, exponente = 0;
//
//            if(lexema.toString().contains("f"))
//                real = Float.parseFloat(lexema.toString().split("f")[0]);
//            else
//                real = Float.parseFloat(lexema.toString());
//
//            if(lexema.toString().contains("f"))
//                exponente = Float.parseFloat(lexema.toString().split("f")[1]);
//
//
//            flotante = real * (float)Math.pow(10, exponente);

            float flotante = 0f;
            try {
                flotante = Float.parseFloat(lex);
            }
            catch (NumberFormatException e){
                error("Flotante incorrecto " + lexema, nroLinea);
            }

            //verificar rango
            Token token = null;
            if(( Main.MIN_FLOAT < flotante && flotante < Main.MAX_FLOAT) || flotante == 0 ) {
                //verificar si esta en TS y agregar
                token = addToken(String.valueOf(flotante), "FLOAT");
            } else {
                float nuevo = Main.MAX_FLOAT-1;
                warning("Flotante fuera de rango: " + lexema + " - Se cambia por: " + flotante, nroLinea);
                token = addToken(String.valueOf(nuevo), "FLOAT");
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

            Token token = null;
            if(TablaSimbolos.reservada(lexema.toString()) == -1) {
                token = addToken(lexema.toString(), "CADENA MULT");
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

            //En caso de las cadenas, donde es necesario eliminar los guiones
            if (lexema.length() > 0) {
                int ult = lexema.length()-1;
                if (lexema.charAt(ult) == '-')
                    lexema.replace(ult, ult+1, " ");
            }
            return null;
        }
    }





}