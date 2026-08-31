# FairCart Architecture

## Overview

FairCart is a monorepo with a decoupled frontend and a Spring Boot REST API.

```
┌─────────────┐     HTTP/JSON      ┌──────────────────┐     JDBC      ┌─────────┐
│  Frontend   │ ◄───────────────► │  Spring Boot API │ ◄───────────► │  MySQL  │
│  (Static)   │     JWT Auth       │  (Java 21)       │               │  8.0    │
└─────────────┘                    └──────────────────┘               └─────────┘
```

## Backend Package Layout

| Package        | Responsibility                          |
|----------------|-----------------------------------------|
| `config`       | Security, CORS, JWT properties          |
| `controller`   | REST endpoints                          |
| `service`      | Business logic & intelligence scoring   |
| `repository`   | JPA data access                         |
| `entity`       | Domain models                           |
| `dto`          | Request/response payloads               |
| `security`     | JWT filter, UserDetails, auth provider  |
| `exception`    | Global error handling                   |

## Product Intelligence Score

A composite metric (0–100) computed from:

- Price competitiveness
- Review sentiment
- Stock reliability
- Category benchmarks

Stored on `Product.intelligenceScore` and exposed via the catalog API.

## API Versioning

All endpoints are prefixed with `/api/v1`.

## Next Phases

1. JWT authentication (register/login)
2. Product CRUD + search
3. Cart & checkout
4. Intelligence score engine (Nemotron-assisted rules)
