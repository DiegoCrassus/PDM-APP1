import settings
import logging

from flask import request
from flask_restplus import Resource, reqparse
from projeto.restplus import api
from projeto.constants import CodeHttp, Message
from projeto.utils import doc_swagger
from projeto.restplus import objLogger, objResponse
from projeto.bo.postgres import create_notebook, get_notebook, update_notebook, delete_notebook
from psycopg2 import OperationalError

log = logging.getLogger(__name__)
ns = api.namespace(settings.ENDPOINT_NOTEBOOK, description='Notebook operation.')


@ns.route(settings.ROUTE)
class GetDatabases(Resource):
    def get(self):
        """
        GET method.\n
        QUERY SELECT
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        parser.add_argument('id_notebook', type=str, required=False, help="This field 'id_notebook' cannot be left blank")
        requset_data = parser.parse_args()

        try:
            response = get_notebook(requset_data["email"], 
                                    requset_data["id_notebook"])

        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))

        return response

    def post(self):
        """
        POST method.\n
        QUERY INSERT
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        parser.add_argument('titulo', type=str, required=False, help="This field 'title' cannot be left blank")
        requset_data = parser.parse_args()
        request_data_body = request.request.get_json()

        try:

            response = create_notebook(requset_data["email"], 
                                       request_data_body["texto"],
                                       requset_data["titulo"])

        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))

        return response

    def delete(self):
        """
        DELETE method.\n
        QUERY DELETE
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        parser.add_argument('id_notebook', type=str, required=True, help="This field 'id_notebook' cannot be left blank")
        requset_data = parser.parse_args()

        try:
            response = delete_notebook(requset_data["email"], 
                                       requset_data["id_notebook"])

        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))

        return response

    def put(self):
        """
        PUT method.\n
        QUERY UPDATE
        """
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help="This field 'email' cannot be left blank")
        parser.add_argument('id_notebook', type=str, required=True, help="This field 'id_notebook' cannot be left blank")
        parser.add_argument('titulo', type=str, required=False, help="This field 'title' cannot be left blank")
        requset_data = parser.parse_args()
        request_data_body = request.request.get_json()

        try:
            response = update_notebook(requset_data["email"], 
                                       requset_data["id_notebook"],
                                       requset_data["titulo"],
                                       request_data_body["texto"])

        except ConnectionError as error:
            return objResponse.send_exception(error, Message.ERROR_NOT_CONNECTED)

        except OperationalError as error:
            return objResponse.send_exception(error, str(error))

        return response
