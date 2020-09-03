public class AnalizadorLexico {


    private StringBuilder codigoFuente;
    private StringBuilder lexemaActual;
    private int posicion = 0, nroLinea = 1;

    static final String LONGINT = "longint";

    public AnalizadorLexico() {
        this.codigoFuente = FileManager.loadCodigoFuente("CasosDePrueba/Test1.txt");
        for (int i = 0; i <= 10; i++)
            getSigToken();
    }

    //estado F: 16
    //estado vacio: -1 (revisar esto)
    private int[][] estados = {
            {1, 2, 3, -1, 1, 5, 1, 9, 16, 16, 14, 16, 11, 14, 15, 0, 0, 16, -1},
            {1, 16, 1, 1, 1, 16, 1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {16, 1, 16, 1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {-1, -1, 3, 4, 16, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1},
            {-1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1},
            {-1, -1, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1},
            {16, 16, 6, 16, 16, 16, 7, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {16, 16, 16, 16, 16, 16, 16, 16, 8, 8, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {16, 16, 8, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {-1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1},
            {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 10, 16, 10},
            {11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 11, 11, 16, 11, 11, -1, 11, 16, 11},
            {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 16, 11, 11, 13, 11, 16, 11},
            {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 16, 11, 11, 11, 11, 16, 11},
            {16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1, 16, -1}
    };

    private AccionSemantica[][] accionesSemanticas;

    // Hash identificador, constante o cadena con correspondientes atributos
    //private HashTable<String, Atributos> tablaSimbolos;

    // ¿Como diferenciar las letras l y f de la columna de letras minusculas?
    public int getIndiceCaracterMatrizEstados(char caracter) {
        int asciiCode = (int) caracter;
        if (asciiCode == 108) return 4;
        if (asciiCode == 102) return 6;
        if (asciiCode >= 97 && asciiCode <= 122) return 0;
        if (asciiCode >= 65 && asciiCode <= 90) return 1;
        if (asciiCode >= 48 && asciiCode <= 57) return 2;
        switch (caracter) {
            case '_':
                return 3;
            case '.':
                return 5;
            case '%':
                return 7;
            case '+':
                return 8;
            case '-':
                return 9;
            case '=':
                return 10;
            case '*':
                return 11;
            case '/':
                return 11;
            case '{':
                return 11;
            case '}':
                return 11;
            case '(':
                return 11;
            case ')':
                return 11;
            case ',':
                return 11;
            case ';':
                return 11;
            case '"':
                return 12;
            case '>':
                return 13;
            case '<':
                return 13;
            case '!':
                return 14;
            case '\n':
                return 15;
            case ' ':
                return 16;
            case '\t':
                return 16;
            case '$':
                return 17;
            default:
                return 18;
        }
    }

    public Token getSigToken() {
        lexemaActual = new StringBuilder();
        int estadoAct = 0;
        Token token = null;
        while (estadoAct != 16 && estadoAct != -1) { // -1 vendria a ser EOF
            char caracter = codigoFuente.charAt(posicion);
            System.out.println(caracter);
            posicion++;
            //Levantar caracter y eliminarlo del buffer
            //Agregar caracter a buffer interno

            int nuevoIndice = getIndiceCaracterMatrizEstados(caracter);
            token = accionesSemanticas[estadoAct][nuevoIndice].execute(caracter);
            estadoAct = estados[estadoAct][nuevoIndice];
        }
        return token;
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

        accionesSemanticas = {
              //  l    L    d    _   "l"   .   "f"   %    +    -    =     * / { } ( ) , ;    "   > <   !   \n   " " \t   $  otro
                {AS2, AS2, AS2, AS4, AS4, AS2, AS4, AS6, AS7, AS7, AS2,         AS7,        AS2, AS2, AS2, AS8,  AS6  , AS6, AS4},
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
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS6, AS2,         AS2,        AS7, AS2, AS2, AS4,  AS2  , AS6, AS2},
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS2, AS2, AS2, AS8,  AS2  , AS2, AS2},
                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS7, AS2, AS2, AS2,  AS2  , AS2, AS2},
                {AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS7,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
                {AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS7,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
        };

    }


    public interface AccionSemantica {
        Token execute(char ultimo);

    }

    public class AS1 implements AccionSemantica {
        //identificadores - palabras reservadas

        @Override
        public Token execute(char ultimo) {
            posicion = posicion - 1;

            if (lexemaActual.length() > 20) {
                lexemaActual = new StringBuilder(lexemaActual.substring(0, 20));
                //TODO Anunciar warning al truncar
                System.out.println("WARNING: " + /*nro linea + */ "Identificador con mas de 20 caracteres");
            }

            Token token = null;
            if (!TablaSimbolos.reservada(lexemaActual.toString())) {
                token = new Token(TablaSimbolos.getId("id"), lexemaActual.toString());
                TablaSimbolos.add(token);
            }
            else
                token = new Token(TablaSimbolos.getId(lexemaActual.toString()));
            return token;
        }
    }

    public class AS2 implements AccionSemantica {
        // Agrega caracter a lexema
        @Override
        public Token execute(char ultimo) {
            lexemaActual.append(ultimo);
            return null;
        }
    }

    public class AS3 implements AccionSemantica {
        //enteros largos

        @Override
        public Token execute(char ultimo) {
            posicion = posicion - 1;
            long enteroLargo = -1;

            try {
                enteroLargo = Long.parseLong(lexemaActual.substring(0, lexemaActual.length()-2));
            }
            catch (NumberFormatException e){
                //TODO warning de rango
                //arreglar enteroLargo
            }

            //verificar rango
            Token token = null;
            if (enteroLargo >= 0 && enteroLargo <= 2147483648L) {
                //verificar si esta en TS y agregar
                token = new Token(TablaSimbolos.getId("id"), lexemaActual.toString());
                token.addAtributo("TIPO","LONGINT");
                TablaSimbolos.add(token);
            } else {
                //TODO error?? warning?
            }

            return token;
        }
    }

    public class AS4 implements AccionSemantica {
        //caracter invalido

        @Override
        public Token execute(char ultimo) {
            //TODO print error
            return null;
        }
    }

    public class AS5 implements AccionSemantica {
        //Flotantes

        @Override
        public Token execute(char ultimo) {
            //TODO
            // devolver ultimo
            // verificar rango
            Token token = null;
            //verificar si esta en TS y agregar
            return token;
        }
    }

    public class AS6 implements AccionSemantica{

        @Override
        public Token execute(char ultimo) {
            return null;
        }
    }

    public class AS7 implements AccionSemantica {
        //Agregar caracter y retornar token

        @Override
        public Token execute(char ultimo) {
            lexemaActual.append(ultimo);
            Token token = null;
            //TODO buscar en TS
            return token;
        }
    }

    public class AS8 implements AccionSemantica{

        @Override
        public Token execute(char ultimo) {
            nroLinea++;
            return null;
        }
    }

}