import java.util.HashMap;
import java.util.Map;

public final class TablaSimbolos {

    private static Map<String, Integer> ids = new HashMap<>();
    private static Map<String, Token> simbolos = new HashMap<>();


    public static void init() {
        ids.put("id", 1);
        ids.put("cte", 29);
        ids.put("+", 2);
        ids.put("-", 3);
        ids.put("*", 4);
        ids.put("/", 5);
        ids.put("(", 6);
        ids.put(")", 7);
        ids.put("{", 8);
        ids.put("}", 9);
        ids.put(",", 10);
        ids.put(";", 11);
        ids.put("\"", 12);
        ids.put("<", 13);
        ids.put("<=", 14);
        ids.put(">", 15);
        ids.put(">=", 16);
        ids.put("=", 17);
        ids.put("==", 18);
        ids.put("IF", 19);
        ids.put("THEN", 20);
        ids.put("ELSE", 21);
        ids.put("FOR", 22);
        ids.put("END_IF", 23);
        ids.put("OUT", 24);
        ids.put("FUNC", 25);
        ids.put("RETURN", 26);
        ids.put("LONGINT", 27);
        ids.put("FLOAT", 28);

    }

    public static int getId(String palabra) {
        return ids.get(palabra);
    }

    public static Token getToken(String lexema) {
        return simbolos.get(lexema);
    }

    public static boolean reservada(String palabra) {
        return ids.containsKey(palabra);
    }

    public static void add(Token token) {
        if (!simbolos.containsKey(token.getLexema()))
            simbolos.put(token.getLexema(), token);
    }

}
