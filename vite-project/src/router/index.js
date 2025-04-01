import { createRouter,createWebHistory } from 'vue-router'

const router = createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/',
            redirect:'/login'
        },
        {
            path:'/login',
            component:()=>import('../views/login/index.vue')
        },
        {
            path:'/register',
            component:()=>import('../views/register/index.vue')
        },{
            path:'/element',
            component:()=>import('../views/element/index.vue')
        },{
            path:'/main',
            component:()=>import('../views/main/index.vue'),
            children:[
                {
                    path:'closet',
                    component:()=>import('../views/closet/index.vue'),
                    meta:{title:'main/closet'}
                }
            ]
        },
        {
            path:'/:pathMatch(.*)*',
            component:()=>import('../views/404/index.vue')
        }
    ]
})
const whiteList = ['/login','/register']
router.beforeEach((to,from,next) => {
    //next()
    let token = localStorage.getItem('taken')
    if(token || whiteList.includes(to.path) ){
        next()
    }else{
        next('/login')
    }
})
export default router