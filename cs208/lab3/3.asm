# Roll.No: 23bcs118
# Name: Saurav Suresh Karki

.data
    prompt: .asciiz "Enter a string"
    isPalindrome: .asciiz "The string is a palindrome.\n"
    notPalindrome: .asciiz "The string is not a palindrome.\n"
    buffer: .space 100
    length: .word 0

.text
.globl main
main:
    li $v0, 4
    la $a0, prompt
    syscall

    li $v0, 8 # v0 code 8 to read a string
    la $a0, buffer
    li $a1, 100 # alloting 100 char space
    syscall

    la $t0, buffer
    li $t1, 0

length_loop:
    lb $t2, ($t0)
    beqz $t2, end_length
    bne $t2, 10, not_newline
    sb $zero, ($t10)
    j end_length

not_newline:
    addi $t1, $t1, 1
    addi $t0, $t0, 1
    j length_loop

end_length:
    sw $t1, length
    la $t0, buffer
    add $t1, $t0, $t1
    addi $t1, $t1, -1

check_palindrome:
    bge $t0, $t1, is_palindrome

    lb $t2, ($t0)
    lb $t3, ($t1)

    bne $t2, $t3m, not_Palindrome

    addi $t0, $t0, 1
    addi $t1, $t1, -1

    j check_palindrome

is_palindrome:
    li $v0, 4
    la $a0, isPalindrome
    syscall
    
    j exit

not_Palindrome:
    li $v0, 4
    la $a0, notPalindrome   
    syscall

    j exit
exit:
    li $v0, 10
    syscall