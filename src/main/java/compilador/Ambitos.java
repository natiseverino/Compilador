package compilador;

import java.util.ArrayList;
import java.util.List;

public class Ambitos {
    private List<String> ambitosAnidados;
    private final String ambitoGlobal = "main";

    public Ambitos() {
        ambitosAnidados = new ArrayList<>();
        ambitosAnidados.add(ambitoGlobal);
    }

    public void agregarAmbito(String ambito) {
        ambitosAnidados.add(ambito);
    }

    public void eliminarAmbito() {
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
