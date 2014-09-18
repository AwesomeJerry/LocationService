var locationService = {
    startService: function(destination, latitude, longitude, regid, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'startService',[{
            'destination': destination,
            'latitude': latitude,
            'longitude': longitude,
            'regid': regid
        }]);
    },
    stopService: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'stopService',[{}]);
    }
}

module.exports = locationService;