import json

from base import logger


class Player(object):
    name = None
    addr = None

    def __init__(self, fp, addr):
        self.fp = fp
        self.addr = addr

    @property
    def id(self):
        return self.name or '%s:%s' % self.addr

    def _event_name(self, what):
        event = '%s%s' % (what[0].upper(), what[1:])
        return 'com.letscode.lcg.network.messages.%sMessage' % event

    def send(self, what, message):
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
        self.send('exception', str(e))


class Trujkont(object):
    owner = None
    building = None
    resources = 0

    def __init__(self, row, col):
        self.row = row
        self.col = col
