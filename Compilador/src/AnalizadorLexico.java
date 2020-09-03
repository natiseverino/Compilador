public class AnalizadorLexico {

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

    private AccionSemantica[][] accion;

// Hash identificador, constante o cadena con correspondientes atributos
//private HashTable<String, Atributos> tablaSimbolos;

    public int getIndiceCaracterMatrizEstados() {

    }

    public Token getSigToken() {

        int estadoAct = 0, estadoAnt = -1;
        while(estadoAct != -1 && estadoAct != 16) {
            //Levantar caracter y eliminarlo del buffer
            //Agregar caracter a buffer interno
            //ejecutar accion semantica
            estadoAnt = estadoAct;
            //estadoAct = estados[estadoAct][/*indice columna matriz de estados*/];
        }

    }

    private void asignarAS(){
        accion = new AccionSemantica[16][19];

        AccionSemantica AS1 = new AS1();
        AccionSemantica AS2 = new AS2();
        AccionSemantica AS3 = new AS3();
        AccionSemantica AS4 = new AS4();
        AccionSemantica AS5 = new AS5();
        AccionSemantica AS7 = new AS7();

        accion[0][0] = AS2;
        accion[0][1] = AS2;
        accion[0][2] = AS2;
        accion[0][3] = AS4;
        accion[0][4] = AS4;
        accion[0][5] = AS2;
        accion[0][6] = AS4;
        //accion[0][7] = vacio;
        accion[0][8] = AS7;
        accion[0][9] = AS7;
        accion[0][10] = AS2;
        accion[0][11] = AS7;
        accion[0][12] = AS2;
        accion[0][13] = AS2;
        accion[0][14] = AS2;
        //accion[0][15] = vacio;
        //accion[0][16] = vacio;
        //accion[0][17] = vacio;
        accion[0][18] = AS4;

        accion[1][0] = AS2;
        accion[1][1] = AS1;
        accion[1][2] = AS2;
        accion[1][3] = AS2;
        accion[1][4] = AS2;
        accion[1][5] = AS1;
        accion[1][6] = AS2;
        accion[1][7] = AS1;
        accion[1][8] = AS1;
        accion[1][9] = AS1;
        accion[1][10] = AS1;
        accion[1][11] = AS1;
        accion[1][12] = AS1;
        accion[1][13] = AS1;
        accion[1][14] = AS1;
        accion[1][15] = AS1;
        accion[1][16] = AS1;
        accion[1][17] = AS1;
        accion[1][18] = AS1;

        accion[2][0] = AS1;
        accion[2][1] = AS2;
        accion[2][2] = AS1;
        accion[2][3] = AS2;
        accion[2][4] = AS1;
        accion[2][5] = AS1;
        accion[2][6] = AS1;
        accion[2][7] = AS1;
        accion[2][8] = AS1;
        accion[2][9] = AS1;
        accion[2][10] = AS1;
        accion[2][11] = AS1;
        accion[2][12] = AS1;
        accion[2][13] = AS1;
        accion[2][14] = AS1;
        accion[2][15] = AS1;
        accion[2][16] = AS1;
        accion[2][17] = AS1;
        accion[2][18] = AS1;

        accion[3][0] = AS4;
        accion[3][1] = AS4;
        accion[3][2] = AS2;
        accion[3][3] = AS2;
        accion[3][4] = AS4;
        accion[3][5] = AS2;
        accion[3][6] = AS4;
        accion[3][7] = AS4;
        accion[3][8] = AS4;
        accion[3][9] = AS4;
        accion[3][10] = AS4;
        accion[3][11] = AS4;
        accion[3][12] = AS4;
        accion[3][13] = AS4;
        accion[3][14] = AS4;
        accion[3][15] = AS4;
        accion[3][16] = AS4;
        accion[3][17] = AS4;
        accion[3][18] = AS4;

        accion[4][0] = AS4;
        accion[4][1] = AS4;
        accion[4][2] = AS4;
        accion[4][3] = AS4;
        accion[4][4] = AS3;
        accion[4][5] = AS4;
        accion[4][6] = AS4;
        accion[4][7] = AS4;
        accion[4][8] = AS4;
        accion[4][9] = AS4;
        accion[4][10] = AS4;
        accion[4][11] = AS4;
        accion[4][12] = AS4;
        accion[4][13] = AS4;
        accion[4][14] = AS4;
        accion[4][15] = AS4;
        accion[4][16] = AS4;
        accion[4][17] = AS4;
        accion[4][18] = AS4;

        accion[5][0] = AS4;
        accion[5][1] = AS4;
        accion[5][2] = AS2;
        accion[5][3] = AS4;
        accion[5][4] = AS4;
        accion[5][5] = AS4;
        accion[5][6] = AS4;
        accion[5][7] = AS4;
        accion[5][8] = AS4;
        accion[5][9] = AS4;
        accion[5][10] = AS4;
        accion[5][11] = AS4;
        accion[5][12] = AS4;
        accion[5][13] = AS4;
        accion[5][14] = AS4;
        accion[5][15] = AS4;
        accion[5][16] = AS4;
        accion[5][17] = AS4;
        accion[5][18] = AS4;

        accion[6][0] = AS5;
        accion[6][1] = AS5;
        accion[6][2] = AS2;
        accion[6][3] = AS5;
        accion[6][4] = AS5;
        accion[6][5] = AS5;
        accion[6][6] = AS2;
        accion[6][7] = AS5;
        accion[6][8] = AS5;
        accion[6][9] = AS5;
        accion[6][10] = AS5;
        accion[6][11] = AS5;
        accion[6][12] = AS5;
        accion[6][13] = AS5;
        accion[6][14] = AS5;
        accion[6][15] = AS5;
        accion[6][16] = AS5;
        accion[6][17] = AS5;
        accion[6][18] = AS5;

        accion[7][0] = AS5;
        accion[7][1] = AS5;
        accion[7][2] = AS5;
        accion[7][3] = AS5;
        accion[7][4] = AS5;
        accion[7][5] = AS5;
        accion[7][6] = AS5;
        accion[7][7] = AS5;
        accion[7][8] = AS2;
        accion[7][9] = AS2;
        accion[7][10] = AS5;
        accion[7][11] = AS5;
        accion[7][12] = AS5;
        accion[7][13] = AS5;
        accion[7][14] = AS5;
        accion[7][15] = AS5;
        accion[7][16] = AS5;
        accion[7][17] = AS5;
        accion[7][18] = AS5;

        accion[8][0] = AS5;
        accion[8][1] = AS5;
        accion[8][2] = AS2;
        accion[8][3] = AS5;
        accion[8][4] = AS5;
        accion[8][5] = AS5;
        accion[8][6] = AS5;
        accion[8][7] = AS5;
        accion[8][8] = AS5;
        accion[8][9] = AS5;
        accion[8][10] = AS5;
        accion[8][11] = AS5;
        accion[8][12] = AS5;
        accion[8][13] = AS5;
        accion[8][14] = AS5;
        accion[8][15] = AS5;
        accion[8][16] = AS5;
        accion[8][17] = AS5;
        accion[8][18] = AS5;

        accion[9][0] = AS4;
        accion[9][1] = AS4;
        accion[9][2] = AS4;
        accion[9][3] = AS4;
        accion[9][4] = AS4;
        accion[9][5] = AS4;
        accion[9][6] = AS4;
        //accion[9][7] = vacio;
        accion[9][8] = AS4;
        accion[9][9] = AS4;
        accion[9][10] = AS4;
        accion[9][11] = AS4;
        accion[9][12] = AS4;
        accion[9][13] = AS4;
        accion[9][14] = AS4;
        accion[9][15] = AS4;
        accion[9][16] = AS4;
        accion[9][17] = AS4;
        accion[9][18] = AS4;

        accion[10][0] = AS2;
        accion[10][1] = AS2;
        accion[10][2] = AS2;
        accion[10][3] = AS2;
        accion[10][4] = AS2;
        accion[10][5] = AS2;
        accion[10][6] = AS2;
        accion[10][7] = AS2;
        accion[10][8] = AS2;
        accion[10][9] = AS2;
        accion[10][10] = AS2;
        accion[10][11] = AS2;
        accion[10][12] = AS2;
        accion[10][13] = AS2;
        accion[10][14] = AS2;
        //accion[10][15] = vacio;
        accion[10][16] = AS2;
        accion[10][17] = AS2;
        accion[10][18] = AS2;

        accion[11][0] = AS2;
        accion[11][1] = AS2;
        accion[11][2] = AS2;
        accion[11][3] = AS2;
        accion[11][4] = AS2;
        accion[11][5] = AS2;
        accion[11][6] = AS2;
        accion[11][7] = AS2;
        accion[11][8] = AS2;
        //accion[11][9] = vacio;
        accion[11][10] = AS2;
        accion[11][11] = AS2;
        accion[11][12] = AS7;
        accion[11][13] = AS2;
        accion[11][14] = AS2;
        accion[11][15] = AS4;
        accion[11][16] = AS2;
        //accion[11][17] = vacio;
        accion[11][18] = AS2;

        accion[12][0] = AS2;
        accion[12][1] = AS2;
        accion[12][2] = AS2;
        accion[12][3] = AS2;
        accion[12][4] = AS2;
        accion[12][5] = AS2;
        accion[12][6] = AS2;
        accion[12][7] = AS2;
        accion[12][8] = AS2;
        accion[12][9] = AS2;
        accion[12][10] = AS2;
        accion[12][11] = AS2;
        accion[12][12] = AS2;
        accion[12][13] = AS2;
        accion[12][14] = AS2;
        //accion[12][15] = vacio;
        accion[12][16] = AS2;
        accion[12][17] = AS2;
        accion[12][18] = AS2;

        accion[13][0] = AS2;
        accion[13][1] = AS2;
        accion[13][2] = AS2;
        accion[13][3] = AS2;
        accion[13][4] = AS2;
        accion[13][5] = AS2;
        accion[13][6] = AS2;
        accion[13][7] = AS2;
        accion[13][8] = AS2;
        accion[13][9] = AS2;
        accion[13][10] = AS2;
        accion[13][11] = AS2;
        accion[13][12] = AS7;
        accion[13][13] = AS2;
        accion[13][14] = AS2;
        accion[13][15] = AS2;
        accion[13][16] = AS2;
        accion[13][17] = AS2;
        accion[13][18] = AS2;

        accion[14][0] = AS1;
        accion[14][1] = AS1;
        accion[14][2] = AS1;
        accion[14][3] = AS1;
        accion[14][4] = AS1;
        accion[14][5] = AS1;
        accion[14][6] = AS1;
        accion[14][7] = AS1;
        accion[14][8] = AS1;
        accion[14][9] = AS1;
        accion[14][10] = AS7;
        accion[14][11] = AS1;
        accion[14][12] = AS1;
        accion[14][13] = AS1;
        accion[14][14] = AS1;
        accion[14][15] = AS1;
        accion[14][16] = AS1;
        accion[14][17] = AS1;
        accion[14][18] = AS1;

        accion[15][0] = AS4;
        accion[15][1] = AS4;
        accion[15][2] = AS4;
        accion[15][3] = AS4;
        accion[15][4] = AS4;
        accion[15][5] = AS4;
        accion[15][6] = AS4;
        accion[15][7] = AS4;
        accion[15][8] = AS4;
        accion[15][9] = AS4;
        accion[15][10] = AS7;
        accion[15][11] = AS4;
        accion[15][12] = AS4;
        accion[15][13] = AS4;
        accion[15][14] = AS4;
        accion[15][15] = AS4;
        accion[15][16] = AS4;
        accion[15][17] = AS4;
        accion[15][18] = AS4;

    }


    public interface AccionSemantica{
        Token execute(StringBuilder lexema, char ultimo);
    }

    public class AS1 implements AccionSemantica{
        //identificadores - palabras reservadas?
        //cambiar AS si palabra reservada o simbolo asi ahorramos un contains

        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            //devolver ultimo
            //truncar a 20 caracteres max
            Token token;
            //verificar si esta en TS y agregar
            return token;
        }
    }

    public class AS2 implements AccionSemantica{
        //agregar caracter a lexema
        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            lexema.append(ultimo);
            return null;
        }
    }

    public class AS3 implements AccionSemantica{
        //enteros largos

        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            lexema.append(ultimo);
            //verificar rango
            Token token;
            //verificar si esta en TS y agregar
            return token;
        }
    }

    public class AS4 implements AccionSemantica{
        //caracter invalido

        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            //print error
            return null;
        }
    }

    public class AS5 implements AccionSemantica{
        //Flotantes

        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            //devolver ultimo
            //verificar rango
            Token token;
            //verificar si esta en TS y agregar
            return token;
        }
    }

    public class AS7 implements AccionSemantica{
        //Agregar caracter y retornar token

        @Override
        public Token execute(StringBuilder lexema, char ultimo){
            lexema.append(ultimo);
            Token token;
            //buscar en TS
            return token;
        }
    }

}