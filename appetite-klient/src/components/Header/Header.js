import React from "react"
import Logo from "../../logo.svg"
import BigLogo from "../../logo_big.svg"
import SmallLogo from "../../logo_small.svg"
import MenuButton from "./MenuButton"
import PropTypes from 'prop-types'

const Header = ( {onToggle} ) => {
    return (
        <div className="header">

            <div className="logo-small">
                <a href="."><img src={SmallLogo} alt="Logo"></img></a>
            </div>

            <div className="logo-default">
                <a href="."><img src={Logo} alt="Logo"></img></a>
            </div>

            <div className="logo-big">
                <a href="."><img src={BigLogo} alt="Logo"></img></a>
            </div>

            <MenuButton onToggle={onToggle} />
            
        </div>
    )
}

/* Proptypes definitions */
Header.propTypes = {
    onToggle: PropTypes.func.isRequired
}

export default Header