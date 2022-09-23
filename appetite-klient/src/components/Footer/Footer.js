import React, {useState} from "react"
import PropTypes from 'prop-types'
import { FaCheckCircle, FaExclamationCircle } from 'react-icons/fa'
import { Collapse } from "react-collapse"

const Footer = ( {onDelete, isConnected, errorMessage} ) => {
    const [toggle, setToggle] = useState(false)
    
    const toggleMenu = () => {
		setToggle(!toggle)
	}

    return (
        <>
        <div className="footer">
            <div className="connection-status">
                {isConnected ? <><FaCheckCircle color="green" /> Tilkoblet</> : <><FaExclamationCircle color="darkred" /> Ikke tilkoblet </>}
            </div>
            <button className='toggle-delete-button' onClick={toggleMenu} style={{display: toggle ? "none" : "block"}} >Slett handleliste</button>
            <Collapse isOpened={toggle} theme={{collapse: "delete-shoppinglist"}}>
            <div className="delete-shoppinglist-text">
                <b>Er du sikker?</b><br />
                Sletting kan ikke angres.<br/>
                <button id="delete-submit" type='submit' className="button" onClick={() => { onDelete(); toggleMenu() }}>Ja</button>
                <button id="delete-cancel" type='submit' className="button" onClick={toggleMenu}>Nei</button>
            </div>
            </Collapse>
        </div>
        <div className="error-message">{errorMessage}</div>
        </>
    )
}

/* Default props */
Footer.defaultProps = {
    isConnected: false
}

/* Proptypes definitions */
Footer.propTypes = {
    onDelete: PropTypes.func.isRequired,
    isConnected: PropTypes.bool,
    errorMessage: PropTypes.string
}

export default Footer
