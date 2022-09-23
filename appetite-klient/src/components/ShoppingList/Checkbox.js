import PropTypes from 'prop-types'
import React from 'react'

const Checkbox = ( {checked, onToggle} ) => {
    return(
        <>
            <input id="checkbox" type="checkbox"
                checked={checked} onChange={onToggle}></input>
        </>
    )
}

/* Default props */
Checkbox.defaultProps = {
    checked: false
}

/* Proptypes definitions */
Checkbox.propTypes = {
    checked: PropTypes.bool,
    onToggle: PropTypes.func.isRequired
}

export default Checkbox
