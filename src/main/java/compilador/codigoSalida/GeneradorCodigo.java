package compilador.codigoSalida;

import compilador.TablaSimbolos;
import compilador.codigoIntermedio.PolacaInversa;
import compilador.codigoIntermedio.PolacaInversaProcedimientos;

public class GeneradorCodigo {

    private static GeneradorCodigo instance = null;

    private GeneradorCodigo() {

    }

    public static GeneradorCodigo getInstance() {
        if (instance == null)
            instance = new GeneradorCodigo();
        return instance;
    }

    public String generarCodigo(PolacaInversa polacaInversa, PolacaInversaProcedimientos polacaInversaProcedimientos) {
        StringBuilder code = new StringBuilder();
        String codigoIntermedio = polacaInversa.generarCodigo(false);
        codigoIntermedio += polacaInversaProcedimientos.generarCodigo();
        String data = TablaSimbolos.getDataAssembler();



        code.append(".386").append(System.lineSeparator())
                .append(".model flat, stdcall").append(System.lineSeparator())
                .append("option casemap :none").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("include \\masm32\\include\\masm32rt.inc").append(System.lineSeparator())
                .append("include \\masm32\\include\\windows.inc").append(System.lineSeparator())
                .append("include \\masm32\\include\\kernel32.inc").append(System.lineSeparator())
                .append("include \\masm32\\include\\user32.inc").append(System.lineSeparator())
                .append("includelib \\masm32\\lib\\kernel32.lib").append(System.lineSeparator())
                .append("includelib \\masm32\\lib\\user32.lib").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("dll_dllcrt0 PROTO C").append(System.lineSeparator())
                .append("printf PROTO C :VARARG").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(".data").append(System.lineSeparator())
                .append(data)
                .append("@string_overflow_longint db \"Error: Overflow en suma de enteros largos\", 0")
                .append(System.lineSeparator())
                .append("@string_overflow_float db \"Error: Overflow en suma de flotantes\", 0")
                .append(System.lineSeparator())
                .append("@string_recursion_mutua db \"Error: Recursion mutua no permitida\", 0")
                .append(System.lineSeparator())
                .append("@string_ni_exceeded db \"Error: Se ha superado el maximo de invocaciones del procedimiento\", 0")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("@max_float dq 3.40282347E38")
                .append(System.lineSeparator())
                .append("@min_float dq -3.40282347E38")
                .append(System.lineSeparator())
                .append("@last_proc_father dd 0")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(".code")
                .append(System.lineSeparator())
                .append("start:")
                .append(System.lineSeparator())
                .append(codigoIntermedio)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("label_overflow_longint:").append(System.lineSeparator())
                .append("invoke MessageBox, NULL, addr @string_overflow_longint, addr @string_overflow_longint, MB_OK")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("label_overflow_float:").append(System.lineSeparator())
                .append("invoke MessageBox, NULL, addr @string_overflow_float, addr @string_overflow_float, MB_OK")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("label_recursion_mutua:").append(System.lineSeparator())
                .append("invoke MessageBox, NULL, addr @string_recursion_mutua, addr @string_recursion_mutua, MB_OK")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("label_ni_exceeded:").append(System.lineSeparator())
                .append("invoke MessageBox, NULL, addr @string_ni_exceeded, addr @string_ni_exceeded, MB_OK")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("label_end:").append(System.lineSeparator())
                .append("invoke ExitProcess, 0").append(System.lineSeparator())
                .append("end start");


        return code.toString();
    }


}
