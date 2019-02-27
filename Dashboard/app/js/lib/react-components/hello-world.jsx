// // class HelloWorld extends React.Component {
// //   // constructor( props ){
// //   //   super( props );
// //   //   this.state = { name: this.props.name || 'World' };
// //   //   this.handleChangeName = this.handleChangeName.bind(this);
// //   // }

// //   // handleChangeName = async () => {
// //   //   let resp = await fetch('/foo');
// //   //   resp = await resp.json();
// //   //   this.setState({ name: resp.name });
// //   // }

// //   render(){
// //     return (
// //       <h1>Hello React</h1>
// //     );
// //     // <div onClick={this.handleChangeName}>
// //     //   <h1>Hello {this.state.name}!</h1>
// //     // </div>
// //   } 
// // }

// // HelloWorld.propTypes = {
// //   name: React.PropTypes.string.isRequired,
// // };
// console.log('define comp');

// const HelloWorld = ({ name }) => {
//   console.log('---');
//   // console.log('---', React);
//   return 'blah  ';
// };
// // const HelloWorld = ({ name }) => <h2>Hello from {name}!!!</h2>;

FLOW.HelloWorldComponent = ({ name }) => name;
