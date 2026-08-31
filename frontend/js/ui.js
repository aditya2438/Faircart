/** FairCart UI Utilities - Common UI helper functions */
class UiUtils {
    static showToast(message, type = 'success', duration = 3000) {
        // Remove any existing toasts
        const existingToasts = document.querySelectorAll('.faircart-toast');
        existingToasts.forEach(toast => toast.remove());

        const toast = document.createElement('div');
        toast.className = `faircart toast toast-${type}`;
        toast.textContent = message;

        // Add toast styles if not present
        if (!document.getElementById('faircart-toast-styles')) {
            const style = document.createElement('style');
            style.id = 'faircart-toast-styles';
            style.textContent = `
                .faircart-toast-styles * { box-sizing: border-box; }
                .faircart-toast {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    padding: 12px 20px;
                    border-radius: 8px;
                    color: white;
                    font-weight: 500;
                    z-index: 9999;
                    animation: slideIn 0.3s ease-out;
                    min-width: 250px;
                    backdrop-filter: blur(10px);
                    background: rgba(0, 0, 0, 0.8);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                }
                .faircart-toast-success { box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
                .faircart-toast-error { box-shadow: 0 4px 12px rgba(255,0,0,0.2); }
                @keyframes slideIn {
                    from { transform: translateX(100%); opacity: 0; }
                    to { transform: translateX(0); opacity: 1; }
                }
                @keyframes slideOut {
                    from { transform: translateX(0); opacity: 1; }
                    to { transform: translateX(100%); opacity: 0; }
                }
            `;
            document.head.appendChild(style);
        }

        document.body.appendChild(toast);

        autoRemove(toast, duration);
    }

    static autoRemove(toast, duration) {
        setTimeout(() => {
            toast.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }

    static showLoading(container, message = 'Loading...') {
        container.innerHTML = `
            <div class="loading-skeleton">
                <div class="skeleton-row" style="height: 20px;"></div>
                <div class="skeleton-row" style="height: 20px; margin-top: 10px;"></div>
            </div>
        `;
        container.querySelector('.loading-skeleton')?.classList.add('visible');
    }

    static hideLoading(container) {
        const loading = container.querySelector('.loading-skeleton');
        if (loading) loading.classList.remove('visible');
    }

    static formatCurrency(amount) {
        if (typeof amount === 'number') {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
        }
        return amount;
    }

    static formatRating(rating) {
        if (typeof rating !== 'number') return 'No ratings';
        const filledStars = Math.floor(rating);
        const hasHalfStar = rating % 1 !== 0;
        const emptyStars = 5 - Math.ceil(rating);
        
        let stars = '';
        for (let i = 0; i < filledStars; i++) stars += '★';
        if (hasHalfStar) stars += '½';
        for (let i = 0; i < emptyStars; i++) stars += '☆';
        
        return `${rating} (${stars})`;
    }

    static debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    static throttle(func, limit) {
        let inThrottle;
        return function executedFunction(...args) {
            if (!inThrottle) {
                func(...args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }
}

export default UiUtils;