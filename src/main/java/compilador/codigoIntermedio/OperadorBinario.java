package compilador.codigoIntermedio;

import compilador.TablaSimbolos;

import java.util.Stack;

public class OperadorBinario extends PolacaElem{

    private int operador;
    private String palabra;
    private boolean huboError = false;


    public OperadorBinario(String palabra){
        this.palabra = palabra;
        this.operador = TablaSimbolos.getId(palabra);
    }

    @Override
    public String generarCodigo(Stack<PolacaElem> stack) {
        //TODO
        return null;
    }

    //TODO CHEQUEO DE SUMA

    @Override
    public boolean error() {
        return huboError;
    }

    public int getOperador(){
        return operador;
    }

    @Override
    public String toString() {
        return palabra;
    }
}
