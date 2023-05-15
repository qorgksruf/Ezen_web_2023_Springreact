import React,{useState, useEffect} from 'react';
import axios from 'axios';
export default function Main( props ){

    //let [ login , setLogin ] = useState( JSON.parse( localStorage.getItem("login_token") ) );


    const [items,setItems] = useState([]);

    useEffect( ()=>{
            axios.get('/product/main').then( r => {setItems(r.data) })
    },[])

    console.log(items);

    return (<>
            <div>
                {
                    items.map( item => {
                        return (<div>
                            <img src={'http://localhost:9090/static/media/' + item.files[0].uuidFile } />
                            <div> { item.pname } </div>
                        </div>)
                    })

                }
            </div>
        </>);

}