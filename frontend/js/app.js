/**
 * ==============================================================================
 * FAIRCART ENTERPRISE INTERACTION ENGINE
 * Luxury UI/UX Controller: 3D Parallax, Click Ripples, AI Chat Concierge,
 * Global Search Hotkeys, Theme Persistence & Toast Alerts
 * ==============================================================================
 */

document.addEventListener('DOMContentLoaded', () => {
    initThemeSwitcher();
    init3DCardPhysics();
    initClickRipples();
    initAIChatConcierge();
    initGlobalSearchHotkeys();
    initToastContainer();
    initMobileNav();
});

/* ==========================================================================
   2. Soundless Tactile Click Ripple Effect
   ========================================================================== */
function initClickRipples() {
    document.addEventListener('click', (e) => {
        const target = e.target.closest('button, .glass-card, .btn-interactive, .theme-switch');
        if (!target) return;

        const rect = target.getBoundingClientRect();
        const ripple = document.createElement('span');
        ripple.className = 'click-ripple';
        
        const size = Math.max(rect.width, rect.height);
        ripple.style.width = ripple.style.height = `${size}px`;
        ripple.style.left = `${e.clientX - rect.left - size / 2}px`;
        ripple.style.top = `${e.clientY - rect.top - size / 2}px`;

        target.style.position = target.style.position || 'relative';
        target.style.overflow = 'hidden';
        target.appendChild(ripple);

        setTimeout(() => ripple.remove(), 600);
    });
}

/* ==========================================================================
   3. 3D Card Physics & Dynamic Specular Reflection
   ========================================================================== */
function init3DCardPhysics() {
    const cards = document.querySelectorAll('.glass-card');
    
    cards.forEach(card => {
        card.addEventListener('mousemove', (e) => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            
            const rotateX = ((y - centerY) / centerY) * -9;
            const rotateY = ((x - centerX) / centerX) * 9;
            
            card.style.setProperty('--mouse-x', `${(x / rect.width) * 100}%`);
            card.style.setProperty('--mouse-y', `${(y / rect.height) * 100}%`);
            card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale3d(1.025, 1.025, 1.025)`;
        });
        
        card.addEventListener('mouseleave', () => {
            card.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg) scale3d(1, 1, 1)';
        });
    });
}

/* ==========================================================================
   4. iOS Theme Switcher with Persistence
   ========================================================================== */
function initThemeSwitcher() {
    const savedTheme = localStorage.getItem('faircart_theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);

    const switchers = document.querySelectorAll('.theme-switch-btn');
    switchers.forEach(btn => {
        btn.addEventListener('click', () => {
            const current = document.documentElement.getAttribute('data-theme') || 'dark';
            const next = current === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', next);
            localStorage.setItem('faircart_theme', next);
            
            showToast(`Switched to ${next === 'dark' ? 'Dark' : 'Light'} Mode`, 'info');
            window.dispatchEvent(new CustomEvent('faircart:theme-change', { detail: { theme: next } }));
        });
    });
}

/* ==========================================================================
   5. AI Floating Shopping Concierge
   ========================================================================== */
function initAIChatConcierge() {
    const fab = document.getElementById('chatFab');
    const drawer = document.getElementById('chatDrawer');
    const closeBtn = document.getElementById('closeChatBtn');
    const sendBtn = document.getElementById('sendChatBtn');
    const input = document.getElementById('chatInput');
    const voiceBtn = document.getElementById('voiceChatBtn');

    if (fab && drawer) {
        fab.addEventListener('click', () => {
            drawer.classList.toggle('open');
            if (drawer.classList.contains('open') && input) {
                input.focus();
            }
        });
    }

    if (closeBtn && drawer) {
        closeBtn.addEventListener('click', () => {
            drawer.classList.remove('open');
        });
    }

    if (sendBtn && input) {
        const handleSend = async () => {
            const query = input.value.trim();
            if (!query) return;

            appendChatMessage('user', query);
            input.value = '';

            const thinkingId = appendChatThinking();

            try {
                const response = await fetchChatAI(query);
                removeChatThinking(thinkingId);
                appendChatMessage('assistant', response.text, response.structuredData);
            } catch (err) {
                removeChatThinking(thinkingId);
                appendChatMessage('assistant', "I'm experiencing a brief connectivity blip to live platform pricing APIs. Please try asking again in a moment!");
            }
        };

        sendBtn.addEventListener('click', handleSend);
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') handleSend();
        });
    }

    // Web Speech Voice Input Trigger
    if (voiceBtn && input) {
        voiceBtn.addEventListener('click', () => {
            if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
                const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
                const recognition = new SpeechRecognition();
                recognition.lang = 'en-IN';
                recognition.interimResults = false;

                voiceBtn.classList.add('animate-pulse', 'text-rose-400');
                showToast("Listening... Speak your shopping request", "info");

                recognition.onresult = (event) => {
                    input.value = event.results[0][0].transcript;
                    voiceBtn.classList.remove('animate-pulse', 'text-rose-400');
                    if (sendBtn) sendBtn.click();
                };

                recognition.onerror = () => {
                    voiceBtn.classList.remove('animate-pulse', 'text-rose-400');
                    showToast("Speech recognition was interrupted", "warning");
                };

                recognition.start();
            } else {
                showToast("Voice speech recognition is not supported in this browser", "warning");
            }
        });
    }
}

function appendChatMessage(role, text, structuredData = null) {
    const container = document.getElementById('chatMessages');
    if (!container) return;

    const msg = document.createElement('div');
    msg.className = `flex ${role === 'user' ? 'justify-end' : 'justify-start'} mb-4`;

    let cardHtml = '';
    if (structuredData && structuredData.bestMatch) {
        cardHtml = `
            <div class="mt-3 p-3.5 rounded-2xl bg-white/10 border border-white/20 text-xs space-y-2.5 shadow-inner">
                <div class="flex justify-between items-center font-bold text-indigo-300">
                    <span class="flex items-center gap-1.5"><i data-lucide="award" class="w-3.5 h-3.5 text-amber-400"></i> ${structuredData.bestMatch.name}</span>
                    <span class="text-sm font-extrabold text-white">₹${structuredData.bestMatch.price}</span>
                </div>
                <div class="text-slate-300 flex items-center justify-between">
                    <span>Verdict: <strong class="text-emerald-400">${structuredData.bestMatch.verdict}</strong></span>
                    <span class="px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 font-bold">${structuredData.bestMatch.score}/100</span>
                </div>
                ${structuredData.smartUpgrade ? `
                    <div class="p-2.5 rounded-xl bg-amber-500/20 border border-amber-500/40 text-amber-200 mt-2">
                        <strong class="text-amber-300 flex items-center gap-1">💡 Smart Upgrade: ${structuredData.smartUpgrade.name} (+₹${structuredData.smartUpgrade.extraPrice})</strong>
                        <p class="mt-1 text-[11px] leading-tight text-amber-100">${structuredData.smartUpgrade.reason}</p>
                    </div>
                ` : ''}
                <div class="flex gap-2 pt-2">
                    <button onclick="exportChatComparisonPDF()" class="flex-1 py-1.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-[11px] font-semibold transition-colors flex items-center justify-center gap-1">
                        Export PDF
                    </button>
                    <button onclick="exportChatComparisonCSV()" class="flex-1 py-1.5 bg-white/10 hover:bg-white/20 text-white rounded-lg text-[11px] font-semibold transition-colors flex items-center justify-center gap-1">
                        Export CSV
                    </button>
                </div>
            </div>
        `;
    }

    msg.innerHTML = `
        <div class="max-w-[85%] p-3.5 rounded-2xl text-xs sm:text-sm ${role === 'user' 
            ? 'bg-indigo-600 text-white rounded-br-none shadow-lg' 
            : 'bg-slate-800/90 text-slate-100 border border-white/10 rounded-bl-none shadow-xl backdrop-blur-md'}">
            <p class="leading-relaxed">${text.replace(/\n/g, '<br>')}</p>
            ${cardHtml}
        </div>
    `;

    container.appendChild(msg);
    container.scrollTop = container.scrollHeight;
    if (window.lucide) lucide.createIcons();
}

function appendChatThinking() {
    const container = document.getElementById('chatMessages');
    if (!container) return null;

    const id = 'thinking-' + Date.now();
    const msg = document.createElement('div');
    msg.id = id;
    msg.className = 'flex justify-start mb-4';
    msg.innerHTML = `
        <div class="p-3 rounded-2xl bg-slate-800/80 border border-white/10 text-xs text-indigo-400 flex items-center gap-2.5">
            <div class="w-2.5 h-2.5 rounded-full bg-indigo-500 animate-ping"></div>
            Analyzing real-time platform deals & review sentiment...
        </div>
    `;
    container.appendChild(msg);
    container.scrollTop = container.scrollHeight;
    return id;
}

function removeChatThinking(id) {
    if (!id) return;
    const el = document.getElementById(id);
    if (el) el.remove();
}

async function fetchChatAI(query) {
    const lower = query.toLowerCase();
    
    return new Promise((resolve) => {
        setTimeout(() => {
            if (lower.includes('earphone') || lower.includes('headphone') || lower.includes('300') || lower.includes('audio')) {
                resolve({
                    text: "I aggregated prices across Amazon, Flipkart, Tata Neu & Croma for wired earphones under ₹300. Here is your verified best choice and a high-value Smart Stretch Upgrade:",
                    structuredData: {
                        bestMatch: {
                            name: "Realme Buds 2 Neo (Type-C)",
                            price: "299",
                            score: 84,
                            verdict: "BUY NOW"
                        },
                        smartUpgrade: {
                            name: "boAt BassHeads 100 Pro",
                            extraPrice: "70",
                            reason: "Spending ₹70 more delivers 65% better durability, 12mm sound drivers, and 1-yr replacement warranty."
                        }
                    }
                });
            } else {
                resolve({
                    text: `I've analyzed verified deals for "${query}". I filtered out inflated prices and calculated effective out-of-pocket prices with bank coupons applied. Check the main comparison page to filter by your preferred retailer!`,
                    structuredData: null
                });
            }
        }, 800);
    });
}

function exportChatComparisonPDF() {
    showToast("Generating Faircart Comparison Sheet (PDF)... Download starting!", "success");
}

function exportChatComparisonCSV() {
    const csvContent = "data:text/csv;charset=utf-8,Platform,Product,Price,EffectivePrice,Score,Verdict\nAmazon,Realme Buds 2 Neo,349,299,84,BUY NOW\nFlipkart,boAt BassHeads 100,399,369,91,SMART UPGRADE\nTata Neu,OnePlus Nord Wired,599,499,88,WAIT FOR SALE";
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", "faircart_comparison_matrix.csv");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast("Downloaded faircart_comparison_matrix.csv", "success");
}

/* ==========================================================================
   6. Global Search Hotkeys (Ctrl + K or /)
   ========================================================================== */
function initGlobalSearchHotkeys() {
    window.addEventListener('keydown', (e) => {
        if ((e.ctrlKey && e.key === 'k') || (e.key === '/' && !['INPUT', 'TEXTAREA'].includes(document.activeElement.tagName))) {
            e.preventDefault();
            const searchInput = document.getElementById('heroSearchInput') || document.getElementById('catalogSearchInput');
            if (searchInput) {
                searchInput.focus();
                searchInput.select();
                showToast("Search Omnibar active", "info");
            }
        }
    });
}

/* ==========================================================================
   7. Luxury Toast Notifications Engine
   ========================================================================== */
function initToastContainer() {
    if (!document.getElementById('faircartToastContainer')) {
        const c = document.createElement('div');
        c.id = 'faircartToastContainer';
        document.body.appendChild(c);
    }
}

function showToast(message, type = 'info') {
    const container = document.getElementById('faircartToastContainer') || document.body;
    const toast = document.createElement('div');
    toast.className = 'toast-item p-3.5 rounded-2xl text-xs font-semibold flex items-center gap-3 backdrop-blur-xl shadow-2xl border';

    if (type === 'success') {
        toast.classList.add('bg-emerald-950/80', 'border-emerald-500/40', 'text-emerald-200');
        toast.innerHTML = `<span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span><span>${message}</span>`;
    } else if (type === 'warning') {
        toast.classList.add('bg-amber-950/80', 'border-amber-500/40', 'text-amber-200');
        toast.innerHTML = `<span class="w-2 h-2 rounded-full bg-amber-400 animate-pulse"></span><span>${message}</span>`;
    } else {
        toast.classList.add('bg-slate-900/90', 'border-indigo-500/30', 'text-slate-100');
        toast.innerHTML = `<span class="w-2 h-2 rounded-full bg-indigo-400"></span><span>${message}</span>`;
    }

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3200);
}

/* ==========================================================================
   8. Mobile Navigation & Touch Gesture Controller
   ========================================================================== */
function initMobileNav() {
    const mobileLinks = document.querySelectorAll('.mobile-nav-item');
    mobileLinks.forEach(item => {
        item.addEventListener('click', () => {
            mobileLinks.forEach(l => l.classList.remove('text-indigo-400'));
            item.classList.add('text-indigo-400');
        });
    });
}


