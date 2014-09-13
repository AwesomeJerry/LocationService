var locationService = {
    startService: function(latitude, longitude, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'startService',[{
            'latitude': latitude,
            'longitude': longitude
        }]);
    },
    stopService: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'stopService',[{}]);
    }
}

module.exports = locationService;