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
