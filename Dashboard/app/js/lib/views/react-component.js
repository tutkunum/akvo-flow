FLOW.ReactComponentView = Ember.View.extend({

  // layout: '',
  tagName: 'div',
  template: Ember.Handlebars.compile(''),

  reactRender(reactComponent) {
    console.log(this);
    ReactDOM.render(reactComponent, this.element);
  },

  unmountReactElement() {
    ReactDOM.unmountComponentAtNode(this.element);
  },

  willDestroyElement() {
    this._super();
    this.unmountReactElement();
  }

});
