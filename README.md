# FlexStream: High-Scale Activity Stream MVP

A high-performance movie activity stream built with **Java 21**, **Spring Boot 3**, and **Apache Cassandra**. Designed to handle massive datasets (25M+ rows) using modern concurrency patterns.

## 🚀 Key Features
* **Virtual-Threaded Ingestion:** Utilizes Java 21 Virtual Threads to ingest 1M+ records from CSV to Cassandra without I/O blocking.
* **NoSQL Schema Design:** Optimized Cassandra schema using Partition Keys for Users and Clustering Columns for reverse-chronological activity.
* **Service-Level Joins:** Decoupled metadata strategy that joins NoSQL behavior data with in-memory CSV metadata for sub-millisecond API response times.
* **Hexagonal Architecture:** Strict separation of concerns between Domain, Repository, and Service layers.

## 🛠️ Tech Stack
* **Backend:** Java 21, Spring Boot 3, Spring Data Cassandra
* **Database:** Apache Cassandra (Dockerized)
* **Performance:** Java Virtual Threads (Project Loom)
* **Tools:** Maven, Docker, cqlsh

## 📈 API Endpoints
| Endpoint | Description |
| :--- | :--- |
| `GET /api/history/{userId}` | Returns top 10 recent ratings for a user with movie titles. |
| `GET /api/top` | Returns globally top-rated (5.0) movies across the platform. |

## 🏗️ Local Setup
1. Start Cassandra: `docker run --name flex-cassandra -p 9042:9042 -d cassandra:latest`
2. Build and Run: `mvn spring-boot:run`
3. Test: `curl -s http://localhost:8080/api/history/1 | python3 -m json.tool`
