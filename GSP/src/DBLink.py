import pymysql

db_settings = {
    "host": "hourent.asuscomm.com",
    "port": 3306,
    "user": "107AB0042",
    "password": "95202shes",
    "db": "GSP",
    "charset": "utf8"
}

try:
    conn = pymysql.connect(**db_settings)
    print("Connection is estabilished!")
except Exception as ex:
    print(ex)
