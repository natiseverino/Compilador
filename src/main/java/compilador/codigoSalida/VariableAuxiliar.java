package compilador.codigoSalida;

import compilador.Main;
import compilador.TablaSimbolos;
import compilador.Token;

public class VariableAuxiliar {

    private static int count = 0;
    private static final String AUX = "AUX";

    public static Token getAux(String size){
        count++;
        String alias = "@aux"+count;
        Token token = new Token(TablaSimbolos.getId("aux"), AUX, alias);
        token.addAtributo("uso", "aux");
        token.addAtributo("tipo", Main.FLOAT);
        token.setAlias(alias);
        token.addAtributo("size", size);
        TablaSimbolos.add(token);
        return token;
    }


}
