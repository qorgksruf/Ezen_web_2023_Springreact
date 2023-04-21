import React , { useState , useEffect } from 'react';
import axios from 'axios';

export default function Signup(props) {

    const onSignup= () =>{
            console.log("onSignup open")

       let info={
        memail:document.querySelector(".memail").value,
        mpassword:document.querySelector(".mpassword").value,
        mname:document.querySelector(".mname").value,
        mphone:document.querySelector(".mphone").value,
       }
       console.log(info)

        //ajax---->axios
        axios
            .post("http://localhost:9090/member/info",info)
            .then (r =>{
                  console.log(r)
                  if(r.data==true){
                     alert('가입되셨습니다');
                     Window.location.href ="/login"; // Window.location.href이동할 경로
                    }
                })
                .catch (err =>{console.log(err) });

    }//const 함수 e

    return(<>
                  <h3>회원가입페이지</h3>
                  <form>
                      아이디[이메일] : <input type="text" className="memail"/><br/>
                      비밀번호 :<input type="text" class="mpassword"/> <br/>
                      회원이름 :<input type="text" class="mname"/> <br/>
                      전화번호 :<input type="text" class="mphone"/> <br/>
                      <button onClick={ onSignup } type="button">가입</button>
                  </form>

     </>)
}

/*
    HTM-->JSX
    1.<> </>
    2.class->className
    3.카멜표기법
        onclick->onClick
        margin-top->marginTop
    4.문자처리

*/