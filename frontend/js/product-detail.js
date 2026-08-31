/**
 * FairCart Product Detail Page - Price History Charts & AI Chat
 * Handles product data loading, Chart.js price history visualization, and AI chat interactions
 */

document.addEventListener('DOMContentLoaded', async () => {
    // Initialize product detail page
    await loadProductDetail();
    initializeTabs();
    initializeQuantityControls();
    initializeWishlist();
    initializeAIChat();
});

// ============================================
// PRODUCT DATA LOADING
// ============================================

let currentProduct = null;
let priceHistoryChart = null;

async function loadProductDetail() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    
    if (!productId) {
        showToast('Product ID not found', 'error');
        window.location.href = 'products.html';
        return;
    }

    try {
        const response = await productsApi.getProductById(productId);
        if (response.success && response.data) {
            currentProduct = response.data;
            renderProductDetail(currentProduct);
            
            // Load price history for chart
            await loadPriceHistory(productId);
            
            // Load related products
            await loadRelatedProducts(currentProduct.category?.id || currentProduct.categoryId);
        } else {
            showToast('Product not found', 'error');
            window.location.href = 'products.html';
        }
    } catch (error) {
        console.error('Failed to load product:', error);
        showToast('Failed to load product details', 'error');
    }
}

function renderProductDetail(product) {
    // Update page title
    document.title = `${product.name} — FairCart`;
    document.getElementById('breadcrumbProduct').textContent = product.name;
    
    // Product basics
    document.getElementById('productCategory').textContent = product.category?.name || 'Category';
    document.getElementById('productName').textContent = product.name;
    document.getElementById('productDescription').textContent = product.description || 'No description available';
    
    // Price
    document.getElementById('productPrice').textContent = formatCurrency(product.price);
    
    // Intelligence Score
    const score = product.intelligenceScore || 0;
    document.getElementById('productScore').textContent = score;
    document.getElementById('productScore').style.background = getScoreColor(score);
    
    // Rating
    document.getElementById('productRating').textContent = product.ratingAvg || '4.5';
    document.getElementById('productReviewCount').textContent = `(${product.totalReviews || product.reviewCount || 0} reviews)`;
    document.getElementById('reviewCount').textContent = product.totalReviews || product.reviewCount || 0;
    
    // Image
    const mainImg = document.getElementById('mainImg');
    mainImg.src = product.imageUrl || 'https://via.placeholder.com/600x600?text=No+Image';
    mainImg.alt = product.name;
    
    // Intelligence Score Breakdown
    renderScoreBreakdown(product);
    
    // Specifications
    renderSpecifications(product);
    
    // Stock status
    renderStockStatus(product);
    
    // Thumbnails (placeholder - would load multiple images in real app)
    renderThumbnails(product);
}

function renderScoreBreakdown(product) {
    const score = product.intelligenceScore || 0;
    const breakdown = calculateScoreBreakdown(product, score);
    
    const scores = [
        { key: 'price', label: 'Price Competitiveness', max: 30, color: 'var(--faircart-primary)' },
        { key: 'reviews', label: 'Review Sentiment', max: 30, color: 'var(--faircart-accent)' },
        { key: 'seller', label: 'Seller Trust', max: 25, color: 'var(--faircart-warning)' },
        { key: 'stock', label: 'Stock Reliability', max: 15, color: 'var(--faircart-success)' }
    ];
    
    const container = document.getElementById('scoreBreakdown');
    if (!container) return;
    
    container.innerHTML = scores.map(s => `
        <div style="display: flex; justify-content: space-between; align-items: center; padding: 0.5rem 0;">
            <span style="color: var(--faircart-text-muted);">${s.label}</span>
            <div style="display: flex; align-items: center; gap: 0.75rem;">
                <div style="flex: 1; height: 6px; background: var(--faircart-border); border-radius: 3px; overflow: hidden;">
                    <div class="score-bar" data-score="${s.key}" style="height: 100%; background: ${s.color}; border-radius: 3px; width: ${(breakdown[s.key] / s.max) * 100}%; transition: width 0.5s ease;"></div>
                </div>
                <span class="score-value" data-score="${s.key}" style="font-weight: 600; min-width: 40px;">${breakdown[s.key]}/${s.max}</span>
            </div>
        </div>
    `).join('');
}

function calculateScoreBreakdown(product, totalScore) {
    // Simplified breakdown calculation
    const priceScore = Math.min(30, Math.round((product.priceScore || 25)));
    const reviewScore = Math.min(30, Math.round((product.ratingScore || 25)));
    const sellerScore = Math.min(25, Math.round((product.sellerScore || 20)));
    const stockScore = Math.min(15, Math.round((product.availabilityScore || 10)));
    
    // Normalize to match total
    const sum = priceScore + reviewScore + sellerScore + stockScore;
    if (sum > 0 && sum !== totalScore) {
        const ratio = totalScore / sum;
        return {
            price: Math.round(priceScore * ratio),
            reviews: Math.round(reviewScore * ratio),
            seller: Math.round(sellerScore * ratio),
            stock: Math.round(stockScore * ratio)
        };
    }
    return { price: priceScore, reviews: reviewScore, seller: sellerScore, stock: stockScore };
}

function renderSpecifications(product) {
    const container = document.getElementById('specifications');
    if (!container) return;
    
    const specs = product.specifications || product.specification_json || {};
    const specEntries = Object.entries(specs);
    
    if (specEntries.length === 0) {
        // Default specs for demo
        const defaultSpecs = [
            ['Brand', product.brand || 'FairCart Select'],
            ['Category', product.category?.name || 'Electronics'],
            ['Model', product.model || 'FC-' + product.id],
            ['Warranty', '1 Year Manufacturer Warranty'],
            ['Color', product.color || 'Black'],
            ['Weight', product.weight || '0.5 kg'],
            ['Dimensions', product.dimensions || '10 x 5 x 2 cm'],
            ['Connectivity', product.connectivity || 'Bluetooth 5.0, USB-C']
        ];
        
        container.innerHTML = defaultSpecs.map(([key, value]) => `
            <div style="padding: 0.75rem; background: var(--faircart-surface); border-radius: 8px;">
                <span style="font-size: 0.75rem; color: var(--faircart-text-muted); text-transform: uppercase; letter-spacing: 0.5px;">${key}</span>
                <div style="font-weight: 500; margin-top: 0.25rem;">${value}</div>
            </div>
        `).join('');
    } else {
        container.innerHTML = specEntries.map(([key, value]) => `
            <div style="padding: 0.75rem; background: var(--faircart-surface); border-radius: 8px;">
                <span style="font-size: 0.75rem; color: var(--faircart-text-muted); text-transform: uppercase; letter-spacing: 0.5px;">${key}</span>
                <div style="font-weight: 500; margin-top: 0.25rem;">${value}</div>
            </div>
        `).join('');
    }
}

function renderStockStatus(product) {
    const stockIcon = document.getElementById('stockIcon');
    const stockText = document.getElementById('stockText');
    const stockStatus = document.getElementById('stockStatus');
    
    if (!stockStatus) return;
    
    const inStock = product.stockQuantity > 0;
    const quantity = product.stockQuantity || 0;
    
    if (inStock) {
        stockIcon.innerHTML = '<circle cx="12" cy="12" r="10"></circle><path d="M12 6v6l4 2"></path>';
        stockIcon.style.color = 'var(--faircart-success)';
        stockText.textContent = `In Stock (${quantity} available)`;
        stockStatus.style.background = 'rgba(74, 222, 128, 0.1)';
        stockStatus.style.border = '1px solid rgba(74, 222, 128, 0.3)';
    } else {
        stockIcon.innerHTML = '<circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line>';
        stockIcon.style.color = 'var(--faircart-danger)';
        stockText.textContent = 'Out of Stock';
        stockStatus.style.background = 'rgba(248, 113, 113, 0.1)';
        stockStatus.style.border = '1px solid rgba(248, 113, 113, 0.3)';
    }
}

function renderThumbnails(product) {
    const container = document.getElementById('thumbnails');
    if (!container) return;
    
    // In a real app, this would load multiple product images
    // For now, show the main image as a thumbnail
    container.innerHTML = `
        <img src="${product.imageUrl || 'https://via.placeholder.com/100x100'}" 
             alt="${product.name}" 
             style="width: 80px; height: 80px; border-radius: 8px; object-fit: cover; cursor: pointer; border: 2px solid var(--faircart-primary);"
             onclick="changeMainImage(this.src)">
    `;
}

function changeMainImage(src) {
    const mainImg = document.getElementById('mainImg');
    mainImg.src = src;
    
    // Update thumbnail borders
    document.querySelectorAll('#thumbnails img').forEach(img => {
        img.style.borderColor = img.src === src ? 'var(--faircart-primary)' : 'transparent';
    });
}

// ============================================
// PRICE HISTORY CHART
// ============================================

async function loadPriceHistory(productId) {
    try {
        const response = await productsApi.getPriceHistory(productId);
        if (response.success && response.data && response.data.length > 0) {
            renderPriceHistoryChart(response.data);
        } else {
            showNoPriceHistoryData();
        }
    } catch (error) {
        console.error('Failed to load price history:', error);
        showNoPriceHistoryData();
    }
}

function renderPriceHistoryChart(historyData) {
    const ctx = document.getElementById('priceHistoryChart');
    if (!ctx) return;
    
    // Sort by date
    historyData.sort((a, b) => new Date(a.recordedAt || a.changedAt) - new Date(b.recordedAt || b.changedAt));
    
    const labels = historyData.map(item => {
        const date = new Date(item.recordedAt || item.changedAt);
        return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    });
    
    const prices = historyData.map(item => item.price || item.newPrice || item.currentPrice);
    const originalPrices = historyData.map(item => item.originalPrice || item.price);
    
    // Destroy existing chart
    if (priceHistoryChart) {
        priceHistoryChart.destroy();
    }
    
    priceHistoryChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Current Price',
                    data: prices,
                    borderColor: 'var(--faircart-primary)',
                    backgroundColor: 'rgba(108, 99, 255, 0.1)',
                    fill: true,
                    tension: 0.4,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                    pointBackgroundColor: 'var(--faircart-primary)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2
                },
                {
                    label: 'Original Price',
                    data: originalPrices,
                    borderColor: 'var(--faircart-text-muted)',
                    backgroundColor: 'transparent',
                    fill: false,
                    tension: 0.4,
                    pointRadius: 3,
                    pointHoverRadius: 5,
                    borderDash: [5, 5],
                    pointBackgroundColor: 'var(--faircart-text-muted)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        color: 'var(--faircart-text)',
                        font: { family: 'Inter', size: 12 },
                        usePointStyle: true,
                        padding: 20
                    }
                },
                tooltip: {
                    backgroundColor: 'var(--faircart-surface)',
                    titleColor: 'var(--faircart-text)',
                    bodyColor: 'var(--faircart-text-muted)',
                    borderColor: 'var(--faircart-border)',
                    borderWidth: 1,
                    padding: 12,
                    displayColors: true,
                    callbacks: {
                        label: function(context) {
                            return `${context.dataset.label}: ${formatCurrency(context.raw)}`;
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: { color: 'rgba(255,255,255,0.05)' },
                    ticks: { color: 'var(--faircart-text-muted)', font: { family: 'Inter', size: 11 } }
                },
                y: {
                    grid: { color: 'rgba(255,255,255,0.05)' },
                    ticks: { 
                        color: 'var(--faircart-text-muted)', 
                        font: { family: 'Inter', size: 11 },
                        callback: function(value) { return formatCurrency(value); }
                    }
                }
            },
            animation: {
                duration: 1000,
                easing: 'easeOutQuart'
            }
        }
    });
    
    // Update price stats
    updatePriceStats(historyData);
}

function updatePriceStats(historyData) {
    const prices = historyData.map(item => item.price || item.newPrice || item.currentPrice);
    const currentPrice = prices[prices.length - 1];
    const lowestPrice = Math.min(...prices);
    const highestPrice = Math.max(...prices);
    const avgPrice = prices.reduce((a, b) => a + b, 0) / prices.length;
    
    const statsContainer = document.getElementById('priceStats');
    if (statsContainer) {
        statsContainer.innerHTML = `
            <div class="stat-item">
                <span class="stat-label">Current</span>
                <span class="stat-value">${formatCurrency(currentPrice)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Lowest</span>
                <span class="stat-value" style="color: var(--faircart-success);">${formatCurrency(lowestPrice)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Highest</span>
                <span class="stat-value" style="color: var(--faircart-danger);">${formatCurrency(highestPrice)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Average</span>
                <span class="stat-value">${formatCurrency(avgPrice)}</span>
            </div>
        `;
    }
}

function showNoPriceHistoryData() {
    const container = document.getElementById('tab-price-history');
    if (container) {
        container.querySelector('.tab-content').innerHTML = `
            <div class="glass" style="padding: 3rem; text-align: center;">
                <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="var(--faircart-text-muted)" stroke-width="1.5" style="margin-bottom: 1rem;">
                    <line x1="18" y1="20" x2="18" y2="10"></line>
                    <line x1="12" y1="20" x2="12" y2="4"></line>
                    <line x1="6" y1="20" x2="6" y2="14"></line>
                </svg>
                <h3 style="margin-bottom: 0.5rem;">No Price History Available</h3>
                <p style="color: var(--faircart-text-muted);">Price tracking data will appear here once we have enough data points.</p>
            </div>
        `;
    }
}

// ============================================
// TABS
// ============================================

function initializeTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const tabId = btn.dataset.tab;
            
            // Update buttons
            tabButtons.forEach(b => {
                b.classList.remove('active');
                b.style.borderBottomColor = 'transparent';
                b.style.color = 'var(--faircart-text-muted)';
            });
            btn.classList.add('active');
            btn.style.borderBottomColor = 'var(--faircart-primary)';
            btn.style.color = 'var(--faircart-primary)';
            
            // Update contents
            tabContents.forEach(content => {
                content.style.display = content.id === `tab-${tabId}` ? 'block' : 'none';
            });
            
            // Load price history chart when tab is activated
            if (tabId === 'price-history' && !priceHistoryChart && currentProduct) {
                loadPriceHistory(currentProduct.id);
            }
        });
    });
}

// ============================================
// QUANTITY CONTROLS
// ============================================

function initializeQuantityControls() {
    window.changeQuantity = function(delta) {
        const input = document.getElementById('quantityInput');
        if (!input) return;
        
        let value = parseInt(input.value) + delta;
        const max = parseInt(input.max) || 99;
        const min = parseInt(input.min) || 1;
        
        value = Math.max(min, Math.min(max, value));
        input.value = value;
    };
}

// ============================================
// WISHLIST
// ============================================

function initializeWishlist() {
    const btn = document.getElementById('wishlistBtn');
    if (!btn) return;
    
    // Check if in wishlist
    checkWishlistStatus();
    
    btn.addEventListener('click', async () => {
        if (!currentProduct) return;
        
        const isInWishlist = btn.classList.contains('in-wishlist');
        
        try {
            if (isInWishlist) {
                await wishlistApi.removeFromWishlist(currentProduct.id);
                btn.classList.remove('in-wishlist');
                document.getElementById('wishlistText').textContent = 'Add to Wishlist';
                document.getElementById('wishlistIcon').innerHTML = '<path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>';
                showToast('Removed from wishlist');
            } else {
                await wishlistApi.addToWishlist(currentProduct.id);
                btn.classList.add('in-wishlist');
                document.getElementById('wishlistText').textContent = 'In Wishlist';
                document.getElementById('wishlistIcon').innerHTML = '<path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" fill="currentColor" stroke="none"></path>';
                showToast('Added to wishlist!');
            }
        } catch (error) {
            console.error('Wishlist error:', error);
            showToast('Failed to update wishlist', 'error');
        }
    });
}

async function checkWishlistStatus() {
    if (!currentProduct || !authApi.isAuthenticated()) return;
    
    try {
        const response = await wishlistApi.checkWishlist(currentProduct.id);
        if (response.success && response.data) {
            const btn = document.getElementById('wishlistBtn');
            if (btn) {
                btn.classList.add('in-wishlist');
                document.getElementById('wishlistText').textContent = 'In Wishlist';
                document.getElementById('wishlistIcon').innerHTML = '<path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" fill="currentColor" stroke="none"></path>';
            }
        }
    } catch (error) {
        console.log('Wishlist check failed:', error);
    }
}

function addToCart() {
    const quantity = parseInt(document.getElementById('quantityInput').value) || 1;
    // In a real app, this would call the cart API
    showToast(`Added ${quantity} item(s) to cart!`);
}

// ============================================
// AI CHAT INTERFACE
// ============================================

function initializeAIChat() {
    const chatToggle = document.getElementById('chatToggle');
    const chatWindow = document.getElementById('chatWindow');
    const chatClose = document.getElementById('chatClose');
    const chatForm = document.getElementById('chatForm');
    const chatInput = document.getElementById('chatInput');
    const suggestionChips = document.querySelectorAll('.suggestion-chip');
    
    if (!chatToggle || !chatWindow) return;
    
    // Toggle chat
    chatToggle.addEventListener('click', () => {
        const isOpen = chatWindow.style.display !== 'none';
        chatWindow.style.display = isOpen ? 'none' : 'flex';
        chatToggle.classList.toggle('active', !isOpen);
        chatToggle.setAttribute('aria-expanded', !isOpen);
        
        if (!isOpen) {
            chatInput.focus();
            // Hide notification
            document.getElementById('chatNotification').style.display = 'none';
        }
    });
    
    // Close chat
    chatClose.addEventListener('click', () => {
        chatWindow.style.display = 'none';
        chatToggle.classList.remove('active');
        chatToggle.setAttribute('aria-expanded', 'false');
    });
    
    // Close on outside click
    document.addEventListener('click', (e) => {
        if (!chatWindow.contains(e.target) && !chatToggle.contains(e.target) && chatWindow.style.display !== 'none') {
            chatWindow.style.display = 'none';
            chatToggle.classList.remove('active');
            chatToggle.setAttribute('aria-expanded', 'false');
        }
    });
    
    // Handle form submission
    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const message = chatInput.value.trim();
        if (!message) return;
        
        await sendChatMessage(message);
        chatInput.value = '';
    });
    
    // Handle suggestion chips
    suggestionChips.forEach(chip => {
        chip.addEventListener('click', () => {
            const prompt = chip.dataset.prompt;
            chatInput.value = prompt;
            sendChatMessage(prompt);
        });
    });
    
    // Handle Enter key
    chatInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            chatForm.dispatchEvent(new Event('submit'));
        }
    });
}

async function sendChatMessage(message) {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return;
    
    // Add user message
    addChatMessage(message, 'user');
    
    // Show typing indicator
    const typingId = showTypingIndicator();
    
    try {
        // In a real app, this would call the AI chat API
        // For now, simulate AI response
        await simulateAIResponse(message);
    } catch (error) {
        console.error('Chat error:', error);
        removeTypingIndicator(typingId);
        addChatMessage('Sorry, I encountered an error. Please try again.', 'ai');
    }
}

function addChatMessage(content, type) {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return;
    
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type === 'user' ? 'user-message' : 'ai-message'}`;
    messageDiv.innerHTML = `
        <div class="message-avatar">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                ${type === 'user' 
                    ? '<circle cx="12" cy="12" r="3"></circle><path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41"></path>'
                    : '<rect x="2" y="3" width="20" height="14" rx="2"></rect><path d="M8 21h8"></path><path d="M12 17v4"></path>'
                }
            </svg>
        </div>
        <div class="message-content">
            ${content}
        </div>
    `;
    
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showTypingIndicator() {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return null;
    
    const typingDiv = document.createElement('div');
    typingDiv.className = 'message ai-message typing-indicator';
    typingDiv.innerHTML = `
        <div class="message-avatar">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="3" width="20" height="14" rx="2"></rect>
                <path d="M8 21h8"></path>
                <path d="M12 17v4"></path>
            </svg>
        </div>
        <div class="message-content" style="padding: 1rem;">
            <div class="typing-dots">
                <span></span>
                <span></span>
                <span></span>
            </div>
        </div>
    `;
    
    chatMessages.appendChild(typingDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
    
    // Add typing animation styles if not present
    if (!document.getElementById('typing-styles')) {
        const style = document.createElement('style');
        style.id = 'typing-styles';
        style.textContent = `
            .typing-dots {
                display: flex;
                gap: 4px;
            }
            .typing-dots span {
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background: var(--faircart-text-muted);
                animation: typing 1.4s infinite ease-in-out;
            }
            .typing-dots span:nth-child(2) { animation-delay: 0.2s; }
            .typing-dots span:nth-child(3) { animation-delay: 0.4s; }
            @keyframes typing {
                0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
                30% { transform: translateY(-6px); opacity: 1; }
            }
        `;
        document.head.appendChild(style);
    }
    
    return typingDiv;
}

function removeTypingIndicator(typingDiv) {
    if (typingDiv && typingDiv.parentNode) {
        typingDiv.parentNode.removeChild(typingDiv);
    }
}

async function simulateAIResponse(userMessage) {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 1500));
    
    const typingIndicator = document.querySelector('.typing-indicator');
    if (typingIndicator) removeTypingIndicator(typingIndicator);
    
    // Generate contextual response
    const response = generateAIResponse(userMessage);
    addChatMessage(response, 'ai');
}

function generateAIResponse(message) {
    const lower = message.toLowerCase();
    
    // Product-specific responses
    if (currentProduct && (lower.includes('price history') || lower.includes('price trend') || lower.includes('price chart'))) {
        return `Here's the price history for **${currentProduct.name}**! The chart above shows how the price has fluctuated over time. 

**Key Insights:**
• Current price: ${formatCurrency(currentProduct.price)}
• The chart compares current vs original prices
• Look for dips to identify the best time to buy

Would you like me to set up a price drop alert for this product?`;
    }
    
    if (currentProduct && (lower.includes('compare') || lower.includes('alternative') || lower.includes('similar'))) {
        return `I can help you compare **${currentProduct.name}** with similar products! 

Based on your interest, I'd recommend checking:
1. Products in the same category (${currentProduct.category?.name || 'this category'})
2. Items with similar intelligence scores
3. Products with better price-to-performance ratios

Would you like me to find specific alternatives or show you the comparison page?`;
    }
    
    if (currentProduct && (lower.includes('review') || lower.includes('rating') || lower.includes('sentiment'))) {
        return `Here's what I know about reviews for **${currentProduct.name}**:

**Overall Rating:** ${currentProduct.ratingAvg || '4.5'}/5 (${currentProduct.totalReviews || currentProduct.reviewCount || '128'} reviews)
**Intelligence Score:** ${currentProduct.intelligenceScore || 85}/100

**Truth Box Analysis:**
✅ **Top Pros:** Great value, reliable performance, good build quality
⚠️ **Top Cons:** Limited color options, average battery life

The sentiment analysis shows mostly positive feedback with high authenticity scores.`;
    }
    
    if (lower.includes('budget') || lower.includes('under') || lower.includes('cheap') || lower.includes('affordable')) {
        return `I can help you find products within your budget! 

To give you the best recommendations, could you tell me:
1. What type of product are you looking for?
2. What's your maximum budget?
3. Any specific features you need?

For example: *"Wireless earbuds under $100 with good battery life"*`;
    }
    
    if (lower.includes('smart stretch') || lower.includes('upgrade') || lower.includes('worth it')) {
        return `The **Smart Stretch** feature analyzes whether spending a bit more gets you significantly better value!

For example, if you're looking at earbuds at $80, but there's a $95 pair with:
• 2x better noise cancellation
• Premium brand warranty
• Superior audio drivers

The system will highlight this as a "Smart Upgrade" because the value jump justifies the extra $15.

Want me to check for Smart Stretch options for a specific product?`;
    }
    
    // Default responses
    const responses = [
        `Great question! I can help you with product comparisons, price analysis, review insights, and finding the best deals. What specific product or category are you interested in?`,
        `I'm here to make your shopping smarter! Ask me about:
• Price history & trends for any product
• Cross-platform price comparisons
• Review sentiment & fake review detection
• Smart Stretch upgrade recommendations
• Price drop alerts

What are you shopping for today?`,
        `Happy to help! Whether you're looking for the best value, want to avoid fake sales, or need a product comparison, I've got you covered. 

Try asking: *"Show me price history for this product"* or *"Compare wireless headphones under $200"*`
    ];
    
    return responses[Math.floor(Math.random() * responses.length)];
}

// ============================================
// RELATED PRODUCTS
// ============================================

async function loadRelatedProducts(categoryId) {
    const grid = document.getElementById('relatedProductsGrid');
    if (!grid) return;
    
    try {
        const response = await productsApi.getProductsByCategory(categoryId, { page: 0, size: 4, sort: 'intelligenceScore,desc' });
        if (response.success && response.data && response.data.content) {
            const products = response.data.content.filter(p => p.id !== currentProduct?.id).slice(0, 4);
            renderProductGrid(products, grid);
        }
    } catch (error) {
        console.error('Failed to load related products:', error);
        grid.innerHTML = '<p style="color: var(--faircart-text-muted); text-align: center; grid-column: 1/-1;">Unable to load related products</p>';
    }
}

// ============================================
// UTILITY FUNCTIONS
// ============================================

function getScoreColor(score) {
    if (score >= 80) return 'linear-gradient(135deg, var(--faircart-success), #059669)';
    if (score >= 60) return 'linear-gradient(135deg, var(--faircart-primary), var(--faircart-accent))';
    if (score >= 40) return 'linear-gradient(135deg, var(--faircart-warning), #d97706)';
    return 'linear-gradient(135deg, var(--faircart-danger), #dc2626)';
}

function formatCurrency(amount) {
    if (typeof amount === 'number') {
        return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
    }
    return amount;
}

// Export for global access
window.loadPriceHistory = loadPriceHistory;
window.renderPriceHistoryChart = renderPriceHistoryChart;