# Function to calculate maximum profit for fractional knapsack
def fractional_knapsack(weights, values, capacity):
    # Create a list to store (weight, value, ratio) for each item
    items = []
    for i in range(len(weights)):
        ratio = values[i] / weights[i]  # Calculate value-to-weight ratio
        items.append((weights[i], values[i], ratio))  # Store weight, value, and ratio

    # Sort items by ratio in descending order
    items.sort(key=lambda x: x[2], reverse=True)

    max_profit = 0
    for weight, value, ratio in items:
        if capacity >= weight:
            # Take the whole item if there's enough capacity
            max_profit += value
            capacity -= weight
        else:
            # Take the fraction of the remaining capacity
            max_profit += ratio * capacity
            break  # Knapsack is full
    
    return max_profit

# Example usage
weights = [10, 20, 30]
values = [60, 100, 120]
capacity = 50
max_profit = fractional_knapsack(weights, values, capacity)
print("Maximum profit that can be generated with the given weights is:", max_profit)
