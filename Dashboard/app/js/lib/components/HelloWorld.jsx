var propTypes = {
  name: React.PropTypes.string.isRequired,
};

export default class HelloWorld extends React.Component {
  // constructor( props ){
  //   super( props );
  //   this.state = { name: this.props.name || 'World' };
  //   this.handleChangeName = this.handleChangeName.bind(this);
  // }

  // handleChangeName = async () => {
  //   let resp = await fetch('/foo');
  //   resp = await resp.json();
  //   this.setState({ name: resp.name });
  // }

  render(){
    return (
      <h1>Hello React</h1>
    );
    // <div onClick={this.handleChangeName}>
    //   <h1>Hello {this.state.name}!</h1>
    // </div>
  }
}

HelloWorld.propTypes = propTypes;