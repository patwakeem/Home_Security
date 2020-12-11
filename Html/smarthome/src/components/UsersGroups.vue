<template>
  <v-container>
    <v-row>
      <v-tooltip top>
        <template v-slot:activator="{ on }">
          <v-btn
            v-on="on"
            class="new-user mx-2"
            fab
            dark
            color="blue"
            @click="addUser = !addUser"
          >
            <v-icon v-if="addUser == false" dark>
              mdi-plus
            </v-icon>
            <v-icon v-else dark>
              mdi-minus
            </v-icon>
          </v-btn>
        </template>
        <span>Add User</span>
      </v-tooltip>

    <v-expand-transition>
      <v-card class="new-user" v-if="addUser == true" v-model="valid">
        <v-container>
          <v-row>
            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="firstname"
                :rules="rules.nameRules"
                :counter="10"
                label="First name"
                required
              ></v-text-field>
            </v-col>

            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="lastname"
                :rules="rules.nameRules"
                :counter="10"
                label="Last name"
                required
              ></v-text-field>
            </v-col>

            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="email"
                :rules="rules.emailRules"
                label="E-mail"
                required
              ></v-text-field>
            </v-col>

            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="username"
                :rules="rules.username"
                label="Username"
                required
              ></v-text-field>
            </v-col>

            <v-col
              cols="12"
              sm="6"
            >
              <v-select
                v-model="selectedGroups"
                :items="groups"
                chips
                label="Groups"
                multiple
                solo
              ></v-select>
            </v-col>

            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="password"
                :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                :rules="[rules.required, rules.min]"
                :type="show1 ? 'text' : 'password'"
                name="input-10-1"
                label="Password"
                hint="At least 8 characters"
                counter
                @click:append="show1 = !show1"
              ></v-text-field>
            </v-col>

            <v-col
              cols="12"
              md="4"
            >
              <v-text-field
                v-model="retypepassword"
                :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                :rules="[rules.required, rules.min]"
                :type="show2 ? 'text' : 'password'"
                name="input-10-1"
                label="Re-type your password"
                hint="At least 8 characters"
                counter
                @click:append="show2 = !show2"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row justify="end">
            <v-btn
             class="form-btn"
             @click="clear()">
              Clear
            </v-btn>
            <v-btn
             class="form-btn"
             @click="submit()">
              Submit
            </v-btn>
          </v-row>
        </v-container>
      </v-card>

    <!-- <v-card v-if="addUser == true"
      v-model="addUser"
      class="new-user mx-auto"
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
    </v-card> -->
    </v-expand-transition>
    </v-row>
    <v-row justify="center">
      <v-data-table
        :headers="headers"
        :items="users"
        :items-per-page="5"
        class="elevation-1"
      ></v-data-table>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    // name: '',
    email: '',
    // select: null,
    users: [],
    groups: [],
    selectedGroups: [],
    // checkbox: null,
    addUser: false,
    valid: false,
    firstname: '',
    lastname: '',
    password: '',
    retypepassword: '',
    show1: false,
    show2: false,
    rules: {
      required: (value) => !!value || 'Required.',
      min: (v) => v.length >= 8 || 'Min 8 characters',
      username: (value) => value.length >= 8 || 'Min 8 characters',
      nameRules: [
        (v) => !!v || 'Name is required',
        (v) => v.length <= 10 || 'Name must be less than 10 characters',
      ],
      emailRules: [
        (v) => !!v || 'E-mail is required',
        (v) => /.+@.+/.test(v) || 'E-mail must be valid',
      ],
    },
    // step: 1,
    // emailRules: [
    //   (v) => !!v || 'E-mail is required',
    //   (v) => /.+@.+/.test(v) || 'E-mail must be valid',
    // ],
    headers: [
      {
        text: 'First Name',
        align: 'start',
        value: 'first_name',
      },
      { text: 'Last Name', value: 'last_name' },
      { text: 'Username', value: 'username' },
      { text: 'Password', value: 'password' },
      { text: 'Group', value: 'group' },
      { text: 'Email', value: 'email' },
    ],
  }),

  async created() {
    await this.getUsers();
    await this.getGroups();
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    // currentTitle() {
    //   switch (this.step) {
    //     case 1: return 'Register User';
    //     case 2: return 'Create a password';
    //     default: return 'Account created';
    //   }
    // },
  },

  methods: {
    getUsers() {
      fetch('http://interactivehome.ddns.net:8080/user')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
            // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.users = data;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    getGroups() {
      fetch('http://interactivehome.ddns.net:8080/group')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
            // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          let i;
          for (i = 0; i < data.length; i += 1) {
            this.groups.push(data[i].name);
          }
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    submit() {
      const requestBody = {
        first_name: this.firstname,
        last_name: this.lastname,
        email: this.email,
        username: this.username,
        password: this.password,
        groups: this.selectedGroups,
      };

      alert(JSON.stringify(requestBody));
      const resource = 'user';

      fetch(`http://interactivehome.ddns.net:8080/${resource}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        })
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }
        })
        .then(this.getSensors(this.activeTab, true));
    },

  },
};

</script>

<style scoped>
.new-user {
  margin-bottom: 30px;
}
.form-btn {
  margin-right: 30px;
}
.form-btn :hover {
  color:dodgerblue;
}
</style>
