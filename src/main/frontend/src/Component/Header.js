import React , { useState , useEffect } from 'react';
import axios from 'axios';

export default function Header( props ){

//로그인상태
    let [login,setLogin] = useState(null);

//로그아웃
const logOut=()=>{
    // js세션 스토리지 초기화
    sessionStorage.setItem("login_token",null);
    axios.get("/member/logout").then(r=>{console.log(r);});
    //window.location.href = "/member/login";
    setLogin(null); //렌더링
}



    //로그인상태 호출
    useEffect(()=>{
        axios.get("/member/info")
            .then(r=>{console.log(r);
                if(r.data !=''){ //로그인되어 있으면 // 서비스에서 null이면 js에서 ''이다

                    sessionStorage.setItem("login_token",JSON.stringify(r.data));
                       //상태변수에 로컬 스트리지를 호출해서 상태변수에 데이터 저장[렌더링하기위해]
                    setLogin( JSON.parse (sessionStorage.getItem("login_token") ) );
                }
            })

    },[])


    return(

<div className="header_wrap">
            <div className="menu_box">
                <a href="/"> Home </a>
                <a href="/board/list"> 게시판 </a>
                <a href="/admin/dashboard"> 관리자 </a>
                <a href="/todo"> Doto </a>
                { login == null
                    ? ( <>
                        <a href="/member/login"> 로그인 </a>
                        <a href="/member/signup"> 회원가입 </a>
                    </> )
                    : ( <>
                        <button onClick={ logOut }> 로그아웃 </button>
                    </> )
                }
            </div>
        </div>
    )
}