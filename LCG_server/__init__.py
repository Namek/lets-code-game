from gevent.server import StreamServer

from overseer import Overseer


class LCG(object):
    def __init__(self, host, port, map_size):
        self.map_size = map_size
        self.server = StreamServer((host, port), None)

    def serve(self):
        self.new_overseer()
        self.server.serve_forever()

    def new_overseer(self):
        self.overseer = Overseer(self, self.map_size)
        self.server.handle = self.overseer.handle
