<template>
  <v-container fluid class="full-height">
    <v-row class="fill-height">
      <v-col
        cols="12"
        sm="10"
        md="8"
        lg="6"
        xl="4"
      >
        <v-card class="mb-6" outlined>
          <v-card-title>
            Title
          </v-card-title>
          <v-card-text>
            <div class="subtitle-2">
              Subtitle
            </div>
          </v-card-text>
          <v-divider class="mx-4"></v-divider>
          <v-card-text>
            <v-text-field
                          flat
                          single-line
                          ></v-text-field>
          </v-card-text>
        <v-col cols="12" class="pa-0 fill-height" justify-self='center'>
          <v-row
            align="center"
            justify="center"
            class="fill-height"
          >
            <v-card outlined>
              <v-card-title>
                Why is there extra whitespace below this v-card?
              </v-card-title>
              <v-card-text>
                <div class="subtitle-2">
                  Centered card
                </div>
              </v-card-text>
            </v-card>
          </v-row>
        </v-col>
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
