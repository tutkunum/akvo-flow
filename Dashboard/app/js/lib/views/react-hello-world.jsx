require('akvo-flow/views/react-component');
// require('akvo-flow/react-components/hello-world');
// @charles - if you comment out the above ^ it throws errors

let Greeter = ({name}) => <h2>Hello from {name}!!!</h2>;

FLOW.ReactHelloWorldView = FLOW.ReactComponentView.extend({
  didInsertElement() {
    this._super(...arguments);
    this.reactRender(
      <Greeter name="React"/>
    );
  }
});
