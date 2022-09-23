import React from "react"
import PropTypes from 'prop-types'

/* Button component */
const Button = ( {text, onClick, disabled }) => {
    return (
        <button className='button' disabled={disabled} onClick={onClick}>
            {text}<span className="add-grocery-tooltip">Fyll inn gyldig tekst</span>
        </button>
    )
}

/* Default props */
Button.defaultProps = {
    text: 'Button',
    disabled: true
}

/* Proptypes definitions */
Button.propTypes = {
    text: PropTypes.string,
    onClick: PropTypes.func.isRequired,
    disabled: PropTypes.bool
}

export default Button
