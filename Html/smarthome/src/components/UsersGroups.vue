<template>
  <v-container>
    <v-row>
    <v-btn
      class="mx-2"
      fab
      dark
      color="blue"
      @click="onAddUser()"
    >
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>

    <v-card v-if="addUser == true"
      v-model="addUser"
      class="mx-auto"
      max-width="500"
    >
      <v-card-title class="title font-weight-regular justify-space-between">
        <span>{{ currentTitle }}</span>
        <v-avatar
          color="primary lighten-2"
          class="subheading white--text"
          size="24"
          v-text="step"
        ></v-avatar>
      </v-card-title>

      <v-window v-model="step">
        <v-window-item :value="1">
          <v-card-text>
            <v-text-field
              label="Email"
              value="user@interactivehome.com"
              :rules="emailRules"
            ></v-text-field>
            <span class="caption grey--text text--darken-1">
              This is the email you will use to login to your Vuetify account
            </span>
          </v-card-text>
        </v-window-item>

        <v-window-item :value="2">
          <v-card-text>
            <v-text-field
              label="Password"
              type="password"
            ></v-text-field>
            <v-text-field
              label="Confirm Password"
              type="password"
            ></v-text-field>
            <span class="caption grey--text text--darken-1">
              Please enter a password for your account
            </span>
          </v-card-text>
        </v-window-item>

        <v-window-item :value="3">
          <div class="pa-4 text-center">
            <v-img
              class="mb-4"
              contain
              height="128"
              src="../assets/home_disarm.png"
            ></v-img>
            <h3 class="title font-weight-light mb-2">
              Welcome to Interactivehome
            </h3>
            <span class="caption grey--text">Thanks for signing up!</span>
          </div>
        </v-window-item>
      </v-window>

      <v-divider></v-divider>

      <v-card-actions>
        <v-btn
          :disabled="step === 1"
          text
          @click="step--"
        >
          Back
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="step === 3"
          color="primary"
          depressed
          @click="step++"
        >
          Next
        </v-btn>
      </v-card-actions>
    </v-card>
    </v-row>
    <v-data-table
      :headers="headers"
      :items="users"
      :items-per-page="5"
      class="elevation-1"
    ></v-data-table>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    step: 1,
    addUser: false,
    emailRules: [
      (v) => !!v || 'E-mail is required',
      (v) => /.+@.+/.test(v) || 'E-mail must be valid',
    ],
    headers: [
      {
        text: 'Name',
        align: 'start',
        sortable: false,
        value: 'name',
      },
      { text: 'Group', value: 'group' },
      { text: 'Email', value: 'email' },
    ],
    users: [
      {
        name: 'Dimitris',
        group: 'administrators',
        email: 'dimitris@interactivehome.com',
      },
    ],
  }),

  created() {
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    currentTitle() {
      switch (this.step) {
        case 1: return 'Register User';
        case 2: return 'Create a password';
        default: return 'Account created';
      }
    },
  },

  methods: {
    onAddUser() {
      this.addUser = true;
    },
  },
};
</script>
