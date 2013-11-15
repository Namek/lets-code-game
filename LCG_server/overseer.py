from socket import error as socketerror

from base import logger


class Overseer(object):
    def handle(self, socket, addr):
        logger.info('%s:%s connected' % addr)
        fp = socket.makefile()
        while True:
            try:
                line = fp.readline()
            except socketerror:
                break
            if not line:
                break
            fp.write(line+'\n')
            fp.flush()
        try:
            socket.shutdown(0)
            socket.close()
        except socketerror: # whatever, I don't care anymore
            pass
        logger.info('%s:%s disconnected' % addr)
