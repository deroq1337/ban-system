<template>
    <tr class="border-t hover:bg-gray-100 transition duration-300 ease-in-out transform hover:scale-[1.005]">
        <td class="px-6 py-4 flex items-center space-x-3">
            <NuxtImg 
                :src="`https://mc-heads.net/avatar/${ban?.player}`" 
                class="w-12 h-12 border-2 border-gray-300 transform transition duration-300 ease-in-out hover:scale-125" 
                :alt="ban?.player"
            />
            <span class="text-lg font-medium text-gray-800 truncate w-32 md:w-full">{{ ban?.player }}</span>
        </td>
        <td class="px-6 py-4 text-sm text-gray-700">{{ ban?.templateId }}</td>
        <td class="px-6 py-4 text-sm text-gray-700">{{ formatDate(ban?.expiresAt) }}</td>
        <td class="px-6 py-4 text-sm text-red-600 font-semibold">{{ type }}</td>
        <td class="px-6 py-4 text-sm">
            <div 
                class="cursor-pointer p-2 hover:bg-gray-200 rounded-full transition transform"
                @click="$emit('selectBan', ban)"
                title="Details"
            >
                <svg 
                    width="24px" 
                    height="24px" 
                    viewBox="0 0 12 12" 
                    fill="none" 
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <circle cx="2" cy="6" r="1" fill="black" />
                    <circle cx="6" cy="6" r="1" fill="black" />
                    <circle cx="10" cy="6" r="1" fill="black"/>
                </svg>
            </div>
        </td>
    </tr>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import type { Ban } from '~/models/Ban';

const props = defineProps({
    type: {
        type: String,
        required: true
    },
    ban: {
        type: Object as PropType<Ban>,
        required: true
    }
});
const emit = defineEmits(['selectBan']);
</script>