import logging
from PIL import Image
from projeto.utils.Base64 import encode_base64_from_image

ALLOWED_EXTENSIONS = {'pdf', 'png', 'jpg', 'jpeg'}


def letterbox_image(image, size):
    """
    Função para realizar o reshape na imagem sem que perca o "aspect-ratio"

    :param image: imagem que se deseja da o resize
    :type image: numpy ndarray

    :return: new_image: Imagem após o resize
    :rtype:  new_image: numpy ndarray
    """
    logger = logging.getLogger(__name__)

    logger.info('letterbox_image: Redimensionando a imagem sem perder o aspecto da mesma')

    img_width, img_height = image.size
    w, h = size
    scale = min(w / img_width, h / img_height)
    nw = int(img_width * scale)
    nh = int(img_height * scale)

    image = image.resize((nw, nh), Image.BICUBIC)

    new_image = Image.new('RGB', size, (128, 128, 128))
    new_image.paste(image, ((w - nw) // 2, (h - nh) // 2))

    return new_image


def draw_faces_bounding_boxes(image, bounding_boxes):
    """
    Função para desenzar retangulos dado os bounding boxes dos retangulos

    :param image:
    :param image_as_64: imagem aonde se deseja desenha os bounding boxes
    :type image_as_64: numpy ndarray

    :param bounding_boxes: Bounding boxes do retangulos
    :type bounding_boxes: list

    :return: response_image: Imagem após desenhar os retangulos base64
    :rtype:  new_image: base64 em str
    """

    # image_with_rectangles = None
    # # image = decode_image_from_base64(image_as_64)
    # for bounding_box in bounding_boxes:
    #     image_with_rectangles = cv2.rectangle(image, (bounding_box[3],
    #                                                   bounding_box[0]),
    #                                           (bounding_box[1],
    #                                            bounding_box[2]),
    #                                           (0, 255, 0),
    #                                           3)

    response_image = encode_base64_from_image(image)

    return response_image


def crop_b64_images(image_frame, bounding_boxes):
    """
    Função para recortar de areas de uma imagem

    :param image_frame:
    :param image_frame_as_64: imagem aonde se deseja desenha os bounding boxes
    :type image_frame_as_64: str de base64

    :param bounding_boxes: Bounding boxes do retangulos
    :type bounding_boxes: list

    :return: croped_images_list: Imagens cropadas em base64
    :rtype:  croped_images_list: list str de base64
    """

    croped_images_list = []
    # image_frame = decode_image_from_base64(image_frame_as_64)

    for bounding_box in bounding_boxes:
        croped_image = image_frame[bounding_box[0]:bounding_box[2], bounding_box[3]:bounding_box[1]]

        croped_image_as_64 = encode_base64_from_image(croped_image)
        croped_images_list.append(croped_image_as_64)

    return croped_images_list


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def return_type_file(filename):
    for split in filename.split("."):
        if split in ALLOWED_EXTENSIONS:
            return split
    return None
