<template>
  <v-container>
    <div>
      <iframe
        src="http://192.168.1.188:9000/javascript_simple.html"
        frameborder="0"
        width="640"
        height="480"
        scrolling="no">
      </iframe>
    </div>
  </v-container>
</template>

<script>
export default {
  name: 'Cameras',
  data() {
    return {
    };
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
  },

  methods: {

  },
};

</script>

<style>

</style>
