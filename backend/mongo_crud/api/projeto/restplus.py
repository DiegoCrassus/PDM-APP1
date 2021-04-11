import logging

from flask_restplus import Api
import settings
from projeto.constants import CodeHttp, Message
from projeto.utils.Logger import objLogger
from projeto.utils.Response import objResponse
from projeto.exception.NotTreatementError import NotTreatmentException

log = logging.getLogger(__name__)

api = Api(version='1.0', title='Documentação - PPA',
          description='Documentação swagger de exemplo')


@api.errorhandler
def default_error_handler(e):
    objLogger.error(Message.ERROR_NOT_TREATMENT)

    if not settings.FLASK_DEBUG:
        return objResponse.send_exception(NotTreatmentException, Message.ERROR_NOT_TREATMENT, CodeHttp.ERROR_500)
