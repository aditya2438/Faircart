/** FairCart Wishlist API - Wishlist related API calls */
class WishlistApi {
    constructor(apiClient) {
        this.api = apiClient;
    }

    async getWishlist() {
        return this.api.get(FairCartConfig.ENDPOINTS.WISHLIST);
    }

    async addToWishlist(productId) {
        return this.api.post(`${FairCartConfig.ENDPOINTS.WISHLIST}/add/${productId}`, {});
    }

    async removeFromWishlist(productId) {
        return this.api.delete(`${FairCartConfig.ENDPOINTS.WISHLIST}/remove/${productId}`);
    }

    async checkWishlist(productId) {
        return this.api.get(`${FairCartConfig.ENDPOINTS.WISHLIST}/check/${productId}`);
    }

    async getWishlistCount() {
        return this.api.get(`${FairCartConfig.ENDPOINTS.WISHLIST}/count`);
    }
}

const wishlistApi = new WishlistApi(api);