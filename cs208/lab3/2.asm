# Roll. No 23bcs118
# Area of an Rectangle
# Description: Takes in 2 values from the user and then calculates its Area

.data
prompt1: .asciiz "Enter the length of the rectangle: "
prompt2: .asciiz "Enter the breadth of the rectangle: "
.text
.globl main
main:
    li $v0, 4 # prints out string
    la $a0, prompt1
    syscall

    li $v0, 5 # takes in integer value
    syscall

    move $t0, $v0

    li $v0, 4 # prints out string
    la $a0, prompt2
    syscall

    li $v0, 5 # takes in integer value
    syscall

    move $t1, $v0

    mul $t2, $t1, $t0 # multiply the values in t1, t0  store and it in t2

    li $v0, 1 # print the values stored in t2
    move $a0, $t2
    syscall

    li $v0, 10
    syscall
