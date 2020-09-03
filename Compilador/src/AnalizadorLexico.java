public class AnalizadorLexico {

    private String codigoFuente;
    private String lexemaActual;
    private int posicion = 0, nroLinea = 1;

    public AnalizadorLexico() {
        this.codigoFuente = FileManager.loadCodigoFuente("CasosDePrueba/Test1.txt");
        for(int i = 0; i <= 10 ; i++)
            getSigToken();
    }

    //estado F: 16
//estado vacio: -1 (revisar esto)
    private int[][] estados = {
            {1,2,3,-1,1,5,1,9,16,16,14,16,11,14,15,0,0,16,-1},
            {1,16,1,1,1,16,1,16,16,16,16,16,16,16,16,16,16,16,16},
            {16,1,16,1,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16},
            {-1,-1,3,4,16,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1},
            {-1,-1,-1,-1,16,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1},
            {-1,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1},
            {16,16,6,16,16,16,7,16,16,16,16,16,16,16,16,16,16,16,16},
            {16,16,16,16,16,16,16,16,8,8,16,16,16,16,16,16,16,16,16},
            {-1,-1,8,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1},
            {-1,-1,-1,-1,-1,-1,-1,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1},
            {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,0,10,16,10},
            {11,11,11,11,11,11,11,11,11,12,11,11,16,11,11,-1,11,16,11},
            {11,11,11,11,11,11,11,11,11,11,11,11,16,11,11,13,11,16,11},
            {11,11,11,11,11,11,11,11,11,11,11,11,16,11,11,11,11,16,11},
            {16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,16,-1,-1,-1,-1,-1,-1,16,-1}
    };

    private AccionSemantica[][] accionesSemanticas;

// Hash identificador, constante o cadena con correspondientes atributos
//private HashTable<String, Atributos> tablaSimbolos;

    // ¿Como diferenciar las letras l y f de la columna de letras minusculas?
    public int getIndiceCaracterMatrizEstados(char caracter) {
        int asciiCode = (int)caracter;
        if(asciiCode >= 97 && asciiCode <= 122) return 0;
        if(asciiCode >= 65 && asciiCode <= 90) return 1;
        if(asciiCode >= 48 && asciiCode <= 57) return 2;
        switch(caracter) {
            case '_' : return 3;
            case 'l' : return 4;
            case '.' : return 5;
            case 'f' : return 6;
            case '%' : return 7;
            case '+' : return 8;
            case '-' : return 9;
            case '=' : return 10;
            case '*' : return 11;
            case '/' : return 11;
            case '{' : return 11;
            case '}' : return 11;
            case '(' : return 11;
            case ')' : return 11;
            case ',' : return 11;
            case ';' : return 11;
            case '"' : return 12;
            case '>' : return 13;
            case '<' : return 13;
            case '!' : return 14;
            case '\n' : return 15;
            case ' ' : return 16;
            case '\t' : return 16;
            case '$' : return 17;
            default: return 18;
        }
    }

    public Token getSigToken() {
        lexemaActual = "";
        int estadoAct = 0;
        while(estadoAct != 16 && estadoAct != -1) { // -1 vendria a ser EOF
            System.out.println(codigoFuente.charAt(posicion));
            posicion++;
            //Levantar caracter y eliminarlo del buffer
            //Agregar caracter a buffer interno
            //ejecutar accion semantica

            //estadoAct = estados[estadoAct][/*indice columna matriz de estados*/];
        }
        return null;
    }

    private void asignarAS(){
        accionesSemanticas = new AccionSemantica[16][19];

//        AccionSemantica AS1 = new AS1();
//        AccionSemantica AS2 = new AS2();
//        AccionSemantica AS3 = new AS3();
//        AccionSemantica AS4 = new AS4();
//        AccionSemantica AS5 = new AS5();
//        AccionSemantica AS7 = new AS7();

//        accionesSemanticas = {
//              //  l    L    d    _   "l"   .   "f"   %    +    -    =     * / { } ( ) , ;    "   > <   !   \n   " " \t   $  otro
//                {AS2, AS2, AS2, AS4, AS4, AS2, AS4,    , AS7, AS7, AS2,         AS7,        AS2, AS2, AS2,    ,       ,    , AS4},
//                {AS2, AS1, AS2, AS2, AS2, AS1, AS2, AS1, AS1, AS1, AS1,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
//                {AS1, AS2, AS1, AS2, AS1, AS1, AS1, AS1, AS1, AS1, AS1,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
//                {AS4, AS4, AS2, AS2, AS4, AS2, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
//                {AS4, AS4, AS4, AS4, AS3, AS4, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
//                {AS4, AS4, AS2, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
//                {AS5, AS5, AS2, AS5, AS5, AS5, AS2, AS5, AS5, AS5, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
//                {AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS2, AS2, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
//                {AS5, AS5, AS2, AS5, AS5, AS5, AS5, AS5, AS5, AS5, AS5,         AS5,        AS5, AS5, AS5, AS5,  AS5  , AS5, AS5},
//                {AS4, AS4, AS4, AS4, AS4, AS4, AS4,    , AS4, AS4, AS4,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
//                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS2, AS2, AS2,    ,  AS2  , AS2, AS2},
//                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,    , AS2,         AS2,        AS7, AS2, AS2, AS4,  AS2  ,    , AS2},
//                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS2, AS2, AS2,    ,  AS2  , AS2, AS2},
//                {AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,         AS2,        AS7, AS2, AS2, AS2,  AS2  , AS2, AS2},
//                {AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS7,         AS1,        AS1, AS1, AS1, AS1,  AS1  , AS1, AS1},
//                {AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS7,         AS4,        AS4, AS4, AS4, AS4,  AS4  , AS4, AS4},
//        };

    }


    public interface AccionSemantica{
        Token execute(AnalizadorLexico lexico, char ultimo);
    }

    public class AS1 implements AccionSemantica {
        //identificadores - palabras reservadas?
        //cambiar AS si palabra reservada o simbolo asi ahorramos un contains

        @Override
        public Token execute(AnalizadorLexico lexico, char ultimo) {
            posicion = posicion - 1;

            if(lexemaActual.length() > 20) {
                lexemaActual = lexemaActual.substring(0,20);
                // Anunciar warning al truncar
            }

            // Buscar en TPR o en TS
            //verificar si esta en TS y agregar
            Token token;
            return token;
        }
    }

    public class AS2 implements AccionSemantica {
        // Agrega caracter a lexema
        @Override
        public Token execute(AnalizadorLexico lexico, char ultimo){
            lexemaActual += ultimo;
            return null;
        }
    }

    public class AS3 implements AccionSemantica{
        //enteros largos

        @Override
        public Token execute(AnalizadorLexico lexico, char ultimo){
            posicion = posicion - 1;

            long enteroLargo = Long.parseLong(lexemaActual);
            //verificar rango
            if(enteroLargo >= 0 && enteroLargo <=  2147483648L) {
                //verificar si esta en TS y agregar
            }
            else //error
            Token token;
            return token;
        }
    }
//
//    public class AS4 implements AccionSemantica{
//        //caracter invalido
//
//        @Override
//        public Token execute(StringBuilder lexema, char ultimo){
//            //print error
//            return null;
//        }
//    }
//
//    public class AS5 implements AccionSemantica{
//        //Flotantes
//
//        @Override
//        public Token execute(StringBuilder lexema, char ultimo){
//            //devolver ultimo
//            //verificar rango
//            Token token;
//            //verificar si esta en TS y agregar
//            return token;
//        }
//    }
//
//    public class AS7 implements AccionSemantica{
//        //Agregar caracter y retornar token
//
//        @Override
//        public Token execute(StringBuilder lexema, char ultimo){
//            lexema.append(ultimo);
//            Token token;
//            //buscar en TS
//            return token;
//        }
//    }

}