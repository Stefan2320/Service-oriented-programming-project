from suds.client import Client
from datetime import datetime, timedelta
c = Client('http://localhost:8000/?wsdl')


# print(c.service.get_roles())
#print(c.service.create_user("gabi2","gabi2"))
# print(c.service.create_user("Stefan2","Stefan2"))
# print(c.service.create_user("ioana","ioana"))
print(c.service.get_users())
# print(c.service.delete_user(9))
# print(c.service.get_users())
#print(c.service.add_role(12,4))
# print(c.service.update_password(10,"branza"))
#print(c.service.get_users())
#print(c.service.add_role(10,4))
print(c.service.login("ioana","branza"))
#print(c.service.login("gabi","gabi"))
#print(c.service.login("Stefan","Stefan"))
# print(c.service.verify_role("ioana","client"))
#print(c.service.get_users())
#print(c.service.autorizare("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJzdWIiOiIxMCIsImV4cCI6MTY3NDM4NDg2MiwicGFzcyI6LTg5NjI3MTAxMzY3OTA2NDk1NTMsInJvbGUiOiIxIDIgMyA0ICIsImp0aSI6ImFhN2UwOTBlLTlhMzEtMTFlZC05MmNjLWRmMzljZDgwN2M1ZiJ9.HR_VXEYb5CiSwkbHqOrPYVw4JrSJ0D4V9L2lp_I5ogY"))
# print(c.service.logout(""))
