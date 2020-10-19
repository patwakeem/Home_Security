getSensorValues(sensorId) {
    fetch(`http://interactivehome.ddns.net:8080/temperature_humidity_gas/${this.alarmId}/${sensorId}`)
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
};
