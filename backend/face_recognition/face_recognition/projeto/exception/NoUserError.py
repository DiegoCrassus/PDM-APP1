from projeto.constants import Message


class NoUserException(Exception):

    def __init__(self):
        print('CPF nao encotrado')

    def __str__(self):
        return Message.ERROR_NO_USER
