import { reactive } from 'vue';
export const getDeleteDialog = () => {
    const dialog = reactive({
        visible: false,
        title: '',
        content: '',
        init() {
          this.visible = false;
          this.title = '';
          this.content = '';
        }
      });
    return dialog;
};




export const getEditDialog = () => {
    const dialog = reactive({
        visible: false,
        title: '',
        data:{name:''},
        addable:false,
        init() {
          this.visible = false;
          this.title = '';
          this.content = '';
          this.data = {name:''};
        },
        toAdd(data){
          this.init();
          this.visible = true;
          this.data.pid = data===undefined?'':data.id
          this.title = '添加下级';
          this.addable = true;
        },
        toUpdate(data){
          this.init();
          this.visible = true;
          this.data = data;
          this.title = '材料修改';
          this.addable = false;
        }
      });
    return dialog;
};

