import logging.config

import os
import settings

from loguru import logger
from flask import Flask, Blueprint
from flask_cors import CORS
from projeto.api.operation_basic import ns as operation_basic
from projeto.restplus import api

app = Flask(__name__)
logging_conf_path = os.path.normpath(os.path.join(os.path.dirname(__file__), '../logging.conf'))
logging.config.fileConfig(logging_conf_path)
log = logging.getLogger(__name__)
logger.add(settings.PATH_LOG, rotation="1 week")


def configure_app(flask_app):
    pass
    #flask_app.config['SERVER_NAME'] = settings.FLASK_SERVER_NAME
    #flask_app.config['SWAGGER_UI_DOC_EXPANSION'] = settings.RESTPLUS_SWAGGER_UI_DOC_EXPANSION
    #flask_app.config['RESTPLUS_VALIDATE'] = settings.RESTPLUS_VALIDATE
    #flask_app.config['RESTPLUS_MASK_SWAGGER'] = settings.RESTPLUS_MASK_SWAGGER
    #flask_app.config['ERROR_404_HELP'] = settings.RESTPLUS_ERROR_404_HELP


def initialize_app(flask_app):
    configure_app(flask_app)
    CORS(flask_app)

    blueprint = Blueprint('api', __name__, url_prefix="/api")
    api.init_app(blueprint)
    api.add_namespace(operation_basic)
    flask_app.register_blueprint(blueprint)


def main():
    #initialize_app(app)
    #logger.debug("[+] --- starting at: {}:{}{}".format(settings.FLASK_HOST, settings.FLASK_PORT, settings.FLASK_URL_FIX))
    #app.run(host=settings.FLASK_HOST, port=settings.FLASK_PORT, debug=settings.FLASK_DEBUG)
    CORS(app)
    blueprint = Blueprint('api', __name__, url_prefix=settings.FLASK_URL_FIX)
    api.init_app(blueprint)
    api.add_namespace(operation_basic)
    app.register_blueprint(blueprint)
    app.run(host='0.0.0.0', port=9000)


if __name__ == "__main__":
    main()
