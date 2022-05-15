import React from 'react';
import { Button, Collapse, Nav, Navbar, NavbarToggler, NavbarBrand, NavItem, NavLink } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

function NavigationButton(props) {
    let navigate = useNavigate()

    function navigateToUrl() {
        if(typeof props.preNavFunc === "function") {
            props.preNavFunc();
        }
        navigate(props.url)
    }

    return (
        <NavItem>
            <Button color={props.color} onClick={navigateToUrl}>
                {props.title}
            </Button>
        </NavItem>
    );
}

function NavigationBar(props) {

    let navEnd;
    
    if (props.isLoggedIn) {
        navEnd = <NavigationButton title="Logout" color="primary" url="/" preNavFunc={() => props.handleUserLogout()} />
    } else {
        navEnd = (
            <React.Fragment>
                <NavigationButton title="Login" color="primary" url="/login" />
                <NavigationButton title="Sign Up" color="secondary" url="/signup" />
            </React.Fragment>
        );
    }

    return (
        <Navbar className="navbar" color="dark" dark expand="md">
            <NavbarBrand href="/">💰 Plundr</NavbarBrand>
            <NavbarToggler />
            <Collapse navbar>
                <Nav navbar>
                    <NavItem>
                        <NavLink>About</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="https://github.com/jchenuw">Author</NavLink>
                    </NavItem>
                </Nav>
            </Collapse>
            <Nav navbar className="gap-3">
                {navEnd}
            </Nav>
        </Navbar>
    );
}

export default NavigationBar;