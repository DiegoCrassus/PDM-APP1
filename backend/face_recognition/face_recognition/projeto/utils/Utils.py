import numpy as np
import dlib
import requests

from projeto.utils.Base64 import decode_image_from_base64
from projeto.entity.recognition_db import face_recognition_collection
from projeto.utils.Exception import ControllException
from projeto.constants import Message, CodeHttp
objException = ControllException()


# Pesos para geração de encodings do dlib
predictor_model = "./face_recognition/projeto/models/shape_predictor_68_face_landmarks.dat"
encoding_model = "./face_recognition/projeto/models/dlib_face_recognition_resnet_model_v1.dat"

face_pose_predictor = dlib.shape_predictor(predictor_model)
face_encoder = dlib.face_recognition_model_v1(encoding_model)
face_detector = dlib.get_frontal_face_detector()


def encode_faces_images(faces, num_jitters=1):
    """
    Função para realizar o face enconding de imagens de rosto com sua respectiva label

    :param num_jitters:
    :param faces: lista de rostos no formato base64
    :return face_encondings_list: lista dos encodings dos rostos
    """

    face_encondings_list = []

    for face in faces:
        face_image = decode_image_from_base64(face)

        rect = face_detector(face_image)
        print(rect)
        try:
            predictor = face_pose_predictor(face_image, rect[0])
        except IndexError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_FACE,
                                                      CodeHttp.ERROR_500)

        face_encode = np.array(face_encoder.compute_face_descriptor(face_image, predictor, num_jitters))

        face_encondings_list.append(face_encode)

    return face_encondings_list


def bb_to_rect(boundbox):
    # x
    left = boundbox[3]

    # y
    top = boundbox[0]

    # w
    right = boundbox[1]

    # h
    botton = boundbox[2]

    rect = dlib.rectangle(left, top, right, botton)

    return rect


def save_knn_inputs(face_encondings_list, names, user_id, face_images_as_b64):
    """
    Função para salvar base de dados para realização do KNN, sendo possivel utilizalos para novos treinamentos da rede.
    Os dados são salvos em uma collection do Mongo.

    :param face_encondings_list: Lista como os face encodings gerados a partir de cada imagem
    :param names: lista com os nomes dos respectivos face encodings
    :param user_id: id do usuário que esta a treinar a rede do face recognition
    :param face_images_as_b64: lista de imagens no formato b64 correspondentes aos encodings
    :return: retorna status recebido da api do mongo
    """

    new_elements_mongo = []

    for face_encode, face_image_as_b64 in zip(face_encondings_list, face_images_as_b64):
        new_element = {
            "user": user_id,
            "face_encode": list(face_encode),
            "name": names,
            "image": face_image_as_b64
        }
        new_elements_mongo.append(new_element)

    status = face_recognition_collection.insert_many(new_elements_mongo)

    return status


def get_face_encondings_from_db(user_id):
    """
    Função para buscar no banco de dados face encondings já gerados.
    :param user_id: Id do usuário relcionado a base de dados de faces para face recognition
    :return: lista com os face encodings e lista com os respectivos nomes
    """
    face_encodings = []
    names = []

    elements = face_recognition_collection.find({"user": user_id})

    for element in elements:
        face_encodings.append(list_to_ndarray(element['face_encode']))
        names.append(element['name'])

    return face_encodings, names


def list_to_ndarray(list_):
    a = np.zeros(len(list_))
    for i_idx, i in enumerate(list_):
        a[i_idx] = i
    return a


def detect_face(face_b64):
    payload = {"image": face_b64}
    response = requests.post("http://face_detection:9000/api/detection/",
                             json=payload).json()

    return response


def get_from_key(list_of_dicts, key):
    response = []

    for dict in list_of_dicts:
        if key in dict:
            response.append(dict[key][0])

    return response
