console.log("js열림")

function onwrite(){
    console.log("onwrite실행");

    //AJAX 이용한 @PostMapping에게 요청응답
    $.ajax({
        url:"/note/write", //매핑 주소값
        method:"post",      //매핑 HTTP 메소드
        //BODY 값에 JSON형식의 문자열타입// CONTENTType
        data: JSON.stringify( {"ncontents":document.querySelector(".ncontents").value}),
        contentType:"application/json",
        success:(r)=>{
            console.log(r);
            if(r==true){
                alert("글쓰기성공")
                onget();
            }else{
               alert("글쓰기실패")
            }

        }

    });
}//end

//등록된 글 목록 호출


onget()
function onget(){
   let html =`<tr>
                   <th>번호</th>
                   <th>내용</th>
                   <th>비고</th>
              </tr> `

    $.ajax({    //AJAX을 이용한 @GetMapping에게 요청응답
        url:"/note/get",
        method:"get",
        success:(r)=>{
            console.log("성공")
            console.log(r);

            r.forEach((e)=>{
            html+=`<tr>
                    <td>${e.nno}</td>
                    <td>${e.ncontents}</td>
                    <td><button onclick="ondelete(${e.nno})" type="button">삭제</td>
                    <td><button onclick="onupdate(${e.nno})" type="button">수저</td>
                </tr>`
            })
            document.querySelector(".newtable").innerHTML=html;
       }
    });
}


/*function ondelete(nno){
    console.log(nno);
    $.ajax({
        url:"/note/delete",
        method:"delete",
        data: {"nno":nno},
        success:(r)=>{
            console.log(r);
            if(r==true){
                alert("삭제성공")
                onget();
            }else{
                alert("삭제실패")
            }
        }
    });
}*/
function ondelete(nno){
    console.log(nno);
    $.ajax({
        url:"/note/delete",
        method:"delete",
        data: {"nno":nno},
        success:(r)=>{
            console.log(r);
            if(r==true){
                alert("삭제성공")
                onget();
            }else{
                alert("삭제실패")
            }
        }
    });
}



function onupdate(nno){
    let ncontents = prompt("수정할내용");
    console.log(ncontents);
    $.ajax({
        url:"/note/update",
        method:"put",
        data:JSON.stringify({"nno":nno,"ncontents":ncontents} ),
        contentType:"application/json",
        success:(r)=>{
            console.log(r);
            if(r==true){
                alert("글수정성공")
                onget();
            }else{
                alert("글수정실패")
            }
        }
    });

}
