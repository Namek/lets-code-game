from gevent.server import StreamServer

from overseer import Overseer


class LCG(object):
    def __init__(self, host, port, map_size):
        self.map_size = map_size
        self.server = StreamServer((host, port), self.redirect_to_overseer)

    def serve(self):
        self.new_overseer()
        self.server.serve_forever()

    def new_overseer(self):
        self.overseer = Overseer(self, self.map_size)

    def redirect_to_overseer(self, socket, addr):
        return self.overseer.handle(socket, addr)
