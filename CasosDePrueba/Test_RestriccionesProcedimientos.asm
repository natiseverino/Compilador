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
 dd 1
 dd 2
_b@main@inv dd 0
@b@main@max_inv dd 2
_d@main@b@inv dd 0
@d@main@b@max_inv dd 1
_c@main@inv dd 0
@c@main@max_inv dd 1
@string_overflow_longint db "Error: Overflow en suma de enteros largos", 0
@string_overflow_float db "Error: Overflow en suma de flotantes", 0
@string_recursion_mutua db "Error: Recursion mutua no permitida", 0
@string_ni_exceeded db "Error: Se ha superado el maximo de invocaciones del procedimiento", 0

@max_float dd 3.40282347E38
@min_float dd -3.40282347E38
@last_proc_father dd 0

.code
start:
mov ebx, _c@main@inv
add ebx, 1
mov _c@main@inv, ebx
cmp ebx, @c@main@max_inv
jg label_ni_exceeded
mov ebx, @last_proc_father
cmp ebx, 1
je label_recursion_mutua
mov @last_proc_father, 0
call _main@c
mov ebx, _c@main@inv
add ebx, 1
mov _c@main@inv, ebx
cmp ebx, @c@main@max_inv
jg label_ni_exceeded
mov ebx, @last_proc_father
cmp ebx, 1
je label_recursion_mutua
mov @last_proc_father, 0
call _main@c
mov ebx, _b@main@inv
add ebx, 1
mov _b@main@inv, ebx
cmp ebx, @b@main@max_inv
jg label_ni_exceeded
mov ebx, @last_proc_father
cmp ebx, 2
je label_recursion_mutua
mov @last_proc_father, 0
call _main@b
jmp label_end

_main@b proc
mov ebx, _d@main@b@inv
add ebx, 1
mov _d@main@b@inv, ebx
cmp ebx, @d@main@b@max_inv
jg label_ni_exceeded
mov ebx, @last_proc_father
cmp ebx, 3
je label_recursion_mutua
mov @last_proc_father, 2
call _main@b@d
ret
_main@b endp
_main@b@d proc
mov ebx, _b@main@inv
add ebx, 1
mov _b@main@inv, ebx
cmp ebx, @b@main@max_inv
jg label_ni_exceeded
mov ebx, @last_proc_father
cmp ebx, 2
je label_recursion_mutua
mov @last_proc_father, 0
call _main@b
ret
_main@b@d endp


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