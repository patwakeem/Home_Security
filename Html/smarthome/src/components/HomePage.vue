<template>
  <v-container fluid fill-height>
    <v-row>
      <v-col>
        <v-card
          height="100%"
          >
            <v-card-title class="justify-center">
              <h2>
                Security Alarm
              </h2>
            </v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6" lg="4" md="6" sm="12" xs="12">
          <v-card class="vcard" fill-height v-on:click="arm(0)"
            elevation="4"
          >
            <v-card-title class="justify-center">
              Disarm
              <v-card-text v-if="alarmState == 0 && alarmState != null">
                <div class="d-flex flex-column justify-space-between align-center">
                  <v-img class="image" src="../assets/disarm.png"/>
                </div>
              </v-card-text>
              <v-card-text v-else-if="alarmState != 0 && alarmState != null">
                <div class="d-flex flex-column justify-space-between align-center">
                  <v-img src="../assets/disarm_disabled.png"/>
                </div>
              </v-card-text>
              <v-card-text v-if="alarmState == null">
                <div class="d-flex flex-column justify-space-between align-center">
                  <v-progress-circular
                    :size="70"
                    :width="7"
                    color="grey"
                    indeterminate
                  ></v-progress-circular>
                </div>
              </v-card-text>
            </v-card-title>
          </v-card>
      </v-col>
      <v-col cols="6" lg="4" md="6" sm="12" xs="12">
        <v-card class="vcard" fill-height v-on:click="arm(1)"
          elevation="4"
        >
          <v-card-title class="justify-center">
            Arm In
            <v-card-text v-if="alarmState == 1 && alarmState != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-img class="image" src="../assets/arm_away.png"/>
              </div>
            </v-card-text>
            <v-card-text v-else-if="alarmState != 1 && alarmState != null">
              <v-img class="image" src="../assets/arm_away_disabled.png"/>
            </v-card-text>
            <v-card-text v-if="alarmState == null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular
                  :size="70"
                  :width="7"
                  color="grey"
                  indeterminate
                ></v-progress-circular>
              </div>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="6" lg="4" md="6" sm="12" xs="12">
        <v-card class="vcard" fill-height v-on:click="arm(2)"
          elevation="4"
        >
          <v-card-title class="justify-center">
            Arm Away
            <v-card-text v-if="alarmState == 2 && alarmState != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-img class="image" src="../assets/arm_away.png"/>
              </div>
            </v-card-text>
            <v-card-text v-else-if="alarmState != 2 && alarmState != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-img class="image" src="../assets/arm_away_disabled.png"/>
              </div>
            </v-card-text>
            <v-card-text v-if="alarmState == null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular
                  :size="70"
                  :width="7"
                  color="grey"
                  indeterminate
                ></v-progress-circular>
              </div>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6" lg="3" md="4" sm="12" xs="12">
        <v-card
          elevation="4">
          <v-card-title class="justify-center">
            Temperature (&#176;C)
            <v-card-text v-if="temperature != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular class="temperature-progress"
                  :rotate="180"
                  :size="170"
                  :width="50"
                  min="-40"
                  max="40"
                  :value="temperature"
                >
                  {{ temperature }}
                </v-progress-circular>
              </div>
            </v-card-text>
            <v-card-text v-else>
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular
                  :size="70"
                  :width="7"
                  color="grey"
                  indeterminate
                ></v-progress-circular>
              </div>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="6" lg="3" md="4" sm="12" xs="12">
        <v-card
          elevation="4">
          <v-card-title class="justify-center">
            Humidity
            <v-card-text v-if="humidity != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular class="humidity-progress"
                  :rotate="180"
                  :size="170"
                  :width="50"
                  :value="humidity"
                >
                  {{ humidity }}%
                </v-progress-circular>
              </div>
            </v-card-text>
            <v-card-text v-else>
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular
                  :size="70"
                  :width="7"
                  color="grey"
                  indeterminate
                ></v-progress-circular>
              </div>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="6" lg="3" md="4" sm="12" xs="12">
        <v-card
          elevation="4">
          <v-card-title class="justify-center">
            Air
            <v-card-text v-if="air != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular class="air-progress"
                  :rotate="180"
                  :size="170"
                  :width="50"
                  :value="airPercent"
                  :min="150"
                  :max="800"
                >
                  {{ air }}
                </v-progress-circular>
              </div>
            </v-card-text>
            <v-card-text v-else>
              <div class="d-flex flex-column justify-space-between align-center">
                <v-progress-circular
                  :size="70"
                  :width="7"
                  color="grey"
                  indeterminate
                ></v-progress-circular>
              </div>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      alarmOn: null,
      alarmState: null,
      temperature: null,
      humidity: null,
      air: null,
      airPercent: null,
      windowSize: {
        x: 0,
        y: 0,
      },
    };
  },
  async created() {
    this.$vuetify.theme.dark = true;
    setInterval(this.getAlarm, 1000);
    setInterval(this.getSensorValues, 1000);
  },
  methods: {
    getAlarm() {
      fetch('http://interactivehome.ddns.net:8080/alarm/1')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.alarmOn = data[0].alarm_on;
          this.alarmState = data[0].alarm_state;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    getSensorValues() {
      fetch('http://interactivehome.ddns.net:8080/temperature_humidity_gas/1')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.temperature = data[0].temperature;
          this.humidity = data[0].humidity;
          this.air = data[0].gas_value;
          this.airPercent = (100 * this.air) / 1024;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    arm(state) {
      if (state === this.alarmState) {
        return;
      }
      const data = { alarm_id: 1, alarm_on: 0, alarm_state: state };

      fetch('http://interactivehome.ddns.net:8080/alarm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      })
        .then(async (response) => {
          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (response.json && response.json.message) || response.statusText;
            alert(error);
          }
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.v-progress-circular {
  margin: 1rem;
  font-size: 250%;
}
.v-progress-circular.temperature-progress {
  color:green;
}
.v-progress-circular.humidity-progress {
  color:aqua;
}
.v-progress-circular.air-progress {
  color:yellow;
}
</style>
