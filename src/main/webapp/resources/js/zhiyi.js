$(function () {
    $("button.btn-search").addClass("btn btn-warning btn-sm");
    $("button.btn-search").click(search);

    $("button.btn-clear").addClass("btn btn-warning btn-sm");
    $("button.btn-clear").attr("type", "reset");

    $("table.dataTable").addClass("table table-striped table-bordered table-hover");

    $("input.datetimepicker").click(function () {
        WdatePicker({
            startDate: '%y-%M-%d 00:00:00',
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            alwaysUseStartDate: true
        });
    });
});

function search() {
    $($(this.form).attr("table")).DataTable().ajax.reload();
}