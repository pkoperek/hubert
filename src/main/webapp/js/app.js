var hubertApp = angular.module('hubertApp', ['ui.bootstrap', 'ngTagsInput']);

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
                var experiment = "{ 'id': -1, 'name':" + $scope. + "}"
                $http.post("experiments/run", experiment).success(function(data) {
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

hubertApp.controller('newExperimentController', function($scope, $modalInstance, $log, configuration) {

    $scope.configuration = configuration;
    
    $scope.loadWords = function() {
        $log.info("Loading words: " + $scope.selectedLanguage.words);
        
        var words = [];
        var selectedWords = $scope.selectedLanguage.words;

        for(var key in selectedWords) {
            words.push(selectedWords[key]);
        }
        
        $log.info("After loop: " + words);
        
        return words;
    };

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

function fetchData() {
    var initInjector = angular.injector(["ng"]);
    var $http = initInjector.get("$http");
    var $log = initInjector.get("$log");

    return $http.get("config").then(function(response) {
        $log.info("Got config from service: " + response.data);
        hubertApp.constant("configuration", response.data);
    }, function(errorResponse) {
        // TODO: Handle error case
    });
}

function bootstrapApplication() {
    angular.element(document).ready(function() {
        angular.bootstrap(document, ["hubertApp"]);
    });
    
    var loadingOverlay = document.getElementById('loading-overlay');
    loadingOverlay.parentNode.removeChild(loadingOverlay);
    var backdropOverlay = document.getElementById('backdrop-overlay');
    backdropOverlay.parentNode.removeChild(backdropOverlay);
}

fetchData().then(bootstrapApplication);