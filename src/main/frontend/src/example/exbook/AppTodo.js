import React , { useState,useEffect } from 'react';
import Todo from './Todo';
import {List,Paper,Container } from '@mui/material';
import AddTodo from './AddTodo';
import axios from 'axios'; //npm install axios
// 교재 App 컴포넌트 --> AppTodo 컴포넌트
export default function AppTodo( props ) {
    // 1.
    // item 객체에 { id : "0" , title : "Hellow World 1" , done : true } 대입한것과 같음
    const [ items , setItems ] = useState(
        [
        ]
    );
    //컴포넌트가 실행될때 한번 이벤트 발생
    useEffect(()=>{
        //ajax: jquery설치가 필요
        //fetch: 리액트 전송 비동기 통신 함수[내장함수-설치x]
        //axios: 리액트 외부 라이브러리 [설치 필요] JSON통신 기본값
        axios.get("http://192.168.17.80:9090/todo").then(r=>{
            console.log(r);
            setItems(r.data);
        })
        //해당 주소의 매핑되는 컨트롤에 @CrossOrigin(origins = "http://localhost:3000")추기

        //axios.put("http://192.168.17.80:9090/todo").then(r=>{console.log(r); })
        //axios.delete("http://192.168.17.80:9090/todo",{params: {id:1} }).then(r=>{console.log(r); })

    }, [])

    //2.items에 새로운 item 등록하는 함수
    const addItem=(item)=>{ //함수로부터 매개변수로 전달받은 아이템
        axios.post( "http://192.168.17.80:9090/todo" , item )
                                           .then( r => {
                                               console.log( r );
                                           });

    console.log(items)
        //item.id="ID-"+items.length
        //item.done= false;
        setItems([...items,item]); //기존 상태에 items에 item추가
        console.log(items)
        //setItems([...기본배열,값]);
    }

            //items에 item 삭제
       const deleteItem= (item)=>{
                //만약에 items에 있는 item중 id와 삭제할 id와 다를경우 해당 item 반환
                const newItems = items.filter(i=>i.id !== item.id);
                    //삭제할 id를 제외한  새로운 newItems배열이 선언
                 setItems([...newItems]);
                 axios.delete("http://192.168.17.80:9090/todo",{params: {id:item.id } })
                        .then(r=>{
                        console.log(r);;
                       })

                //js 반복함수 제공
                    //배열/리스트.forEach((o)=>{ }) :반복문만 가능 [return 없음]
                    //let array = r.forEach((o)=>{return o+3});
                    //반복문이 끝나면 array에는 아무것도 들어있지않다
                    //배열/리스트.map((o)=>{)
/*                items.forEach((o,i)=>{
                    if(o.id==item.id){
                        items.splice(i,1);
                    }
                })*/
                //items에는 삭제할 item이 제거된 배열
                //???다시 화면 새로고침???
               // print();
       }


    //수정함수
    const editItem = ()=>{
        setItems([...items])
    }



    // 반복문 이용한 Todo 컴포넌트 생성
    //jsx의 style속성 방법
    let TodoItems =
        <Paper style={{margin:16}}>
            <List>
            {
                   items.map( ( i ) =>
                    <Todo
                    item={ i }
                    key={ i.id }
                    deleteItem={deleteItem}
                    editItem={editItem}/>
                   )
            }
            </List>
        </Paper>
    return ( <>
        <div className="App">
                <Container>
                     <AddTodo addItem={addItem} />
                     { TodoItems }
                </Container>
        </div>
    </> );
}