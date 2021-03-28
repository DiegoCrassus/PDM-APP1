import dlib
from imutils import face_utils


# Transforma os bounding boxes da dlib para o formato (x, y, w, h) usado pelo opencv
def rect_to_bb(rects):
    (x, y, w, h) = face_utils.rect_to_bb(rects)
    bounding_boxes = (y, x + w, y + h, x)

    return bounding_boxes


def faces_detector(image_data):
    # converte imagem em base64 para numpy ndarray
    # image_data = decode_image_from_base64(image_as_base64)
    face_detector = dlib.get_frontal_face_detector()
    faces_rects = face_detector(image_data)

    faces_boundings_boxes = []

    for face_rect in faces_rects:
        print(face_rect)
        face_bb = rect_to_bb(face_rect)
        faces_boundings_boxes.append(face_bb)

    return faces_boundings_boxes
