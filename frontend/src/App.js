import './App.css';
import React, {Component} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import NavigationBar from './components/NavigationBar';

class App extends Component {
  render() {
    return(
      <BrowserRouter>
        <Routes>
        </Routes>
      </BrowserRouter>
    )
  }
}

export default App;
