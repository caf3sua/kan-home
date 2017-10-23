(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('TopicController', TopicController);

    TopicController.$inject = ['$state', '$timeout', 'Topic', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function TopicController($state, $timeout, Topic, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        // mqtt
        vm.subscriptions = subscriptions;
        vm.publish = publish;
        
        // socket
        var stompClient = null;
        vm.connect = connect;
        vm.disconnect = disconnect;
        vm.sendName = sendName;
        
        //======================
        function connect() {
            var socket = new SockJS('/kanhome-websocket');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
//                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/notification', function (greeting) {
                	console.log(greeting);
                    //showGreeting(JSON.parse(greeting.body).content);
                });
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
        
        
        loadAll();

        function subscriptions() {
        	mqtt.on('testtopic/1', function( t, msg ){
        		//var uint8array = new TextEncoder("utf-8").encode(msg);
        		var string = new TextDecoder("utf-8").decode(msg);
                console.log(string);
            });
        }
        
        function publish() {
        	mqtt.publish('testtopic/1','hello mqtt - nam');
        }
        
        function loadAll () {
            Topic.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.topics = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
