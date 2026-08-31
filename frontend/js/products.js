/** FairCart Products API - Product related API calls */
class ProductsApi {
    constructor(apiClient) {
        this.api = apiClient;
    }

    async getAllProducts(page = 0, size = 12, sort = 'createdAt,desc') {
        const params = { page, size, sort };
        return this.api.get(FairCartConfig.ENDPOINTS.PRODUCTS, params);
    }

    async getProductById(id) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRODUCTS}/${id}`);
    }

    async getProductsByCategory(categoryId, page = 0, size = 12, sort = 'createdAt,desc') {
        const params = { page, size, sort };
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRODUCTS}/category/${categoryId}`, params);
    }

    async searchProducts(keyword, page = 0, size = 12, sort = 'createdAt,desc') {
        const params = { keyword, page, size, sort };
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRODUCTS}/search`, params);
    }

    async filterProducts(filters = {}, page = 0, size = 12, sort = 'createdAt,desc') {
        const params = { ...filters, page, size, sort };
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRODUCTS}/filter`, params);
    }

    async getTopProducts(limit = 10) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRODUCTS}/top`, { limit });
    }

    async createProduct(productData) {
        return this.api.post(FairCartConfig.ENDPOINTS.PRODUCTS, productData);
    }

    async updateProduct(id, productData) {
        return this.api.put(`${FairCartConfig.ENDPOINTS.PRODUCTS}/${id}`, productData);
    }

    async deleteProduct(id) {
        return this.api.delete(`${FairCartConfig.ENDPOINTS.PRODUCTS}/${id}`);
    }

    async calculateIntelligenceScore(id) {
        return this.api.post(`${FairCartConfig.ENDPOINTS.PRODUCTS}/${id}/score`, {});
    }

    async getPriceHistory(id) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRICE_HISTORY}/product/${id}`);
    }

    async getPriceStats(id) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.PRICE_HISTORY}/product/${id}/stats`);
    }

    async compareProducts(productIds) {
        return this.api.post(FairCartConfig.ENDPOINTS.COMPARISON, { productIds });
    }
}

const productsApi = new ProductsApi(api);