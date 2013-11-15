from socket import error as socketerror
import json

from base import logger, GameError, ServerError
from handlers import Handlers
from mapper import Mapper
from models import Player


class Overseer(object):
    _players = []
    current_player_index = None
    game_started = False

    def __init__(self, map_size):
        self.handlers = Handlers(self)
        self.mapper = Mapper(self, *map_size)

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
        self.remove_player(player)
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

    @property
    def current_player(self):
        return self._players[self.current_player_index]

    @current_player.setter
    def current_player(self, player):
        self.current_player_index = self._players.index(player)

    def next_player(self):
        if self.current_player_index is None:
            self.current_player_index = 0
            return
        while self.current_player:
            i = (self.current_player_index+1) % len(self._players)
            self.current_player_index = i
            if self.current_player:
                break

    def remove_player(self, player):
        if not self.game_started:
            self._players.remove(player)
        else:
            i = self._players.index(player)
            self._players[i] = None
        # Notify others
        for p in self.players:
            p.send('playerLeft', {'nickname': player.name})
