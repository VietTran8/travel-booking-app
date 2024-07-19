const chatBodyElement = $(".chat-body");
const roomsContainerElement = $(".rooms-container");
const roomName = $(".joined-room-name");
const roomAvatar = $(".joined-room-avatar");
const loginForm = $("#login-form");
const chatForm =("$chat-form");

const renderRoom = (room) => {
    console.log(room.avatar);
    const roomElement = $(`<div class="d-flex gap-1 align-items-center room" onclick="service.joinRoom(${room.roomId}, '${room.name}', '${room.avatar}')">
                                <img class="avatar" src="${room.avatar}"/>
                                <div>
                                    <div class="room-name">${room.name}<span class='unread-point'></span></div>
                                    <div class="room-message">${room.newestMessage.message}</div>
                                </div>
                            </div>`);

    roomElement.appendTo(roomsContainerElement);
}

const renderMessage = (message) => {
    const messageElement = $(`<div class="message-container ${message.role === "CUSTOMER" ? "sender" : "receiver"}">
                        <div class="message-wrapper">
                            <img class="avatar" src="${message.avatar}"/>
                            <div class="message">${message.message}</div>
                        </div>
                    </div>`);
    messageElement.appendTo(chatBodyElement);
}

const getUser = () => {
    return JSON.parse(localStorage.getItem("user"));
}
 
const login = (event) => {
    event.preventDefault();
    const email = $('input[name="email"]').val();
    const password = $('input[name="password"]').val();

    $.post({
        url: "http://localhost:8080/api/auth/signin",
        contentType: "application/json",
        data: JSON.stringify({ email, password }),
        success: function(response, status) {
            const user = {
                id: response.data?.id,
                email: response.data?.email,
                address: response.data?.address,
                fullName: response.data?.fullName,
                phone: response.data?.phone,
                roles: response.data?.roles,
                avatar: response.data?.avatar,
                active: response.data?.active,
                accessToken: response.data.accessToken,
            }
            location.href = "/";
            localStorage.setItem("user", JSON.stringify(user));
        },
        error: function(xhr, status, error) {
            console.error("Sai email hoặc mặt khẩu");
        }
    });
}

const fetchRoom = () => {
    $.get({
        url: 'http://localhost:8080/api/admin/message/rooms',
        headers: {
            'Authorization': "Bearer " + getUser().accessToken,
            'content-type': 'application/json'
        },
        success: function(response, status, xhr) {
            response.data.forEach(room => {
                renderRoom(room);
            })
        },
        error: function(xhr, status, error) {
            // alert(error);
        }
    });
}

const connect = () => {
    let currentRoom = -1;
    const socket = io("http://192.168.1.3:8085");

    socket.on("connected", (messages) => {
        messages.forEach(message => {
           renderMessage(message)
        });
        chatBodyElement.scrollTop(chatBodyElement[0].scrollHeight);
    });

    socket.on("receive", (message) => {
        renderMessage(message);
        chatBodyElement.scrollTop(chatBodyElement[0].scrollHeight);
    });

    const joinRoom = (newRoom, name, img) => {
        currentRoom = newRoom;
        roomAvatar.attr("src", img);
        roomName.html(name);
        chatBodyElement.html("");
        socket.emit("join-room", { room: currentRoom, admin: true });
    }

    const sendMessage = (message) => {
        socket.emit("send", message);
    }

    const getRoom = () => {
        return currentRoom;
    }

    return {
        joinRoom,
        sendMessage,
        getRoom
    }
}

const sendMessage = (event) => {
    event.preventDefault();

    const newMessage = {
        room: service.getRoom(),
        uid: getUser().id,
        role: "ADMIN",
        avatar: getUser().avatar,
        message: $('input[name="message"]').val(),
    }
    $('input[name="message"]').val("");
    renderMessage(newMessage);
    chatBodyElement.scrollTop(chatBodyElement[0].scrollHeight);
    service.sendMessage(newMessage);
}
