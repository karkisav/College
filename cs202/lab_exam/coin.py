def coin_change(coins, target):
    # Initialize DP array with a large number (infinity) to represent unreachable sums
    dp = [float('inf')] * (target + 1)
    
    # Base case: 0 coins are needed to make a sum of 0
    dp[0] = 0

    # Fill DP array
    for coin in coins:
        for i in range(coin, target + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)

    # If target sum is still infinity, it means it's not reachable
    return dp[target] if dp[target] != float('inf') else -1

# Example usage
coins = [1, 2, 5]
target = 11
print("Minimum coins needed:", coin_change(coins, target))