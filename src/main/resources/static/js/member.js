console.log("signip js열림");
let memberInfo='';
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
            async:false,
            success:(r)=>{
            console.log("테스트");
            console.log(r);
            memberInfo=r;
                document.querySelector('.infobox').innerHTML=`${r.mname}님`
                document.querySelector('.infobox').innerHTML+=
                            `<a href="/member/logout"><button type="button"> 로그아웃 </button></a>
                            <button onclick="getDelete()" type="button"> 회원탈퇴 </button>`
            }
       })
}
console.log("memberInfo확인");
console.log(memberInfo.mno)
/*
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
*/


//아이디찾기
function findId() {
    console.log("아이디찾기확인");

   let info={
    mname:document.querySelector('.mname').value,
    mphone:document.querySelector('.mphone').value
   }

    $.ajax({
        url:"/member/findid",
        method:"post",
        data:JSON.stringify(info),
        contentType:"application/json",
        success:(r)=>{
             console.log(r);
            document.querySelector('.find').innerHTML=`${r}`
        }
    })

}


//비밀번호찾기
function findpassword() {
    console.log("비밀번호찾기확인");

   let info={
    memail:document.querySelector('.memail').value,
    mphone:document.querySelector('.mphone').value
   }

    $.ajax({
        url:"/member/findpassword",
        method:"post",
        data:JSON.stringify(info),
        contentType:"application/json",
        success:(r)=>{
             console.log(r);
            document.querySelector('.findpasswordy').innerHTML=`${r}`
        }
    })

}


//회원탈퇴
function bye() {
    console.log("회원탈퇴하기버튼 클릭");

    let mpassword=document.querySelector('.mpassword').value;
    let mno=memberInfo.mno;

    console.log("password확인"+mpassword);
    $.ajax({
        url:"/member/bye",
        method:"delete",
        data:{mpassword:mpassword,mno:mno},
        success:(r)=>{
             console.log(r);
             if(r==true){
                alert("회원탈퇴되셨습니다")
             }else{
                alert("탈퇴가되지않았습니다")
             }
        }
    })

}