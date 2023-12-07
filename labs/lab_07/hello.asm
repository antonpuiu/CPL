    .data
msg:    .asciiz "Hello World\n"
one: .asciiz "1\n"
two: .asciiz "2\n"
nw:  .asciiz "\n"
l: .asciiz "Large value\n"
s: .asciiz "Small value\n"
i:  .word 20
var:    .word 12
two_w:  .word 2
zero:   .word 0

while_str:  .asciiz "While loop\n"
for_str:    .asciiz "For loop\n"
hailstone:  .asciiz "Hailstone\n"
comma:  .asciiz ","

    .text
    .globl main

main:
    li $v0, 4       # syscall 4 (print_str)
    la $a0, msg     # argument: string
    syscall         # print the string

	sub $sp, $sp, 4
    sw $v1, 0($sp)

	lw $a0, 0($sp)
    addiu $sp, $sp, 4

    li $v0, 4
    la $a0, one
    syscall

    li $v1, 63
    ble $v1, 64, else
then:
    li $v0, 4
    la $a0, l
    syscall
    j end
else:
    li $v0, 4
    la $a0, s
    syscall
end:
    li $v0, 4
    la $a0, two
    syscall

    lw $a1, i

    li $v0, 4
    la $a0, while_str
    syscall

while:
    ble $a1, 0, end_while

    li $v0, 1
    move $a0, $a1
    syscall

    li $v0, 4
    la $a0, nw
    syscall

    addiu $a1, $a1, -1
    j while

end_while:
    lw $a1, i

    li $v0, 4
    la $a0, for_str
    syscall

for:
    ble $a1, 0, end_for

    sub $sp, $sp, 4
    sw $a1, 0($sp)

    addiu $a1, $a1, -1
    j for
end_for:
    lw $a1, 0($sp)
    add $sp, $sp, 4

    li $v0, 1
    move $a0, $a1
    syscall

    li $v0, 4
    la $a0, nw
    syscall

    bge $a1, 20, end_end_for
    j end_for

end_end_for:
    li $v0, 4
    la $a0, hailstone
    syscall

    lw $t0, var

    li $v0, 1
    move $a0, $t0
    syscall

    lw $t6, two_w
    lw $t7, zero

hailstone_start:
    beq $t0, 1, exit
    divu $t0, $t6
    mfhi $t1

    beq $t1, $t7, if_true
    mul $t0, $t0, 3
    addiu $t0, $t0, 1
    j hailstone_continue

if_true:
    mflo $t0
    j hailstone_continue

hailstone_continue:
    li $v0, 4
    la $a0, comma
    syscall

    li $v0, 1
    move $a0, $t0
    syscall

    j hailstone_start

exit:
    li $v0, 4
    la $a0, nw
    syscall

    li $v0, 10 #exit
    syscall
