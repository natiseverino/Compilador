package compilador.codigoIntermedio;

import java.util.Stack;

public class OperadorUnario extends PolacaElem {

    public enum Tipo{
        BF,
        BI,
        OUT,
        INV
    }

    private Tipo tipo;

    public OperadorUnario(Tipo tipo){
        this.tipo = tipo;
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
       //TODO
        return "";
    }

    @Override
    public boolean error() {
        return false;
    }

    @Override
    public String toString() {
        return tipo.toString();
    }
}
