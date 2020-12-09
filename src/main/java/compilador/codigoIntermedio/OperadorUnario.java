package compilador.codigoIntermedio;

import compilador.Main;
import compilador.TablaSimbolos;
import compilador.Token;

import java.util.Stack;

public class OperadorUnario extends PolacaElem {

    public enum Tipo {
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
            case BF: //como es un branch por falso, las condiciones estan negadas
                pos = ((ElemPos) stack.pop()).getPos();
                OperadorBinario operador = (OperadorBinario) stack.pop();
                int op = operador.getOperador();
                switch (op) {
                    case '>':
                        return operador.getTipo().equals(Main.FLOAT) ? ("jbe label" + pos) : ("jle label" + pos);
                    case '<':
                        return operador.getTipo().equals(Main.FLOAT) ? ("jae label" + pos) : ("jge label" + pos);
                    case TablaSimbolos.IGUAL:
                        return "jne label" + pos;
                    case TablaSimbolos.DISTINTO:
                        return "je label" + pos;
                    case TablaSimbolos.MAYOR_IGUAL:
                        return operador.getTipo().equals(Main.FLOAT) ? ("jb label" + pos) : ("jl label" + pos);
                    case TablaSimbolos.MENOR_IGUAL:
                        return operador.getTipo().equals(Main.FLOAT) ? ("ja label" + pos) : ("jg label" + pos);
                }
            case OUT:
                ElemSimple elem = (ElemSimple) stack.pop();
                Token token = elem.getToken();
                String out = token.getLexema(true);
                if (token.getTipoToken().equals(Main.IDENTIFICADOR)) {
                    if (token.getAtributo("uso") != null) {
                        if (token.getAtributo("uso").equals(Main.VARIABLE)) {
                            return print(token.getAtributo("tipo").toString(), out);
                        }
                    }
                } else if (token.getTipoToken().equals(Main.CONSTANTE)) {
                    return print(token.getAtributo("tipo").toString(), out);
                }

                return "invoke MessageBox, NULL, addr " + out + ", addr " + out + ", MB_OK";

            case INV:
                elem = (ElemSimple) stack.pop();
                token = elem.getToken();
                return "call _" + token.getLexema(false);
        }
        return "";
    }

    @Override
    public boolean error() {
        return false;
    }

    private String print(String tipo, String out) {
        if (tipo.equals(Main.FLOAT)) {
            return "invoke printf, cfm$(\"%f \\n\"), " + out;
        } else return "invoke printf, cfm$(\"%d \\n\"), " + out;
    }

    @Override
    public String toString() {
        return tipo.toString();
    }
}
