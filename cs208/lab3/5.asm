# Roll. No 23bcs118
# Name Suarav Karki
# takes in user input and calculates the value for nth integer in fibonacci series

.data
    prompt: .asciiz "Enter a positive integer n: "
    result: .asciiz "The nth and (n+1)th Fibonacci numbers are: "
    space:  .asciiz " "
    newline: .asciiz "\n"

.text
.globl main

main:
    # Print prompt
    li $v0, 4
    la $a0, prompt
    syscall

    # Read integer n
    li $v0, 5
    syscall
    move $t0, $v0  # $t0 = n

    # Calculate Fibonacci numbers using loop
    jal fib_loop

    # Print result
    li $v0, 4
    la $a0, result
    syscall

    move $a0, $v0
    li $v0, 1
    syscall

    li $v0, 4
    la $a0, space
    syscall

    move $a0, $v1
    li $v0, 1
    syscall

    # Exit program
    li $v0, 10
    syscall

fib_loop:
    li $t1, 0  # First Fibonacci number
    li $t2, 1  # Second Fibonacci number
    li $t3, 1  # Counter

    beq $t0, $zero, return_zero  # If n == 0, return 0
    beq $t0, 1, return_one       # If n == 1, return 1

loop:
    beq $t3, $t0, done  # If counter == n, exit loop
    add $t4, $t1, $t2   # Next Fibonacci number
    move $t1, $t2       # Shift numbers
    move $t2, $t4
    addi $t3, $t3, 1    # Increment counter
    j loop

done:
    move $v0, $t2       # nth Fibonacci number
    add $v1, $t1, $t2   # (n+1)th Fibonacci number
    jr $ra

return_zero:
    li $v0, 0
    li $v1, 1
    jr $ra

return_one:
    li $v0, 1
    li $v1, 1
    jr $ra