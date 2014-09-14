var locationService = {
    startService: function(latitude, longitude, regid, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'startService',[{
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