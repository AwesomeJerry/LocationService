var locationService = {
    startService: function(arrived_title, arrived_message, start_title, stop_message, latitude, longitude, check_interval, arrived_range, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'startService',[{
            'arrived_title': arrived_title,
            'arrived_message': arrived_message,
            'start_title': start_title,
            'stop_message': stop_message,
            'latitude': latitude,
            'longitude': longitude,
            'check_interval': check_interval,
            'arrived_range': arrived_range
        }]);
    },
    stopService: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'LocationService', 'stopService',[{}]);
    }
}

module.exports = locationService;