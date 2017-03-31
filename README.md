## FindMe - Android Project with Firebase
Simple location based Android application using Google Firebase.
After registering, user can add friends who use the same application and locate their locations real-time. This app could use to find the exact locations of a friend in real-time.

Here are some screen-shots:

![Screen](https://github.com/sathindukavneth/findme/blob/master/images/screen.png)


## How to run the app

* Go to your terminal and execute this command
```
git clone https://github.com/sathindukavneth/findme.git
```
* Create a new project on firebase.google.com and download the ```google-services.json```
* Add that file to /app folder in project 

![Json Add](https://github.com/sathindukavneth/findme/blob/master/images/snip.png)

* Create a Google Map API Key for Android and set that key in res/values/strings.xml
```
...
<string name="GOOGLE_MAP_KEY">YOUR GOOGLE MAP KEY</string>
...
```

* Run the project
