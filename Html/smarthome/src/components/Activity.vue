<template>
  <v-container>
    <v-row>
      <v-col
        cols="4"
        sm="12"
        md="4"
      >
        <v-menu
          v-model="menuFrom"
          :close-on-content-click="false"
          :nudge-right="40"
          color="blue"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="dateFrom"
              label="From date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="dateFrom"
            color="blue"
            @input="menuFrom = false"
          ></v-date-picker>
        </v-menu>
      </v-col>
      <v-col
        cols="4"
        sm="12"
        md="4"
      >
        <v-menu
          v-model="menuTo"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="dateTo"
              label="To date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="dateTo"
            color="blue"
            @input="menuTo = false"
          ></v-date-picker>
        </v-menu>
      </v-col>
      <v-row>
        <v-col cols="4" sm="12">
          <v-select
          v-model="sensorDescription"
          :items="doorSensors"
          chips
          label="Sensor Type"
          multiple
          outlined
          ></v-select>
        </v-col>
      </v-row>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card>
          <div class="subheading font-weight-light grey--text">
              Temperature
          </div>
          <v-sheet
          elevation="5"
          >
          <v-sparkline
              :labels="labels"
              :value="value1"
              color="orange"
              line-width=".5"
              padding="16"
          ></v-sparkline>
          </v-sheet>
          <v-divider class="my-2"></v-divider>

          <div class="subheading font-weight-light grey--text">
              Humidity
          </div>
          <v-sheet
          elevation="5"
          >
          <v-sparkline
              :labels="labels"
              :value="value2"
              color="cyan"
              line-width=".5"
              padding="16"
          ></v-sparkline>
          </v-sheet>

        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: (vm) => ({
    dialog: true,
    username: null,
    password: null,
    dateFrom: new Date().toISOString().substr(0, 10),
    dateTo: new Date().toISOString().substr(0, 10),
    fromDateFormatted: vm.formatDate(new Date().toISOString().substr(0, 10)),
    toDateFormatted: vm.formatDate(new Date().toISOString().substr(0, 10)),
    menuFromDate: false,
    menuToDate: false,
    menuFrom: false,
    menuTo: false,
    fromTime: null,
    toTime: null,
    menuFromTime: false,
    menuToTime: false,
    doorSensors: ['Environment', 'Door', 'Motion', 'Camera'],
    sensorDescription: ['Main Entrance', 'Entrance', 'Window', 'Kitchen'],
    labels: [
      '12am',
      '3am',
      '6am',
      '9am',
      '12pm',
      '3pm',
      '6pm',
      '9pm',
    ],
    value1: [
      200,
      675,
      410,
      390,
      310,
      460,
      250,
      240,
    ],
    value2: [
      10,
      8,
      7,
      6,
      5,
      7,
      11,
      15,
    ],
  }),

  computed: {
    computedDateFormatted() {
      return this.formatDate(this.date);
    },
    loggedIn() {
      return this.$store.state.loggedIn;
    },
  },

  watch: {
    date(val) {
      this.dateFormatted = this.formatDate(val);
    },
  },

  methods: {
    formatDate(date) {
      if (!date) return null;

      const [year, month, day] = date.split('-');
      return `${year}-${month}-${day}`;
    },
    parseDate(date) {
      if (!date) return null;

      const [month, day, year] = date.split('-');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },
  },
};
</script>
