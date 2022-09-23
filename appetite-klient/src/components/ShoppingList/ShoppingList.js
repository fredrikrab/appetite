import React, { createRef } from 'react'
import PropTypes from 'prop-types'
import ShoppingListTitle from './ShoppingListTitle'
import AddGrocery from './AddGrocery'
import Grocery from "./Grocery"

/* Grocery list component */
const ShoppingList = ({ shoppingList, onAdd, onRemove, onToggle }) => {
    return (
        <div className='container'>
            <ShoppingListTitle title={shoppingList['name']}/>
            <AddGrocery onAdd={onAdd} />
                {shoppingList['groceryItems']
                .map((item) => (
                    <Grocery
                        key={item.groceryText} ref={createRef()}
                        text={item.groceryText} quantity={item.groceryUnits} checked={item.checked}
                        onRemove={onRemove} onToggle={onToggle} />
                    )
                )
                .reverse()}
        </div>
    )
}

/* Proptypes definitions */
ShoppingList.propTypes = {
    shoppingList: PropTypes.object.isRequired,
    onAdd: PropTypes.func.isRequired,
    onRemove: PropTypes.func.isRequired,
    onToggle: PropTypes.func.isRequired
}

export default ShoppingList