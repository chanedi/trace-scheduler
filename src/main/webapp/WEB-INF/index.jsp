<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="page-content" class="page-content">
    <div class="page-header">
        <h1>
            客户管理
        </h1>
    </div>
    <form>
        <input type="hidden" id="currentPage" name="currentPage" value="1"/>
        <input type="hidden" id="pageSize" name="pageSize" value="10"/>

        <div class="row" style="height:40px;">
            <div class="col-sm-3" style="text-align: center;">
                <label class="col-sm-4 control-label  no-padding-right">客户电话:</label>
                <input id="customer-search-phone" name="userPhone" class="col-sm-8" type="text"/>
            </div>
            <div class="col-sm-3">
                <label class="col-sm-4 control-label  no-padding-right">姓名:</label>
                <input id="customer-search-user-name" name="userName" class="col-sm-8" type="text"/>

            </div>
            <div class="col-sm-3">
                <label class="col-sm-4 control-label  no-padding-right">地址:</label>
                <input id="customer-search-user-address" name="address" class="col-sm-8" type="text"/>
            </div>
            <div class="col-sm-3" style="width:80px;">
                <button id="customer-search-query-button" style="width:80px;" class="btn btn-warning btn-sm"
                        type="button" onclick="customerSearch()">
                    <i class="icon-edit"></i>查询
                </button>
            </div>
            <div class="col-sm-3" style="width:80px;margin-left: 10px;">
                <button id="customer-search-clear-button" style="width:80px;" class="btn btn-warning btn-sm"
                        type="button" onclick="customerClear()">
                    <i class="icon-edit"></i>清空
                </button>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-responsive">
                <div id="customerlist-ftable_wrapper" class="dataTables_wrapper">
                    <table id="customer-table" class="table table-striped table-bordered table-hover dataTable"
                           width="100%" aria-describedby="stafflist-table_info" style="width: 100%;">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
