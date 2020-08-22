import http from '@/util/http'

export const stockList = (params, data) => http('/stock', params, data, 'GET')

export const stockOne = (params, data) => http('/stock/{code}', params, data, 'GET')

export const stockHistory = (params, data) => http('/stock/history/{code}', params, data, 'GET')

export const backTestStrategy = (params, data) => http('/stock_back_test/strategy', params, data, 'GET')

export const backTestStrategyParams = (params, data) => http('/stock_back_test/strategy/params', params, data, 'GET')

export const backTest = (params, data) => http('/stock_back_test', params, data, 'POST')
