import React , { useState } from 'react';
import Todo from './Todo';
import {List,Paper,Container } from '@mui/material';
import AddTodo from './AddTodo';
// 교재 App 컴포넌트 --> AppTodo 컴포넌트
export default function AppTodo( props ) {

    // 1.
    // item 객체에 { id : "0" , title : "Hellow World 1" , done : true } 대입한것과 같음
    const [ items , setItems ] = useState(
        [
        ]
    );

    //items에 새로운 item 등록하는 함수
    const addItem=(item)=>{ //함수로부터 매개변수로 전달받은 아이템
        item.id="ID-"+item.length
        item.done=false;
        setItems([...items,item]); //기존 상태에 items에 item추가
        //setItems([...기본배열,값]);
    }

    // 반복문 이용한 Todo 컴포넌트 생성
    //jsx의 style속성 방법
    let TodoItems =
        <Paper style={{margin:16}}>
            <List>
            {
                   items.map( ( i ) =>
                    <Todo item={ i } key={ i.id } />
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