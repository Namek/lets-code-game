import logging


def set_logger():
    logger = logging.getLogger('lcg')
    logger.setLevel(logging.INFO)
    ch = logging.StreamHandler()
    ch.setLevel(logging.DEBUG)
    formatter = logging.Formatter(
        fmt='[%(asctime)s] (%(levelname)s) %(message)s',
        datefmt='%H:%M:%S'
    )
    ch.setFormatter(formatter)
    logger.addHandler(ch)


logger = logging.getLogger('lcg')


class GameError(Exception):
    def __str__(self):
        return 'Game exception: %s' % self.message


class ServerError(Exception):
    def __str__(self):
        return 'Server exception: %s' % self.message
