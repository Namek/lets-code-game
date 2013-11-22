from gevent.server import StreamServer
from gevent import pywsgi
from geventwebsocket.handler import WebSocketHandler

from overseer import Overseer


class LCG(object):
    def __init__(self, host, port, map_size):
        self.map_size = map_size
        #self.server = StreamServer((host, port), self.redirect_to_overseer)
        self.server = pywsgi.WSGIServer((host, port), self.redirect_to_overseer, handler_class=WebSocketHandler)

    def serve(self):
        self.new_overseer()
        self.server.serve_forever()

    def new_overseer(self):
        self.overseer = Overseer(self, self.map_size)

    def redirect_to_overseer(self, environ, start_response):
        return self.overseer.handle(environ, start_response)
