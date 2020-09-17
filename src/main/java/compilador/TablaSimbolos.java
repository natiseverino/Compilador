package compilador;

import java.util.HashMap;
import java.util.Map;

public final class TablaSimbolos {

    private static Map<String, Integer> ids = new HashMap<>();
    private static Map<String, Token> simbolos = new HashMap<>();

    private static final int ID = 271;
    private static final int CTE = 272;
    private static final int CADENA_MULT = 273;


    public static void init() {
        ids.put("\"", 34);
        ids.put("(", 40);
        ids.put(")", 41);
        ids.put("*", 42);
        ids.put("+", 43);
        ids.put(",", 44);
        ids.put("-", 45);
        ids.put("/", 47);
        ids.put(";", 59);
        ids.put("<", 60);
        ids.put("=", 61);
        ids.put(">", 62);
        ids.put("{", 123);
        ids.put("}", 125);
        ids.put("==", 257);
        ids.put(">=", 258);
        ids.put("<=", 259);
        ids.put("!=", 260);
        ids.put("IF", 261);
        ids.put("THEN", 262);
        ids.put("ELSE", 263);
        ids.put("FOR", 264);
        ids.put("END_IF", 265);
        ids.put("OUT", 266);
        ids.put("FUNC", 267);
        ids.put("RETURN", 268);
        ids.put("LONGINT", 269);
        ids.put("FLOAT", 270);
        ids.put("id", ID);
        ids.put("cte", CTE);
        ids.put("cadenaMult", CADENA_MULT);

    }

    public static int getId(String palabra) {
        return ids.get(palabra);
    }

    public static Token getToken(String lexema) {
        return simbolos.get(lexema);
    }

    public static int reservada(String palabra) {
        if(!ids.containsKey(palabra))
            return -1;
        else
            if(ids.get(palabra) < 261)
                return 0;
            else
                return 1;
    }

    public static void add(Token token) {
        if (!simbolos.containsKey(token.getLexema()))
            simbolos.put(token.getLexema(), token);
    }

}
