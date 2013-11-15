import json


class Player(object):
    name = None

    def __init__(self, fp):
        self.fp = fp

    def send(self, what, message):
        to_send = json.dumps({
            'type': what,
            'message': message
        })
        self.fp.write(to_send + '\n')
        self.fp.flush()

    def exception(self, e):
        self.send('exception', str(e))
