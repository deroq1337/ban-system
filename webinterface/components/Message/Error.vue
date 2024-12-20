<template>
    <div class="flex flex-col items-center p-6 bg-red-50 border border-red-200 rounded-lg shadow-md mb-6">
        <h2 class="text-lg font-semibold text-red-700 mb-4">Etwas ist schiefgelaufen! 😢</h2>
        <p class="text-gray-700 text-center mb-4">Versuche es erneut oder kontaktiere einen Administrator.</p>
        <button 
            v-if="canRetry"
            @click="$emit('retry')"
            class="px-4 py-2 bg-red-600 text-white rounded-xl hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
        >
            Erneut versuchen
        </button>
    </div>
</template>

<script setup lang="ts">
const props = defineProps({
    canRetry: {
        type: Boolean,
        required: true
    }
});
const emit = defineEmits(['retry']);
</script>