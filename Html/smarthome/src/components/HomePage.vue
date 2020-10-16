<template>
  <v-container>
    <v-row>
      <v-col sm="12">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Security Alarm
          </v-card-title>
          <v-card-actions>
            <v-btn-toggle>
              <!--v-img src="./assets/disarmed_shade.png" >

              </v-img-->
              Disarm
            </v-btn-toggle>
            <v-spacer></v-spacer>
            <v-btn-toggle>
              <!--v-img src="./assets/disarmed_shade.png" >

              </!--v-img-->
              Arm in
            </v-btn-toggle>
            <v-spacer></v-spacer>
            <v-btn-toggle>
              <!--v-img src="./assets/disarmed_shade.png" >

              </!--v-img-->
              Arm out
            </v-btn-toggle>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Temperature
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Humidity
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="4">
        <v-card
          elevation="4"
          height="200">
          <v-card-title>
            Air quality
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Dashboard',
  created() {
    this.$vuetify.theme.dark = true;
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
