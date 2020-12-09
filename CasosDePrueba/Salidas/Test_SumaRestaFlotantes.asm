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
@aux7 dw ?
@aux11 dw ?
@aux6 dd ?
@aux12 dw ?
@aux5 dd ?
@string4 db "b != 5", 0
@aux4 dw ?
_b@main dd ?
_a@main dd ?
@aux9 dw ?
@aux8 dd ?
@aux10 dd ?
@string1 db "a = 5", 0
@float2 dd 1.0
@float1 dd 2.0
@float4 dd 3.0
@float3 dd 4.0
@float5 dd 5.0
@string2 db "a != 5", 0
@aux3 dd ?
@string3 db "b = 5", 0
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
fld @float2
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
fld @float3
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
fld @float1
fsub
fstp @aux5

fld @aux5
fstp _a@main

fld @float1
fld @float2
fadd
fstp @aux6

fld @aux6
fld @max_float
fcompp
fstsw @aux7
mov ax, @aux7
sahf
jae label_overflow_float
fld @aux6
fld @min_float
fcompp
fstsw @aux7
mov ax, @aux7
sahf
jbe label_overflow_float


fld @aux6
fld @float3
fadd
fstp @aux8

fld @aux8
fld @max_float
fcompp
fstsw @aux9
mov ax, @aux9
sahf
jae label_overflow_float
fld @aux8
fld @min_float
fcompp
fstsw @aux9
mov ax, @aux9
sahf
jbe label_overflow_float


fld @aux8
fld @float4
fsub
fstp @aux10

fld @aux10
fstp _b@main

fld @float5
fld _a@main
fcompp 
fstsw @aux11
mov ax, @aux11
sahf

jne label27
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
jmp label30
label27:
invoke MessageBox, NULL, addr @string2, addr @string2, MB_OK
label30:
fld @float5
fld _b@main
fcompp 
fstsw @aux12
mov ax, @aux12
sahf

jne label40
invoke MessageBox, NULL, addr @string3, addr @string3, MB_OK
jmp label43
label40:
invoke MessageBox, NULL, addr @string4, addr @string4, MB_OK
label43:
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