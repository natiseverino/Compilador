package compilador.codigoSalida;

import java.util.HashMap;
import java.util.Map;

public class SeguimientoRegistros {

    private static final String EAX = "eax"; //usar para dest de imul y idiv
    private static final String EBX = "ebx";
    private static final String ECX = "ecx";
    private static final String EDX = "edx";

    private static SeguimientoRegistros instance = null;
    private Map<String, Boolean> registros = new HashMap<>();
    //verdadero : liberado
    //falso : ocupado

    private SeguimientoRegistros(){
        registros.put("eax", true);
        registros.put("ebx", true);
        registros.put("ecx", true);
        registros.put("edx", true);
    }

    public static SeguimientoRegistros getInstance(){
        if (instance == null)
            instance = new SeguimientoRegistros();
        return instance;
    }

    public void liberar(String registro){
        if (registros.containsKey(registro))
            registros.put(registro, true);
    }

    public boolean estaLibre(String registro){
        return registros.get(registro);
    }

    public String regBC(){
        if (registros.get(EBX)){
            registros.put(EBX, false);
            return EBX;
        }
        if (registros.get(ECX)){
            registros.put(ECX, false);
            return ECX;
        }

        return regAD(); //en el peor de los casos de que esten ocupados EBX y ECX
    }

    public String regAD(){
        if (registros.get(EAX)){
            registros.put(EAX, false);
            return EAX;
        }
        if (registros.get(EDX)){
            registros.put(EDX, false);
            return EDX;
        }

        return ""; //TODO retornar auxiliar?
    }



}
