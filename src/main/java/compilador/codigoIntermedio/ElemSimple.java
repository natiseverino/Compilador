package compilador.codigoIntermedio;

import compilador.Token;

import java.util.Stack;

public class ElemSimple extends PolacaElem {

    private Token token;

    public ElemSimple(Token token, int nroLinea) {
        this.token = token;
        setNroLinea(nroLinea);
    }

    public ElemSimple(Token token) {
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

    public Token getToken() {
        return token;
    }

    public String getTipo() { //TODO cambiar con la nueva tabla de simbolos
        String tipo = "";
        if (token.getTipoToken().equals("IDENTIFICADOR") || token.getTipoToken().equals("AUX"))
            tipo = (String) token.getAtributo("tipo");
        else if (token.getTipoToken().equals("FLOAT"))
            tipo = "FLOAT";
        else if (token.getTipoToken().equals("LONGINT"))
            tipo = "LONGINT";

        return tipo;
    }

    @Override
    public String toString() {
        return token.getLexema(false);
    }
}
