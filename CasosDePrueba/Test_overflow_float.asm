.386
.model flat, stdcall
option casemap :none

include \masm32\include\masm32rt.inc
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.data
@float1 dd 3.0E38
@float4 dd -6.0E37
@aux4 dw ?
_b@main dd ?
_a@main dd ?
@float3 dd -3.0E38
@aux3 dd ?
@float2 dd 5.0E37
@aux2 dw ?
@aux1 dd ?
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@string_ni_exceeded db "Error: Se ha superado el maximo de invocaciones del procedimiento", 0

@max_float dd 3.40282347E38
@min_float dd -3.40282347E38
@last_proc_father dd 0

.code
start:
fld @float1
fstp _a@main

fld @float2
fstp _b@main

fld _a@main
fld _b@main
fadd
fstp @aux1

fld @aux1
fld @max_float
fcompp
fstsw @aux2
mov ax, @aux2
sahf
jae label_overflow_float
fld @aux1
fld @min_float
fcompp
fstsw @aux2
mov ax, @aux2
sahf
jbe label_overflow_float


fld @aux1
fstp _a@main

fld @float3
fstp _a@main

fld @float4
fstp _b@main

fld _a@main
fld _b@main
fadd
fstp @aux3

fld @aux3
fld @max_float
fcompp
fstsw @aux4
mov ax, @aux4
sahf
jae label_overflow_float
fld @aux3
fld @min_float
fcompp
fstsw @aux4
mov ax, @aux4
sahf
jbe label_overflow_float


fld @aux3
fstp _b@main

jmp label_end



label_overflow_longint:
invoke MessageBox, NULL, addr @string_overflow_longint, addr @string_overflow_longint, MB_OK
jmp label_end

label_overflow_float:
invoke MessageBox, NULL, addr @string_overflow_float, addr @string_overflow_float, MB_OK
jmp label_end

label_recursion_mutua:
invoke MessageBox, NULL, addr @string_recursion_mutua, addr @string_recursion_mutua, MB_OK
jmp label_end

label_ni_exceeded:
invoke MessageBox, NULL, addr @string_ni_exceeded, addr @string_ni_exceeded, MB_OK
jmp label_end

label_end:
invoke ExitProcess, 0
end start