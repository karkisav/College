# Roll. No 23bcs118
# Name Suarav Karki
# usses bubble sort to sort the elements

.data
    array:    .word 64, 34, 25, 12, 22, 11, 90   # The array to be sorted
    size:     .word 7                            # Size of the array
    space:    .asciiz " "                        # Space for printing
    sorted:   .asciiz "\nSorted array: "         # Message for sorted array

.text
.globl main

main:
    la $s0, array    # Load address of the array
    lw $s1, size     # Load size of the array
    
    # Outer loop (i)
    li $t0, 0        # i = 0
outer_loop:
    addi $t0, $t0, 1         # i++
    bge $t0, $s1, print_result # If i >= size, exit outer loop

    # Inner loop (j)
    li $t1, 0        # j = 0
inner_loop:
    sub $t2, $s1, $t0        # t2 = size - i
    bge $t1, $t2, outer_loop # If j >= size-i, go to next iteration of outer loop

    # Load elements to compare
    sll $t3, $t1, 2          # t3 = j * 4 (byte offset)
    add $t3, $s0, $t3        # Address of array[j]
    lw $t4, 0($t3)           # t4 = array[j]
    lw $t5, 4($t3)           # t5 = array[j+1]

    # Compare and swap if necessary
    ble $t4, $t5, no_swap
    sw $t5, 0($t3)           # array[j] = t5
    sw $t4, 4($t3)           # array[j+1] = t4

no_swap:
    addi $t1, $t1, 1         # j++
    j inner_loop

print_result:
    # Print "Sorted array: "
    li $v0, 4
    la $a0, sorted
    syscall

    # Print sorted array
    la $t0, array    # Reset to start of array
    li $t1, 0        # Counter

print_loop:
    beq $t1, $s1, exit   # If we've printed all elements, exit
    
    lw $a0, 0($t0)       # Load the current element
    li $v0, 1            # Print integer
    syscall

    li $v0, 4            # Print space
    la $a0, space
    syscall

    addi $t0, $t0, 4     # Move to next element
    addi $t1, $t1, 1     # Increment counter
    j print_loop

exit:
    li $v0, 10           # Exit program
    syscall