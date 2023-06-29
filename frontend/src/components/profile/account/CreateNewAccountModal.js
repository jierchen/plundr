import React, { useState } from 'react';
import { Button, Form, FormGroup, Input, Label, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function CreateNewAccountModal(props) {
    const [accountType, setAccountType] = useState("CHEQUING");
    const [name, setName] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();

        await props.createAccount({
            name: name,
            accountType: accountType,
        })

        // Reset values
        setAccountType("CHEQUING")
        setName("")

        props.closeModal();
    }

    return (
        <Modal isOpen={props.isModalOpen} toggle={props.toggleModal}>
            <Form id="create-account-form" onSubmit={handleSubmit}>
                <ModalHeader>
                    Create New Account
                </ModalHeader>
                <ModalBody>
                        <FormGroup>
                            <Label for="accountType">Account Type</Label>
                            <Input type="select" name="accountType" id="accountType" value={accountType} onChange={(event) => setAccountType(event.target.value)}>
                                <option>
                                    CHEQUING
                                </option>
                                <option>
                                    SAVINGS
                                </option>
                            </Input>
                        </FormGroup>
                        <FormGroup>
                            <Label for="name">Account Name</Label>
                            <Input type="text" name="name" id="name" value={name} onChange={(event) => setName(event.target.value)}/>
                        </FormGroup>
                </ModalBody>
                <ModalFooter>
                    <FormGroup>
                        <Button color="primary" type="submit">
                            + Create
                        </Button>
                        {' '}
                        <Button onClick={props.closeModal}>
                            Cancel
                        </Button>
                    </FormGroup>
                </ModalFooter>
            </Form>
        </Modal>
    );
}

export default CreateNewAccountModal;