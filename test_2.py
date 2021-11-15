import datetime
today = datetime.datetime.today()
today = datetime.datetime.strftime(today, "%Y/%m/%d %H:%M:%S")
today = datetime.datetime.strptime(today, "%Y/%m/%d %H:%M:%S")
print(today)