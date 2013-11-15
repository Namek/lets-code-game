# coding: utf-8
from base import GameError


class Handlers(object):
    EVENTS = {}

    def __init__(self, ovrs):
        self.EVENTS = {
            'handshake': self.handshake,
            'gameStart': self.game_start,
            'endTurn': self.end_turn
        }
        self.ovrs = ovrs  # ovrs as OVERSEEEEEARH

    def do(self, who, what, message):
        if not who.name and what != 'handshake':
            raise GameError('Handshake first')
        handler = self.EVENTS.get(what)
        if not handler:
            raise GameError('Invalid event type')
        handler(who, message)

    def handshake(self, who, message):
        if who.name:
            raise GameError('I already know who you are')
        if 'nickname' not in message:
            raise GameError(
                'Nickname must be provided, or else I will call you a '
                'perkeleen vittupää'
            )
        who.name = message['nickname']
        self.ovrs._players.append(who)
        players = [p for p in self.ovrs.players if p is not who]
        msg = {'players': [p.name for p in self.ovrs.players]}
        who.send('playerList', msg)
        for p in players:
            p.send('playerJoined', {'nickname': who.name})

    def end_turn(self, who, message):
        self.ovrs.next_player()

    def game_start(self, who, message):
        self.ovrs.mapper.generate()
        for p in self.ovrs.players:
            p.send('gameStarting', {'nickname': who.name})
            p.send('state', self.ovrs.mapper.to_dict())
