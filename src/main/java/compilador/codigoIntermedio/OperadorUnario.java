package compilador.codigoIntermedio;

import compilador.Main;
import compilador.TablaSimbolos;
import compilador.Token;

import java.util.Stack;

public class OperadorUnario extends PolacaElem {

    public enum Tipo{
        BF,
        BI,
        OUT,
        INV
    }

    private Tipo tipo;

    public OperadorUnario(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        int pos;
        switch (tipo) {
            case BI:
                pos = ((ElemPos) stack.pop()).getPos();
                return "jmp label" + pos;
            case BF:
                pos = ((ElemPos) stack.pop()).getPos();
                OperadorBinario operador = (OperadorBinario) stack.pop();
                int op = operador.getOperador();
                switch (op) {
                    case '>':
                        return "jle label" + pos;
                    case '<':
                        return "jge label" + pos;
                    case TablaSimbolos.IGUAL:
                        return "jne label" + pos;
                    case TablaSimbolos.DISTINTO:
                        return "je label" + pos;
                    case TablaSimbolos.MAYOR_IGUAL:
                        return "jl label" + pos;
                    case TablaSimbolos.MENOR_IGUAL:
                        return "jg label" + pos;
                }
            case OUT:
                ElemSimple elem = (ElemSimple) stack.pop();
                Token token = elem.getToken();
                String out = "";
                if (token.getTipoToken().equals(Main.IDENTIFICADOR)) {
                    if (token.getAtributo("uso") != null) {
                        if (token.getAtributo("uso").equals(Main.VARIABLE)) {
                            out = token.getLexema(true);
                        }
                    }
                } else
                    out = token.getAlias();

                return "invoke MessageBox,NULL, addr " + out + ", MB_OK";
            case INV:
                elem = (ElemSimple) stack.pop();
                token = elem.getToken();
                return "call " + token.getLexema(true);
        }
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
