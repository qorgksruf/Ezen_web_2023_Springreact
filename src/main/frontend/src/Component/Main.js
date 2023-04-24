import React,{useState} from 'react';

export default function Main( props ){

   let [ login , setLogin ] = useState( JSON.parse( localStorage.getItem("login_token") ) );

    return (<>
         대문입니다
     </>)
}