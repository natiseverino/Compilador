package compilador.codigoIntermedio;

import java.util.Stack;

public class EtiquetaElem extends PolacaElem {
    //TODO ?
    int pos;

    public EtiquetaElem(int pos) {
        this.pos = pos;
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        return "Etiqueta " + pos + ":";
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