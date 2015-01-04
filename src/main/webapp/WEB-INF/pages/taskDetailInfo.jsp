<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="application/javascript">
        $(function() {
            $("#taskDetailInfoTable").DataTable({
                processing: true,
                serverSide: true,
                searching: false,
                ajax: {
                    url: "taskDetailInfo.json" + window.location.search,
                    data: function(data) {
                        var handleStatus = [];
                        $("#taskDetailInfoForm input[name='handleStatus']:checked").each(function(i) {
                            handleStatus.push($(this).val());
                        });
                        data.filter = [{
                            field: "modifyTime",
                            data: {
                                comparison: "gt",
                                type: "date",
                                value: $("#taskDetailInfoForm input[name='earliestRunTime']").val()
                            }
                        },{
                            field: "modifyTime",
                            data: {
                                comparison: "lt",
                                type: "date",
                                value: $("#taskDetailInfoForm input[name='latestRunTime']").val()
                            }
                        }, {
                            field: "handleStatus",
                            data: {
                                type: "list",
                                value: handleStatus
                            }
                        }];
//                        var arr = $("#taskDetailInfoForm").serializeArray();
//                        for(var i = 0; i < arr.length; i++) {
//                            var param = arr[i];
//                            data[param.name] = param.value;
//                        }
                    }
                },
                columns: [
                    { data: "modifyTime", title: "数据处理时间"},
                    { data: "dataTargetId", title: "处理数据主键"},
                    { data: "handleStatus", title: "数据处理结果", render : function(data, type, full, meta) {
                        return $("#taskDetailInfoForm input[name=handleStatus][value=" + data + "]").next("label").html();
                    }}
                ]
            });
        });

    </script>
</head>
<body>
<h1>
    ${taskInfo.jobName}
</h1>
<form id="taskDetailInfoForm" table="#taskDetailInfoTable">
    <div class="row">
        <div class="col-sm-3" style="text-align: center;">
            <label>最早数据处理时间:</label>
            <input name="earliestRunTime" type="text" class="datetimepicker"/>
        </div>
        <div class="col-sm-3">
            <label>最晚数据处理时间:</label>
            <input name="latestRunTime" type="text" class="datetimepicker"/>
        </div>
        <div class="col-sm-3" style="text-align: center;">
            <label>数据处理结果:</label>
            <c:forEach items="${handleStatus}" var="item">
                <input name="handleStatus" value="${item.key}" type="radio"><label>${item.value}</label>
            </c:forEach>
        </div>
        <div class="col-sm-3">
            <button class="btn-search" type="button">
                <i class="icon-edit"></i>查询
            </button>
            <button class="btn-clear" type="reset">
                <i class="icon-edit"></i>清空
            </button>
            <button class="btn btn-warning btn-sm" type="button" onclick="window.location='taskInfo'">返回</button>
        </div>
    </div>
</form>
<table id="taskDetailInfoTable" class="dataTable"></table>
</body>
</html>
