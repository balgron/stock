import http from '../util/http'

export const incomeType = (params, data) => http('/income/income_type', params, data, 'GET')

export const consumeType = (params, data) => http('/income/consume_type', params, data, 'GET')

export const getIncomeList = (params, data) => http('/income', params, data, 'GET')

export const saveIncome = (params, data) => http('/income', params, data, 'POST')

export const editIncome = (params, data) => http('/income', params, data, 'PUT')

export const deleteIncome = (params, data) => http('/income/{id}', params, data, 'DELETE')
