# FlexStream: Real-Time Movie Analytics Dashboard

A high-performance movie rating dashboard utilizing a **Medallion Architecture** (Bronze/Silver/Gold) data pipeline and a vintage-themed Vue.js frontend.

## 🚀 Tech Stack
* **Frontend**: Vue.js 3 (Composition API), Vite, Axios
* **Backend**: Spring Boot 3, Java 21
* **Data Layer**: Apache Cassandra (Permanent Store), Redis (Caching Layer)
* **Architecture**: Hexagonal / Ports & Adapters

## 🛠️ System Architecture
1. **Ingestion**: 1.2M records processed via PySpark into DuckDB.
2. **Persistence**: Structured ratings stored in Cassandra.
3. **Speed**: Frequently accessed user history cached in Redis.
4. **Interface**: REST API providing sub-100ms response times for user history.

## 🏃 How to Run
1. **Containers**: `docker-compose up -d` (Start Cassandra & Redis)
2. **Backend**: `mvn spring-boot:run` (Port 8080)
3. **Frontend**: `cd flexstream-frontend && npm install && npm run dev` (Port 5173)

## 🎨 UI Design
Features a cinematic "Vintage Celluloid" theme with dynamic film-strip borders and real-time movie filtering.
