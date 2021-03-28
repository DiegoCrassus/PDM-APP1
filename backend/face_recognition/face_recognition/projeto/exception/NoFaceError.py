from projeto.constants import Message


class FaceException(Exception):

    def __init__(self):
        print('Sem face')

    def __str__(self):
        return Message.ERROR_FACE
