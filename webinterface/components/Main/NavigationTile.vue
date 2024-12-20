<template>
    <NuxtLink :to="to">
        <div 
            class="p-16 rounded-xl text-white text-center border shadow-xl hover:bg-red-600 transform transition-all hover:scale-110" 
            :style="{ backgroundColor: color }"
        >
            <slot name="icon" />
            <span class="block mt-4 text-xl font-semibold">{{ title }}</span>
        </div>
    </NuxtLink>
</template>

<script setup lang="ts">
const props = defineProps({
    to: {
        type: String,
        required: true
    },
    title: {
        type: String,
        required: true
    },
    color: {
        type: String,
        required: false,
        default: '#ef4444'
    }
});
</script>