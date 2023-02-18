from models.user_orm import User
from base.sql_base import Session
from models.users_roles_orm import user_roles_relationship


def get_users():
    session = Session()
    users = session.query(User).all()
    return users

def get_user_by_username(username):
    session = Session()
    users = session.query(User).all()
    for user in users:
        if user.username == username:
            return user.id
    return -1

def update_user_password(user_id,new_password):
    session = Session()
    user = session.query(User).get(user_id)
    user.password = new_password
    session.commit()
    print(user.password)

def delete_user(user_id):
    session = Session()
    user = session.query(User).get(user_id)
    try:
        session.delete(user)
        session.commit()
    except Exception as exc:
        print(f"Failed to delete user - {exc}")



def create_user(username, password):
    session = Session()
    user = User(username, password)
    try:
        session.add(user)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    return user


def show(user_id):
    session = Session()
    print(user_id)
    user = session.query(User).get(user_id)
    roluri = "Rol/Roluri: "
    for role in user.roles:
        roluri += str(role.value) + " "
    if(roluri == "Rol/Roluri: "):
        roluri = "Nu are rol!"

    data = "Nume: "+ user.username + " "+roluri
    print(data)

def add_user_role(userId,roleId):
    session = Session()
    role = user_roles_relationship.insert().values(user_id=int(userId),role_id=int(roleId))
    try:
        session.execute(role)
        session.commit()
    except Exception as exc:
        print(f"Failed to add role to user - {exc}")
