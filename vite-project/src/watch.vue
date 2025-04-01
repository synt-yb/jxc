<script setup>
import { ref ,onMounted,computed, watch} from 'vue'


onMounted(() => {
  console.log('mounted')

})


let user=ref({})
let tip=ref('')

// watch(name,(newValue,oldValue)=>{
//   if(newValue.length<2||newValue.length>8)
//       tip.value = '长度2-8'
//   else
//     tip.value = ''

// })
// watch(()=>user.value.name,(newValue,oldValue)=>{
//   console.log(newValue)
//   if(newValue.length<2||newValue.length>8)
//       tip.value = '长度2-8'
//   else
//     tip.value = ''
// })
//watch()
watch(user,(newValue,oldValue)=>{
  if(newValue.name.length<2||newValue.name.length>8)
      tip.value = '长度2-8'
  else
    tip.value = ''
},
{deep:true})
</script>
  
<template>
<div>
 

  <input type="text" v-model="user.name">{{ tip }}<br>
  <input type="text" v-model="user.age"><br></br>
</div>
  

</template>


<style scoped>

</style>
