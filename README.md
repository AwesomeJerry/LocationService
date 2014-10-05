LocationService
============

Phonegap LocationService Plugin
Setting a destination Latitude Longitude, When you get near the LatLng, it will push a notification to your phone.

Install
===========
Assuming the PhoneGap CLI is installed, from the command line run:

phonegap local plugin add https://github.com/BonerChick/LocationService.git


or Apache Cordova CLI is installed, run:

cordova plugin add https://github.com/BonerChick/LocationService.git

Usage
==========
locationService.startService(arrived_title, arrived_message, start_title, start_message, lat, lng, check_interval, arrived_range, success, error);

locationService.stopService(success, error);




