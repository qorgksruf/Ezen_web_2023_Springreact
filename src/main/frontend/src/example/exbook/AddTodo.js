import React,{useState}from 'react';
import {Button,Grid,TextField} from '@mui/material'

export default function AddToDo(props){

        //사용자가 입력한 데이터를 저장할 상태변수
        const [item,setItem]=useState({title:""})

        //1.사용자가 입력한 데이터를 가져오는 함수
        //onChange={onInputChange}
        //onChange이벤트속성: 해당 요소가 해당 이벤트를 발생했을때 이벤트 정보 반환
        //onChange이벤트 정보 e를 onInputChange함수로 매개변수 e 전달



        const onInputChange= (e)=>{
                //console.log(e)://해당 이벤트가 발생했을때의 이벤트정보 들어있는 객체
                //console.log(e.target)://해당 이벤트가 발생된요소[TextField]
            setItem({title:e.target.value});
                console.log(item);
        }

              const addItem=props.addItem

              //3.+버튼을 클릭했을떄
              const onButtonClick=()=>{
                  addItem(item);  //입력받은 데이터를 AppTodo컴포넌트한테
                  //전달받은 addItem함수의 매개변수 넣고 함수 실행
                  setItem({title:""})
              }

            //4.엔터를 입력했을때
            const enterKeyEventHandler=(e)=>{
                if(e.key=='Enter'){
                    onButtonClick();
                }
            }


        return(<>

            <Grid container style={{marginTop:20}}>
                <Grid xs={11} md={11} item style={{paddingRight:16}}>
                        <TextField
                            placeholder="여기에 Todo 작성"
                            fullWidth
                            value={item.title}
                            onChange={onInputChange}
                            onKeyPress={enterKeyEventHandler}
                         />
                </Grid>
                <Grid xs={1} md={1} item>
                    <Button
                        fullWidth
                        style={{height:'100%'}}
                        color="secondary"
                        variant="outlined"
                        onClick={onButtonClick}
                        >
                        +
                    </Button>
                </Grid>
            </Grid>

            </>)
}