package compilador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ambitos {
    private List<String> ambitosAnidados;
    public static final String ambitoGlobal = "main";

    public Ambitos() {
        ambitosAnidados = new ArrayList<>();
        ambitosAnidados.add(ambitoGlobal);
    }

    public void agregarAmbito(String ambito) {
        ambitosAnidados.add(ambito);
    }

    public void eliminarAmbito(boolean actualizarTablaSimbolos) {
        if(actualizarTablaSimbolos) {
            HashMap<String, Token> simbolos = (HashMap)TablaSimbolos.getSimbolos();
            for (String lexema: simbolos.keySet()
                 ) {
                if(lexema.contains(getAmbitos()))
                    TablaSimbolos.remove(lexema);
            }
        }
        ambitosAnidados.remove(ambitosAnidados.size()-1);
    }

    public String getAmbitos() {
        String retorno = new String();
        for (String ambito: ambitosAnidados
             ) {
            retorno += ambito + ":";
        }
        return (retorno.substring(0, retorno.length()-1));

    }
}
