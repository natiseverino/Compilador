public class Token {
    private int idToken;
    private String lexema;

    public Token(int idToken, String lexema) {
        this.idToken = idToken;
        this.lexema = lexema;
    }

    public Token(int idToken) {
        this.idToken = idToken;
    }

    public int getIdToken() {return this.idToken;}

    public String getLexema() {return this.lexema;}
}