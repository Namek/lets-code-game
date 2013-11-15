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

    def send(self, what, message):
        to_send = json.dumps({
            'type': what,
            'message': message
        })
        logger.debug('%s <- %s' % (self.id, to_send))
        self.fp.write(to_send + '\n')
        self.fp.flush()

    def exception(self, e):
        self.send('exception', str(e))
