from PIL import Image
import logging


# Função para realizar o reshape na imagem sem que perca o "aspect-ratio"
def letterbox_image(image, size):
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
