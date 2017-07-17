SSM框架整合参考：[http://blog.csdn.net/x_iya/article/details/74377566](http://blog.csdn.net/x_iya/article/details/74377566)

# 使用EasyUI编写CRUD界面

```
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户管理系统</title>
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.2/demo.css">
    <script type="text/javascript" src="static/jquery-easyui-1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="static/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <script>
        var url;
        function deletePerson() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $.messager.confirm("系统提示", "您确定要删除这条记录吗?", function (r) {
                    if (r) {
                        $.post('deletePerson', {delId: row.id}, function (result) {
                            if (result.success) {
                                $.messager.alert("系统提示", "已成功删除这条记录!");
                                $("#dg").datagrid("reload");
                            } else {
                                $.messager.alert("系统提示", result.errorMsg);
                            }
                        }, 'json');
                    }
                });
            }
        }

        function newPerson() {
            $("#dlg").dialog('open').dialog('setTitle', '添加用户');
            $('#fm').form('clear');
            url = 'newPerson';
        }


        function editPerson() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $("#dlg").dialog('open').dialog('setTitle', '编辑用户');
                $('#fm').form('load', row);
                url = 'updatePerson?id=' + row.id;
            }
        }


        function savePerson() {
            $('#fm').form('submit', {
                url: url,
                onSubmit: function () {
                    return $(this).form('validate');
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.errorMsg) {
                        $.messager.alert("系统提示", result.errorMsg);
                        return;
                    } else {
                        $.messager.alert("系统提示", "保存成功");
                        $('#dlg').dialog('close');
                        $("#dg").datagrid("reload");
                    }
                }
            });
        }

    </script>
</head>
<body>
<table id="dg" title="用户管理" class="easyui-datagrid" style="width:700px;height:350px"
       url="listPersons"
       toolbar="#toolbar" pagination="true"
       rownumbers="true" fitColumns="true" singleSelect="true">
    <thead>
        <tr>
            <th field="id" width="50" hidden="true">编号</th>
            <th field="name" width="50">姓名</th>
            <th field="age" width="50">年龄</th>
            <!--<th field="birth" width="50">出生日期</th>-->
            <th field="email" width="50">email</th>
        </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newPerson()">添加用户</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editPerson()">编辑用户</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deletePerson()">删除用户</a>
</div>

<div id="dlg" class="easyui-dialog" style="width:400px;height:250px;padding:10px 20px"
     closed="true" buttons="#dlg-buttons">
    <div class="ftitle">用户信息</div>
    <form id="fm" method="post">
        <table cellspacing="10px;">
            <div class="fiteam">
                <label>姓&nbsp;&nbsp;名：</label>
                <input name="name" class="easyui-validatebox" required="true" />
            </div>
            <div class="fiteam">
                <label>年&nbsp;&nbsp;龄：</label>
                <input name="age" class="easyui-validatebox" />
            </div>
            <!--<div class="fiteam">
                <label>生&nbsp;&nbsp;日：</label>
                <input name="birth" class="easyui-validatebox" validType="date" />
            </div>-->
            <div class="fiteam">
                <label>email：</label>
                <input name="email" class="easyui-validatebox" validType="email" />
            </div>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="savePerson()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg').dialog('close')">关闭</a>
</div>
</body>
</html>
```
![EasyUI](http://img.blog.csdn.net/20170717200708013?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
以上基本都是参照官方网站文档
[http://www.jeasyui.com/tutorial/app/crud.php](http://www.jeasyui.com/tutorial/app/crud.php)

接下来将上面的页面与SSM进行整合。

------------------------------

首先来看最关键的一个功能：用户以列表形式输出以及分页。

`pagination="true"` 即为打开分页主键。

> 我们定义数据网格（datagrid）列，并设置 'pagination' 属性为 true，它将在数据网格（datagrid）的底部生成一个分页（pagination）工具栏。pagination将发送两个参数到服务器：page：页码，起始值 1。rows：每页显示行。

观察实例代码:
```
$page = isset($_POST['page']) ? intval($_POST['page']) : 1;
$rows = isset($_POST['rows']) ? intval($_POST['rows']) : 10;
// ...
$rs = mysql_query("select count(*) from item");
$row = mysql_fetch_row($rs);
$result["total"] = $row[0];
 
$rs = mysql_query("select * from item limit $offset,$rows");
 
$items = array();
while($row = mysql_fetch_object($rs)){
	array_push($items, $row);
}
$result["rows"] = $items;
 
echo json_encode($result);
```
服务器将会读取page和rows参数来进行分页操作。我们可以直接使用[PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)插件来实现分页。
> PageHelper.startPage(page, rows);

由实例代码也可得到，数据已json格式返回，为了实现分页操作，还需要在返回的json数据中添加`total` 参数，即返回数据库记录的条数。事实上，增删改也都是返回json数据，用来标识增删改成功与否。

实现：
```
@ResponseBody
    @RequestMapping(value = "/listPersons", method = RequestMethod.POST)
    public Map<String, Object> getPersons(@RequestParam("page")Integer page, @RequestParam("rows")Integer rows) {
        System.out.println("page = " + page);
        System.out.println("rows = " + rows);
        Map<String, Object> map = new HashMap<>();
        map.put("total", personService.getCount());
        map.put("rows", personService.getAllPersons(page, rows));
        return map;
    }
```

分页实现：

```
@Override
    public List<Person> getAllPersons(int page, int rows) {
        PageHelper.startPage(page, rows);
        return personMapper.selectAllPersons();
    }
```


其他增删改的代码也都是大同小异了。

代码：[https://github.com/N3verL4nd/SSM_EasyUI](https://github.com/N3verL4nd/SSM_EasyUI)