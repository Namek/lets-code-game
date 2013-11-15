import json

from base import logger


class Player(object):
    name = None
    addr = None
    action_points = 0
    gold = 500

    def __init__(self, fp, addr):
        self.fp = fp
        self.addr = addr

    @property
    def id(self):
        return self.name or '%s:%s' % self.addr

    def _event_name(self, what):
        event = '%s%s' % (what[0].upper(), what[1:])
        return 'com.letscode.lcg.network.messages.%sMessage' % event

    def send(self, what, message={}):
        # lol java
        message.update({
            'class': self._event_name(what)
        })
        to_send = json.dumps({
            'type': what,
            'message': message
        })
        logger.debug('%s <- %s' % (self.id, to_send))
        self.fp.write(to_send + '\n')
        self.fp.flush()

    def exception(self, e):
        self.send('exception', {'message': str(e)})

    @property
    def state(self):
        return {
            'actionPoints': self.action_points,
            'gold': self.gold
        }

class Trujkont(object):
    owner = None
    building = None
    resources = 0

    def __init__(self, row, col, mapper):
        self.row = row
        self.col = col
        self.mapper = mapper

    def to_dict(self):
        return {
            'type': 'gold' if self.resources > 0 else 'normal',
            'owner': self.owner.name if self.owner else None,
            'building': self.building,
            'resourceSize': self.resources,
        }

    @property
    def is_upper(self):
        return self.row % 2 != self.col % 2

    @property
    def neighbours(self):
        neighbours = []
        if self.col > 0:
            neighbours.append(self.mapper.get_trujkont(self.row, self.col+1))
        if self.col < self.mapper.cols-1:
            neighbours.append(self.mapper.get_trujkont(self.row, self.col-1))
        if self.row > 0 and not self.is_upper:
            neighbours.append(self.mapper.get_trujkont(self.row-1, self.col))
        elif self.row < self.mapper.rows-1 and self.is_upper:
            neighbours.append(self.mapper.get_trujkont(self.row+1, self.col))
        return [n for n in neighbours if n]
