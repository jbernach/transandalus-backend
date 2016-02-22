'use strict';

angular.module('backendApp')
	.controller('ArticleDeleteController', function($scope, $uibModalInstance, entity, Article) {

        $scope.article = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Article.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
