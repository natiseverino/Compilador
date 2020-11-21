package compilador.codigoIntermedio;

import java.util.Stack;

public abstract class PolacaElem {

    public abstract String generarCodigo(Stack<PolacaElem> stack);
    public abstract boolean error();


}
