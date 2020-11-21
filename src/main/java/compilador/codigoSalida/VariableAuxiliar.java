package compilador.codigoSalida;

import compilador.TablaSimbolos;
import compilador.Token;

public class VariableAuxiliar {

    private static int count = 0;
    private static final String FLOAT = "FLOAT";

    public static Token getAux(){
        count++;
        String alias = "@aux"+count;
        Token token = new Token(TablaSimbolos.getId("aux"), FLOAT,alias);
        token.addAtributo("uso", "aux");
        TablaSimbolos.add(token);
        return token;
    }

}
