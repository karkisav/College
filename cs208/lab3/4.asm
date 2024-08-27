# Roll.No 23bcs118
# Name: Saurav Karki
# Takes in two values from the user and prints its gcd and lcm

.data
prompt1: .asciiz "Enter The first Number: "
prompt2: .asciiz "Enter the second Number: "
prompt3: .asciiz "LCM: "
prompt4: .asciiz "GCD: "
newline: .asciiz "\n"
.text
.globl main
main:
    li $v0, 4 # print "Enter The first Number"
    la $a0, prompt1
    syscall

    li $v0, 5 # Load the first number in t0 register
    syscall

    move $t0, $v0

    li $v0, 4 # print "Enter The second Number"
    la $a0, prompt1
    syscall

    li $v0, 5 # Load the second number in t1 register
    syscall

    move $t1, $v0

    move $t4, $t0  # store orignal first number
    move $t5, $t1  # store orignal second number

gcd:
    beq $t0, $t1, gcdEnd
    blt $t0, $t1, gcdSwap
    sub $t0, $t0, $t1
    j gcd
gcdSwap:
    move $t3, $t0
    move $t0, $t1
    move $t1, $t3
    j gcd
gcdEnd:
    move $t6, $t0
    mul $t3, $t4, $t5
    div $t3, $t6

    mflo $t3

    li $v0, 4
    la $a0, prompt3
    syscall
    
    li $v0, 1
    move $a0, $t6
    syscall

    li $v0, 4
    la $a0, newline
    syscall

    li $v0, 4
    la $a0, prompt4
    syscall

    li $v0, 1
    move $a0, $t3
    syscall

    li $v0, 10
    syscall
