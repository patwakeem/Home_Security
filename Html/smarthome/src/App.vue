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
        <v-list-item to="/dashboard">
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              Dashboard
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
    </v-app-bar>

    <v-main>
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
  }),
  async created() {
    this.$vuetify.theme.dark = true;
  },
};
</script>

<style scoped>
.nav-link {
  text-decoration: none;
  color: inherit;
}
</style>
