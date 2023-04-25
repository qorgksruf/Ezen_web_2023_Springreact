import React , { useState , useEffect } from 'react';
import axios from 'axios';
export default function DashBoard( props ){

    const setCategory=()=>{ console.log('setCategory')
        let cname=document.querySelector(".cname");
        axios.post('http://localhost:9090/board/category/write',{"cname":cname.value})
            .then((r)=>{
                if(r.data==true){ alert('카테고리등록성공'); cname.value=''}
            })
     }
    return(<>
            <h6>관리자페이지</h6>
            <h6>카테고리등록</h6>
            <input type="text" className="cname"/>
            <button onClick={setCategory} type="button">카테고리등록</button>
    </>)
}