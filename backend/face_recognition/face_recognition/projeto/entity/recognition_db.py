from bson.objectid import ObjectId
from pymongo import MongoClient
from projeto.utils.Logger import objLogger
import settings


class MongoDB():
    def connect(self):
        self.client = MongoClient(settings.MONGO_URI)
        self.database = self.client[settings.MONGO_DATABASE]
        self.POC_collection = self.database[settings.MONGO_POC_COLLECTION]
        objLogger.debug("Opened connection!")

    def close(self):
        self.client.close()
        objLogger.debug("Closed connection!")

    def get_faces_n_names_from_db(self, user_id=None):
        self.connect()
        faces_n_names = {
            "face": [],
            "name": [],
            "ids": []
        }
        query_results = self.POC_collection.find(user_id)

        for result in query_results:
            faces_n_names["face"].append(result["image"])
            faces_n_names["name"].append(result["name"])
            faces_n_names["ids"].append(str(result["_id"]))

        self.close()
        return faces_n_names

    def get_num_of_faces(self, user_id):
        """
            Função para retornar o numero de faces armazenadas no banco de dados

            :return Numero de faces no banco de dados
        """
        objLogger.debug("User_id: {}".format(user_id))
        self.connect()
        query = {
            "user": user_id
        }
        objLogger.debug("Query: {}".format(query))
        query_results = self.POC_collection.find(query)
        self.close()

        return query_results.count()


mongo_db = MongoDB()
