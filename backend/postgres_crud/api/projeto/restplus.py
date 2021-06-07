import logging
from flask_restplus import Api
import settings
from projeto.constants import CodeHttp, Message
from projeto.utils.Logger import objLogger
from projeto.utils.Response import objResponse

log = logging.getLogger(__name__)


api = Api(version='1.0', title='Postgres CRUD',
          description='API que realiza a recuperação dos objetos armazenados no PostgreSQL.')


@api.errorhandler
def default_error_handler(e):
    log.exception(Message.ERROR_NOT_TREATMENT)

    if not settings.FLASK_DEBUG:
        return {'message': Message.ERROR_NOT_TREATMENT}, CodeHttp.ERROR_500
