import os
path = "./txt/CombinROI#1.txt"
f = None
length = 0
DataList = []
try:
    f = open(path, 'r')
    for line in f.readlines():
        DataList.append(line)
        length += 1
except IOError:
    print("ERROR: can not found" + path)
    if f:
        f.close()
finally:
    if f:
        f.close()
# NumOfGroups = length-1
# ResultList = []
# Fitness = DataList[-1]
# for i in range(NumOfGroups):
#     ResultList.append(DataList[i])
# print(ResultList)
