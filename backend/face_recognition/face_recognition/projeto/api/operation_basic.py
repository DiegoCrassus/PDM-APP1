import re
import logging
import base64
import requests
import multiprocessing as mp
from flask import request
from flask_restplus import Resource
from projeto.restplus import api
from flask import jsonify

from projeto.entity.recognition_db import mongo_db
from projeto.bo.model_identificador import predict_face_recognition, train_knn_model
from projeto.utils.Exception import ControllException
from projeto.exception.NoFaceError import FaceException
from projeto.constants import CodeHttp, Message

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

            request_data_detection = requests.post("http://face_detection:9000/api/detection/",
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

        return jsonify(response)


@ns.route('/train')
class PostsCollection(Resource):

    @api.response(200, 'Enviado com sucesso.')
    @ns.doc(params={
        "user": "id do usuário associado a base de dados",
        "faces": "Lista de rostos a serem adcionados na base de dados em base64",
        "name": "Nome correspondente as faces com mesmo indice no formato string"})
    def post(self):
        try:
            request_data = request.get_json()[0]
            pool = mp.Pool()

            user_id = request_data["user"]
            faces = request_data["face"]
            names = request_data["name"]

        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)
        if not isinstance(faces, list):
            faces = [faces]

        

        face_pessoa = []
        for face in faces:
            face_pessoa.append((face, name, user_id))
        
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

        # É necessário que haja ao menos 3 elementos na base de dados
        if mongo_db.get_num_of_faces(user_id) < 3 :
            return "nao ha imagens suficientes na base de dados"

        if re.findall("data:image/jpeg;base64,", image_as_64):
            image_as_64 = image_as_64.split(",")[1]
            print("Info : Imagem enviada pelo client ")
        else:
            print("Imagem enviada por API")

        detection_response = requests.post("http://face_detection:9001/api/detection/", json={"image": image_as_64})

        data = detection_response.json()

        if not data["have_faces"] :
            return "nao foi identificados faces na imagem"

        faces = data["faces"]

        predictions = predict_face_recognition(faces, user_id)

        if predictions:
            response = {
                "names": predictions,
                "faces": faces,
                "recognised": True,
                "have_faces": True
            }

        return response

