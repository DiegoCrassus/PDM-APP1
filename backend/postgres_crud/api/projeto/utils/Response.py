from projeto.constants import Message, CodeHttp


class ControllResponse(object):

    def __init__(self):
        print('ControllResponse class for response controll')

    @staticmethod
    def send_exception(objError, messages, status=CodeHttp.ERROR_500):
        """
        Method that handles errors without passing exception real

        :param objError: Object Error ex: TypeError.
        :param messages: Message.
        :param status: Code Http.
        :return: jsonException
        """
        jsonException = {}

        if isinstance(objError, Exception):
            jsonException['messages'] = messages
            jsonException['status'] = status

        elif isinstance(objError, BaseException):
            jsonException['messages'] = messages
            jsonException['status'] = status
        else:
            jsonException['messages'] = Message.ERROR_GENERIC
            jsonException['status'] = CodeHttp.ERROR_500

        return jsonException

    @staticmethod
    def send_success(data, messages, status=CodeHttp.SUCCESS_200):
        """
        Method that handles sucess

        :param data: Response data, dado a ser enviado.
        :param messages: Message.
        :param status: Code Http.
        :return: json_sucess
        """
        json_sucess = {'data': data, 'message': [messages], 'status': status}
        return json_sucess

# Objeto para resposta
objResponse = ControllResponse()
