# Boston University EC327 Fall 2016 Final Project
# "OOPs, we did it again" presents: Report-A-Potty
Rachel Manzelli, Vijay Thakkar, Alecia Griffin, Peter Jang

Designed for API 25 (Android Nougat 7.2), runs on API 23 (Android Marshmallow 6.0) at minimum.

Report-a-Potty is a map-based public restroom finder. Upon starting the app, the user has the ability to select 
"Find Restrooms Near Me", which will use the device's location services to pinpoint their current location. ( NOTE: THE GPS FUNCTIONALITY DOES NOT WORK YET AND WILL MAKE THE APP CRASH. However, the overall funciton of the app is stil fine as the user can still input a string address manually and search for restrooms nearby.)

Alternatively, the user may choose "Enter Address", allowing them to enter a specific address. The device will then use the Android Geocoder utility to convert the address into latitude and longitude coordinates. The reason for adding the capability of keying in any location manually is to address the issue of limited GPS reception in vertical cities (like New York) or to allow the user to plan ahead in case they are planning to travel to a place with limited GPS and internet availability. 

To achieve the best results, the user should enter a specific location containing a street, city, and state. More generic locations can also be entered, such as a street in a town, or a locale. Zip code is not required, but will also function. A country name may be required in case of requesting locations outside of the US. 

Example calls to "Enter Address":

  700 Commonwealth Avenue, Boston, MA

  Somerville, MA

  Vastrapur, Ahmedabad, India

  Orange Street, Malden
  
The app will first zoom into the user's location, and then will display places closest to the user that have a high
probability of containing a public restroom. These will include gas stations, cafes, shopping malls, and department stores. The user may press the back button on the device to reenter the location via a string address or switch to GPS location. When the back button is pressed once, the map resets to its original position if the user has moved it. The second time it is pressed, it returns to the previous activity. When another address is received after the back button is pressed, the map will clear the results from the previous search, thus only showing the results of the current search.

Extensive permissions checking for GPS services, location services, and Google Play services is built into the app. The first time the app is launched on a device, a dialog box will appear asking for the user's permission to use such services if they are not enabled. The app will not ask for permission on the following launches once they have been granted once.
