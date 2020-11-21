package compilador.codigoIntermedio;

import compilador.Main;
import compilador.TablaSimbolos;
import compilador.Token;
import compilador.codigoSalida.SeguimientoRegistros;
import compilador.codigoSalida.VariableAuxiliar;

import java.util.Stack;

public class OperadorBinario extends PolacaElem {

    private int operador;
    private String palabra;
    private boolean huboError = false;


    public OperadorBinario(String palabra, int nroLinea) {
        this.palabra = palabra;
        this.operador = TablaSimbolos.getId(palabra);
        setNroLinea(nroLinea);
    }

    public OperadorBinario(String palabra) {
        this.palabra = palabra;
        this.operador = TablaSimbolos.getId(palabra);
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        ElemSimple elem1 = (ElemSimple) stack.pop();
        ElemSimple elem2 = (ElemSimple) stack.pop();

        if (!elem1.getTipo().equals(elem2.getTipo())) {
            System.out.printf(Main.ANSI_RED + "[GC] | Linea %d: Tipos incompatibles %n" + Main.ANSI_RESET, elem1.getNroLinea());
            huboError = true;
            return "";
        }

        String tipo = elem1.getTipo();
        StringBuilder code = new StringBuilder();

        if (operador != '=') {
            if (tipo.equals("LONGINT")) {
                switch (operador) {
                    //TODO operaciones de enteros largos con seguimiento de registros
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '>':
                    case '<':
                    case TablaSimbolos.DISTINTO:
                    case TablaSimbolos.MAYOR_IGUAL:
                    case TablaSimbolos.MENOR_IGUAL:
                    case TablaSimbolos.IGUAL:
                }
            } else if (tipo.equals("FLOAT")) {
                ElemSimple aux = new ElemSimple(VariableAuxiliar.getAux());
                switch(operador){
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        stack.push(aux);
                }
                code.append(generarFloat(elem1, elem2, operador, aux));

                switch (operador){
                    case '>':
                    case '<':
                    case TablaSimbolos.DISTINTO:
                    case TablaSimbolos.MAYOR_IGUAL:
                    case TablaSimbolos.MENOR_IGUAL:
                    case TablaSimbolos.IGUAL:
                        stack.push(this);
                }
            }
        } else { //asignaciones
            Token id1 = elem1.getToken();
            Token id2 = elem1.getToken();

            if (tipo.equals("FLOAT")) {
                code.append("fld ").append(id2.getLexema())
                        .append(System.lineSeparator())
                        .append("fstp ").append(id1.getLexema())
                        .append(System.lineSeparator());
            } else if (tipo.equals("LONGINT")) {
                String reg = SeguimientoRegistros.getInstance().regLibre();
                code.append("mov ").append(reg).append(", ").append(id2.getLexema())
                        .append(System.lineSeparator())
                        .append("mov ").append(id1.getLexema()).append(" ,").append(reg)
                        .append(System.lineSeparator());
                SeguimientoRegistros.getInstance().liberar(reg);

            }
        }

        return null;
    }


    private String generarFloat(ElemSimple elem1, ElemSimple elem2, int op, ElemSimple aux) {
        StringBuilder code = new StringBuilder();

        switch (op) {
            case '+':
                code.append("fld ").append(elem1.getToken().getLexema()).append(System.lineSeparator())
                    .append("fld ").append(elem2.getToken().getLexema()).append(System.lineSeparator())
                    .append("fadd").append(System.lineSeparator())
                    .append("fstp ").append(aux.getToken().getLexema())
                    .append(System.lineSeparator());
                //TODO chequear overflow
                break;
            case '-':
                code.append("fld ").append(elem1.getToken().getLexema()).append(System.lineSeparator())
                        .append("fld ").append(elem2.getToken().getLexema()).append(System.lineSeparator())
                        .append("fsub").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema())
                        .append(System.lineSeparator());
                break;
            case '*':
                code.append("fld ").append(elem1.getToken().getLexema()).append(System.lineSeparator())
                        .append("fld ").append(elem2.getToken().getLexema()).append(System.lineSeparator())
                        .append("fmul").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema())
                        .append(System.lineSeparator());
                break;
            case '/':
                code.append("fld ").append(elem1.getToken().getLexema()).append(System.lineSeparator())
                        .append("fld ").append(elem2.getToken().getLexema()).append(System.lineSeparator())
                        .append("fdiv").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema())
                        .append(System.lineSeparator());
                break;
            case '>':
            case '<':
            case TablaSimbolos.DISTINTO:
            case TablaSimbolos.MAYOR_IGUAL:
            case TablaSimbolos.MENOR_IGUAL:
            case TablaSimbolos.IGUAL:
                code.append("fld ").append(elem1.getToken().getLexema()).append(System.lineSeparator())
                        .append("fld ").append(elem2.getToken().getLexema()).append(System.lineSeparator())
                        .append("fcompp").append(System.lineSeparator())
                        .append("fstsw ax").append(System.lineSeparator())
                        .append("sahf").append(System.lineSeparator());
                break;


        }

        return code.toString();

    }


    @Override
    public boolean error() {
        return huboError;
    }

    public int getOperador() {
        return operador;
    }

    @Override
    public String toString() {
        return palabra;
    }
}
