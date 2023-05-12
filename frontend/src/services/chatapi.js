import axios from "axios";

const api = axios.create({
    baseURL: 'http://localhost:8080/api/',
});

const chatAPI = {
    getMessages: (groupId) => {
        console.log('Calling get messages from API');
        return api.get(`messages/${groupId}`);
    },

    sendMessage: (username, text) => {
        let msg = {
            sender: username,
            message: text
        }
        return api.post(`send`, msg);
    }
}

export default chatAPI