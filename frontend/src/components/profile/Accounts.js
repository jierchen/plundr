import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, Table } from 'reactstrap';
import { CredentialsContext } from '../../App';
import { BsCheckCircle } from 'react-icons/bs';
import CreateNewAccountModal from './account/CreateNewAccountModal';
import Transactions from './account/Transactions';

const MAX_ACCOUNTS = 5;

function Accounts(props) {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(null);
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

    async function setDepositAccount(accountId) {
        await axios.post("http://localhost:8080/api/depositAccount", {accountId: accountId}, {
            auth: credentials,
        })
        .then(async (res) => {
            await retrieveAccounts();
        })
        .catch(err => {
            console.log("Error setting deposit account")
        });
    }

    const accountsRows = accounts ? accounts.map(account => {
        return (
            <tr key={account.id}>
                <td>{account.id}</td>
                <td>{account.name}</td>
                <td>{account.accountType}</td>
                <td>{parseFloat(account.balance).toFixed(2)}</td>
                <td>
                    <Button color="primary" disabled={account.isDepositAccount} onClick={() => setDepositAccount(account.id)}>
                            Set
                    </Button>
                 </td>
                <td>
                    <Button color="info" onClick={() => {
                        setIsTransactionsModalOpen(true)
                        setSelectedAccount(account.id)
                    }}>
                        Check Transactions
                    </Button>
                    {' '}
                    <Button color="danger" onClick={() => removeAccount(account.id)}>
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
                            <th>Set As Deposit Account</th>
                            <th>Options</th>
                        </tr>
                    </thead>
                    <tbody>
                        {accountsRows}
                    </tbody>
                </Table>
            </div>
            <div>
                <Modal size="lg" isOpen={isTransactionsModalOpen} toggle={toggleTransactionModal}>
                    <ModalBody>
                        <Transactions currentAccountId={selectedAccount} />
                    </ModalBody>
                    <ModalFooter>
                        <Button color="danger" onClick={() => setIsTransactionsModalOpen(false)}>
                            Cancel
                        </Button>
                    </ModalFooter>
                </Modal>
                </div>
        </React.Fragment>
    );
}

export default Accounts;