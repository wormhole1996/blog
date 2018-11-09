layui.use(['table', 'jquery', 'layer'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;

    var parameter = {
        id: 'menu-table',
        elem: '#menu-table',
        url: '/admin/menu/list',
        method: 'get',
        width: 867,
        cellMinWidth: 100,
        page: true,
        toolbar: '#toolbar-head',
        parseData: function (response) {
            return {
                code: response.status,
                message: response.message,
                count: response.data.count,
                data: response.data.items
            }
        },
        cols: [[
            {field: 'id', width: 300, title: 'ID'},
            {field: 'name', width: 150, title: '菜单名', sort: true, edit: 'text'},
            {field: 'url', width: 200, title: 'URL', sort: true, edit: 'text'},
            {field: 'deleteTag', width: 110, title: '能否删除'},
            {fixed: 'right', width: 100, title: '操作', toolbar: '#toolbar-col'}
        ]]
    };

    var tableIns = table.render(parameter);

    table.on('toolbar(menu-table-1)', function (obj) {
        if (obj.event === 'add') {
            layer.open({
                type: 1,
                title: '新增',
                skin: 'layui-layer-rim',
                area: ['420px', '240px'],
                btn: ['新增'],
                content: $('#add').text(),
                yes: function (index, layero) {
                    var name = $('#name').val();
                    var url = $('#url').val();
                    if (!(name.length && url.length)) {
                        layer.open({
                            type: 0,
                            content: "不能为空"
                        });
                    } else {
                        var data = {};
                        data['name'] = name;
                        data['url'] = url;

                        var param = {
                            data: {
                                menu: [data]
                            }
                        };
                        insertMenuAjax(param);
                        layer.close(index);
                    }
                }
            });
        }
    });

    table.on('tool(menu-table-1)', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'del') {
            layer.confirm('确认删除该菜单吗', function (index) {

                var param = {
                    data: {
                        menu: [{
                            id: data.id
                        }]
                    }
                };
                deleteMenuAjax(param);
                layer.close(index);
            });
        }
    });

    table.on('edit(menu-table-1)', function (obj) {
        if (!obj.value.length) {
            layer.open({
                type: 0,
                content: "不能为空"
            });
            tableIns.reload(parameter);
        } else {
            var param = {
                data: {
                    menu: [{
                        id: obj.data.id,
                        name: obj.data.name,
                        url: obj.data.url
                    }]
                }
            };
            updateMenuAjax(param);
        }
    });

    function updateMenuAjax(param) {
        $.ajax({
            url: "/admin/menu/update",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
                tableIns.reload(parameter);
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }

    function deleteMenuAjax(param) {
        $.ajax({
            url: "/admin/menu/delete",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
                tableIns.reload(parameter);
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }

    function insertMenuAjax(param) {
        $.ajax({
            url: "/admin/menu/insert",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
                tableIns.reload(parameter);
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }
});