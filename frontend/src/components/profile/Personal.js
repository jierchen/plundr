import axios from 'axios';
import React, { useContext, useEffect, useRef, useState } from 'react';
import { Button, Col, Form, FormGroup, Input, Label, Row } from 'reactstrap';
import { CredentialsContext } from '../../App';

function Personal(props) {
    const [personalData, setPersonalData] = useState({});
    const oldPersonalData = useRef({});
    const {credentials, setCredentials} = useContext(CredentialsContext);

    useEffect(() => {
        axios.get("http://localhost:8080/api/profile", {
            headers: {
                "Accept": "application/json"
            },
            auth: credentials,
        })
        .then(res => {
            const currentData = res.data;
            setPersonalData((prevData) => ({
                ...prevData,
                ...currentData,
                password: credentials.password
            }));
            oldPersonalData.current = {...currentData, password: credentials.password};
        })
        .catch(err => {
            console.log("Error retrieving customer profile")
        });
    }, []);

    async function handleSave(event) {
        event.preventDefault();
        
        await axios.put("http://localhost:8080/api/profile", personalData, {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            auth: credentials
        })
        .then(res => {
            console.log(res);
            oldPersonalData.current = personalData;
        })
        .catch(err => {
            console.log("Error updating customer")
        });
    }

    function handleReset(event) {
        event.preventDefault();
        
        setPersonalData((prevData) => ({
            ...prevData,
            ...oldPersonalData.current,
        }));
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setPersonalData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    return (
        <Form onSubmit={handleSave} onReset={handleReset} id="signup-form">
            <Row>
                <Col md={5}>
                    <FormGroup>
                        <Label for="firstName">First Name</Label>
                        <Input type="text" name="firstName" id="firstName"
                            value={personalData.firstName} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
                <Col md={{ offset: 1, size: 6 }}>
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input type="text" name="username" id="username"
                            value={personalData.username} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
            </Row>

            <Row>
                <Col md={5}>
                    <FormGroup>
                        <Label for="lastName">Last Name</Label>
                        <Input type="text" name="lastName" id="lastName"
                            value={personalData.lastName} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
                <Col md={{ offset: 1, size: 6 }}>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="password" name="password" id="password"
                            value={personalData.password} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
            </Row>

            <FormGroup>
                <Label for="email">Email Address</Label>
                <Input type="email" name="email" id="email"
                    value={personalData.email} onChange={handleChange} autoComplete="false" />
            </FormGroup>

            <Row>
                <Col md={6}>
                    <FormGroup>
                        <Label for="dateOfBirth">Date Of Birth</Label>
                        <Input type="date" name="dateOfBirth" id="dateOfBirth"
                            value={personalData.dateOfBirth} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
                <Col md={6}>
                    <FormGroup>
                        <Label for="phoneNumber">Phone Number</Label>
                        <Input type="tel" name="phoneNumber" id="phoneNumber"
                            value={personalData.phoneNumber} onChange={handleChange} autoComplete="false" />
                    </FormGroup>
                </Col>
            </Row>

            <FormGroup>
                <Button color="primary" type="submit">Submit</Button>
                {' '}
                <Button color="secondary" type="reset">Cancel</Button>
            </FormGroup>
        </Form>
    );
}

export default Personal;