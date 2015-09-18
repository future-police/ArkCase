'use strict';

angular.module('search').controller('Search.InputController', ['$scope', 'SearchService','ResultService',
    function ($scope, SearchService,ResultService) {
        $scope.$emit('req-component-config', 'input');
        $scope.start = 0;
        $scope.count = 10;
        $scope.search = function () {
            if(!$scope.searchQuery){
                $scope.searchQuery='';
            }
            SearchService.queryFacetedSearch({
                input: $scope.searchQuery + '*',
                start: $scope.start,
                n: $scope.count},
            function (data) {
                ResultService.passData(data,$scope.searchQuery+'*');
            });

        };
    }
]);