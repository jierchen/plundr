import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { Button, Card, CardBody, CardText, Col, Container, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, Row } from 'reactstrap';
import { CredentialsContext } from '../../App';
import { BsCheckCircle } from 'react-icons/bs';
import Contacts from './Contacts';
import './Funds.css';

function Funds(props) {
    const [fundType, setFundType] = useState("deposit");
    const [accounts, setAccounts] = useState([]);
    const [fundAccountId, setFundAccountId] = useState(null);
    const [internalTransferAccountId, setInternalTransferAccountId] = useState(null);
    const [recipient, setRecipient] = useState(null);
    const [transactionData, setTransactionData] = useState({
        amount: "0.00",
        description: "",
    });
    const [isSelectContactModalOpen, setIsSelectContactModalOpen] = useState(false);
    const [fundSuccess, setFundSuccess] = useState(false);
    const {credentials} = useContext(CredentialsContext);

    useEffect(() => {
        retrieveAccounts();
    }, [])

    function toggleSelectContactModal() {
        setIsSelectContactModalOpen(!isSelectContactModalOpen);
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        setTransactionData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    function resetFundsPage() {
        setFundType("deposit");
        setFundAccountId(null);
        setInternalTransferAccountId(null);
        setRecipient(null);
        setTransactionData({
            amount: "0.00",
            description: ""
        });
        setFundSuccess(false);
        retrieveAccounts();
    }

    async function retrieveAccounts() {
        await axios.get("http://localhost:8080/api/accounts", {
            headers: {
                "Accept": "application/json"
            },
            auth: credentials
        })
        .then(res => {
            let accountData = res.data;
            console.log(accountData);
            setAccounts(accountData);
            
            if(accountData.length > 0) {
                setFundAccountId(accountData[0].id);
            }
        })
        .catch(err => {
            console.log("Error retrieving customer bank accounts")
        })
    }

    async function submitFunds() {
        let requestBody = {...transactionData};
        if(fundType === "internalTransfer") {
            requestBody.recipientAccountId = internalTransferAccountId;
        } else if (fundType === "externalTransfer") {
            requestBody.recipientId = recipient.id;
        }
        console.log("Request Body:");
        console.log(requestBody);

        await axios.post(`http://localhost:8080/api/account/${fundAccountId}/${fundType}`, requestBody, {
            headers: {
                "Content-Type": "application/json",
            },
            auth: credentials
        })
        .then(res => {
            setFundSuccess(true);
        })
        .catch(err => {
            console.log("Error submitting funds");
        })
    }

    let fundComponent = null;

    if(!fundSuccess) {
        fundComponent = (
            <React.Fragment>
                <Row>
                    <Col>
                        <Card>
                            <CardBody>
                                <Label for="fundType">Fund Type</Label>
                                <Input type="select" name="fundType" id="fundType" onChange={(event) => setFundType(event.target.value)}>
                                    <option value="deposit">
                                        Deposit
                                    </option>
                                    <option value="withdraw">
                                        Withdraw
                                    </option>
                                    <option value="internalTransfer">
                                        Internal Transfer
                                    </option>
                                    <option value="externalTransfer">
                                        External Transfer
                                    </option>
                                </Input>
                            </CardBody>
                        </Card>
                    </Col>
                    <Col>
                        <Card>
                            <CardBody>
                                <Label for="fundAccount">Account</Label>
                                <Input type="select" name="fundAccount" id="fundAccount" onChange={(event) => {
                                    setFundAccountId(parseInt(event.target.value))
                                    if(parseInt(event.target.value) == internalTransferAccountId) {
                                        setInternalTransferAccountId(null);
                                    }
                                }}>
                                    {accounts && accounts.map((account) => {
                                        return (
                                            <option key={account.id} value={account.id}>
                                                {account.name + " - $" + parseFloat(account.balance).toFixed(2)}
                                            </option>
                                        );
                                    })}
                                </Input>
                            </CardBody>
                        </Card>
                    </Col>
                    {(fundType === "internalTransfer" || fundType === "externalTransfer") &&
                        <Col>
                            <Card>
                                <CardBody>
                                    {fundType === "internalTransfer" &&
                                        <React.Fragment>
                                            <Label for="internalTransferAccount">Internal Account</Label>
                                            <Input type="select" name="internalTransferAccount" id="internalTransferAccount"
                                                onChange={(event) => setInternalTransferAccountId(parseInt(event.target.value))}>
                                                <option selected={internalTransferAccountId === null} disabled hidden>
                                                    Choose An Account...
                                                </option>
                                                {accounts.map((account) => {
                                                    return (
                                                        <option disabled={fundAccountId == account.id} key={account.id} value={account.id}>
                                                            {account.name + " - $" + parseFloat(account.balance).toFixed(2)}
                                                        </option>
                                                    );
                                                })}
                                            </Input>
                                        </React.Fragment>
                                    }
                                    {fundType === "externalTransfer" &&
                                        <React.Fragment>
                                            <Label for="externalTransferAccount">Contact</Label>
                                            <Input type="text" name="externalTransferAccount" id="externalTransferAccount" disabled
                                                value={recipient !== null
                                                    ? recipient.firstName + " " + recipient.lastName + " (" + recipient.email + ")"
                                                    : "No Contact Selected"} />

                                            <Button className="mt-3" color="primary" onClick={() => setIsSelectContactModalOpen(true)}>
                                                Search Contact

                                                <Modal size="lg" isOpen={isSelectContactModalOpen} toggle={toggleSelectContactModal}>
                                                    <ModalBody>
                                                        <Contacts isSelectModal={true} handleSelectRecipient={(recipient) => {
                                                            setRecipient(recipient);
                                                            setIsSelectContactModalOpen(false);
                                                        }} />
                                                    </ModalBody>
                                                    <ModalFooter>
                                                        <Button color="danger" onClick={() => setIsSelectContactModalOpen(false)}>
                                                            Cancel
                                                        </Button>
                                                    </ModalFooter>
                                                </Modal>
                                            </Button>
                                        </React.Fragment>
                                    }
                                </CardBody>
                            </Card>
                        </Col>
                    }
                    <Col>
                        <Card>
                            <CardBody>
                                <FormGroup>
                                    <Label for="amount">Amount</Label>
                                    <Input type="text" name="amount" id="amount"
                                        onChange={handleChange} />
                                </FormGroup>
                                <FormGroup>
                                    <Label for="description">Description</Label>
                                    <Input type="text" name="description" id="description"
                                        onChange={handleChange} />
                                </FormGroup>
                            </CardBody>
                        </Card>
                    </Col>
                </Row >
                <Row className="justify-content-center mt-5">
                    <Col sm="1">
                        <Button color="success" onClick={submitFunds}>
                            Submit
                        </Button>
                    </Col>
                </Row>
            </React.Fragment>
        );
    } else {
        fundComponent = (
            <Row className="justify-content-center">
                <Card id="funds-success-card" body color="light" className="text-center">
                    <CardText>
                        <BsCheckCircle />
                    </CardText>
                    <CardText>
                        Transaction Successful
                    </CardText>
                    <Button color="success" onClick={resetFundsPage}>
                        Continue
                    </Button>
                </Card>
            </Row>
        );
    }

    return (
        <Container>
            {fundComponent}
        </Container>
    );
}

export default Funds;