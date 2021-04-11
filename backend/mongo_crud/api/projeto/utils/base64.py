import io
import base64
import numpy as np
from PIL import Image

from projeto.constants import Message
from projeto.restplus import objLogger


def base64_decoder(value):
    file = base64.b64decode(value.encode("utf-8"))
    objLogger.debug(messages=Message.DEBUG_B64_DECODE)
    return file


def base_encoding(value):
    buff = io.BytesIO()
    img = Image.fromarray(value, 'RGB')

    img.save(buff, format='png')

    file = base64.b64encode(buff.getvalue()).decode("utf-8")
    objLogger.debug(messages=Message.DEBUG_B64_ENCODE)
    return file


def buffering_image(image_as_base64):
    img = np.array(Image.open(io.BytesIO(image_as_base64)).convert('RGB'))

    return img
