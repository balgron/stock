import datetime
import json

import pymongo
import tushare as ts


def parse_code(d):
    index = d.index('.')
    return d[index + 1:].lower() + d[:index]


def parse_date(d):
    return d[0:4] + '-' + d[4:6] + '-' + d[6:]


def save(date1, date2):
    myClient = pymongo.MongoClient("mongodb://localhost:27017/")
    stock = myClient["stock"]
    stock_history = stock["stock_history"]
    stock_history.delete_many({'day': date2})

    pro = ts.pro_api('you token')
    data = pro.daily(trade_date=date1)
    data = data[['ts_code', 'trade_date', 'open', 'high', 'low', 'close', 'vol', 'amount']]
    data['code'] = data['ts_code'].apply(parse_code)
    data['day'] = data['trade_date'].apply(parse_date)
    data['volume'] = data['vol']
    data = data.drop(['ts_code', 'trade_date', 'vol'], axis=1)
    print(data, len(data))
    if len(data) > 0:
        stock_history.insert_many(json.loads(data.T.to_json()).values())
    myClient.close()


date = datetime.datetime.strptime('2006-05-01', '%Y-%m-%d')
today = datetime.datetime.now()
while date <= today:
    d1 = date.strftime('%Y%m%d')
    d2 = date.strftime('%Y-%m-%d')
    date = date + datetime.timedelta(days=1)
    print(d1)
    save(d1, d2)
