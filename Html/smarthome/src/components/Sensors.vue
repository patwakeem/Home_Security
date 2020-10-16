<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Sensors
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Cameras
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  created() {
    this.$vuetify.theme.dark = true;
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
