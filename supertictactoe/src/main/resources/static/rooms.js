const roomList = document.getElementById('roomsList');
const searchBar = document.getElementById('searchBar');
let rooms = [];

searchBar.addEventListener('keyup', (e) => {
    const searchString = e.target.value.toLowerCase();

    const filteredCharacters = rooms.filter((room) => {
        return (
            room.roomName.toLowerCase().includes(searchString)
        );
    });
    displayRooms(filteredCharacters);
});

const loadRooms = async () => {
    try {
        const res = await fetch('http://localhost:8080/roomlist');
        rooms = await res.json();
        displayRooms(rooms);
    } catch (err) {
        console.error(err);
    }
};

const displayRooms = (rooms) => {
    const htmlString = rooms
        .map((room) => {
            return `
            <li class="room">
                <h2>${room.roomName}</h2>
                <button onclick="location.href='rooms/join/${room.roomId}'" type="button">Join</button>
            </li>
        `;
        })
        .join('');
    roomList.innerHTML = htmlString;
};

loadRooms();
