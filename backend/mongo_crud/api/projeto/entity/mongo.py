import settings

from projeto.constants import Message
from projeto.restplus import objLogger
from pymongo import MongoClient
from bson.objectid import ObjectId
from pymongo.errors import ServerSelectionTimeoutError


class MongoDB():
    def connect(self):
        self.client = MongoClient(settings.MONGO_URI)
        self.database = self.client[settings.MONGO_DATABASE]
        self.POC_collection = self.database[settings.MONGO_POC_COLLECTION]
        objLogger.debug(Message.DEBUG_MONGO_CONNECTED)

    def close(self):
        objLogger.debug(Message.DEBUG_MONGO_CLOSED)
        self.client.close()

    def read(self, dict_find=None):
        """
        This function searches for a routine.

        params
        ------
        dict_find: dict, {'_id': ObjectId('21938712398712')}.

        rotina: str, serve routine.

        return
        ------

        result_getrotinas: JSON, search result for routine.
        """
        self.connect()
        response = self.POC_collection.find(dict_find)
        db_response = []
        for result in response:
            result["_id"] = str(result["_id"])
            db_response.append(result)

        self.close()

        return db_response

    def create(self, id):
        """
        This function registers data.

        params
        ------
        new_item_id: JSON, json of the data to be inserted in MongoDB.

        return
        ------
        """
        self.connect()
        item_id = self.POC_collection.insert_one(id).inserted_id
        objLogger.debug(Message.DEBUG_MONGO_INSERT.format(id))
        self.close()

    def delete(self, id):
        """
        This function deletes an item from mongodb by ID.

        params
        ------

        id: str.

        return
        ------
        """
        self.connect()
        delete = {"_id": ObjectId(id)}

        self.POC_collection.delete_one(delete)
        objLogger.debug(settings.DEBUG_MONGO_DELETE.format(id))
        self.close()

    def update(self, id):
        """
        This function updates mongodb data.

        params
        ------

        Rotina: str, server error routine.\n

        return
        ------
        """
        self.connect()
        self.POC_collection.update_one(id, upsert=True)
        objLogger.debug(settings.DEBUG_MONGO_UPDATE.format(id))
        self.close()

mongo_db = MongoDB()
