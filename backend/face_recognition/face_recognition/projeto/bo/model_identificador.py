import numpy as np
import settings
from projeto.utils.FaceRecognition import KNN
from projeto.utils.Utils import encode_faces_images, save_knn_inputs, get_face_encondings_from_db
from projeto.utils.Exception import ControllException
from projeto.constants import CodeHttp, Message
from projeto.exception.NoUserError import NoUserException
from projeto.exception.MinFacesException import MinFacesError


objException = ControllException()


def predict_face_recognition(faces_images_as_b64, user_id, boundbox):
    face_encodings_list = encode_faces_images(faces_images_as_b64)

    response_from_db = get_face_encondings_from_db(user_id)
    if not response_from_db[0] and not response_from_db[1]:
        try:
            raise NoUserException
        except NoUserException as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_NO_USER,
                                                      CodeHttp.ERROR_500)
    face_encode_from_db, name = response_from_db

    if len(face_encode_from_db) < settings.MIN_FACE and len(name) < settings.MIN_FACE:
        try:
            raise MinFacesError
        except MinFacesError as error:
            return objException.send_exception_simple(error,
                                                      Message.ERROR_MIN_FACE,
                                                      CodeHttp.ERROR_500)

    knn_model = KNN()
    knn_model.generate_KNN_model(face_encode_from_db, name)
    response = {}

    for idx, face in enumerate(face_encodings_list):
        face = np.expand_dims(face, 0)
        predictions = knn_model.get_predictions(face)

        response[idx] = {"names": predictions}

    return response


def train_knn_model(face_pessoa):
    """
    Função para treinar o modelo de KNN

    :param face_pessoa: tupla, with faces, names, user_id. (faces, names, user_id)
    """
    faces, names, user_id = face_pessoa
    face_encodings_list = encode_faces_images(faces)

    if "messages" in face_encodings_list:
        return face_encodings_list

    # Salva no db somente caso haja novos elementos
    if len(faces) != 0:
        _ = save_knn_inputs(face_encodings_list, names, user_id, faces)

    knn_model = KNN()

    face_encondings_list, names = get_face_encondings_from_db(user_id)
    knn_model.generate_KNN_model(face_encondings_list, names)

    response = {"messages": Message.SUCESSO_NEW_USER, "status": CodeHttp.SUCCESS_200}
    return response

