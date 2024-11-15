def min_edit_distance(str1, str2):
    m, n = len(str1), len(str2)
        
    # Create a table to store the minimum edit distance at each point
    dp = []
    for i in range(m + 1):
        # Create a row with n+1 columns initialized to 0
        row = [0] * (n + 1)
        dp.append(row)
    
    # Fill the dp table
    for i in range(m + 1):
        for j in range(n + 1):
            # If first string is empty, only option is to insert all characters of second string
            if i == 0:
                dp[i][j] = j
            # If second string is empty, only option is to remove all characters of first string
            elif j == 0:
                dp[i][j] = i
            # If characters are the same, no operation is required, take diagonal value
            elif str1[i - 1] == str2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1]
            # If characters are different, consider insert, delete, and replace operations
            else:
                dp[i][j] = min(dp[i - 1][j - 1],   # Replace
                               dp[i - 1][j],       # Delete
                               dp[i][j - 1]) + 1   # Insert
    
    # The answer is in the bottom-right corner of the dp table
    return dp[m][n]

# Example usage
str1 = "dinitrophenylhydrazine"
str2 = "dimethylhydrazine"
print("Minimum Edit Distance:", min_edit_distance(str1, str2))
