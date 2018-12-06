import React, { Component } from 'react';
import Button from '@material-ui/core/Button';

import './App.css';
import map from './campus_map.jpg';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';

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
        this.drawBuildings = this.drawBuildings.bind(this);
        this.getAndPrintPath = this.getAndPrintPath.bind(this);
        this.highLightBuilding = this.highLightBuilding.bind(this);
    }

    update = (buildings) => {
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
        highLightBuilding = (shortName, buildings, ctx) => {
            console.log("Drawing" + shortName);
            const building = buildings.find(element => element['shortName'] === shortName);
            const x = parseFloat(building['location']['x']);
            const y = parseFloat(building['location']['y']);
            ctx.beginPath();
            ctx.arc(x, y, 50 ,0 , 2*Math.PI);
            ctx.stroke();
        };

    drawBuildings = (state) =>{
        console.log('drawing');
        console.log(state);
        const ctx = this.canvas.getContext('2d');
        ctx.strokeStyle = "#FF0000";

        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        if (state.start) {
            this.highLightBuilding(state.start, state.buildings, ctx);
        }
        if (state.end) {
            this.highLightBuilding(state.end, state.buildings, ctx);
        }
    };

    getAndPrintPath = state => {
        fetch(`http://localhost:8080/findPath?start=${state.start}&end=${state.end}`)
            .then(res => res.json())
            .then(json => {
                if (!(json)) {
                    console.log(json);
                    return;
                }
                const ctx = this.canvas.getContext('2d');
                ctx.beginPath();
                const origin = json[0]['origin'];
                ctx.moveTo(parseFloat(origin['x']), parseFloat(origin['y']));
                json.forEach(element => {
                    const destination = element['destination'];
                    ctx.lineTo(destination['x'], destination['y']);
                })
                ctx.stroke();
            });
    }

  render() {
    return (
      <div>
              <FormControl className={'DropDownMenu'}>
                  <InputLabel >Start</InputLabel>
                  <Select
                      value={this.state.start}
                      onChange={(event) => {
                          this.setState({start: event.target.value});
                      }}
                  >
                      {this.update(this.state.buildings)}

                  </Select>
                  <FormHelperText>Select a Start Building</FormHelperText>
              </FormControl>

              <FormControl className={'DropDownMenu'}>
                  <InputLabel >End</InputLabel>
                  <Select
                      value={this.state.end}
                      onChange={(event) => {this.setState({end: event.target.value})}}
                  >
                      {this.update(this.state.buildings)}

                  </Select>
                  <FormHelperText>Select a End Building</FormHelperText>
              </FormControl>


              <Button
                  variant='contained'
                  color='primary'
                  onClick={ () => {
                      (state => {
                          this.drawBuildings(state);
                          this.getAndPrintPath(state);
                      }) (this.state);
                  }}
                  className={'Button'}
              >
                  Submit
              </ Button>

              <Button
                  variant='contained'
                  color='primary'
                  onClick={() => {this.canvas.getContext('2d').clearRect(0, 0, this.canvas.width, this.canvas.height)}}
                  className={'Button'}
              >
                  Reset
              </ Button>

          <div>
            <canvas ref={this.setCanvas} width={'4330'} height={'2960'}  className={'Canvas'}/>
          </div>

      </div>
    );
  }
}



export default App;
