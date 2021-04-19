import numpy as np
from PIL import Image
import io
import logging
import base64
from time import time
import cv2
import os
from projeto.utils.Logger import objLogger


def base_decoding(file_base):
    file = base64.decodestring(file_base)
    return file


def base_encoding(file_base):
    file = base64.b64encode(file_base)
    return file


def decode_image_from_base64(image_as_base64):
    """
    Realiza a decodificação da imagem em formato base64,
    e salva a imagem em uma pasta temporária para ser lida.

    :param image_as_base64: imagem codificada em base64, no formato string
    :type image_as_base64: str

    :return: array do NumPy contendo a representação da imagem
    :rtype: ndarray
    """
    # logger = logging.getLogger(__name__)

    # logger.info("decode_image_from_base64: Decodificando a imagem em formato base64")

    # if isinstance(image_as_base64, bytes):
    #     image_as_base64 = image_as_base64.decode("utf-8")

    # image_data = base64.b64decode(image_as_base64)
    data = base64.b64decode(image_as_base64)
    name = "{}.jpg".format(str(int(time() * 1000000)))
    with open("./{}".format(name), "wb") as file:
        file.write(data)

    objLogger.debug("./{}".format(name))
    image = cv2.imread("./{}".format(name))
    os.remove("./{}".format(name))    
    
    return image

def encode_base64_from_image(image_data):
    """
    Realiza a codificação da imagem para formato base64,

    :param: array do NumPy contendo a representação da imagem
    :type: ndarray

    :return image_as_base64: imagem codificada em base64, no formato string
    :rtype image_as_base64: str
    """

    if isinstance(image_data, np.ndarray):

        buff = io.BytesIO()
        img = Image.fromarray(image_data,'RGB')
        img.save(buff, format='png')
        image_as_base64 = base64.b64encode(buff.getvalue()).decode('utf-8')
    else:

        return None

    return image_as_base64
