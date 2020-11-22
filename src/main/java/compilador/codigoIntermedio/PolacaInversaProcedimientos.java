package compilador.codigoIntermedio;

import compilador.Main;

import java.util.HashMap;
import java.util.Map;

public class PolacaInversaProcedimientos {
    private Map<String, PolacaInversa> polacaProcedimientos;

    public PolacaInversaProcedimientos() {
        this.polacaProcedimientos = new HashMap<>();
    }

    public void addElem(String ambito, PolacaElem elem, boolean usaPila) {
        if(polacaProcedimientos.containsKey(ambito))
            polacaProcedimientos.get(ambito).addElem(elem, usaPila);
        else {
            PolacaInversa nuevaPolaca = new PolacaInversa();
            nuevaPolaca.addElem(elem, usaPila);
            polacaProcedimientos.put(ambito, nuevaPolaca);
        }
    }

    public void pushPos(String ambito, boolean espacioVacio){
        if(polacaProcedimientos.containsKey(ambito))
            polacaProcedimientos.get(ambito).pushPos(espacioVacio);
    }

    public int popPos(String ambito){
        if(polacaProcedimientos.containsKey(ambito))
            return(polacaProcedimientos.get(ambito).popPos());
        return -1;
    }

    public int getPolacaSize(String ambito) {
        if(polacaProcedimientos.containsKey(ambito))
            return(polacaProcedimientos.get(ambito).size());
        return -1;
    }

    public void print(){
        if(!polacaProcedimientos.isEmpty()) {
            System.out.println(Main.ANSI_BOLD_WHITE + ">> POLACA INVERSA PROCEDIMIENTOS" + Main.ANSI_RESET);
            for (String procedimiento: polacaProcedimientos.keySet()
                 ) {
                System.out.println("Proc.: " + procedimiento);
                System.out.println(polacaProcedimientos.get(procedimiento).toString());
                System.out.println();
            }
        }
    }

    public String generarCodigo(){
        StringBuilder builder = new StringBuilder();
        for (String procedimiento: polacaProcedimientos.keySet()
             ) {
            builder.append("_"+procedimiento+" proc").append(System.lineSeparator());
            builder.append(polacaProcedimientos.get(procedimiento).generarCodigo());
            builder.append("ret").append(System.lineSeparator());
            builder.append("_"+procedimiento+" endp").append(System.lineSeparator());
        }

        return builder.toString();
    }
}
