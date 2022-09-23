import React from "react"
import PropTypes from 'prop-types'

const MenuButton = ( {onToggle} ) => {

    return (
        <button className="menu-button" onClick={onToggle}>Meny</button>
    )
}

/* Proptypes definitions */
MenuButton.propTypes = {
    onToggle: PropTypes.func.isRequired
}

export default MenuButton
