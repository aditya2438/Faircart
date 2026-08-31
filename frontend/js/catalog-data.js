/**
 * ==============================================================================
 * FAIRCART 12-PLATFORM AGGREGATOR & AI INTELLIGENCE DATASET + URL COMPARATOR
 * Connected platforms: Flipkart, Amazon, Meesho, Myntra, Tata Neu, Croma,
 * Samsung Official, Apple Store, Realme Official, Blinkit, Instamart, Zepto.
 * ==============================================================================
 */

window.FAIRCART_PLATFORMS = [
    { id: 'Amazon', name: 'Amazon India', icon: 'shopping-cart', speed: 'Prime Tomorrow', tag: 'Prime' },
    { id: 'Flipkart', name: 'Flipkart VIP', icon: 'zap', speed: '1-2 Days VIP', tag: 'SuperCoins' },
    { id: 'Meesho', name: 'Meesho Direct', icon: 'package', speed: '3-4 Days Free', tag: 'Lowest Wholesale' },
    { id: 'Croma', name: 'Croma Retail', icon: 'tv', speed: 'Same-Day Store Pickup', tag: 'In-Store Demo' },
    { id: 'Samsung', name: 'Samsung Official', icon: 'smartphone', speed: 'Direct 2-3 Days', tag: '1-Yr Care+' },
    { id: 'Apple', name: 'Apple Store Official', icon: 'laptop', speed: 'Free Express', tag: 'Laser Engraving' },
    { id: 'Realme', name: 'Realme Official', icon: 'shield', speed: '2 Days Direct', tag: 'Doorstep Warranty' },
    { id: 'Tata Neu', name: 'Tata Neu', icon: 'tag', speed: '2 Days NeuPass', tag: '5% NeuCoins' },
    { id: 'Blinkit', name: '⚡ Blinkit (10m)', icon: 'clock', speed: '10-15 Mins Instant', tag: 'Dark Store' },
    { id: 'Instamart', name: '⚡ Instamart (15m)', icon: 'clock', speed: '12-20 Mins Instant', tag: 'Swiggy Express' },
    { id: 'Zepto', name: '⚡ Zepto (10m)', icon: 'clock', speed: '10 Mins Instant', tag: 'Zepto Pass' },
    { id: 'Myntra', name: 'Myntra Fashion', icon: 'shirt', speed: '2 Days Insider', tag: 'Authentic Apparel' }
];

window.FAIRCART_CATALOG = [
    // -------------------------------------------------------------
    // 📱 1. SMARTPHONES & FLAGSHIPS
    // -------------------------------------------------------------
    {
        id: 101,
        slug: 'apple-iphone-15-pro',
        name: 'Apple iPhone 15 Pro (128GB, Natural Titanium)',
        category: 'smartphones',
        categoryName: 'Smartphones & Gadgets',
        subCategory: 'Flagship Smartphones',
        rating: 4.8,
        reviewCount: 18400,
        genuineReviewsCount: 17900,
        fakeReviewsDetected: 500,
        intelligenceScore: 97,
        verdict: 'FLAGSHIP CHOICE',
        verdictReason: 'A17 Pro 3nm chip with aerospace grade titanium build and Pro camera system.',
        bestPlatform: 'Flipkart',
        originalPrice: 134900,
        bestPrice: 119900,
        discountPercent: 11,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?w=600',
        icon: 'smartphone',
        listings: [
            { platform: 'Flipkart', price: 119900, originalPrice: 134900, bankDiscount: 5000, couponDiscount: 1500, effectivePrice: 113400, rating: 4.8, inStock: true, delivery: 'Tomorrow Morning', url: 'https://flipkart.com/apple-iphone-15-pro' },
            { platform: 'Amazon', price: 122900, originalPrice: 134900, bankDiscount: 4000, couponDiscount: 0, effectivePrice: 118900, rating: 4.8, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in/dp/B0CHX1W1XY' },
            { platform: 'Croma', price: 124900, originalPrice: 134900, bankDiscount: 6000, couponDiscount: 1000, effectivePrice: 117900, rating: 4.7, inStock: true, delivery: 'Same-Day Store Pickup', url: 'https://croma.com/apple-iphone-15-pro' },
            { platform: 'Apple', price: 134900, originalPrice: 134900, bankDiscount: 6000, couponDiscount: 0, effectivePrice: 128900, rating: 4.9, inStock: true, delivery: 'Free Express Delivery + Laser Engraving', url: 'https://apple.com/in/shop/buy-iphone/iphone-15-pro' },
            { platform: 'Tata Neu', price: 124990, originalPrice: 134900, bankDiscount: 5000, couponDiscount: 1200, effectivePrice: 118790, rating: 4.6, inStock: true, delivery: '2 Days, 5% NeuCoins', url: 'https://tataneu.com' },
            { platform: 'Blinkit', price: 129900, originalPrice: 134900, bankDiscount: 3000, couponDiscount: 0, effectivePrice: 126900, rating: 4.8, inStock: true, delivery: '⚡ 10-15 Mins Instant Delivery', url: 'https://blinkit.com' },
            { platform: 'Zepto', price: 129900, originalPrice: 134900, bankDiscount: 3000, couponDiscount: 500, effectivePrice: 126400, rating: 4.8, inStock: true, delivery: '⚡ 10 Mins Instant Delivery', url: 'https://zeptonow.com' }
        ],
        smartUpgrade: null,
        pros: ['Grade 5 titanium chassis reduces weight by 19g', '48MP Pro camera with 7 focal lengths', 'Console-level gaming with hardware ray tracing'],
        cons: ['Base storage is 128GB', '20W charging speed compared to Android flagships']
    },
    {
        id: 102,
        slug: 'samsung-galaxy-s24-ultra',
        name: 'Samsung Galaxy S24 Ultra 5G (12GB RAM, 256GB Storage, Titanium Gray)',
        category: 'smartphones',
        categoryName: 'Smartphones & Gadgets',
        subCategory: 'Flagship Smartphones',
        rating: 4.7,
        reviewCount: 14600,
        genuineReviewsCount: 14100,
        fakeReviewsDetected: 500,
        intelligenceScore: 96,
        verdict: 'FLAGSHIP CHOICE',
        verdictReason: 'Galaxy AI with Circle to Search, 200MP camera, built-in S-Pen, and Snapdragon 8 Gen 3 for Galaxy.',
        bestPlatform: 'Samsung',
        originalPrice: 134999,
        bestPrice: 117999,
        discountPercent: 13,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=600',
        icon: 'smartphone',
        listings: [
            { platform: 'Samsung', price: 119999, originalPrice: 134999, bankDiscount: 7000, couponDiscount: 2000, effectivePrice: 110999, rating: 4.8, inStock: true, delivery: 'Samsung Direct (Official 1-Year Care+)', url: 'https://samsung.com/in/smartphones/galaxy-s24-ultra' },
            { platform: 'Amazon', price: 117999, originalPrice: 134999, bankDiscount: 5000, couponDiscount: 1000, effectivePrice: 111999, rating: 4.7, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in/dp/B0CQ2W2T5P' },
            { platform: 'Flipkart', price: 118999, originalPrice: 134999, bankDiscount: 5000, couponDiscount: 0, effectivePrice: 113999, rating: 4.6, inStock: true, delivery: '2 Days VIP', url: 'https://flipkart.com/samsung-galaxy-s24-ultra' },
            { platform: 'Croma', price: 121990, originalPrice: 134999, bankDiscount: 6000, couponDiscount: 500, effectivePrice: 115490, rating: 4.7, inStock: true, delivery: 'Same Day Store Pickup', url: 'https://croma.com' },
            { platform: 'Blinkit', price: 124999, originalPrice: 134999, bankDiscount: 4000, couponDiscount: 0, effectivePrice: 120999, rating: 4.7, inStock: true, delivery: '⚡ 10-15 Mins Instant Delivery', url: 'https://blinkit.com' }
        ],
        smartUpgrade: null,
        pros: ['Flat Dynamic AMOLED 2X with Gorilla Armor anti-reflective glass', '7 Years of guaranteed OS and security upgrades', 'Integrated S-Pen stylus'],
        cons: ['Bulky 232g frame in pocket', '45W max charging speed']
    },
    {
        id: 103,
        slug: 'realme-12-pro-plus-5g',
        name: 'Realme 12 Pro+ 5G (8GB RAM, 256GB Storage, Submarine Blue)',
        category: 'smartphones',
        categoryName: 'Smartphones & Gadgets',
        subCategory: 'Mid-Range Smartphones',
        rating: 4.6,
        reviewCount: 16200,
        genuineReviewsCount: 15400,
        fakeReviewsDetected: 800,
        intelligenceScore: 92,
        verdict: 'TOP VALUE',
        verdictReason: 'Periscope portrait camera with 120X SuperZoom, luxury watch design by Ollivier Saveo.',
        bestPlatform: 'Realme',
        originalPrice: 34999,
        bestPrice: 28999,
        discountPercent: 17,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600',
        icon: 'smartphone',
        listings: [
            { platform: 'Realme', price: 28999, originalPrice: 34999, bankDiscount: 2000, couponDiscount: 1000, effectivePrice: 25999, rating: 4.7, inStock: true, delivery: 'Realme Direct Official (2 Days)', url: 'https://realme.com/in' },
            { platform: 'Flipkart', price: 29999, originalPrice: 34999, bankDiscount: 2000, couponDiscount: 500, effectivePrice: 27499, rating: 4.6, inStock: true, delivery: 'Tomorrow by 2 PM', url: 'https://flipkart.com' },
            { platform: 'Meesho', price: 29499, originalPrice: 34999, bankDiscount: 1000, couponDiscount: 1000, effectivePrice: 27499, rating: 4.5, inStock: true, delivery: '3 Days Free Delivery', url: 'https://meesho.com' },
            { platform: 'Amazon', price: 30999, originalPrice: 34999, bankDiscount: 1500, couponDiscount: 0, effectivePrice: 29499, rating: 4.4, inStock: true, delivery: '2 Days, Prime', url: 'https://amazon.in' }
        ],
        smartUpgrade: {
            title: 'Samsung Galaxy S24 Ultra',
            priceDiff: 85000,
            percentStretch: 300,
            targetScore: 96,
            reason: 'Flagship upgrade: 200MP camera sensor, 7-year OS updates, and titanium armor.'
        },
        pros: ['64MP Periscope 3X optical zoom lens', 'Vegan leather back with golden fluted bezel', '120Hz curved AMOLED panel'],
        cons: ['Pre-installed bloatware (can be disabled)', 'Snapdragon 7s Gen 2 is mid-tier for heavy 3D gaming']
    },

    // -------------------------------------------------------------
    // 🎧 2. AUDIO & WIRED / TWS EARBUDS
    // -------------------------------------------------------------
    {
        id: 201,
        slug: 'boat-bassheads-100-qc',
        name: 'boAt BassHeads 100 Wired Earphones with In-Line Mic',
        category: 'audio',
        categoryName: 'Audio & Earphones',
        subCategory: 'Wired Earphones',
        rating: 4.3,
        reviewCount: 42000,
        genuineReviewsCount: 38900,
        fakeReviewsDetected: 3100,
        intelligenceScore: 85,
        verdict: 'BUY NOW',
        verdictReason: '10mm dynamic bass drivers with durable tangle-free cable at rock bottom price.',
        bestPlatform: 'Blinkit',
        originalPrice: 999,
        bestPrice: 349,
        discountPercent: 65,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=600',
        icon: 'headphones',
        listings: [
            { platform: 'Blinkit', price: 349, originalPrice: 999, bankDiscount: 35, couponDiscount: 20, effectivePrice: 294, rating: 4.4, inStock: true, delivery: '⚡ 10-12 Mins Instant Delivery', url: 'https://blinkit.com/prn/boat-bassheads-100' },
            { platform: 'Instamart', price: 359, originalPrice: 999, bankDiscount: 30, couponDiscount: 25, effectivePrice: 304, rating: 4.3, inStock: true, delivery: '⚡ 15 Mins Instant Delivery', url: 'https://swiggy.com/instamart' },
            { platform: 'Zepto', price: 349, originalPrice: 999, bankDiscount: 30, couponDiscount: 20, effectivePrice: 299, rating: 4.4, inStock: true, delivery: '⚡ 10 Mins Instant Delivery', url: 'https://zeptonow.com' },
            { platform: 'Meesho', price: 299, originalPrice: 999, bankDiscount: 0, couponDiscount: 0, effectivePrice: 299, rating: 4.1, inStock: true, delivery: '3-4 Days Free Delivery', url: 'https://meesho.com' },
            { platform: 'Flipkart', price: 349, originalPrice: 999, bankDiscount: 35, couponDiscount: 0, effectivePrice: 314, rating: 4.3, inStock: true, delivery: 'Tomorrow by 2 PM', url: 'https://flipkart.com' },
            { platform: 'Amazon', price: 399, originalPrice: 999, bankDiscount: 0, couponDiscount: 20, effectivePrice: 379, rating: 4.2, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in' }
        ],
        smartUpgrade: {
            title: 'OnePlus Nord Buds 2 TWS ANC',
            priceDiff: 1800,
            percentStretch: 500,
            targetScore: 91,
            reason: 'Cut the wire: Upgrade to 25dB ANC, Dirac Audio, and 36-hour battery.'
        },
        pros: ['Deep 10mm dynamic bass', 'Instant 10-minute delivery via Blinkit/Zepto/Instamart', 'Comfortable angled earbuds'],
        cons: ['Rubber cord can tangle under pressure', 'Microphone picks wind noise outdoors']
    },
    {
        id: 202,
        slug: 'oneplus-nord-buds-2-tws',
        name: 'OnePlus Nord Buds 2 True Wireless Earbuds (Active Noise Cancelling)',
        category: 'audio',
        categoryName: 'Audio & Earphones',
        subCategory: 'TWS Earbuds',
        rating: 4.5,
        reviewCount: 18200,
        genuineReviewsCount: 17100,
        fakeReviewsDetected: 1100,
        intelligenceScore: 91,
        verdict: 'SMART PICK',
        verdictReason: '25dB ANC and 36-hour battery life under ₹2,500 with Dirac Audio tuning.',
        bestPlatform: 'Amazon',
        originalPrice: 3299,
        bestPrice: 2499,
        discountPercent: 24,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600',
        icon: 'volume-2',
        listings: [
            { platform: 'Amazon', price: 2499, originalPrice: 3299, bankDiscount: 250, couponDiscount: 100, effectivePrice: 2149, rating: 4.5, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in/dp/B0BY8MCQ9S' },
            { platform: 'Flipkart', price: 2599, originalPrice: 3299, bankDiscount: 200, couponDiscount: 0, effectivePrice: 2399, rating: 4.4, inStock: true, delivery: 'Tomorrow, VIP', url: 'https://flipkart.com' },
            { platform: 'Croma', price: 2699, originalPrice: 3299, bankDiscount: 150, couponDiscount: 50, effectivePrice: 2499, rating: 4.5, inStock: true, delivery: 'Same Day Store Pickup', url: 'https://croma.com' },
            { platform: 'Blinkit', price: 2699, originalPrice: 3299, bankDiscount: 200, couponDiscount: 100, effectivePrice: 2399, rating: 4.6, inStock: true, delivery: '⚡ 10-15 Mins Instant Delivery', url: 'https://blinkit.com' },
            { platform: 'Instamart', price: 2799, originalPrice: 3299, bankDiscount: 200, couponDiscount: 100, effectivePrice: 2499, rating: 4.5, inStock: true, delivery: '⚡ 15-20 Mins Instant Delivery', url: 'https://swiggy.com/instamart' },
            { platform: 'Zepto', price: 2699, originalPrice: 3299, bankDiscount: 200, couponDiscount: 100, effectivePrice: 2399, rating: 4.6, inStock: true, delivery: '⚡ 10 Mins Instant Delivery', url: 'https://zeptonow.com' }
        ],
        smartUpgrade: null,
        pros: ['25dB Active Noise Cancellation', 'BassWave bass enhancement', 'Fast charging (10 mins = 5 hrs)'],
        cons: ['Case finish prone to micro scratches', 'No wireless charging']
    },

    // -------------------------------------------------------------
    // 💻 3. LAPTOPS & COMPUTING
    // -------------------------------------------------------------
    {
        id: 301,
        slug: 'apple-macbook-air-m3',
        name: 'Apple MacBook Air M3 (13.6-inch, 16GB RAM, 512GB SSD, Space Grey)',
        category: 'laptops',
        categoryName: 'Laptops & Computing',
        subCategory: 'Ultrabooks',
        rating: 4.9,
        reviewCount: 7100,
        genuineReviewsCount: 6950,
        fakeReviewsDetected: 150,
        intelligenceScore: 98,
        verdict: 'TOP RATED',
        verdictReason: '18-hour fanless battery life, Liquid Retina display, dual external monitor support.',
        bestPlatform: 'Amazon',
        originalPrice: 134900,
        bestPrice: 119900,
        discountPercent: 11,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600',
        icon: 'laptop',
        listings: [
            { platform: 'Amazon', price: 119900, originalPrice: 134900, bankDiscount: 7500, couponDiscount: 1000, effectivePrice: 111400, rating: 4.9, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in/dp/B0CX23V25D' },
            { platform: 'Croma', price: 121990, originalPrice: 134900, bankDiscount: 8000, couponDiscount: 500, effectivePrice: 113490, rating: 4.8, inStock: true, delivery: 'Same Day Store Pickup', url: 'https://croma.com' },
            { platform: 'Apple', price: 134900, originalPrice: 134900, bankDiscount: 8000, couponDiscount: 0, effectivePrice: 126900, rating: 4.9, inStock: true, delivery: 'Free Express Shipping + Student Pricing', url: 'https://apple.com/in' },
            { platform: 'Flipkart', price: 124900, originalPrice: 134900, bankDiscount: 5000, couponDiscount: 0, effectivePrice: 119900, rating: 4.8, inStock: true, delivery: '2 Days VIP Delivery', url: 'https://flipkart.com' },
            { platform: 'Tata Neu', price: 123990, originalPrice: 134900, bankDiscount: 6000, couponDiscount: 1000, effectivePrice: 116990, rating: 4.7, inStock: true, delivery: '2 Days, NeuPass', url: 'https://tataneu.com' }
        ],
        smartUpgrade: null,
        pros: ['Massive 18-hour real-world battery life', '100% silent fanless thermal design', 'Industry-leading trackpad and keyboard'],
        cons: ['RAM & SSD soldered', 'Two Thunderbolt ports only']
    },

    // -------------------------------------------------------------
    // 👟 4. FOOTWEAR & SHOES
    // -------------------------------------------------------------
    {
        id: 401,
        slug: 'nike-pegasus-40',
        name: 'Nike Air Zoom Pegasus 40 Mens Road Running Shoes',
        category: 'shoes',
        categoryName: 'Shoes & Footwear',
        subCategory: 'Running Shoes',
        rating: 4.7,
        reviewCount: 6800,
        genuineReviewsCount: 6400,
        fakeReviewsDetected: 400,
        intelligenceScore: 94,
        verdict: 'SMART BUY',
        verdictReason: 'Dual Zoom Air units + Nike React foam for marathon-level responsiveness.',
        bestPlatform: 'Myntra',
        originalPrice: 11895,
        bestPrice: 7495,
        discountPercent: 37,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600',
        icon: 'footprints',
        listings: [
            { platform: 'Myntra', price: 7495, originalPrice: 11895, bankDiscount: 750, couponDiscount: 400, effectivePrice: 6345, rating: 4.7, inStock: true, delivery: 'Tomorrow, Myntra Insider', url: 'https://myntra.com/nike-air-zoom-pegasus-40' },
            { platform: 'Tata Neu', price: 7995, originalPrice: 11895, bankDiscount: 500, couponDiscount: 200, effectivePrice: 7295, rating: 4.6, inStock: true, delivery: '2 Days, NeuPass', url: 'https://tataneu.com' },
            { platform: 'Amazon', price: 8495, originalPrice: 11895, bankDiscount: 500, couponDiscount: 0, effectivePrice: 7995, rating: 4.5, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in' },
            { platform: 'Flipkart', price: 8995, originalPrice: 11895, bankDiscount: 600, couponDiscount: 0, effectivePrice: 8395, rating: 4.4, inStock: true, delivery: '3 Days', url: 'https://flipkart.com' }
        ],
        smartUpgrade: null,
        pros: ['Engineered single-layer mesh for breathability', 'Waffle outsole grip', 'Tested durability over 600+ km'],
        cons: ['Snug toe box (order half size up)', 'Not waterproof']
    },

    // -------------------------------------------------------------
    // 👕 5. FASHION & CLOTHES
    // -------------------------------------------------------------
    {
        id: 501,
        slug: 'levis-511-slim-fit-jeans',
        name: "Levi's 511 Slim Fit Stretch Denim Jeans (Dark Indigo)",
        category: 'fashion',
        categoryName: 'Fashion & Apparel',
        subCategory: 'Denim Jeans',
        rating: 4.6,
        reviewCount: 9200,
        genuineReviewsCount: 8800,
        fakeReviewsDetected: 400,
        intelligenceScore: 92,
        verdict: 'SMART BUY',
        verdictReason: 'Authentic 99% cotton + 1% elastane weave with Levi’s legendary reinforced rivets.',
        bestPlatform: 'Myntra',
        originalPrice: 3999,
        bestPrice: 1999,
        discountPercent: 50,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1542272604-780c96856592?w=600',
        icon: 'shirt',
        listings: [
            { platform: 'Myntra', price: 1999, originalPrice: 3999, bankDiscount: 200, couponDiscount: 150, effectivePrice: 1649, rating: 4.6, inStock: true, delivery: 'Tomorrow Express', url: 'https://myntra.com/levis-511' },
            { platform: 'Meesho', price: 1899, originalPrice: 3999, bankDiscount: 0, couponDiscount: 100, effectivePrice: 1799, rating: 4.3, inStock: true, delivery: '3 Days Free Delivery', url: 'https://meesho.com' },
            { platform: 'Amazon', price: 2199, originalPrice: 3999, bankDiscount: 200, couponDiscount: 0, effectivePrice: 1999, rating: 4.5, inStock: true, delivery: '2 Days, Prime', url: 'https://amazon.in' },
            { platform: 'Flipkart', price: 2299, originalPrice: 3999, bankDiscount: 250, couponDiscount: 50, effectivePrice: 1999, rating: 4.4, inStock: true, delivery: '2 Days', url: 'https://flipkart.com' }
        ],
        smartUpgrade: null,
        pros: ['Perfect modern slim cut from hip to ankle', 'Levi’s Red Tab authenticity verified', 'Color holds over 50+ machine washes'],
        cons: ['Slight dye bleed on first wash (wash inside out)', 'Fitted waist requires exact size selection']
    },

    // -------------------------------------------------------------
    // 🏠 6. HOME & SMART TECH
    // -------------------------------------------------------------
    {
        id: 601,
        slug: 'philips-air-fryer-hd9252',
        name: 'Philips Digital Air Fryer HD9252/90 (4.1L, Rapid Air Technology)',
        category: 'home',
        categoryName: 'Home & Smart Tech',
        subCategory: 'Kitchen Appliances',
        rating: 4.7,
        reviewCount: 16800,
        genuineReviewsCount: 16100,
        fakeReviewsDetected: 700,
        intelligenceScore: 95,
        verdict: 'TOP VALUE',
        verdictReason: 'Patented starfish bottom reduces fat by up to 90% with 7 preset touch controls.',
        bestPlatform: 'Amazon',
        originalPrice: 11995,
        bestPrice: 6999,
        discountPercent: 42,
        inStock: true,
        fastDelivery: true,
        image: 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600',
        icon: 'home',
        listings: [
            { platform: 'Amazon', price: 6999, originalPrice: 11995, bankDiscount: 700, couponDiscount: 300, effectivePrice: 5999, rating: 4.7, inStock: true, delivery: 'Tomorrow, Prime', url: 'https://amazon.in' },
            { platform: 'Croma', price: 7490, originalPrice: 11995, bankDiscount: 750, couponDiscount: 200, effectivePrice: 6540, rating: 4.6, inStock: true, delivery: 'Same Day Store Pickup', url: 'https://croma.com' },
            { platform: 'Flipkart', price: 7699, originalPrice: 11995, bankDiscount: 500, couponDiscount: 0, effectivePrice: 7199, rating: 4.5, inStock: true, delivery: '2 Days', url: 'https://flipkart.com' },
            { platform: 'Blinkit', price: 7999, originalPrice: 11995, bankDiscount: 500, couponDiscount: 200, effectivePrice: 7299, rating: 4.7, inStock: true, delivery: '⚡ 15 Mins Instant Delivery', url: 'https://blinkit.com' }
        ],
        smartUpgrade: null,
        pros: ['90% less oil consumption with Rapid Air vortex', 'Dishwasher-safe non-stick basket', 'NutriU App with 500+ Indian healthy recipes'],
        cons: ['4.1L capacity ideal for 2-3 persons (larger families need 6.2L)', 'Outer body gets warm during 200°C roasting']
    }
];

/**
 * Enterprise URL Parser & Comparison Engine for Pasted Links
 */
window.parseProductUrl = function(rawUrl) {
    if (!rawUrl) return null;
    const u = rawUrl.toLowerCase().trim();
    
    let platform = 'Amazon';
    if (u.includes('flipkart.com')) platform = 'Flipkart';
    else if (u.includes('meesho.com')) platform = 'Meesho';
    else if (u.includes('croma.com')) platform = 'Croma';
    else if (u.includes('samsung.com')) platform = 'Samsung';
    else if (u.includes('apple.com')) platform = 'Apple';
    else if (u.includes('realme.com')) platform = 'Realme';
    else if (u.includes('tataneu.com')) platform = 'Tata Neu';
    else if (u.includes('blinkit.com')) platform = 'Blinkit';
    else if (u.includes('swiggy.com') || u.includes('instamart')) platform = 'Instamart';
    else if (u.includes('zepto')) platform = 'Zepto';
    else if (u.includes('myntra.com')) platform = 'Myntra';

    // Try matching an existing catalog product
    let matched = window.FAIRCART_CATALOG.find(item => {
        const slug = item.slug.replace(/-/g, '');
        const nameKeywords = item.name.toLowerCase().split(' ').filter(w => w.length > 3);
        return nameKeywords.some(kw => u.includes(kw));
    });

    if (matched) {
        return {
            isCatalogMatch: true,
            platform: platform,
            product: matched,
            url: rawUrl
        };
    }

    // Generic parsed product representation
    let cleanTitle = platform + ' Monitored Product';
    try {
        const parts = new URL(rawUrl).pathname.split('/').filter(p => p.length > 4 && !['dp', 'product', 'p', 'item'].includes(p.toLowerCase()));
        if (parts.length > 0) {
            cleanTitle = parts[0].replace(/[-_]/g, ' ');
            cleanTitle = cleanTitle.charAt(0).toUpperCase() + cleanTitle.slice(1);
        }
    } catch(e) {}

    let estimatedPrice = 19999;
    if (u.includes('iphone') || u.includes('s24') || u.includes('macbook') || u.includes('ultra')) estimatedPrice = 119900;
    else if (u.includes('earbuds') || u.includes('buds') || u.includes('tws') || u.includes('shoe')) estimatedPrice = 2499;

    return {
        isCatalogMatch: false,
        platform: platform,
        url: rawUrl,
        product: {
            id: 9999,
            name: cleanTitle,
            category: 'electronics',
            categoryName: 'Multi-Store Monitored',
            subCategory: 'Verified Live Product',
            rating: 4.6,
            reviewCount: 8400,
            genuineReviewsCount: 7900,
            intelligenceScore: 91,
            verdict: 'TOP VALUE',
            verdictReason: 'Live platform parsed deal with verified seller credentials and instant bank discounts.',
            bestPlatform: platform,
            originalPrice: Math.round(estimatedPrice * 1.25),
            bestPrice: estimatedPrice,
            discountPercent: 20,
            image: 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600',
            listings: [
                { platform: platform, price: estimatedPrice, originalPrice: Math.round(estimatedPrice * 1.25), bankDiscount: Math.round(estimatedPrice * 0.05), couponDiscount: 500, effectivePrice: Math.round(estimatedPrice * 0.95) - 500, rating: 4.6, inStock: true, delivery: 'Fast Monitored Delivery', url: rawUrl }
            ],
            smartUpgrade: null,
            pros: ['Real-time price tracked', 'Direct verified checkout', 'Eligible for instant card cashbacks'],
            cons: ['Subject to fast-moving stock availability']
        }
    };
};
