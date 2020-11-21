package compilador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TablaSimbolos {

    private static Map<String, Integer> ids = new HashMap<>();
    private static Map<String, Token> simbolos = new HashMap<>();

    public final static int IGUAL=257;
    public final static int MAYOR_IGUAL=258;
    public final static int MENOR_IGUAL=259;
    public final static int DISTINTO=260;
    public final static int IF=261;
    public final static int THEN=262;
    public final static int ELSE=263;
    public final static int FOR=264;
    public final static int END_IF=265;
    public final static int OUT=266;
    public final static int FUNC=267;
    public final static int RETURN=268;
    public final static int LONGINT=269;
    public final static int FLOAT=270;
    public final static int ID=271;
    public final static int CTE=272;
    public final static int CADENA_MULT=273;
    public final static int PROC=274;
    public final static int VAR=275;
    public final static int NI=276;
    public final static int UP=277;
    public final static int DOWN=278;
    public final static int AUX=279;

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
        ids.put("==", IGUAL);
        ids.put(">=", MAYOR_IGUAL);
        ids.put("<=", MENOR_IGUAL);
        ids.put("!=", DISTINTO);
        ids.put("IF", IF);
        ids.put("THEN", THEN);
        ids.put("ELSE", ELSE);
        ids.put("FOR", FOR);
        ids.put("END_IF", END_IF);
        ids.put("OUT", OUT);
        ids.put("FUNC", FUNC);
        ids.put("RETURN", RETURN);
        ids.put("LONGINT", LONGINT);
        ids.put("FLOAT", FLOAT);
        ids.put("id", ID);
        ids.put("cte", CTE);
        ids.put("cadenaMult", CADENA_MULT);
        ids.put("PROC", PROC);
        ids.put("VAR",VAR);
        ids.put("NI", NI);
        ids.put("UP",UP);
        ids.put("DOWN", DOWN);
        ids.put("aux", AUX);
    }

    public static int getId(String palabra) {
        return ids.get(palabra);
    }

    public static Token getToken(String lexema) {
        return simbolos.get(lexema);
    }

    public static Map<String, Token> getSimbolos() { return (new HashMap<>(simbolos)); }

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
            simbolos.put(token.getLexema(), token);
    }

    public static void remove(String token){
        simbolos.remove(token);
    }

    public static boolean existe(String lexema){
        return simbolos.containsKey(lexema);
    }

    public static String getDataAssembler(){
        StringBuilder builder = new StringBuilder();
        for (Token token: simbolos.values()){
            token.initAlias();
            String asm = token.getAsm();
            if (!asm.equals(""))
                builder.append(asm).append(System.lineSeparator());
        }
        return builder.toString();
    }

    public static void print() {
        if(!simbolos.isEmpty()) {
            System.out.println(Main.ANSI_BOLD_WHITE + ">> TABLA DE SÍMBOLOS" + Main.ANSI_RESET);
            System.out.printf( "%-3s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s %n", "ID", "Tipo (Token)", "Lexema", "Uso", "Tipo", "Contador Ref.", "Atributos adicionales");
            System.out.printf(new String(new char[115]).replace("\0", "-") + "%n");
            for (String simbolo: simbolos.keySet()
            ) {
                String uso = (String)simbolos.get(simbolo).getAtributo("uso");
                String tipo = (String)simbolos.get(simbolo).getAtributo("tipo");
                if(uso == "Procedimiento") {
                    Object maxInvocaciones = simbolos.get(simbolo).getAtributo("max. invocaciones");
                    String atributosAdicionales = (maxInvocaciones != null) ? "NI: " + maxInvocaciones.toString() : "";

                    List<String> parametros = (List)simbolos.get(simbolo).getAtributo("parametros");
                    if(!parametros.isEmpty()) {
                        String infoParametros = String.join(", ", parametros);
                        atributosAdicionales += " / Parametros: " + infoParametros;
                    }

                    System.out.printf( "%-1s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s %n", simbolos.get(simbolo).getIdToken(), simbolos.get(simbolo).getTipoToken(), simbolos.get(simbolo).getLexema(), (uso != null) ? uso : "-", (tipo != null) ? tipo : "-", simbolos.get(simbolo).getAtributo("contador"), atributosAdicionales);
                }
                else if(uso == "Parametro") {
                    String atributosAdicionales = "Tipo pasaje: " + simbolos.get(simbolo).getAtributo("tipo pasaje");
                    System.out.printf( "%-1s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s %n", simbolos.get(simbolo).getIdToken(), simbolos.get(simbolo).getTipoToken(), simbolos.get(simbolo).getLexema(), (uso != null) ? uso : "-", (tipo != null) ? tipo : "-", simbolos.get(simbolo).getAtributo("contador"), atributosAdicionales);

                }
                else
                    System.out.printf( "%-1s | %-15s | %-15s | %-15s | %-15s | %-15s | - %n", simbolos.get(simbolo).getIdToken(), simbolos.get(simbolo).getTipoToken(), simbolos.get(simbolo).getLexema(), (uso != null) ? uso : "-", (tipo != null) ? tipo : "-", simbolos.get(simbolo).getAtributo("contador"));
            }
            System.out.println();
        }
    }

}
