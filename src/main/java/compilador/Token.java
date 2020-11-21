package compilador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Token {


    private int idToken;
    private String tipoToken;
    private String lexema;

    private String alias = "";
    private static int countString = 0;
    private static int countLongint = 0;
    private static int countFloat = 0;

    private Map<String, Object> atributos = new HashMap<>();

    public Token(int idToken, String tipoToken, String lexema) {
        this.idToken = idToken;
        this.tipoToken = tipoToken;
        this.lexema = lexema;
    }


    public void initAlias() {
        switch (this.tipoToken) {
            case "CONSTANTE":
                if (getAtributo("tipo").equals("LONGINT")) {
                    countLongint++;
                    alias = "@long" + countLongint;
                } else if (getAtributo("tipo").equals("FLOAT")) {
                    countFloat++;
                    alias = "@float" + countFloat;
                }
                break;
            case "CADENA MULT":
                countString++;
                alias = "@string" + countString;
                break;
            case "FLOAT": //TODO borrar con nueva tabla de simbolos
                countFloat++;
                alias = "@float" + countFloat;
                break;
            case "LONGINT": //TODO borrar con nueva tabla de simbolos
                countLongint++;
                alias = "@long" + countLongint;
                break;
        }
    }

    public Token(int idToken) {
        this.idToken = idToken;
    }

    public void addAtributo(String tipo, Object atributo) {
        atributos.put(tipo, atributo);
    }

    public Object getAtributo(String tipo) {
        return atributos.get(tipo);
    }

    public int getIdToken() {
        return this.idToken;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getLexema(boolean alias) {
        if (alias && (tipoToken.equals("FLOAT")))
            return this.alias;
        return lexema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return idToken == token.idToken &&
                tipoToken.equals(token.tipoToken) &&
                lexema.equals(token.lexema) &&
                Objects.equals(atributos, token.atributos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idToken, tipoToken, lexema, atributos);
    }

    public String getAsm() {
        StringBuilder builder = new StringBuilder();
        switch (this.tipoToken) {
            case "IDENTIFICADOR":
                if (this.getAtributo("uso").equals("VARIABLE"))
                    builder.append("_").append(lexema).append(" dd ?");
                break;
            case "LONGINT":
            case "FLOAT": //TODO MODIFICAR A CONSTANTE CON LA NUEVA TABLA DE SIMBOLOS
                builder.append(alias).append(" dd ").append(lexema);
                break;
            case "CADENA MULT":
                builder.append(alias).append(" db ").append(lexema).append(", 0");
                break;
            case "AUX":
                builder.append(lexema).append(" dd ?");
                break;
        }

        return builder.toString();
    }

    public String getAlias() {
        return alias;
    }
}