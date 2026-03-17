# FlexStream: Real-Time Movie Analytics Dashboard

A high-performance movie rating dashboard utilizing a **Medallion Architecture** (Bronze/Silver/Gold) data pipeline and a vintage-themed Vue.js frontend.

## 🏗️ High-Level Design (HLD)

```mermaid
graph TD
    subgraph "Data Pipeline (Medallion)"
        A[(Raw Movie Data)] -->|PySpark Ingestion| B[Bronze: Raw Tables]
        B -->|Transformation| C[Silver: Cleaned Ratings]
        C -->|Aggregation| D[Gold: Analytics Ready]
    end

    subgraph "Persistence Layer"
        D -->|Write Through| E[(Apache Cassandra)]
        E -->|Cache Latency| F[(Redis Cache)]
    end

    subgraph "Application Layer (Spring Boot)"
        F <--> G[MovieService]
        G <--> H[HistoryController]
    end

    subgraph "Frontend (Vue.js 3)"
        H <--> I[Axios Proxy]
        I --> J[Vintage Celluloid Dashboard]
    end

    style J fill:#2b231d,stroke:#4a3f35,color:#e0d5c1
    style E fill:#1261a0,color:#fff
    style F fill:#a41e11,color:#fff
```

<img width="808" height="399" alt="image" src="https://github.com/user-attachments/assets/60accdac-ca6e-4785-9000-733c93ccc3fb" />


## 🚀 Tech Stack
* **Frontend**: Vue.js 3 (Composition API), Vite, Axios
* **Backend**: Spring Boot 3, Java 21
* **Data Layer**: Apache Cassandra (Permanent Store), Redis (Caching Layer)
* **Architecture**: Hexagonal / Ports & Adapters

## 🏃 How to Run
1. **Containers**: `docker-compose up -d`
2. **Backend**: `mvn spring-boot:run`
3. **Frontend**: `cd flexstream-frontend && npm install && npm run dev`

## 🎨 UI Design
Features a cinematic "Vintage Celluloid" theme with dynamic film-strip borders and real-time movie filtering.
