'use strict';

angular.module('backendApp').controller('ArticleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Article', 'Category', '$translate',
        function($scope, $stateParams, $uibModalInstance, entity, Article, Category, $translate) {

        $scope.language = $translate.use();

        $scope.article = entity;
        $scope.categories = Category.query({size:1000});
        $scope.load = function(id) {
            Article.get({id : id}, function(result) {
                $scope.article = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:articleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.article.id != null) {
                Article.update($scope.article, onSaveSuccess, onSaveError);
            } else {
                Article.save($scope.article, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
