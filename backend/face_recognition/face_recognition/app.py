import logging.config

import os
from flask import Flask, Blueprint
import settings
from projeto.api.operation_basic import ns as operation_basic
from projeto.restplus import api
from flask_cors import CORS

app = Flask(__name__)
logging_conf_path = os.path.normpath(os.path.join(os.path.dirname(__file__),
                                                  '../logging.conf'))
logging.config.fileConfig(logging_conf_path)
log = logging.getLogger(__name__)


def configure_app(flask_app):
    flask_app.config['SERVER_NAME'] = settings.FLASK_SERVER_NAME
    flask_app.config['SWAGGER_UI_DOC_EXPANSION'] = settings.RESTPLUS_SWAGGER_UI_DOC_EXPANSION
    flask_app.config['RESTPLUS_VALIDATE'] = settings.RESTPLUS_VALIDATE
    flask_app.config['RESTPLUS_MASK_SWAGGER'] = settings.RESTPLUS_MASK_SWAGGER
    flask_app.config['ERROR_404_HELP'] = settings.RESTPLUS_ERROR_404_HELP


def initialize_app(flask_app):
    configure_app(flask_app)
    CORS(flask_app)

    blueprint = Blueprint('api', __name__, url_prefix='/api')
    api.init_app(blueprint)
    api.add_namespace(operation_basic)
    flask_app.register_blueprint(blueprint)


def main():
    initialize_app(app)
    app.run(debug=settings.FLASK_DEBUG, host=settings.FLASK_HOST, port=settings.FLASK_PORT)


if __name__ == "__main__":
    main()
