#include <iostream>

class ALU {
public: 
    int And(int a, int b){
        return a & b;
    }

    int Or(int a, int b){
        return a | b;
    }

    int Xor(int a, int b){
        return a ^ b;
    }

    int Not(int a){
        return !a;
    }

    int Nor(int a, int b){
        return !(a | b);
    }

    int Nand(int a, int b){
        return !( a & b);
    }

    int Stl(int a, int b){
        return a < b;
    }

    int add(int a, int b, int cin, int& cout){
        int sum = a ^ b ^ cin;
        cout = (a & b) | (a & cin) | (b & cin);
        return sum;
    }
}