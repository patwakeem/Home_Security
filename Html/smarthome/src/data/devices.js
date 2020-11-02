function getTemHumAirSensors() {
  let alarmId = 0;
  fetch('http://interactivehome.ddns.net:8080/active_alarm_system')
    .then(async (response) => {
      const data = await response.json();

      // check for error response
      if (!response.ok) {
      // get error message from body or default to response statusText
        const error = (data && data.message) || response.statusText;
        alert(error);
      }

      alarmId = data.alarm_id;
      alert(alarmId);
    })
    .catch((error) => {
      this.errorMessage = error;
      console.error('There was an error!', error);
    });

  fetch(`http://interactivehome.ddns.net:8080/t_h_g_sensors/${alarmId}`)
    .then(async (response) => {
      const data = await response.json();

      // check for error response
      if (!response.ok) {
      // get error message from body or default to response statusText
        const error = (data && data.message) || response.statusText;
        alert(error);
      }

      alert(data);
    })
    .catch((error) => {
      this.errorMessage = error;
      console.error('There was an error!', error);
    });
}

const temHumAirSensors = getTemHumAirSensors();

export default temHumAirSensors;
