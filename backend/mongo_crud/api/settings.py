import os

"""
    Variaveis booleanas e objetos nao poderao ser definidas nas variaveis de ambiente,
    pois todas serao convertidas para string.
    Para as variaveis definidas com "os.environ.get()" o primeiro valor é referente
    a variavel que está buscando, o segundo valor será usado como valor padrão caso
    não encontre nas variaveis de ambiente.
"""

# flask settings
FLASK_SERVER_NAME = None
RESTPLUS_VALIDATE = True
RESTPLUS_MASK_SWAGGER = False
RESTPLUS_SWAGGER_UI_DOC_EXPANSION = list
RESTPLUS_ERROR_404_HELP = False

# Env_vars
FLASK_DEBUG = int(os.environ.get("FLASK_DEBUG", "1"))

# Do not use debug mode in production
if FLASK_DEBUG == 1:
    FLASK_DEBUG == True
else:
    FLASK_DEBUG == False

FLASK_HOST = os.environ.get('FLASK_HOST', "0.0.0.0")
FLASK_PORT = os.environ.get('FLASK_PORT', "9000")

FLASK_URL_FIX = os.environ.get("FLASK_URL_FIX", "/api")
ROUTE = os.environ.get("ROUTE", "/POC")
ENDPOINTS = os.environ.get("ENDPOINTS", "pdm")
PATH_LOG = os.environ.get("PATH_LOG", "./log_mongo_crud")

MONGO_DATABASE = os.environ.get("MONGO_DATABASE", "pdm_recognition")
MONGO_POC_COLLECTION = os.environ.get("MONGO_POC_COLLECTION", "PDM")
MONGO_ID = os.environ.get("MONGO_ID", "root")
MONGODB_PORT = int(os.environ.get("MONGODB_PORT", "27017"))
MONGO_PASSWORD = os.environ.get("MONGO_PASSWORD", "pdm123")
MONGO_HOST = os.environ.get("MONGO_HOST", "mongo")
MONGO_URI = "mongodb://{}:{}@{}:{}/".format(MONGO_ID, MONGO_PASSWORD, MONGO_HOST, MONGODB_PORT)
