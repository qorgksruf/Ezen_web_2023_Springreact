import React,{useState,useEffect} from 'react';
import axios from 'axios';
/*------------------------mui select--------------------*/
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

export default function CategoryList(props){

    let[list,setList]=useState([]);
    useEffect(()=>{
        axios.get('/board/category/list')
            .then(r=>{setList(r.data)});

    },[])

    //선택된 카테고리
    const [category, setCategory] = useState(0);

      const handleChange = (event) => {
        setCategory(event.target.value);
        props.CategoryChange(event.target.value)
      };


    return(<>
            <Box sx={{ minWidth: 120 }}>
              <FormControl style={{width: '200px', marigin:'50px 0px' }}>
                <InputLabel id="demo-simple-select-label">카테고리</InputLabel>
                <Select id="demo-simple-select"
                  value={category}
                  label="카테고리" onChange={handleChange}
                >
                <MenuItem value={0}>전체보기</MenuItem>
                {/*서버로부터 받은 카테고리 리스트를 반복해서 출력*/}
                 {
                    list.map((c)=>{
                        return  <MenuItem value={c.cno}>{c.cname}</MenuItem>
                    })
                 }
                </Select>
              </FormControl>
            </Box>
    </>)
}