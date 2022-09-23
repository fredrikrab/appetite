import axios from 'axios'
import {localhost} from './localhost.js'


export default axios.create({
    baseURL: localhost,
    headers: {
       
    }
})




