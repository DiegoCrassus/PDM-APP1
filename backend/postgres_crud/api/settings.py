import os

"""
    Variaveis booleanas e objetos nao poderao ser definidas nas variaveis de ambiente,
    pois todas serao convertidas para string.
    Para as variaveis definidas com "os.environ.get()" o primeiro valor é referente
    a variavel que está buscando, o segundo valor será usado como valor padrão caso
    não encontre nas variaveis de ambiente.
"""

# Project
VERSION = os.environ.get("VERSION", 'v1.0')

# Flask settings
FLASK_SERVER_NAME = None
FLASK_HOST = os.environ.get('FLASK_HOST', "0.0.0.0")
FLASK_PORT = os.environ.get('FLASK_PORT', "9000")
FLASK_DEBUG = True  # Do not use debug mode in production

# Flask-Restplus settings
RESTPLUS_SWAGGER_UI_DOC_EXPANSION = 'list'
RESTPLUS_VALIDATE = True
RESTPLUS_MASK_SWAGGER = False
RESTPLUS_ERROR_404_HELP = False

# database settings
POSTGRES_HOST = os.environ.get('POSTGRES_HOST', '192.168.101.5')
POSTGRES_USER = os.environ.get('POSTGRES_USER', 'postgres')
POSTGRES_PASSWORD = os.environ.get('POSTGRES_PASSWORD', 'pdm@123')
POSTGRES_DATABASE = os.environ.get('POSTGRES_DATABASE', 'postgres')


ENDPOINT_USER = os.environ.get("ENDPOINTS", "user")
ENDPOINT_NOTEBOOK = os.environ.get("ENDPOINTS", "notebook")
URL_PREFIX = os.environ.get("URL_PREFIX", "/api")

# Routes
ROUTE = os.environ.get("ROUTE", "/")

PATH_LOG = os.environ.get("PATH_LOG", "./log_postgres")
