import React , { useState , useEffect } from 'react';
import axios from 'axios';
import {useParams}from 'react-router-dom';  //http경로상의 매개변수 호출 해주는 함수


export default function View( props ){
    const params = useParams(); //useParams()훅: 경로[URL]상의 매개변수 반환
    console.log(params);
    console.log(params.bno);

    let [board,setBoard] = useState({});

            /*{params: {bno:params.bno} }*/
   useEffect(()=>{
        axios.get('/board/print', { params :{bno:params.bno} } )

            .then(res=>{
                console.log(res.data);
                setBoard(res.data)
            })
   },[])

        //세션스토리지 확인해서 로그인 정보 보기
/*        let login=JSON.parse(sessionStorage.getItem('login_token'));
        console.log("login session주머니 확인")
        console.log(login) //로그인 안하면 NULL 일반이면 나오고 OAUTH:0
        console.log(login.mno)

        console.log("board의 mno확인")
        console.log(board.mno)*/


    //삭제함수
    const onDeltete = () =>{
        console.log("delete함수실행")

        axios.delete("/board",{params:{ bno : params.bno }})
            .then(r=>{
                console.log(r.data)
               if(r.data==true){
                    alert('삭제성공')
                    window.location.href="/board/list"
               }else{
                    alert('삭제실패')
               }

            })
    }

    //수정함수
    const onupdate=()=>{
        console.log("update 함수 실행")
          window.location.href="/board/update?bno="+board.bno


    }


    const [ login,setLogin ] = useState(JSON.parse(sessionStorage.getItem('login_token')));
    //현재 로그인된 회원이 들어왔으면
    const btnBox =
        login !=null && login.mno == board.mno
        ?
        <div> <button onClick={ onDeltete } >삭제</button> <button onClick={onupdate}>수정</button> </div>
        : <div></div>


    return(<>
        <div>
            <h3> {board.btitle} </h3>
            <h3> {board.bcontent}  </h3>
            {btnBox}
        </div>

      </>)


}