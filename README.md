## FindMe - Android Project with Firebase
Simple location based Android application using Google Firebase.
After registering, user can add friends who use the same application and locate their locations real-time. This app could use to find the exact locations of a friend in real-time.

Here are some screen-shots:

![Screen](https://github.com/sathindukavneth/findme/blob/master/images/screen.png)

Some of Functionalities in the app

* Can locate friends real-time
* View the friend status that he/she is online or offline
* User authentication

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

## License
MIT License

Copyright (c) 2017 Sathindu Kavneth

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
