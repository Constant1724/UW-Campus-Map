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

     generateListItems = () => {
        const result = [];
        const buildings = this.props.buildings;

        for (let i = 0; i < buildings.length; i++) {
            const building = buildings[i];
            result.push(
                <MenuItem value={building['shortName']} >
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
        this.canvas = null;
        this.image = null;
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
    //
    // update = (buildings) => {
    //     const result = [];
    //
    //     for (let i = 0; i < buildings.length; i++) {
    //         const building = buildings[i];
    //         result.push(
    //             <MenuItem value={building['shortName']} >
    //                 {building['shortName'] + ' - ' + building['longName']}
    //             </MenuItem>)
    //     }
    //
    //     return result;
    // };

    componentWillMount() {
        fetch('http://localhost:8080/listBuilding')
            .then(res => res.json())
            .then(json => this.setState({buildings: json}));

    }

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

    highLightBuilding = (shortName, buildings, ctx) => {
        const location = buildings.find(element => element['shortName'] === shortName)['location'];
        ctx.beginPath();
        ctx.arc(location['x'], location['y'], 22 ,0 , 2*Math.PI);
        ctx.fill();
    };

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

    getAndPrintPath = state => {
        fetch(`http://localhost:8080/findPath?start=${state.start}&end=${state.end}`)
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

            });
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
          {/*<FormControl className={'DropDownMenu'} required>*/}
              {/*<InputLabel >Start</InputLabel>*/}
              {/*<Select*/}
                  {/*value={this.state.start}*/}
                  {/*onChange={(event) => {*/}
                      {/*this.setState({start: event.target.value});*/}
                  {/*}}*/}
              {/*>*/}
                  {/*{this.update(this.state.buildings)}*/}

              {/*</Select>*/}
              {/*<FormHelperText>Select a Start Building</FormHelperText>*/}
          {/*</FormControl>*/}
          {/*<FormControl className={'DropDownMenu'} required>*/}
              {/*<InputLabel >End</InputLabel>*/}
              {/*<Select*/}
                  {/*value={this.state.end}*/}
                  {/*onChange={(event) => {this.setState({end: event.target.value})}}*/}
              {/*>*/}
                  {/*{this.update(this.state.buildings)}*/}

              {/*</Select>*/}
              {/*<FormHelperText>Select a End Building</FormHelperText>*/}
          {/*</FormControl>*/}

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
        {/*<canvas ref={this.setCanvas} width={'4330'} height={'2960'}  className={'Canvas'}/>*/}

      </div>
    );
  }
}



export default App;
