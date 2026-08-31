# 🛒 Faircart — Enterprise AI Multi-Platform E-Commerce Decision Engine

[![Author: Aditya Singh Chouhan](https://img.shields.io/badge/Author-Aditya%20Singh%20Chouhan-blue.svg)](https://github.com/)
[![License: All Rights Reserved](https://img.shields.io/badge/License-All%20Rights%20Reserved-red.svg)](LICENSE)
[![Java 21/26](https://img.shields.io/badge/Java-21%20%7C%2026%20Loom-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3.4](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Tailwind CSS 3](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis 7](https://img.shields.io/badge/Redis-7%20MFA%20OTP-DC382D?logo=redis&logoColor=white)](https://redis.io/)
[![Build Status](https://img.shields.io/badge/Tests-32%2F32%20Passing-brightgreen.svg)]()

> **Faircart** is an intelligent multi-platform e-commerce aggregator, decision engine, and AI shopping concierge engineered by **Aditya Singh Chouhan**. It eliminates manual shopping friction by scraping, normalizing, and comparing real-time product prices, instant bank discounts, coupon deductions, authentic user reviews, and return policies across 12 major platforms (**Amazon, Flipkart, Meesho, Myntra, Tata Neu, Croma, Samsung, Apple, Realme, Blinkit, Instamart, and Zepto**).

---

## 🌟 Key Architecture & Capabilities

```
                  ┌────────────────────────────────────────────────────────┐
                  │          FAIRCART CLIENT INTERFACE (HTML5/CSS3)         │
                  │   Nordic Obsidian & Alpine Alabaster • 3D Tilt Glare   │
                  └──────────────────────────┬─────────────────────────────┘
                                             │ REST API / JWT
                                             ▼
                  ┌────────────────────────────────────────────────────────┐
                  │              SPRING BOOT 3.4 CORE BACKEND              │
                  ├──────────────────────────┬─────────────────────────────┤
                  │ ⚡ 12-Platform Scraper   │ 🛡️ Dual-Channel MFA OTP     │
                  │ (Java 21 Virtual Threads)│ (Redis 5m TTL + Lockout)    │
                  ├──────────────────────────┼─────────────────────────────┤
                  │ 💡 Smart Stretch Engine  │ 🔍 Verified Truth Box       │
                  │ (+10–25% Tier-Jump Math) │ (Anti-Deception Audit)      │
                  └─────────────┬────────────────────────────┬─────────────┘
                                │                            │
                                ▼                            ▼
                  ┌──────────────────────────┐ ┌───────────────────────────┐
                  │ PostgreSQL 16 Database   │ │ Redis 7 In-Memory Cache   │
                  │ Product History & Auth   │ │ 5-Min TTL & Rate Limiting │
                  └──────────────────────────┘ └───────────────────────────┘
```

### 1. ⚡ Virtual-Thread Multi-Platform Scraper (`ParallelScraperService.java`)
- Executes non-blocking concurrent queries across 12 platforms with latency SLA $< 5\text{s}$.
- Connects **Flipkart, Amazon, Meesho, Myntra, Tata Neu, Croma, Samsung Store, Apple Store, Realme Store, Blinkit, Swiggy Instamart, and Zepto**.
- Calculates true out-of-pocket prices:
  $$\text{Effective Price} = \text{Base Price} - \text{Instant Bank Discount} - \text{Coupon Deductions}$$

### 2. 💡 Smart Stretch Budget Engine (`RecommendationService.java`)
- Evaluates candidate products: $P \le \text{Budget}$ vs $\text{Budget} < P \le \text{Budget} \times 1.25$.
- Computes composite value jump ratio:
  $$\Delta V = \frac{\text{Score}_{\text{upgrade}} / P_{\text{upgrade}}}{\text{Score}_{\text{base}} / P_{\text{base}}}$$
- Produces deterministic **0–100 Buy / Skip / Wait Verdicts** based on 90-day time-series pricing and verified sentiment.

### 3. 🛡️ Dual-Channel Redis MFA OTP (`RedisOtpService.java`)
- Cryptographically secure 6-digit numeric OTP generation (`SecureRandom`).
- 5-minute TTL stored in Redis (`faircart:otp:<destination>`) with resilient concurrent in-memory fallback.
- Enforces 3-attempt lockout security to prevent brute force attacks.

### 4. 🎨 Senior-Level Luxury Design System 2.0
- **Nordic Obsidian & Alpine Alabaster Dual Themes**: Studio-grade diffused lighting, zero harsh neon.
- **Instant 0ms Page Transitions**: Link speculation prefetcher and cross-document View Transitions.
- **Interactive Sliding Price Physics**: Custom slider tracks with live floating monetary bubbles.
- **Mobile Safe UI Layout**: Raised AI floating concierge button with zero overlap on fixed bottom navigation tabs.

---

## 🚀 100% Free Live Deployment Options (Zero Cost)

| Platform | Type | Free URL Format | Setup Time |
| :--- | :--- | :--- | :--- |
| **Vercel** (Recommended) | Frontend + CDN | `https://faircart-<username>.vercel.app` | 1 Minute |
| **GitHub Pages** | Frontend | `https://<username>.github.io/Faircart/` | Automatic on Push |
| **Render** | Full Stack (Spring Boot + Postgres) | `https://faircart-api.onrender.com` | 3 Minutes |
| **DuckDNS** (Custom Domain) | Free Custom DNS | `https://faircart.duckdns.org` | 2 Minutes |

### Option A: Deploy to GitHub Pages (Automatic on `git push`)
1. Push your repository to GitHub (see instructions below).
2. On GitHub, navigate to **Settings** → **Pages**.
3. Under **Build and deployment** → **Source**, select **GitHub Actions**.
4. The workflow in `.github/workflows/deploy-gh-pages.yml` will automatically build and publish your site at:
   `https://<YOUR_GITHUB_USERNAME>.github.io/Faircart/`

### Option B: Deploy to Vercel (Instant HTTPS & Global CDN)
1. Sign up on [Vercel](https://vercel.com) using your GitHub account.
2. Click **Add New Project** → Import your `Faircart` GitHub repository.
3. Keep default settings (`vercel.json` is already bundled) and click **Deploy**.
4. Your site will instantly go live at `https://faircart-<username>.vercel.app`.

### Option C: Deploy Backend & Database on Render
1. Sign up on [Render.com](https://render.com).
2. Click **New** → **Blueprint** → Select your `Faircart` repo.
3. Render reads `render.yaml` to auto-provision a free PostgreSQL database and Spring Boot web service.

---

## 📦 How to Upload This Project to Your GitHub Account

Open your terminal in the project directory and run:

```bash
# 1. Add all files to git
git add .

# 2. Commit changes
git commit -m "feat: complete Faircart enterprise release - Copyright 2026 Aditya Singh Chouhan"

# 3. Rename branch to main
git branch -M main

# 4. Link to your GitHub repository (replace <YOUR_GITHUB_USERNAME> with your actual username)
git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/Faircart.git

# 5. Push code to your GitHub account
git push -u origin main
```

---

## 💻 Local Quickstart

### 1. Run Backend Tests
```bash
cd backend
mvn test
```
*(All 32 tests execute against in-memory H2 with 100% pass rate)*

### 2. Launch Full Stack with Docker
```bash
docker compose up -d --build
```
- **Frontend UI:** `http://localhost:3000` (or double-click `frontend/index.html`)
- **Backend REST API:** `http://localhost:8080/api/v1`
- **Swagger Documentation:** `http://localhost:8080/swagger-ui.html`

---

## 📜 Copyright & License

**Copyright © 2026 Aditya Singh Chouhan. All rights reserved.**

This software and associated documentation files are proprietary and confidential. Unauthorized copying, distribution, modification, reverse engineering, or commercial use without express written consent from **Aditya Singh Chouhan** is strictly prohibited. See [LICENSE](LICENSE) for full terms.
