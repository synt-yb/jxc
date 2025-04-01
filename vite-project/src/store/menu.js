import { defineStore } from "pinia";
import router from "../router";
import { th } from "element-plus/es/locales.mjs";


export const menuStore=defineStore('menuStore',{
    state:()=>({
        tags:[],  //标签页
        permitMenus:[],  //用户权限菜单
        allMenus:[],    //所有菜单
        showlist:[],   //显示菜单
        active_index:''
    }),
    actions:{
        setPermitMenus(list){//设置用户权限菜单
            this.showlist=list
            this.permitMenus=[]
            this.allMenus=[]
            list.forEach(item=>{    
                item.children.forEach(item=>{
                    this.permitMenus.push(item.url)
                    this.allMenus.push(item)
                })
            })
            if(this.permitMenus.includes('/main/inventorySummary')){
                let index=this.permitMenus.indexOf('/main/inventorySummary')
                if(index!=0){
                    this.permitMenus.splice(index,1)
                    this.permitMenus.unshift('/main/inventorySummary')
                }
            }
        },
        addTags(url){//添加标签
            
            let flag=true;
            this.tags.forEach(item=>{//判断是否已经存在
                if(item.url==url){
                    flag=false
                    return
                }
            })
            
            if(flag){//不存在添加
                const params=this.allMenus.find(item=>item.url==url)
                params.deletable=this.tags.length==0?false:true
                params.selected=false
                this.tags.push(params)
            }
            this.tags.forEach(item=>{
                if(item.url==url){
                    item.selected=true
                    this.active_index=item.url
                }else{
                    item.selected=false
                }
            })
            
        },
        delTag(index,selected){
            console.log(index,selected)
            this.tags.splice(index,1)
            if(selected){
                this.tags[index-1].selected=true
                router.push(this.tags[index-1].url)
            }
        }
    },persist:true
})