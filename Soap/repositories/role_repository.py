from models.role_orm import Role
from base.sql_base import Session
from repositories.user_repository import get_users


def get_roles():
    session = Session()
    roles = session.query(Role).all()
    return roles

def user_get_roles(username):
    rezult = ""
    for user in get_users():
        if username == user.username:
            for role in user.roles:
                rezult += str(role.id) + " "
    return rezult
def add_role():
    session = Session()
    # role = Role("admin")
    role1 = Role("content_manager")
    role2 = Role("artist")
    role3 = Role("client")

    try:
        session.add(role3)
        session.commit()
    except Exception as exc:
        print(f"Failed to add role - {exc}")
