
Phonegap Plugin For Location Service
=======

cordova plugin add https://github.com/BonerChick/LocationService.git

Prerequisition
=========
AndroidManifest.xml
    <application>
        <service android:name="jerry.shen.plugin.MainService">
            <intent-filter>
                  <action android:name="jerry.shen.plugin.MainService" />
            </intent-filter>
      </service>
    </application>
    
put MainService into ....platforms\android\src\jerry\shen\plugin
