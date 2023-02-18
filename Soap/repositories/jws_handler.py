import uuid
from datetime import datetime, timedelta

import jwt
import url64
from repositories.role_repository import user_get_roles
from repositories.user_repository import get_user_by_username
from base64 import urlsafe_b64encode, urlsafe_b64decode


#TODO de pus expirare -> verificat in autorizare daca e expirat sau nu
def create_jws(username,password):
    key = "POS"
    user_id = get_user_by_username(username)
    roles = user_get_roles(username)

    id = uuid.uuid1();
    time = datetime.now();
    time = time + timedelta(minutes=30)
    exp_date = time

    print(exp_date)
    encoded = jwt.encode({"iss": "http://127.0.0.1:8000","sub":str(user_id),"exp":exp_date,"pass":hash(password),"role":roles,"jti":str(id)}, key, algorithm="HS256",headers={"alg": "HS256"})
    # payload = url64.encode({"iss": "http://127.0.0.1:8000","sub":str(user_id),"role":roles,"jti":str(id)})
    # header = url64.encode({"alg": "HS256"})
    # print(payload)
    jwt.decode(encoded, key, algorithms="HS256")
    print(jwt.decode(encoded, key, algorithms="HS256"))
    return encoded