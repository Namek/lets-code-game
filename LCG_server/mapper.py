import random

from base import GameError
from models import Trujkont


class Mapper(object):
    GOLD = 0.1  # how many % of trujkonts get gold
    MISSING = 0.1  # how many % of trujkonts will be missing

    def __init__(self, ovrs, rows, cols):
        self.ovrs = ovrs
        self.rows = int(rows)
        self.cols = int(cols)

    def generate(self):
        arr = [
            [Trujkont(row-1, col-1, self) for col in range(self.cols)]
            for row in range(self.rows)
        ]
        self.map = arr
        # Missing
        for _ in range(int(self.rows*self.cols*self.MISSING)):
            trujkont = None
            while not trujkont:
                row = random.choice(arr)
                trujkont = random.choice(row)
            self.map[trujkont.row][trujkont.col] = None
        # Gold
        for _ in range(int(self.rows*self.cols*self.GOLD)):
            trujkont = None
            while not trujkont:
                row = random.choice(arr)
                trujkont = random.choice(row)
            trujkont.resources = random.randint(10, 100)
        # Players
        for p in self.ovrs.players:
            valid_triangle = False
            while not valid_triangle:
                row = random.choice(arr)
                trujkont = random.choice(row)
                if not trujkont:
                    continue
                valid_triangle = (
                    not trujkont.resources and
                    not trujkont.owner and
                    [t for t in trujkont.neighbours if not t.owner]
                )
            trujkont.owner = p
            trujkont.building = 'townhall'

    def to_dict(self):
        return {
            'map': [[t.to_dict() if t else None for t in r] for r in self.map]
        }

    def get_trujkont(self, row, col):
        row, col = int(row), int(col)
        try:
            return self.map[row][col]
        except IndexError:
            raise GameError('Why are you trying to get this trujkont?')

    def get_townhalls_count(self, player):
        # comprehensions, ho!
        return len([
            t for r in self.map for t in r if
            t and t.owner is player and t.building == 'townhall'
        ])

    @property
    def remaining_players(self):
        remaining = []
        for t in [t for r in self.map for t in r if t and t.owner]:
            if t.building == 'townhall' and t.owner not in remaining:
                remaining.append(t.owner)
        return remaining
