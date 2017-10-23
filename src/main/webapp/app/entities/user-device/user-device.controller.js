(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserDeviceController', UserDeviceController);

    UserDeviceController.$inject = ['Principal', 'User', '$scope', '$state', '$timeout', 'AlertService', 'Device', 'DeviceStat', '$mdToast', '$translate', 'Country', 'City', 'ModelDevice'];

    function UserDeviceController(Principal, User, $scope, $state, $timeout, AlertService, Device, DeviceStat, $mdToast, $translate, Country, City, ModelDevice) {

    	// socket
        var stompClient = null;
        var vm = this;
        vm.currentAccount = null;
        vm.devices = [];
        vm.models = [];
        
        initSocketConnect();
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
            loadAllModelDevice();
        });
        
        function loadAllDeviceInfo() {
        	angular.forEach(vm.currentAccount.userDevices, function (device, key) {
        		Device.get({id: device.id}, onSaveSuccess, onSaveError);
            	
            	function onSaveSuccess (result) {
            		// select model
                	angular.forEach(vm.models, function (model, key) {
        				// wQ
        				if (result.model == model.model) {
        					result.models = model;
        				}
                    });
                	vm.devices.push(result);
                };

                function onSaveError () {
                }
            });
        }
        
        function loadAllModelDevice() {
        	ModelDevice.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.models = result;
    			loadAllDeviceInfo();
            };

            function onSaveError () {
            }
        }

        
      //======================
        function initSocketConnect() {
        	var socket = new SockJS('/kanhome-websocket');
            stompClient = Stomp.over(socket);
            connect();
            // subscribe
//            iWaterSubscribe();
        }
        
        function connect() {
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/iWater', function (greeting) {
                	console.log(greeting);
                });
                
                iWaterSubscribe();
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
//            setConnected(false);
            console.log("Disconnected");
        }

        function iWaterSubscribe() {
            stompClient.send("/app/iWater", {}, JSON.stringify({'deviceId': 'USeM0000'}));
        }

        function showGreeting(message) {
            console.log('Socket: ' + message);
        }
        //========================
    }
})();
