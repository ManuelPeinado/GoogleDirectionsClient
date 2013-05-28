GoogleDirectionsClient
======================

This library provides a set of classes that simplify working with the Google Directions API by taking care of performing the HTTP request in the background and parsing the result.

Three different clients are provided:

#####GoogleDirectionsClient
A low level client that takes care of performing a request to the API in a background thread, parsing the result and passing it to you using a listener interface. This client will even allow you to make synchronous requests, using the `getDirectionsSync` method. Just don't do it in the UI thread!

#####GoogleDirectionsFragment
A non-retain-instance fragment that makes it extremely simple to implement an activity that makes requests to the Directions API, as it takes care of handling the orientation changes in a transparent manner.
#####GoogleDirectionsService
An IntentService derived class that will perform a request to the API in the background and notify you when the result is ready using a local broadcast message. This should be the client of choice when you don't want to tie your requests to a particular activity. Just remember that one request cannot be processed until the previous one has finished, so this client might perform poorly in cases where you need to make several requests in parallel, or where you need to cancel a request and start a new one immediately.



Including in your project
-------------------------

*To be done*

Usage
---------

Browse the source code of the sample application to see working examples of all three clients.

Or read one of the tutorials:

*To be done*


Libraries used
--------------------

*To be done*

Who's using it
--------------
 
*Does your app use GoogleDirectionsClient? If you want to be featured on this list drop me a line.*


Developed By
--------------------

Manuel Peinado Gallego - <manuel.peinado@gmail.com>

<a href="https://twitter.com/mpg2">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/twitter.png" />
</a>
<a href="https://plus.google.com/106514622630861903655">
  <img alt="Follow me on Google+"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/google-plus.png" />
</a>
<a href="http://www.linkedin.com/pub/manuel-peinado-gallego/1b/435/685">
  <img alt="Follow me on LinkedIn"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/linkedin.png" />


License
-----------

    Copyright 2013 Manuel Peinado

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





