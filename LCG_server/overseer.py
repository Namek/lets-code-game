from socket import error as socketerror
import json

from base import logger, GameError, ServerError
from handlers import Handlers
from models import Player


class Overseer(object):
    _players = []

    def __init__(self):
        self.handlers = Handlers(self)

    def handle(self, socket, addr):
        fp = socket.makefile()
        player = Player(fp, addr)
        logger.info('%s:%s connected' % addr)
        while True:
            try:
                line = fp.readline()
            except socketerror:
                break
            line = line.strip()
            if not line:
                break
            logger.debug('%s -> %s' % (player.id, line))
            try:
                parsed = json.loads(line)
            except ValueError:
                player.exception('What the hell are you sending to me?')
                continue
            try:
                self.delegate(player, parsed)
            except (GameError, ServerError) as e:
                player.exception(e)
                logger.info('%s raised %s' % (player.id, e))
                continue
        try:
            socket.shutdown(0)
            socket.close()
        except socketerror:  # whatever, I no more care about this socket
            pass
        logger.info('%s disconnected' % player.id)

    def delegate(self, who, msg):
        if not ('type' in msg and 'message' in msg):
            raise ServerError('Not enough JSON fields')
        what = msg['type']
        message = msg['message']
        self.handlers.do(who, what, message)

    @property
    def players(self):
        return [p for p in self._players if p]
