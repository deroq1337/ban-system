<template>
    <div class="max-w-md mx-auto p-6 bg-white rounded-lg shadow-xl relative">
        <button 
            class="absolute top-2 right-2 text-gray-600 hover:text-gray-800 focus:outline-none"
            @click="close"
        >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
        </button>

        <div 
            v-if="status === 'idle' || unbanStatus === 'idle'" 
            class="flex justify-center my-6"
        >
            <AnimationLoadingSpinner />
        </div>
        <div v-else-if="status === 'error'">
            <MessageError
                :canRetry="true"
                @retry="refresh"
            />
        </div>
        <div v-else-if="unbanStatus === 'error'">
            <MessageError
                :canRetry="true"
                @retry="unban"
            />
        </div>
        <div v-else-if="unbanStatus === 'success'">
            <MessageSuccess
                :title="'Strafe wurde aufgehoben! 🥳'"
                :text="`Die Strafe des Spielers ${ban?.player} wurde aufgehoben.`"
            />
        </div>
        <div v-else-if="status === 'success' && ban">
            <div class="flex items-center gap-4 border-b pb-4 mb-4">
                <NuxtImg 
                    :src="`https://mc-heads.net/avatar/${ban?.player}`" 
                    class="w-16 h-16 border border-gray-300" 
                    :alt="ban?.player"
                />
                <div>
                    <span class="text-xl font-semibold text-gray-800">{{ ban?.player }}</span>
                </div>
            </div>
            <div class="space-y-4">
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Template-ID:</span>
                    <span class="font-medium text-gray-800">{{ ban?.templateId }}</span>
                </div>
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Grund:</span>
                    <span class="font-medium text-gray-800">{{ ban?.reason }}</span>
                </div>
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Gebannt von:</span>
                    <span class="font-medium text-gray-800">{{ ban?.bannedBy }}</span>
                </div>
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Von:</span>
                    <span class="font-medium text-gray-800">{{ formatDate(ban?.bannedAt) }}</span>
                </div>
                <div class="flex justify-between text-sm text-gray-600">
                    <span>Bis:</span>
                    <span class="font-medium text-gray-800">{{ formatDate(ban?.expiresAt) }}</span>
                </div>
            </div>
            <div class="flex justify-center pt-6">
                <button 
                    class="px-4 py-2 bg-red-600 text-white rounded-xl hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
                    @click="unban"
                >
                    Strafe aufheben
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { BanListEntry } from '~/models/BanListEntry';

const props = defineProps({
    id: {
        type: Number,
        required: true,
    },
});
const emit = defineEmits(['close', 'unban']);

const { status, data: ban, refresh }: { status: Ref<string>, data: Ref<BanListEntry>, refresh: any } = await useFetch(`/api/bans/${props.id}`);
const unbanStatus: Ref<string | undefined> = ref<string | undefined>(undefined);

const unban = async (): Promise<void> => {
    unbanStatus.value = 'idle';
    const { status }: { status: Ref<string> } = await useFetch<void>('/api/unban', {
        method: 'POST',
        body: { id: props?.id }
    });

    unbanStatus.value = status.value;
}

const close = (): void => {
    emit('close');
    if (unbanStatus.value === 'success') {
        emit('unban', props.id);
    }
}
</script>