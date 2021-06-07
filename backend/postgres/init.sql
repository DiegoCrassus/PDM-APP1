CREATE TABLE pdm_user(
 		email VARCHAR(120) NOT NULL,
		nome VARCHAR(150),
 		PRIMARY KEY(email)
);

CREATE TABLE notebook(
	id_notebook SERIAL,
 	titulo VARCHAR(100),
 	texto TEXT,
	email VARCHAR(120) NOT NULL,
 	FOREIGN KEY(email) REFERENCES pdm_user(email)
);