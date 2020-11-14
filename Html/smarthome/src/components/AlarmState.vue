<script>
import alarmStateObjects from '../data/alarmState';

export default {

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
      selectedAlarmObject: -1,
      unselectedAlarmObjects: [],
      alarmStateObjects,
    };
  },

  async created() {
    let isLoggedIn = false;
    if (sessionStorage.getItem('loggedIn')) {
      isLoggedIn = sessionStorage.getItem('loggedIn');
      this.$store.commit('setLoggedIn', isLoggedIn);
    }
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
  },
};

</script>
