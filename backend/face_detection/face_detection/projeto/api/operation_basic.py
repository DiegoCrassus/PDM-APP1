import re
import logging
import base64
from flask import request
from flask_restplus import Resource
from projeto.restplus import api
from flask import jsonify

from projeto.bo.model_detector import faces_detector
from projeto.utils.Utils import draw_faces_bounding_boxes, crop_b64_images, allowed_file, return_type_file, ALLOWED_EXTENSIONS
from projeto.utils.Base64 import decode_image_from_base64
from projeto.utils.Exception import ControllException
from projeto.constants import CodeHttp, Message

log = logging.getLogger(__name__)
ns = api.namespace('detection', description='Post operação.')
objException = ControllException()


@ns.route('/')
class PostsCollection(Resource):

    @api.response(200, 'Enviado com sucesso.')
    def post(self):
        request_data = request.get_json()
        try:
            image_base64 = request_data["image"]

            img_extension = image_base64.split(",")[0]
            img_extension = img_extension.split(",")[0].split("/")[1].split(";")[0]

            if img_extension in ALLOWED_EXTENSIONS:
                image_base64 = image_base64.split(",")[1]

                print("Info : Imagem enviada pelo client ")
            else:
                print("Imagem enviada por API")

        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)

        try:
            face_image = decode_image_from_base64(image_base64)

        except base64.binascii.Error as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_BASE64,
                                                      CodeHttp.ERROR_500)

        except KeyError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NONE_TYPE,
                                                      CodeHttp.ERROR_500)

        bounding_box = faces_detector(face_image)

        if not bounding_box:
            response = {
                "have_faces": False
            }
            return jsonify(response)

        detected_image = draw_faces_bounding_boxes(face_image, bounding_box)
        croped_faces_images = crop_b64_images(image_frame=face_image, bounding_boxes=bounding_box)

        response = {
            "have_faces": True,
            "detected_image": detected_image,
            "faces": croped_faces_images,
            "bounding_box": bounding_box}

        return jsonify(response)
