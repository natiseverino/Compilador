package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolacaInversa {

    List<String> elementos;
    Stack<Integer> stack;


    public PolacaInversa() {
        this.elementos = new ArrayList<>();
        this.stack = new Stack<>();
    }

    public void addElem(String elem){
        elementos.add(elem);
    }

    public int getSize(){
        return elementos.size();
    }
}
