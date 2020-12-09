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
@long3 dd 1
@string1 db "funcion", 0
@float2 dd -2.0
@long1 dd 25
_b@main dd ?
@float1 dd 2.0
_funcion@main@inv dd 0
@funcion@main@max_inv dd 1
@long2 dd 5
_a@main dd ?
_c@main dd ?
@long4 dd 10
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@string_ni_exceeded db "Error: Se ha superado el maximo de invocaciones del procedimiento", 0

@max_float dd 3.40282347E38
@min_float dd -3.40282347E38
@last_proc_father dd 0

.code
start:
mov ebx, @long1
mov _a@main ,ebx

mov ebx, _a@main
cmp ebx, @long2

jle label15
mov ecx, _a@main
add ecx, @long3

jo label_overflow_longint

mov _a@main, ecx

jmp label16
label15:
label16:
fld @float1
fstp _b@main

mov ecx, _funcion@main@inv
add ecx, 1
mov _funcion@main@inv, ecx
cmp ecx, @funcion@main@max_inv
jg label_ni_exceeded
mov ecx, @last_proc_father
cmp ecx, 1
je label_recursion_mutua
mov @last_proc_father, 0
call _main@funcion
mov ecx, @long3
mov _a@main ,ecx

label26:
mov ecx, _a@main
cmp ecx, @long4

jge label45
mov edx, @long2
mov _a@main ,edx

fld @float2
fstp _b@main

mov edx, _a@main
add edx, @long3

jo label_overflow_longint

mov _a@main, edx

jmp label26
label45:
jmp label_end

_main@funcion proc
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
ret
_main@funcion endp


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