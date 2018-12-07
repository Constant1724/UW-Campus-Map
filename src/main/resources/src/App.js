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
                    value={this.props.currentDisplayValue}
                    onChange={ (event) => {
                        this.props.onChange(event.target.value);
                    }}
                >

                    {this.generateListItems()}

                </Select>
                <FormHelperText>{this.props.helperText}</FormHelperText>
            </FormControl>
        );
    }
}

class MyCanvas extends Component {

    constructor(props) {
        super(props);
        // this.canvas is the canvas we will drawing on
        this.canvas = null;
        this.image = null;

    }

    // after Dom has rendered, set the canvas and load image.
    componentDidMount() {
        this.canvas = this.refs.canvas;
        this.image = this.props.loadImage();
        this.image.onload = () => {
            this.canvas.width = this.image.naturalWidth;
            this.canvas.height = this.image.naturalHeight;
            this.canvas.getContext('2d').drawImage(this.image, 0, 0);
        };
    }

    // After props and state has been update, draw corresponding items.
    componentDidUpdate(prevProps, prevState, snapshot) {
        this.resetCanvas();
        this.drawBuildings(this.props.start, this.props.end, this.props.buildings);
        this.drawPaths(this.props.paths);

    }

    // Simply put a dot on the location of the building.
    highLightBuilding = (shortName, buildings, ctx) => {
        const location = buildings.find(element => element['shortName'] === shortName)['location'];
        ctx.beginPath();
        ctx.arc(location['x'], location['y'], 22 ,0 , 2*Math.PI);
        ctx.fill();
    };

    // Remove all inks on the canvas
    resetCanvas = () => {
        const ctx = this.canvas.getContext('2d');
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        ctx.drawImage(this.image, 0, 0);
    };

    // Draw two buildings on the campus map.
    drawBuildings = (start, end, buildings) =>{
        const ctx = this.canvas.getContext('2d');
        ctx.fillStyle = "#FF0000";

        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        ctx.drawImage(this.image, 0, 0);
        if (start) {
            this.highLightBuilding(start, buildings, ctx);
        }
        if (end) {
            this.highLightBuilding(end, buildings, ctx);
        }
    };

    // draw paths
    drawPaths= (paths) => {
        if (paths.length === 0) {
            return
        }
        const ctx = this.canvas.getContext('2d');
        ctx.beginPath();
        ctx.lineWidth = '10';
        ctx.lineJoin = 'round';
        ctx.strokeStyle = '#FF0000';
        const origin = paths[0]['origin'];
        ctx.moveTo(origin['x'], origin['y']);
        paths.forEach(element => {
            const destination = element['destination'];
            ctx.lineTo(destination['x'], destination['y']);
        });
        ctx.stroke();

    };

    render() {
        return (
            <canvas ref={"canvas"}/>
        )
    }
}

class SubmitButton extends Component {
    render() {
        return (
            <Button
                variant='contained'
                color='primary'
                onClick={ () => {
                    if (!this.props.start || !this.props.end) {
                        alert("Please Select Building");
                    } else {
                        this.props.getPath(this.props.start, this.props.end);
                    }
                }}
                className={'Button'}
                disabled={this.props.disableSubmit}
            >
                Find Path
            </ Button>
        );
    }
}

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            start: '',
            end: '',
            buildings: [],
            paths: [],
            isHidden: false,
            disableSubmit: false,
        };

        this.getPath = this.getPath.bind(this);
    }

    // Before rendering, make the initial request to get a list of buildings.
    componentWillMount() {
        fetch('http://localhost:8080/listBuilding')
            .then(res => res.json())
            .then(json => this.setState({buildings: json}));

    }

    // the function to load the background image.
    loadImage = () => {
        const image = new Image();
        image.src = map;
        image.onerror = () => {
            // if image is absent, hide the whole thing.
            this.setState({isHidden: true});
            alert("Image does not found due to server error. \n" +
                "All functionality will not be available. \n" +
                "Sorry for your inconvenience. \n")
        };
        return image;
    };

    // Make request to find path between two buildings and print them out.
    getPath = (start, end) => {
        this.setState({disableSubmit: true});
        fetch(`http://localhost:8080/findPath?start=${start}&end=${end}`)
            .then(res => {
                if (!res.ok) {
                    throw res;
                }
                return res;
            })
            .then(res => res.json())
            .then(json => { this.setState({paths: json}) })
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
                {!this.state.isHidden &&
                <div>

                    <DropDownMenu buildings={((state) => state.buildings)(this.state)}
                                  label={'Start'} helperText={'Select a Start Building'}
                                  currentDisplayValue={this.state.start}
                                  onChange={(value) => this.setState({start: value, paths: []})}/>

                    <DropDownMenu buildings={((state) => state.buildings)(this.state)}
                                  label={'End'} helperText={'Select an End Building'}
                                  currentDisplayValue={this.state.end}
                                  onChange={(value) => this.setState({end: value, paths: []})}/>

                    <div>
                        <SubmitButton start={this.state.start}
                                      end={this.state.end}
                                      getPath={this.getPath}
                                      disableSubmit={this.state.disableSubmit}
                        />

                        <Button
                            variant='contained'
                            color='primary'
                            onClick={() => {
                                this.setState({start: '', end: '', paths: []})
                            }}
                            className={'Button'}
                        >
                            Reset
                        </ Button>
                    </div>

                    <div>

                        <MyCanvas start={this.state.start} end={this.state.end}
                                  buildings={this.state.buildings}
                                  paths={this.state.paths} loadImage={this.loadImage}
                                  canvas={this.canvas}
                        />
                    </div>


                </div>
                }

                {this.state.isHidden &&
                <div>
                    <p>Image does not found due to server error.</p>
                    <p>All functionality will be unavailable </p>
                    <p>Sorry for your inconvenience</p>
                </div>
                }
            </div>


    );
  }
}



export default App;
