from projeto.constants import Message


class NotTreatmentException(Exception):

    def __init__(self):
        super().__init__(Message.ERROR_NOT_TREATMENT)
