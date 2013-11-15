


class Mapper(object):
    def __init__(self, ovrs, rows, cols):
        self.ovrs = ovrs
        self.rows = rows
        self.cols = cols

    def generate(self):
        pass

    @property
    def state(self):
        return self.map
