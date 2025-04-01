import { createRouter,createWebHistory } from "vue-router";
import { menuStore } from '../store/menu'


const router =createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/main',
            component:()=>import('../views/main/index.vue'),
            children:[
                {
                    path:'menuManagement',
                    component:()=>import('../views/menuManagement/index.vue')
                },
                {
                    path:'roleManagement',
                    component:()=>import('../views/roleManagement/index.vue')
                },
                {
                    path:'userManagement',
                    component:()=>import('../views/userManagement/index.vue')
                },
                {
                    path:'materialManagement',
                    component:()=>import('../views/materialManagement/index.vue')
                },
                {
                    path:'inventorySummary',
                    component:()=>import('../views/inventorySummary/index.vue')
                },
                {
                    path:'outboundOrder',
                    component:()=>import('../views/outboundOrder/index.vue')
                },{
                    path:'inboundOrder',
                    component:()=>import('../views/inboundOrder/index.vue')
                },{
                    path:'orderAudit',
                    component:()=>import('../views/orderAudit/index.vue')
                },{
                    path:'warehouseManagement',
                    component:()=>import('../views/warehouseManagement/index.vue')
                },
               
            ]
        },{
            path:'/',
            component:()=>import('../views/main/index.vue')
        },
        {
            path:'/login',
            component:()=>import('../views/login/index.vue')
        },  
        {
            path:'/:pathMatch(.*)*',
            component:()=>import('../views/404/index.vue')
        }
      
    ]

});
const whiteList=['/login'];

router.beforeEach((to,from,next)=>{
    
    //localStorage取值
    const useMenuStore=menuStore();
    let token=localStorage.getItem('token');
    const permitMenus=useMenuStore.permitMenus;
    if(whiteList.includes(to.path)||to.path==='/404'){
        next();
    }
    else if(token){      
        //情况一：进入主页    
       if(to.path==='/'||to.path=='/main'){
        next()
        return
       }
       //情况二：进入权限页面
       if(permitMenus.includes(to.path)){
        useMenuStore.addTags(to.path)
        
        next()
       }else
        next('404');
    }
    else{
        next('/login');
    }
    
})
export default router