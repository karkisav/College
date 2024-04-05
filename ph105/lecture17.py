def Square(X):
    return X * X
def sumofSquares(Array, n):
    sum = 0
    for i in range(n):
        SquaredValue = Square(Array[i])
        Sum += SquaredValue
    return Sum
Array = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10 , 11, 12]
n = len(Array)

class Main:
    def __init__(self):
        self.String1 = "Hello"
        self.String2 = "world"
    def function1(self):
        self.Function2()
        print("Function1: ", self.String2)
        return
    def function2(self):
        print("Function2: ", self.String1)
        return
object =  Main()
object.Function1()

class parent:
    def __init__(self):
        self.String1 = "Hello"
        self.String2 = "world"
    def function2(self):
        print("Function2: ", self.String1)
        return
    
class Child(parent):
    def function1(self):
        self.Function2()
        print("Function1: ", self.String2)
        return
object1 = parent()
object2 = Child()
object2.Function1()