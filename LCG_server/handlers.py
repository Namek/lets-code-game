# coding: utf-8
from base import GameError


class Handlers(object):
    EVENTS = {}

    def __init__(self, ovrs):
        self.EVENTS = {
            'handshake': self.handshake
        }
        self.ovrs = ovrs  # ovrs as OVERSEEEEEARH

    def do(self, who, what, message):
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
