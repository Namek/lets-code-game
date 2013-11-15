import argparse
import logging

from gevent.monkey import patch_all

from LCG_server import LCG
from LCG_server.base import set_logger


if __name__ == '__main__':
    # gevent!
    patch_all()
    # logging!
    set_logger()
    logger = logging.getLogger('lcg')
    # args!
    parser = argparse.ArgumentParser()
    parser.add_argument('--host', help='host', default='localhost')
    parser.add_argument('-p', '--port', help='port', default=80, type=int)
    parser.add_argument(
        '-m', '--map', help='map size as rowsxcols (e.g. 19x30)',
        default='19x30', type=str
    )
    parser.add_argument('--debug', help='debug mode', action='store_true')
    args = parser.parse_args()
    HOST = args.host
    PORT = args.port
    map_size = args.map
    # debug!
    if args.debug:
        logger.setLevel(logging.DEBUG)
        logger.debug('Debug mode is ON!')
    # show info that something is happening
    to_log = (HOST, PORT, map_size)
    logger.info('Starting server on %s:%d with map %s' % to_log)
    # her be game
    # TODO
    # bye bye!
    logger.info('Exiting.')
