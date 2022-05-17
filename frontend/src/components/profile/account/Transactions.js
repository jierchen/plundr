import axios from "axios";
import React, { useContext, useEffect, useState } from 'react';
import { Table } from "reactstrap";
import { CredentialsContext } from "../../../App";
import CustomPagination from "../../custom/CustomPagination";

function Transactions(props) {
    const [transactions, setTransactions] = useState([]);
    const [totalPages, setTotalPages] = useState(-1);
    const [currentPage, setCurrentPage] = useState(0);
    const {credentials} = useContext(CredentialsContext);

    useEffect(() => {
        retrieveTransactionPage();
    }, [currentPage]);

    async function retrieveTransactionPage() {
        await axios.get(`http://localhost:8080/api/account/${props.currentAccountId}/transactions?page=${currentPage}&size=5`, {
            headers: {
                "Accept": "application/json"
            },
            auth: credentials
        })
        .then(res => {
            const transactionPage = res.data;
            setTransactions(transactionPage.content);
            setTotalPages(transactionPage.totalPages);
        }).catch(err => {
            console.log("Error retrieving transactions");
        });
    }

    const transactionsRows = transactions ? transactions.map(transaction => {
        console.log(transaction)
        return (
            <tr key={transaction.id}>
                <td>{transaction.transactionType}</td>
                <td>{transaction.description}</td>
                {() => {
                    if(transaction.transactionType === "DEPOSIT") {
                        return (
                            <React.Fragment>
                                <td>{"$" + transaction.amount}</td>
                                <td></td>
                            </React.Fragment>
                        );
                    } else if (transaction.transactionType === "WITHDRAW") {
                        return (
                            <React.Fragment>
                                <td></td>
                                <td>{"$" + transaction.amount}</td>
                            </React.Fragment>
                        );
                    } else if (transaction.transactionType === "INT_TRANSFER") {
                        return (
                            <React.Fragment>
                                <td>{transaction.owningAccountId == props.currentAccountId ? transaction.amount : ""}</td>
                                <td>{transaction.recipientAccountId == props.currentAccountId ? transaction.amount : ""}</td>
                            </React.Fragment>
                        );
                    } else if (transaction.transaction_type === "EXT_TRANSFER") {
                        return (
                            <React.Fragment>
                                <td>{transaction.owningAccountId == props.currentAccountId ? transaction.amount : ""}</td>
                                <td>{transaction.recipientAccountId == props.currentAccountId ? transaction.amount : ""}</td>
                            </React.Fragment>
                        );
                    }
                }}
                <td>{transaction.createDate}</td>
            </tr>
        );
    }) : <React.Fragment></React.Fragment>

    return (
        <React.Fragment>
            <div>
                <h1>Transactions</h1>
            </div>
            <div>
                <Table>
                    <thead>
                        <tr>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Sent</th>
                            <th>Received</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactionsRows}
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

export default Transactions;