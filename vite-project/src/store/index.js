import {defineStore} from 'pinia'
export const indexStore=defineStore('index',{
    state:()=>{
            return {
                userName:'',
                user:{
                    id:'',
                    name:'',
                    age:'',
                    sex:'',
                },

            }
    },
    actions:{
            show(){
                console.log('this.userName')
            }
    },
    getters:{
            getUserName(state){
                return state.userName
            }
    },
    persist:{
        enabled:true
    }
})