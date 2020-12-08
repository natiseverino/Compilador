package compilador.codigoIntermedio;

import compilador.Errores;
import compilador.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolacaInversa {

    private List<PolacaElem> elementos;
    private Stack<Integer> stack;
    private int errores;


    public PolacaInversa() {
        this.elementos = new ArrayList<>();
        this.stack = new Stack<>();
        this.errores = 0;
    }

    public int getErrores(){
        return errores;
    }

    public void addElem(PolacaElem elem, boolean usaPila){
        if(usaPila)
            if (!this.stack.empty())
                this.elementos.set(this.stack.pop(), elem); //obtengo la posicion de la pila e inserto el elemento ahi
            else{
                //Errores.addError("[PI] Error - Pila de posiciones de vacia \n");
                errores++;
            }

        else
            this.elementos.add(elem); //agrego el elemento al final
    }

    public void pushPos(boolean espacioVacio){
        this.stack.push(this.elementos.size()); //guardo posicion del espacio vacio

        if (espacioVacio)
            this.elementos.add(new ElemVacio()); //agrego elemento vacio
    }

    public int popPos(){
        if (!stack.empty())
            return this.stack.pop();
        else {
            errores++;
            return -1;
        }
    }

    public int size(){
        return elementos.size();
    }

    public String generarCodigo(boolean procedimiento){
        StringBuilder builder = new StringBuilder();
        Stack<PolacaElem> stackPol = new Stack<>(); //Stack de elementos de la polaca
        errores = 0;

        for(PolacaElem elem: elementos){
            String elemCodigo = elem.generarCodigo(stackPol);
            if (!elemCodigo.equals(""))
                builder.append(elemCodigo).append(System.lineSeparator());
            if (elem.error()){
                errores++;
                break;
            }
        }

        if(!procedimiento)
            builder.append("jmp label_end").append(System.lineSeparator()).append(System.lineSeparator());

        if (errores == 0)
            return builder.toString();

        return "";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        int cantDigitos = String.valueOf(elementos.size()-1).length();
        for (PolacaElem elem: elementos){
            builder.append(String.format("[%0"+cantDigitos+"d]\t", i));
            builder.append(elem.toString()).append("\n");
            i++;
        }

        return builder.toString();
    }

    public void print(){
        if (!elementos.isEmpty()) {
            System.out.println(Main.ANSI_BOLD_WHITE + ">> POLACA INVERSA" + Main.ANSI_RESET);
            System.out.println(this.toString());
        }
    }
}
