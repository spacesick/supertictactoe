let stompClient = null;

function connect(gameId) {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/games/' + gameId, function (result) {
            let json = JSON.parse(result.body);
            console.log(json);
            let position = json.markPos;
            let mark = json.mark;
            let winner = json.winner;
            let isOver = json.over;

            showMove(position, mark);
            if (isOver) {
                showWinner(winner);
                disconnect();
            }
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function sendMove(gameId, position) {
    console.log("sendingmoveeee");
    $.ajax({
        url: '/service/' + gameId + '/move?pos=' + position,
        type: 'POST',
        success: function (data) {
            console.log(data);
            if (data.valid) {
                stompClient.send("/app/play/" + gameId, {}, JSON.stringify({
                    'markPos': position,
                    'mark': data.mark,
                    'winner': data.winner?.username,
                    'over': data.over
                }));
            }
        }
    });

}

function showMove(position, mark) {
    if (mark == 'NOUGHT')
        $("#" + position).append("<i class='fa-regular fa-circle fa-4x'></i>");
    else if (mark == 'CROSS')
        $("#" + position).append("<i class='fa-regular fa-xmark fa-4x'></i>");
}

function showWinner(username) {
    if (!username) {
        $("#winner").append("<h2>It's a draw, game over!</h2>");
    }
    else {
        $("#winner").append("<h2>Game Over! Winner is:</h2><h2>" + username + "</h2>");
    }
}