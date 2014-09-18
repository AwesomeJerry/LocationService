var locationService = {
    startService: function(destination, latitude, longitude, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'startService',[{
            'destination': destination,
            'latitude': latitude,
            'longitude': longitude
        }]);
    },
    stopService: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'stopService',[{}]);
    }
}

module.exports = locationService;