import random

from models import Trujkont


class Mapper(object):
    GOLD = 0.1  # how many % of trujkonts get gold

    def __init__(self, ovrs, rows, cols):
        self.ovrs = ovrs
        self.rows = int(rows)
        self.cols = int(cols)

    def generate(self):
        arr = [
            [Trujkont(row, col) for col in range(self.cols)]
            for row in range(self.rows)
        ]
        for _ in range(int(self.rows*self.cols*self.GOLD)):
            trujkont = None
            while not Trujkont:
                row = random.choice(arr)
                trujkont = random.choice(row)
            trujkont.gold = random.randint(10, 100)
        for p in self.ovrs.players:
            trujkont = None
            while not Trujkont:
                row = random.choice(arr)
                trujkont = random.choice(row)
                if trujkont and (trujkont.gold or trujkont.owner):
                    trujkont = None
            trujkont.owner = p
            trujkont.building = 'townhall'
        self.map = arr

    def to_dict(self):
        return {
            'map': [[t.to_dict() for t in r] for r in self.map]
        }

    def get_trujkont(self, row, col):
        row, col = int(row), int(col)
        return self.map[row][col]
