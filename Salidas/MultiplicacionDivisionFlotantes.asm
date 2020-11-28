.386
.model flat, stdcall
option casemap :none

include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@string4 db "b != 4", 0
@aux7 dd ?
@aux6 dd ?
@aux5 dd ?
@aux4 dd ?
_b@main dd ?
_a@main dd ?
@aux8 dd ?
@string1 db "a = 4", 0
@float2 dd 1.0
@float1 dd 2.0
@float4 dd 3.0
@float3 dd 4.0
@string3 db "b = 4", 0
@aux3 dd ?
@string2 db "a != 4", 0
@aux2 dd ?
@aux1 dd ?
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@max_float dq 2147483647
@max_long dq 3.40282347e+38

.code
start:
fld @float1
fld @float2
fmul
fstp @aux1

fld @aux1
fld @float3
fmul
fstp @aux2

fld @aux2
fld @float1
fdiv
fstp @aux3

fld @aux3
fstp _a@main

fld @float1
fld @float2
fmul
fstp @aux4

fld @aux4
fld @float3
fmul
fstp @aux5

fld @aux5
fld @float4
fdiv
fstp @aux6

fld @aux6
fstp _b@main

fld _a@main
fld @float3
fcompp
fstsw ax
sahf

jne label27
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
jmp label30
label27:
invoke MessageBox, NULL, addr @string2, addr @string2, MB_OK
label30:
fld _b@main
fld @float3
fcompp
fstsw ax
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
invoke MessageBox, NULL, @string_recursion_mutua, @string_recursion_mutua, MB_OK
jmp label_end

label_end:
invoke ExitProcess, 0
end start