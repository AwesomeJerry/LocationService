
Phonegap Plugin For Location Service
=======

cordova plugin add https://github.com/BonerChick/LocationService.git

Prerequisition
=========
AndroidManifest.xml
<code>
    &ltapplication&gt
        &ltservice android:name="jerry.shen.plugin.MainService"&gt
            &ltintent-filter&gt
                  &ltaction android:name="jerry.shen.plugin.MainService" /&gt
            &lt/intent-filter&gt
      &lt/service&gt
    &lt/application&gt
</code>
put MainService into ....platforms\android\src\jerry\shen\plugin
