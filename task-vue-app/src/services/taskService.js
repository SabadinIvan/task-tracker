import axios from 'axios'

//const BASE_API_URL = process.env.VUE_APP_API_URL || 'http://localhost:3000'

const BASE_API_URL = 'http://localhost:8080/api/tasks';

class TaskService {
    constructor() {
        this.apiUrl = `${BASE_API_URL}`
    }

    async createTask(taskData) {
        try {
            const response = await axios.post(this.apiUrl + '/task', {
                title: taskData.title.trim(),
                description: taskData.description.trim(),
                teamId: localStorage.getItem('teamId')
                // createdAt: new Date().toISOString(),
                // status: 'pending',
                // updatedAt: new Date().toISOString()
            })

            return {
                success: true,
                data: response.data,
                status: response.status
            }
        } catch (error) {
            console.error('Ошибка в taskService.createTask:', error)

            if (error.response) {
                // Сервер ответил с ошибкой
                return {
                    success: false,
                    error: error.response.data.message || 'Ошибка сервера',
                    status: error.response.status,
                    errors: error.response.data.errors || null
                }
            } else if (error.request) {
                // Запрос был сделан, но нет ответа
                return {
                    success: false,
                    error: 'Нет ответа от сервера. Проверьте подключение к интернету',
                    status: null
                }
            } else {
                // Ошибка при настройке запроса
                return {
                    success: false,
                    error: 'Ошибка при отправке запроса',
                    status: null
                }
            }
        }
    }

    async getTasks() {
        try {
            const response = await axios.get(this.apiUrl)
            return {
                success: true,
                data: response.data,
                status: response.status
            }
        } catch (error) {
            console.error('Ошибка в taskService.getTasks:', error)
            return {
                success: false,
                error: error.response?.data?.message || 'Ошибка получения задач',
                status: error.response?.status || null
            }
        }
    }

    async getTaskById(taskId) {
        try {
            const response = await axios.get(`${this.apiUrl}/${taskId}`)
            return {
                success: true,
                data: response.data,
                status: response.status
            }
        } catch (error) {
            console.error('Ошибка в taskService.getTaskById:', error)
            return {
                success: false,
                error: error.response?.data?.message || 'Ошибка получения задачи',
                status: error.response?.status || null
            }
        }
    }

    async updateTask(taskId, taskData) {
        try {
            const response = await axios.put(`${this.apiUrl}/${taskId}`, {
                ...taskData,
                updatedAt: new Date().toISOString()
            })
            return {
                success: true,
                data: response.data,
                status: response.status
            }
        } catch (error) {
            console.error('Ошибка в taskService.updateTask:', error)
            return {
                success: false,
                error: error.response?.data?.message || 'Ошибка обновления задачи',
                status: error.response?.status || null
            }
        }
    }

    async deleteTask(taskId) {
        try {
            const response = await axios.delete(`${this.apiUrl}/${taskId}`)
            return {
                success: true,
                data: response.data,
                status: response.status
            }
        } catch (error) {
            console.error('Ошибка в taskService.deleteTask:', error)
            return {
                success: false,
                error: error.response?.data?.message || 'Ошибка удаления задачи',
                status: error.response?.status || null
            }
        }
    }
}

export default new TaskService()