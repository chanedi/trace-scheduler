<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="application/javascript">
        function compensate(id) {
            $.get('taskInfo/compensate/' + id, function(data) {
                if (data == true) {
                    alert('已执行修复！');
                } else {
                    alert('修复发生异常！');
                }
            });
        }

        function retry(id) {
            $.get('taskInfo/retry/' + id, function(data) {
                if (data == true) {
                    alert('已重新执行！');
                } else {
                    alert('执行发生异常！');
                }
            });
        }

        $(function() {
            $("#taskInfoTable").DataTable({
                processing: true,
                serverSide: true,
                searching: false,
                ajax: {
                    url: "taskInfo.json",
                    data: function(data) {
                        data.filter = [{
                            field: "modifyTime",
                            data: {
                                comparison: "gt",
                                type: "date",
                                value: $("#taskInfoForm input[name='earliestRunTime']").val()
                            }
                        },{
                            field: "modifyTime",
                            data: {
                                comparison: "lt",
                                type: "date",
                                value: $("#taskInfoForm input[name='latestRunTime']").val()
                            }
                        }, {
                            field: "onlyFailure",
                            data: {
                                type: "custom",
                                value: $("#taskInfoForm input[name='onlyFailure']").prop('checked')
                            }
                        }];
//                        var arr = $("#taskInfoForm").serializeArray();
//                        for(var i = 0; i < arr.length; i++) {
//                            var param = arr[i];
//                            data[param.name] = param.value;
//                        }
                    }
                },
                order: [[ 3, "desc" ]],
                columns: [
                    { data: null, title: "任务名称", render : function(data, type, full, meta) {
                        return '<a href="taskDetailInfo?taskInfoId=' + data.id + '">'+ data.jobName + '</a>';
                    } },
                    { data: "mainTableName", title: "数据处理主表"},
                    { data: "jobData", title: "任务详细信息"},
                    { data: "createTime", title: "任务实际执行时间"},
                    { data: "dataCount", title: "应处理数据"},
                    { data: "processedCount", title: "已处理数据"},
                    { data: "successedCount", title: "处理成功的数据"},
                    { data: "failureCount", title: "处理失败的数据"},
                    { data: "isFailed", title: "执行结果", render : function(data, type, full, meta) {
                        if (data == true) {
                            return '失败';
                        }
                        return '成功';
                    }},
                    { data: null, title: "操作", render : function(data, type, full, meta) {
                        var btn = '';
                        if (data.isFailed && data.allowRetry) {
                            btn += '<button class="btn btn-warning btn-sm" type="button" onclick="retry(\''+ data.id +'\')">重新执行</button>';
                        }
                        if (data.failureCount > 0) { // 只能修复此情况
                            btn += '<button class="btn btn-warning btn-sm" type="button" onclick="compensate(\''+ data.id +'\')">修复</button>';
                        }
                        return btn;
                    } }
                ]
            });
        });

    </script>
</head>
<body>
<h1>
    ${title}
</h1>
<form id="taskInfoForm" table="#taskInfoTable">
    <div class="row">
        <div class="col-sm-3" style="text-align: center;">
            <label><input name="onlyFailure" type="checkbox">执行失败的任务</label>
        </div>
        <div class="col-sm-3" style="text-align: center;">
            <label>最早任务执行时间:</label>
            <input name="earliestRunTime" type="text" class="datetimepicker"/>
        </div>
        <div class="col-sm-3">
            <label>最晚任务执行时间:</label>
            <input name="latestRunTime" type="text" class="datetimepicker"/>
        </div>
        <div class="col-sm-3">
            <button class="btn-search" type="button">
                <i class="icon-edit"></i>查询
            </button>
            <button class="btn-clear" type="reset">
                <i class="icon-edit"></i>清空
            </button>
        </div>
    </div>
</form>
<table id="taskInfoTable" class="dataTable"></table>
</body>
</html>
