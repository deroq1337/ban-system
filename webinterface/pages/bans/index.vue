<template>
    <div class="flex flex-col gap-6 p-6 bg-gray-50 rounded-xl">
        <div v-if="selectedBan">
            <BanDetails 
                :id="selectedBan" 
                @unban="unban"
                @close="unselectBan"
            />
        </div>
        <div>
            <BanOverview 
                :type="type ?? 'BAN'"
                :bans="bans?.content"
                @selectBan="selectBan"
            />
        </div>
        <div class="pt-6">
            <Pagination 
                :currentPage="currentPage" 
                :totalPages="bans?.totalPages ?? 0"
                @change="goToPage"
                class="text-center"
            />
        </div>
    </div>
</template>

<script setup lang="ts">
import type { PaginatedResponse } from '~/models/PaginatedResponse';
import type { Ban } from '../../models/Ban';

const type: Ref<string | undefined> = ref<string>(useRoute()?.query?.type as string ?? 'BAN');
const PAGE_SIZE: number = 25;
const currentPage: Ref<number> = ref(0);

const bans: Ref<PaginatedResponse<Ban> | undefined> = ref<PaginatedResponse<Ban> | undefined>();
const selectedBan: Ref<number | undefined> = ref<number | undefined>();

const fetchBans = async (): Promise<void> => {
    const { data } : { data: Ref<PaginatedResponse<Ban>> } = await useFetch('/api/bans', {
        query: { 
            page: currentPage.value,
            pageSize: PAGE_SIZE,
            type: type.value
        }
    });
    bans.value = data.value;
}
await fetchBans();

const goToPage = (page: number): void => {
    currentPage.value = page;
}

watch(
    () => currentPage.value,
    async () => await fetchBans()
);

const selectBan = (ban: Ban): void => {
    if (selectedBan.value === ban?.id) {
        selectedBan.value = undefined;
        return;
    }

    selectedBan.value = ban?.id;
}

const unselectBan = (): void => {
    selectedBan.value = undefined;
}

const unban = (id: number): void => {
    if (bans.value) {
        bans.value.content = bans.value?.content?.filter(ban => ban?.id !== id);
    }
}
</script>