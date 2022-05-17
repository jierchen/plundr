import React, { useState } from 'react';
import { Button, Form, FormGroup, Input, Label, Modal, ModalHeader, ModalBody, ModalFooter} from 'reactstrap';

function AddNewContactModal(props) {
    const [contactEmail, setContactEmail] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();

        await props.addContact(contactEmail);

        props.closeModal();
    }

    return (
        <Modal isOpen={props.isModalOpen} toggle={props.toggleModal}>
            <Form id="add-contact-form" onSubmit={handleSubmit}>
                <ModalHeader>
                    Add New Contact
                </ModalHeader>
                <ModalBody>
                    <FormGroup row>
                        <Label for="contactEmail">
                            Email
                        </Label>
                        <Input type="text" name="contactEmail" id="contactEmail" 
                            onChange={(event) => setContactEmail(event.target.value)} />
                    </FormGroup>
                </ModalBody>
                <ModalFooter>
                    <FormGroup>
                        <Button color="primary" type="submit">
                            + Add
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

export default AddNewContactModal;