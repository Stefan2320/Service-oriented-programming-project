import { useNavigate } from 'react-router-dom';
import React, { useEffect, useState } from "react";
import { Container, Row, Col} from 'react-grid';
import Grid from '@mui/material/Grid';
import './Playlist.css';

//todo pt melodii goale
const Playlist = ({jwt,role}) => {
     const [playlists, setPlaylists] = useState([]);

     useEffect(() => {
       fetch('http://localhost:8080/playlistcollection/playlist')
         .then((res) => res.json())
         .then((data) => setPlaylists(data._embedded.playlistDTOList))
         .catch((err) => console.log(err));
     }, []);

     return (
       <div>
         {playlists.map((playlist) => (

           <div  className="playlist" key={playlist.id}>
                {playlist.name ? <h3>{playlist.name}</h3> : <h2>NO NAME</h2> }

             <ul>
               {playlist.melodii.map((song) => (
                 <li key={song.song}>
                   <a href={song.songLink}>{song.song}</a> by <a href={song.artistLink}>{song.artist}</a>
                 </li>
               ))}
                {playlist.melodii.map((song) => (
                    <li key={song.song}>
                      <a href={song.songLink}>{song.song}</a> by <a href={song.artistLink}>{song.artist}</a>
                    </li>
                  ))}
                  <hr></hr>
                <h2>Playlists:</h2>
                {playlist.playlists.length ? playlist.playlists.map((p)=>(
                    <div>

                    <h4 key={p.id}>Playlist name: {p.name}</h4>
                    <ul>
                        {p.melodii.map((inside) => (
                                    <li key={inside.song}>
                                      <a href={inside.songLink}>{inside.song}</a> by <a href={inside.artistLink}>{inside.artist}</a>
                                    </li>
                                  ))}
                    </ul>
                    </div>
                )) : <p>Empty</p>}


             </ul>
           </div>



         ))}
       </div>
     );

}

export default Playlist;