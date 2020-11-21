package compilador.codigoIntermedio;

import java.util.Stack;

public class EtiquetaElem extends PolacaElem {

    int pos;

    public EtiquetaElem(int pos) {
        this.pos = pos;
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        return "label" + pos + ":";
    }

    @Override
    public boolean error() {
        return false;
    }

    @Override
    public String toString() {
        return "Etiqueta "+ pos;
    }
}
