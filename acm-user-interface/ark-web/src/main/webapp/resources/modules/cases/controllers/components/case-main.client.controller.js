'use strict';

angular.module('cases').controller('Cases.MainController', ['$scope', '$stateParams', '$translate', 'UtilService', 'ConfigService'
    , 'Case.InfoService', 'ObjectService', 'Object.CorrespondenceService', 'Object.NoteService', 'Object.TaskService'
    , 'Object.AuditService', 'Object.CostService', 'Object.TimeService', 'dashboard', 'Dashboard.DashboardService', 'StoreService'
    , function ($scope, $stateParams, $translate, Util, ConfigService
        , CaseInfoService, ObjectService, ObjectCorrespondenceService, ObjectNoteService, ObjectTaskService
        , ObjectAuditService, ObjectCostService, ObjectTimeService, dashboard, DashboardService, Store) {

        $scope.$emit('main-component-started');

        var promiseConfig = ConfigService.getModuleConfig("cases").then(function (moduleConfig) {
            $scope.components = moduleConfig.components;
            $scope.config = _.find(moduleConfig.components, {id: "main"});
            $scope.allowedWidgets = ['details'];
            return moduleConfig;
        });


        _.forEach(dashboard.widgets, function (widget, widgetId) {
            widget.title = $translate.instant('dashboard.widgets.' + widgetId + '.title');
            widget.description = $translate.instant('dashboard.widgets.' + widgetId + '.description');
        });

        $scope.dashboard = {
            structure: '6-6',
            collapsible: false,
            maximizable: false,
            model: {
                titleTemplateUrl: 'modules/dashboard/views/dashboard-title.client.view.html'
            }
        };

        DashboardService.getConfig({moduleName: "CASE"}, function (data) {
            $scope.dashboard.caseModel = angular.fromJson(data.dashboardConfig);
            $scope.dashboard.model.titleTemplateUrl = 'modules/dashboard/views/dashboard-title.client.view.html';

        });

        $scope.$on('adfDashboardChanged', function (event, name, model) {
            DashboardService.saveConfig({
                dashboardConfig: angular.toJson(model),
                moduleName: "CASE"
            });
        });
    }
]);