<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const ratings = ref([]);
const error = ref(null);
const loading = ref(true);

const fetchHistory = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await axios.get('/api/history/1');
    // Handle both raw string lists and object lists
    ratings.value = res.data.map(item => {
      return typeof item === 'string' ? JSON.parse(item) : item;
    });
  } catch (err) {
    error.value = "Failed to fetch data: " + (err.response?.status || "Check Docker/Spring");
  } finally {
    loading.value = false;
  }
};

onMounted(fetchHistory);
</script>

<template>
  <div class="dashboard">
    <div class="card">
      <div class="header">
        <div>
          <h1>FlexStream History</h1>
          <p class="subtitle">User ID: 1 | Redis Cache Active</p>
        </div>
        <button @click="fetchHistory" class="refresh-icon" title="Refresh">🔄</button>
      </div>

      <div v-if="loading" class="loader">Accessing Redis nodes...</div>
      <div v-else-if="error" class="error-msg">{{ error }}</div>
      
      <div v-else class="table-container">
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th class="center">Rating</th>
              <th class="right">Movie ID</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in ratings" :key="r.movieId">
              <td class="title-cell">{{ r.title }}</td>
              <td class="center">
                <span :class="['rating-badge', r.rating >= 4 ? 'high' : 'low']">
                  {{ r.rating }}
                </span>
              </td>
              <td class="right dim">{{ r.movieId }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style>
:root {
  --bg: #0f172a;
  --card: #1e293b;
  --text: #f8fafc;
  --accent: #38bdf8;
  --border: #334155;
}

body { 
  background: var(--bg); 
  color: var(--text); 
  font-family: 'Inter', -apple-system, sans-serif; 
  margin: 0; 
}

.dashboard { display: flex; justify-content: center; padding: 40px 20px; }

.card { 
  background: var(--card); 
  padding: 30px; 
  border-radius: 12px; 
  width: 100%; 
  max-width: 800px; 
  box-shadow: 0 10px 25px rgba(0,0,0,0.3);
  border: 1px solid var(--border);
}

.header { 
  display: flex; 
  justify-content: space-between; 
  align-items: flex-start;
  margin-bottom: 30px;
}

h1 { margin: 0; font-size: 1.5rem; color: var(--accent); }
.subtitle { margin: 5px 0 0; color: #94a3b8; font-size: 0.85rem; }

.refresh-icon {
  background: none; border: none; font-size: 1.2rem; cursor: pointer; padding: 5px;
  transition: transform 0.3s;
}
.refresh-icon:hover { transform: rotate(180deg); }

.table-container { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; text-align: left; }
th { padding: 12px; border-bottom: 2px solid var(--border); font-size: 0.8rem; text-transform: uppercase; color: #94a3b8; }
td { padding: 14px 12px; border-bottom: 1px solid var(--border); font-size: 0.95rem; }

.title-cell { font-weight: 500; }
.center { text-align: center; }
.right { text-align: right; }
.dim { color: #64748b; font-size: 0.8rem; }

.rating-badge {
  padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 0.8rem;
}
.high { background: #065f46; color: #34d399; }
.low { background: #7f1d1d; color: #f87171; }

.loader { padding: 40px; text-align: center; color: var(--accent); }
.error-msg { padding: 20px; background: #451a1a; color: #f87171; border-radius: 6px; }
</style>
