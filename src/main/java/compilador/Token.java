package compilador;

import java.util.HashMap;
import java.util.Map;

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

    public void removeAtributo(String tipo) { atributos.remove(tipo); }

    public int getIdToken() {return this.idToken;}

    public String getTipoToken() {
        return tipoToken;
    }

    public String getLexema() {return this.lexema;}
}