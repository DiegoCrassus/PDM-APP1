from flask_restplus import fields
from projeto.restplus import api

READ = api.parser()
READ.add_argument('_id', type=str, help='Mongo id', location='form')

CREATE = api.model('CREAD',
                       {'_id': fields.String(required=True,
                                              description="id mongo",
                                              example="5fdfee9c9cbc990007013deb")})

DELETE = api.model('DELETE',
                       {'_id': fields.String(required=True,
                                              description="id mongo",
                                              example="5fdfee9c9cbc990007013deb")})

UPDATE = api.model('UPDATE',
                       {'_id': fields.String(required=True,
                                              description="id mongo",
                                              example="5fdfee9c9cbc990007013deb")})

OUTPUT_DATA = api.model('Output',
                        {'messages': fields.Raw(required=True,
                                                   description='[Sucess or Error mensage]'),
                         'status': fields.String(required=True,
                                                 description='Status code'),
                         'data': fields.Raw(required=False,
                                            description="list of image or text in base64")})
