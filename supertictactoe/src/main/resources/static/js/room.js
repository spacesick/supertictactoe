var stompClient = null;
var url = window.location.origin + "/";

function connect(roomId, player2) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        if (player2 != "null") {
            updatePlayer(roomId, player2)
        }
        stompClient.subscribe('/topic/room/' + roomId, function (result) {
            let json = JSON.parse(result.body);
            console.log(json);
            console.log("Started? " + json['started']);
            if (json['started']) {
                console.log("STARTED. Disconnecting...");
                if (json['url'] != null) {
                    console.log(json['url']);
                    window.location.href = url + json['url'];
                }
            } else {
                let player2 = json['player2'];
                console.log(player2);
                showPlayer2(player2);
            }
        });
    });


}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function updatePlayer(roomId, player2) {
    console.log("UPDATE PLAYER: " + player2);
    stompClient.send("/app/join/" + roomId, {}, JSON.stringify({
        'player2' : player2,
        'started' : false,
    }));
}

function start(roomId) {
    console.log("START GAME: " + roomId);
    console.log($("#size").val());
    console.log($("#length").val());
    window.location.href = url + roomId + "/play?b=" + $("#size").val() + "&w=" + $("#length").val();
    console.log(window.location.href);
    stompClient.send("/app/join/" + roomId, {}, JSON.stringify({
        'player2' : null,
        'started' : true,
    }));
}

function showPlayer2(player2) {
    console.log("SHOW PLAYER: " + player2);
    $("#player2").replaceWith("<h2>" + player2 + "</h2>");
    $("#start").css("display", "initial");
}
