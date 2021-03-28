from projeto.constants import Message


class MinFacesError(Exception):

    def __init__(self):
        print('Numero de faces menor que 3')

    def __str__(self):
        return Message.ERROR_MIN_FACE
