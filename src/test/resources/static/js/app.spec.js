describe('TransactionController', function() {

  beforeEach(module('customerAnalytics'));

  it('should create a transaction model objects', inject(function($controller) {
    var scope = {};
    var ctrl = $controller('TransactionController', {$scope: scope});

    expect(ctrl.transaction).toBeDefined();
    expect(ctrl.transactions).toBeDefined();
    expect(ctrl.transactions.length).toBe(0);
    expect(ctrl.classification).toEqual('');
    expect(ctrl.currentBalance).toEqual('');
    expect(ctrl.submit).toBeDefined();
    expect(ctrl.reset).toBeDefined();
    expect(ctrl.successMessage).toEqual('');
    expect(ctrl.errorMessage).toEqual('');
    expect(ctrl.done).toEqual(false);
    expect(ctrl.onlyIntegers).toEqual(/^\d+$/);
    expect(ctrl.onlyNumbers).toEqual(/^\d+([,.]\d+)?$/);

  }));

});