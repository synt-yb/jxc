<template>
    
    <div style="height: 100%;width: 100%;text-align: center;padding-top: 100px;">
        
        404 not found<br>
        页面不存在,{{second}}秒后跳转至<a href="#" @click.prevent="gotologin">首页</a>
    </div>
</template>
<script setup>
    import {ref,onMounted} from 'vue'
    import {useRouter} from 'vue-router'
    const router = useRouter()
    onMounted(()=>{

    })
    let second = ref(5)
    const timer = setInterval(()=>{
        console.log(second.value)
        second.value--
        if(second.value<=0){
            clearInterval(timer)
            gotologin()
        }
    },1000)
    function gotologin(){
        router.replace('/')
    }
    let val=ref('')
</script>
<style scoped>

</style>