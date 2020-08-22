import Main from '@/view/main'

export const commonRouter = [
  {
    path: '/',
    title: '首页',
    hidden: true,
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/view/home')
      }
    ]
  }
]

export const appRouter = [
  {
    path: '/stock',
    title: '股票',
    icon: 'el-icon-location',
    component: Main,
    children: [
      {
        path: '/stock/list',
        title: '股票信息',
        component: () => import('@/view/stock/list')
      },
      {
        path: '/stock/history',
        title: '股票历史',
        component: () => import('@/view/stock/history')
      },
      {
        path: '/stock/operate',
        title: '股票操作',
        component: () => import('@/view/stock/operate')
      },
      {
        path: '/stock/back_test',
        title: '股票回测',
        component: () => import('@/view/stock/back-test')
      }
    ]
  }
]
