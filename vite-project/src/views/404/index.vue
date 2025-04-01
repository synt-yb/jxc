<template>
    <div class="container">
     <el-button  @click="goback" class="back">返回首页</el-button>
    </div>
</template>
<script setup>
    import {useRouter} from 'vue-router'
    import {ref,onMounted} from 'vue'
    const router = useRouter()
    onMounted(()=>{
        //
        

        //

    })
    const goback=()=>{
        router.push('/')
    }
</script>

<style scoped>
.container{
    height: 100%;
    background: url('https://cyberdefender.hk/en-us/wp-content/uploads/2021/07/404-01-scaled.jpg');
    background-size: cover;
    background-position: center;
}
.back{
    position: absolute;
    top: 85%;
    left: 75%;
    width: 100px;
    height: 50px;
    color: #021133;
    font-size: 20px;
    border-radius: 20px;
    font-weight: bold;
    background-color: #04BCCC;
}
</style>