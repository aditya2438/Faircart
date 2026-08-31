# 🛒 Faircart — Enterprise AI E-Commerce Decision Engine & Multi-Platform Aggregator

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java 21/26](https://img.shields.io/badge/Java-21%20%7C%2026%20Loom-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3.4](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Tailwind CSS 3](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis 7](https://img.shields.io/badge/Redis-7%20MFA%20OTP-DC382D?logo=redis&logoColor=white)](https://redis.io/)
[![Build Status](https://img.shields.io/badge/Tests-30%2F30%20Passing-brightgreen.svg)]()

> **Faircart** is an intelligent, multi-platform e-commerce aggregator, price-drop alert engine, and decision-making platform. It eliminates shopping friction by scraping, normalizing, and comparing real-time product prices, instant bank discounts, coupon deductions, authentic user reviews, and return policies across major trusted platforms (**Amazon, Flipkart, Tata Neu, Myntra, Croma**).

---

## 🌟 Key Architecture & Capabilities

`
                  ┌────────────────────────────────────────────────────────┐
                  │          FAIRCART CLIENT INTERFACE (HTML5/CSS3)         │
                  │   Frosted Glassmorphism • Magnetic Cursor • 3D Tilt   │
                  └──────────────────────────┬─────────────────────────────┘
                                             │ REST API / JWT
                                             ▼
                  ┌────────────────────────────────────────────────────────┐
                  │              SPRING BOOT 3.4 CORE BACKEND              │
                  ├──────────────────────────┬─────────────────────────────┤
                  │ ⚡ Parallel Scraper      │ 🛡️ Dual-Channel MFA OTP     │
                  │ (Java 26 Virtual Threads)│ (Redis 5m TTL + Lockout)    │
                  ├──────────────────────────┼─────────────────────────────┤
                  │ 💡 Smart Stretch Engine  │ 🔍 Verified Truth Box       │
                  │ (125% Budget Jump Math)  │ (Fake Review Bot Filter)    │
                  └─────────────┬────────────────────────────┬─────────────┘
                                │                            │
                                ▼                            ▼
                  ┌──────────────────────────┐ ┌───────────────────────────┐
                  │ PostgreSQL 16 Database   │ │ Redis 7 In-Memory Cache   │
                  │ Product History & Auth   │ │ 5-Min TTL & Rate Limiting │
                  └──────────────────────────┘ └───────────────────────────┘
`

### 1. ⚡ Virtual-Thread Parallel Scraper (ParallelScraperService.java)
- Executes non-blocking concurrent queries across 5 platforms with latency SLA $< 5.
- Calculates true out-of-pocket prices: $\text{Effective Price} = \text{Base Price} - \text{Instant Bank Discount} - \text{Coupon Deductions}$.

### 2. 💡 Smart Stretch Budget Engine (RecommendationService.java)
- Mathematical formulation: Evaluates  \le \text{Budget}$ vs $\text{Budget} < P \le \text{Budget} \times 1.25$.
- Computes composite value jump ratio $\Delta V = \frac{\text{Score}_{\text{upgrade}} / P_{\text{upgrade}}}{\text{Score}_{\text{base}} / P_{\text{base}}}$.
- Produces deterministic **0–100 Buy / Good Choice / Wait / Do Not Buy verdicts**.

### 3. 🛡️ Dual-Channel Redis MFA OTP (RedisOtpService.java)
- Cryptographically secure 6-digit numeric OTP generation (SecureRandom).
- 5-minute TTL stored in Redis (aircart:otp:<destination>) with in-memory resilient fallback.
- Rate-limiting lockout enforcement after 3 consecutive failed verification attempts.

### 4. 🎨 Senior-Level Luxury UI/UX Design System
- **Apple iOS Frosted Glassmorphism**: ackdrop-filter: blur(24px), translucent cards with glowing borders.
- **Physics-Based Magnetic Cursor**: Lerp follower and tactile soundless click ripples.
- **3D Card Parallax Tilt**: Real-time specular glare with dynamic mouse coordinates (--mouse-x, --mouse-y).
- **Autonomous AI Concierge**: Built-in voice input via Web Speech API, streaming answers, and 1-click CSV/PDF exports.

---

## 🚀 100% Free Deployment Guide (Live URL in 5 Mins)

### Option A: Deploy Frontend to GitHub Pages (100% Free Forever)
1. Push your repository to GitHub (see commands below).
2. On GitHub, go to your repository **Settings** → **Pages**.
3. Under **Build and deployment** → **Source**, select **GitHub Actions**.
4. The workflow in .github/workflows/deploy-gh-pages.yml will automatically build and publish your site at:
   https://<your-username>.github.io/<your-repo>/

### Option B: Deploy Backend & Database on Render (100% Free Tier)
1. Create a free account on [Render.com](https://render.com).
2. Click **New** → **Blueprint**.
3. Connect your GitHub repository.
4. Render will automatically read ender.yaml and provision:
   - **PostgreSQL 16 Database** (Free)
   - **Spring Boot Docker Web Service** (Free)
   - **Frontend Static Site with SSL** (Free)

### Option C: Deploy Frontend to Vercel (100% Free)
1. Install Vercel CLI: 
pm i -g vercel (or connect on [Vercel.com](https://vercel.com)).
2. In the project root, run:
   `ash
   vercel
   `
3. Vercel automatically deploys using the bundled ercel.json configuration with instant SSL and global CDN.

---

## 💻 Local Quickstart

### Prerequisites
- Java 21 or Java 26
- Maven 3.9+
- Docker & Docker Compose (Optional)

### 1. Run Full Stack with Docker
`ash
docker compose up -d
`
- Frontend: http://localhost:3000 (or open rontend/index.html)
- Backend API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html

### 2. Run Backend Standalone
`ash
cd backend
mvn clean test
mvn spring-boot:run
`

---

## 📦 Step-by-Step Git Push Instructions

Run these terminal commands to initialize and push your repository to your GitHub account:

`ash
# 1. Initialize Git repository
git init

# 2. Add all project files
git add .

# 3. Commit changes
git commit -m "feat: complete Faircart enterprise aggregator, UI/UX interaction engine, and CI/CD"

# 4. Rename default branch to main
git branch -M main

# 5. Link your GitHub remote repository (replace with your GitHub repository URL)
git remote add origin https://github.com/<YOUR_USERNAME>/FairCart.git

# 6. Push code to GitHub
git push -u origin main
`

---

## 📜 License
This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details. Free for commercial and private use.
