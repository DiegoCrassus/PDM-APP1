from bson.objectid import ObjectId
from pymongo import MongoClient

client = MongoClient('mongo', 27017)
db = client['horus']
face_recognition_collection = db['face_recognition']


def get_faces_n_names_from_db(user_id=None):
    faces_n_names = {
        "face": [],
        "name": [],
        "ids": []
    }
    query_results = face_recognition_collection.find(user_id)

    for result in query_results:
        faces_n_names["face"].append(result["image"])
        faces_n_names["name"].append(result["name"])
        faces_n_names["ids"].append(str(result["_id"]))

    return faces_n_names


def get_num_of_faces(user_id):
    """
        Função para retornar o numero de faces armazenadas no banco de dados

        :return Numero de faces no banco de dados
    """
    query = {
        "user": user_id
    }

    query_results = face_recognition_collection.find(query)

    return query_results.count()


def delete_document_from_db(document_id):
    if isinstance(document_id, list):
        for id in document_id:
            query = {
                "_id": ObjectId(id)
            }
            status = face_recognition_collection.delete_one(query)

    else:
        query = {
            "_id": ObjectId(document_id)
        }
        status = face_recognition_collection.delete_one(query)

    return status
