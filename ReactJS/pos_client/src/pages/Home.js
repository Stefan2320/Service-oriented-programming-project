import { useNavigate } from 'react-router-dom';
import { XMLParser } from 'react-xml-parser';
import axios from 'axios';
import React, { useEffect, useState } from "react";
import Song from './Song'
import { Container, Row, Col } from 'react-grid';
import Grid from '@mui/material/Grid';
//import Item from '@mui/material';



function invalidateJWT(jwt){

     var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.bd.soap">' +
                                    '<soap11env:Body>' +
                                        '<sample:logout>'+
                                            '<sample:jwt>'+  jwt +'</sample:jwt>' +
                                        '</sample:logout>' +
                                    '</soap11env:Body>' +
                                '</soap11env:Envelope>';
                      var xmlhttp = new XMLHttpRequest();
                      var url = "http://127.0.0.1:8000"
                      xmlhttp.open('POST',url);
                      xmlhttp.send(sr);


        }




const ObjectTest = {
  1: {
      id : 1,
      name:'ABC'
  },
  3: {
      id: 3,
      name:'DEF'
  }
}

const Home = ({setJWT,jwt,role}) => {

    const navigate = useNavigate();
    const [data, setData] = useState();
    const [isLoading, setIsLoading] = useState(true)
    const [isContent, setIsContent] = useState(false)
    const [isArtist, setIsArtist] = useState(false)
    const [isClient, setIsClient] = useState(false)
    const element_number = 0;

      useEffect(() => {

        if(!jwt)
            navigate("/")

        const dataFetch = async () => {
          const data = await (
            await fetch(
              "http://localhost:8081/songcollection/songs"
            )
          ).json();
          setData(data);
          for( let i = 0 ; i < role.length ;i++){
                if(role[i] == '2')
                    setIsContent(true)
                if(role[i] == '3')
                    setIsArtist(true)
                if(role[i] == '4')
                    setIsClient(true)
        }

          setIsLoading(false);
        };
        dataFetch();
  }, []);

    function goToPlaylist(){
            navigate('/Playlist')
        }
    function goToMyPlaylist(){
                navigate('/myplaylist')
            }
    function addArtist(){
        navigate('/artist/post/new')
    }

    function addSong(){
            navigate('/song/post/new')
        }

    function deleteJWT(){

         invalidateJWT(jwt)
         setJWT('');
         navigate('/');
    }



    return isLoading ? <p>Loading</p> : (
        <div>
            <button onClick={deleteJWT}> Logout </button>
            <div>
             <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 1, md: 1 }}>
               {
               Object.keys(data._embedded.songDTOList).map((key)=>{
                return (
                 <Grid item xs={1}>
                    <h5>Song name is {data._embedded.songDTOList[key].songs_name}</h5>
                 </Grid>
                )
               })
               }
            </Grid>
            </div>
            <div>
            {   isContent ? (
                <button onClick={addArtist}>Add artist</button>
            ) :(<p></p>)
            }
            </div>
            <div>
            {
                isContent ? (
                <button onClick={addSong}>Add songs/albums</button>
            ) :(<p></p>)
            }
            </div>
            <div>
            {
                isClient ? (
                                <button onClick={goToPlaylist}>All Playlists</button>
                            ) :(<p></p>)
            }
            </div>
            <div>
                {
                    isContent ? (
                                    <button onClick={goToMyPlaylist}>My playlist</button>
                                ) :(<p></p>)
                }
            </div>


        </div>
    );

}


//{for(const d in data._embedded.songDTOList)}
//            <Song name={d.songs_name} genre={d.songs_genre} year={d.songs_year.f} />
//<Song> name={{data._embedded.songDTOList[key].songs_name}} genre={{data._embedded.songDTOList[key].songs_genre}} year={{data._embedded.songDTOList[key].songs_year}}</Song>
export default Home;