.386
.model flat, stdcall
option casemap :none

include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@string1 db "Hola mundo!", 0
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@max_float dq 2147483647
@max_long dq 3.40282347e+38

.code
start:
invoke MessageBox, NULL, addr @string1, addr @string1, MB_OK
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