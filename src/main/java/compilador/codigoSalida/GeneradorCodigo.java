package compilador.codigoSalida;

import compilador.TablaSimbolos;
import compilador.codigoIntermedio.PolacaInversa;

public class GeneradorCodigo {

    private static GeneradorCodigo instance = null;

    private GeneradorCodigo() {

    }

    public static GeneradorCodigo getInstance() {
        if (instance == null)
            instance = new GeneradorCodigo();
        return instance;
    }

    public String generarCodigo(PolacaInversa polacaInversa) {
        StringBuilder code = new StringBuilder();
        String data = TablaSimbolos.getDataAssembler();
        String codigoIntermedio = polacaInversa.generarCodigo();

        code.append(".386").append(System.lineSeparator())
                .append(".model flat, stdcall").append(System.lineSeparator())
                .append("option casemap :none").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("includelib \\masm32\\lib\\kernel32.lib").append(System.lineSeparator())
                //TODO revisar lo de masm32
                .append("includelib \\masm32\\lib\\user32.lib").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("include \\masm32\\include\\masm32rt.inc").append(System.lineSeparator())
                .append("dll_dllcrt0 PROTO C").append(System.lineSeparator())
                .append("printf PROTO C :VARARG").append(System.lineSeparator())
                .append(".data").append(System.lineSeparator())
                .append(data)
                .append("@string_overflow_longint db \"Error: Overflow en suma de enteros largos\", 0")
                .append(System.lineSeparator())
                .append("@string_overflow_float db \"Error: Overflow en suma de flotantes\", 0")
                .append(System.lineSeparator())
                .append("@string_recursion_mutua db \"Error: Recursion mutua no permitida\", 0")
                .append(System.lineSeparator())
                .append("@max_float dq 2147483647")
                .append(System.lineSeparator())
                .append("@max_long dq 3.40282347e+38")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(".code")
                .append(System.lineSeparator())
                .append("start:")
                .append(System.lineSeparator())
                .append(codigoIntermedio)
                .append("jmp label_end")
                .append(System.lineSeparator())
                .append("label_overflow_longint:").append(System.lineSeparator())
                .append("print addr @string_overflow_longint") //TODO probar los prints
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append("label_overflow_float:").append(System.lineSeparator())
                .append("print addr @string_overflow_float")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append("label_recursion_mutua:").append(System.lineSeparator())
                .append("print addr @string_recursion_mutua")
                .append(System.lineSeparator())
                .append("jmp label_end").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("Label_end:").append(System.lineSeparator())
                .append("invoke ExitProcess, 0").append(System.lineSeparator())
                .append("end start");


        return code.toString();
    }


}
