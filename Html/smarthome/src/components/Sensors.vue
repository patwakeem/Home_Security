<template>
  <v-container fluid>
    <v-row>
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
      <v-tab @click="getSensors('t_h_g_sensors', false)">Temperature-Humidity-Gas</v-tab>
      <v-tab @click="getSensors('door_sensors', false)">Door</v-tab>
      <v-tab @click="getSensors('movement_sensors', false)">Movement</v-tab>
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
            <!-- <v-col>
              <label>Sensor Id </label>
              <br>
              <label for="description">Description </label>
              <br>
              <label for="identifier">Identifier </label>
              <br>
              <label>Enabled </label>
              <br>
              <label>Battery Powered </label>
              <br>
              <label>Created (UTC) </label>
            </v-col> -->
            <v-col>
              <!-- <label id="sensor_id">{{sensor.sensor_id}}</label> -->
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
                :value="sensor.created_utc"
              ></v-text-field>
              <v-text-field
                label="Description"
                class="text-field"
                maxlength="30"
                dense
                :value="sensor.description"
                v-model="sensor.description"
              ></v-text-field>
              <!-- <input
              class="input"
              type="text"
              maxlength="30"
              id="description"
              :value="sensor.description"
              @change="updateDescription"
              @input="updateDescription"> -->
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
              <!-- <label class="switch">
                <input
                type="checkbox"
                id="enabled"
                :checked="sensor.enabled"
                @change="updateEnabled(sensor.sensorId, this.value)">
                <span class="slider round"></span>
              </label>
              <br>
              <label class="switch">
                <input
                type="checkbox"
                id="battery_powered"
                :checked="sensor.battery_powered"
                @change="updateBatteryPowered(sensor.sensorId, this.value)">
                <span class="slider round"></span>
              </label> -->
              <!-- <br>
              <label
                class="label"
                id="created">
                {{sensor.created_utc}}
              </label> -->
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
      </v-row>
  </v-container>
</template>

<script>

export default {
  name: 'Sensors',
  data() {
    return {
      newDevice: null,
      activeTab: 't_h_g_sensors',
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
    this.$vuetify.theme.dark = true;
    await this.getSensors(this.activeTab, true);
  },

  methods: {

    async discoverDevice() {
      navigator.bluetooth.requestDevice({
        acceptAllDevices: true,
      })
        .then((device) => { this.newDevice = device.name; })
        .catch((error) => { console.error(error); });
    },

    getSensors(tab, forceRefresh) {
      if (tab === this.activeTab && !forceRefresh) { return; }

      this.sensors = null;
      this.activeTab = tab;
      fetch('http://interactivehome.ddns.net:8080/active_alarm_system')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          return data.alarm_id;
        })
        .then((alarmId) => {
          fetch(`http://interactivehome.ddns.net:8080/${tab}/${alarmId}`)
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
        });
    },

    undoChanges() {
      this.getSensors(this.activeTab, true);
    },

    saveChanges(editedSensor) {
      // eslint-disable-next-line no-restricted-globals
      if (!confirm('Are you sure you want to update this sensor?')) { return; }

      const requestBody = {
        alarm_id: editedSensor.alarm_id,
        sensor_id: editedSensor.sensor_id,
        description: editedSensor.description,
        device_identifier: editedSensor.device_identifier,
        enabled: editedSensor.enabled,
        battery_powered: editedSensor.battery_powered,
        arm_in: editedSensor.arm_in,
        arm_away: editedSensor.arm_away,
        created_utc: editedSensor.created,
      };

      fetch(
        'http://interactivehome.ddns.net:8080/modify_t_h_g_sensor',
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        },
      )
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }
        })
        .then(this.getSensors(this.activeTab, true));
    },

    deleteSensor(alarmId, sensorId) {
      // eslint-disable-next-line no-restricted-globals
      if (!confirm('Are you sure you want to delete this sensor?')) { return; }

      fetch(
        `http://interactivehome.ddns.net:8080/delete_t_h_g_sensor/${alarmId}/${sensorId}`,
        { method: 'delete' },
      )
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
          // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }
        })
        .then(this.getSensors(this.activeTab));
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
