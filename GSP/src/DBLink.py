import pymysql

dbSettingIncludeDB = {
    "host": "hourent.asuscomm.com",
    "port": 3306,
    "user": "107AB0042",
    "password": "107-Ab0042",
    "db": "GSP",
    "charset": "utf8"
}
dbSettingNOTIncludeDB = {
    "host": "hourent.asuscomm.com",
    "port": 3306,
    "user": "107AB0042",
    "password": "95202shes",
    "charset": "utf8"
}


def sqlIncludeDBconnector():
    try:
        conn = pymysql.connect(**dbSettingIncludeDB)
        return conn
    except Exception as ex:
        print(ex)


def sqlNOTIncludeDBconnector():
    try:
        conn = pymysql.connect(**dbSettingNOTIncludeDB)
        return conn
    except Exception as ex:
        print(ex)
