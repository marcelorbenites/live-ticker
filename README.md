# Marcelo Benites
**Globo.com: coding challenge**

## Build

### Android Studio Version

I use the bleeding edge versions of Android Studio, Android Gradle Plugin and Gradle. It is only possible to access those version with Canary Channel enabled. Feel free to downgrade them if needed in order to build.

| Dependency | Version |
|---------|------------|
| 2.0 beta4   | Android Studio | 
| 2.0.0-beta4   | Android Gradle Plugin | 
| 2.11   | Gradle | 

### Keys

Application depends on brasileirao-keystore.jks keystore for release builds and on an API key to access the backend server. I use some environment variables to provide this information. For the project to build the following entries must be added to the gradle.properties Global file (usually under /Users/YourUser/.gradle/gradle.properties):

    BRASILEIRAO_KEYSTORE_PATH=../keystore/brasileirao-keystore.jks
    BRASILEIRAO_KEYSTORE_PASSWORD=XyBgK3c;u3qr^s4];q6P
    BRASILEIRAO_KEY_ALIAS=brasileiraokey
    BRASILEIRAO_KEY_PASSWORD=VMr+$j42t23DK&rTdvR=
    API_KEY="dHaBQmPaTAgClSUnjVrs3aEMkqfxFASI"

It is not a best practice to add the application keystore to source control, instead a separate file management mechanism should take care of allowing only the responsible for publishing the application to access the keystore. For the sake of simplicity you can find application's keystore under **keystore/brasileirao-keystore.jks**.


## Backend

To speed-up backend development a tool called Heroku (https://dashboard.heroku.com/) was used together with a service called MongoLab (mongolab.com). MongoLab exposes a REST API to access data stored in a MongoDB NoSQL database.

**Note**: the only match the actually has live ticker entries for now is **Fluminense x Grêmio**.


## Application Architecture

The main architecture decision was to use a persistent layer to store the data that is updated in every network call. Views are updated in a reactive fashion whenever the persistent layer changes. This approach has the advantage of allowing background events to update the UI without tha hassle of Activity to Service communication. 

The application has two different Activities. One for current matches and the other one with a live ticker for a selected match. Both Activities fire a background service to keep their data up-to-date while they are alive. The service is implemented using [GCMNetworkManager] (https://developers.google.com/android/reference/com/google/android/gms/gcm/GcmNetworkManager) which improves battery life since it gives the system the chance to define the best moment to execute the requests.
Images are cached using [Picasso] (http://square.github.io/picasso/) a library to manage image downloading.


## Improvements

* Instead of calling MongoLab REST API directly use NodeJS and ExpressJS to create a custom REST API in Heroku. It would allow us to define a caching mechanism for requests using Cache-Control HTTP header. It will also allow us to query multiple collections in a single request. Currently in order to update LiveTickerActivity data we perform two requests. One for the match score and the other one for the live ticker entries list.
* Migrate match list and live ticker list to Fragments. It would allow us to support bigger screen sizes by displaying both Fragments in a single Activity when necessary.
* Taking advantage of application's architecture it is simple to implement a push-to-sync mechanism using [Google Cloud Messaging] (https://developers.google.com/cloud-messaging/) instead of pooling the informaton from server like we do right now.
* A custom server would allow us to reduce bandwidth usage by returning only the updated/new live ticker entries for a match (if-modified-since would help on that). I've started by using a skip mechanism provided by MongoLab REST API but end up abandoning this approach. Since I would skip entries already persisted in the local database I would never get an update if an entry changes remotely.






