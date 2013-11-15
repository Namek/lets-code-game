from gevent.server import StreamServer

from overseer import Overseer


class LCG(object):
    def __init__(self, host, port, map_size):
        self.overseer = Overseer(map_size)
        self.server = StreamServer((host, port), self.overseer.handle)

    def serve(self):
        self.server.serve_forever()
