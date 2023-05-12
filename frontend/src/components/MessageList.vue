<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="1000px">
                <v-card-text>
                    <div class="font-weight-bold ms-1 mb-2">
                        Messages
                    </div>

                    <v-timeline density="compact" align="start">
                        <v-timeline-item
                                v-for="message in messages"
                                :key="message.time"
                                :dot-color="message.color"
                                size="x-small"
                        >
                            <div class="mb-4">
                                <div class="font-weight-normal">
                                    <strong>{{ message.sender }}</strong> @{{ message.timestamp }}
                                </div>
                                <div>{{ message.message }}</div>
                            </div>
                        </v-timeline-item>
                    </v-timeline>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: "MessageList",
    props: {
      messages: Array,
        currentUser: Object
    },
    data: () => {
        return {
            sender: "",
            content: ""
        }
    },
    methods: {
    }
}
</script>

<style scoped>

</style>