import random

from base import ServerError, GameError, logger
from models import Trujkont


class Mapper(object):
    GOLD = 0.1  # how many % of trujkonts get gold
    MISSING = 0.05  # how many % of trujkonts will be missing

    def __init__(self, ovrs, rows, cols):
        self.ovrs = ovrs
        self.rows = int(rows)
        self.cols = int(cols)
        if self.rows < 3 or self.cols < 3:
            raise ServerError('Map must be at least 4x4')

    def generate(self):
        arr = [
            [Trujkont(row, col, self) for col in range(self.cols)]
            for row in range(self.rows)
        ]
        self.map = arr
        # Missing
        for _ in range(int(self.rows*self.cols*self.MISSING)):
            valid_triangle = False
            while not valid_triangle:
                row = random.choice(arr)
                trujkont = random.choice(row)
                valid_triangle = (
                    trujkont and
                    len(trujkont.neighbours) == 3
                )
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
                    0 < trujkont.row < self.rows-1 and
                    0 < trujkont.col < self.cols-1 and
                    len([t for t in trujkont.neighbours if not t.owner]) == 3
                )
            trujkont.owner = p
            trujkont.building = 'townhall'
        self._display()

    def _display(self):
        for row in self.map:
            blah = []
            for t in row:
                if not t:
                    blah.append(' ')
                elif t.owner:
                    blah.append('P')
                elif t.resources:
                    blah.append('g')
                else:
                    blah.append('.')
            logger.debug(''.join(blah))

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
