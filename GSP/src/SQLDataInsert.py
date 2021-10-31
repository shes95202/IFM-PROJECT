import DBLink
import TxtFilter

mycursor = DBLink.sqlconnector().cursor()
resultString = "".join(TxtFilter.getResult())
fitnessString = TxtFilter.getFitness()
sqlSyntax = "INSERT INTO Results (ID,maxCapital,numberOfCom,maxUnit,numberOfGroups,result,fitness) VALUE (%s,%s,%s,%s,%s,%s,%s)"
value = [('0', "1000", "31", "100", "40", resultString, fitnessString)]

mycursor.executemany(sqlSyntax, value)

DBLink.sqlconnector().commit()
