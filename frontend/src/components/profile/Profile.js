import React, { useState } from 'react';
import Sidebar from './Sidebar';
import Dashboard from './Dashboard';
import Personal from './Personal';
import Accounts from './Accounts';
import Contacts from './Contacts';
import Funds from './Funds';
import axios from 'axios';
import './Profile.css';
import { Navigate } from 'react-router-dom';

export const TAB = {
    Dashboard: "Dashboard",
    Personal: "Personal",
    Accounts: "Accounts",
    Contacts: "Contacts",
    Funds: "Funds",
}

function Profile(props) {
    const [activeTab, setActiveTab] = useState(TAB.Dashboard);

    function switchActiveTab(tab) {
        setActiveTab(tab);
    }

    function isActiveTab(tab) {
        return activeTab === tab;
    }

    // Check authentication
    if(!props.isLoggedIn) {
        return <Navigate replace to="/login" />
    }

    let activeProfilePage = null;

    switch (activeTab) {
        case TAB.Dashboard:
            activeProfilePage = <Dashboard />
            break;

        case TAB.Personal:
            activeProfilePage = <Personal />
            break;

        case TAB.Accounts:
            activeProfilePage = <Accounts />
            break;

        case TAB.Contacts:
            activeProfilePage = <Contacts />
            break;

        case TAB.Funds:
            activeProfilePage = <Funds />
            break;

        default:
            activeProfilePage = <Dashboard />
    }

    return (
        <div id="profile">
            <div id="sidebar">
                <Sidebar isActiveTab={isActiveTab} switchActiveTab={switchActiveTab} />
            </div>

            <section id="profile-main">
                {activeProfilePage}
            </section>
        </div>
    );
}

export default Profile;