import settings
import logging

from flask import request
from flask_restplus import Resource, reqparse
from projeto.restplus import api
from projeto.constants import CodeHttp, Message
from projeto.utils import doc_swagger
from projeto.restplus import objLogger, objResponse
from projeto.bo.postgres import get_user, create_user
from psycopg2 import OperationalError
from psycopg2.errors import UniqueViolation

log = logging.getLogger(__name__)
ns = api.namespace(settings.ENDPOINT_USER, description='User operation.')


@ns.route(settings.ROUTE)
class GetDatabases(Resource):
    def get(self):
        """
        GET method.\n
        QUERY SELECT
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        requset_data = parser.parse_args()

        try:
            response = get_user(requset_data["email"])

        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))
        
        return response

    def post(self):
        """
        PUT method.\n
        QUERY CREATE
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        parser.add_argument('nome', type=str, required=True, help="This field 'nome' cannot be left blank")
        requset_data = parser.parse_args()

        try:
            response = create_user(requset_data["email"], 
                                    requset_data["nome"])
            
        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))

        except UniqueViolation as error:
            return objResponse.send_exception(error, str(error))

        return response
