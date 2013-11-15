from socket import error as socketerror
import json

from base import logger, GameError
from handlers import Handlers
from models import Player


class Overseer(object):
    def __init__(self):
        self.handlers = Handlers(self)

    def handle(self, socket, addr):
        logger.info('%s:%s connected' % addr)
        fp = socket.makefile()
        player = Player(fp)
        while True:
            try:
                line = fp.readline()
            except socketerror:
                break
            if not line:
                break
            player_id = player.name or '%s:%s' % addr
            logger.debug('%s: %s' % (player_id, line))
            try:
                parsed = json.loads(line)
            except ValueError:
                player.exception('Invalid JSON')
            try:
                self.delegate(parsed)
            except GameError as e:
                player.exception(e)
        try:
            socket.shutdown(0)
            socket.close()
        except socketerror:  # whatever, I no more care about this socket
            pass
        logger.info('%s disconnected' % player_id)

    def delegate(self):
        pass
