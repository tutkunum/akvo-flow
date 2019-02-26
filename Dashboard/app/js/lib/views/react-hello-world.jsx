require('akvo-flow/views/react-component');

let Greeter = ({name}) => <h2>Hello from {name}!!!</h2>;

FLOW.ReactHelloWorldView = FLOW.ReactComponentView.extend({
  didInsertElement() {
    this._super(...arguments);
    this.reactRender(<Greeter name="React"/>);
  }
});
