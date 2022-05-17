import { Nav, Navbar, NavItem, NavLink } from 'reactstrap';
import { BsCashStack, BsCreditCard2Back, BsClipboardData, BsFillFilePersonFill, BsGearFill } from 'react-icons/bs';
import { TAB } from './Profile';

function Sidebar(props) {

    return (
        <Nav tabs vertical pills>
            <NavItem>
                <NavLink active={props.isActiveTab(TAB.Dashboard)} onClick={() => props.switchActiveTab(TAB.Dashboard)}>
                    <BsClipboardData /> Dashboard
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink active={props.isActiveTab(TAB.Personal)} onClick={() =>props.switchActiveTab(TAB.Personal)}>
                    <BsGearFill /> Personal
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink active={props.isActiveTab(TAB.Accounts)} onClick={() =>props.switchActiveTab(TAB.Accounts)}>
                    <BsCreditCard2Back /> Accounts
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink active={props.isActiveTab(TAB.Contacts)} onClick={() =>props.switchActiveTab(TAB.Contacts)}>
                    <BsFillFilePersonFill /> Contacts
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink active={props.isActiveTab(TAB.Funds)} onClick={() =>props.switchActiveTab(TAB.Funds)}>
                    <BsCashStack /> Funds
                </NavLink>
            </NavItem>
        </Nav>
    );
}

export default Sidebar;