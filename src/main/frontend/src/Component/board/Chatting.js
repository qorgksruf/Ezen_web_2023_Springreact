import React , { useState , useEffect , useRef } from 'react';
import { List , Paper , Container , Button } from '@mui/material';
import styles from '../../css/board/Chatting.css';
import axios from 'axios';


export default function Chatting( props ) {

    let [ id , setId ] = useState(''); // 익명채팅방에서 사용할 id [ 난수 저장 ]
    let [ msgContent , setMsgContent ] = useState( [] ); // 현재 채팅중인 메시지를 저장하는 변수

    let msgInput = useRef(null);    //채팅입력창 DOM 객체 제어 변수
    let fileForm = useRef(null);    //패팅입력창 INPUT DOM 객체 제어 변수
    let fileInput=useRef(null);
    let chatContentBox = useRef(null);


    console.log( msgContent );



    // 1. 재렌더링 될때마다 새로운 접속
    // let clientSocket = new WebSocket("ws/localhost:8080/chat");

    // 2. 재렌더링 될때 데이터 상태 유지
    let ws = useRef( null );        // 1. 모든 함수 사용할 클라이언트 소켓 변수
    useEffect( () => {              // 2. 재 렌더링시 1번만 실행
        if( !ws.current ){          // 3. 클라이언트 소켓에 접속이 안되어있을때 [ * 유효성검사 ]

            ws.current = new WebSocket("ws://localhost:9090/chat"); // 4. 서버소켓 연결

            // 3. 클라이언트소켓이 서버소켓에 접속했을때 이벤트
            ws.current.onopen = () => {
                console.log("접속했습니다.");
                let randId = Math.floor( Math.random() * (9999 - 1) + 1);
                setId( '익명 ' + randId );
            }

            // 3. 서버소켓에서 나갔을때 이벤트
            ws.current.onclose = (e) => {
                console.log("나갔습니다.");
            }

            // 3. 서버소켓과 오류가 발생했을때 이벤트
            ws.current.onerror = (e) => {
                console.log("에러났습니다.");
            }

            // 3. 서버소켓으로부터 메시지를 받았을때 이벤트
            ws.current.onmessage = (e) => {
                console.log("서버소켓으로부터 메시지 받음.");
                console.log( e ); console.log( e.data );
                // msgContent.push( e.data );
                let data = JSON.parse( e.data );
                setMsgContent( (msgContent) => [ ...msgContent , data ] ); // 재 렌더링
            }
        }
    })

    // 4. 메시지 전송
    const onSend = () => {
        console.log( msgInput );
        console.log( msgInput.current.value );
        // msgInput 변수가 참조중인  <input ref={ msgInput } > 해당 input 를 DOM 객체로 호출

        let time = new Date().toLocaleTimeString();

        let msgBox = {
            id : id,                            // 보낸사람
            msg : msgInput.current.value,       // 보낸 내용
            time : time,                         // 현재 시간만
            type:'msg'
        }
        if(msgBox.msg !=''){ // 내용일 있으면 메시지 전송
                ws.current.send( JSON.stringify( msgBox ) ); // 클라이언트 메시지 전송 [ .send() ]
                msgInput.current.value = '' ;
        }
        //첨부파일 전송[ axios 이용한 서버에게 첨부파일 업로드]
        if(fileInput.current.value != ''){ //첨부파일이 존재하면
            let formData = new FormData(fileForm.current);
            fileAxios( formData ) //파일전송
        }

    }

    // 5. 렌더링 할때마다 스크롤 가장 하단으로 내리기
    useEffect( () => {
        document.querySelector('.chatContentBox').scrollTop = document.querySelector('.chatContentBox').scrollHeight;
    },[msgContent])


    //6.파일 전송 axios
    const fileAxios = (formData)=>{
                    axios.post("/chat/fileupload" ,formData)
                            .then( r => {
                                    console.log(r.data);
                                    // 다른 소켓들에게 업로드 결과 전달
                                            let msgBox = {
                                                id : id,                            // 보낸사람
                                                msg : msgInput.current.value,       // 보낸 내용
                                                type:'file',
                                                fileInfo: r.data                    //업로드 후 응답받은 파일 정보
                                            }
                                     ws.current.send(JSON.stringify(msgBox));
                                     fileInput.current.value ='';
                            });
    }



    return (<>
        <Container className="Container">
            <div
                className="chatContentBox"
                onDragEnter={ (e)=>{
                    console.log('onDragEnter');
                    e.preventDefault();
                } }
                onDragOver = { (e)=>{
                     console.log('onDragOver');
                     e.preventDefault();
                     e.target.style.backgroundColor = '#e8e8e8';

                } }

                onDragLeave = { (e)=>{
                      console.log('onDragLeave');
                      e.preventDefault();
                      e.target.style.backgroundColor = '#ffffff';
                } }
                onDrop = {(e)=>{
                      console.log('onDrop');
                      e.preventDefault();
                      {/* 드랍된 파일 호출= e.dataTransfer.files */}
                      let files = e.dataTransfer.files;
                      for(let i =0; i<files.length; i++){
                            if(files[i] != null && files[i] !=undefined){ {/*파일이 존재하면*/}

                                let formData= new FormData(fileForm.current)

                                formData.set( 'attachFile',files[i] )
                                fileAxios(formData)
                            }
                      }
                }}

            >
            {
                msgContent.map( (m) => {
                    return (<>
                        <div className="chatContent" style={ m.id == id ? { backgroundColor: '#e4ebea' , textAlign : 'right' } : { } }>
                            <span> { m.id } </span>
                            <span> { m.time } </span>
                            {
                                m.type == 'msg' ?
                                <span> {m.msg} </span>
                                : (<>
                                        <span>
                                            <span> {m.fileInfo.originalFilename} </span>
                                            <span> {m.fileInfo.sizekb} </span>
                                            <span> <a href ={"/chat/filedownload?uuidFile="+m.fileInfo.uuidFile} >저장 </a>  </span>
                                        </span>
                                  </>)
                            }
                        </div>
                    </>)
                })
            }
            </div>
            <div className="chatInputBox">
                <span> { id } </span>
                <input className="msgInput" ref={ msgInput } type="text" />
                <button onClick={ onSend }> 전송 </button>
                <form ref={fileForm}>
                   <input ref={ fileInput } type="file" name="attachFile"/>
                </form>
            </div>
        </Container>
    </>)
}

/*

    let 숫자 = 10;                            // 지역변수 : 컴포넌트[함수] 호출될때마다 초기화
    let 숫자2 = useRef(10);                   // 재렌더링시 초기값이 적용되지 않는 함수 [ 반환값 : 객체{ current } ]
    let inputRef = useRef(null);             // documet.querySelector vs useRef

    const [ id , setId ] = useState('');     // set메소드 사용시 컴포넌트 재호출 [ 재렌더링 ]

    // 1. 웹소켓 = webSocket = JS
    let webSocket = useRef( null );

    console.log( 숫자 );
    console.log( 숫자2 );
    console.log( id );

    // 2. 난수 생성
    let randId = Math.floor( Math.random() * (9999 - 1) + 1);
        // Math.floor( Math.random() * ( 최댓값 - 최솟값 ) + 최소값 ); : 정수 1~1000
    숫자 = randId;
    숫자2.current = randId;

    // setId( randId );

    const onSend = () => {
        console.log( inputRef.current.value );
        console.log( document.querySelector('.msgInput').value );
    }

 */