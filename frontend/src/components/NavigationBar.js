import React, { Component } from 'react';
import {
    Button,
    Collapse,
    Nav,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    NavItem,
    NavLink,
} from 'reactstrap';
import { useNavigate } from 'react-router-dom';

function NavigationButton(props) {
    let navigate = useNavigate()

    function navigateToUrl() {
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

class NavigationBar extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Navbar className="navbar" color="dark" dark expand="md" fixed="top">
                <NavbarBrand href="/">Plundr</NavbarBrand>
                <NavbarToggler/>
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
                    <NavigationButton title="Login" color="primary" url="/login" />
                    <NavigationButton title="Sign Up" color="secondary" url="/signup" />
                </Nav>
            </Navbar>
        );
    }
}

export default NavigationBar;