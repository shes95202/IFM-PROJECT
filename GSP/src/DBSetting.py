import DBLink

mydb = DBLink.sqlconnector()
mycursor = mydb.cursor()

# mycursor.execute("DROP DATABASE GSP")
# mycursor.execute("CREATE DATABASE GSP")
mycursor.execute("CREATE TABLE Results(ID INT AUTO_INCREMENT PRIMARY KEY)")
# mycursor.execute("ALTER TABLE ")
