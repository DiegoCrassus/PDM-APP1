import psycopg2
import settings

from projeto.constants import Message
from projeto.restplus import objLogger, objResponse


class Postgres:
    def __init__(self, host=settings.POSTGRES_HOST, database=settings.POSTGRES_DATABASE, 
                 user=settings.POSTGRES_USER, password=settings.POSTGRES_PASSWORD):
        self.host = host
        self.database = database
        self.user = user
        self.password = password

    def INSERT_NOTEBOOK(self, email, title, text):
        self.__conect_bd()

        self.connection
        self.cursor = self.connection.cursor()
        if email:

            sql = """INSERT INTO notebook(email, titulo, texto) VALUES('{}', '{}', '{}')""".format(email, title, text)
            objLogger.debug("QUERY: {}".format(sql))
            self.cursor.execute(sql)
            self.__close_bd()
            return objResponse.send_success(messages=Message.SUCESS_INSERT, data=None)
            
        else:
            self.__close_bd()
            raise ValueError

    def INSERT_USER(self, email, name):
        self.__conect_bd()
        self.connection
        self.cursor = self.connection.cursor()
        if email:
            sql = """INSERT INTO pdm_user(email, nome) VALUES('{}', '{}')""".format(email, name)
            objLogger.debug("QUERY: {}".format(sql))
            self.cursor.execute(sql)
            self.__close_bd()
            
            return objResponse.send_success(messages=Message.SUCESS_INSERT, data=None)
        else:
            self.__close_bd()
            raise ValueError

    def UPDATE(self, email, id_notebook, title, text):
        self.__conect_bd()

        self.connection
        self.cursor = self.connection.cursor()
        if email and id_notebook:

            sql = """UPDATE notebook SET (texto, titulo)=('{}', '{}') WHERE id_notebook={} AND email='{}'""".format(text, title, id_notebook, email)
            objLogger.debug("QUERY: {}".format(sql))
            self.cursor.execute(sql)
            self.__close_bd()
            return objResponse.send_success(messages=Message.SUCESS_UPDATE, data=None)
            
        else:
            self.__close_bd()
            raise ValueError

    def SELECT_NOTEBOOK(self, email, id_notebook):
        self.__conect_bd()

        self.connection
        self.cursor = self.connection.cursor()
        if not id_notebook:

            sql = """SELECT * FROM notebook WHERE email='{}'""".format(email)
            self.cursor.execute(sql)
            objLogger.debug("QUERY: {}".format(sql))
            data = self.cursor.fetchall()
            self.__close_bd()
            return objResponse.send_success(messages=Message.SUCESS_SELECT, data=data)
            
        else:

            sql = """SELECT * FROM notebook WHERE email='{}' AND id_notebook={}""".format(email, id_notebook)
            self.cursor.execute(sql)
            objLogger.debug("QUERY: {}".format(sql))
            data = self.cursor.fetchall()
            self.__close_bd()
            return objResponse.send_success(messages=Message.SUCESS_SELECT, data=data)

    def SELECT_USER(self, email):
        self.__conect_bd()

        self.connection
        self.cursor = self.connection.cursor()

        sql = """SELECT * FROM pdm_user WHERE email='{}'""".format(email)
        objLogger.debug("QUERY: {}".format(sql))
        self.cursor.execute(sql)
        data = self.cursor.fetchall()
        self.__close_bd()
        return objResponse.send_success(messages=Message.SUCESS_SELECT, data=data)

    def DELETE(self, email, id_notebook):

        self.__conect_bd()

        self.connection
        self.cursor = self.connection.cursor()

        sql = """DELETE FROM notebook WHERE id_notebook='{}'""".format(id_notebook)
        objLogger.debug("QUERY: {}".format(sql))
        self.cursor.execute(sql)
        self.__close_bd()
        return objResponse.send_success(messages=Message.SUCESS_DELETE, data=None)

    def __conect_bd(self):
        """
        Faz a conexão com o banco de dados.
        """
        objLogger.debug("INICIANDO A CONEXÃO COM O BANCO DE DADOS.")
        self.connection = psycopg2.connect(host=self.host, database=self.database, user=self.user, password=self.password)
        self.connection.autocommit = True
    
    def __close_bd(self):
        """
        Fecha a conexão com o banco de dados.
        """
        objLogger.debug("FINALZIANDO A CONEXÃO COM O BANCO DE DADOS.")
        self.connection.close()


objPostgres = Postgres()
