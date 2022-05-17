import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, Table } from 'reactstrap';
import { CredentialsContext } from '../../App';
import CreateNewAccountModal from './account/CreateNewAccountModal';
import Transactions from './account/Transactions';

const MAX_ACCOUNTS = 5;

function Accounts(props) {
    const [accounts, setAccounts] = useState([]);
    const [isAddAccountModalOpen, setIsAddAccountModalOpen] = useState(false);
    const [isTransactionsModalOpen, setIsTransactionsModalOpen] = useState(false);
    const {credentials} = useContext(CredentialsContext);

    useEffect(() => {
        retrieveAccounts();
    }, []);

    async function retrieveAccounts() {
        await axios.get("http://localhost:8080/api/accounts", {
            headers: {
                "Accept": "application/json"
            },
            auth: credentials
        })
        .then(res => {
            setAccounts(res.data)
        })
        .catch(err => {
            console.log("Error retrieving customer bank accounts")
        })
    }

    function toggleAddAccountModal() {
        setIsAddAccountModalOpen(!isAddAccountModalOpen);
    }

    function toggleTransactionModal() {
        setIsTransactionsModalOpen(!isTransactionsModalOpen);
    }

    async function removeAccount(removeId) {
        await axios.delete("http://localhost:8080/api/account/" + removeId, {
            auth: credentials
        })
        .then(async (res) => {
            await retrieveAccounts();
        })
        .catch(err => {
            console.log("Error deleting account")
        });
    }

    async function createAccount(newAccount) {
        await axios.post("http://localhost:8080/api/account", newAccount, {
            auth: credentials,
        })
        .then(async (res) => {
            await retrieveAccounts();
        })
        .catch(err => {
            console.log("Error creating account")
        })
    }

    const accountsRows = accounts ? accounts.map(account => {
        return (
            <tr key={account.id}>
                <td>{account.id}</td>
                <td>{account.name}</td>
                <td>{account.accountType}</td>
                <td>{parseFloat(account.balance).toFixed(2)}</td>
                <td>
                    <Button color="info" onClick={() => setIsTransactionsModalOpen(true)}>
                        Check Transactions
                        
                        <Modal size="lg" isOpen={isTransactionsModalOpen} toggle={toggleTransactionModal}>
                            <ModalBody>
                                <Transactions currentAccountId={account.id} />
                            </ModalBody>
                            <ModalFooter>
                                <Button color="danger" onClick={() => setIsTransactionsModalOpen(false)}>
                                    Cancel
                                </Button>
                            </ModalFooter>
                        </Modal>
                    </Button>
                    {' '}
                    <Button color="danger" onClick={async () => await removeAccount(account.id)}>
                        Delete
                    </Button>
                </td>
            </tr>
        );
    }) : <React.Fragment></React.Fragment>

    return (
        <React.Fragment>
            <div>
                <h1>Accounts</h1>

                {accounts.length < MAX_ACCOUNTS &&
                    <div>
                        <Button color="primary" onClick={() => setIsAddAccountModalOpen(true)}>
                            + Add New Account
                            <CreateNewAccountModal 
                                isModalOpen={isAddAccountModalOpen} 
                                toggleModal={toggleAddAccountModal} 
                                closeModal={() => setIsAddAccountModalOpen(false)}
                                createAccount={createAccount} />
                        </Button>
                    </div>
                }
            </div>
            <div>
                <Table>
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Balance</th>
                            <th>Options</th>
                        </tr>
                    </thead>
                    <tbody>
                        {accountsRows}
                    </tbody>
                </Table>
            </div>
        </React.Fragment>
    );
}

export default Accounts;