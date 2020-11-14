<template>
  <v-app id="smarthome">
    <v-navigation-drawer
      v-model="drawer"
      app
      clipped
    >
      <v-list dense>
        <v-list-item to="/">
          <v-list-item-action>
            <v-icon>mdi-home</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Main
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/activity">
          <v-list-item-action>
            <v-icon>mdi-poll</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Activity
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/users">
          <v-list-item-action>
            <v-icon>mdi-account-multiple</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Users/Groups
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/sensors">
          <v-list-item-action>
            <v-icon>mdi-remote</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Sensors
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/cameras">
          <v-list-item-action>
            <v-icon>mdi-video</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Cameras
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/settings">
          <v-list-item-action>
            <v-icon>mdi-cog</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Settings
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      app
      clipped-left
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Home Security</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-icon
      v-on="on"
      v-if="loggedIn == true" color="blue"
      align-end
      >
      mdi-account
      </v-icon>
      <v-icon
       v-if="loggedIn == false" color="grey"
       align-end
      >
       mdi-account
      </v-icon>

    </v-app-bar>

    <v-main>
      <v-alert v-if="alarmOn"
       prominent
       justify-center
       align-baseline
       type="error">
        <v-row align="center">
          <v-col class="grow">
            Alarm is On !!!
          </v-col>
          <v-col class="shrink">
            <v-btn color="blue">Stop Alarm</v-btn>
          </v-col>
        </v-row>
      </v-alert>
      <v-row justify="center" v-if="loggedIn == false">
        <v-overlay
            :absolute="true"
            :opacity="1"
            :value="true"
          >
          <v-dialog
            v-model="dialog"
            persistent
            max-width="600px"
          >
            <v-card>
              <v-card-title>
                <span class="headline">Login</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        label="Username*"
                        v-model="username"
                        required
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        label="Password*"
                        type="password"
                        v-model="password"
                        required
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="resetPassword()"
                >
                  Forgot Password
                </v-btn>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="login()"
                >
                  Login
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-overlay>
      </v-row>
      <router-view/>
    </v-main>

    <v-footer app>
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>

export default {
  name: 'smarthomeApp',
  data: () => ({
    drawer: null,
    dialog: true,
    username: null,
    password: null,
  }),

  async created() {
    const theme = localStorage.getItem('theme');
    if ((theme === null) || (theme != null && theme === 'dark')) {
      this.$vuetify.theme.dark = true;
    } else if (theme != null && theme === 'light') {
      this.$vuetify.theme.dark = false;
    }

    let isLoggedIn = false;
    if (sessionStorage.getItem('loggedIn')) {
      isLoggedIn = sessionStorage.getItem('loggedIn');
      this.$store.commit('setLoggedIn', isLoggedIn);
    }

    if (localStorage.getItem('activeAlarm')) {
      const activeAlarm = localStorage.getItem('activeAlarm');
      this.activeAlarmGroup = parseInt(activeAlarm, 10);
      this.$store.commit('setActiveAlarm', parseInt(activeAlarm, 10));
    } else {
      this.activeAlarmGroup = 0;
      localStorage.setItem('activeAlarm', this.activeAlarmGroup);
      this.$store.commit('setActiveAlarm', 0);
    }

    this.getAlarm();
    setInterval(this.getAlarm, 1000);
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    alarmId() {
      return this.$store.state.activeAlarm;
    },
    alarmOn() {
      return this.$store.state.alarmOn;
    },
  },

  methods: {
    login() {
      if (this.username === 'dimzarris@interactivehome.ddns.net'
       && this.password === 'abc123!@#') {
        this.$store.commit('setLoggedIn', true);
        this.$store.commit('setUsername', this.username);
        sessionStorage.setItem('loggedIn', true);
      }
    },

    getAlarm() {
      const aId = parseInt(this.alarmId, 10);
      fetch(`http://interactivehome.ddns.net:8080/alarm/${aId}`)
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.$store.commit('setAlarmOn', data[0].alarm_on);
          this.$store.commit('setAlarmState', data[0].alarm_state);
          // this.setSelectedAlarmState();
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },
  },
};
</script>

<style scoped>
.nav-link {
  text-decoration: none;
  color: inherit;
}
</style>
