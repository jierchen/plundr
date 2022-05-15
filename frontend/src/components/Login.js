import axios from 'axios';
import React, { useState } from 'react';
import { Card, Button, Form, FormGroup, Input, Label, CardBody, CardHeader } from 'reactstrap';
import './Login.css';
import { useNavigate } from 'react-router-dom';

function Login(props) {
    const [loginData, setLoginData] = useState({
        username: "",
        password: "",
    });
    const navigate = useNavigate();

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setLoginData((loginState) => ({
            ...loginState,
            [name]: value,
        }));
    }

    async function handleLogin(event) {
        event.preventDefault();
        axios.post("http://localhost:8080/customerLogin", loginData, {
            headers: {
                "Content-Type": "application/json"
            }
        }).then(res => {
            props.handleUserLogin();
            navigate("/profile")
        }).catch(err => {
            console.log("Login failed")
        })
    }

    return (
        <Card>
            <CardHeader>Please Log In</CardHeader>
            <CardBody>
                <Form onSubmit={handleLogin} id="login-form">
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input type="text" name="username" id="username"
                            onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="password" name="password" id="password"
                            onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Submit</Button>
                    </FormGroup>
                </Form>
            </CardBody>
        </Card>
    );
}

export default Login;