<template>
  <v-container fluid fill-height>
    <v-row>
      <v-col>
        <v-card
          height="100%"
          >
            <v-card-title class="title-text justify-center">
              <div class="d-flex flex-column justify-space-between align-center">
                Security Alarm
              </div>
            </v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col cols="8" xs="12">
        <v-card elevation="4">
          <v-card-title class="active-alarm justify-center">
             {{alarmStateObjects[this.selectedAlarmObject].title}}
            <v-card-text v-if="alarmState != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-img :src="alarmStateObjects[this.selectedAlarmObject].src"/>
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
    <v-row justify="center">
      <v-col cols="4" xs="6"
       v-for="(unselectedAlarm, index) in unselectedAlarmObjects" :key="index">
        <v-card fill-height @click="arm(unselectedAlarm.alarmState)"
          elevation="4"
        >
          <v-card-title class="inactive-alarm justify-center">
            {{unselectedAlarm.title}}
            <v-card-text v-if="alarmState != null">
              <div class="d-flex flex-column justify-space-between align-center">
                <v-img :src="unselectedAlarm.src"/>
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
    <!-- <v-row v-for="(sensor, index) of sensors" :key="index"> -->
    <v-row>
      <v-col>
        <v-card
          height="100%"
          >
            <v-card-title class="justify-center">
              <h2>
                Kitchen Sensors
              </h2>
            </v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6" lg="3" md="4" sm="12" xs="12">
        <v-card
          elevation="4">
          <v-card-title class="justify-center">
            Temp (&#176;C)
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
      <v-col cols="6" lg="3" md="4" sm="12" x="12">
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
    <!-- </v-row> -->
  </v-container>
</template>

<script>
import alarmStateObjects from '../data/alarmState';

export default {
  name: 'Main',
  data() {
    return {
      sensorId: 0,
      // alarmOn: null,
      // alarmState: null,
      temperature: null,
      humidity: null,
      air: null,
      airPercent: null,
      // temHumAirSensors,
      // doorSensors,
      // movementSensors,
      selectedAlarmObject: -1,
      unselectedAlarmObjects: [],
      alarmStateObjects,
    };
  },

  created() {
    this.setSelectedAlarmState();
    this.getSensorsValues();
    setInterval(this.getSensorsValues, 1000);
    setInterval(this.setSelectedAlarmState, 1000);
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    alarmId() {
      return this.$store.state.activeAlarm;
    },
    alarmState() {
      return this.$store.state.alarmState;
    },
  },

  methods: {

    setSelectedAlarmState() {
      if (this.alarmId === null) {
        return;
      }

      this.unselectedAlarmObjects = [];
      switch (this.alarmState) {
        case 0:
          this.selectedAlarmObject = 0;
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[4] });
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[5] });
          break;
        case 1:
          this.selectedAlarmObject = 1;
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[3] });
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[5] });
          break;
        case 2:
          this.selectedAlarmObject = 2;
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[3] });
          this.unselectedAlarmObjects.push({ ...alarmStateObjects[4] });
          break;
        default:
      }
    },

    getSensorsValues() {
      const aId = parseInt(this.alarmId, 10);
      fetch(`http://interactivehome.ddns.net:8080/environment_sensor_state/${this.sensorId}?alarmId=${aId}`)
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.temperature = data[0].temperature;
          this.humidity = parseInt(data[0].humidity, 10);
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
      const data = { verification_activated: false, alarm_on: 0, alarm_state: state };

      fetch(`http://interactivehome.ddns.net:8080/alarm_system_state/${this.alarmId}`, {
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

  getImgUrl(alarmStateImg) {
    const images = require.context('../assets/', false, /\.png$/);
    return images(`./${alarmStateImg}.png`);
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
  font-size: 170%;
}
.v-progress-circular.humidity-progress {
  color:aqua;
  font-size: 170%;
}
.v-progress-circular.air-progress {
  color:yellow;
  font-size: 170%;
}
.title-text {
  font-size: 5vmin;
}
.active-alarm {
  font-size: 4.5vmin;
}
.inactive-alarm {
  font-size: 3vmin;
}
</style>
