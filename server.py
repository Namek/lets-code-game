import argparse
import logging

from gevent.monkey import patch_all

from LCG_server import LCG
from LCG_server.base import logger, set_logger, ServerError


if __name__ == '__main__':
    # gevent!
    patch_all()
    # logging!
    set_logger()
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
    try:
        rows, cols = args.map.split('x')
    except ValueError:
        raise ServerError('Invalid map size')
    # debug!
    if args.debug:
        logger.setLevel(logging.DEBUG)
        logger.debug('Debug mode is ON!')
    # show info that something is happening
    to_log = (HOST, PORT, args.map)
    logger.info('Starting server on %s:%d with map %s' % to_log)
    # here be game
    lcg = LCG(HOST, PORT, (rows, cols))
    try:
        lcg.serve()
    except KeyboardInterrupt:  # expected
        pass
    # bye bye!
    logger.info('Exiting.')
