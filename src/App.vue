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
    error.value = "Backend unreachable on 8080.";
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
  <div class="main-container">
    <div class="film-strip left"></div>
    
    <div class="dashboard">
      <div class="card">
        <div class="header">
          <div>
            <h1>FlexStream Dashboard</h1>
            <p class="status">Connected to Redis | {{ ratings.length }} Titles</p>
          </div>
          <button @click="fetchHistory" class="refresh-btn">Refresh Data</button>
        </div>

        <div class="toolbar">
          <input v-model="searchQuery" placeholder="Search movies..." class="search-box" />
          <select v-model="sortBy" class="sort-box">
            <option value="recent">Most Recent</option>
            <option value="rating">Top Rated</option>
          </select>
        </div>

        <div v-if="loading" class="state">Fetching records...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        
        <div v-else class="table-wrap">
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
                  <span :class="['rating-badge', r.rating >= 4 ? 'good' : 'bad']">
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
body { background: var(--bg); color: var(--text); font-family: sans-serif; margin: 0; padding: 0; }

.main-container {
  display: flex;
  justify-content: space-between;
  min-height: 100vh;
  background: #000;
}

.film-strip {
  width: 120px;
  background-image: url('/film-frames.png');
  background-repeat: repeat-y;
  background-size: contain;
  opacity: 0.6;
  flex-shrink: 0;
}

.dashboard {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 40px 0;
  background: var(--bg);
}

.card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 12px;
  width: 90%;
  max-width: 900px;
  padding: 30px;
  box-shadow: 0 20px 50px rgba(0,0,0,0.7);
}

.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
h1 { margin: 0; font-size: 1.8rem; color: #fff; letter-spacing: -0.5px; }
.status { margin: 5px 0 0; color: #8b949e; font-size: 0.9rem; }

.toolbar { display: flex; gap: 15px; margin-bottom: 30px; }
.search-box, .sort-box { 
  background: #0d1117; 
  border: 1px solid var(--border); 
  color: #fff; 
  padding: 12px; 
  border-radius: 8px;
  font-size: 1rem;
}
.search-box { flex-grow: 1; }

.table-wrap { border: 1px solid var(--border); border-radius: 8px; overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #21262d; padding: 15px; text-align: left; color: #8b949e; font-size: 0.85rem; border-bottom: 1px solid var(--border); }
td { padding: 15px; border-bottom: 1px solid var(--border); font-size: 1rem; }
.center { text-align: center; }

.rating-badge { 
  font-weight: bold; 
  padding: 5px 12px; 
  border-radius: 20px; 
  font-size: 0.9rem;
}
.good { background: rgba(46, 160, 67, 0.2); color: #3fb950; border: 1px solid rgba(63, 185, 80, 0.3); }
.bad { background: rgba(248, 81, 73, 0.2); color: #f85149; border: 1px solid rgba(248, 81, 115, 0.3); }

.refresh-btn { 
  background: var(--blue); 
  color: #0d1117; 
  border: none; 
  padding: 10px 20px; 
  border-radius: 8px; 
  font-weight: bold;
  cursor: pointer;
}
</style>
