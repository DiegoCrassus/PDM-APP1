from flask import request
from loguru import logger


class ControllLog:

    def __init__(self):
        print('ControllLog class for log controll')

    @staticmethod
    def debug(messages):
        """
            Informações interessantes para desenvolvedores, ao tentar depurar um problema.
        """
        logger.debug("[+] - {} --- {}".format(request.remote_addr, messages))

    @staticmethod
    def info(messages):
        """
            Informações interessantes para a equipe de suporte que tenta
            descobrir o contexto de um determinado erro.
        """
        logger.info("[+] - {} --- {}".format(request.remote_addr, messages))

    @staticmethod
    def success(messages):
        logger.success("[-] - {} --- {}".format(request.remote_addr, messages))

    @staticmethod
    def warning(messages):
        """
            declarações que descrevem eventos ou estados potencialmente prejudiciais no programa.
        """
        logger.warning("[-] - {} --- {}".format(request.remote_addr, messages))

    @staticmethod
    def error(messages):
        """
            declarações que descrevem erros não fatais no aplicativo; esse nível é
            usado com bastante frequência para registrar exceções tratadas.
        """
        logger.error("[-] - {} --- {}".format(request.remote_addr, messages))

    @staticmethod
    def critical(messages):
        """
            declarações que representam as condições de erro mais graves,
            supostamente resultando no encerramento do programa.
        """
        logger.critical("[-] - {} --- {}".format(request.remote_addr, messages))


# Objeto de log
objLogger = ControllLog()
