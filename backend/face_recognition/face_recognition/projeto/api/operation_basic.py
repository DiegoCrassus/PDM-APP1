import re
import logging
import base64
import requests
import multiprocessing as mp
import ast
import settings
from flask import request
from flask_restplus import Resource
from projeto.restplus import api
from flask import jsonify

from projeto.entity.recognition_db import mongo_db
from projeto.bo.model_identificador import predict_face_recognition, train_knn_model
from projeto.utils.Exception import ControllException
from projeto.exception.NoFaceError import FaceException
from projeto.constants import CodeHttp, Message
from projeto.utils.Logger import objLogger

log = logging.getLogger(__name__)
ns = api.namespace('recognition', description='Post operação.')
objException = ControllException()


@ns.route('/identifier')
class PostsCollection(Resource):
    @ns.doc(params={
        "user": "id do usuário associado a base de dados",
        "faces": "Lista de rostos a serem adcionados na base de dados em base64",
        "names": "Lista dos nomes correspondente as faces com mesmo indice no formato string"})
    @api.response(200, 'Enviado com sucesso.')
    def post(self):
        request_data = request.get_json()

        try:
            image_base64 = request_data["image"]

            if re.findall("data:image/png;base64,", image_base64):
                image_base64 = image_base64.split(",")[1]

                print("Info : Imagem enviada pelo client ")
            else:
                print("Imagem enviada por API")

        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)

        try:
            payload = {"image": image_base64}

            request_data_detection = requests.post("http://{}:9001/api/detection/".format(settings.IP),
                                                   json=payload).json()

            try:
                if not request_data_detection["have_faces"]:
                    raise FaceException

            except FaceException as error:
                return objException.send_exception_simple(error,
                                                          Message.ERROR_FACE,
                                                          CodeHttp.ERROR_500)

        except base64.binascii.Error as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_BASE64,
                                                      CodeHttp.ERROR_500)

        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)
        if "messages" in request_data_detection:
            return request_data_detection

        name = request_data["user"]
        face = request_data_detection["faces"]
        bounding_box = request_data_detection["bounding_box"]

        response = predict_face_recognition(face, name, bounding_box)

        if "messages" in response:
            return response

        return response


@ns.route('/train')
class PostsCollection(Resource):
    @api.response(200, 'Enviado com sucesso.')
    def post(self):
        try:
            request_data = request.get_json()
            pool = mp.Pool()

            user_id = request_data["user"]
            faces = request_data["face"]
            name = request_data["name"]

            if isinstance(faces, str):
                faces = faces.replace("[", "")
                faces = faces.replace("]", "")
                faces = faces.split(",")

            objLogger.debug("name: {}".format(name))
            objLogger.debug("user_id: {}".format(user_id))
            objLogger.debug("num faces: {}".format(len(faces)))


        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)
        if not isinstance(faces, list):
            faces = [faces]

        

        face_pessoa = []
        for face in faces:
            face_pessoa.append(([face], name, user_id))
        
        response = pool.map(train_knn_model, face_pessoa)

        pool.close()
        pool.terminate()

        return response


@ns.route('/use')
class UseFaceRecognition(Resource):

    @ns.doc(params={
        "user": "id do usuário associado a base de dados",
        "image": "imagem que se deseja passar o face recognition"})
    def post(self):

        request_data = request.get_json()

        user_id = request_data["user_id"]
        image_as_64 = request_data["image"]

        num_faces_db = mongo_db.get_num_of_faces(user_id=user_id)
        objLogger.debug("{}".format(num_faces_db))

        # É necessário que haja ao menos 3 elementos na base de dados
        if num_faces_db < 3 :
            return "nao ha imagens suficientes na base de dados"

        payload = {"image": image_as_64}
        detection_response = requests.post("http://{}:9001/api/detection/".format(settings.IP), json=payload).json()

        if not detection_response["have_faces"] :
            return "nao foi identificados faces na imagem"

        faces = detection_response["faces"]
        bound_box = detection_response["bounding_box"]

        predictions = predict_face_recognition(faces, user_id, bound_box)
        objLogger.debug("Predict: {}".format(predictions))

        payload = {"email": user_id}
        mongo_response = requests.get("http://{}:9003/api/PDM/app".format(settings.IP), json=payload).json()
        objLogger.debug("{}".format(mongo_response))
        

        if predictions:
            response = {
                "names": predictions,
                "recognised": False,
                "have_faces": True}

            objLogger.debug("predictions: {}".format(predictions))
            for user in mongo_response["data"]:
                objLogger.debug("mongo_data: {}".format(user))
                for predict in predictions:
                    objLogger.debug("predict: {}".format(predict))
                    if user["name"] == predictions[predict]["names"][0]:
                        response["recognised"] = True


            objLogger.debug("response: {}".format(response))


        return response

