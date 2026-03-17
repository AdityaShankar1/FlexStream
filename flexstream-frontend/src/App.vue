<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const ratings = ref([]);
const searchQuery = ref('');
const sortBy = ref('recent');
const error = ref(null);
const loading = ref(true);

const fetchHistory = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await axios.get('/api/history/1');
    ratings.value = res.data.map(item => {
      return typeof item === 'string' ? JSON.parse(item) : item;
    });
  } catch (err) {
    error.value = "Backend unreachable on 8080. Is Spring running?";
  } finally {
    loading.value = false;
  }
};

const processedRatings = computed(() => {
  let list = [...ratings.value].filter(r => 
    r.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
  if (sortBy.value === 'rating') {
    return list.sort((a, b) => b.rating - a.rating);
  }
  return list; 
});

onMounted(fetchHistory);
</script>

<template>
  <div class="main-layout">
    <div class="film-strip left"></div>

    <div class="dashboard">
      <div class="card">
        <div class="header">
          <div>
            <h1>FlexStream Dashboard</h1>
            <p class="status-text">Connected to Redis | {{ ratings.length }} Titles</p>
          </div>
          <button @click="fetchHistory" class="btn-refresh">Refresh Data</button>
        </div>

        <div class="toolbar">
          <input v-model="searchQuery" placeholder="Search movies..." class="search-input" />
          <select v-model="sortBy" class="select-input">
            <option value="recent">Most Recent</option>
            <option value="rating">Top Rated</option>
          </select>
        </div>

        <div v-if="loading" class="info">Fetching...</div>
        <div v-else-if="error" class="error-msg">{{ error }}</div>
        
        <div v-else class="table-container">
          <table>
            <thead>
              <tr>
                <th>Title</th>
                <th class="center">Rating</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="r in processedRatings" :key="r.movieId">
                <td>{{ r.title }}</td>
                <td class="center">
                  <span :class="['score', r.rating >= 4 ? 'high' : 'low']">
                    {{ r.rating }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="film-strip right"></div>
  </div>
</template>

<style>
:root { --bg: #0d1117; --card: #161b22; --border: #30363d; --text: #c9d1d9; --blue: #58a6ff; }
body { background: var(--bg); color: var(--text); font-family: -apple-system, sans-serif; margin: 0; padding: 0; }

.main-layout { display: flex; min-height: 100vh; width: 100vw; }

.film-strip { 
  width: 100px; 
  background-image: url('/film-frames.png');
  background-repeat: repeat-y;
  background-size: contain;
  opacity: 0.5;
  flex-shrink: 0;
}

.dashboard { flex-grow: 1; display: flex; justify-content: center; padding: 40px 20px; overflow-y: auto; }

.card { 
  background: var(--card); 
  border: 1px solid var(--border); 
  border-radius: 8px; 
  width: 100%; 
  max-width: 800px; 
  padding: 24px; 
  height: fit-content;
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
}

.header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; }
h1 { margin: 0; font-size: 1.5rem; color: white; }
.status-text { margin: 4px 0 0; font-size: 0.85rem; color: #8b949e; }
.toolbar { display: flex; gap: 12px; margin-bottom: 24px; }
.search-input, .select-input { background: #0d1117; border: 1px solid var(--border); color: white; padding: 10px; border-radius: 6px; }
.search-input { flex-grow: 1; }
.table-container { border: 1px solid var(--border); border-radius: 6px; overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #21262d; padding: 12px; text-align: left; font-size: 0.8rem; color: #8b949e; border-bottom: 1px solid var(--border); }
td { padding: 14px 12px; border-bottom: 1px solid var(--border); font-size: 0.95rem; }
.center { text-align: center; }
.score { font-weight: bold; padding: 4px 10px; border-radius: 20px; font-size: 0.85rem; }
.high { background: rgba(46, 160, 67, 0.2); color: #3fb950; }
.low { background: rgba(248, 81, 73, 0.2); color: #f85149; }
.btn-refresh { background: var(--blue); color: #0d1117; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; font-weight: 600; }
</style>
