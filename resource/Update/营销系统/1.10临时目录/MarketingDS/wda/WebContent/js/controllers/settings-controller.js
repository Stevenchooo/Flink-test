'use strict';

var SettingsCtrl = ['$rootScope', '$scope', '$stateParams', function($rootScope, $scope) {

  $scope.initialise = function() {

    $scope.tabData   = [
      {
        heading: 'One',
        route:   'app.user.settings.one',
        url: 'user/settings/one/:test',
        controller: 'ExampleCtrl'
      },
      {
        heading: 'Two',
        route:   'app.user.settings.two'
      }
    ];
  };

  $scope.initialise();
}];

angular.module('app').controller('SettingsCtrl', SettingsCtrl);
