'use strict';
 
angular.module('customerAnalytics').controller('TransactionController',
    ['TransactionService', '$scope',  function( TransactionService, $scope) {
 
        var self = this;
        self.transaction = {};
        self.transactions=[];
        self.classification='';
        self.currentBalance='';

        self.submit = submit;
        self.reset = reset;
 
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
 
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
 
        function submit() {
            console.log('Submitting : ' + self.transaction.customerid + '|' + self.transaction.month);

            TransactionService.loadCustomerTransactions(self.transaction)
                .then(
                    function (response) {
                        console.log('Transactions loaded successfully');
                        self.successMessage = 'Transactions loaded successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.transactions=response.data.transactions;
                        self.classification = response.data.classification;
                        self.currentBalance = response.data.currentBalance;
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while loading Transactions');
                        self.errorMessage = 'Error while loading Transactions: ' + errResponse.data.message;
                        self.successMessage='';
                    }
                );
        }

        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.transaction={};
            self.transactions=[];
            self.transaction.customerid='';
            self.transaction.month='';
            self.classification='';
            self.currentBalance='';
            $scope.myForm.$setPristine(); //reset Form
        }
    }
    ]);