package compilador.codigoIntermedio;

import java.util.Stack;

public class ElemPos extends PolacaElem{
    int pos;

    public ElemPos(int pos){
        this.pos = pos;
    }

    public int getPos(){
        return pos;
    }

    @Override
    public String toString() {
        return String.valueOf(pos);
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        stack.push(this);
        return "";
    }

    @Override
    public boolean error() {
        return false;
    }
}
