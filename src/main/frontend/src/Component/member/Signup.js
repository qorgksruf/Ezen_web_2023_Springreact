import React , { useState , useEffect } from 'react';
import axios from 'axios';

export default function Signup(props) {





    let [ memailMsg , setMemailMsg ] = useState( {} ) ;
    //아이디 중복체크
    const idCheck =(e)=>{
        //1.
       console.log(document.querySelector('.memail').value);
        //2.
       console.log( e.target.value) ;

        axios.get("/member/idcheck",{params: {memail: e.target.value} } )
            .then(res=> {
                console.log(res)

                        if(res.data==true){
                        setMemailMsg( { asd:'사용중인 이메일입니다', yn : false } )

                        }else{
                        setMemailMsg({asd:'사용가능한 이메일입니다',yn:true})
                        }

            })

            .catch(e=>console.log(e))
    }



    //전화번호 중복체크
    let [mphoneMsg,setMphoneMsg]=useState({});

    const phoneCheck=(e)=>{
        console.log('phone중복체크함수접근');

        console.log(document.querySelector('.mphone').value);

        console.log(e.target.value);



        axios.get("/member/phonecheck",{params: {mphone: e.target.value} } )
            .then(res=>{
                console.log(res)
                   if(res.data==true){
                   setMphoneMsg( {asd : '사용중인전화번호입니다ㅠㅠ', yn : false})

                   }else{
                   setMphoneMsg( {asd:'사용가능한전화번호입니다>< ',yn:true})
                   }
            })


    }

    const onSignup= () =>{
                console.log("onSignup open")

        if(memailMsg.yn==true && mphoneMsg.yn==true){


           let info={
            memail:document.querySelector(".memail").value,
            mpassword:document.querySelector(".mpassword").value,
            mname:document.querySelector(".mname").value,
            mphone:document.querySelector(".mphone").value,
           }
           console.log(info)

            //ajax---->axios
            axios
                .post("/member/info",info)
                .then (r =>{
                      console.log(r)
                      if(r.data==true){
                         alert('가입되셨습니다');
                         Window.location.href ="/member/login"; // Window.location.href이동할 경로
                        }else{
                            alert('가입실패 꺼져');
                        }
                    })
                    .catch (err =>{console.log(err) });

        }else{
            alert('가입실패')
        }


        }//const 함수 e


    return(<>
                  <h3>회원가입페이지</h3>
                  <form>
                      아이디[이메일] : <input type="text" className="memail" onChange={idCheck}/>
                       <span>{memailMsg.asd}</span>
                      <br/>
                      비밀번호 :<input type="text" class="mpassword"/> <br/>
                      회원이름 :<input type="text" class="mname"/> <br/>
                      전화번호 :<input type="text" className="mphone" onChange={phoneCheck}/>
                        <span>{mphoneMsg.asd}</span>
                       <br/>
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