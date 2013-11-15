
from models import Trujkont


class Mapper(object):
    def __init__(self, ovrs, rows, cols):
        self.ovrs = ovrs
        self.rows = int(rows)
        self.cols = int(cols)

    def generate(self):
        arr = [
            [Trujkont(row, col) for col in range(self.cols)]
            for row in range(self.rows)
        ]
        self.map = arr

    def to_dict(self):
        return {
            'map': [[t.to_dict() for t in r] for r in self.map]
        }

    def get_trujkont(self, row, col):
        row, col = int(row), int(col)
        return self.map[row][col]
