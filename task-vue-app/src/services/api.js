import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
})

export default {
    register(userData) {
        return api.post('/auth/register', {
            email: userData.email,
            logonName: userData.logonName,
            password: userData.password
        })
    },

    login(credentials) {
        return api.post('/auth/login', {
            logonName: credentials.logonName,
            password: credentials.password
        })
    }
}