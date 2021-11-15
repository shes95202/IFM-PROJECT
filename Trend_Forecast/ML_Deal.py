import numpy as np
import pandas as pd
import pymysql
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import KFold
from sklearn import tree
from sklearn import ensemble
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import mean_squared_error
from sklearn.metrics import classification_report,confusion_matrix
import datetime
import gc

def stock_select(cursor):

    today = datetime.datetime.today()
    today = datetime.datetime.strftime(today, "%Y/%m/%d %H:%M:%S")
    today = datetime.datetime.strptime(today, "%Y/%m/%d %H:%M:%S")
    command = f"""SELECT stock_ID, stock_name FROM stock.stock_top_30"""
    cursor.execute(command)
    stock_number = cursor.fetchall()
    stock_number = pd.DataFrame(stock_number, columns=['stock_ID', 'stock_name'])
    check = 1
    count = 1
    answer_ele_list = ['綜合評估', 'MA5', 'MA30', 'MA60', 'RSI', 'MACD', '布林通道', '九轉序列', '新聞情感']
    for n in range(0, len(stock_number['stock_ID'])):
        try:
            Sklearn_Deal(cursor, stock_number['stock_ID'][n], stock_number['stock_name'][n], today, check, answer_ele_list, count)
            check = check + 9
            count = count + 1
        except Exception as e:
           print(stock_number['stock_ID'][n],e)

def Sklearn_Deal(cursor, stock_ID, stock_name, today, check, answer_ele_list, count):
    Data_File = f"""
                SELECT News_Score, MA5_Score, MA30_Score, MA60_Score, RSI_Score, BBAND_Score, MACD_Score, TD_Score, UpDown_Trend
                FROM news_score.Final_Score_{stock_ID} WHERE Date_ID >= '2020-01-01'
                """
    cursor.execute(Data_File)
    Data_File = cursor.fetchall()
    Data_File = pd.DataFrame(Data_File, columns=['News_Score', 'MA5_Score', 'MA30_Score', 'MA60_Score', 'RSI_Score', 'BBAND_Score', 'MACD_Score', 'TD_Score', 'UpDown_Trend'])     
    y = Data_File[['UpDown_Trend']].values.reshape(-1, 1)
    x = Data_File.drop('UpDown_Trend', axis = 1).values

    X_train, X_validation, y_train, y_validation = train_test_split(x, y, test_size = 0.33)

    # 隨機森林調參
    rfg_param_grid = {'n_estimators': [100,105,110,115,120], 'max_depth': [26,28,30,32,34]}
    rfg_forest_reg = ensemble.RandomForestClassifier()
    rfg_grid_search = GridSearchCV(rfg_forest_reg, rfg_param_grid, cv=5, scoring='neg_mean_squared_error')
    rfg_grid_search.fit(X_train, y_train.ravel())
    rfg_result = rfg_grid_search.best_params_

    rf = ensemble.RandomForestClassifier(n_estimators = rfg_result['n_estimators'], max_depth = rfg_result['max_depth'])
    rf.fit(X_train, y_train.ravel())

    # 套袋模型調參
    bagg_param_grid = {'n_estimators':[8,9,10,11,12]} # 'max_samples':[1], 'max_features':[1], 'n_jobs':[1]
    bagg_forest_reg = ensemble.BaggingClassifier()
    bagg_grid_search = GridSearchCV(bagg_forest_reg, bagg_param_grid, cv=5, scoring='neg_mean_squared_error')
    bagg_grid_search.fit(X_train, y_train.ravel())
    bagg_result = bagg_grid_search.best_params_

    bag = ensemble.BaggingClassifier(n_estimators = bagg_result['n_estimators']) # max_samples = bagg_result['max_samples'], max_features = bagg_result['max_features'], n_jobs = bagg_result['n_jobs']
    bag.fit(X_train, y_train.ravel())

    # 決策樹調參
    tg_param_grid = {'criterion':["gini"], 'max_depth':[10,11,12,13,14,15], 'min_samples_split':[2,3,4], 'splitter': ["random"]}
    tg_forest_reg = tree.DecisionTreeClassifier()
    tg_grid_search = GridSearchCV(tg_forest_reg, tg_param_grid, cv=5, scoring='neg_mean_squared_error')
    tg_grid_search.fit(X_train, y_train.ravel())
    tg_result = tg_grid_search.best_params_

    tg = tree.DecisionTreeClassifier(criterion = tg_result['criterion'], max_depth = tg_result['max_depth'], splitter = tg_result['splitter'], min_samples_split = tg_result['min_samples_split'])
    tg.fit(X_train, y_train.ravel())

    # AdaBoost調參
    Adag_param_grid = {'base_estimator':[tg], 'n_estimators':[130,132,134,136,138,140]}
    Adag_forest_reg = ensemble.AdaBoostClassifier()
    Adag_grid_search = GridSearchCV(Adag_forest_reg, Adag_param_grid, cv=5, scoring='neg_mean_squared_error')
    Adag_grid_search.fit(X_train, y_train.ravel())
    Adag_result = Adag_grid_search.best_params_

    Ada = ensemble.AdaBoostClassifier(base_estimator = Adag_result['base_estimator'],n_estimators = Adag_result['n_estimators'])
    Ada.fit(X_train, y_train.ravel())
    
    eclfg_param_grid = {'weights':[[1,2,3],[2,3,1],[3,2,1],[2,1,3]]}
    eclfg_forest_reg = ensemble.VotingClassifier(estimators=[('rf', rf), ('bag', bag), ('Ada', Ada)], voting='soft')
    eclfg_grid_search = GridSearchCV(eclfg_forest_reg, eclfg_param_grid, cv=5, scoring='neg_mean_squared_error')
    eclfg_grid_search.fit(X_train, y_train.ravel())
    eclfg_result = eclfg_grid_search.best_params_
    
    eclf = ensemble.VotingClassifier(estimators=[('rf', rf), ('tg', tg), ('bag', bag)], voting='soft', weights = eclfg_result['weights'])
    eclf.fit(X_train, y_train.ravel())

    today_file = [[Data_File['News_Score'][len(Data_File)-1], Data_File['MA5_Score'][len(Data_File)-1], Data_File['MA30_Score'][len(Data_File)-1], Data_File['MA60_Score'][len(Data_File)-1], Data_File['RSI_Score'][len(Data_File)-1], Data_File['BBAND_Score'][len(Data_File)-1], Data_File['MACD_Score'][len(Data_File)-1], Data_File['TD_Score'][len(Data_File)-1]]]
    
    y_pred = eclf.predict(today_file)

    print("rf訓練資料分數：",rf.score(X_train,y_train))
    print("rf測試資料分數：",rf.score(X_validation,y_validation))
    print('rfg',rfg_result)
    print("bag訓練資料分數：",bag.score(X_train,y_train))
    print("bag測試資料分數：",bag.score(X_validation,y_validation))
    print('bag',bagg_result)
    print("tg訓練資料分數：",tg.score(X_train,y_train))
    print("tg測試資料分數：",tg.score(X_validation,y_validation))
    print('tg',tg_result)
    print("Ada訓練資料分數：",Ada.score(X_train,y_train))
    print("Ada測試資料分數：",Ada.score(X_validation,y_validation))
    print('Ada',Adag_result)
    print("eclf訓練資料分數：",eclf.score(X_train,y_train))
    print("eclf測試資料分數：",eclf.score(X_validation,y_validation))
    print('eclf',eclfg_result)

    answer_list = [y_pred[0], Data_File['MA5_Score'][len(Data_File)-1], Data_File['MA30_Score'][len(Data_File)-1], Data_File['MA60_Score'][len(Data_File)-1], Data_File['RSI_Score'][len(Data_File)-1], Data_File['MACD_Score'][len(Data_File)-1], Data_File['BBAND_Score'][len(Data_File)-1], Data_File['TD_Score'][len(Data_File)-1], Data_File['News_Score'][len(Data_File)-1]]
    for i in range(len(answer_list)):
        now_ele_answer = answer_list[i]
        if now_ele_answer == 1.0:
            now_ele_answer = '上漲'
        elif now_ele_answer == -1.0:
            now_ele_answer = '下跌'
        else:
            now_ele_answer = '無影響'

        final_insert_command = f"""
        REPLACE INTO APP.stock_short_profit(pk, company_name, stock_symbol, name, `result`, date_time)
        VALUES({check+i}, '{stock_name}', {stock_ID}, '{answer_ele_list[i]}', '{now_ele_answer}', '{today}')
        """
        # print(final_insert_command)
        cursor.execute(final_insert_command)
        conn.commit()

        final_group_info = f"""
        UPDATE APP.stock_group_info
        SET short_profit = {now_ele_answer}
        WHERE pk = {count}
        """

        cursor.execute(final_group_info)
        conn.commit()

    #print('真實數字:')
    #print(y_validation)

    #print('\n')
    #print('預測結果:')
    #print(y_pred)

if __name__ == '__main__':
    
    db_settings = {
                    "host": "hourent.asuscomm.com",
                    "port": 3306,
                    "user": "107AB0014",
                    "password": "107-Ab0014",
                    "db": "news",
                    "charset": "utf8"
                    }

    conn = pymysql.connect(**db_settings)
    cursor = conn.cursor()
    stock_select(cursor)
    cursor.close()
    conn.close()
    gc.collect()