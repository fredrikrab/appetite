import React from "react"
import { Collapse } from 'react-collapse'
import DropdownSelect from "./DropdownSelect"
import CreateShoppingList from "./CreateShoppingList"
import PropTypes from "prop-types"

const Navigation = ( {showMenu, fetchDropdown, onChangeShoppingList, onCreate } ) => {

    return (
        <Collapse isOpened={showMenu} theme={{collapse: "menu"}} >
            <div className="navigation-menu">
                <DropdownSelect fetchDropdown={fetchDropdown} onChange={onChangeShoppingList} />
                <CreateShoppingList onCreate={onCreate} showMenu={showMenu} />
            </div>
        </Collapse>
    )
}

/* Proptypes definitions */
Navigation.propTypes = {
    showMenu: PropTypes.bool.isRequired,
    fetchDropdown: PropTypes.array.isRequired, 
    onChangeShoppingList: PropTypes.func.isRequired, 
    onCreate: PropTypes.func.isRequired
}

export default Navigation