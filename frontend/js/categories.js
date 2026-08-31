/** FairCart Categories API - Category related API calls */
class CategoriesApi {
    constructor(apiClient) {
        this.api = apiClient;
    }

    async getAllCategories() {
        return this.api.get(FairCartConfig.ENDPOINTS.CATEGORIES);
    }

    async getActiveCategories() {
        return this.api.get(`${FairCartConfig.ENDPOINTS.CATEGORIES}/active`);
    }

    async getCategoryById(id) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.CATEGORIES}/${id}`);
    }

    async createCategory(categoryData) {
        return this.api.post(FairCartConfig.ENDPOINTS.CATEGORIES, categoryData);
    }

    async updateCategory(id, categoryData) {
        return this.api.put(`${FairCartConfig.ENDPOINTS.CATEGORIES}/${id}`, categoryData);
    }

    async deleteCategory(id) {
        return this.api.delete(`${FairCartConfig.ENDPOINTS.CATEGORIES}/${id}`);
    }

    async activateCategory(id) {
        return this.api.patch(`${FairCartConfig.ENDPOINTS.CATEGORIES}/${id}/activate`, {});
    }
}

const categoriesApi = new CategoriesApi(api);