package compilador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Token {
    private int idToken;
    private String tipoToken;
    private String lexema;

    private Map<String, Object> atributos = new HashMap<>();

    public Token(int idToken, String tipoToken, String lexema) {
        this.idToken = idToken;
        this.tipoToken = tipoToken;
        this.lexema = lexema;
    }

    public Token(int idToken) {
        this.idToken = idToken;
    }

    public void addAtributo(String tipo, Object atributo){
        atributos.put(tipo, atributo);
    }

    public Object getAtributo(String tipo){
        return atributos.get(tipo);
    }

    public int getIdToken() {return this.idToken;}

    public String getTipoToken() {
        return tipoToken;
    }

    public String getLexema() {return this.lexema;}

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
}