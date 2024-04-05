import math
from typing import Self

class Path:
    def __init__(self, vO, theta):
        self.g = 9.8
        self.vO = vO
        self.theta = theta
        self.vOx = self.vO*math.cos(self.theta*math.pi/180.0)
        self.vOy = self.vO*math.sin(self.theta*math.pi/180.0)
    
    def getX(self, t):
        self.t = t
        return self.vOx*self.t
    def getY(self, t):
        self.t = t
        return self.vOy*self.t
