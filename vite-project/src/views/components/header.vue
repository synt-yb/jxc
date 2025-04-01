<template>
       
        <div :class="{header: flag,header2: !flag}">
            <slot name="header">1111</slot><br>
            {{ val.name }}
            <input type="text" v-model="temp.name">
            <button @click="$emit('send',temp.name,true)">确定</button>
            <button @click="changeBackground">变换</button>
            <slot>2222</slot>
            <slot name="footer">3333</slot>
        </div>
    
</template>
<script setup>
    import {ref,onMounted,defineProps, watch,defineEmits,defineExpose} from 'vue'
    onMounted(()=>{
        console.log(props.val)
    })
    const props=defineProps(["val","name"])
    let temp=ref({})
   
    watch(()=>props.val,()=>{
        let str=JSON.stringify(props.val)
        temp.value=JSON.parse(str)
    },{deep:true})
    const emit=defineEmits(["send"])
    let flag=ref(true)
    const changeBackground=()=>{
        flag.value=!flag.value;
    }
    defineExpose({changeBackground})
</script>
<style scoped>
    .header{background: #f00;line-height: 50px;text-align: center;color: #fff;}
    .header2{background: rgb(8, 0, 255);line-height: 50px;text-align: center;color: #fff;}
</style>