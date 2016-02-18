var tvShowsApp = angular.module('tvShowsApp', [
    'angular-growl',
    'ui.bootstrap',
    'ngSanitize',
    'ngAnimate'
]);

tvShowsApp.config(["growlProvider", "$httpProvider", function(growlProvider, $httpProvider) {
    growlProvider.globalTimeToLive(30000);
    growlProvider.globalEnableHtml(true);
    growlProvider.onlyUniqueMessages(true);
}]);

tvShowsApp.service('TvShowDetailsService', function($http, $q) {
    // Return public API.
    return({
        addDetails: addDetails
    });

    function addDetails( growl, series, item ) {
        $http.get('/api/series/' + series + '/' + item.title + '/details').success(function(data) {
            if(data.status == 'Ended') {
                growl.addWarnMessage('<b>Warning:</b>&nbsp;&nbsp;' + item.title + ' has Ended!')
            }
        });
    }
});

tvShowsApp.controller('AppInfoCtrl', function ($scope, growl, $http) {
    $http.get('/info').success(function(data) {
        $scope.appVersion = data.app.version
    });
});

tvShowsApp.controller('BackupRestoreCtrl', function ($scope, growl, $http) {
    $scope.backup = function() {
        growl.addInfoMessage('<b>Not yet implemented:</b>&nbsp;&nbsp;')
    }

    $scope.restore = function() {
        growl.addInfoMessage('<b>Not yet implemented:</b>&nbsp;&nbsp;')
    }
});

tvShowsApp.controller('ListHdTvShowsCtrl', function ($scope, growl, $http, TvShowDetailsService) {
    $http.get('/api/series/hdGroup').success(function(data) {
      $scope.hdtvshows = data;
      angular.forEach(data, function(item) {
          TvShowDetailsService.addDetails(growl, 'hdGroup', item)
      });
    });

    $scope.addShow = function(title, exact) {
        var settings = ''
        if (exact) {
            settings = JSON.stringify({"exact": true})
            $scope.tvshow.addExactTitle = false;
        }
        $http({
            url: "/api/series/hdGroup/" + title,
            method: "POST",
            data: settings,
            headers: {'Content-Type': 'application/json'}
        }).success(function (item) {
            $scope.hdtvshows.push(item);
            $scope.$apply();
            $scope.tvshow.addTitle = null;
            TvShowDetailsService.addDetails(growl, 'hdGroup', item)
        }).error(function (resp) {
            $scope.tvshow.addTitle = null;
            growl.addErrorMessage('<b>Error:</b>&nbsp;&nbsp;' + resp.message)
        });
    }

    $scope.deleteShow = function(tvshow) {
        $http.delete("/api/series/hdGroup/" + tvshow.title).success(function () {
            var index = $scope.hdtvshows.indexOf(tvshow);
            $scope.hdtvshows.splice(index, 1);
            $scope.$apply();
        });
    }
});

tvShowsApp.controller('ListStdTvShowsCtrl', function ($scope, growl, $http, TvShowDetailsService) {
    $http.get('/api/series/stdGroup').success(function(data) {
        $scope.stdtvshows = data;
        angular.forEach(data, function(item) {
            TvShowDetailsService.addDetails(growl, 'stdGroup', item)
        });
    });

    $scope.addShow = function(title, exact) {
        var settings = ''
        if (exact) {
            settings = JSON.stringify({"exact": true})
            $scope.tvshow.addExactTitle = false;
        }
        $http({
            url: "/api/series/stdGroup/" + title,
            method: "POST",
            data: settings,
            headers: {'Content-Type': 'application/json'}
        }).success(function (item) {
            $scope.stdtvshows.push(item);
            $scope.$apply();
            $scope.tvshow.addTitle = null;
            TvShowDetailsService.addDetails(growl, 'stdGroup', item)
        }).error(function (resp) {
            $scope.tvshow.addTitle = null;
            growl.addErrorMessage('<b>Error:</b>&nbsp;&nbsp;' + resp.message)
        });
    }

    $scope.deleteShow = function(tvshow) {
        if (confirm("Are you sure? Deleting: " + tvshow.title + "?")) {
            $http.delete("/api/series/stdGroup/" + tvshow.title).success(function () {
                var index = $scope.stdtvshows.indexOf(tvshow);
                $scope.stdtvshows.splice(index, 1);
                $scope.$apply();
            });
        }
    }
});

tvShowsApp.directive('ngConfirmMessage', [function () {
  return {
      restrict: 'A',
      link: function (scope, element, attrs) {
          element.on('click', function (e) {
              var message = attrs.ngConfirmMessage || "Are you sure ?";
              if (!confirm(message)) {
                  e.stopImmediatePropagation();
              }
          });
      }
  }
}]);
