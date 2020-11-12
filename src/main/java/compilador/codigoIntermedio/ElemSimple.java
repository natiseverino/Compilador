package compilador.codigoIntermedio;

import compilador.Token;

import java.util.Stack;

public class ElemSimple extends PolacaElem {

    private Token token;

    public ElemSimple(Token token){
        this.token = token;
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

    public Token getToken(){
        return this.token;
    }

    @Override
    public String toString() {
        return token.getLexema();
    }
}
