# python -m pip install mariadb sqlalchemy
from spyne import Application, rpc, ServiceBase, Integer, Double, String, Iterable
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication

from base.sql_base import Session
from models.role_orm import Role
from models.user_orm import User
from repositories.jws_handler import create_jws
from repositories.role_repository import get_roles, add_role
from repositories.user_repository import get_users, create_user, update_user_password, delete_user, add_user_role,show
import jwt




class BDService(ServiceBase):

    @rpc(_returns=String)
    def get_roles(ctx):
        session = Session()
        roles = session.query(Role).all()
        result = ""
        for role in roles:
            result += str(role.id) + " " + str(role.value) + "\n"
        return result

    @rpc(_returns=String)
    def get_users(self):
        response = ""
        for user in get_users():
            response += f"{user.id} - {user.username} - {user.password} - roles: "
            for role in user.roles:
                response += f"{role.value} "
            response+="\n"
        return response

    @rpc(String,String,_returns=String)
    def create_user(ctx,username,password):
        session = Session()
        roles = session.query(Role).all()
        result = ""
        user = create_user(username,password)
        if user:
            result = "User created."
        else:
            result = "Error."
        return result

    @rpc(Integer,_returns=String)
    def delete_user(self,user_id):
        response = ""
        exists = False
        #user_id = input("Introduceti id-ul utilizatorului:")
        for user in get_users():
            if user.id == user_id:
                exists = True
                delete_user(user_id)
                response = "S-a sters."
        if not exists:
            response = "Nu s-a gasit userul cu id ul dorit."
        return response

    @rpc(Integer,String, _returns=String)
    def update_password(self,user_id,new_password):
        response = ""
        exists = False
        # user_id = input("Introduceti id-ul utilizatorului:")
        # new_password = input("Introduceti parola noua:")
        for user in get_users():
            if user.id == user_id:
                exists = True
                update_user_password(user_id,new_password)
                response = "Parola schimbata!"
        if not exists:
            response = "Nu s-a gasit userul cu id ul dorit."
        return response

    @rpc(Integer,Integer,_returns=String)
    def add_role(ctx,user_id,role_id):
        session = Session()
        roles = session.query(Role).all()
        result = ""
        exists = False
        # new_role = input("Introdu id-ul rolului nou:")
        # user_id = input("Introdu id-ul utilizatorului:")
        for user in get_users():
            if user.id == int(user_id):
                add_user_role(user.id,role_id)
                exists = True
                result = "Adaugat"
        if not exists:
            response = "Nu s-a gasit userul cu id ul dorit."
        #TODO daca nu exista rolul
        return result

    @rpc(String,String,_returns=String)
    def login(self,username,password):
        response = ""
        # username = input("Introdu username-ul:")
        # password = input("Introdu parola:")
        jws = "Date gresite"
        for user in get_users():
            if username == user.username:
                if password == user.password:
                    jws = create_jws(username, password)
                else:
                    jws = "Parola gresita."
        print(jwt.decode(jws,"POS", algorithms="HS256"))
        print(jws)
        return jws

    @rpc(String, _returns=String)
    def logout(self,jwt):
        response = ""
        try:
            f = open("repositories/blacklist.txt", "a")
            f.write(jwt+"\n")
            response = "True"
            f.close()
        except:
            response = "False"
        return response



    @rpc(String,_returns = String)
    def autorizare(self,jwt1):
        rezult = ""
        f = open("repositories/blacklist.txt","r")
        content = f.readlines()
        for jwts in content:
            if jwt1 == jwts[:-1]:
                return "Eroare, invalid JWT, token is in blacklist!"
        try:
            jwt.decode(jwt1,"POS", algorithms="HS256")
        except:
            return "Eroare, Token has invalid signature!"

        decodat = jwt.decode(jwt1,"POS", algorithms="HS256")
        print(decodat)
        user_id = decodat["sub"]
        roles = decodat["role"]
        role = roles.split(" ")
        role = role[:-1]
        role =  [eval(i) for i in role]

        exista_userID = False
        for user in get_users():
            if int(user_id) == user.id:
                exista_userID = True
                roles = user.roles

        nr_roles = len(role)
        for rol in roles:
            for ro in role:
                if rol.id == ro:
                    nr_roles = nr_roles - 1

        print(exista_userID)
        print(role)

        if nr_roles != 0 or exista_userID == False:
            f = open("repositories/blacklist.txt", "a")
            f.write(jwt1 + "\n")
            f.close()
            if( exista_userID == False):
                return "Eroare, inavlid user id!"
            else:
                return "Eroare, bad roles my friend!"


        response = str(user_id) + "|"
        response = response + ' '.join(str(element) for element in role)
        return response



    @rpc(String,String,_returns=String)
    def verify_role(ctx,username,role_name):
        session = Session()
        roles = session.query(Role).all()
        result = ""
        #user_name = input("Introdu username-ul:")
        # role_cautat = input("Introdu rolul cautat:")
        for user in get_users():
            if username == user.username:
                for role in user.roles:
                    if(role.value == role_name):
                        result = "Are rolul "+role_name
                    else:
                        result = "Nu are rolul "+ role_name
        return result


#TODO parametru suplimentar JWT la creare,listare, etc...



application = Application([BDService], 'services.bd.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

wsgi_application = WsgiApplication(application)

import logging
from wsgiref.simple_server import make_server

if __name__ == "__main__":
    # print("Creating user:")
    # new_user = create_user("test", "test")
    # print(new_user)

    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)
    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()

    # print("\nUsers:")
    # for user in get_users():
    #     print(f"{user.id} - {user.username} - {user.password} - roles: ", end="")
    #     for role in user.roles:
    #         print(f"{role.value} ", end="")
    #     print()




    # print("\nActualizare parola.")
    # user_id = input("Introduceti id-ul utilizatorului:")
    # new_password = input("Introduceti parola noua:")
    # for user in get_users():
    #     if user.id == user.id:
    #         update_user_password(user.id,new_password)

    #add_role()
    # print("\n\nRoles:")
    # for role in get_roles():
    #     print(f"{role.value}")

    # print("\nSterge user.")
    # user_id = input("Introduceti id-ul utilizatorului:")
    # for user in get_users():
    #     if user.id == user.id:
    #         delete_user(user.id)

    # print("Adauga roluri:")
    # new_role = input("Introdu id-ul rolului nou:")
    # user_id = input("Introdu id-ul utilizatorului:")
    # for user in get_users():
    #     if user.id == int(user_id):
    #         add_user_role(user.id,new_role)

    # print("Afisare detalii despre un singur utilizator.")
    # user_id = input("Introduceti id-ul utilizatorului:")
    # for user in get_users():
    #     if user.id == int(user_id):
    #         show(user.id)
    # De luat si rolul/rolurile

    # print("Log in:")
    # user_name =  input("Introdu username-ul:")
    # user_pass = input("Introdu parola:")
    # for user in get_users():
    #     if user_name == user.username:
    #         if user_pass == user.password:
    #             print("Logat cu succes!")
    #         else:
    #             print("Parola gresita.")

    # print("Verificare rol la user")
    # user_name =  input("Introdu username-ul:")
    # role_cautat = input("Introdu rolul cautat:")
    # for user in get_users():
    #     if user_name == user.username:
    #         for role in user.roles:
    #             if(role.value == role_cautat):
    #                 print("Are rolul "+role_cautat)
    #             else:
    #                 print("Nu are rolul "+ role_cautat)


