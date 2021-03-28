from projeto.constants import Message, CodeHttp


class ControllException(object):

    def __init__(self):
        print('ControllException class for exception controll')

    def send_exception_simple(self, objError, messages, status=500):
        '''
        Method that handles errors without passing exception real
        :param objError:
        :param messages:
        :param status:
        :return:
        '''
        jsonException = {}

        if isinstance(objError, Exception):
            jsonException['messages'] = messages
            jsonException['status'] = status
        elif isinstance(objError, BaseException):
            jsonException['messages'] = messages
            jsonException['status'] = status
        else:
            jsonException['messages'] = Message.ERROR_GENERIC
            jsonException['status'] = 500

        return jsonException

    def send_exception_complex(self, objError, messages, status=500, error=None):
        '''
        Method that handles errors passing exception real
        :param objError:
        :param messages:
        :param status:
        :param error:
        :return:
        '''
        jsonException = {}

        if isinstance(objError, Exception):
            jsonException['messages'] = messages
            jsonException['status'] = status
            jsonException['error'] = error
        elif isinstance(objError, BaseException):
            jsonException['messages'] = messages
            jsonException['status'] = status
            jsonException['error'] = error
        else:
            jsonException['messages'] = Message.ERROR_GENERIC
            jsonException['status'] = 500
            jsonException['error'] = Message.ERROR_EXCEPTION

        return jsonException
