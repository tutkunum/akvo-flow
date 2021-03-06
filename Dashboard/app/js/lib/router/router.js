// ***********************************************//
//                 Router
// ***********************************************//
require('akvo-flow/core-common');

FLOW.Router = Ember.Router.extend({
  enableLogging: true,
  loggedIn: false,
  location: 'none',
  //'hash'or 'none' for URLs

  resetState: function () {
    // We could have unsaved changes
    FLOW.store.commit();

    FLOW.selectedControl.set('selectedQuestionGroup', null);
    FLOW.selectedControl.set('selectedSurveyGroup', null);
    FLOW.selectedControl.set('selectedSurvey', null);
    FLOW.selectedControl.set('selectedQuestion', null);
    FLOW.selectedControl.set('selectedCascadeResource', null);
    FLOW.selectedControl.set('cascadeImportNumLevels', null);
    FLOW.selectedControl.set('cascadeImportIncludeCodes', null);
    FLOW.surveyControl.set('content', null);
    FLOW.questionControl.set('OPTIONcontent', null);
    FLOW.metaControl.set('since', null);
  },

  root: Ember.Route.extend({
    doNavSurveys: function (router, context) {
      router.transitionTo('navSurveys.index');
    },
    doNavDevices: function (router, context) {
      router.transitionTo('navDevices.index');
    },
    doNavData: function (router, context) {
      router.transitionTo('navData.index');
    },
    doNavResources: function (router, context) {
      router.transitionTo('navResources.index');
    },
    doNavMaps: function (router, context) {
      router.transitionTo('navMaps');
    },
    doNavUsers: function (router, context) {
      router.transitionTo('navUsers');
    },
    doNavMessages: function (router, context) {
      router.transitionTo('navMessages');
    },
    // not used at the moment
    doNavAdmin: function (router, context) {
      router.transitionTo('navAdmin');
    },

    // non-working code for transitioning to navHome at first entry of the app
    //    setup: function(router){
    //      router.send("goHome");
    //    },
    //    goHome:function(router){
    //      router.transitionTo('navHome');
    //    },
    index: Ember.Route.extend({
      route: '/',
      redirectsTo: 'navSurveys.index'
    }),

    // ******************* SURVEYS ROUTER ********************
    navSurveys: Ember.Route.extend({
      route: '/surveys',
      connectOutlets: function (router, event) {
        router.get('applicationController').connectOutlet('navSurveys');
        router.set('navigationController.selected', 'navSurveys');
      },

      doNewSurvey: function (router, event) {
        router.transitionTo('navSurveys.navSurveysNew');
      },

      doEditSurvey: function (router, event) {
        FLOW.selectedControl.set('selectedSurvey', event.context);
        router.transitionTo('navSurveys.navSurveysEdit.index');
      },

      doSurveysMain: function (router, event) {
        FLOW.selectedControl.set('selectedQuestion', null);
        router.transitionTo('navSurveys.navSurveysMain');
      },

      index: Ember.Route.extend({
        route: '/',
        redirectsTo: 'navSurveysMain'
      }),

      navSurveysMain: Ember.Route.extend({
        route: '/main',
        connectOutlets: function (router, event) {
          router.get('navSurveysController').connectOutlet({
            name: 'navSurveysMain'
          });
          FLOW.projectControl.populate();
          FLOW.surveyControl.populateAll();
          FLOW.cascadeResourceControl.populate();
          FLOW.projectControl.set('currentProject', null);
          FLOW.projectControl.set('newlyCreated', null);
          FLOW.selectedControl.set('selectedQuestionGroup', null);
          FLOW.selectedControl.set('selectedSurvey', null);
          FLOW.selectedControl.set('selectedQuestion', null);
          FLOW.questionControl.set('OPTIONcontent', null);
        }
      }),

      navSurveysNew: Ember.Route.extend({
        route: '/new',
        connectOutlets: function (router, event) {
          var newSurvey;

          newSurvey = FLOW.store.createRecord(FLOW.Survey, {
            "name": "",
            "defaultLanguageCode": "en",
            "requireApproval": false,
            "status": "NOT_PUBLISHED",
            "surveyGroupId": FLOW.selectedControl.selectedSurveyGroup.get('keyId'),
            "version":"1.0"
          });

          FLOW.selectedControl.set('selectedSurvey', newSurvey);
          router.transitionTo('navSurveys.navSurveysEdit.index');
        }
      }),

      navSurveysEdit: Ember.Route.extend({
        route: '/edit',
        connectOutlets: function (router, event) {
          router.get('navSurveysController').connectOutlet({
            name: 'navSurveysEdit'
          });
          // all questions should be closed when we enter
          FLOW.selectedControl.set('selectedQuestion', null);
        },

        doEditQuestions: function (router, event) {
          router.transitionTo('navSurveys.navSurveysEdit.editQuestions');
        },

        index: Ember.Route.extend({
          route: '/',
          redirectsTo: 'editQuestions'
        }),

        manageNotifications: Ember.Route.extend({
          route: '/notifications',
          connectOutlets: function (router, event) {
            router.get('navSurveysEditController').connectOutlet({
              name: 'manageNotifications'
            });
            FLOW.notificationControl.populate();
          }
        }),

        manageTranslations: Ember.Route.extend({
          route: '/translations',
          connectOutlets: function (router, event) {
            router.get('navSurveysEditController').connectOutlet({
              name: 'manageTranslations'
            });
            FLOW.translationControl.populate();
          }
        }),

        editQuestions: Ember.Route.extend({
          route: '/questions',
          connectOutlets: function (router, event) {
            router.get('navSurveysEditController').connectOutlet({
              name: 'editQuestions'
            });

          }
        })
      })
    }),

    //********************** DEVICES ROUTER *******************
    navDevices: Ember.Route.extend({
      route: '/devices',
      connectOutlets: function (router, event) {
        router.get('applicationController').connectOutlet('navDevices');
        router.set('navigationController.selected', 'navDevices');
      },

      doCurrentDevices: function (router, event) {
        router.transitionTo('navDevices.currentDevices');
      },

      doAssignSurveysOverview: function (router, event) {
        router.transitionTo('navDevices.assignSurveysOverview');
      },

      doEditSurveysAssignment: function (router, event) {
        router.transitionTo('navDevices.editSurveysAssignment');
      },

      doSurveyBootstrap: function (router, event) {
        router.transitionTo('navDevices.surveyBootstrap');
      },

      index: Ember.Route.extend({
        route: '/',
        redirectsTo: 'currentDevices'
      }),

      currentDevices: Ember.Route.extend({
        route: '/current-devices',
        connectOutlets: function (router, context) {
          router.get('navDevicesController').connectOutlet('currentDevices');
          router.resetState();
          FLOW.deviceGroupControl.populate();
          FLOW.deviceControl.populate();
          router.set('devicesSubnavController.selected', 'currentDevices');
        }
      }),

      assignSurveysOverview: Ember.Route.extend({
        route: '/assign-surveys',
        connectOutlets: function (router, context) {
          router.get('navDevicesController').connectOutlet('assignSurveysOverview');
          FLOW.surveyAssignmentControl.populate();
          router.set('devicesSubnavController.selected', 'assignSurveys');
        }
      }),

      editSurveysAssignment: Ember.Route.extend({
        route: '/assign-surveys',
        connectOutlets: function (router, context) {
          router.get('navDevicesController').connectOutlet('editSurveyAssignment');
          router.set('devicesSubnavController.selected', 'assignSurveys');
        }
      }),

      surveyBootstrap: Ember.Route.extend({
        route: '/manual-transfer',
        connectOutlets: function (router, context) {
          router.get('navDevicesController').connectOutlet('surveyBootstrap');
          router.set('devicesSubnavController.selected', 'surveyBootstrap');
        }
      })
    }),


    // ******************* DATA ROUTER ***********************
    navData: Ember.Route.extend({
      route: '/data',
      connectOutlets: function (router, event) {
        router.get('applicationController').connectOutlet('navData');
        router.set('navigationController.selected', 'navData');
      },

      doInspectData: function (router, event) {
        router.transitionTo('navData.inspectData');
      },
      doBulkUpload: function (router, event) {
        router.transitionTo('navData.bulkUpload');
      },
      doDataCleaning: function (router, event) {
        router.transitionTo('navData.dataCleaning');
      },
      doMonitoringData: function (router, event) {
        router.transitionTo('navData.monitoringData');
      },

      doReportsList: function (router, event) {
        router.transitionTo('navData.reportsList');
      },

      doExportReports: function (router, event) {
        router.transitionTo('navData.exportReports');
      },

      doChartReports: function (router, event) {
        router.transitionTo('navData.chartReports');
      },

      index: Ember.Route.extend({
        route: '/',
        redirectsTo:  'inspectData'
      }),

      inspectData: Ember.Route.extend({
        route: '/inspectdata',
        connectOutlets: function (router, context) {
          router.get('navDataController').connectOutlet('inspectData');
          router.set('datasubnavController.selected', 'inspectData');
          router.resetState();
        }
      }),

      bulkUpload: Ember.Route.extend({
        route: '/bulkupload',
        connectOutlets: function (router, context) {
          router.get('navDataController').connectOutlet('bulkUpload');
          router.set('datasubnavController.selected', 'bulkUpload');
        }
      }),

      dataCleaning: Ember.Route.extend({
        route: '/datacleaning',
        connectOutlets: function (router, context) {
          router.get('navDataController').connectOutlet('dataCleaning');
          router.set('datasubnavController.selected', 'dataCleaning');
          router.resetState();
        }
      }),

      monitoringData: Ember.Route.extend({
        route: '/monitoringdata',
        connectOutlets: function (router, context) {
          router.get('navDataController').connectOutlet('monitoringData');
          router.set('datasubnavController.selected', 'monitoringData');
          router.resetState();
        }
      }),

      reportsList: Ember.Route.extend({
        route: '/reportslist',
        connectOutlets: function (router, context) {
          //if landing on tab, show reports list first
          router.get('navDataController').connectOutlet('reportsList');
          router.set('datasubnavController.selected', 'exportReports');
          router.resetState();
        }

      }),

      exportReports: Ember.Route.extend({
        route: '/exportreports',
        connectOutlets: function (router, context) {
          router.get('navDataController').connectOutlet('exportReports');
          router.set('datasubnavController.selected', 'exportReports');
          router.resetState();
        }
      }),

      chartReports: Ember.Route.extend({
        route: '/chartreports',
        connectOutlets: function (router, context) {
          router.resetState();
          router.get('navDataController').connectOutlet('chartReports');
          router.set('datasubnavController.selected', 'chartReports');
        }
      })
    }),

    // ************************** RESOURCES ROUTER **********************************
    navResources: Ember.Route.extend({
      route: '/resources',
      connectOutlets: function (router, event) {
        router.get('applicationController').connectOutlet('navResources');
        router.set('navigationController.selected', 'navResources');
      },

      doCascadeResources: function (router, event) {
        router.transitionTo('navResources.cascadeResources');
      },

      doDataApproval: function (router, event) {
          router.transitionTo('navResources.dataApproval.listApprovalGroups');
      },

      index: Ember.Route.extend({
        route: '/',
        redirectsTo: 'cascadeResources'
      }),

      cascadeResources: Ember.Route.extend({
        route: '/cascaderesources',
        connectOutlets: function (router, context) {
          router.get('navResourcesController').connectOutlet('cascadeResources');
          router.set('resourcesSubnavController.selected', 'cascadeResources');
          FLOW.cascadeResourceControl.populate();
        }
      }),

      dataApproval: Ember.Route.extend({
          route: '/dataapproval',

          connectOutlets: function (router, context) {
              router.get('navResourcesController').connectOutlet('dataApproval');
              router.set('resourcesSubnavController.selected', 'approvalGroup');
          },

          doAddApprovalGroup: function (router, event) {
              router.get('approvalGroupController').add();
              router.get('approvalStepsController').loadByGroupId();
              router.transitionTo('navResources.dataApproval.editApprovalGroup');
          },

          doEditApprovalGroup: function (router, event) {
              var groupId = event.context.get('keyId');
              var lastLoadedGroup = router.get('approvalGroupController').get('content');
              if (!lastLoadedGroup || (lastLoadedGroup.get('keyId') !== groupId)) {
                  router.get('approvalGroupController').load(groupId);
                  router.get('approvalStepsController').loadByGroupId(groupId);
              }
              router.transitionTo('navResources.dataApproval.editApprovalGroup');
          },

          doSaveApprovalGroup: function (router, event) {
              router.get('approvalGroupController').save();
              router.transitionTo('navResources.dataApproval.listApprovalGroups');
          },

          doCancelEditApprovalGroup: function (router, event) {
              router.get('approvalGroupController').cancel();
              router.transitionTo('navResources.dataApproval.listApprovalGroups');
          },

          doDeleteApprovalGroup: function (router, event) {
              var group = event.context;
              router.get('approvalGroupListController').delete(group);
          },

          // default route for dataApproval tab
          listApprovalGroups: Ember.Route.extend({
              route: '/list',

              connectOutlets: function (router, context) {
                  router.get('dataApprovalController').connectOutlet('approvalMain', 'approvalGroupList');
                  var approvalList = router.get('approvalGroupListController');
                  if (!approvalList.get('content')) {
                      approvalList.set('content', FLOW.ApprovalGroup.find())
                  }
              },
          }),

          editApprovalGroup: Ember.Route.extend({
              route: '/approvalsteps',

              connectOutlets: function (router, event) {
                  router.get('dataApprovalController').connectOutlet('approvalMain', 'approvalGroup');
                  router.get('approvalGroupController').connectOutlet('approvalStepsOutlet', 'approvalSteps');
              },
          }),
      })
    }),

    // ************************** MAPS ROUTER **********************************
    navMaps: Ember.Route.extend({
      route: '/maps',
      connectOutlets: function (router, context) {
        router.get('applicationController').connectOutlet('navMaps');
        router.set('navigationController.selected', 'navMaps');
      }
    }),

    // ************************** USERS ROUTER **********************************
    navUsers: Ember.Route.extend({
      route: '/users',
      connectOutlets: function (router, context) {
        router.get('applicationController').connectOutlet('navUsers');
        router.set('navigationController.selected', 'navUsers');
      }
    }),

    // ************************** MESSAGES ROUTER **********************************
    navMessages: Ember.Route.extend({
      route: '/users',
      connectOutlets: function (router, context) {
        router.get('applicationController').connectOutlet('navMessages');
        router.set('navigationController.selected', 'navMessages');
        FLOW.messageControl.populate();
        router.resetState();
      }
    }),

    // ************************** ADMIN ROUTER **********************************
    // not used at the moment
    navAdmin: Ember.Route.extend({
      route: '/admin',
      connectOutlets: function (router, context) {
        router.get('applicationController').connectOutlet('navAdmin');
        router.set('navigationController.selected', 'navAdmin');
      }
    })
  })
});
