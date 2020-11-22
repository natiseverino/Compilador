package compilador.codigoSalida;

import compilador.TablaSimbolos;
import compilador.Token;

public class VariableAuxiliar {

    private static int count = 0;
    private static final String AUX = "AUX";

    public static Token getAux(){
        count++;
        String alias = "@aux"+count;
        Token token = new Token(TablaSimbolos.getId("aux"), AUX, alias);
        token.addAtributo("uso", "aux");
        token.addAtributo("tipo", "FLOAT");
        token.setAlias(alias);
        TablaSimbolos.add(token);
        return token;
    }

}
