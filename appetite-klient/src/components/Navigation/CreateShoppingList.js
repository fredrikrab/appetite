import React, { useState } from "react"
import { Collapse } from 'react-collapse'
import PropTypes from 'prop-types'

const CreateShoppingList = ( {showMenu, onCreate} ) => {
    const [toggle, setToggle] = useState(false)
    const [text, setText] = useState('')

    // Toggle menu button
	const toggleMenu = () => {
		setToggle(!toggle)
	}

    const handleSubmit = (e) => {
        e.preventDefault()      // Return false
        setText('')             // Reset text field
        onCreate(text)          // Call parent function
    }

    const isValid = () => {
        if (text === '' || !text.match(/^[a-zA-ZæøåÆØÅ -]*$/)) return false
        return true
    }

    return (
        <>
        <button className="create-shoppinglist-button" onClick={toggleMenu}>Ny handleliste</button>
        <Collapse isOpened={showMenu && toggle} theme={{collapse: "create-shoppinglist"}}>
            <div className="create-shoppinglist-form">
            <form>
                <input id='shoppinglist-text' type='text' className="create-shoppinglist-form"
                        placeholder='Navn på handleliste'
                        value={text} onChange={e => setText(e.target.value)} />

                <button id="shoppinglist-submit" type='submit' className="button"
                        disabled={!isValid()} onClick={(e) => { handleSubmit(e) }}>Opprett</button>
            </form>
            </div>
        </Collapse>
        </>
    )
}

/* Proptypes definitions */
CreateShoppingList.propTypes = {
    onCreate: PropTypes.func.isRequired,
    showMenu: PropTypes.bool.isRequired
}

export default CreateShoppingList

