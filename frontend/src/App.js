import './App.css';
import React, { useRef, useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import NavigationBar from './components/NavigationBar';
import Landing from './components/Landing';
import Login from './components/Login';
import SignUp from './components/SignUp';
import Profile from './components/profile/Profile';

export const CredentialsContext = React.createContext({});

function App() {
	const [isLoggedIn, setIsLoggedIn] = useState(false);
	const credentials = useRef({
		username: "",
		password: "",
	});

	function setCredentials(username, password) {
		credentials.current = {
			username: username,
			password: password
		}
	}

	function handleUserLogout() {
		credentials.current = null;
		setIsLoggedIn(false);
	}

	function handleUserLogin(username, password) {
		setCredentials(username, password);
		setIsLoggedIn(true);
	}

	return (
		<BrowserRouter>
			<div className="app">
				<NavigationBar isLoggedIn={isLoggedIn} handleUserLogout={handleUserLogout} />

				<Routes>
					<Route path="/" element={<Landing />} />
					<Route path="/login" element={<Login handleUserLogin={handleUserLogin} />} />
					<Route path="/signup" element={<SignUp />} />
					<Route path="/profile" element={
						<CredentialsContext.Provider value={{
							credentials: credentials.current,
							setCredentials: setCredentials
						}}>
							<Profile isLoggedIn={isLoggedIn}/>
						</CredentialsContext.Provider>} 
					/>
				</Routes>
			</div>
		</BrowserRouter>
	);
}

export default App;
