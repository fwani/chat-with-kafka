<template>
    <v-card
            class="mx-auto"
            color="grey-lighten-3"
            max-width="1000"
    >
        <v-row>
            <v-responsive
                    class="mx-auto"
                    max-width="344"
            >
                <v-text-field
                        v-model="username"
                        label="유저이름 입력"
                        hide-details="auto"
                ></v-text-field>
            </v-responsive>
            <v-btn v-on:click="handleSubmit">
                Login
            </v-btn>
        </v-row>
    </v-card>
</template>

<script>
export default {
    name: "LoginForm",
    props: {
        onSubmit: Function,
    },
    data: () => {
        return {
            loaded: false,
            loading: false,
            username: ""
        }
    },
    methods: {
        onClick() {
            this.loading = true

            setTimeout(() => {
                this.loading = false
                this.loaded = true
            }, 2000)
        },
        handleSubmit() {
            this.onSubmit(this.username)
        }
    }
}
</script>

<style scoped>

</style>