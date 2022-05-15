import './App.css';
import React, { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import NavigationBar from './components/NavigationBar';
import Landing from './components/Landing';
import Login from './components/Login';
import SignUp from './components/SignUp';
import Profile from './components/profile/Profile';

function App() {
	const [isLoggedIn, setIsLoggedIn] = useState(false);

	return (
		<BrowserRouter>
			<div className="app">
				<NavigationBar isLoggedIn={isLoggedIn} handleUserLogout={() => setIsLoggedIn(false)} />

				<Routes>
					<Route path="/" element={<Landing />} />
					<Route path="/login" element={<Login handleUserLogin={() => setIsLoggedIn(true)} />} />
					<Route path="/signup" element={<SignUp />} />
					<Route path="/profile" element={<Profile isLoggedIn={isLoggedIn}/>} />
				</Routes>
			</div>
		</BrowserRouter>
	);
}

export default App;
