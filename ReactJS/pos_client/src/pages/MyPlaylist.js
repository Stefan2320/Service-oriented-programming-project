import { useNavigate } from 'react-router-dom';
import React, { useEffect, useState } from "react";
import { Container, Row, Col} from 'react-grid';
import Grid from '@mui/material/Grid';
import './Playlist.css';



const MyPlaylist = ({jwt,role,id}) => {

      const navigate = useNavigate();
      const [playlist, setPlaylist] = useState({});
      const [isLoading, setIsLoading] = useState(true)

      useEffect(() => {

        fetch('http://localhost:8080/playlistcollection/playlist/'+id)
          .then((response) => response.json())
          .then((data) => setPlaylist(data))
         .catch((error) => {
                 if(error.respone.status == 403){
                     alert("Nu ai drepturi sa adaugi artist!")
                     navigate("/home")
                 }
                 if(error.respone.status == 401){
                     navigate("/")
                 }
                 }
                 );

            setIsLoading(false)
      }, []);

      function addPlaylist(){

          navigate("/playlist/post")
      }

      function addSongToPlaylist(){

        navigate("/playlist/song/post")
            }

      return isLoading ? <p>Loading...</p> : (
      <div>
       <div  className="playlist" key={playlist.id}>
          {playlist.name ? <h3>{playlist.name}</h3> : <h2>NO NAME</h2> }
           <ul>
           <hr></hr>
           <p>Liked Songs:</p>
             {!playlist.melodii ? <p>Currently no liked songs</p> : playlist.melodii.map((song) => (
               <li key={song.song}>
                 <a href={song.songLink}>{song.song}</a> by <a href={song.artistLink}>{song.artist}</a>
               </li>
             ))}
             <hr></hr>
             <p>Playlists:</p>
             {playlist.playlists ? playlist.playlists.map((p)=>(
                            <div>
                            <h4 key={p.id}>Playlist name: {p.name}</h4>
                            <ul>
                            {!p.melodii ? <p>No songs in playlist</p>: p.melodii.map((inside) => (
                                 <li key={inside.song}>
                                   <a href={inside.songLink}>{inside.song}</a> by <a href={inside.artistLink}>{inside.artist}</a>
                                 </li>
                               ))}
                            </ul>
                            </div>
                         )):<p>No playlists!</p>}
           </ul>

       </div>
       <button onClick={addPlaylist}>Add new playlist </button><br></br>
       <button onClick={addSongToPlaylist}> Add song to playlist </button>
    </div>
      )
 }


export default MyPlaylist;