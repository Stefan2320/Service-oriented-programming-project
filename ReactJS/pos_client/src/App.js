import React, { useEffect, useState } from "react";
import Login from './pages/Login';
import Home from './pages/Home';
import AddArtist from './pages/AddArtist';
import AddSong from './pages/AddSong';
import Playlist from './pages/Playlist';
import MyPlaylist from './pages/MyPlaylist';
import NewPlaylist from './pages/NewPlaylist';
import AddSongsToPlaylist from './pages/AddSongsToPlaylist';
import AddSongToArtist from './pages/AddSongToArtist';
import { Routes, Route } from 'react-router-dom';



function App(){

    const [jwt,setJWT] = useState('');
    const [role,setRole] = useState([]);
    const [id,setId] = useState('');

    console.log(jwt)
    console.log(id)
    console.log(role)

    return (

    <Routes>
       <Route path="/" element={<Login setJWT = {setJWT} setId = {setId}  setRole={setRole} />} />
       <Route path="/home" element={<Home setJWT = {setJWT} jwt = {jwt} role = {role} />} />
       <Route path="/artist/post/new" element={<AddArtist jwt={jwt} role={role}/>} />
       <Route path="/song/post/new" element={<AddSong jwt={jwt} role={role}/>} />
       <Route path="/playlist" element={<Playlist jwt={jwt} role={role}/>} />
       <Route path="/myplaylist" element={<MyPlaylist jwt={jwt} role={role} id={id}/>} />
       <Route path="/playlist/post" element={<NewPlaylist jwt={jwt} role={role} id={id}/>} />
       <Route path="/playlist/song/post" element={<AddSongsToPlaylist jwt={jwt} role={role} id={id}/>} />
    </Routes>
    );
}



export default App;