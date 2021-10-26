let app = angular.module('front', [])
app.controller('getAll', function($scope, $http) {
    $scope.sort='';
    $scope.obj = {};
    $scope.getAll = function() {
        $http.get('http://localhost:8080/api/currencies', $scope.obj ).
            then(function(response) {
               $scope.currencies = response.data;
            });
    };
    
    $scope.sortName = function() {
        $scope.sort='name';
        $scope.obj = {params: {'sort': $scope.sort} };
        $scope.getAll();
    };
    
    $scope.sortTicker = function() {
        $scope.sort='ticker';
        $scope.obj = {params: {'sort': $scope.sort} };
        $scope.getAll();
    };
    
    $scope.sortCoins = function() {
        $scope.sort='number_of_coins';
        $scope.obj = {params: {'sort': $scope.sort} };
        $scope.getAll();
    };
    
    $scope.sortCap = function() {
        $scope.sort='market_cap';
        $scope.obj = {params: {'sort': $scope.sort} };
        $scope.getAll();
    };
    
    $scope.clearSort = function() {
        $scope.sort='';
        $scope.obj = {};
        $scope.getAll();
    };
    
    $scope.newCurrency = function() {
        $http.post('http://localhost:8080/api/currencies', $scope.user).
            then(function(response) {
                $scope.getAll();
            });
    };
    
    $scope.updateCurrency = function() {
        $http.put('http://localhost:8080/api/currencies/'+$scope.user.id, $scope.user).
            then(function(response) {
                $scope.getAll();
            });
    };
    
    $scope.deleteCurrency = function() {
        $http.delete('http://localhost:8080/api/currencies/'+$scope.user.id, $scope.user).
            then(function(response) {
                $scope.getAll();
            });
    };
    
    $scope.getAll();
});
