import React , { useState , useEffect } from 'react';
import axios from 'axios';
import styles from '../../css/board/board.css';
//import { useSearchParams }from 'react-router-dom';


export default function ReplyList(props){
    //로그인정보


        //상위 view에게 받은 댓글 리스트
       const [replyDtoList,setReplyDtoList] = useState();
       console.log("replyDtoList확인")
       console.log(replyDtoList)

    console.log("props replyDtoList 확인")
    console.log(props.replyList)

    //댓글 작성 핸들러
    const onWriteHandler = ()=>{
        props.onReplyWrite(document.querySelector('.rcontent').value);
        document.querySelector('.rcontent').value = '';
    }

    //삭제 핸들러
    const onDeleteHandler = (e,rno)=>{
        console.log('삭제'+rno);
        props.onReplyDelete(rno); //props로 전달받은 삭제함수 실행하기

    }

    //수정 핸들러
    const onUpdateHandler = (e,rno)=>{
        console.log('수정'+rno);
        props.onReplyUpdate(rno); //props로 전달받은 수정함수 실행하기

    }

    //대댓글작성 헨들러
    const onReplyHandler = (e, rno)=>{
        console.log('대댓글작성'+rno);

        props.onReplyRegi(rno);
    }

    const onDisplayHandler = (e, rno)=>{
        console.log('+버튼')

        document.querySelector('.rere'+rno).style.display = 'block' ;

    }


    return(<>
      <input className="rcontent" type="text"/>
      <button className="button" onClick={onWriteHandler}> 댓글작성 </button>

            <h3> 댓글목록 </h3>
        {
            props.replyList.map( (r) => {
                if(r.rindex==0){
                    return (<>
                        <div>
                             <span> { r.rcontent } </span>
                             <span> { r.rdate } </span>

                             {
                                 /* JSX 형식에서 함수에 매개변수 전달 */
                                 /*
                                     <마크업 이벤트 = { (e) => { 함수명(e , 매개변수) } } />
                                 */
                             }
                             <button className="button" onClick={ (e) => onDeleteHandler( e, r.rno ) }> 삭제 </button>
                             <button className="button" onClick={ (e) => onUpdateHandler( e, r.rno) }> 수정 </button>
                             <button className="button" onClick={ (e) => onDisplayHandler(e,r.rno) } > + </button>
                        </div>

                        <div className={ "rere"+r.rno } style={{display:"none"}}>

                        {
                                props.replyList.map( (t) => {
                                    if( r.rno == t.rindex) {
                                            return (<>
                                              <div>
                                                <span> { t.rcontent } </span>
                                                <span> { t.rdate } </span>
                                                  <button  className="rebutton" onClick={ (e) => onDeleteHandler( e, t.rno) }> 삭제 </button>
                                                  <button  className="rebutton" onClick={ (e) => onUpdateHandler( e, t.rno) }> 수정 </button>
                                              </div>
                                             </>)
                                    }
                                } )
                        }
                             <button className="button" onClick={ (e) => onReplyHandler( e, r.rno) }> 대댓글쓰기 </button>

                        </div>
                    </>);

            }

            })
        }

   </>)

}

/* <button onClick={ (e) => onReplyHandler(e, r.rno)}> 대댓글쓰기 </button>*/