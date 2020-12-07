package compilador.codigoSalida;

import compilador.Main;
import compilador.TablaSimbolos;
import compilador.Token;
import compilador.codigoIntermedio.ElemSimple;
import compilador.codigoIntermedio.PolacaElem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SeguimientoRegistros {

    private static final String EAX = "eax"; //usar para dest de imul y idiv
    private static final String EBX = "ebx";
    private static final String ECX = "ecx";
    private static final String EDX = "edx";

    private static SeguimientoRegistros instance = null;
    private Map<String, Boolean> registros = new HashMap<>();
    //verdadero : liberado
    //falso : ocupado

    private SeguimientoRegistros() {
        registros.put("eax", true);
        registros.put("ebx", true);
        registros.put("ecx", true);
        registros.put("edx", true);
    }

    public static SeguimientoRegistros getInstance() {
        if (instance == null)
            instance = new SeguimientoRegistros();
        return instance;
    }

    public void liberar(String registro) {
        if (registros.containsKey(registro))
            registros.put(registro, true);
    }

    public boolean estaLibre(String registro) {
        return registros.get(registro);
    }

    public String regBC() {
        if (registros.get(EBX)) {
            registros.put(EBX, false);
            return EBX;
        }
        if (registros.get(ECX)) {
            registros.put(ECX, false);
            return ECX;
        }

        return regD(); //en el peor de los casos de que esten ocupados EBX y ECX
    }

    public String regD() {
        if (registros.get(EDX)) {
            registros.put(EDX, false);
            return EDX;
        }
        return "";
    }

    public String regA() {
        if (registros.get(EAX)) {
            registros.put(EAX, false);
            return EAX;
        }

        return "";
    }

    public String liberarA(Stack<PolacaElem> stack, Token reg, Token segReg) {
        StringBuilder code = new StringBuilder();
        String regLibre = regBC();

        if (reg == null && segReg == null) {
            //si eax esta usado en algun lado de la pila
            int pos = -1;
            for (int i = 0; i < stack.size(); i++)
                if ( stack.get(i).getLexema().equals(EAX)) {
                    pos = i;
                    liberar(EAX);
                }

            Token eax = ((ElemSimple) stack.get(pos)).getToken();

            Token nuevoToken = new Token(eax.getIdToken(), eax.getTipoToken(), regLibre);
            for (Map.Entry<String, Object> atributo : eax.getAtributos().entrySet()) {
                nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
            }
            ElemSimple elemNuevo = new ElemSimple(nuevoToken);

            stack.remove(pos);
            stack.insertElementAt(elemNuevo, pos);

        } else if (reg != null) {

            Token nuevoToken = new Token(reg.getIdToken(), reg.getTipoToken(), regLibre);
            for (Map.Entry<String, Object> atributo : reg.getAtributos().entrySet()) {
                nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
            }

            reg = nuevoToken;
            registros.put(EAX, false);
        }

        if (reg == null && segReg != null)
            if (segReg.getLexema(true).equals(EAX)) {
                liberar(EAX);
                Token nuevoToken = new Token(segReg.getIdToken(), segReg.getTipoToken(), regLibre);
                for (Map.Entry<String, Object> atributo : segReg.getAtributos().entrySet()) {
                    nuevoToken.addAtributo(atributo.getKey(), atributo.getValue());
                }
                segReg = nuevoToken;
                System.out.println(segReg.getLexema(true));
            }

        code.append("mov ").append(regLibre).append(", ").append(EAX)
                .append(System.lineSeparator());
        registros.put(regLibre, false);

        return code.toString();

    }


}
