/** FairCart API Client - Core HTTP wrapper with JWT auth */
class ApiClient {
    constructor() {
        this.baseUrl = FairCartConfig.API_BASE_URL;
        this.token = localStorage.getItem('faircart_token');
    }

    setToken(token) {
        this.token = token;
        if (token) {
            localStorage.setItem('faircart_token', token);
        } else {
            localStorage.removeItem('faircart_token');
        }
    }

    getHeaders() {
        const headers = {
            'Content-Type': 'application/json',
        };
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        return headers;
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        const config = {
            headers: this.getHeaders(),
            ...options,
        };

        try {
            const response = await fetch(url, config);
            
            // Handle 401 - token expired or invalid
            if (response.status === 401) {
                this.setToken(null);
                window.dispatchEvent(new CustomEvent('auth:logout'));
                throw new Error('Session expired. Please login again.');
            }

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || `HTTP error! status: ${response.status}`);
            }

            return data;
        } catch (error) {
            console.error(`API Error (${endpoint}):`, error);
            throw error;
        }
    }

    // GET request
    async get(endpoint, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const url = queryString ? `${endpoint}?${queryString}` : endpoint;
        return this.request(url, { method: 'GET' });
    }

    // POST request
    async post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body),
        });
    }

    // PUT request
    async put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body),
        });
    }

    // PATCH request
    async patch(endpoint, body) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(body),
        });
    }

    // DELETE request
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

// Export singleton instance
const api = new ApiClient();