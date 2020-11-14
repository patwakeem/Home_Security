<template>
  <v-container fluid>
    <v-radio-group
     label="Active Alarm"
     v-model="activeAlarmGroup"
     @change="setActiveAlarm">
      <v-radio
       label="House Alarm">
      </v-radio>
      <v-radio
       label="Summer House Alarm">
      </v-radio>
    </v-radio-group>
    <v-radio-group
     label="Theme"
     v-model="themeGroup"
     @change="setTheme">
      <v-radio label="Dark">
      </v-radio>
      <v-radio label="Light">
      </v-radio>
    </v-radio-group>
  </v-container>
</template>

<script>

export default {
  name: 'Settings',
  data() {
    return {
      activeAlarmGroup: 0,
      themeGroup: null,
    };
  },

  created() {
    this.activeAlarmGroup = parseInt(this.activeAlarm, 10);

    const theme = localStorage.getItem('theme');
    if ((theme === null) || (theme != null && theme === 'dark')) {
      this.themeGroup = 0;
      this.$vuetify.theme.dark = true;
    } else if (theme != null && theme === 'light') {
      this.themeGroup = 1;
      this.$vuetify.theme.dark = false;
    }
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    activeAlarm() {
      return this.$store.state.activeAlarm;
    },
  },

  methods: {

    setActiveAlarm() {
      localStorage.removeItem('activeAlarm');
      localStorage.setItem('activeAlarm', this.activeAlarmGroup);
      this.$store.commit('setActiveAlarm', this.activeAlarmGroup);
      this.$store.commit('setActiveAlarmName', this.activeAlarmGroup);
    },

    setTheme() {
      const isDarkTheme = this.themeGroup === 0;
      this.$vuetify.theme.dark = isDarkTheme;
      localStorage.removeItem('theme');
      localStorage.setItem('theme', isDarkTheme ? 'dark' : 'light');
    },
  },
};
</script>
