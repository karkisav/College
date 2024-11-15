def subset_sum(arr, target):
    n = len(arr)
    
    # Initialize dp table with False values for each cell
    dp = []
    for i in range(n + 1):
        row = [False] * (target + 1)  # Each row has target + 1 columns
        dp.append(row)

    # Set dp[i][0] to True for all i, since a sum of 0 is always possible with an empty subset
    for i in range(n + 1):
        dp[i][0] = True

    # Fill the dp table
    for i in range(1, n + 1):
        for j in range(1, target + 1):
            if j < arr[i - 1]:
                dp[i][j] = dp[i - 1][j]
            else:
                dp[i][j] = dp[i - 1][j] or dp[i - 1][j - arr[i - 1]]

    return dp[n][target]

# Example usage
arr = [3, 34, 4, 12, 5, 2]
target = 9
print("Is subset sum possible?", subset_sum(arr, target))