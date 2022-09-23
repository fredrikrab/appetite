import React, { useState, useEffect } from 'react'
import Header from './components/Header/Header'
import Navigation from './components/Navigation/Navigation'
import ShoppingList from './components/ShoppingList/ShoppingList'
import Footer from './components/Footer/Footer'
import API from './API'

 /**
  * Demonstration shoppinglist. Will only be loaded if no other shoppinglist is available.
  * Cannot be saved to backend because title has invalid characters (this is on purpose).
  */
const demoState = {"name":"Demo-handleliste (endringer lagres ikke)", "groceryItems":[
	{"groceryText":"Brød","groceryUnits":1,"checked":false},
	{"groceryText":"Syltetøy","groceryUnits":1,"checked":false},
	{"groceryText":"Brunost","groceryUnits":1,"checked":false},
	{"groceryText":"Melk","groceryUnits":1,"checked":false}
]}


/**
 * Helper function that checks whether text consists of invalid characters.
 * 
 * @param {string} text 
 * @returns true if text only has valid characters and false otherwise.
 */
const isValidText = (text) => {
	if (text.match(/^[a-zA-ZæøåÆØÅ -]*$/)) return true
	return false
}

/**
 * Helper function that capitalizes first letter and returns rest of string in lowercase.
 * This is identical to text validation on the backend.
 * 
 * @param {string} text 
 * @returns text with required capitalization
 */
const setCapitalization = (text) => {
	return text[0].toUpperCase() + text.substring(1).toLowerCase()
}

/**
 * Main react component - it is the only component rendered by index.js.
 * Defines the global state, executes core logic and sends props to sub-components.
 * 
 * @returns JSX that renders Header, Navigation, ShoppingList and Footer components.
 */
function App() {
	/* eslint-disable no-tabs */
	const [showMenu, setShowMenu] = useState(false)						// boolean for displaying navigation menu
	const [isConnected, setIsConnected] = useState(false)				// boolean for reporting connection status
	const [dropdownOptions, setDropdownOptions] = useState([])			// array containing dropdown options
	const [shoppingList, setShoppingList] = useState(demoState)			// current shoppinglist objected
	const [shoppingListsPendingSave, setPendingSave] = useState([])		// array containing unsaved shoppinglist objects
	const [errorMessage, setErrorMessage] = useState(null)				// string for displaying error messages
	/* eslint-enable no-tabs */


	/**
	 * Function for toggling visibility of navigation menu.
	 */
	const toggleMenu = () => {
		setShowMenu(!showMenu)
	}

	
	/**
	 * React Hook that will run when App-component renders. It checks whether the server responds to a request or not.
	 * The empty dependency array ensures this function only runs once (on initial rendering).
	 */
	useEffect(() => {
		checkServerConnection()
	}, []) // eslint-disable-line react-hooks/exhaustive-deps


	/**
	 * React Hook that will run if and when errorMessage is set.
	 * errorMessage is cleared after the timeout.
	 */
	useEffect(() => {
		const timer = setTimeout(() => setErrorMessage(null), 8000)
		return () => clearTimeout(timer)
	}, [errorMessage])


	/**
	 * React Hook that will run on change in connection status.
	 * It GETs dropdown options from server if connected.
	 * It also POSTs any unsaved shoppinglists to the server.
	 */
	useEffect(() => {
		if (isConnected) {
			if (shoppingListsPendingSave.length > 0) {
				shoppingListsPendingSave.forEach(pendingShoppingList => {
					if (isValidText(pendingShoppingList['name'])) { // eslint-disable-line curly
						API.post('/shoppinglist/create', pendingShoppingList, {
							headers: {"Content-Type": "application/json; charset=UTF-8"},
							params: {overwrite: true}
						})
					}
				})
				setPendingSave([]) // Clear shoppingListsPendingSave array
			}

			fetchDropdownOptions() // Refresh dropdown options from server if we're connected
		}
	}, [isConnected]) // eslint-disable-line react-hooks/exhaustive-deps

	/**
	 * Set global state to connected if server responds to ping with HTTP Status 200. Set to false otherwise.
	 * 
	 * If coming from a disconnected state, the shoppinglists in array shoppingListsPendingSave
	 * is submitted to the server with the parameter overwrite=true. The array is cleared afterwards.
	 */
	const checkServerConnection = async () => {

		// Send GET-request to ping path
		API.get('/shoppinglist/ping')

		// Set global state to connected if server response is OK
		.then(() => setIsConnected(true))

		// Set global state to disconnected if request is unsuccessful
		.catch(() => setIsConnected(false))
	}


	/**
	 * Request list with the names of all available shoppinglists.
	 * Use the list to create an array of available shoppinglists for the dropdown menu component.
	 */
	const fetchDropdownOptions = async() => {

		// Send GET-request
		API.get('/shoppinglist/getAll')
		.then((response) => {

			// If successful reply but no shoppinglists…
			if (response.status === 200 && typeof response.data[0] === 'undefined') {
				setShowMenu(true) // …show the navigation menu for easy creation of new shoppinglist
				setIsConnected(true) // …set global state to connected

			// If successful reply and available shoppinglists, construct corresponding dropdown array
			} else {
				const options = []
				response.data.forEach((element) => {
					options.push({value: element, label: element})
				})

				// Automatically load first available shoppinglist to global state if in demoState
				if (shoppingList['name'] === demoState.name) getShoppingList(options[0].value)

				setDropdownOptions(options) // Use constructed array for dropdown menu
			}
		})

		// If unsuccessful…
		.catch(() => {
			checkServerConnection() // …check server connection
			if (dropdownOptions.length === 0) setShowMenu(true) // …show the navigation if no shoppinglists exists
		})
	}


	/**
	 * Add shoppinglist to array of local shoppinglists.
	 * These are stored in state, including any changes, until the page is refreshed (the states are then lost). 
	 * If the connection is regained, all shoppinglist objects in this array will be posted to server.
	 * 
	 * @param {object} shoppingListToSave shoppinglist object
	 */
	
	const addToPendingSave = (shoppingListToSave) => {

		/* eslint-disable curly */
		// Only add shoppinglist to array if it's not already included. Changes to shoppinglists in the array are tracked,
		// ie. there is no need to replace an existing shoppinglist in this array with an updated version of itself.
		if (shoppingListsPendingSave

			// Filter out objects in array with the same name as the inputted shoppinglist
			.filter(pendingShoppingList => pendingShoppingList['name'] === shoppingListToSave['name'])

			// If the length of the resulting array is 0, add inutted shoppinglist to array
			.length === 0) {
				setPendingSave(shoppingListsPendingSave => [...shoppingListsPendingSave, shoppingListToSave])
			}
		/* eslint-enable curly */
	}


	/**
	 * Requests specific shoppinglist from the server and set that shoppinglist to global state.
	 * 
	 * @param {string} shoppingListName name of shoppinglist
	 */
	const getShoppingList = async shoppingListName => {
		
		if (!isValidText(shoppingListName)) return // Return if illegal shoppinglist name
		checkServerConnection()

		// If not connected to server, get shoppinglist from local storage
		if (!isConnected) {
			const findShoppingList = shoppingListsPendingSave.find(pendingShoppingList => pendingShoppingList['name'] === shoppingListName)
			if (typeof findShoppingList !== 'undefined') setShoppingList(findShoppingList)
			return
		}

		API.get(`/shoppinglist/get/${shoppingListName}`) // Send GET-request for inputted shoppinglist name

		// If successful reply…
		.then((response) => {
			setShoppingList(response.data) // …use received shoppinglist for global state
			setIsConnected(true) // …set global state to connected
			setShowMenu(false) // …close navigation menu
		})

		// If unsuccessful despite being connected to the server, log the error.
		// If unsuccessful because no server connection, store current shoppinglist to local state.
		.catch((error) => { (isConnected) ? console.log(error) : addToPendingSave(shoppingList) })
	}
	

	/**
	 * Creates and loads a new shoppinglist via the following steps:
	 * 1. Construct shoppinglist object with inputted name and an empty grocery list.
	 * 2. Use this new shoppinglist for the global state.
	 * 3. Add dropdown entry for the new shoppinglist.
	 * 4. POST new shoppinglist to server.
	 * Add both current and new shoppinglist to local storage array if no server connection.
	 * 
	 * @param {string} shoppingListName name of shoppinglist
	 */
	const createShoppingList = async (shoppingListName) => {

		if (!isValidText(shoppingListName)) return // Return if illegal shoppinglist name
		shoppingListName = setCapitalization(shoppingListName) // Set capitalization
		if (dropdownOptions.some(entry => entry.value === shoppingListName)) {
			setErrorMessage("Kan ikke opprette handleliste fordi det finnes en handleliste med samme navn.")
			return // Return if object already exists
		}
		
		// Create shoppinglist object
		const newShoppingList = { name: shoppingListName, groceryItems: [] }

		// Construct and add dropdown entry
		dropdownOptions.push({value: `${newShoppingList['name']}`, label: `${newShoppingList['name']}`})
		setDropdownOptions(dropdownOptions)

		// If disconnected, save current and new shoppinglist to local storage
		checkServerConnection().then(() => {
			if (!isConnected) { 
				addToPendingSave(shoppingList)
				addToPendingSave(newShoppingList)
			}
		})

		// Set new shoppinglist object to global state
		setShoppingList(newShoppingList)

		// Send POST-request with shoppinglist object
		API.post('/shoppinglist/create', newShoppingList)
		.then(response => {
			if (response.status === 200) {
				getShoppingList(newShoppingList['name']) // Get new shoppinglist from server
				setIsConnected(true) // Set global state to connected
			}
		})
		.catch(error => {
			/* eslint-disable curly */
			if (typeof error.response !== 'undefined' && error.response.status === 409) {
				console.error(error.response.status,  error.response.data.message)
				setErrorMessage("Kan ikke opprette handleliste fordi det finnes en handleliste med samme navn.")
			} else {
				console.error(error) // Log error if connected to server and an unexpected error is returned
			}
			/* eslint-enable curly */
		})
	}

	
	/**
	 * Deletes shoppinglist.
	 * First remove shoppinglist from dropdown menu, then select either first dropdown option or demoState.
	 * Finally send DELETE-request to server. If no connection, begin to keep track of local changes in shoppingListsPendingSave array.
	 * DELETE-requests will not be re-broadcasted if connection is regained, thus permanent deletion requires an active connection to the backend.
	 * 
	 * @param {object} shoppingListToDelete shoppinglist object
	 */
	const deleteShoppingList = async (shoppingListToDelete = shoppingList['name']) => {

		if (!isValidText(shoppingListToDelete)) return // Return if illegal shoppinglist name

		// Replace dropdown options with an array that does not include the name of the shoppinglist to delete
		const updatedDropdownOptions = dropdownOptions.filter(entry => entry.value !== shoppingListToDelete)
		setDropdownOptions(updatedDropdownOptions)

		// Set global state to first dropdown option if it exists, otherwise use demoState
		if (updatedDropdownOptions.length > 0) getShoppingList(updatedDropdownOptions[0]['value'])
		else setShoppingList(demoState)

		// Update local storage if disconnected
		checkServerConnection().then(() => {
			if (!isConnected) {
				// Remove inputted shoppinglist from local storage array
				// This ensures that shoppinglists that are first created and then deleted, all while offline, won't be submitted to server
				const updatedPendingSave = shoppingListsPendingSave.filter(pendingShoppingList => pendingShoppingList.name !== shoppingListToDelete)
				setPendingSave(updatedPendingSave)
			}
		})

		// Send DELETE-request with name of shoppinglist to delete
		API.delete('/shoppinglist/delete/', { data: { name: shoppingListToDelete } })
		.then(response => {
			if (response.status === 200) {
				fetchDropdownOptions() // Refresh dropdown options from server
				setIsConnected(true) // Set global state to connected
			}
		})
		.catch(error => { console.error(error) } ) // Log error if connected to server and an unexpected error is returned

	}

	
	/**
	 * Add grocery to current shoppinglist and send PUT-request to server.
	 * 
	 * @param {string} groceryText name of grocery
	 * @param {string} quantity quantity to add (integer sent as string)
	 */
	const addGrocery = async (groceryText, quantity) => {
		
		if (!isValidText(groceryText)) return // Return if illegal groceryText
		groceryText = setCapitalization(groceryText) // Set capitalization
		
		
		// quantity is submitted via HTML form and will be of type string
		// Convert to int. If not possible, default to 1 (e.g. if nothing is entered in quanity field)
		if (isNaN(parseInt(quantity, 10))) quantity = 1
		else quantity = parseInt(quantity, 10)
		
		// Construct GroceryItem object
		const newGrocery = { groceryText,
			groceryUnits: quantity,
			checked: false }

		/* eslint-disable curly */
		const updatedShoppingList = {...shoppingList}

		// Only add quantity if element already exists in the shoppingList
		if (shoppingList['groceryItems'].some(grocery => grocery.groceryText === groceryText)) {
			updatedShoppingList['groceryItems'].find(grocery => grocery.groceryText === groceryText).groceryUnits += quantity
		} else {
			// Add element to shoppinglist
			updatedShoppingList['groceryItems'].push(newGrocery)
		}
		/* eslint-disable curly */
		
		setShoppingList(updatedShoppingList)

		if (!isValidText(shoppingList['name'])) return // Return if illegal shoppinglist name (ie. if using demoState)

		// Update local storage if disconnected
		checkServerConnection().then(() => {
			if (!isConnected) addToPendingSave(shoppingList)
		})


		// Send PUT-request with new GroceryItem
		API.put(`/shoppinglist/${shoppingList.name}/grocery/add/`, newGrocery)
		.catch(error => {
			/* eslint-disable curly */
			if (typeof error.response !== 'undefined' && error.response.status === 404) {
				console.error(error.response.status,  error.response.data.message)
				setErrorMessage("Fant ikke etterspurt handleliste.")
			} else {
				console.error(error) // Log error if connected to server and an unexpected error is returned
			}
			/* eslint-enable curly */
		})
	}

	
	/**
	 * Remove grocery from current shoppinglist and send DELETE-request to server.
	 * 
	 * @param {string} groceryText name of grocery
	 */
	const removeGrocery = async groceryText => {

		if (!isValidText(groceryText)) return // Return if illegal groceryText

		// Update state without grocery in current shoppinglist
		const updatedShoppingList = {...shoppingList}
		updatedShoppingList['groceryItems'] = updatedShoppingList.groceryItems.filter(grocery => grocery.groceryText !== groceryText)
		setShoppingList(updatedShoppingList)

		if (!isValidText(shoppingList['name'])) return // Return if illegal shoppinglist name (ie. if using demoState)

		// Update local storage if disconnected
		checkServerConnection().then(() => {
			if (!isConnected) addToPendingSave(shoppingList)
		})

		// Send DELETE-request to server with groceryText
		API.delete(`/shoppinglist/${shoppingList.name}/grocery/remove/`, { data: { groceryText } })
		.catch(error => {
			/* eslint-disable curly */
			if (typeof error.response !== 'undefined' && error.response.status === 404) {
				console.error(error.response.status,  error.response.data.message)
				setErrorMessage("Fant ikke etterspurt handleliste.")
			} else {
				console.error(error) // Log error if connected to server and an unexpected error is returned
			}
			/* eslint-enable curly */
		})
	}

	
	/**
	 * Toggles (checks or unchecks) specified grocery in current shoppinglist.
	 * 
	 * @param {string} groceryText name of grocery
	 */
	const toggleGrocery = async groceryText => {

		if (!isValidText(groceryText)) return // Return if illegal groceryText

		// Update state with flipped boolean as checked status for specified grocery
		const updatedShoppingList = {...shoppingList}

		/* eslint-disable no-tabs */
		updatedShoppingList['groceryItems']						// Return groceryItem array of current shoppinglist
		.filter(grocery => grocery.groceryText === groceryText) // Filter for grocery that matches given groceryText
		.map((grocery) => { 									// Run function on remaining object
			grocery.checked = !grocery.checked
			return grocery
		})
		/* eslint-enable no-tabs */

		setShoppingList(updatedShoppingList) // Update global state
		if (!isValidText(shoppingList['name'])) return // Return if illegal shoppinglist name (ie. if using demoState)

		// Update local storage if disconnected
		checkServerConnection().then(() => {
			if (!isConnected) addToPendingSave(shoppingList)
		})

		// Store new checked status as variable, which will be passed to server. This enables the server to verify that the checked state is in sync.
		const checked = shoppingList['groceryItems'].find(grocery => grocery.groceryText === groceryText)['checked']

		// Send PUT-request to server
		API.put(`/shoppinglist/${shoppingList.name}/grocery/toggle/`, {
			shoppingListName: shoppingList['name'],
			groceryText,
			checked
		})
		.catch(error => {
			/* eslint-disable curly */
			if (typeof error.response !== 'undefined' && error.response.status === 404) {
				console.error(error.response.status,  error.response.data.message)
				setErrorMessage("Fant ikke etterspurt handleliste.")
			} else {
				console.error(error) // Log error if connected to server and an unexpected error is returned
			}
			/* eslint-enable curly */
		})
	}

	return (
	<>
	<Header onToggle={toggleMenu}/>
	<Navigation showMenu={showMenu} fetchDropdown={dropdownOptions} key={shoppingList['name']}
				onChangeShoppingList={getShoppingList} onCreate={createShoppingList} onDelete={deleteShoppingList} />
	<ShoppingList shoppingList={shoppingList} onAdd={addGrocery} onRemove={removeGrocery} onToggle={toggleGrocery} />
	<Footer onDelete={deleteShoppingList} isConnected={isConnected} errorMessage={errorMessage} />
    </>
	)

}

export default App