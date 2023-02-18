import { useNavigate } from 'react-router-dom';
import { XMLParser } from 'react-xml-parser';
import axios from 'axios';
import React, { useEffect, useState } from "react";
import Song from './Song'
import { Container, Row, Col } from 'react-grid';
import Grid from '@mui/material/Grid';



const AddArtist = ({jwt,role}) =>{

  const navigate = useNavigate();
  const [isContent, setIsContent] = useState(false);
  const [isArtist, setIsArtist] = useState(false);
  useEffect(() => {

        if(!jwt)
            navigate("/")
          for( let i = 0 ; i < role.length ;i++){
                if(role[i] == '2')
                    setIsContent(true)
                if(role[i] == '3')
                    setIsArtist(true)
        }
          setIsArtist(true);
  }, []);

const  handleSubmit = (e) =>{

          e.preventDefault();
          const name = e.target.elements.artists_name.value
          const state = e.target.elements.artists_state.value
          const uid = e.target.elements.uuid.value
            const requestOptions = {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' ,'Authorization': jwt},
                    body: JSON.stringify({ artists_name:name,artists_state:state})
                };
                fetch('http://localhost:8081/songcollection/artists'+uid, requestOptions)
                    .then(response => response.json())
                    .then(data => console.log(data)).catch((error) => {
                    if(error.respone.status == 403){
                        alert("Nu ai drepturi sa adaugi artist!")
                        navigate("/home")
                    }
                    if(error.respone.status == 401){
                        navigate("/")
                    }

                    }
                    )
           navigate("/home")
}
return isArtist ? (
 <div>
         <form onSubmit={handleSubmit} >
             <label for="artists_name">Artist name:</label><br></br>
             <input type="text" id="artists_name" name="artists_name"></input><br></br>
             <label for="artists_state">Artist state (1 or 0) :</label><br></br>
             <input type="pass" id="artists_state" name="artists_state"></input><br></br>
             <label for="uuid">UID:</label><br></br>
             <input type="text" id="uuid" name="uuid"></input><br></br>
             <button type="submit">Submit</button>
         </form>
    </div>
): <p>403 FORBIDDEN</p>
}
export default AddArtist;