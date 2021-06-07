from projeto.restplus import objLogger, objResponse
from projeto.constants import CodeHttp, Message
from projeto.entity.postgres_entity import objPostgres

def delete_notebook(email, id_notebook):
    return objPostgres.DELETE(email, id_notebook)

def update_notebook(email, id_notebook, title=None, text=None):
    return objPostgres.UPDATE(email, id_notebook, title, text)

def create_notebook(email, title=None, text=None):
    return objPostgres.INSERT_NOTEBOOK(email, title, text)

def get_notebook(email, id_notebook=None):
    return objPostgres.SELECT_NOTEBOOK(email, id_notebook)

def get_user(email):
    return objPostgres.SELECT_USER(email)

def create_user(email, name=None):
    return objPostgres.INSERT_USER(email, name)