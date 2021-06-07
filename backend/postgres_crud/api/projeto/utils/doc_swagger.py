from flask_restplus import fields
from projeto.restplus import api

INPUT_DATA_ID = api.model('get_id',
                          {
                              'id': fields.String(required=True, description='mongo item to return',
                                                  example='5e3b0ba256d95efdacf44fdd')
                          })

INPUT_DATA_DELETE = api.model('input_delete',
                              {
                                  'id': fields.String(required=True, description='mongo item to be deleted',
                                                      example='5e3b0ba256d95efdacf44fdd')
                              })

OUTPUT_DATA_DELETE = api.model('output_delete',
                               {'mensage': fields.String(required=True, description='item delete', example='successfuly deleted item')})

INPUT_DATA_UPDATE = api.model('input_update',
                              {
                                  'id': fields.String(required=True, description='mongo item to update',
                                                      example='5e3b0ba256d95efdacf44fdd'),
                                  'binary_cnn': fields.String(required=False, description='classificação de imagens',
                                                              example='residencia'),
                                  'binary_nlp': fields.String(required=False, description='classificação de texto',
                                                              example='residencia'),
                                  'four_classes_nlp': fields.String(required=False, description='classificação de texto',
                                                                    example='agua'),
                                  'bairro': fields.String(required=False,
                                                          description='bairro do endereco extraido', example='asa sul'),
                                  'cep': fields.String(required=False, description='bairro do endereco extraido',
                                                       example='72.000-00'),
                                  'cidade_estado': fields.String(required=False, description='cidade estado extraido',
                                                                 example='São Paulo SP'),
                                  'end': fields.String(required=False, description='logradouro extraido',
                                                       example='SNC QUADRA 1 CONJUNTO 01'),
                              })

OUTPUT_DATA_UPDATE = api.model('output_update',
                               {'mensage': fields.String(required=True, description='item updated', example='successfuly updated item')})
