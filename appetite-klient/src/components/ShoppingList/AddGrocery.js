import Button from './Button';
import PropTypes from 'prop-types'
import React, { useState } from 'react'

/* Comoponent for adding new Grocery items to the shopping list */
const AddGrocery = ( {onAdd} ) => {
    const [text, setText] = useState('')
    const [quantity, setQuantity] = useState('')

    const handleQuantityChange = (number) => {
        setQuantity(number)
    }

    const handleTextChange = (text) => {
        setText(text)
    }

    const isValid = () => {
        if (text === '' || !text.match(/^[a-zA-ZæøåÆØÅ -]*$/)) return false
        return true
    }

    const handleSubmit = (e) => {
        e.preventDefault()      // Return false
        onAdd(text, quantity)   // Call parent function with correct arguments
        setText('')             // Reset text field
        setQuantity('')         // Reset quantity field
    }

    return (
        <form className='add-grocery-form'>
            <input id='add-grocery-text'            /* Text input */
                type='text'
                placeholder='Legg til vare'
                value={text}
                onChange={e => handleTextChange(e.target.value)}
            />
            <input id='add-grocery-quantity'        /* Quantity input */
                type='number'
                min='1'
                placeholder='Antall'
                value={quantity}              /* Allows controlled component with empty placeholder */
                onChange={e => handleQuantityChange(e.target.value)}
                    
            />
            <Button text='Legg til' type='submit' 
                    disabled={!isValid()} onClick={(e) => { handleSubmit(e) }} />
        </form>
    )
}

/* Proptypes definitions */
AddGrocery.propTypes = {
    onAdd: PropTypes.func.isRequired
}

export default AddGrocery
