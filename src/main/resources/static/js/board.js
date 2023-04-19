console.log("board js열림")

//1.카테고리 등록
function setCategory(){
    console.log("setCategory()")
    let cname=document.querySelector(".cname").value;
    $.ajax({
        url:"/board/category/write",
        method:"post",
        data:JSON.stringify({"cname":cname}),
        contentType:"application/json",
        success:(r)=>{
            console.log(r)
            if(r==true){
                //document.querySelector(".cname").value="";
                getCategory()
            }
        }//success end

    }) //ajax end

}//setCategory end

//카테고리 모든 출력
getCategory();
function getCategory(){
    console.log("getCategory()")
        $.ajax({
            url:"/board/category/list",
            method:"get",
            //data:JSON.stringify({"cname":cname}),
            //contentType:"application/json",
            success:(r)=>{
                console.log(r)

                let html=`<button onclick="selectorCno(0)" type="button">전체보기</button>`
                for(let cno in r){
                    console.log("키/필드에 저장된 값"+ cno);
                    console.log("키/필드에 저장된 값"+ r.cno);
                    html+=`<button onclick="selectorCno(${cno})" type="button">${r[cno]}</button>`;
                }//for end
                document.querySelector(".categoryListbox").innerHTML=html;

            }//success end
        }) //ajax end

}

//카테고리 선택
let selectCno = 0; //선택된 카테고리 번호[기본값=0(전체보기) ]
function selectorCno(cno){
    console.log(cno+"의 카테고리 선택");
    selectCno=cno;  //이벤트로 선택한 카테고리번호를 전역변수에 대입
    getBoard(cno);
}
//4.게시물쓰기
function setBoard(){
    if(selectCno==0){
        alert('작성할 게시물의 카테고리 선택해주세요');
        return;
    }

      let btitle = document.querySelector(".btitle").value;
      let bcontent = document.querySelector(".bcontent").value;

    $.ajax({
        url:"/board/write",
        method:"post",
        data:JSON.stringify({"btitle":btitle,"bcontent":bcontent,"cno":selectCno}),
        contentType:"application/json",
        success:(r)=>{
            console.log(r);
            if(r==4){
                alert('클쓰기성공');
                document.querySelector(".btitle").value="";
                document.querySelector(".bcontent").value="";
                getBoard(selectCno);
            }else{
                alert('글쓰기실패');
            }
        }
    })
}

//게시물출력 [선택된 카테고리의 게시물 출력]
getBoard(0)
function getBoard(cno){
   selectCno=cno;
        //선택된 카태고리 기준으로 게시물 출력
       $.ajax({
           url:"/board/list",
           method:"get",
           data:{"cno":selectCno},
           //contentType:"application/json",
           success:(r)=>{
               console.log(r);

               let html=`
                           <tr>
                                <th>번호</th>
                                <th>제목</th>
                                <th>작성자</th>
                                <th>작성일</th>
                                <th>조회수</th>
                           </tr>
                        `;
               r.forEach((o)=>{
                  html+=  `
                              <tr>
                                   <td>${o.bno}</td>
                                   <td>
                                   <button onclick="setResponse(${o.bno})" type="button">${o.btitle}</button>
                                   </td>
                                   <td>${o.memail}</td>
                                   <td>${o.bdate}</td>
                                   <td>${o.bview}</td>
                              </tr>
                  `
               })
               document.querySelector(".boardListbox").innerHTML=html;
           }
       })

}

//6.내가 작성한(로그인이 되어있는 가정)게시물
function myboards(){
    console.log("myboards열림 ");
        $.ajax({
            url:"/board/myboards",
            method:"get",
            success:(r)=>{
                console.log(r);
                               let html=`
                                           <tr>
                                                <th>번호</th>
                                                <th>제목</th>
                                                <th>작성자</th>
                                                <th>작성일</th>
                                                <th>조회수</th>
                                           </tr>
                                        `;
                               r.forEach((o)=>{
                                  html+=  `
                                              <tr>
                                                   <td>${o.bno}</td>
                                                   <td>${o.btitle}</td>
                                                   <td>${o.memail}</td>
                                                   <td>${o.bdate}</td>
                                                   <td>${o.bview}</td>
                                              </tr>
                                  `
                               })
                               document.querySelector(".boardListbox").innerHTML=html;

            }
        })
}

//개별개시물출력
function setResponse(bno){
    console.log("setResponse()열림");
    console.log(bno);

    $.ajax({
         url:"/board/print",
         method:"get",
         data:{"bno":bno},
         success:(r)=>{
            console.log(r);
            let html ='';

            html=`
                                <div> 글번호 : ${ r.bno } </div>
                                <div> 작성자 : ${ r.memail } </div>
                                <div> 작성일 : ${ r.bdate } </div>
                                <div> 제목 : ${ r.btitle } </div>
                                <div> 내용 : ${ r.bcontent } </div>
                                <button type="button" onclick="deleteBoard(${r.bno})"> 삭제 </button>
            `

                               document.querySelector(".boardbox").innerHTML=html;
         }
    })
}

//해당게시물 삭제
function deleteBoard(bno){
    console.log("deleteBoard()확인");
    console.log(bno);
        $.ajax({
             url:"/board/delete",
             method:"delete",
             data:{"bno":bno},
             success:(r)=>{
                    console.log(r);
                    if(r==true){
                        alert("삭제성공");
                        getBoard(selectCno);
                    }else{
                        alert("너가쓴거아냐 ㅡㅡ")
                    }
             }
        })
}

/*
    해당 변수의 자료형 확인
    Array:forEach() 가능
        {object,object,object}
    object:forEach() 불가능
        {
           필드명:값,
           필드명:값,
           필드명:값
        }
      object[필드명]:해당 필드의 값 호출


*/