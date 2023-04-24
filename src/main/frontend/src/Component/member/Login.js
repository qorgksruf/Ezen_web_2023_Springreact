import React , { useState , useEffect } from 'react';
import axios from 'axios';
import styles from '../../css/member/login.css';



export default function Login(props) {
    //로그인
    const onLogin=()=> {
        console.log("onlogin open")
        let loginform = document.querySelectorAll(".loginform")[0];
        let loginformData = new FormData(loginform);

        axios
            .post("http://localhost:9090/member/login", loginformData)
            .then(r=>{
                console.log(r);
                if(r.data==false){
                    alert('동일한 회원정보가 없습니다')
                }else{
                    alert('로그인성공')

                    //js 로컬 스토리지에 로그인 성공한 흔적 남기기
                    //localStorage.setItem("key",value)://String타입
                        //jsom.stringfly()
                    //localStorage.setItem("login_token" , JSON.stringify( r.data ) );
                    sessionStorage.setItem("login_token" , JSON.stringify( r.data ) );
                    window.location.href="/";
                }
            })
    }

    return(<>
            <h3>로그인페이지입니다</h3>
            <form className="loginform" >
                아이디[이메일] : <input type="text" name="memail"/><br/>
                비밀번호 :<input type="text" name="mpassword"/> <br/>
                <button onClick={onLogin} type="button">로그인</button>
                <a href="/member/find">계정정보 찾기</a>
                <a href="http://localhost:9090/oauth2/authorization/google">구글로그인</a>
                <a href="http://localhost:9090/oauth2/authorization/kakao">카카오로그인</a>
                <a href="http://localhost:9090/oauth2/authorization/naver">네이버로그인</a>
            </form>
    </>)
}