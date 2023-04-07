console.log("item js열림")


//등록버튼
function regi(){
    console.log("등록버튼")
    let pname = document.querySelector(".productname").value;
    let pcontent = document.querySelector(".productcontent").value;

    console.log(pcontent);

    $.ajax({
        url:"/item/write",
        data:JSON.stringify({"pname":pname,"pcontent":pcontent}),
        contentType:"application/json; charset=utf-8",
        method:"post",
        success:(r)=>{
            console.log(r);
            if(r==true){
            alert("성공")
            print();
            }else{
            alert("실패")
            }
       }
    });


}



//등록된 글 목록 호출
print();
function print(){
console.log("등록버튼")
   let html =`<tr>
                   <th>번호</th>
                   <th>제품설명</th>
                   <th>제품이름</th>
              </tr> `

    $.ajax({    //AJAX을 이용한 @GetMapping에게 요청응답
        url:"/item/get",
        method:"get",
        success:(r)=>{
            console.log("item전체 출력 성공")
            console.log(r);
           r.forEach((e)=>{
            html+=`<tr>
                    <td>${e.pno}</td>
                    <td>${e.pcontent}</td>
                    <td>${e.pname}</td>
                    <td><button onclick="itemdelete(${e.pno})" type="button">삭제</td>
                    <td><button onclick="onupdate(${e.pno})" type="button">수정</td>
                </tr>`
            })
            document.querySelector(".itemtable").innerHTML=html;
       }
    });
}

function itemdelete(pno){
    console.log("삭제버튼")
    $.ajax({
        url:"/item/delete",
        data:{"pno":pno},
        method:"delete",
        success:(r)=>{
            console.log(r);
            if(r==true){
            alert("성공")
            print();
            }else{
            alert("실패")
            }
       }

    });

}

function onupdate(pno){
    let pname = prompt("수정할제목입력해")
    let pcontent =prompt("수정할내용입력해")
    console.log("수정버튼")
    $.ajax({
        url:"/item/update",
        data:JSON.stringify({"pno":pno,"pname":pname,"pcontent":pcontent}),
        contentType:"application/json; charset=utf-8",
        method:"put",
        success:(r)=>{
            console.log(r);
            if(r==true){
            alert("성공")
            print();
            }else{
            alert("실패")
            }
       }
    });
}