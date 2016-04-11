'use strict';

angular.module('backendApp')
    .controller('RegisterController', function ($scope, $translate, $timeout, Auth, vcRecaptchaService) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {recaptcha:{}};
        $scope.recaptchaInvalid = null;

        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.errorEmailExists = null;
                $scope.recaptchaInvalid = null;

                Auth.createAccount($scope.registerAccount).then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        $scope.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        $scope.errorEmailExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'invalid recaptcha') {
                        $scope.recaptchaInvalid = 'ERROR';
                    }else {
                        $scope.error = 'ERROR';
                    }
                });
            }
        };

        $scope.setWidgetId = function (widgetId) {
            $scope.registerAccount.recaptcha.widgetId = widgetId;
        };

    });
