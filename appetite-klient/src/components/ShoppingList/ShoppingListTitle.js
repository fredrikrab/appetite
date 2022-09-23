import React from "react"
import PropTypes from 'prop-types'

/* Header component */
const ShoppingListTitle = ({ title }) => {
    return (
        <h1>{title}</h1>
    )
}

/* Default props */
ShoppingListTitle.defaultProps = {
    title: 'Tittel',
}

/* Proptypes definitions */
ShoppingListTitle.propTypes = {
    title: PropTypes.string.isRequired,
}

export default ShoppingListTitle