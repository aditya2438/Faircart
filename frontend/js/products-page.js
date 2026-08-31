/** FairCart Products Page - Product listing functionality */

// State
let currentPage = 0;
const pageSize = 12;
let currentFilters = {
    keyword: '',
    categoryId: '',
    minPrice: '',
    maxPrice: '',
    minScore: '',
    sort: 'createdAt,desc'
};
let currentViewMode = 'grid';

// Initialize
document.addEventListener('DOMContentLoaded', async () => {
    // Load categories for filter
    await loadCategories();
    
    // Check URL params for search
    const urlParams = new URLSearchParams(window.location.search);
    const searchParam = urlParams.get('search');
    if (searchParam) {
        document.getElementById('searchInput').value = searchParam;
        currentFilters.keyword = searchParam;
    }
    
    // Load initial products
    await loadProducts();
});

// Load categories for filter dropdown
async function loadCategories() {
    try {
        const response = await categoriesApi.getActiveCategories();
        if (response.success && response.data) {
            const select = document.getElementById('categoryFilter');
            response.data.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
}

// Load products with current filters
async function loadProducts() {
    const grid = document.getElementById('productsGrid');
    const loading = document.getElementById('loadingSkeleton');
    
    if (!grid) return;
    
    // Show loading
    loading.style.display = 'block';
    grid.style.display = 'none';
    
    try {
        const params = {
            page: currentPage,
            size: pageSize,
            sort: currentFilters.sort
        };
        
        if (currentFilters.keyword) params.keyword = currentFilters.keyword;
        if (currentFilters.categoryId) params.categoryId = currentFilters.categoryId;
        if (currentFilters.minPrice) params.minPrice = currentFilters.minPrice;
        if (currentFilters.maxPrice) params.maxPrice = currentFilters.maxPrice;
        if (currentFilters.minScore) params.minIntelligenceScore = currentFilters.minScore;
        
        const response = await productsApi.filterProducts(params);
        
        loading.style.display = 'none';
        grid.style.display = 'grid';
        
        if (response.success && response.data && response.data.content) {
            renderProducts(response.data.content);
            updatePagination(response.data);
            updateResultsInfo(response.data);
        } else {
            grid.innerHTML = createEmptyState('No products found', 'Try adjusting your filters or search terms');
        }
    } catch (error) {
        console.error('Failed to load products:', error);
        loading.style.display = 'none';
        grid.style.display = 'grid';
        grid.innerHTML = createEmptyState('Unable to load products', 'Please check if the API is running');
    }
}

// Render products in grid
function renderProducts(products) {
    const grid = document.getElementById('productsGrid');
    if (!grid) return;
    
    if (products.length === 0) {
        grid.innerHTML = createEmptyState('No products found', 'Try adjusting your filters or search terms');
        return;
    }
    
    grid.innerHTML = products.map(product => createProductCard(product)).join('');
}

// Create product card HTML
function createProductCard(product) {
    const score = product.intelligenceScore || 0;
    const price = product.price ? new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(product.price) : '$0.00';
    const category = product.category?.name || 'Uncategorized';
    const imageUrl = product.imageUrl || 'https://via.placeholder.com/400x300?text=No+Image';
    
    return `
        <article class="product-card" onclick="location.href='product-detail.html?id=${product.id}'">
            <div style="position: relative;">
                <img src="${imageUrl}" alt="${product.name}" loading="lazy">
                <span class="intelligence-badge">${score}</span>
                ${score >= 80 ? '<span class="product-card-badge">SMART PICK</span>' : ''}
            </div>
            <div class="product-card-body">
                <p style="font-size: 0.75rem; color: var(--faircart-text-muted); text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.25rem;">${category}</p>
                <h3 class="product-card-title">${product.name}</h3>
                <div class="product-card-price">${price}</div>
                <div class="product-card-rating">
                    <svg class="star" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
                    <span class="count">4.5 (128)</span>
                </div>
            </div>
        </article>
    `;
}

// Create empty state HTML
function createEmptyState(title, message) {
    return `
        <div class="empty-state" style="grid-column: 1 / -1;">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
            </svg>
            <h3>${title}</h3>
            <p>${message}</p>
        </div>
    `;
}

// Update pagination
function updatePagination(pageData) {
    const pagination = document.getElementById('pagination');
    if (!pagination) return;
    
    const totalPages = pageData.totalPages || 1;
    const currentPageNum = pageData.number || 0;
    
    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }
    
    let html = '';
    
    // Previous button
    html += `<button class="btn btn-secondary" ${currentPageNum === 0 ? 'disabled' : ''} onclick="changePage(${currentPageNum - 1})"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="15 18 9 12 15 6"></polyline></svg></button>`;
    
    // Page numbers
    const startPage = Math.max(0, currentPageNum - 2);
    const endPage = Math.min(totalPages - 1, currentPageNum + 2);
    
    if (startPage > 0) {
        html += `<button class="btn btn-secondary" onclick="changePage(0)">1</button>`;
        if (startPage > 1) html += `<span style="display: flex; align-items: center; padding: 0 0.5rem; color: var(--faircart-text-muted);">...</span>`;
    }
    
    for (let i = startPage; i <= endPage; i++) {
        html += `<button class="btn ${i === currentPageNum ? 'btn-primary' : 'btn-secondary'}" onclick="changePage(${i})">${i + 1}</button>`;
    }
    
    if (endPage < totalPages - 1) {
        if (endPage < totalPages - 2) html += `<span style="display: flex; align-items: center; padding: 0 0.5rem; color: var(--faircart-text-muted);">...</span>`;
        html += `<button class="btn btn-secondary" onclick="changePage(${totalPages - 1})">${totalPages}</button>`;
    }
    
    // Next button
    html += `<button class="btn btn-secondary" ${currentPageNum === totalPages - 1 ? 'disabled' : ''} onclick="changePage(${currentPageNum + 1})"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"></polyline></svg></button>`;
    
    pagination.innerHTML = html;
}

// Update results info
function updateResultsInfo(pageData) {
    const info = document.getElementById('resultsInfo');
    if (!info) return;
    
    const total = pageData.totalElements || 0;
    const start = (pageData.number || 0) * (pageData.size || 12) + 1;
    const end = Math.min(start + (pageData.size || 12) - 1, total);
    
    if (total === 0) {
        info.textContent = 'No products found';
    } else {
        info.textContent = `Showing ${start}–${end} of ${total} products`;
    }
}

// Change page
function changePage(page) {
    currentPage = page;
    loadProducts();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Apply filters
function applyFilters() {
    currentFilters.categoryId = document.getElementById('categoryFilter').value || '';
    currentFilters.minPrice = document.getElementById('minPrice').value || '';
    currentFilters.maxPrice = document.getElementById('maxPrice').value || '';
    currentFilters.minScore = document.getElementById('minScoreFilter').value || '';
    currentFilters.sort = document.getElementById('sortFilter').value;
    currentPage = 0;
    loadProducts();
}

// Reset filters
function resetFilters() {
    document.getElementById('categoryFilter').value = '';
    document.getElementById('minPrice').value = '';
    document.getElementById('maxPrice').value = '';
    document.getElementById('minScoreFilter').value = '';
    document.getElementById('sortFilter').value = 'createdAt,desc';
    document.getElementById('searchInput').value = '';
    
    currentFilters = {
        keyword: '',
        categoryId: '',
        minPrice: '',
        maxPrice: '',
        minScore: '',
        sort: 'createdAt,desc'
    };
    currentPage = 0;
    loadProducts();
}

// Handle search
function handleSearch(event) {
    if (event.key === 'Enter') {
        const query = event.target.value.trim();
        currentFilters.keyword = query;
        currentPage = 0;
        loadProducts();
    }
}

// Set view mode
function setViewMode(mode) {
    currentViewMode = mode;
    const grid = document.getElementById('productsGrid');
    if (mode === 'list') {
        grid.style.gridTemplateColumns = '1fr';
    } else {
        grid.style.gridTemplateColumns = 'repeat(auto-fill, minmax(280px, 1fr))';
    }
    loadProducts();
}