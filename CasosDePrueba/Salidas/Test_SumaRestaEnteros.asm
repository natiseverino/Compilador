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
@long2 dd 1
@long1 dd 2
@long4 dd 3
@string4 db "b != 5", 0
_b@main dd ?
@long3 dd 4
@long5 dd 5
_a@main dd ?
@string1 db "a = 5", 0
@string2 db "a != 5", 0
@string3 db "b = 5", 0
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
add ebx, @long2

jo label_overflow_longint

add ebx, @long3

jo label_overflow_longint

sub ebx, @long1

mov _a@main, ebx

mov ebx, @long1
add ebx, @long2

jo label_overflow_longint

add ebx, @long3

jo label_overflow_longint

sub ebx, @long4

mov _b@main, ebx

mov ebx, _a@main
cmp ebx, @long5

jne label27
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
jmp label30
label27:
invoke MessageBox, NULL, addr @string2, addr @string2, MB_OK
label30:
mov ecx, _b@main
cmp ecx, @long5

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