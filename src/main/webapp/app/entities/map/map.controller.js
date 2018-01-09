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
                    styles: [
                        {
                            featureType: 'poi.business',
                            stylers: [{visibility: 'off'}]
                          },
                          {
                            featureType: 'transit',
                            elementType: 'labels.icon',
                            stylers: [{visibility: 'off'}]
                          }
                        ]
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
        vm.isDeviceActive = isDeviceActive;
        vm.isFilterActive = isFilterActive;
        vm.changeFilter = changeFilter;
        vm.devicesInfo = {
        		num_gray: 0
        		, num_green: 0
        		, num_yellow: 0
        		, num_red: 0
        		, num_all: 0
        };
        vm.deviceFilterSelected = 0;
        vm.statusFilter;
        
        function reDrawMaker(status) {
        	vm.map.markers = [];
        	
        	var isAlreadyMove = false;
        	
        	angular.forEach(vm.devices, function (device, key) {
        		if (status == '' || device.dsts == status) {
        			var maker = {
                		    id:device.id,
                		    coords: {
                		        latitude: device.gpsX,
                		        longitude: device.gpsY
                		    },
                		    options: {
                		        extra: device
                		}
                    };
        			vm.map.markers.push(maker);
        		}
        	});
		
        	// Move to marker on map
        	if (vm.map.markers != null && vm.map.markers.length > 0) {
        		var markerTmp = vm.map.markers[0];
        		vm.map.center.latitude = markerTmp.coords.latitude;
            	vm.map.center.longitude = markerTmp.coords.longitude;
        	}
        }
        
        function changeFilter(status) {
    		if (status == 1) {
    			vm.statusFilter = 'GRAY';
    		} else if (status == 2) {
    			vm.statusFilter = 'BLUE';
			} else if (status == 3) {
				vm.statusFilter = 'YELLOW';
			} else if (status == 4) {
				vm.statusFilter = 'RED';
			} else {
				vm.statusFilter = '';
			}
        		
    		vm.deviceFilterSelected = status;
    		// redraw maker
    		reDrawMaker(vm.statusFilter);
        }
        
        function isFilterActive(position) {
        		return vm.deviceFilterSelected === position;
        }
        
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
        
        function isDeviceActive(device) {
        		if (vm.selectedDevice == undefined || vm.selectedDevice == null) {
        			return false;
        		}
        		return vm.selectedDevice.id === device.id;
        }
        
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
        			//val.unit = content.PA['u'];
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
	        	// Reset
	    		vm.devicesInfo = {
	            		num_gray: 0
	            		, num_green: 0
	            		, num_yellow: 0
	            		, num_red: 0
	            		, num_all: 0
	            };
	    		vm.devicesInfo.num_all = vm.devices.length;
        	
	    		reDrawMaker('');
        });
        
        function search() {
        	vm.map.markers = [];
        	vm.devices = [];
        	
    		Device.searchWithMapData(vm.searchDevice, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			// Show message
            	var message = $translate.instant('kanHomeApp.map.found-device', { number: result.length });
            	showMessage(message);
            	
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
        
        function loadAllDevice () {
        	vm.map.markers = [];
        	Device.queryWithMapData({}, onSaveSuccess, onSaveError);
            
            function onSaveSuccess (result) {
            	//$scope.$apply();
            	// Show message
            	var message = $translate.instant('kanHomeApp.map.found-device', { number: result.length });
            	showMessage(message);
            	
            	vm.devices = result;
            };

            function onSaveError () {
            }
        }
    }
})();
