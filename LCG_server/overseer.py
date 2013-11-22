from socket import error as socketerror
from base64 import b64encode
import hashlib
import json

from base import logger, GameError, ServerError
from handlers import Handlers
from mapper import Mapper
from models import Player


class Overseer(object):
    def __init__(self, lcg, map_size):
        self.current_player_index = None
        self.game_started = False
        self._players = []
        self.lcg = lcg
        self.handlers = Handlers(self)
        self.mapper = Mapper(self, *map_size)

    def handle(self, environ, start_response):
        logger.info('Using overseer %s' % id(self))
        socket = environ["wsgi.websocket"]
   
        logger.debug(socket.__dict__)
        #fp = socket.makefile()
        player = Player(socket, start_response)
        enter_teh_infiniteh_loopah = True
        if self.game_started:
            logger.info(
                '%s tried to connect, but game has already started' % player.id
            )
            logger.info('Delegating %s to new overseer...' % player.id)
            self.lcg.new_overseer()
            return self.lcg.redirect_to_overseer(environ, start_response)
        logger.info('%s connected' % player.id)
        while enter_teh_infiniteh_loopah:
            try:
                line = socket.receive()
            except socketerror:
                break
            line = line.strip()
            if not line:
                break
            logger.debug('%s -> %s' % (player.id, line))
            try:
                parsed = json.loads(line)
            except ValueError:
                # is it websocket?
                is_websocket = (
                    not player.websocket_checked and
                    line == 'GET / HTTP/1.1'
                )
                if is_websocket:
                    player.websocket_checked = True
                    # loop!
                    key = None
                    while True:
                        try:
                            line = socket.receive().strip()
                            logger.debug(line)
                        except socketerror:
                            break
                        if line.lower().startswith('sec-websocket-key:'):
                            key = line.split(' ')[1]
                            key += '258EAFA5-E914-47DA-95CA-C5AB0DC85B11'
                            key = b64encode(hashlib.sha1(key).digest())
                        if line == '':
                            break
                    if key:
                        fp.write('HTTP/1.1 101 Switching Protocols\r\n')
                        fp.write('Upgrade: websocket\r\n')
                        fp.write('Connection: Upgrade\r\n')
                        fp.write('Sec-WebSocket-Accept: %s\r\n' % key)
                        fp.write('Sec-WebSocket-Protocol: chat\r\n\r\n')
                        fp.flush()
                        continue
                    else:
                        break
                else:
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
            #socket.shutdown(0)
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
        return [p for p in self._players if p and p.name]

    @property
    def current_player(self):
        return self._players[self.current_player_index]

    @current_player.setter
    def current_player(self, player):
        self.current_player_index = self._players.index(player)

    def next_player(self):
        if self.current_player_index is None:
            self.current_player_index = 0
        else:
            while self.current_player:
                i = (self.current_player_index+1) % len(self._players)
                self.current_player_index = i
                valid = (
                    self.current_player and
                    self.current_player in self.mapper.remaining_players
                )
                if valid:
                    break
        townhall_count = self.mapper.get_townhalls_count(self.current_player)
        self.current_player.action_points = 10 + (townhall_count-1)*2
        for p in [p for p in self.players if p is not self.current_player]:
            p.send('nextPlayer', {'nickname': self.current_player.name})
        self.current_player.send('yourTurn', self.current_player.state)

    def remove_player(self, player):
        try:
            if not self.game_started:
                self._players.remove(player)
            else:
                i = self._players.index(player)
                self._players[i] = None
        except ValueError:  # lol
            pass
        # Notify others
        for p in self.players:
            p.send('playerLeft', {'nickname': player.name})

    def end_game(self, winner):
        for p in self.players:
            p.send('gameEnd', {'winner': winner.name})
        self._players = [p for p in self.players]
        self.game_started = False
        self.current_player_index = None
