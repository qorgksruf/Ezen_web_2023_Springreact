import React , { useState , useEffect,useRef } from 'react';
import axios from 'axios';
import { DataGrid } from '@mui/x-data-grid';

export default function ProductWrite(props){

    //1.
    const writeForm = useRef(null); //useRef() 객체 반환해줌 이 객체는 current에 데이터가 들어있음(밑의 Dom데이터)
    //2.
    const onWriteHandler = ()=>{
        console.log('버튼작용됨')
        console.log(writeForm)
        const writeFormData = new FormData(writeForm.current);
        axios.post('/product',writeFormData).then(r=>{
            if(r.data==true){
                alert('등록성공')
                props.tabmove('3');
            }else{
                alert('등록실패')
            }
        })
    }

    return(<>
        <form ref={writeForm}>
            제품명 : <input type="text" name="pname" />
            제품가격 : <input type="text" name="pprice" />
            제품카테고리: <input type="text" name="pcategory" />
            제품설명: <input type="text" name="pcommnet" />
            제품제조사: <input type="text" name="pmanufacturer" />
            제품초기상태: <input type="text" name="pstate" />
            제품재고: <input type="text" name="pstock" />
            제품이미지: <input
                            type="file" multiple
                            accept="image/gif, image/jpeg, image/png"
                            name="pimgs"  />
            <button type="button" onClick={onWriteHandler}>제품등록</button>
        </form>

    </>)
}

/*
        console.log(writeForm.current)
        console.log(new FormData(writeForm.current) );*/