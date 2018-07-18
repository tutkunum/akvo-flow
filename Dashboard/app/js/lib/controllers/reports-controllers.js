FLOW.surveyQuestionSummaryControl = Ember.ArrayController.create({
  content: null,

  doSurveyQuestionSummaryQuery: function (questionId) {
    this.set('content', FLOW.store.find(FLOW.SurveyQuestionSummary, {
      'questionId': questionId
    }));
  }
});

FLOW.chartDataControl = Ember.Object.create({
  questionText: "",
  maxPer: null,
  chartData: [],
  smallerItems: [],
  total: null
});

FLOW.chartTypeControl = Ember.Object.create({
  content: [
    Ember.Object.create({
      label: Ember.String.loc('_doughnut_chart'),
      value: "doughnut"
    }), Ember.Object.create({
      label: Ember.String.loc('_vertical_bar_chart'),
      value: "vbar"
    }),
    Ember.Object.create({
      label: Ember.String.loc('_horizontal_bar_chart'),
      value: "hbar"
    })
  ]
});

FLOW.ReportsController = Ember.ArrayController.extend({
  sortProperties: ["createdDateTime"],
  sortAscending: false,
  content: null,
  reportsAvailable: false,
  reportsCheckScheduled: false,

  populate: function () {
    this.set('content', FLOW.store.find(FLOW.Report));
  },

  reportsObserver: function () {
    var reports = this.get('content'), reportsQuery = null, self = this;
    if (reports && !reports.isUpdating) {
      this.set('reportsAvailable', reports.content.length > 0);

      if (!this.get('reportsCheckScheduled')) {
        var generatingReports = reports.filter(function(report) {
          return report.get('state') === "IN_PROGRESS" || report.get('state') === "QUEUED";
        });
        if (generatingReports.length > 0) {
          this.set('reportsCheckScheduled', true);
          reportsQuery = setInterval(function(){//if reports are still generating, wait 5s and then recheck
            var stillGeneratingReports = self.get('content').filter(function(report){
              return report.get('state') === "IN_PROGRESS" || report.get('state') === "QUEUED";
            });

            if (stillGeneratingReports.length > 0) {
              stillGeneratingReports.forEach(function(report){
                FLOW.store.findQuery(FLOW.Report, {keyId: report.get('keyId')});
              });
            } else {
              self.set('reportsCheckScheduled', false);
              clearInterval(reportsQuery);
            }
          }, 10000);
        } else {
          if (reportsQuery) {
            clearInterval(reportsQuery);
          }
        }
      }
    }
  }.observes('content', 'content.isUpdating')
});
