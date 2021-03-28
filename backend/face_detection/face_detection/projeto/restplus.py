import logging
import settings

from flask_restplus import Api
from .constants import CodeHttp, Message

log = logging.getLogger(__name__)

api = Api(version='2.0', title='Documentação - Stefanini PPA 2.0',
          description='APIs dizer o que cada uma contém')


@api.errorhandler
def default_error_handler(e):
    log.exception(Message.ERROR_NOT_TREATMENT)

    if not settings.FLASK_DEBUG:
        return {'message': Message.ERROR_NOT_TREATMENT}, CodeHttp.ERROR_500
