import React, { Component } from 'react';
import Button from '@material-ui/core/Button';

import './App.css';
import map from './campus_map.jpg';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';

class DropDownMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
        }
    }
    // Generate buildings into an array of MenuItem.
     generateListItems = () => {
        const result = [];
        const buildings = this.props.buildings;

        for (let i = 0; i < buildings.length; i++) {
            const building = buildings[i];
            result.push(
                <MenuItem value={building['shortName']}
                          key={building['shortName'] + this.props.label}
                >
                    {building['shortName'] + ' - ' + building['longName']}
                </MenuItem>)
        }
        return result;
    };


    render() {
        return (
            <FormControl required>
                <InputLabel>{this.props.label}</InputLabel>
                <Select
                    value={this.state.name}
                    onChange={ (event) => {
                        this.setState({name: event.target.value});
                        this.props.callback(event.target.value);
                    }}
                >

                    {this.generateListItems()}

                </Select>
                <FormHelperText>{this.props.helperText}</FormHelperText>
            </FormControl>
        );
    }
}

class App extends Component {

    constructor(props) {
        super(props);
        // this.canvas is the canvas we will drawing on
        this.canvas = null;
        // the campus map image.
        this.image = null;

        this.setCanvas = canvas => {
            this.canvas = canvas;
        };


        this.state = {
            start: '',
            end: '',
            buildings: [],
            disableSubmit: false,
            disableReset: false
        };

        this.drawBuildings = this.drawBuildings.bind(this);
        this.getAndPrintPath = this.getAndPrintPath.bind(this);
        this.highLightBuilding = this.highLightBuilding.bind(this);
    }

    // Before rendering, make the initial request to get a list of buildings.
    componentWillMount() {
        fetch('http://localhost:8080/listBuilding')
            .then(res => res.json())
            .then(json => this.setState({buildings: json}));

    }

    // After rendering, we may load the image and draw it to the canvas
    componentDidMount() {
        const image = new Image();
        image.src = map;
        this.image = image;
        image.onload = () => {
            this.canvas.width = this.image.naturalWidth;
            this.canvas.height = this.image.naturalHeight;
            this.canvas.getContext('2d').drawImage(this.image, 0, 0);
        };
    }

    // Simply put a dot on the location of the building.
    highLightBuilding = (shortName, buildings, ctx) => {
        const location = buildings.find(element => element['shortName'] === shortName)['location'];
        ctx.beginPath();
        ctx.arc(location['x'], location['y'], 22 ,0 , 2*Math.PI);
        ctx.fill();
    };

    // Draw two buildings on the campus map.
    drawBuildings = (state) =>{
        const ctx = this.canvas.getContext('2d');
        ctx.fillStyle = "#FF0000";

        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        ctx.drawImage(this.image, 0, 0);
        if (state.start) {
            this.highLightBuilding(state.start, state.buildings, ctx);
        }
        if (state.end) {
            this.highLightBuilding(state.end, state.buildings, ctx);
        }
    };

    // Make request to find path between two buildings and print them out.
    getAndPrintPath = state => {
        this.setState({disableSubmit: true});
        // fetch(`http://localhost:8080/findPath?start=${state.start}&end=${state.end}`)
        fetch(`http://localhost:8080/findPath?start=aaa&end=${state.end}`)
            .then(res => {
                if (!res.ok) {
                    throw res;
                }
                return res;
            })
            .then(res => res.json())
            .then(json => {
                if (json.length !== 0) {
                    const ctx = this.canvas.getContext('2d');
                    ctx.beginPath();
                    ctx.lineWidth = '10';
                    ctx.lineJoin = 'round';
                    ctx.strokeStyle = '#FF0000';
                    const origin = json[0]['origin'];
                    ctx.moveTo(origin['x'], origin['y']);
                    json.forEach(element => {
                        const destination = element['destination'];
                        ctx.lineTo(destination['x'], destination['y']);
                    });
                    ctx.stroke();
                }

            })
            .catch(err => {
                if (err.code === 500) {

                    // alert("Local website is corrupted please refresh");
                    console.log(err);
            }})
            .finally(() => this.setState({disableSubmit: false}));
    };

  render() {
    return (
      <div>

          <DropDownMenu buildings={((state) => state.buildings) (this.state)}
                        label={'Start'} helperText={'Select a Start Building'}
                        callback={(value) => this.setState({start: value})}/>

          <DropDownMenu buildings={((state) => state.buildings) (this.state)}
                        label={'End'} helperText={'Select an End Building'}
                        callback={(value) => this.setState({end: value})}/>

              <div>
                  <Button
                      variant='contained'
                      color='primary'
                      onClick={ () => {
                          (state => {
                              if (!state.start || !state.end) {
                                  alert("Please Select Building");
                              } else {
                                  this.drawBuildings(state);
                                  this.getAndPrintPath(state);
                              }

                          }) (this.state);
                      }}
                      className={'Button'}
                      disabled={this.state.disableSubmit}
                  >
                      Find Path
                  </ Button>

                  <Button
                      variant='contained'
                      color='primary'
                      onClick={() => {
                          const ctx = this.canvas.getContext('2d');
                          ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
                          ctx.drawImage(this.image, 0, 0);
                      }}
                      className={'Button'}
                  >
                      Reset
                  </ Button>
          </div>

        <div>
            <canvas ref={this.setCanvas}/>
        </div>

      </div>
    );
  }
}



export default App;
