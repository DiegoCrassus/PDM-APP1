print('Start #################################################################');

db = db.getSiblingDB('pdm_recognition');
db.createUser(
  {
    user: 'pdm',
    pwd: 'pdm123',
    roles: [{ role: 'readWrite', db: 'mongo' }],
  },
);
db.createCollection('PDM');


print('END #################################################################');
