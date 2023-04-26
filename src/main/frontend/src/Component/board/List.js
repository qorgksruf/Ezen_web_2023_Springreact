import React , { useState , useEffect } from 'react';
import axios from 'axios';
import CategoryList from './CategoryList'
/*----------------------table mui---------------------*/
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
//---------------------------------------------------------
import Pagination from '@mui/material/Pagination';


export default function List( props ){

    //1.요청한 게시물 정보를 가지고 있는 리스트 변수 [상태관리변수]
    let[ rows,setRows ] = useState([]) //처음엔 깡통배열임
    let [pageInfo,setPageInfo] = useState({'cno':0, 'page':1 , 'key':'' , 'keyword':'' });
    let [totalPage,setTotalPage] = useState(1);
    let [totalCount,setTotalCount] = useState(1);
    //서버에게 요청하기 [컴포넌트가 처음 생성되었을떄 ]
    useEffect(()=>{
        axios.get('/board',{params:pageInfo}) //0은 전체보기
            .then(res=>{console.log(res);
            setRows(res.data.boardDtoList)
            setTotalPage(res.data.totalPage)
            setTotalCount(res.data.totalCount)

            })
            .catch(err=>{console.log(err);})
    }, [pageInfo]) //pageInfo(cno,page)  변경될때마다 해당 useEffect 실행된다


    //useEffect(()=>{})   : 생성,업데이트
    //useEffect(()=>{},[]):생성될때 1번
    //useEffect(()=>)

    // 3. 카테고리 변경
    const categoryChange = ( cno ) => {
        pageInfo.cno = cno; setPageInfo( {...pageInfo} );
    } // [ ...배열명 ] , { ...객체명 } : 기존 배열/객체의 새로운 메모리 할당

    //4.페이징 번호 선택
    const selectPage = (e,value)=>{
        console.log("value값")
         console.log(value);
         pageInfo.page = value; //버튼이 체인지되었을때 페이지번호 가져와서 상태변수에 대입
       //console.log(e);
       //console.log(e.target);           //button
       //console.log(e.target.value);     //button이라서 value속성 없음
       //console.log(e.target.innerHTML); //해당 button 에서 안에 작성된 html 호출
       console.log(e.target.outerText); //해당 button 에서 밖으로 출력되는 text 호춣
       pageInfo.page = e.target.outerText;
       setPageInfo( {...pageInfo} ) //버튼이 교체 되었을때 페이지번호를 상태변수에 새로고침 [렌더링]
    }

    //검색했을때
    const onSearch=()=>{
       pageInfo.key = document.querySelector('.key').value
       pageInfo.keyword = document.querySelector('.keyword').value
       pageInfo.page=1 //검색했을때 첫 페이지 이동
       setPageInfo( {...pageInfo} )
    }


    return(<>
       <Container>
       <div> 현재 페이지 : { pageInfo.page } 게시물수:{totalCount} </div>
        <div style={{display:'flex', justifyContent:'space-between', align:'center'}}>
           <CategoryList categoryChange = { categoryChange } />
           <a href="/board/write"> <Button variant="outlined">게시물 작성</Button></a>
        </div>
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell align="center" style={{width:'10%'}}>번호</TableCell>
                <TableCell align="center" style={{width:'10%'}} >제목</TableCell>
                <TableCell align="center" style={{width:'10%'}} >작성자</TableCell>
                <TableCell align="center" style={{width:'10%'}} >작성일</TableCell>
                <TableCell align="center" style={{width:'10%'}} >조회수</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow
                  key={row.name}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                  <TableCell align="center" scope="row"> {row.bno} </TableCell>
                  <TableCell align="center"> <a href={"/board/view/"+row.bno}> {row.btitle}</a></TableCell>
                  <TableCell align="center">{row.memail}</TableCell>
                  <TableCell align="center">{row.bdate}</TableCell>
                  <TableCell align="center">{row.bview}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
            <div>
                {/*전체페이지수 API 사용 */}
              <Pagination count={ totalPage } color="primary" onChange={selectPage} />
            </div>

            {/*검색*/}
            <div class="searchBok">
                <select className="key">
                    <option value="btitle"> 제목 </option>
                    <option value="bcontent"> 내용 </option>
                </select>
                <input type="text" className="keyword"/>
                <button type="button" onClick={onSearch}> 검색 </button>
            </div>


       </Container>
    </>);
}

