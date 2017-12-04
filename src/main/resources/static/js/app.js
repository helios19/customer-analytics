
var app = angular.module('customerAnalytics',['ui.router','ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8080/',
    TRANSACTION_SERVICE_API : 'http://localhost:8080/transaction-summary'
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'templates/list.html',
                controller:'TransactionController',
                controllerAs:'ctrl'
            });
        $urlRouterProvider.otherwise('/');
    }]);