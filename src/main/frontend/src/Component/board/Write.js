import React , { useState , useEffect } from 'react';
import axios from 'axios';
import Container from '@mui/material/Container';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import CategoryList from './CategoryList'

export default function Write( props ){

    //게시물쓰기
    const setBoard=()=>{
        let info={
            btitle:document.querySelector('#btitle').value,
            bcontent:document.querySelector('#bcontent').value,
            cno:cno
        }
        console.log(info);

        axios.post('/board', info)
            .then(r=>{
                console.log(r);
                if(r.data==1){
                    alert('카테고리 선택후 쓰기 가능합니다')
                }else if(r.data==2){
                    alert('로그인하고와')
                }else if(r.data==3){
                    alert('게시물작성실패')
                }else if(r.data==4){
                    alert('게시물작성성공');
                    window.location.href="/board/list";
                }
            } )

    }
    // 카테고리 선택
    let [ cno , setCno ] = useState( 0 );
    const categoryChange = (cno) =>{   setCno( cno );   }


    return(<>
        <Container>
            <CategoryList categoryChange={  categoryChange } />
            <TextField fullWidth className="btitle" id="btitle" label="제목" variant="standard" />
            <TextField fullWidth className="bcontent" id="bcontent"
                      label="내용"
                      multiline
                      rows={10}
                      variant="standard"
            />
            <Button variant="outlined" onClick={ setBoard }> 등록 </Button>
            <Button variant="outlined"> 취소 </Button>
        </Container>
    </>)
}