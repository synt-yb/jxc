import { defineStore } from "pinia";
import CryptoJS from 'crypto-js'

export const userStore = defineStore("userStore", {
  state: () => ({
    user:{id:"",name:""},
    userRemember:{account:"",password:""},
    userRole:''
  }),
  actions: {
    remenberUser(account,password){
      this.userRemember.account=account
      this.userRemember.password=encryptDES(password)
    },
    getRemberPassword(){
        
      if(this.userRemember.password==="")
        return ""
      else{
        let ciphertext=this.userRemember.password
        return decryptDES(ciphertext)
      }      
    },
    clearUser(){
      this.user={id:"",name:""}
    }
  
  },
  getters: {
    
  },
  persist: true,
});

 //加密
 function encryptDES(password) {
    return CryptoJS.DES.encrypt(password, CryptoJS.enc.Utf8.parse('areyouok'), {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString();
}
// 解密
function decryptDES(ciphertext) {
    const bytes  = CryptoJS.DES.decrypt(ciphertext, CryptoJS.enc.Utf8.parse('areyouok'), {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return bytes.toString(CryptoJS.enc.Utf8);
}