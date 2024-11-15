def lcs(X, Y):
    # Get the lengths of the two strings
    m, n = len(X), len(Y)
    
    # Create a 2D table to store the LCS lengths for substrings of X and Y
    dp =[]
    for i in range(m + 1):
        row = [0] * (n + 1)
        dp.append(row)

    # Fill the table using a nested loop
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            # If characters match, add 1 to the previous diagonal value
            if X[i - 1] == Y[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                # If they don't match, take the maximum of the top or left cell
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    lcs_length = dp[m][n]
    # The length of the LCS is found in the bottom-right cell
    lcs_str = ""
    i, j = m, n
    while i > 0 and j > 0:
        if X[i - 1] == Y[j - 1]:
            lcs_str = X[i - 1] + lcs_str  # Add the character to the LCS string
            i -= 1
            j -= 1
        elif dp[i - 1][j] > dp[i][j - 1]:
            i -= 1
        else:
            j -= 1

    return lcs_length, lcs_str
    

# Example usage
#X = "ABCBDAB"
#Y = "BDCAB"
X = str(input("Enter X : "))
Y = str(input("Enter Y : "))
length, lcs_result = lcs(X, Y)

print("Length of LCS:", length)
print("Longest Common Subsequence:", lcs_result)