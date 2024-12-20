<template>
    <div class="flex justify-center gap-4 items-center">
        <button 
            :disabled="currentPage === 0" 
            @click="goToPage(currentPage - 1)" 
            class="px-4 py-2 bg-gray-300 rounded-md disabled:bg-gray-200"
        >
            🡠
        </button>
        <span 
            v-for="page in displayedPages" 
            :key="page" 
            @click="goToPage(page - 1)" 
            :class="{'font-bold': page - 1 === currentPage}" 
            class="cursor-pointer"
        >
            {{ page }}
        </span>
        <button 
            :disabled="currentPage === totalPages - 1" 
            @click="goToPage(currentPage + 1)" 
            class="px-4 py-2 bg-gray-300 rounded-md disabled:bg-gray-200"
        >
            🡢
        </button>
    </div>
</template>
  
<script setup lang="ts">
const props = defineProps({
    currentPage: {
        type: Number,
        required: true
    },
    totalPages: {
        type: Number,
        required: true
    }
});

const emit = defineEmits(['change']);
const DISPLAYED_PAGES: number = 5;

const displayedPages: ComputedRef<number[]> = computed<number[]>(() => {
    let startPage: number = Math.max(props.currentPage - 2, 0);
    let endPage: number = Math.min(startPage + DISPLAYED_PAGES - 1, props.totalPages - 1);

    if (endPage - startPage < DISPLAYED_PAGES - 1) {
        startPage = Math.max(endPage - DISPLAYED_PAGES + 1, 0);
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index + 1);
});

const goToPage = (page: number): void => {
    if (page >= 0 && page < props.totalPages) {
        emit('change', page);
    }
};
</script>
