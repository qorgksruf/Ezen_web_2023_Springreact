import React,{useState,useEffect} from 'react';
import axios from 'axios';
import styles from '../../css/member/login.css';

//아이디찾기 (이름,폰으로)
export default function Find( props ){
    console.log("Find페이지열림")

    let [returnId,setreturnId]=useState();
    let [returnpwd,setreturnpwd]=useState();

    //아이디찾기
    const findId=()=>{
        console.log("findId실행")

                 let info={
                   mname:document.querySelector('.mname').value,
                   mphone:document.querySelector('.mphone').value
                  }

                axios
                    .post("/member/findid", info)
                    .then(r=>{
                        console.log("find axios확인")
                        console.log(r);
                        console.log(r.data);
                            if(r.data != ""){
                              setreturnId(`회원님의아이디는${r.data}임`)
                            }else{
                              setreturnId('일치하는 회원정보가없습니다')
                            }

                            //document.querySelector('.work').innerHTML = `${r.data}`
                    })


    }
        //비밀번호찾기
        const findpwd=()=>{
            console.log("findpwd실행")

                     let info={
                       memail:document.querySelector('.memail').value,
                       mphone:document.querySelector('.mphone').value
                      }

                    axios
                        .post("/member/findpassword", info)
                        .then(r=>{
                            console.log("find비번 axios확인")
                            console.log(r);
                            if(r.data !=null){
                                setreturnpwd(`회원님의임시비번은 ${r.data}입니다`)
                            }else{
                                setreturnpwd('아이디와 전화번호가 일치하지 않습니다')
                            }
                        })


        }


   return (<>회원정보찾기[아이디/비밀번호]
                    <h3>아이디찾기페이지입니다</h3>
                    <form>
                      전화번호 :<input type="text" class="mphone" /> <br/>
                      회원이름 :<input type="text" class="mname" /> <br/>
                      <button onClick={findId} type="button">아이디찾기</button>
                      <div>{returnId}</div>
                    </form>

                    <div class="/member/find"></div>


                 <h3>비번찾기페이지임</h3>
                 <form>
                   회원아이디 :<input type="text" class="memail"/> <br/>
                   전화번호 :<input type="text" class="mphone"/> <br/>

                   <button onClick={findpwd} type="button">비밀번호찾기</button>
                   {<div>{returnpwd}</div>}
                 </form>

                 <div class="/member/findpassword"></div>


              </>)



}