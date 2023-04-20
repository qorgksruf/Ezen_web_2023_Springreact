import React, {useState,useEffect} from 'react';


export default function Hook2(props){

        let [value,setValue]=useState(0);

      console.log("value::"+value)

        useEffect( ()=>{
            console.log("useEffect1()실행 ");
            return()=>{ //mount  실행x
                console.log("[]없는 useEffect종료되면서 실행")
            }

        })

        useEffect( ()=>{
            console.log("useEffect1()실행 ");
            return()=>{
                console.log("[]있는 useEffect종료되면서 실행")
            }

        }, [ ] )// 빈 배열 [] update 제외


        useEffect( ()=>{
            console.log("[value]있는 useEffect1()실행 ");
            return()=>{
                console.log("[value]있는 useEffect종료되면서 실행")
            }

        }, [value ] )// 빈 배열 [상태변수명]대입 해당 상태변수가 update 될때마다

    return(
        <>
                <p> {value} </p>
                <button onClick={ ()=>setValue(value+1) }>
                +
                </button>
        </>)
}