import os
# Flask settings
FLASK_SERVER_NAME = None
FLASK_HOST = '0.0.0.0'
FLASK_PORT = 9000
FLASK_DEBUG = True  # Do not use debug mode in production

# Flask-Restplus settings
RESTPLUS_SWAGGER_UI_DOC_EXPANSION = 'list'
RESTPLUS_VALIDATE = True
RESTPLUS_MASK_SWAGGER = False
RESTPLUS_ERROR_404_HELP = False

MIN_FACE = 3
IP = os.environ.get("IP", "13.68.183.62") # IP AZURE
MONGO_DATABASE = os.environ.get("MONGO_DATABASE", "pdm_recognition")
MONGO_POC_COLLECTION = os.environ.get("MONGO_POC_COLLECTION", "PDM")
MONGO_ID = os.environ.get("MONGO_ID", "root")
MONGODB_PORT = int(os.environ.get("MONGODB_PORT", "27017"))
MONGO_PASSWORD = os.environ.get("MONGO_PASSWORD", "pdm123")
MONGO_HOST = os.environ.get("MONGO_HOST", "mongo")
MONGO_URI = "mongodb://{}:{}@{}:{}/".format(MONGO_ID, MONGO_PASSWORD, MONGO_HOST, MONGODB_PORT)