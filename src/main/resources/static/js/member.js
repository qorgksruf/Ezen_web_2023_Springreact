console.log("signip js열림");
//회원가입
function onSignup() {
    console.log("확인확인");

   let info={
    memail:document.querySelector(".memail").value,
    mpassword:document.querySelector(".mpassword").value,
    mname:document.querySelector(".mname").value,
    mphone:document.querySelector(".mphone").value,
   }

    $.ajax({
        url:"/member/info",
        method:"post",
        data:JSON.stringify(info),
        contentType:"application/json",
        success:(r)=>{
             console.log(r);
                  if(r==true){
                      alert("가입이되셨습니다");
                      location.href="/"
                  }else{
                  alert("가입이실패하셨습니다");
               }
        }
    })

}

//로그인인데 시큐리티 사용하므로 폼 전송으로 로그인 요청
/*function onLogin() {
    console.log("확인확인");

   let info={
    memail:document.querySelector(".memail").value,
    mpassword:document.querySelector(".mpassword").value,
   }

    $.ajax({
        url:"/member/login",
        method:"post",
        data:JSON.stringify(info),
        contentType:"application/json",
        success:(r)=>{
             console.log(r);
             if(r==true){
                alert("로그인되셨습니다")
                location.href="/"
             }else{
                alert("로그인실패하셨습니다")
             }
        }
    })

}*/
//전체출력
getMember();
function getMember(){
       $.ajax({
            url:"/member/info",
            method:"get",
            contentType:"application/json",
            success:(r)=>{
            console.log("테스트");
            console.log(r);
                document.querySelector('.infobox').innerHTML=`${r.mname}님`
                document.querySelector('.infobox').innerHTML+=`<button onclick="getLogout()" type="button">로그아웃</button>`
            }
       })
}
//로그아웃
function getLogout(){
    console.log("로구아웃")
    $.ajax({
        url:"/member/logout",
        method:"get",
        //contentType:"application/json",
        success:(r)=>{
            console.log(r);
            alert("로그아웃완료됨")
               location.href="/"
        }

     })
}

/*
//아이디찾기
function findId() {
    console.log("아이디찾기확인");

   let info={
    mname:document.querySelector(".mname").value,
    mphone:document.querySelector(".mphone").value,
   }

    $.ajax({
        url:"/member/info",
        method:"get",
        data:JSON.stringify(info),
        contentType:"application/json",
        success:(r)=>{
             console.log(r);
                  if(r==true){
                      alert("통신완료");
                  }else{
                  alert("통신실패");
               }
        }
    })

}*/