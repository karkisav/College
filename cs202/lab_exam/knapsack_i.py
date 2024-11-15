def knapsack_01(weights, profits, capacity):
    # Number of items
    n = len(weights)
    
    # Create a DP table with (n+1) rows and (capacity+1) columns
    dp =[]
    for i in range(capacity + 1):
        row = [0] * (n + 1)
        dp.append(row)
    
    # Fill the DP table
    for i in range(1, n + 1):
        for w in range(capacity + 1):
            if weights[i - 1] <= w:
                # Option 1: Include the item
                include_item = dp[i - 1][w - weights[i - 1]] + profits[i-1]
                # Option 2: Exclude the item
                exclude_item = dp[i - 1][w]
                # Take the maximum of including or excluding the item
                dp[i][w] = max(include_item, exclude_item)
            else:
                # Item can't be included, so we exclude it
                dp[i][w] = dp[i - 1][w]
    
    # Find the items to include
    included_items = [0] * n  # Initially, all items are marked as excluded (0)
    w = capacity
    
    # Backtracking to find which items are included
    for i in range(n, 0, -1):
        if dp[i][w] != dp[i - 1][w]:  # Item i-1 was included
            included_items[i - 1] = 1  # Mark item as included
            w -= weights[i - 1]        # Reduce the remaining capacity
    
    # The bottom-right cell contains the maximum value we can achieve
    max_profit = dp[n][capacity]
    return max_profit, included_items

# Example usage:
weights = [2, 3, 4, 5]      # Weights of items
profits = [3, 4, 5, 6]       # Values of items
capacity = 5                # Knapsack capacity
max_profit, inclusion_list = knapsack_01(weights, profits, capacity)
print("Maximum value:", max_profit)
print("Inclusion list:", inclusion_list)