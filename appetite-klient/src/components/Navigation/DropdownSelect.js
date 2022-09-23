import React from "react"
import Select from 'react-select'
import PropTypes from "prop-types"

const DropdownSelect = ( {fetchDropdown, onChange, shoppingList} ) => {    
    return (        
        <div className="dropdown-select">
            <Select cacheOptions defaultOptions key={shoppingList}
                         placeholder="Velg handleliste"
                         loadingMessage="Laster inn handlelister..."
                         noOptionsMessage={() => "Fant ingen handlelister"}
                         options={fetchDropdown}
                         onChange={(e) => onChange(e['value'])}
                          />
        </div>
    )
}

/* Proptypes definitions */
DropdownSelect.propTypes = {
    fetchDropdown: PropTypes.array.isRequired, 
    onChange: PropTypes.func.isRequired, 
    shoppingList: PropTypes.shape({
        name: PropTypes.string,
        groceryItems: PropTypes.array
    })
}

export default DropdownSelect

