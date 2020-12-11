<template>
  <v-container fluid>
    <v-row>
      <v-radio-group
       class="radio-grp"
       label="Active Alarm"
       v-model="activeAlarmGroup"
       @change="storeActiveAlarm()"
      >
        <v-radio v-for="(alarmSystem, index) of alarmSystems" :key="index"
        :label=alarmSystem.description>
        </v-radio>
      </v-radio-group>
      <v-radio-group
       class="radio-grp"
       label="Theme"
       v-model="themeGroup"
       @change="setTheme"
      >
        <v-radio label="Dark">
        </v-radio>
        <v-radio label="Light">
        </v-radio>
      </v-radio-group>
    </v-row>
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
      <v-tab @click="getSensors('environment_sensor', false)">Environment</v-tab>
      <v-tab @click="getSensors('door_sensor', false)">Door</v-tab>
      <v-tab @click="getSensors('motion_sensor', false)">Motion</v-tab>
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
              :value="sensor._id"
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
            <v-switch v-if="activeTab == 'door_sensor'"
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
                  @click="deleteSensor(sensor.sensor_id)">
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
  name: 'Configuration',
  data() {
    return {
      alarmSystems: [],
      activeAlarmGroup: 0,
      themeGroup: null,
      newDevice: null,
      activeTab: 'environment_sensor',
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

  created() {
    this.getAlarmSystems();

    const theme = localStorage.getItem('theme');
    if ((theme === null) || (theme != null && theme === 'dark')) {
      this.themeGroup = 0;
      this.$vuetify.theme.dark = true;
    } else if (theme != null && theme === 'light') {
      this.themeGroup = 1;
      this.$vuetify.theme.dark = false;
    }
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

    setTheme() {
      const isDarkTheme = this.themeGroup === 0;
      this.$vuetify.theme.dark = isDarkTheme;
      localStorage.removeItem('theme');
      localStorage.setItem('theme', isDarkTheme ? 'dark' : 'light');
    },

    async discoverDevice() {
      navigator.bluetooth.requestDevice({
        acceptAllDevices: true,
      })
        .then((device) => { this.newDevice = device.name; })
        .catch((error) => { console.error(error); });
    },

    getAlarmSystems() {
      this.alarmSystems = null;
      fetch('http://interactivehome.ddns.net:8080/alarm_system')
        .then(async (response) => {
          const data = await response.json();

          // check for error response
          if (!response.ok) {
            // get error message from body or default to response statusText
            const error = (data && data.message) || response.statusText;
            alert(error);
          }

          this.alarmSystems = data;
        })
        .then(() => {
          this.setActiveAlarm();
          this.storeActiveAlarm();
          this.getSensors(this.activeTab, true);
        })
        .catch((error) => {
          this.errorMessage = error;
          console.error('There was an error!', error);
        });
    },

    setActiveAlarm() {
      let i = 0; // The alarm Ids start from 1
      for (; i < this.alarmSystems.length; i += 1) {
        if (this.alarmId === this.alarmSystems[i].id) {
          // The radio button is zero-based, the alarm_id starts from 1
          this.activeAlarmGroup = this.alarmId - 1;
          break;
        }
      }
    },

    storeActiveAlarm() {
      const activeAlarm = this.activeAlarmGroup + 1;
      localStorage.removeItem('activeAlarm');
      localStorage.setItem('activeAlarm', activeAlarm);
      this.$store.commit('storeActiveAlarm', activeAlarm);
      this.getSensors(this.activeTab, true);
    },

    getSensors(tab, forceRefresh) {
      if (tab === this.activeTab && !forceRefresh) { return; }

      this.sensors = null;
      this.activeTab = tab;
      const aId = parseInt(this.alarmId, 10);
      fetch(`http://interactivehome.ddns.net:8080/${tab}?alarmId=${aId}`)
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

    undoChanges() {
      this.getSensors(this.activeTab, true);
    },

    saveChanges(editedSensor) {
      // eslint-disable-next-line no-restricted-globals
      if (!confirm('Are you sure you want to update this sensor?')) { return; }

      const requestBody = {
        description: editedSensor.description,
        device_identifier: editedSensor.device_identifier,
        enabled: editedSensor.enabled,
        battery_powered: editedSensor.battery_powered,
        trigger_verification_process: editedSensor.trigger_verification_process,
        arm_in: editedSensor.arm_in,
        arm_away: editedSensor.arm_away,
      };

      fetch(`http://interactivehome.ddns.net:8080/${this.activeTab}/${editedSensor.id}?alarmId=${this.alarmId}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        })
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

    deleteSensor(id) {
      // eslint-disable-next-line no-restricted-globals
      if (!confirm('Are you sure you want to delete this sensor?')) { return; }

      fetch(
        `http://interactivehome.ddns.net:8080/${this.activeTab}/${id}?alarmId=${this.alarmId}`,
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
        .then(this.getSensors(this.activeTab, true));
    },

    onButtonClick() {
      let filters = [];

      let filterName = document.querySelector('#name').value;
      if (filterName) {
        filters.push({name: filterName});
      }

      let filterNamePrefix = document.querySelector('#namePrefix').value;
      if (filterNamePrefix) {
        filters.push({namePrefix: filterNamePrefix});
      }

      let options = {};
      if (document.querySelector('#allAdvertisements').checked) {
        options.acceptAllAdvertisements = true;
      } else {
        options.filters = filters;
      }

      try {
        log('Requesting Bluetooth Scan with options: ' + JSON.stringify(options));
        const scan = await navigator.bluetooth.requestLEScan(options);

        log('Scan started with:');
        log(' acceptAllAdvertisements: ' + scan.acceptAllAdvertisements);
        log(' active: ' + scan.active);
        log(' keepRepeatedDevices: ' + scan.keepRepeatedDevices);
        log(' filters: ' + JSON.stringify(scan.filters));

        navigator.bluetooth.addEventListener('advertisementreceived', event => {
          log('Advertisement received.');
          log('  Device Name: ' + event.device.name);
          log('  Device ID: ' + event.device.id);
          log('  RSSI: ' + event.rssi);
          log('  TX Power: ' + event.txPower);
          log('  UUIDs: ' + event.uuids);
          event.manufacturerData.forEach((valueDataView, key) => {
            logDataView('Manufacturer', key, valueDataView);
          });
          event.serviceData.forEach((valueDataView, key) => {
            logDataView('Service', key, valueDataView);
          });
        });

        setTimeout(stopScan, 10000);
        function stopScan() {
          log('Stopping scan...');
          scan.stop();
          log('Stopped.  scan.active = ' + scan.active);
        }
      } catch(error)  {
        log('Argh! ' + error);
      }
    },

  },
};
</script>

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
.radio-grp {
  margin-left: 3em;
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
