# coding: utf-8
from base import GameError


class Handlers(object):
    COST = {  # action points, gold
        'conquer': (2, 0),
        'mine_gold': (3, 0),
        'build_townhall': (7, 600),
        'build_mine': (6, 400),
        'build_barricade': (4, 300),
    }

    def __init__(self, ovrs):
        self.EVENTS = {
            'handshake': self.handshake,
            'gameStart': self.game_start,
            'endTurn': self.end_turn,
            'makeMove': self.move,
        }
        self.MOVES = {
            'conquer': self.conquer,
            'mine_gold': self.mine_gold,
            'build_townhall': self.build_townhall,
            'build_mine': self.build_mine,
            'build_barricade': self.build_barricade,
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

    def move(self, who, message):
        if not ('row' in message and 'col' in message and 'what' in message):
            raise GameError('Row, col or what not provided')
        row, col = message['row'], message['col']
        trujkont = self.ovrs.mapper.get_trujkont(row, col)
        fnc = self.MOVES.get(message['what'])
        if not fnc:
            raise GameError('Invalid move type')
        cost_ap, cost_gold = self.COST.get(message['what'])
        if who.action_points < cost_ap:
            raise GameError('Not enough action points')
        if who.gold < cost_gold:
            raise GameError('Not enough gold moneyz')
        fnc(who, trujkont)

    def build_mine(self, who, trujkont):
        self._build(who, trujkont, 'mine')

    def build_townhall(self, who, trujkont):
        self._build(who, trujkont, 'mine')

    def build_barricade(self, who, trujkont):
        self._build(who, trujkont, 'mine')

    def _build(self, who, trujkont, building):
        if trujkont.owner is not who:
            raise GameError('You do not own that trujkont')
        if trujkont.building:
            raise GameError('One triangle = one building')
        trujkont.building = building
