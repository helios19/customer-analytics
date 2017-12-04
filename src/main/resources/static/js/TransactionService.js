
'use strict';
 
angular.module('customerAnalytics').factory('TransactionService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {
 
            var factory = {
                loadCustomerTransactions: loadCustomerTransactions
            };
 
            return factory;

             function loadCustomerTransactions(transaction) {
                console.log('Fetching all transactions given customer:' + transaction.customerid + ' and month:' + transaction.month);
                var deferred = $q.defer();
                $http.get(urls.TRANSACTION_SERVICE_API + '/' + transaction.customerid + '/' + transaction.month)
                    .then(
                        function (response) {
                            console.log('Fetched successfully customer transactions');
                            $localStorage.transactions = response.data.transactions;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading transactions');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
             }

        }
    ]);