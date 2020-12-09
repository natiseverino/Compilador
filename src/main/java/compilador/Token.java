package compilador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Token {


    private int idToken;
    private String tipoToken;
    private String lexema;

    private String alias;
    private static int countString = 0;
    private static int countLongint = 0;
    private static int countFloat = 0;

    private Map<String, Object> atributos = new HashMap<>();

    public Token(int idToken, String tipoToken, String lexema) {
        this.idToken = idToken;
        this.tipoToken = tipoToken;
        this.lexema = lexema;
        if (tipoToken.equals("REGISTRO")) //registros
            this.alias = lexema;
        else
            this.alias = "";
    }


    public void initAlias() {
        switch (this.tipoToken) {
            case Main.CONSTANTE:
                if (getAtributo("tipo").equals(Main.LONGINT)) {
                    countLongint++;
                    this.alias = "@long" + countLongint;
                } else if (getAtributo("tipo").equals(Main.FLOAT)) {
                    countFloat++;
                    this.alias = "@float" + countFloat;
                }
                break;
            case Main.CADENA:
                countString++;
                this.alias = "@string" + countString;
                break;
            case Main.IDENTIFICADOR:
                if(getAtributo("uso").equals(Main.PROCEDIMIENTO))
                    this.alias = lexema.substring(lexema.lastIndexOf("@")+1) + "@" + getAtributo("ambito");
                else
                    this.alias = "_"+lexema+"@"+getAtributo("ambito");
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

    public Map<String, Object> getAtributos() {
        return(new HashMap<String, Object>(atributos));
    }

    public void removeAtributo(String tipo) { atributos.remove(tipo); }


    public int getIdToken() {
        return this.idToken;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getLexema(boolean getAlias) {
        if (getAlias)
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
                alias.equals(alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idToken, tipoToken, lexema, atributos);
    }

    public String getAsm() {
        StringBuilder builder = new StringBuilder();
        switch (this.tipoToken) {
            case Main.IDENTIFICADOR:
                String usoToken = this.getAtributo("uso").toString();
                if (usoToken.equals(Main.VARIABLE) || usoToken.equals(Main.PARAMETRO))
                    builder.append("_").append(lexema).append(" dd ?");
                if (usoToken.equals(Main.PROCEDIMIENTO)) {
                    builder.append("_").append(lexema).append("@inv").append(" dd 0").append(System.lineSeparator());
                    builder.append("@").append(lexema).append("@max_inv").append(" dd ").append(getAtributo("max. invocaciones"));
                }
                break;
            case Main.CONSTANTE:
                builder.append(alias).append(" dd ").append(lexema);
                break;
            case Main.CADENA:
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

    public void setAlias(String alias){
        this.alias = alias;
    }
}