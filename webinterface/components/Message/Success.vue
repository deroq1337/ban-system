<template>
    <div class="flex flex-col items-center p-6 bg-green-50 border border-green-200 rounded-lg shadow-md mb-6">
        <h2 class="text-lg font-semibold text-green-700 mb-4">{{ title }}</h2>
        <p class="text-green-600 text-center mb-4">{{ text }}</p>
    </div>
</template>

<script setup lang="ts">
const props = defineProps({
    title: {
        type: String,
        required: false,
        default: 'Aktion erfolgreich! 🥳'
    },
    text: {
        type: String,
        required: false,
        default: 'Die Aktion wurde erfolgreich ausgeführt.'
    }
});
</script>