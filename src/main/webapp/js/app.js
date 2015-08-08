var hubertApp = angular.module('hubertApp', ['ui.bootstrap']);

hubertApp.controller('NavbarButtonController', function($scope, $modal, $log, $http) {

    var openModal = function(modalToOpen, successCallback, failureCallback) {
        var new_experiment = $modal.open({
            animation: true,
            size: 'lg',
            controller: modalToOpen + 'Controller',
            templateUrl: modalToOpen + 'Modal.html'
        });

        new_experiment.result.then(successCallback, failureCallback);
    };

    $scope.new_experiment = function() {
        openModal(
            'newExperiment',
            function(result) {
                $log.info("New experiment modal success: " + result);
                $http.post("add", "{experiment}").success(function(data) {
                    $log.info("Got data from service: " + data)
                })
            },
            function(reason) {
                $log.info("New experiment modal cancelled: " + reason);
            }
        );
        $log.info("defining new experiment");
    };
    
    $scope.upload_experiment = function() {
        openModal(
            'uploadExperiment',
            function(result) {
                $log.info("Upload experiment modal success: " + result);
            },
            function(reason) {
                $log.info("Upload experiment modal cancelled: " + reason);
            }
        );
        $log.info("uploading experiment");
    };
});

hubertApp.controller('newExperimentController', function($scope, $modalInstance) {

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
    
    $scope.ok = function() {
        $modalInstance.close('new experiment');
    };

});

hubertApp.controller('uploadExperimentController', function($scope, $modalInstance) {

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.ok = function() {
        $modalInstance.close('upload experiment');
    };

});