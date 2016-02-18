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

tvShowsApp.factory('LatestBuildService', function($http) {
    var getData = function() {
        return $http({
                method: "GET",
                url: "https://www.recknerd.com/jenkins/job/flexget-series-ui+ci/lastSuccessfulBuild/api/json"
            }).then(function(result){
            return angular.fromJson(result.data.artifacts[0]);
        });
    };
    return { getData: getData };
});

tvShowsApp.controller('AppInfoCtrl', function ($scope, growl, $http, LatestBuildService) {

    $http.get('/info').success(function(data) {
        $scope.appVersion = data.app.version

        var latestBuildPromise = LatestBuildService.getData();
        latestBuildPromise.then(function(result) {
           // 1.0.0-SNAPSHOT
           var currentVersion = $scope.appVersion.split("-")[0]

           // flexget-series-ui-1.0.0-SNAPSHOT.jar
           //[flexget,series,ui,1.0.0,SNAPSHOT]
           // flexget-series-ui-1.1.0.jar
           //[flexget,series,ui,1.1.0]
           var latestBuildVersion = result.fileName.slice(0, -4).split("-")[3]
           console.log("Comparing: " + currentVersion + " to " + latestBuildVersion)
           if(versionCompare(currentVersion, latestBuildVersion) < 0) { // Notify when current version < latest version
               growl.addInfoMessage('<b>New Update Available:</b>&nbsp;&nbsp;' + latestBuildVersion)
           }
        });
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

function versionCompare(v1, v2, options) {
    var lexicographical = options && options.lexicographical,
        zeroExtend = options && options.zeroExtend,
        v1parts = v1.split('.'),
        v2parts = v2.split('.');

    function isValidPart(x) {
        return (lexicographical ? /^\d+[A-Za-z]*$/ : /^\d+$/).test(x);
    }

    if (!v1parts.every(isValidPart) || !v2parts.every(isValidPart)) {
        return NaN;
    }

    if (zeroExtend) {
        while (v1parts.length < v2parts.length) v1parts.push("0");
        while (v2parts.length < v1parts.length) v2parts.push("0");
    }

    if (!lexicographical) {
        v1parts = v1parts.map(Number);
        v2parts = v2parts.map(Number);
    }

    for (var i = 0; i < v1parts.length; ++i) {
        if (v2parts.length == i) {
            return 1;
        }

        if (v1parts[i] == v2parts[i]) {
            continue;
        }
        else if (v1parts[i] > v2parts[i]) {
            return 1;
        }
        else {
            return -1;
        }
    }

    if (v1parts.length != v2parts.length) {
        return -1;
    }

    return 0;
}
