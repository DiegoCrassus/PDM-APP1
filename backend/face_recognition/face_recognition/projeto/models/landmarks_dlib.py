import sys
import dlib
import cv2
import numpy as np
import os
from imutils import face_utils
import sklearn
# import openface

predictor_model = "shape_predictor_68_face_landmarks.dat"
encoding_model = 'dlib_face_recognition_resnet_model_v1.dat'

#Creating a HOG face detector using the built-in dlib class
face_detector = dlib.get_frontal_face_detector()
face_pose_predictor = dlib.shape_predictor(predictor_model)
face_encoder = dlib.face_recognition_model_v1(encoding_model)
# face_aligner = openface.AlignDlib(predictor_model)

def whirldata_face_detectors(img, number_of_times_to_upsample=1):
    return face_detector(img, number_of_times_to_upsample)

def whirldata_face_encodings(face_image,num_jitters=1):
    face_locations = whirldata_face_detectors(face_image)
    pose_predictor = face_pose_predictor
    predictors = [pose_predictor(face_image,face_location) for face_location in face_locations]
    return face_locations,[np.array(face_encoder.compute_face_descriptor(face_image,predictor, num_jitters)) for predictor in predictors][0]

# # Foi utilizado o "get_frontal_face_detector" para gerar as bounding boxes dos rostos
#     # Este método realiza a transformação da bounding box para o padrão do opencv 
# def rect_to_bb(rects):
#     # take a bounding predicted by dlib and convert it
#     # to the format (x, y, w, h) as we would normally do
#     # with OpenCV
#     bounding_boxes = []
#     for (i, rect) in enumerate(rects):
        
#         (x, y, w, h) = face_utils.rect_to_bb(rect)
#         bounding_boxes.append((x, y, w, h))
        
#     return bounding_boxes

# Foi utilizado o "get_frontal_face_detector" para gerar as bounding boxes dos rostos
    # Este método realiza a transformação da bounding box para o padrão do opencv 
def rect_to_bb(rects):
    # take a bounding predicted by dlib and convert it
    # to the format (x, y, w, h) as we would normally do
    # with OpenCV
    bounding_boxes = []
    (x, y, w, h) = face_utils.rect_to_bb(rects)
    bounding_boxes.append((y, x+w, y+h,x))
    
#     for (i, rect) in enumerate(rects):

#         (x, y, w, h) = face_utils.rect_to_bb(rect)
#         bounding_boxes.append((x, y, w, h))

    return bounding_boxes

def paint_detected_face_on_image(frame, location, name=None):
    """
    Paint a rectangle around the face and write the name
    """
    # unpack the coordinates from the location tuple
    top, right, bottom, left = location

    if name is None:
        name = 'Unknown'
        color = (0, 0, 255)  # red for unrecognized face
    else:
        color = (0, 128, 0)  # dark green for recognized face

    # Draw a box around the face
    cv2.rectangle(frame, (left, top), (right, bottom), color, 2)

    # Draw a label with a name below the face
    cv2.rectangle(frame, (left, bottom - 35), (right, bottom), color, cv2.FILLED)
    cv2.putText(frame, name, (left + 6, bottom - 6), cv2.FONT_HERSHEY_DUPLEX, 1.0, (255, 255, 255), 1)

dir = '../../../../../Downloads/curado-test'
listdir = os.listdir(dir)

count = 0
for subdir in listdir:
    image_list = os.listdir(dir + '/' +subdir)
    for image_name in image_list:
        # win = dlib.image_window()
        image = cv2.imread(dir + '/'+ subdir + '/' + image_name)

        # use the name in the filename as the identity key


        # Run HOG face detector on the image data
        detected_faces = face_detector(image,1)
        print(dir + '/'+ subdir + '/' + image_name)
        # print("Found {} faces in the image file {}".format(len(detected_faces), file_name))
        
        for i, face_rect in enumerate(detected_faces):
            #The detected faces are returned as an object with the its coordenates
            left = face_rect.left()
            top = face_rect.top()
            right = face_rect.right()
            bottom = face_rect.bottom()
            # print(" - Face #{} found at left: {} top: {} right: {} bottom: {}".format(i,left,top,right,bottom))
            # win.add_overlay(face_rect)
            
            try:
                # alignedFace = face_aligner.align(534,image,face_rect,landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
                faces_locations,encoded_image = whirldata_face_encodings(image)
                faces_locations = rect_to_bb(faces_locations[0])
                
                # print(faces_locations)
                # print(encoded_image)
                for location, face_encoding in zip(faces_locations, encoded_image):
                    paint_detected_face_on_image(image, location, name=None)
                    cv2.imshow('Image', image)
                    cv2.waitKey(0)
                
                #  encoded_image = whirldata_face_encodings(image)
                # print(faces_locations)
                # print(encoded_image)
                # cv2.imwrite("cut/"+ subdir+"/"+"aligned_face{}.jpg".format(count), alignedFace)
            except(IndexError):    
                print("index error")

            count+=1         

        # dlib.hit_enter_to_continue()