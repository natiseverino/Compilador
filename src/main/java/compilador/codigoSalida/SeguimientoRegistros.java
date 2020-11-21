package compilador.codigoSalida;

import java.util.HashMap;
import java.util.Map;

public class SeguimientoRegistros {

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

    public String regLibre(){
        for(String reg : registros.keySet())
            if (registros.get(reg)) {
                registros.put(reg, false);
                return reg;
            }
        return "";
    }



}
