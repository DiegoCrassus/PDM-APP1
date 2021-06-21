from projeto.restplus import objLogger, objResponse
from projeto.constants import CodeHttp, Message
from projeto.entity.postgres_entity import objPostgres

def delete_notebook(email, id_notebook):
    return objPostgres.DELETE(email=email, id_notebook=id_notebook)

def update_notebook(email, id_notebook, title=None, text=None):
    return objPostgres.UPDATE(email=email, id_notebook=id_notebook, title=title, text=text)

def create_notebook(email, title=None, text=None):
    return objPostgres.INSERT_NOTEBOOK(email=email, title=title, text=text)

def get_notebook(email, id_notebook=None):
    return objPostgres.SELECT_NOTEBOOK(email=email, id_notebook=id_notebook)

def get_user(email):
    return objPostgres.SELECT_USER(email=email)

def create_user(email, name=None):
    return objPostgres.INSERT_USER(email=email, name=name)