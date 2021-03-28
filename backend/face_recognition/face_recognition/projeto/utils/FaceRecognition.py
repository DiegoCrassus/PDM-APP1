from sklearn import neighbors


class KNN(object):

    def __init__(self, num_jitters=3, n_neighbors=3, distance_threshold=0.5,
                 algorithm='ball_tree', verbose=False, weights='distance',
                 model_save_path='./face_recognition/projeto/models/weights/'):
        """
         Método construtor da classe. Alguns valores possuem valor default.

        """
        # Setando macro variaveis da classe
        self.closest_distances = None
        self.knn_model = None
        self.are_matches = None
        self.num_jitters = num_jitters
        self.n_neighbors = n_neighbors
        self.algorithm = algorithm
        self.verbose = verbose
        self.weights = weights
        self.model_save_path = model_save_path
        self.distance_threshold = distance_threshold

    def generate_KNN_model(self, X=None, y=None):
        """
        Gera o modelo de KNN treinando com os encodes e labels gerados pela função "prepare_knn_inputs_from_dir"
        Caso os os elementos de treino não tenham sido setados, é necessário passar como paramentro, caso não seja
        ..feito, retorna erro.

        """

        self.knn_model = neighbors.KNeighborsClassifier(n_neighbors=self.n_neighbors,
                                                        algorithm=self.algorithm,
                                                        weights=self.weights)
        self.knn_model.fit(X, y)

        # self.save_KNN_model(user_id)

        return self.knn_model

    def matches_images(self, closest_distances):
        """
         Verifica a distância entre os encodes das faces a serem identificadas e os encodes presentes no banco
         de dados.
         Se a distância for maior do q o valor setado em "distance_threshold" a face é considerada desconhecida e
         é apendado o valor "False" na variável "are_matches", caso contrário é apendado "True"

        """

        are_matches = []

        for distances_to_element in closest_distances[0]:
            if min(distances_to_element) <= self.distance_threshold:
                are_matches.append(True)
            else:
                are_matches.append(False)

        return are_matches

    # Verifica se a variável "are_matches" é "True" e apenda na variável "predictions" o nome da pessoa e
    # localização da face na imagem
    def predict_location(self, face_image, are_matches):
        """
        Função para gerar a predições a partir do matches gerados

        :param face_image: Imagem da face ja cropada no fomrato numpy ndarray
        :param are_matches: Variavel booleana para caso a classe seja identificada
        :return: retorna os nomes das pessoas identificadas
        """
        predictions = []

        for pred, rec in zip(self.knn_model.predict(face_image), are_matches):

            if rec:
                predictions.append((pred))
            else:
                predictions.append(("unknown"))

        return predictions

    def get_predictions(self, faces_encodings):
        """
        Gera a predição de quais individuos estão na image

        :param faces_encodings: Encondes das faces que se deseja identificar
        :return: listas das predições de cada uma das faces no formato string
        """

        # mede as distancias
        closest_distances = self.knn_model.kneighbors(faces_encodings, self.n_neighbors)

        # calcula se houve matches com os encodes presentes no treino
        are_matches = self.matches_images(closest_distances)

        # Prediz de quem é a face
        predictions = self.predict_location(faces_encodings, are_matches)
        return predictions
