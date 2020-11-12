package compilador.codigoIntermedio;

import java.util.Stack;

public class ElemVacio extends PolacaElem{

    public ElemVacio(){
    }

    @Override
    public String toString() {
        return "[Vacio]";
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        return "";
    }

    @Override
    public boolean error() {
        return true;
    }
}
