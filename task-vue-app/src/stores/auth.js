import { defineStore } from 'pinia'
import api from '../services/api'
import axios from 'axios';
import {jwtDecode} from "jwt-decode";

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: localStorage.getItem('user'),
        token: localStorage.getItem('token'),
        isAuth: localStorage.getItem('isAuth'),
        firstName: localStorage.getItem('firstName'),
        lastName: localStorage.getItem('lastName'),
        middleName: localStorage.getItem('middleName'),
        teamId: localStorage.getItem('teamId')
    }),
    actions:{
        async register(userDate) {
            console.log('called register...');
            try {
                const response = await api.register(userDate);
                const tokenDecoded = jwtDecode(response.data.token);
                this.setUserData(tokenDecoded);
                this.showUserData();
            } catch (error) {
                console.log('Registration error: ' + error);
            }
        },
        async logon(credentials) {
            console.log('called logon...');
            try {
                const response = await api.login(credentials);
                this.setUserData(response.data.token);
                this.showUserData();
            } catch (error) {
                console.log('Login error: ' + error);
            }
        },
        logout() {
            console.log('called logout...');
            this.clearUserData();
            this.showUserData();
        },
        setUserData(token) {
            console.log('called setUserData: token -> ' + token);
            const tokenDecoded = jwtDecode(token);
            this.user = tokenDecoded.sub;
            this.token = token;
            this.isAuth = true;
            this.firstName = tokenDecoded.first_name;
            this.lastName = tokenDecoded.last_name;
            this.middleName = tokenDecoded.middle_name;
            this.teamId = tokenDecoded.teams_id[0];

            localStorage.setItem('user', tokenDecoded.sub);
            localStorage.setItem('token', token);
            localStorage.setItem('isAuth', 'true');
            localStorage.setItem('firstName', tokenDecoded.first_name);
            localStorage.setItem('lastName', tokenDecoded.last_name);
            localStorage.setItem('middleName', tokenDecoded.middle_name);
            localStorage.setItem('teamId', tokenDecoded.teams_id[0]);

            axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
        },
        clearUserData() {
            this.user = null;
            this.token = null;
            this.isAuth = false;
            this.firstName = null;
            this.lastName = null;
            this.middleName = null;

            localStorage.removeItem('user');
            localStorage.removeItem('token');
            localStorage.removeItem('isAuth');
            localStorage.removeItem('firstName');
            localStorage.removeItem('lastName');
            localStorage.removeItem('middleName');
            localStorage.removeItem('teamId');

            delete axios.defaults.headers.common['Authorization'];
        },
        showUserData() {
            console.log('Authorization -> ' + axios.defaults.headers.common['Authorization']);
            console.log('Current user: \n' +
                'logon -> ' + this.user + ';\n' +
                'token -> ' + this.token + ';\n' +
                'first name -> ' + this.firstName + ';\n' +
                'last name -> ' + this.lastName + ';\n' +
                'middle name -> ' + this.middleName + ';\n' +
                'team id -> ' + this.teamId + ';'
            );
        },
        getUserName() {
            return this.user;
        },
        getFullUserName() {
            return this.firstName + ' ' + this.lastName + ' ' + this.middleName;
        }
    }
});