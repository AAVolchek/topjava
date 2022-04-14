const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$('#startDate').datetimepicker({
    formatDate: 'Y-m-d',
    format: 'Y-m-d',
    onShow: function (ct) {
        this.setOptions({
            maxDate: $('#endDate').val() ? $('#endDate').val() : false
        })
    },
    timepicker: false
});

$('#endDate').datetimepicker({
    formatDate: 'Y-m-d',
    format: 'Y-m-d',
    onShow: function (ct) {
        this.setOptions({
            minDate: $('#startDate').val() ? $('#startDate').val() : false
        })
    },
    timepicker: false
});

$('#startTime').datetimepicker({
    format: 'H:i',
    onShow: function (ct) {
        this.setOptions({
            maxTime: $('#endTime').val() ? $('#endTime').val() : false
        })
    },
    datepicker: false
});

$('#endTime').datetimepicker({
    format: 'H:i',
    onShow: function (ct) {
        this.setOptions({
            minTime: $('#startTime').val() ? $('#startTime').val() : false
        })
    },
    datepicker: false
});

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});

$.ajaxSetup({
    converters: {
        "text json": function( result ) {
            var json = JSON.parse(result);
            $(json).each(function (){
               if(this.hasOwnProperty("dateTime")){
                   this.dateTime = this.dateTime.replace('T', ' ').substr(0, 16);
               }
            });
            return json;
        }
    }
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});
