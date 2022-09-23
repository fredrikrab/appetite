import React, { forwardRef } from 'react'
import PropTypes from 'prop-types'
import { FaRegTrashAlt } from 'react-icons/fa'
import Checkbox from "./Checkbox"

/* Grocery component */
const Grocery = forwardRef(( {text, quantity, checked, onToggle, onRemove}, ref) => {
    return (
        <div className='grocery-item' id={checked ? 'grocery-checked' : null} ref={ref}>
            {checked ? <span className='checked-line'></span> : null}
            <h3 id='grocery-text'>{text}</h3>
            <p id='grocery-quantity'>{quantity} stk</p>
            <p id='grocery-check'><Checkbox checked={checked} onToggle={() => onToggle(text)} /></p>
            <p id='grocery-remove'><FaRegTrashAlt onClick={() =>
                                    onRemove(text)} id="remove-icon" color="darkred" size ="1.2em" /></p>
        </div>
    )
})
Grocery.displayName = "Grocery"

/* Default props */
Grocery.defaultProps = {
    quantity: 1,
    checked: false,
}

/* Proptypes definitions */
Grocery.propTypes = {
    text: PropTypes.string.isRequired,
    quantity: PropTypes.number,
    checked: PropTypes.bool,
    onToggle: PropTypes.func.isRequired,
    onRemove: PropTypes.func.isRequired
}

export default Grocery