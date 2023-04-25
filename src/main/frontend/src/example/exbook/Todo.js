import React , { useState, useEffect } from 'react';
import { ListItem , ListItemText , InputBase ,
        Checkbox,ListItemSecondaryAction,IconButton } from '@mui/material';

import DeleteOutlined from "@mui/icons-material/DeleteOutlined";
import axios from 'axios';
//---------------과제----------------------------------------//
import Pagination from '@mui/material/Pagination';
/*
    npm install @mui/material @emotion/react @emotion/styled
    npm install @mui/material @mui/styled-engine-sc styled-components
    npm install @mui/icons-material
*/

export default function Todo( props ) {
    //페이징수 처리를위한 배열
/*    let[ rows,setRows ]=useState([])
    let [pageInfo,setPageInfo]=useState({'page':1});
    let [totalPage,setTotalPage]=useState(1);
    let [totalCount,setTotalCount]=useState(1);*/

/*   useEffect( () => {
        axios.get('/todo.do',{params:pageInfo}).then( r => {
            console.log(r);
            setRows(r.data.list)
            setTotalPage(r.data.totalPage)
            setTotalCount(r.data.totalCount)
        } )
   } , [pageInfo] )*/


//---------------과제---------------------------

    console.log( props );

    // 1. Hook 상태관리 useState
    const [ item , setItem ] = useState( props.item );

    //2. props 전달된 삭제함수 변수로 이동
    const deleteItem = props.deleteItem

    //3.삭제함수 이벤트처리 핸들러
    const deleteEventHandler = ()=>{
        deleteItem(item);

    }
    //4.
    const[readOnly,setReadOnly] =useState( true );


    //5.읽기모드 해제
    const turnOffReadOnly = () =>{
        console.log("turnOffReadOnly")
        setReadOnly(false); //readonly = true 수정불가능 false 수정가능
    }


    //6.엔터키를 눌렀을때 수정금지
    const turnOnReadOnly = (e) =>{
        console.log("turnOnReadOnly")
        if(e.key == "Enter"){
           setReadOnly(true);
            axios.put("/todo.do",item).then(r=>{console.log(r); })

        }

    }


    let editItem = props.editItem
    //7.입력받은 값을 변경
    const editEventHandler = (e)=>{
         console.log("editEventHandler")
        item.title=e.target.value; //
        editItem(item);
    }

    //체크박스 업데이트
    const checkboxEventHandler = (e)=>{
        item.done = e.target.value;
        editItem(); //상위 컴포넌트 렌더링
    }




    return ( <>
        <ListItem>
            <Checkbox checked = { item.done }  onChange={checkboxEventHandler}/>
            <ListItemText>
                <InputBase inputProps = {{readOnly:readOnly}}
                    onClick ={turnOffReadOnly}
                    onKeyDown={turnOnReadOnly}
                    onChange = {editEventHandler}
                    //inputProps={{ "aria-label" : "naked" }}
                    type="text"
                    id={ item.id }
                    name={ item.name }
                    value={ item.title }
                    multiline={ true }
                    fullWidth={ true }
                />
            </ListItemText>
                <ListItemSecondaryAction>
                    <IconButton onClick={deleteEventHandler}>
                        <DeleteOutlined />
                    </IconButton>
                </ListItemSecondaryAction>
        </ListItem>
    </> );
}