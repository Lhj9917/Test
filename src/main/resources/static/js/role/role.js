var table=layui.table;
var tableIns = table.render({
    elem:"#roleList",
    url:'/role/list',//数据接口
    page:true,//开启分页
    parseData: function(res){ //res 即为原始返回的数据
        return {
            "code": res.code, //解析接口状态
            "msg": res.msg, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.records //解析数据列表
        };
    },
    cols:[[//表头
        {field:'roleName',title:'角色名称'},
        //{field:'remarks',title:'角色备注'},
        {field:'createTime',title:'创建时间'},
        {title:'操作',toolbar:'#barDemo'}
    ]]
});

function query(){
    tableIns.reload({
        where: { //设定异步数据接口的额外参数，任意设
            roleName: $("#roleName").val(),
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
/**
 * 进入新增页
 */
function intoAdd(){
    openlayer('/role/toAdd','新增角色');

    showTree('/role/listResource','resource',true);

    mySubmit('addSubmit','POST',addIds);
}

/**
 * 通用的资源数方法
 * @param url
 * @param id
 */
function showTree(url,id,showCheckbox) {
    if (typeof (showCheckbox)=='undefined'){
        showCheckbox=true;
    }
    $.ajax({
        url:url,
        async:false,
        type: 'GET',
        success:function (res) {
            if (res.code==0){
                layui.tree.render({
                    elem:'#'+id,
                    data:res.data,
                    id:id,
                    showCheckbox:showCheckbox
                });
            }
        }
    });
}
var addIds=function (field) {
    let checkedData = layui.tree.getChecked('resource');
    field.resourceIds=getIds(checkedData,[]);
}
function getIds(checkData,arr) {
    for (let i in checkData) {
        arr.push(checkData[i].id);
        arr=getIds(checkData[i].children,arr);
    }
    return arr;
}
//工具条事件

table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    let roleId=data.roleId;

    if(layEvent === 'detail'){ //查看
        //do somehing
        openlayer('/role/toDetail/'+roleId,'角色详情');

        showTree('/role/listResource/'+roleId+'/1','resource',false);
    } else if(layEvent === 'del'){ //删除
        layer.confirm('真的删除行么', function(index){

            layer.close(index);
            myDelete("/role/"+roleId);

            //向服务端发送删除指令
        });
    } else if(layEvent === 'edit'){ //编辑
        //do something
        openlayer('/role/toUpdate/'+roleId,'编辑角色');

        showTree('/role/listResource/'+roleId+'/0','resource',true)

        mySubmit('updateSubmit','PUT',addIds);
    }
});