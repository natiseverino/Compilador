package compilador;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        TablaSimbolos.init();

        AnalizadorLexico al = new AnalizadorLexico();
        System.out.println();
    }

}
