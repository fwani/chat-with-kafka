<template>
    <MessageList :messages="messages" :currentUser="user"></MessageList>
    <InputText :onSendMessage="onSendMessage"/>
    <LoginForm :onSubmit="handleLoginSubmit"/>
</template>

<script>

import {randomHexColor} from "vuetify/lib/util/index.mjs";
import InputText from "@/components/InputText.vue";
import chatAPI from "@/services/chatapi"
import LoginForm from "@/components/LoginForm.vue";
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import MessageList from "@/components/MessageList.vue";

export default {
    name: "ChatRoom",
    components: {MessageList, LoginForm, InputText},
    data: () => {
        return {
            SOCKET_URL: 'http://localhost:8080/ws-chat/',
            messages: [],
            user: null,
        }
    },
    created() {
        // App.vue가 생성되면 소켓 연결을 시도합니다.
        this.connect()
        this.getOlderMessages()
    },
    methods: {
        getOlderMessages(){
            // chatAPI.getMessages("foo")
        },
        connect() {
            const serverURL = "http://localhost:8080/ws-chat"
            let socket = new SockJS(serverURL);
            this.stompClient = Stomp.over(socket);
            console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
            this.stompClient.connect(
                {},
                frame => {
                    // 소켓 연결 성공
                    this.connected = true;
                    console.log('소켓 연결 성공', frame);
                    // 서버의 메시지 전송 endpoint를 구독합니다.
                    // 이런형태를 pub sub 구조라고 합니다.
                    this.stompClient.subscribe("/topic/group", res => {
                        console.log('구독으로 받은 메시지 입니다.', res.body);

                        // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                        this.messages.push(JSON.parse(res.body))
                    });
                },
                error => {
                    // 소켓 연결 실패
                    console.log('소켓 연결 실패', error);
                    this.connected = false;
                }
            );
        },
        onMessageReceived(msg) {
            console.log('New Message Received!!!', msg);
            this.messages = this.messages.concat(msg)
        },
        onSendMessage(msgText) {
            chatAPI.sendMessage(this.user.username, msgText).then(res => {
                console.log('Sent', res);
            }).catch(err => {
                console.log('Error occur while sending message to api', err);
            })
        },
        handleLoginSubmit(username) {
            console.log(username, " Logged in ...");
            this.user = {
                username: username,
                color: randomHexColor(),
            }
        }
    }

}
</script>

<style scoped>

</style>