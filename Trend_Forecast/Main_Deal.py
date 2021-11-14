import pymysql
import csv
import jieba
import jieba.analyse
import pandas as pd
import talib
import json
import re
import os
from tqdm import trange

def stock_select(cursor):

    command = f"""SELECT stock_ID, stock_name FROM stock.stock_top_30"""
    cursor.execute(command)
    stock_number = cursor.fetchall()
    stock_number = pd.DataFrame(stock_number, columns=['stock_ID', 'stock_name'])
    for n in range(5, len(stock_number['stock_ID'])):
        try:
            hy(cursor, stock_number['stock_ID'][n], stock_number['stock_name'][n])
        except Exception as e:
            print(stock_number['stock_ID'][n],e)

def hy(cursor, s_i, s_n): #斷詞

    jieba.set_dictionary('D:\\project\\jiebatool\\dict.txt.big') #改用繁體中文詞庫
    jieba.load_userdict("D:\\project\\jiebatool\\user_defined_dictionary.txt") #增加自訂義詞庫
    jieba.analyse.set_stop_words('D:\\project\\jiebatool\\stop_Words.txt') #增加停用字
    
    stopW=[]
    segments=[]
    remainderW=[]

    with open('D:\\project\\jiebatool\\stop_Words.txt', 'r', encoding='UTF-8') as file:
        for data in file.readlines():
            data = data.strip()
            stopW.append(data)

    with open('D:\\project\\jiebatool\\del_Words.txt', 'r', encoding='UTF-8') as file:
        for del_word in file.readlines():
            jieba.del_word(del_word)
    
    checkNum = re.compile("[^0-9 /()◆◇「」（）。，『』【】;；、,.?？!！:：·‧......<>…~%％〈〉★→《》-]+")

    get_news = f"""
        SELECT Post_Date_Time, News_ID, Headline, Author, Article_Content, Related_Stock_Number, Source
        FROM news.useful_{s_i} ORDER BY Post_Date_Time"""
    cursor.execute(get_news)
    news_file = cursor.fetchall()
    news_file = pd.DataFrame(news_file, columns=['Post_Date_Time', 'News_ID', 'Headline', 'Author', 'Article_Content', 'Related_Stock_Number', 'Source'])
   
    get_stocks = f"""
        SELECT Date_ID, stock_ID, Number_of_stocks_traded, turnover, Opening_price, Highest_price, Lowest_price, Closing_price, Price_difference, Number_of_transactions
        FROM stock.stock_{s_i} ORDER BY Date_ID"""
    cursor.execute(get_stocks)
    stocks_file = cursor.fetchall()
    stocks_file = pd.DataFrame(stocks_file, columns=['Date_ID', 'stock_ID', 'Number_of_stocks_traded', 'turnover', 'Opening_price', 'Highest_price', 'Lowest_price', 'Closing_price', 'Price_difference', 'Number_of_transactions'])

    get_Positive_Score = f"""SELECT Word, Score FROM news_score.Positive_Score"""
    cursor.execute(get_Positive_Score)
    Positive_file = cursor.fetchall()
    Positive_file = pd.DataFrame(Positive_file, columns=['word', 'score'])
    Positive_file_word = Positive_file['word'].tolist()
    Positive_file_score = Positive_file['score'].tolist()

    get_Negative_Score = f"""SELECT Word, Score FROM news_score.Negative_Score"""
    cursor.execute(get_Negative_Score)
    Negative_file = cursor.fetchall()
    Negative_file = pd.DataFrame(Negative_file, columns=['word', 'score'])
    Negative_file_word = Negative_file['word'].tolist()
    Negative_file_score = Negative_file['score'].tolist()

    now_stock_news_date = []
    now_stock_news_Score = []

    print(f"{s_i}斷詞與計分迴圈")
    hy_len = len(news_file['Headline'])
    for i in trange(hy_len):
        now_stock_news_date.append(news_file['Post_Date_Time'][i])
        AC = news_file["Article_Content"][i]
        AC_textrank = jieba.analyse.textrank(AC, topK = 10, withWeight=False)
        AC_list = AC.split('。')
        AC_have = []
        for a in range(len(AC_list)):
            for tk in AC_textrank:
                if tk in AC_list[a]:
                    words = jieba.lcut(AC_list[a], cut_all=False)
                    remainderW = list(filter(lambda c: c not in stopW and c != '\n', words))
                    remainderW = list(filter(checkNum.match, remainderW))
                    AC_have.append(remainderW)
                    break
                else:
                    pass

        News_Score = keywords(AC_have, Positive_file_word, Positive_file_score, Negative_file_word, Negative_file_score)
        now_stock_news_Score.append(News_Score)
    
    date_list, news_score_list = date_arrange(stocks_file, now_stock_news_date, now_stock_news_Score, s_i)
    MA5_Answer, MA30_Answer, MA60_Answer, MA100_Answer, MA200_Answer, MA365_Answer, RSI_Answer, bband_Answer, MACD_Answer, td_Answer, rise_or_down_list = news_tec_merge(stocks_file, s_i)
    print(f"{s_i}寫入迴圈")
    end_len = len(date_list)
    for ans in trange(end_len):
        time_switch = date_list[ans][0:4] + '-' + date_list[ans][5:7] + '-' + date_list[ans][8:10]

        #answer_update_command = f"""
            #UPDATE news_score.Final_Score_{s_i} SET Date_ID = '{time_switch}' AND News_Score = {news_score_list[ans]} AND
            #MA5_Score = {MA5_Answer[ans]} AND MA30_Score = {MA30_Answer[ans]} AND MA200_Score = {MA200_Answer[ans]} AND
            #RSI_Score = {RSI_Answer[ans]} AND BBAND_Score = {bband_Answer[ans]} AND MACD_Score = {MACD_Answer[ans]} AND 
            #TD_Score = {td_Answer[ans]} AND UpDown_Trend = {rise_or_down_list[ans]} WHERE Date_ID = '{time_switch}'
            #"""
        #cursor.execute(answer_update_command)
        #conn.commit()

        
        answer_insert_command = f"""
            Insert ignore INTO news_score.Final_Score_{s_i}(Date_ID, News_Score, MA5_Score, MA30_Score, MA60_Score, MA100_Score, MA200_Score, MA365_Score, RSI_Score, BBAND_Score, MACD_Score, TD_Score, UpDown_Trend)
            VALUES('{time_switch}', {news_score_list[ans]}, {MA5_Answer[ans]}, {MA30_Answer[ans]}, {MA60_Answer[ans]}, {MA100_Answer[ans]}, {MA200_Answer[ans]}, {MA365_Answer[ans]}, {RSI_Answer[ans]}, {bband_Answer[ans]}, {MACD_Answer[ans]}, {td_Answer[ans]}, {rise_or_down_list[ans]}) """
        print(answer_insert_command)
        cursor.execute(answer_insert_command)
    conn.commit()

# 將 dict 寫入 sql
def score_list():

    with open('D:\\project\\emotionwrod\\P_score.txt','r',encoding='utf-8') as P_jsonfile:
        P_score_data = json.load(P_jsonfile)

    for k, v in P_score_data.items():
        command = f"""INSERT INTO news_score.Positive_Score (Word, Score)
                      VALUES('{k}', {v})"""
        cursor.execute(command)
        conn.commit()
    
    with open('D:\\project\\emotionwrod\\N_score.txt','r',encoding='utf-8') as N_jsonfile:
        N_score_data = json.load(N_jsonfile)

    for s, j in N_score_data.items():
        command = f"""INSERT INTO news_score.Negative_Score (Word, Score)
                      VALUES('{s}', {j})"""
        cursor.execute(command)
        conn.commit()

def keywords(AC_have, Positive_file_word, Positive_file_score, Negative_file_word, Negative_file_score): #關鍵字

    sentence_score = 0
    News_Score = 0
    
    # 每一個句子的分數(正負相減)
    for l_p in range(len(AC_have)):
        good = 0
        bad = 0
        AC_Positive = set(AC_have[l_p]) & set(Positive_file_word) # 情感字典與該句子的交集
        AC_Negative = set(AC_have[l_p]) & set(Negative_file_word)
        for AC_P in AC_Positive:
            good += Positive_file_score[Positive_file_word.index(AC_P)] # 該句子的正向分數
        for AC_N in AC_Negative:
            bad += Negative_file_score[Negative_file_word.index(AC_N)] # 該句子的負向分數
        sentence_score += good - bad # 該句子的總評分
    try:
        News_Score = sentence_score / len(AC_have) # 該新聞的總評分
    except:
        News_Score = 0
    return News_Score
    
def date_arrange(stocks_file, now_stock_news_date, now_stock_news_Score, s_i): #新聞影響資料整理

    s_date_list = []
    for s in range(len(stocks_file['Date_ID'])):
        s_date = stocks_file['Date_ID'][s]
        s_date_str = str(int(s_date[0:3])+1911)+"/"+s_date[4:6]+"/"+s_date[7:9]
        s_date_list.append(s_date_str)

    date_list = []
    news_score_list = []
    n_news_Score = 0
    n_news_Score_tomorrow = 0
    n_news_Score_tomorrow_temp = 0
    check_day = 0
    check_day_tomorrow = 0
    check_day_tomorrow_temp = 0
    news_score = 0
    print(f"{s_i}新聞每天分數彙整")
    for i in s_date_list:
        while len(now_stock_news_date) > 0:
            top_date = now_stock_news_date[0]
            n_date_str = top_date[0:4] + "/" + top_date[5:7] + "/"+top_date[8:10]
            if i < n_date_str:
                break
            elif i == n_date_str:
                n_hour = top_date[11:13]
                n_mins = top_date[14:16]
                n_news_score_w = now_stock_news_Score[0]
                if int(n_hour) >= 9 and int(n_hour) <=13:
                    n_news_Score += n_news_score_w
                    check_day += 1
                elif int(n_hour) == 13 and int(n_mins) <=30:
                    n_news_Score += n_news_score_w
                    check_day += 1
                elif int(n_hour) < 9:
                    n_news_Score += n_news_score_w
                    check_day += 1
                else:
                    n_news_Score_tomorrow_temp += n_news_score_w
                    check_day_tomorrow_temp += 1
                now_stock_news_date.remove(top_date)
                now_stock_news_Score.remove(now_stock_news_Score[0])
            elif i > n_date_str:
                n_news_score_w = now_stock_news_Score[0]
                n_news_Score += n_news_score_w
                check_day += 1
                now_stock_news_date.remove(top_date)
                now_stock_news_Score.remove(now_stock_news_Score[0])
                
        n_news_Score += n_news_Score_tomorrow
        check_day += check_day_tomorrow
        if n_news_Score == 0:
            news_score = 0
        else:
            news_score = (n_news_Score)/check_day
            if news_score > 0.15:
                news_score = 1
            elif news_score < -0.15:
                news_score = -1
            else:
                news_score = 0
        date_list.append(i)
        news_score_list.append(news_score)

        n_news_Score_tomorrow = n_news_Score_tomorrow_temp
        check_day_tomorrow = check_day_tomorrow_temp
        n_news_Score = 0
        check_day = 0
        n_news_Score_tomorrow_temp = 0
        check_day_tomorrow_temp = 0
    
    return date_list, news_score_list

def SMA(stocks_file,MA,MA_Answer,MA_Value):#均線判斷
    data_MA = talib.MA(stocks_file['Closing_price'],timeperiod = MA_Value,matype = 0)
    data_MA = pd.DataFrame(data_MA,columns = ["SMA"+str(MA_Value)])
    data_MA = data_MA.fillna(0,axis = 0)
    for ma in data_MA.index:    
        MA.append(data_MA.at[ma,"SMA"+str(MA_Value)])
    for ma_data in range(len(MA)):
        if ma_data == 0:
            MA_Answer.append(0)
        else:
            if MA[ma_data - 1] > stocks_file['Closing_price'][ma_data - 1]:
                MA_Answer.append(1)
            elif MA[ma_data - 1] == stocks_file['Closing_price'][ma_data - 1]:
                MA_Answer.append(0)
            elif MA[ma_data - 1] < stocks_file['Closing_price'][ma_data - 1]:
                MA_Answer.append(-1)

def news_tec_merge(stocks_file, s_i): # 新聞與技術分析資料整合

    print(f"{s_i}技術分析")
    #技術指標 - MA 判斷
    MA5 = []
    MA5_Answer = []
    SMA(stocks_file,MA5,MA5_Answer,5)

    MA30 = []
    MA30_Answer = []
    SMA(stocks_file,MA30,MA30_Answer,30)

    MA60 = []
    MA60_Answer = []
    SMA(stocks_file,MA60,MA60_Answer,60)

    MA100 = []
    MA100_Answer = []
    SMA(stocks_file,MA100,MA100_Answer,100)

    MA200 = []
    MA200_Answer = []
    SMA(stocks_file,MA200,MA200_Answer,200)

    MA365 = []
    MA365_Answer = []
    SMA(stocks_file,MA365,MA365_Answer,365)

    #技術指標 - RSI 判斷
    RSI = []
    RSI_Answer = []
    data_RSI = talib.RSI(stocks_file['Closing_price'],timeperiod = 12)
    data_RSI = pd.DataFrame(data_RSI,columns = ["RSI"])
    data_RSI = data_RSI.fillna(0,axis = 0)
    for rsi in data_RSI.index:
        RSI.append(data_RSI.at[rsi,"RSI"])
    for rsi_data in range(len(RSI)):
        if rsi_data == 0:
            RSI_Answer.append(0)
        elif RSI[rsi_data-1] >= 70:
            RSI_Answer.append(-1)
        elif RSI[rsi_data-1] <= 20:
            RSI_Answer.append(1)
        elif RSI[rsi_data-1]>RSI[rsi_data-2]:
            RSI_Answer.append(1)
        elif RSI[rsi_data-1]<RSI[rsi_data-2]:
            RSI_Answer.append(-1)
        else:
            RSI_Answer.append(0)

    #布林通道
    bband_Answer=[]
    upper, middle, lower = talib.BBANDS(stocks_file['Closing_price'], timeperiod=20, nbdevup=2.1, nbdevdn=2.1, matype=0)
    for d in range(len(stocks_file['Closing_price'])):
        if d < 21:
            bband_Answer.append(0)
        else:
            if stocks_file['Closing_price'][d-1] > upper[d-1]:
                bband_Answer.append(0)
            elif stocks_file['Closing_price'][d-1] < lower[d-1]:
                bband_Answer.append(0)
            elif stocks_file['Closing_price'][d-1] < middle[d-1] and stocks_file['Closing_price'][d-1] < stocks_file['Closing_price'][d-2] :
                bband_Answer.append(-1)
            elif stocks_file['Closing_price'][d-1] > middle[d-1] and stocks_file['Closing_price'][d-1] > stocks_file['Closing_price'][d-2] :
                bband_Answer.append(1)
            else:
                bband_Answer.append(0)
    

    # 技術指標 - MACD 判斷
    MACD = []
    MACD_Answer = []
    macd, dic, md = talib.MACD(stocks_file['Closing_price'],fastperiod=12, slowperiod=26, signalperiod=9)
    macd = pd.DataFrame(macd,columns = ["MACD"])
    macd = macd.fillna(0,axis = 0)
    dic = pd.DataFrame(dic,columns = ["DIC"])
    dic = dic.fillna(0,axis = 0)
    md = pd.DataFrame(md,columns = ["MD"])
    md = md.fillna(0,axis = 0)
    for md_data in md.index:
        MACD.append(md.at[md_data,"MD"])
    for macd_data in range(len(MACD)):
        if macd_data == 0 or macd_data == 1:
            MACD_Answer.append(0)
        elif MACD[macd_data-1] > MACD[macd_data-2]:
            MACD_Answer.append(1)
        elif MACD[macd_data-1] < MACD[macd_data-2]:
            MACD_Answer.append(-1)
        else:
            MACD_Answer.append(0)

    # td判斷
    td_Answer = []
    for k in range(len(stocks_file['Closing_price'])):
        if k < 13:
            td_Answer.append(0)
        else:
            count_up = 0
            count_down = 0
            for tic in range(1,9):
                if stocks_file['Closing_price'][k-tic] > stocks_file['Closing_price'][k-4-tic]:
                    if count_down > 0:
                        td_Answer.append(0)
                        break
                    else:
                        count_up += 1
                        if count_up >= 8:
                            td_Answer.append(-1)                   
                elif stocks_file['Closing_price'][k-tic] < stocks_file['Closing_price'][k-4-tic]:
                    if count_up > 0:
                        td_Answer.append(0)
                        break
                    else:
                        count_down += 1
                        if count_down >= 8:
                            td_Answer.append(1)
                else:
                    td_Answer.append(0)
                    break

    #漲跌判斷
    rise_or_down = 0
    rise_or_down_list = []
    for p in range(len(stocks_file['Closing_price'])):
        if p+2 >= len(stocks_file['Closing_price']) or  p == 0:
            rise_or_down = 0
        else:
            if stocks_file['Closing_price'][p+2] < stocks_file['Closing_price'][p-1] * 0.995:
                rise_or_down = -1
            elif stocks_file['Closing_price'][p+2] > stocks_file['Closing_price'][p-1] * 1.005:
                rise_or_down = 1
            else:
                rise_or_down = 0        
        rise_or_down_list.append(rise_or_down)
    
    return MA5_Answer, MA30_Answer, MA60_Answer, MA100_Answer, MA200_Answer, MA365_Answer, RSI_Answer, bband_Answer, MACD_Answer, td_Answer, rise_or_down_list

# 創建每個標的的最終分數表 
def create_table():

    command = f"""SELECT stock_ID, stock_name FROM stock.stock_top_30"""
    cursor.execute(command)
    stock_number = cursor.fetchall()
    stock_number = pd.DataFrame(stock_number, columns=['stock_ID', 'stock_name'])
    for n in range(len(stock_number['stock_ID'])):
        create_command = f"""
            CREATE TABLE news_score.Final_Score_{stock_number['stock_ID'][n]}(
            Date_ID VARCHAR(20) primary key,
            News_Score FLOAT,
            MA5_Score FLOAT,
            MA30_Score FLOAT,
            MA60_Score FLOAT,
            MA100_Score FLOAT,
            MA200_Score FLOAT,
            MA365_Score FLOAT,
            RSI_Score FLOAT,
            BBAND_Score FLOAT,
            MACD_Score FLOAT,
            TD_Score FLOAT,
            UpDown_Trend FLOAT
            )"""
        cursor.execute(create_command)
    conn.commit()

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
    # create_table()

# print()
# keywords()
# print()
# date_arrange()
# print()
# news_tec_merge()
