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

    private static final String REGISTRO = "REGISTRO";


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
            System.out.println(elem1.getToken().getLexema(true) + " -> " + elem1.getTipo());
            System.out.println(elem2.getToken().getLexema(true) + " -> " + elem2.getTipo());
            huboError = true;
            return "";
        }

        String tipo = elem1.getTipo();
        StringBuilder code = new StringBuilder();

        if (operador != '=') {
            if (tipo.equals("LONGINT")) {
                codeLongint(elem2, elem1, operador, stack, code);

            } else if (tipo.equals("FLOAT")) {
                ElemSimple aux = new ElemSimple(VariableAuxiliar.getAux());
                switch (operador) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        stack.push(aux);
                }
                code.append(generarFloat(elem1, elem2, operador, aux));

                switch (operador) {
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
            Token id2 = elem2.getToken();


            if (tipo.equals("FLOAT")) {
                code.append("fld ").append(id2.getLexema(true))
                        .append(System.lineSeparator())
                        .append("fstp ").append(id1.getLexema(true))
                        .append(System.lineSeparator());
            } else if (tipo.equals("LONGINT")) {
                if (id2.getTipoToken().equals(REGISTRO)) {
                    code.append("mov ").append(id1.getLexema(true)).append(", ").append(id2.getLexema(true))
                            .append(System.lineSeparator());
                    SeguimientoRegistros.getInstance().liberar(id2.getLexema(true));
                } else {
                    String reg = SeguimientoRegistros.getInstance().regBC();
                    code.append("mov ").append(reg).append(", ").append(id1.getLexema(true))
                            .append(System.lineSeparator())
                            .append("mov ").append(id2.getLexema(true)).append(" ,").append(reg)
                            .append(System.lineSeparator());
                    SeguimientoRegistros.getInstance().liberar(reg);
                }

            }
        }

        return code.toString();
    }


    private String generarFloat(ElemSimple elem1, ElemSimple elem2, int op, ElemSimple aux) {
        StringBuilder code = new StringBuilder();

        switch (op) {
            case '+':
                code.append("fld ").append(elem2.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fld ").append(elem1.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fadd").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema(true))
                        .append(System.lineSeparator());
                //TODO chequear overflow
                break;
            case '-':
                code.append("fld ").append(elem2.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fld ").append(elem1.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fsub").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema(true))
                        .append(System.lineSeparator());
                break;
            case '*':
                code.append("fld ").append(elem2.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fld ").append(elem1.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fmul").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema(true))
                        .append(System.lineSeparator());
                break;
            case '/':
                code.append("fld ").append(elem2.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fld ").append(elem1.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fdiv").append(System.lineSeparator())
                        .append("fstp ").append(aux.getToken().getLexema(true))
                        .append(System.lineSeparator());
                break;
            case '>':
            case '<':
            case TablaSimbolos.DISTINTO:
            case TablaSimbolos.MAYOR_IGUAL:
            case TablaSimbolos.MENOR_IGUAL:
            case TablaSimbolos.IGUAL:
                code.append("fld ").append(elem2.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fld ").append(elem1.getToken().getLexema(true)).append(System.lineSeparator())
                        .append("fcompp").append(System.lineSeparator())
                        .append("fstsw ax").append(System.lineSeparator())
                        .append("sahf").append(System.lineSeparator());
                break;


        }

        return code.toString();

    }

    private void codeLongint(ElemSimple elem2, ElemSimple elem1, int op, Stack<PolacaElem> stack, StringBuilder code) {
        String reg1 = "";
        String reg2 = "";

        Token token1 = elem1.getToken();
        Token token2 = elem2.getToken();

        String tipo1 = token1.getTipoToken();
        String tipo2 = token2.getTipoToken();

        switch (op) {
            case '+':
                if (tipoVar(tipo2)) {
                    if (tipoVar(tipo1)) { //caso var var
                        reg1 = SeguimientoRegistros.getInstance().regBC();
                        code.append("mov ").append(reg1).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator());
                    } else if (tipo1.equals(REGISTRO)) { //caso reg var
                        reg1 = token1.getLexema(true);
                    }
                    code.append("add ").append(reg1).append(", ").append(token2.getLexema(true)).append(System.lineSeparator());
                    Token t_reg = new Token(0, REGISTRO, reg1);
                    t_reg.addAtributo("tipo", "LONGINT");
                    ElemSimple registro = new ElemSimple(t_reg);
                    stack.push(registro);
                } else if (tipo2.equals(REGISTRO)) { //casos var reg - reg reg
                    code.append("add ").append(token1.getLexema(true)).append(", ").append(token2.getLexema(true))
                            .append(System.lineSeparator());
                    if (tipo1.equals(REGISTRO)) //en caso reg reg libero el segundo
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                    stack.push(elem1);
                }
                break;
            case '-':
                if (tipoVar(tipo2)) {
                    if (tipoVar(tipo1)) { //caso var var
                        reg1 = SeguimientoRegistros.getInstance().regBC();
                        code.append("mov ").append(reg1).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator());
                    } else if (tipo1.equals(REGISTRO)) { //caso reg var
                        reg1 = token1.getLexema(true);
                    }
                    code.append("sub ").append(reg1).append(", ").append(token2.getLexema(true)).append(System.lineSeparator());
                    Token t_reg = new Token(0, REGISTRO, reg1);
                    t_reg.addAtributo("tipo", "LONGINT");
                    ElemSimple registro = new ElemSimple(t_reg);
                    stack.push(registro);
                } else if (tipo2.equals(REGISTRO)) {
                    if (tipo1.equals(REGISTRO)) { //caso reg reg
                        code.append("sub ").append(token1.getLexema(true)).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        stack.push(elem1);
                    } else if (tipoVar(tipo1)) { //caso var reg
                        reg2 = SeguimientoRegistros.getInstance().regBC();
                        code.append("mov ").append(reg2).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator())
                                .append("sub ").append(reg2).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        Token t_reg = new Token(0, REGISTRO, reg2);
                        t_reg.addAtributo("tipo", "LONGINT");
                        ElemSimple registro = new ElemSimple(t_reg);
                        stack.push(registro);
                    }
                }
                break;
            case '*':
                if (tipoVar(tipo2)) {
                    if (tipoVar(tipo1)) { //caso var var
                        reg1 = SeguimientoRegistros.getInstance().regA();
                        if (reg1.equals("")) {
                            code.append(SeguimientoRegistros.getInstance().liberarA(stack, null, null));
                            reg1 = SeguimientoRegistros.getInstance().regA();
                        }
                        code.append("mov ").append(reg1).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator());
                    } else if (tipo1.equals(REGISTRO)) { //caso reg var
                        reg1 = token1.getLexema(true);
                    }
                    code.append("imul ").append(reg1).append(", ").append(token2.getLexema(true)).append(System.lineSeparator());
                    Token t_reg = new Token(0, REGISTRO, reg1);
                    t_reg.addAtributo("tipo", "LONGINT");
                    ElemSimple registro = new ElemSimple(t_reg);
                    stack.push(registro);
                } else if (tipo2.equals(REGISTRO)) { //casos var reg - reg reg
                    if (!token1.getLexema(true).equals("eax")) {
                        if (token2.getLexema(true).equals("eax")) {
                            ElemSimple aux = elem1;
                            elem1 = elem2;
                            elem2 = aux;
                            token1 = elem1.getToken();
                            token2 = elem2.getToken();
                        } else
                            code.append(SeguimientoRegistros.getInstance().liberarA(stack, token1, null));

                    }

                    code.append("imul ").append(token1.getLexema(true)).append(", ").append(token2.getLexema(true))
                            .append(System.lineSeparator());

                    if (tipo1.equals(REGISTRO)) //en caso reg reg libero el segundo
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                    stack.push(elem1);
                }
                break;
            case '/':
                if (tipoVar(tipo2)) {
                    if (tipoVar(tipo1)) { //caso var var
                        reg1 = SeguimientoRegistros.getInstance().regA();
                        if (reg1.equals("")) {
                            code.append(SeguimientoRegistros.getInstance().liberarA(stack, null, null));
                            reg1 = SeguimientoRegistros.getInstance().regA();
                        }
                        code.append("mov ").append(reg1).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator());
                    } else if (tipo1.equals(REGISTRO)) { //caso reg var
                        reg1 = token1.getLexema(true);
                    }
                    code.append("idiv ").append(reg1).append(", ").append(token2.getLexema(true)).append(System.lineSeparator());
                    Token t_reg = new Token(0, REGISTRO, reg1);
                    t_reg.addAtributo("tipo", "LONGINT");
                    ElemSimple registro = new ElemSimple(t_reg);
                    stack.push(registro);
                } else if (tipo2.equals(REGISTRO)) {
                    if (tipo1.equals(REGISTRO)) { //caso reg reg
                        if (!token1.getLexema(true).equals("eax"))
                            if (token2.getLexema(true).equals("eax")) {
                                String regAux = SeguimientoRegistros.getInstance().regBC();
                                code.append("mov ").append(regAux).append(", ").append(token2.getLexema(true)).append(System.lineSeparator())
                                        .append("mov ").append(token2.getLexema(true)).append(", ").append(token1.getLexema(true)).append(System.lineSeparator())
                                        .append("mov ").append(token1.getLexema(true)).append(", ").append(regAux).append(System.lineSeparator());
                                ElemSimple aux = elem1;
                                elem1 = elem2;
                                elem2 = aux;
                                token1 = elem1.getToken();
                                token2 = elem2.getToken();
                            }
                            else
                                code.append(SeguimientoRegistros.getInstance().liberarA(stack, token1, null));

                        code.append("idiv ").append(token1.getLexema(true)).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        stack.push(elem1);
                    } else if (tipoVar(tipo1)) { //caso var reg
                        reg2 = SeguimientoRegistros.getInstance().regA();
                        if (reg2.equals("")) {
                            code.append(SeguimientoRegistros.getInstance().liberarA(stack, null, token2));
                            reg2 = SeguimientoRegistros.getInstance().regA();
                        }
                        code.append("mov ").append(reg2).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator())
                                .append("idiv ").append(reg2).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        Token t_reg = new Token(0, REGISTRO, reg2);
                        t_reg.addAtributo("tipo", "LONGINT");
                        ElemSimple registro = new ElemSimple(t_reg);
                        stack.push(registro);
                    }
                }
                break;
            case '>':
            case '<':
            case TablaSimbolos.DISTINTO:
            case TablaSimbolos.MAYOR_IGUAL:
            case TablaSimbolos.MENOR_IGUAL:
            case TablaSimbolos.IGUAL:
                if (tipoVar(tipo2)) {
                    if (tipoVar(tipo1)) { //caso var var
                        reg1 = SeguimientoRegistros.getInstance().regBC();
                        code.append("mov ").append(reg1).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator());
                    } else if (tipo1.equals(REGISTRO)) { //caso reg var
                        reg1 = token1.getLexema(true);
                    }
                    code.append("cmp ").append(reg1).append(", ").append(token2.getLexema(true)).append(System.lineSeparator());
                    Token t_reg = new Token(0, REGISTRO, reg1);
                    t_reg.addAtributo("tipo", "LONGINT");
                    ElemSimple registro = new ElemSimple(t_reg);
                    stack.push(registro);
                } else if (tipo2.equals(REGISTRO)) {
                    if (tipo1.equals(REGISTRO)) { //caso reg reg
                        code.append("cmp ").append(token1.getLexema(true)).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        stack.push(elem1);
                    } else if (tipoVar(tipo1)) { //caso var reg
                        reg2 = SeguimientoRegistros.getInstance().regBC();
                        code.append("mov ").append(reg2).append(", ").append(token1.getLexema(true))
                                .append(System.lineSeparator())
                                .append("cmp ").append(reg2).append(", ").append(token2.getLexema(true))
                                .append(System.lineSeparator());
                        SeguimientoRegistros.getInstance().liberar(token2.getLexema(true));
                        Token t_reg = new Token(0, REGISTRO, reg2);
                        t_reg.addAtributo("tipo", "LONGINT");
                        ElemSimple registro = new ElemSimple(t_reg);
                        stack.push(registro);
                    }
                }
                stack.push(this);

        }

    }

    private boolean tipoVar(String tipo) {
        return tipo.equals(Main.IDENTIFICADOR) || tipo.equals(Main.CONSTANTE);
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
