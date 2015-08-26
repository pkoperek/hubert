var hubertApp = angular.module('hubertApp', ['ui.bootstrap', 'ngTagsInput']);

var openModal = function(modal, modalToOpen, successCallback, failureCallback, title, experimentModel) {
    var modalInstance = modal.open({
        animation: true,
        size: 'lg',
        controller: modalToOpen + 'Controller',
        templateUrl: modalToOpen + 'Modal.html',
        resolve: {
            title: function() { return title || "!No title passed!"; },
            experiment: function() { return experimentModel || {}; }
        }
    });

    modalInstance.result.then(successCallback, failureCallback);
};

hubertApp.controller('NavbarButtonController', function($scope, $modal, $log, $http) {

    $scope.new_experiment = function() {
        openModal(
            $modal,
            'experiment',
            function(result) {
                $log.info("New experiment to send: " + result);
                $http.post("experiments/run", result).success(function(data) {
                    $log.info("Got data from service: " + data)
                })
            },
            function(reason) {
                $log.info("New experiment modal cancelled: " + reason);
            },
            "Define new experiment"
        );
        $log.info("defining new experiment");
    };
    
    $scope.upload_experiment = function() {
        openModal(
            $modal,
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

hubertApp.controller('experimentsListController', function($scope, $modal, $log, $http) {
    
    $scope.loadingIndicator = "(loading...)";
    
    $http.get("experiments").then(function(response) {
        $log.info("Got list of experiments from service: " + response.data);
        $scope.loadingIndicator = "";
        $scope.experiments = response.data;
    }, function(errorResponse) {
        // TODO: Handle error case
    });
    
});

hubertApp.controller('experimentController', function($scope, $modalInstance, $log, configuration, title, experiment) {

    $scope.title = title;
    $scope.configuration = configuration;
    $scope.experimentName = experiment.name;
    $scope.description = experiment.description;
    $scope.iterations = experiment.iterations;
    $scope.selectedLanguage = experiment.language;

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
        var selectedWords = $scope.selectedLanguage.words;
        var classNames = [];
        for(var word in selectedWords) {
            classNames.push(selectedWords[word]['text']);
        }
    
        var newExperiment = {
            "id": -1,
            "name": $scope.experimentName || "Experiment",
            "description": $scope.experimentDescription || "Description",
            "iterations": $scope.iterations || 10,
            "language": {
                "name": $scope.selectedLanguage.name,
                "words": classNames
            }
        };

        $modalInstance.close(newExperiment);
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