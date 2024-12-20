<template>
    <table class="min-w-full table-auto bg-white rounded-lg shadow-lg overflow-hidden">
        <thead>
            <tr class="bg-gradient-to-r from-blue-600 to-purple-500 text-white">
                <th class="px-6 py-3 text-left text-lg">Spieler</th>
                <th class="px-6 py-3 text-left text-lg">Template</th>
                <th class="px-6 py-3 text-left text-lg">Bis</th>
                <th class="px-6 py-3 text-left text-lg">Typ</th>
                <th class="px-6 py-3 text-left text-lg">Aktion</th>
            </tr>
        </thead>
        <tbody v-if="bans?.length > 0">
            <BanOverviewEntry 
                v-for="ban in bans"
                :key="ban?.id"
                :ban="ban"
                :type="type"
                @selectBan="$emit('selectBan', ban)"
            />
        </tbody>
        <tbody v-else>
            <tr>
                <td 
                    colspan="5" 
                    class="text-xl p-4 font-semibold text-center text-gray-500"
                >
                    Es wurden keine Strafen gefunden! <span role="img" aria-label="star_truck">🤩</span>
                </td>
            </tr>
        </tbody>
    </table>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import type { Ban } from '~/models/Ban';

const props = defineProps({
    type: {
        type: String,
        required: true
    },
    bans: {
        type: Object as PropType<Ban[]>,
        required: false,
        default: []
    }
});
const emit = defineEmits(['selectBan']);
</script>