import logging
import settings

from flask import request
from flask_restplus import Resource
from projeto.restplus import api
from projeto.constants import CodeHttp, Message
from projeto.utils import doc_swagger
from projeto.restplus import objLogger, objResponse

from bson.objectid import ObjectId
from projeto.entity.mongo import mongo_db
from flask_restplus import Resource, Api, fields
from projeto.utils import doc_swagger
from pymongo.errors import ServerSelectionTimeoutError

log = logging.getLogger(__name__)
ns = api.namespace(settings.ENDPOINTS, description='CRUD mongo operation.')


@ns.route(settings.ROUTE)
class PostsCollection(Resource):

    @api.response(200, 'Enviado com sucesso.')
    @api.marshal_with(doc_swagger.OUTPUT_DATA)
    def get(self):
        """
        Método de READ
        """
        objLogger.debug(Message.REQUEST)
        request_data = request.headers
        objLogger.debug(request_data)

        try:
            if "email" in request_data:
                data = mongo_db.read({"user": request_data["email"]})

            else:
                data = mongo_db.read()

        except KeyError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_KEY_REQUEST, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_KEY_REQUEST)

        except ServerSelectionTimeoutError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_TIMEOUT, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_TIMEOUT)

        except TypeError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_REQUEST_DATA, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_REQUEST_DATA)

        else:
            response = objResponse.send_success(messages=Message.SUCESS_EXEMPLO, status=CodeHttp.SUCCESS_200, data=data)
            objLogger.success(messages=Message.SUCESS_EXEMPLO)

        return response

    @api.response(200, 'Enviado com sucesso.')
    @api.expect(doc_swagger.CREATE)
    @api.marshal_with(doc_swagger.OUTPUT_DATA)
    def post(self):
        """
        Método de CREATE
        """
        objLogger.debug(Message.REQUEST)
        request_data = request.get_json()

        try:
            teste = request_data["file"]

        except KeyError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_KEY_REQUEST, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_KEY_REQUEST)

        except TypeError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_REQUEST_DATA, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_REQUEST_DATA)

        else:
            response = objResponse.send_success(messages=Message.SUCESS_EXEMPLO, status=CodeHttp.SUCCESS_200, data=teste)
            objLogger.success(messages=Message.SUCESS_EXEMPLO)

        return response

    @api.response(200, 'Enviado com sucesso.')
    @api.expect(doc_swagger.UPDATE)
    @api.marshal_with(doc_swagger.OUTPUT_DATA)
    def put(sefl):
        """
        Método de UPDATE
        """
        objLogger.debug(Message.REQUEST)
        request_data = request.get_json()

        try:
            teste = request_data["file"]

        except KeyError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_KEY_REQUEST, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_KEY_REQUEST)

        except TypeError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_REQUEST_DATA, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_REQUEST_DATA)

        else:
            response = objResponse.send_success(messages=Message.SUCESS_EXEMPLO, status=CodeHttp.SUCCESS_200, data=teste)
            objLogger.success(messages=Message.SUCESS_EXEMPLO)

        return response

    @api.response(200, 'Enviado com sucesso.')
    @api.expect(doc_swagger.DELETE)
    @api.marshal_with(doc_swagger.OUTPUT_DATA)
    def delete(self):
        """
        Método de DELETE
        """
        objLogger.debug(Message.REQUEST)
        request_data = request.get_json()

        try:
            teste = request_data["file"]

        except KeyError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_KEY_REQUEST, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_KEY_REQUEST)

        except TypeError as error:
            response = objResponse.send_exception(objError=error, messages=Message.ERROR_NO_REQUEST_DATA, status=CodeHttp.ERROR_500)
            objLogger.error(messages=Message.ERROR_NO_REQUEST_DATA)

        else:
            response = objResponse.send_success(messages=Message.SUCESS_EXEMPLO, status=CodeHttp.SUCCESS_200, data=teste)
            objLogger.success(messages=Message.SUCESS_EXEMPLO)

        return response
