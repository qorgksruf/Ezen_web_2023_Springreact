import React from 'react';

export default function Header( props ){
    return(

        <div>
            <a href="/">Home</a>
            <a href="/board/list">게시판</a>
            <a href="/admin/dashboard">관리자</a>
            <a href="/member/login">로그인</a>
            <a href="/member/signup">회원가입</a>

        </div>
    )
}