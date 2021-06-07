from projeto.constants import Message


class ConnectionError(Exception):
    def __init__(self):
        super().__init__(Message.ERROR_NOT_CONNECTED)
