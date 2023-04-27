import React , { useState , useEffect } from 'react';
import axios from 'axios';
import {useParams}from 'react-router-dom';  //http경로상의 매개변수 호출 해주는 함수

import ReplyList from './ReplyList'


export default function View( props ){
    const params = useParams(); //useParams()훅: 경로[URL]상의 매개변수 반환
    console.log(params);
    console.log(params.bno);

    let [board,setBoard] = useState( {
        replyDtoList: []
    } );

            /*{params: {bno:params.bno} }여기손보기*/
         const getBoard=()=>{
                axios.get('/board/print', { params :{bno:params.bno} } )
                     .then(res=>{
                         console.log(res.data);
                         setBoard(res.data);
                     })
         }

        //컴포넌트 첯음 열렸을때
        useEffect(()=>{ getBoard();},[]);


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
    //댓글 삭제 렌더링
    const onReplyDelete=(rno)=>{
        console.log(rno);
             axios.delete("/board/reply", {params: {"rno":rno } })
                     .then(r=>{
                         if(r.data == true){
                             alert('삭제완료')
                             getBoard();
                         }else{
                            alert('삭제실패')
                         }
                     })

    }
    //댓글 수정함수
    const onReplyUpdate=(rno)=>{
        console.log("replyupdate함수실행")
        console.log(rno);
       let rcontent= prompt('입력하세요');

        let info={
                  rcontent: rcontent,
                  }

        console.log(info);
             axios.put("/board/reply", info , {params: {"rno":rno } })
                     .then(r=>{
                         if(r.data == true){
                             alert('수정완료')
                             getBoard();
                         }else{
                            alert('수정실패')
                         }
                     })

    }

    const onReplyRegi = (rno)=>{
        console.log("onReplyRegi함수실행");
        console.log(rno)
    }


    const [ login,setLogin ] = useState(JSON.parse(sessionStorage.getItem('login_token')));


    //댓글 작성시 렌더링
    const onReplyWrite= (rcontent) =>{
           let info={
                rcontent: rcontent,
                bno : board.bno
           }
            console.log(info);
            axios.post("/board/reply", info)
                    .then( (r)=>{
                    console.log("r.data 확인")
                    console.log(r.data);
                    if(r.data==true){
                        alert("글쓰기 완료")
                        getBoard();
                    }else{
                        alert("로그인후 가능");
                    }
            });

    }


    //현재 로그인된 회원이 들어왔으면
    const btnBox =
        login !=null && login.mno == board.mno
        ?
        <div> <button onClick={ onDeltete } >삭제</button>
              <button onClick={ onupdate }>수정</button>
        </div>
        : <div></div>


    return(<>
        <div>
            <h3> {board.btitle} </h3>
            <h3> {board.bcontent}  </h3>
            {btnBox}
        </div>
            <ReplyList
                onReplyDelete={onReplyDelete}
                onReplyWrite={onReplyWrite}
                onReplyUpdate={onReplyUpdate}
                onReplyRegi = { onReplyRegi }
                replyList={board.replyDtoList}
             />
      </>)


}