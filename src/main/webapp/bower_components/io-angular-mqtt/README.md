# angular-mqtt

## Install

    bower install angular-mqtt

Your project need to include angular libraries :

    bower install angular

## How to use it

HTML code : "index.html"

    <!DOCTYPE html>
    <html lang="en">
      <head>
        <meta charset="utf-8">
        <title>angular mqtt test</title>
      </head>
      <body ng-app="app">
        <form novalidate>
          <div ng-controller="basic">
            <p>Topic to publish / subscribe :</p>
            <p>
              <input type="text" ng-model="topic">
              <button type="button" ng-click="subscribe()">Subscribe</button><br>
            </p>
            <p>Message to publish on topic {{topic}} :</p>
            <p>
              <input type="text" ng-model="pub">
              <button type="button" ng-click="publish()">Publish</button>
            </p>
          </form>
          <p><strong>Received datas :</strong></p>
          <p>{{ result }}</p>
        </div>

        <!-- INCLUDE ANGULAR LIBRARIES -->
        <script type="text/javascript" src="bower_components/angular/angular.min.js"></script>

        <!-- INCLUDE ANGULAR MQTT MODULE AND browserMqtt.js browerserified from mqtt.js -->
        <script type="text/javascript" src="bower_components/angular-mqtt/browserMqtt.js"></script>
        <script type="text/javascript" src="bower_components/angular-mqtt/angular-mqtt.js"></script>

        <!-- YOUR ANGULAR APPLICATION MODULE -->
        <script type="text/javascript" src="js/index.js"></script>
      </body>
    </html>

Javascript code : "js/index.js"

    "use strict";

    let mqttclient = {
      config: {
        ip: '192.168.1.211',
        port: 10001,
        qos: 1,
        retain: true
      }
    };

    let app = angular.module('app', ['ngMqtt']);

    app.config(function(mqttProvider){
      mqttclient.client = mqttProvider.client( mqttclient.config );
    });

    app.controller('basic', ['$scope', 'mqtt', function($scope, mqtt) {

      $scope.publish = () => {
        let topic = $scope.topic || "test/1";
        let pub = $scope.pub || "test";
        mqtt.publish( topic, pub );
      };
      $scope.subscribe = () => {
        let topic = $scope.topic || "test";
        mqtt.on( topic, ( t, msg ) => {
          $scope.result = `New message on topic ${t} : ${msg}`;
        });
      };

      $scope.result = "Nothing to display yet...";

    }]);
