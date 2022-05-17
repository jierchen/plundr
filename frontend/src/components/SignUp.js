import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card, CardText, Col, Form, FormGroup, Input, Label, Row } from 'reactstrap';
import { BsCheckCircle } from 'react-icons/bs';
import './SignUp.css';

function SignUp(props) {
    const [signUpData, setSignUpData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        username: "",
        password: "",
        phoneNumber: "",
        dateOfBirth: "",
    });
    const[signUpSuccess, setSignUpSuccess] = useState(false);
    
    let navigate = useNavigate();

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setSignUpData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    async function handleSignUp(event) {
        event.preventDefault();

        await axios.post("http://localhost:8080/signup", signUpData, {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        }).then(res => {
            setSignUpSuccess(true);
        }).catch(err => {
            console.log("Error signing up")
        });
    }

    let signUpPage = null;

    if (signUpSuccess === true) {
        signUpPage = (
            <Card id="signup-success-card" body color="light" className="text-center">
                <CardText>
                    <BsCheckCircle />
                </CardText>
                <CardText>
                    Sign Up Successful!
                </CardText>
                <Button color="success" onClick={() => navigate("/login")}>
                    Login
                </Button>
            </Card>
        );
    } else {
        signUpPage = (
            <Form onSubmit={handleSignUp} id="signup-form">
                <Row>
                    <Col md={5}>
                        <FormGroup>
                            <Label for="firstName">First Name</Label>
                            <Input type="text" name="firstName" id="firstName"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                    <Col md={{ offset: 1, size: 6 }}>
                        <FormGroup>
                            <Label for="username">Username</Label>
                            <Input type="text" name="username" id="username"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                </Row>

                <Row>
                    <Col md={5}>
                        <FormGroup>
                            <Label for="lastName">Last Name</Label>
                            <Input type="text" name="lastName" id="lastName"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                    <Col md={{ offset: 1, size: 6 }}>
                        <FormGroup>
                            <Label for="password">Password</Label>
                            <Input type="password" name="password" id="password"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                </Row>

                <FormGroup>
                    <Label for="email">Email Address</Label>
                    <Input type="email" name="email" id="email"
                        onChange={handleChange} autoComplete="false" />
                </FormGroup>

                <Row>
                    <Col md={6}>
                        <FormGroup>
                            <Label for="dateOfBirth">Date Of Birth</Label>
                            <Input type="date" name="dateOfBirth" id="dateOfBirth"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                    <Col md={6}>
                        <FormGroup>
                            <Label for="phoneNumber">Phone Number</Label>
                            <Input type="tel" name="phoneNumber" id="phoneNumber"
                                onChange={handleChange} autoComplete="false" />
                        </FormGroup>
                    </Col>
                </Row>

                <FormGroup>
                    <Button color="primary" type="submit">Submit</Button>
                </FormGroup>
            </Form>
        );
    }

    return (
        <React.Fragment>
            {signUpPage}
        </React.Fragment>
    );
}

export default SignUp;