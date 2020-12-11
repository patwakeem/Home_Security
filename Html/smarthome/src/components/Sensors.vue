<template>
  <v-container fluid>
    <v-tabs v-model="tab" align-with-title>
      <v-tabs-slider color="grey"></v-tabs-slider>
      <v-tab @click="getSensorsValues('environment_sensor_state', false)">Environment</v-tab>
      <v-tab @click="getSensorsValues('door_sensor_state', false)">Door</v-tab>
      <v-tab @click="getSensorsValues('movement_sensor_state', false)">Motion</v-tab>
    </v-tabs>
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
    <!-- <v-row>
      <v-col>
        <v-card
            class=""
        elevation="4"
            max-width="200"
          >
            <v-card-text>
              Find new sensor
            </v-card-text>
            <v-card-actions>
              <v-btn @click="discoverDevice()"
                color="darkgrey"
              >
                <v-icon color="blue">mdi-access-point-network</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card class="title"
          height="100%"
          >
            <v-card-title class="title-text justify-center">
              Sensors
            </v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-tabs v-model="tab" align-with-title>
      <v-tabs-slider color="grey"></v-tabs-slider>
      <v-tab @click="getSensors('environment_sensor', false)">Environment</v-tab>
      <v-tab @click="getSensors('door_sensor', false)">Door</v-tab>
      <v-tab @click="getSensors('movement_sensor', false)">Motion</v-tab>
    </v-tabs>
    <v-row v-for="(sensor, index) of sensors" :key="index">
      <v-col>
        <v-card id="sensor" class="sensor-values">
          <v-row justify="end">
            <v-col>
              <v-tooltip top>
                <template v-slot:activator="{ on }">
                  <v-icon
                    color="blue"
                    v-on="on"
                  >mdi-access-point-network</v-icon>
                </template>
                <span>Sensor is Online</span>
              </v-tooltip>
            </v-col>
          </v-row>
          <v-row>
          <v-col>
            <v-text-field
              label="Sensor Id"
              class="text-field"
              readonly
              dense
              disabled
              :value="sensor.sensor_id"
            ></v-text-field>
            <v-text-field
              label="Created (UTC)"
              class="text-field"
              readonly
              dense
              disabled
              :value="Date(sensor.created_utc).toString()"
            ></v-text-field>
            <v-text-field
              label="Description"
              class="text-field"
              maxlength="30"
              dense
              :value="sensor.description"
              v-model="sensor.description"
            ></v-text-field>
            <v-text-field
              label="Device Identifier"
              class="text-field"
              maxlength="30"
              dense
              :value="sensor.device_identifier"
              v-model="sensor.device_identifier"
            ></v-text-field>
            <v-switch
              label="Enabled"
              v-model="sensor.enabled"
              dense
              reverse
              class="vswitch"
              :checked="sensor.enabled">
            </v-switch>
            <v-switch
              label="Battery Powered"
              v-model="sensor.battery_powered"
              dense
              class="vswitch"
              :checked="sensor.battery_powered">
            </v-switch>
            <v-switch v-if="activeTab == 'door_sensors'"
              label="Trigger verification process"
              v-model="sensor.trigger_verification_process"
              dense
              reverse
              class="vswitch"
              :checked="sensor.trigger_verification_process">
            </v-switch>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-switch
                  v-on="on"
                  label="Arm In"
                  v-model="sensor.arm_in"
                  dense
                  reverse
                  class="vswitch"
                  :checked="sensor.arm_in">
                </v-switch>
              </template>
              <span>Enable/Disable when Alarm is in 'Alarm In' state</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                <v-switch
                  v-on="on"
                  label="Arm Away"
                  v-model="sensor.arm_away"
                  dense
                  reverse
                  class="vswitch"
                  :checked="sensor.arm_away">
                </v-switch>
              </template>
              <span>Enable/Disable when Alarm is in 'Alarm In' state</span>
            </v-tooltip>
          </v-col>
          </v-row>
          <v-row justify="end">
            <v-tooltip top>
              <template v-slot:activator="{ on, attrs }">
                <v-card
                  v-bind="attrs"
                  v-on="on"
                  class="card-button"
                  elevation="4"
                  @click="deleteSensor(sensor.alarm_id, sensor.sensor_id)">
                  <div
                  class="d-flex flex-column justify-space-between align-center">
                    <v-img src="../assets/delete.png"/>
                  </div>
                </v-card>
              </template>
              <span>Delete Sensor</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on, attrs }">
                <v-card
                  v-bind="attrs"
                  v-on="on"
                  class="card-button"
                  elevation="4"
                  @click="undoChanges()">
                  <div
                  class="d-flex flex-column justify-space-between align-center">
                    <v-img src="../assets/cancel.png"/>
                  </div>
                </v-card>
              </template>
              <span>Undo Changes</span>
            </v-tooltip>
            <v-tooltip top>
              <template v-slot:activator="{ on, attrs }">
                <v-card
                  v-bind="attrs"
                  v-on="on"
                  class="card-button"
                  elevation="4"
                  @click="saveChanges(sensor)">
                  <div
                  class="d-flex flex-column justify-space-between align-center">
                    <v-img src="../assets/check.png"/>
                  </div>
                </v-card>
              </template>
              <span>Save Changes</span>
            </v-tooltip>
          </v-row>
        </v-card>
      </v-col>
    </v-row> -->
  </v-container>
</template>

<script>

export default {
  name: 'Sensors',
  data() {
    return {
      newDevice: null,
      activeTab: 'environment_sensor',
      devices: [],
      device_values: {
        temperature: null,
        humidity: null,
        pressure: null,
        altitude: null,
        air: null,
        airPercent: null,
      },
      sensorHeaders: [
        {
          text: 'Sensor Id',
          sortable: false,
          value: 'sensor_id',
        },
        { text: 'Description', value: 'description' },
        { text: 'Identifier', value: 'device_identifier' },
        { text: 'Battery Powered', value: 'battery_powered' },
        { text: 'Enabled', value: 'enabled' },
        { text: 'Created (UTC)', value: 'created_utc' },
      ],
      sensors: [],
    };
  },

  async created() {
    await this.getSensors(this.activeTab, true);
  },

  computed: {
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    alarmId() {
      return this.$store.state.activeAlarm;
    },
  },

  methods: {

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
          this.humidity = data[0].humidity;
          this.air = data[0].gas_value;
          this.airPercent = (100 * this.air) / 1024;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    async discoverDevice() {
      navigator.bluetooth.requestDevice({
        acceptAllDevices: true,
      })
        .then((device) => { this.newDevice = device.name; })
        .catch((error) => { console.error(error); });
    },

    getSensors(tab, forceRefresh) {
      if (tab === this.activeTab && !forceRefresh) { return; }

      fetch(`http://interactivehome.ddns.net:8080/${this.activeTab}_state?alarmId=${this.alarmId}`)
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
            // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.sensors = data;
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
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
.title {
  margin-left: 1em;
  margin-right: 1em;
}
.title-text {
  font-size: 5vmin;
}
.button-style {
  margin: .1em;
}
.card-button {
  margin: 1em;
  margin-left: .4em;
  margin-right: 2em;
  padding: .4em;
  padding-left: 2em;
  padding-right: 2em;
}

.input {
  border-style: solid;
  border-width: .1em;
  border-color: #ccc;
  display: inline-block;
  white-space: nowrap;
  width: 80%;
  overflow: hidden;
  text-overflow:ellipsis;
  cursor: text;
  color: #ccc;
}
.label {
  display: inline-block;
  white-space: nowrap;
  width: 80%;
  overflow: hidden;
  text-overflow:ellipsis;
  cursor: text;
  color: #ccc;
}
.sensor-values {
  font-size: .8em;
  border-radius: .5em;
  padding-left: 1em;
  margin-left: 3em;
  margin-right: 3em;
  width: 80%;
}
.switch {
  position: absolute;
  margin: .1em;
  width: 2em;
  height: 1em;
}
.switch input {
  opacity: 0;
  width: 0em;
  height: 0em;
}

.text-field {
  margin: .8em;
  margin-right: 10vmin;
  padding-left: 2vmin;
}

.vswitch {
  margin: -.8em;
  padding-left: 3.5em;
  padding-right: 25vmin;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0em;
  left: 0em;
  right: 0em;
  bottom: 0em;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 1em;
  width: 1em;
  left: 0em;
  bottom: 0em;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(1em);
  -ms-transform: translateX(1em);
  transform: translateX(1em);
}

/* Rounded sliders */
.slider.round {
  border-radius: 2vmin;
}

.slider.round:before {
  border-radius: 50%;
}

</style>
