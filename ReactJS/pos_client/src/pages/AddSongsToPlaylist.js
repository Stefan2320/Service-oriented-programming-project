import { useNavigate } from 'react-router-dom';











const AddSongsToPlaylist =({jwt,role,id})=> {

    const navigate = useNavigate();

     const  handleSubmit = (e) =>{

             e.preventDefault();
            const playlist = e.target.elements.playlist_name.value
            const name = e.target.elements.song_name.value
            var artist_name = e.target.elements.artist_name.value
            if(!artist_name)
               artist_name = "empty"

                 const requestOptions = {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' ,'Authorization': jwt},
                        body: JSON.stringify({  song : name,
                                                artist: artist_name
                                               })
                    };
                    fetch('http://localhost:8080/playlistcollection/'+id+"/playlists/"+playlist+"?option=add", requestOptions)
                        .then(response => response.json())
                        .then(data => console.log(data)).catch((error) => {
                         if(error.respone.status == 403){
                             alert("Nu ai drepturi sa adaugi artist!")
                             navigate("/home")
                         }
                         if(error.respone.status == 401){
                             navigate("/")
                         }

                         });
                     navigate("/home")
        }
    return(
         <div>
            <form onSubmit={handleSubmit} >

                <label for="playlist_name">Playlist name:</label><br></br>
                <input type="text" id="playlist_name" name="playlist_name"></input><br></br>

                <label for="song_name">Song name:</label><br></br>
                <input type="text" id="song_name" name="song_name"></input><br></br>

                <label for="artist_name">Artist name(Optional):</label><br></br>
                <input type="text" id="artist_name" name="artist_name"></input><br></br>

                <button type="submit">Submit</button>
            </form>
       </div>
    )

}

export default AddSongsToPlaylist;