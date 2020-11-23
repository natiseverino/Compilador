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
@long2 dd 1
@long1 dd 2
@long4 dd 3
_b@main dd ?
@long3 dd 4
_a@main dd ?
@string1 db "a = 4", 0
@string3 db "b = 4", 0
@string2 db "a != 4", 0
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@max_float dq 2147483647
@max_long dq 3.40282347e+38

.code
start:
mov eax, @long1
imul eax, @long2

imul eax, @long3

cdq
idiv @long1

mov _a@main, eax

mov eax, @long1
imul eax, @long2

imul eax, @long3

cdq
idiv @long4

mov _b@main, eax

mov ebx, _a@main
cmp ebx, @long3

jne label27
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
jmp label30
label27:
invoke MessageBox, NULL, addr @string2, addr @string2, MB_OK
label30:
mov ecx, _b@main
cmp ecx, @long3

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