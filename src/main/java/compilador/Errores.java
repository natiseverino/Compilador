package compilador;

import java.util.ArrayList;

public class Errores {
    private static ArrayList<String> errors = new ArrayList<>();
    private static ArrayList<String> warnings = new ArrayList<>();

    public static void addError(String error) {
        errors.add(error);
    }

    public static void addWarning(String warning) {
        warnings.add(warning);
    }

    public static boolean existenErrores() {
        return(errors.isEmpty());
    }

    public static void printErrores() {
        if(!errors.isEmpty()) {
            System.out.println(Main.ANSI_BOLD_WHITE + ">> ERRORES" + Main.ANSI_RESET);
            for (String error: errors
            ) {
                System.out.printf(Main.ANSI_RED + error + Main.ANSI_RESET);
            }
            System.out.println();
        }
    }

    public static void printWarnings() {
        if(!warnings.isEmpty()) {
            System.out.println(Main.ANSI_BOLD_WHITE + ">> WARNINGS" + Main.ANSI_RESET);
            for (String warning: warnings
            ) {
                System.out.printf(Main.ANSI_YELLOW + warning + Main.ANSI_RESET);
            }
            System.out.println();
        }
    }

    public static int getErrores(){
        return errors.isEmpty()? 0 : errors.size()-1;
    }
}
