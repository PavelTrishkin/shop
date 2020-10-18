var stomp = null;

// подключаемся к серверу по окончании загрузки страницы
window.onload = function () {
    connect();
};

function connect() {
    var socket = new SockJS('/socket');
    stomp = Stomp.over(socket);
    stomp.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stomp.subscribe('/topic/bucket', function (bucket) {
            renderItem(bucket);
        });
    });
}

// // хук на интерфейс
// $(function () {
//     $("form").on('submit', function (e) {
//         e.preventDefault();
//     });
//     $( "#send" ).click(function() { sendContent(); });
// });
//
// // отправка сообщения на сервер
// function sendContent() {
//     stomp.send("/app/bucket", {}, JSON.stringify({
//         'title': $("#title").val(),
//         'price': $("#price").val()
//     }));
// }

// рендер сообщения, полученного от сервера

// }

// function deleteTable() {
//     var tbl = $("#table");
//     while (tbl.rows.length > 0) {
//         tbl.deleteRow(0);
//     }
function renderItem(bucketJson) {
    var tbl = $("#table");
    var detail = JSON.parse(bucketJson.body);
    tbl.append("<tr>" +
        "<td>" + detail.amountProducts + "</td>" +
        "<td>" + detail.sum + "</td>" +
        // "<td>"+detail.am+"</td>" +
        // "<td>" +"</td>" +
        "</tr>");
}