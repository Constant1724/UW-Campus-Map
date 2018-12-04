import React, { Component } from 'react';
import './App.css';
import map from './campus_map.jpg';


class App extends Component {
  render() {
    return (
      <div>
          Update this file.
          <div className="Image">
              <img src={map} alt="loading.." height="50%" width="50%"/>
          </div>
      </div>
    );
  }
}

export default App;
