import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import { CredentialsContext } from '../../App';
import CustomPagination from '../custom/CustomPagination';
import AddNewContactModal from './contacts/AddNewContactModal';

function Contacts(props) {
    const [contacts, setContacts] = useState([]);
    const [totalPages, setTotalPages] = useState(-1);
    const [currentPage, setCurrentPage] = useState(0);
    const [isAddContactModalOpen, setIsAddContactModalOpen] = useState(false);
    const {credentials} = useContext(CredentialsContext);
    
    useEffect(() => {
        retrieveContactPage();
    }, [currentPage]);

    function toggleAddContactModal() {
        setIsAddContactModalOpen(!isAddContactModalOpen);
    }

    async function retrieveContactPage() {
        await axios.get(`http://localhost:8080/api/contacts?page=${currentPage}&size=5`, {
            headers: {
                "Accept": "application/json"
            },
            auth: credentials
        })
        .then(res => {
            const contactPage = res.data;
            setContacts(contactPage.content);
            setTotalPages(contactPage.totalPages);
        }).catch(err => {
            console.log("Error retrieving contacts")
        });
    }

    async function deleteContact(contactId) {
        await axios.delete("http://localhost:8080/api/contact/" + contactId, {
            auth: credentials
        })
        .then(async (res) => {
            if(contacts.length === 1 && currentPage !== 0) {
                setCurrentPage(currentPage - 1);
            } else {
                await retrieveContactPage();
            }
        })
        .catch(err => {
            console.log("Error delete contact");
        });
    }

    async function addContact(contactEmail) {
        await axios.post("http://localhost:8080/api/contact", {contactEmail: contactEmail}, {
            headers: {
                "Content-Type": "application/json"
            },
            auth: credentials
        })
        .then(async (res) => {
            await retrieveContactPage();
        })
        .catch(err => {
            console.log("Error adding new contact")
        })
    }

    const contactsRows = contacts ? contacts.map(contact => {
        return (
            <tr key={contact.id}>
                <td>{contact.firstName + " " + contact.lastName}</td>
                <td>{contact.email}</td>
                <td>{contact.phoneNumber}</td>
                <td>
                    {props.isSelectModal && 
                    <React.Fragment>
                        <Button color="primary" onClick={() => props.handleSelectRecipient(contact)}>
                            Select
                        </Button>
                        {' '}
                    </React.Fragment>
                    }
                    <Button color="danger" onClick={async () => await deleteContact(contact.id)}>
                        Delete
                    </Button>
                </td>
            </tr>
        );
    }) : <React.Fragment></React.Fragment>

    return (
        <React.Fragment>
            <div>
                <h1>Contacts</h1>

                <div>
                    <Button color="primary" onClick={() => setIsAddContactModalOpen(true)}>
                        + Add New Contact

                        <AddNewContactModal 
                            isModalOpen={isAddContactModalOpen}
                            toggleModal={toggleAddContactModal} 
                            closeModal={() => setIsAddContactModalOpen(false)} 
                            addContact={addContact} />
                    </Button>
                </div>
            </div>
            <div>
                <Table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Phone Number</th>
                            <th>Options</th>
                        </tr>
                    </thead>
                    <tbody>
                        {contactsRows}
                    </tbody>
                </Table>
                <CustomPagination
                    totalPages={totalPages}
                    currentPage={currentPage}
                    switchPage={(pageNum) => setCurrentPage(pageNum - 1)} />
            </div>
        </React.Fragment>
    );
}

export default Contacts;