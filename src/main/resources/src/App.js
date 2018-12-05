import React, { Component } from 'react';
import Button from '@material-ui/core/Button';

import './App.css';
import map from './campus_map.jpg';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';

// class Canvas extends Component {
//     constructor(props) {
//         super(props);
//         this.canvas = null;
//         this.setCanvas = canvas => {
//             this.canvas = canvas;
//         };
//
//     }
//
//     highLightSingleBuilding(x, y) {
//
//     }
//
//
//
//     render() {
//         const ctx = this.props.ctx;
//         return (
//         )
//     }
// }
class App extends Component {
    constructor(props) {
        super(props);
        this.canvas = null;
        this.setCanvas = canvas => {
            this.canvas = canvas;
        };
        this.state = {
            start: '',
            end: '',
            buildings: []
        };
    }

    update = (state) => {
        const buildings = state.buildings;
        const result = [];

        console.log(buildings.length)
        for (let i = 0; i < buildings.length; i++) {
            const building = buildings[i];
            result.push(
                <MenuItem value={building['shortName']} >
                    {building['shortName'] + ' - ' + building['longName']}
                </MenuItem>)
        }

        return result;
    }

    componentWillMount() {
        fetch('http://localhost:8080/listBuilding')
            .then(res => res.json())
            // .then(json => {
            //     const result = new Map();
            //     json.forEach(building => {
            //         result.set(building['shortName'], building);
            //     });
            //     this.setState({buildings: {result}});
            //     console.log(result);
            // });
            .then(json => this.setState({buildings: json}));


    }
        highLightBuilding = (shortName, buildings) => {
            console.log("Drawing" + shortName);
            const ctx = this.canvas.getContext('2d');
            const building = buildings.find(element => element['shortName'] === shortName);
            const x = parseFloat(building['location']['x']);
            const y = parseFloat(building['location']['y']);
            ctx.beginPath();
            ctx.arc(x, y, 50 ,0 , 2*Math.PI);
            ctx.fill();
        };

    drawBuildings = (state) =>{
        //TODO dynamically change to the size of canvas
        console.log('drawing')
        const ctx = this.canvas.getContext('2d');
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.highLightBuilding('SUZ', state.buildings, ctx);

        if (state.start) {
            this.highLightBuilding(state.start, state.buildings, ctx);
        }
        if (state.end) {
            this.highLightBuilding(state.end, state.buildings, ctx);
        }
    };
  render() {
    return (
      <div>
          <FormControl>
              <InputLabel >Start</InputLabel>
              <Select
                  value={this.state.start}
                  onChange={(event) => {
                      this.setState({start: event.target.value});
                      this.drawBuildings(this.state);
                  }}
              >
                  {this.update(this.state)}

              </Select>
              <FormHelperText>Select a Start Building</FormHelperText>
          </FormControl>

          <FormControl>
              <InputLabel >End</InputLabel>
              <Select
                  value={this.state.end}
                  onChange={(event) => {this.setState({end: event.target.value})}}
              >
                  {this.update(this.state)}

              </Select>
              <FormHelperText>Select a End Building</FormHelperText>
          </FormControl>

          <Button
              variant='contained'
              color='primary'
              // onClick={}
          >
              Submit
          </ Button>
          <div className='Image'>
              {/*<img src={map} alt='loading..'/>*/}
              <canvas ref={this.setCanvas} width={4330} height={2964} className={'Canvas'}/>

          </div>
      </div>
    );
  }
}



export default App;
