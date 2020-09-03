import java.util.HashMap;
import java.util.Map;

public class Token {
    private int idToken;
    private String lexema;
    private String atributo;
    private Map<String, Object> atributos = new HashMap<>();

    public Token(int idToken, String lexema) {
        this.idToken = idToken;
        this.lexema = lexema;
    }

    public Token(int idToken) {
        this.idToken = idToken;
    }

    public void addAtributo(String tipo, Object atributo){
        atributos.put(tipo, atributo);
    }

    public int getIdToken() {return this.idToken;}

    public String getLexema() {return this.lexema;}
}