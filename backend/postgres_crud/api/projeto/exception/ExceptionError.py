from api.arquitetura.projeto.constants import Message


class ExceptionBO(Exception):

    def __init__(self):
        print('Class exception is app')

    def __str__(self):
        return Message.ERROR_BO
