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
  </v-container>
</template>

<script>
import alarmStateObjects from '../data/alarmState';

export default {
  name: 'Dashboard',
  data() {
    return {
      alarmId: 0,
      sensorId: 0,
      alarmOn: null,
      alarmState: null,
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

  async created() {
    this.$vuetify.theme.dark = true;
    this.getAlarm();
    setInterval(this.getActiveAlarm, 1000);
    setInterval(this.getAlarm, 1000);
    setInterval(this.getSensorValues, 1000);
  },

  methods: {

    setSelectedAlarmState() {
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

    getActiveAlarm() {
      fetch('http://interactivehome.ddns.net:8080/active_alarm_system')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.alarmId = data.alarm_id;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    getAlarm() {
      fetch(`http://interactivehome.ddns.net:8080/alarm/${this.alarmId}`)
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
          this.setSelectedAlarmState();
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    getSensorValues() {
      fetch(`http://interactivehome.ddns.net:8080/temperature_humidity_gas_state/${this.alarmId}/${this.sensorId}`)
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
}
.v-progress-circular.humidity-progress {
  color:aqua;
}
.v-progress-circular.air-progress {
  color:yellow;
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
