# Cordova MRAA/UPM Plugin

Simple plugin creates an interface to upm_bmp280, upm_tmp006, upm_grove_led, and upm_grove_buzzer.

## Using

```js
    var success = function(message) {
        alert(message);
    }

    var failure = function() {
        alert("Error calling Hello Plugin");
    }

    ATmraa.TMP006({i2cAddress:"I2C2"},success,failure);
    ATmraa.BME280({i2cAddress:"I2C2",RequestSensor:"Pressure"},success,failure);
    ATmraa.LED({pin:"J6_47",on:true,delay:2000},success,failure);
    ATmraa.BUZZER({pin:"PWM_3",note:"FA",delay:500000},success,failure);

```
### TMP006
    i2cAddress:  The i2c buss the sensor is attached to.
        defalut: "I2C0"

### BME280
    i2cAddress: The i2c buss the sensor is attached to.
        defalut: "I2C0"

    RequestSensor: The sensor to request valus form.
        options: "Temperature", "Pressure", "Altitude", "Humidity"
        defalut: "Temperature"

### LED
    pin: The GPIO the led is attached to.
        defalut: "J6_47"

    on: A boolen true turen on the led.
        options: true, flalse
        defalut: false

    delay: A delay untel the call back is called.
        defalut: 0

### BUZZER
    pin: The PWM the buzzer is attached to.
        defalut: "PWM_3"

    note: The Note to play (DO, RE, MI, etc.)
        options: "DO", "RE", "MI", "FA", "SOL", "LA", "SI", 
        defalut: "DO"

    delay: The time in microseconds for which to play the sound; if the value is 0, the sound is played indefinitely
        defalut: 500000

## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)
