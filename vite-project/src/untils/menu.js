

export const  menuConvert = (dataParam) => {
    
    let resData = []
    dataParam.forEach(element => {
        
        if (element.pid == null) {
            resData.push(element)
        } else {
            const parent = resData.find(item => item.id == element.pid)
            if (parent) {
                if (parent.children) {
                    parent.children.push(element)
                } else {
                    parent.children = [element]
                }
            }
        }
    });
    //重新赋值
    return resData
}

export const  getKeys = (dataParam) => {
    
    let resData = []
    dataParam.forEach(element => {
        if (element.pid) {
            resData.push(element.id)
        }
        
    })
    
return resData
}



