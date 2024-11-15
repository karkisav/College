def rod_cutting(prices, n):
    
    dp = [0] * (n + 1)

    for length in range(1, n + 1):
        max_rev = 0
        for cut_length in range(1, length + 1):
            max_rev = max(max_rev, dp[length - cut_length] + prices[cut_length - 1])
        dp[length] = max_rev
    return dp[n]

def main():
    prices = [2, 5, 9, 8, 10, 14, 18, 20]  # Prices for rod lengths 1 to 8
    n = 8  # Length of the rod

    print(rod_cutting(prices, n))

if __name__ == "__main__":
    main()