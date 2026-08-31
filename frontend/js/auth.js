/** FairCart Auth API - Authentication related API calls */
class AuthApi {
    constructor(apiClient) {
        this.api = apiClient;
    }

    async register(userData) {
        const response = await this.api.post(`${FairCartConfig.ENDPOINTS.AUTH}/register`, userData);
        if (response.success && response.data?.accessToken) {
            this.api.setToken(response.data.accessToken);
        }
        return response;
    }

    async login(credentials) {
        const response = await this.api.post(`${FairCartConfig.ENDPOINTS.AUTH}/login`, credentials);
        if (response.success && response.data?.accessToken) {
            this.api.setToken(response.data.accessToken);
        }
        return response;
    }

    async logout() {
        this.api.setToken(null);
        return { success: true, message: 'Logged out successfully' };
    }

    async getCurrentUser() {
        return this.api.get(`${FairCartConfig.ENDPOINTS.AUTH}/me`);
    }

    isAuthenticated() {
        return !!this.api.token;
    }

    getToken() {
        return this.api.token;
    }
}

const authApi = new AuthApi(api);