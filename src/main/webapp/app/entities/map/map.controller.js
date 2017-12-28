(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('MapController', MapController);

    MapController.$inject = ['$scope', '$state', '$timeout', 'AlertService', 'uiGmapGoogleMapApi', 'Device', 'DeviceStat', '$mdToast', '$mdSidenav', '$translate', 'Country', 'City', 'ModelDevice'];

    function MapController($scope, $state, $timeout, AlertService, uiGmapGoogleMapApi, Device, DeviceStat, $mdToast, $mdSidenav, $translate, Country, City, ModelDevice) {

        var vm = this;
        vm.devices = [];
        vm.countries = [];
        vm.cities = [];
        vm.models = [];
        vm.map = {
                center: {
                    latitude: 21.046018,
                    longitude: 105.800402
                },
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                zoom: 14,
                markers: [],
                options : {
                	streetViewControl: false,
//                	zoomControl: boolean,
                	mapTypeControl: false,
//                	scaleControl: boolean,
//                	streetViewControl: boolean,
//                	rotateControl: boolean,
                	fullscreenControl: true,
                	fullscreenControlOptions: {
                        position: google.maps.ControlPosition.RIGHT_CENTER
                    },
                },
                clusterOptions : {
                	maxZoom: 16
                }
            };

        vm.searchDevice = {};
        vm.selectedDevice;
        
        vm.search = search;
        vm.clean = clean;
        vm.tooggleDevices = tooggleDevices;
        vm.tooggleSelectedDevice = tooggleSelectedDevice;
        vm.showSideBar = showSideBar;
        vm.showDevice = showDevice;
        vm.clickOnMarker = clickOnMarker;
        vm.toogleFullscreen = toogleFullscreen;
        
        (function initController() {
        		// Init
            loadAllDevice();
            loadAllCountry();
            loadAllCity();
            loadAllModelDevice();
        })();
        
        // Connect socket
        var stompClient = null;
        connect();

        angular.element(document).ready(function () {
        		setHeightMap();
        		angular.element('#id').focus();
        		vm.tooggleDevices();
        });
        
        function setHeightMap() {
        		var navHeight = $("#nav-bar-container").height();
        		var winHeight = window.innerHeight;
        		
        		if (navHeight != undefined && navHeight > 0) {
        			var mapHeight = winHeight - navHeight - 1;
        			$(".angular-google-map-container").css({"height": mapHeight + "px"});
        		}
        }
        
        $(window).on("resize.doResize", function (){
            console.log(window.innerHeight);

            $scope.$apply(function() {
                //do something to update current scope based on the new innerWidth and let angular update the view.
            		setHeightMap();
            });
        });
        
        //======================
        function connect() {
            var socket = new SockJS('/kanhome-websocket');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/iWater', function (message) {
                		updateMqttMessage(message);
                });
//                
//                stompClient.subscribe('/topic/iheater', function (message) {
//            			updateMqttMessage(message);
//                });
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        }

        function requestSubscribeTopic() {
        		console.log('Request subscribe topic, device id:' + vm.selectedDevice.id);
        		if (vm.selectedDevice.name == "Water Heater") {
        			stompClient.send("/app/iheater", {}, JSON.stringify({'deviceId': vm.selectedDevice.id}));
        		} else {
        			stompClient.send("/app/iWater", {}, JSON.stringify({'deviceId': vm.selectedDevice.id}));
        		}
        }
        
        function updateMqttMessage(message) {
        	var mqtt = JSON.parse(message.body);
        	var topic = mqtt.topic;
        	var content = JSON.parse(mqtt.message);
        	
        	// Only update if topic equal device id
        	// iwater/USeM0000
        	if (vm.selectedDevice == undefined || vm.selectedDevice == null || topic.indexOf(vm.selectedDevice.id) == -1) {
        		return;
        	}
        	// Only SHW
        	if (content.type != "SHW") {
        		return;
        	}
        	// Update para
        	//{name: val.name, unit: val.unit, value: 0.0, time: 0};
        	angular.forEach(vm.selectedDevice.mqttParam, function (val, key) {
        		var name = val.name;
        		if (content.PA[name] != null && content.PA[name] != undefined) {
        			console.log("PA name:" + name + ", value:" + content.PA[name]);
        			val.value = content.PA[name];
        			val.unit = content.PA['u'];
        			val.time = content.time * 1000;
        		}
            });
        	
        	$scope.$apply();
        }
        
//        function subscribeTopic() {
//        	stompClient.subscribe('/topic/iWater', function (message) {
//            	console.log(message);
//            });
//        }
        //========================
        
        function toogleFullscreen() {
        	console.log("toogleFullscreen");
        	if ($('#sidenav-right').hasClass("sidebar-fullscreen")) {
        		$('#sidenav-right').removeClass("sidebar-fullscreen");
            	$('#sidenav-right').addClass("side-bar-map");
        	} else {
        		$('#sidenav-right').addClass("sidebar-fullscreen");
            	$('#sidenav-right').removeClass("side-bar-map");
        	}
        }
        
        // click on maker
        function clickOnMarker(marker, eventName, model, eventArgs) {
            //marker.setAnimation(google.maps.Animation.BOUNCE);
//        	if (marker.options.animation == null) {
//        		marker.options.animation = google.maps.Animation.BOUNCE;
//        	} else {
//        		marker.options.animation = null;	
//        	}
        	
        	// Open selected device
//        	$scope.$apply();
        	// Show selected device
        	showDevice(marker.model.options.extra);
        }
        
        function showSideBar() {
        	if ($mdSidenav('right').isOpen() || $mdSidenav('left').isOpen()) {
        		return true;
        	}
        	return false;
        }
        
        function initDeviceParam(device) {
        	console.log(device);
        	var param = {};
        	angular.forEach(device.para, function (val, key) {
        		param[val.name] = {name: val.name, unit: val.unit, value: 0.0, time: null};
            });
        	
        	console.log(param);
        	device.mqttParam = param;
        }
        
        function loadMoreDeviceInfo() {
        	Country.getbyCountryCode({countryCode: vm.selectedDevice.countryCode}, function(result) {
        		vm.selectedDevice.countryCodeObj = result;
        	});
        	
        	ModelDevice.getByModel({model: vm.selectedDevice.model}, function(result) {
        		vm.selectedDevice.modelObj = result;
        	});
        	
        	City.get({id: vm.selectedDevice.cityCode}, function(result) {
        		vm.selectedDevice.cityObj = result;
        	});
        }
        
        function showDevice(device) {
        	//vm.selectedDevice = device;
        	vm.map.center.latitude = device.gpsX;
        	vm.map.center.longitude = device.gpsY;
        	
        	Device.get({id: device.id}, onSaveSuccess, onSaveError);
        	
        	function onSaveSuccess (result) {
        		vm.selectedDevice = result;
        		// select model
            	angular.forEach(vm.models, function (model, key) {
    				// wQ
    				if (vm.selectedDevice.model == model.model) {
    					vm.selectedDevice.models = model;
    				}
                });
            	
	            	initDeviceParam(vm.selectedDevice);
	            	
	            	// Request scribe socket
	            	requestSubscribeTopic();
	            	
	            	loadMoreDeviceInfo();
	            };
	
	            function onSaveError () {
	            }
	            
	            if (!$mdSidenav('right').isOpen()) {
	        		tooggleSelectedDevice();
	        	}
        }
        
        function tooggleSelectedDevice() {
            // Component lookup should always be available since we are not using `ng-if`
            $mdSidenav('right')
              .toggle()
              .then(function () {
                	console.log('Toogle left');
              });
        }
        
        function tooggleDevices() {
            // Component lookup should always be available since we are not using `ng-if`
        	console.log('click Toogle left');
            $mdSidenav('left')
              .toggle()
              .then(function () {
                	console.log('Toogle left');
              });
        }
        
        function loadAllModelDevice() {
        	ModelDevice.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.models = result;
            };

            function onSaveError () {
            }
        }
        
        function loadAllCity() {
        	City.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.cities = result;
            };

            function onSaveError () {
            }
        }
        
        function loadAllCountry() {
        	Country.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.countries = result;
            };

            function onSaveError () {
            }
        }
        
        function clean() {
        	vm.searchDevice = {};
        	loadAllDevice();
        }
        
        $scope.$watch('vm.devices', function (newValue) {
        	// publish data for selectedDevices
        	angular.forEach(newValue, function (device, key) {
        		var maker = {
            		    id:device.id,
            		    coords: {
            		        latitude: device.gpsX,
            		        longitude: device.gpsY
            		    },
            		    options: {
//            		        icon: {
//            		            url: 'content/icon/iWater-GRAY.png'
//            		        },
            		        extra: device
            		}
                };

        		vm.map.markers.push(maker);
        	});
        });
        
        function search() {
        	vm.map.markers = [];
        	vm.devices = [];
        	
    		Device.searchWithMapData(vm.searchDevice, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			// Show message
            	var message = $translate.instant('kanHomeApp.map.found-device', { number: result.length });
            	showMessage(message);
            	
            	// Move to marker on map
            	if (result != null && result.size > 0) {
            		var deviceTmp = result[0];
            		vm.map.center.latitude = deviceTmp.gpsX;
                	vm.map.center.longitude = deviceTmp.gpsY;
            	}
            	
            	vm.devices = result;
            	//$scope.$apply();
            	
            	// Load status
//            	loadAllStatus();
            };

            function onSaveError () {
            }
        	
        };

        function showMessage(msg) {
        	$mdToast.show(
        		      $mdToast.simple()
        		        .textContent(msg)
        		        //.position(pinTo )
        		        .hideDelay(5000)
        	);
        }
        
        function loadAllStatus() {
        	DeviceStat.queryByIds(vm.devices, onSaveSuccess, onSaveError);
            
            function onSaveSuccess (result) {
            	angular.forEach(vm.map.markers, function (marker, key) {
        			angular.forEach(result, function (stat, keyStat) {
        				// wQ
//        				if (marker.id == stat.id) {
//        					marker.options.icon.url = 'content/icon/iWater-' + stat.wQ + '.png';
//        				}
                    });
                });
            	
            	angular.forEach(vm.devices, function (device, key) {
        			angular.forEach(result, function (stat, keyStat) {
        				// wQ
        				if (device.id == stat.id) {
        					device.status = stat.dsts;
        				}
                    });
                });
        		
        		// No-status
        		angular.forEach(vm.devices, function (device, key) {
    				// wQ
    				if (device.status == "" || device.status == undefined) {
    					device.status = 'GRAY';
    				}
                });
            };

            function onSaveError () {
            }
        }
        
        function loadAllDevice () {
        	vm.map.markers = [];
        	Device.queryWithMapData({}, onSaveSuccess, onSaveError);
            
            function onSaveSuccess (result) {
            	//$scope.$apply();
            	// Show message
            	var message = $translate.instant('kanHomeApp.map.found-device', { number: result.length });
            	showMessage(message);
            	
            	vm.devices = result;
            	
            	// Load status
            	// TODO: namnh 22/12/2017 commnet: 
            	// loadAllStatus();
            };

            function onSaveError () {
            }
        }
    }
})();
