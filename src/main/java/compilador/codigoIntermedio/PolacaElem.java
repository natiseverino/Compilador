package compilador.codigoIntermedio;

import java.util.Stack;

public abstract class PolacaElem {

    private int nroLinea = -1;

    public abstract String generarCodigo(Stack<PolacaElem> stack);
    public abstract boolean error();

    public int getNroLinea(){
        return nroLinea;
    }

    public void setNroLinea(int nroLinea){
        this.nroLinea = nroLinea;
    }


}
