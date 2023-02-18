import { useNavigate } from 'react-router-dom';





const AddSong = ({jwt,role}) =>{

    const navigate = useNavigate();
    const  handleSubmit = (e) =>{

              e.preventDefault();
              const name = e.target.elements.song_name.value
              const genre = e.target.elements.songs_genre.value
              const year = e.target.elements.songs_year.value
              const type = e.target.elements.song_type.value
              var album = e.target.elements.album_id.value
                if(!album)
                    album = null
                const requestOptions = {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' ,'Authorization': jwt},
                        body: JSON.stringify({  songs_name : name,
                                                songs_genre: genre,
                                                songs_year : year,
                                                songs_type : type,
                                                album_id : album})
                    };
                    fetch('http://localhost:8081/songcollection/songs', requestOptions)
                        .then(response => response.json())
                        .then(data => console.log(data)).catch((error) => {
                         if(error.respone.status == 403){
                             alert("Nu ai drepturi sa adaugi artist!")
                             navigate("/home")
                         }
                         if(error.respone.status == 401){
                             navigate("/")
                         }

                         })
                        navigate("/home")
    }

    return(
       <div>
                <form onSubmit={handleSubmit} >
                    <label for="song_name">Song/Album name:</label><br></br>
                    <input type="text" id="song_name" name="song_name"></input><br></br>
                    <label for="songs_genre">Genre ( "Hip Hop","Rock","Jazz","Pop" ) :</label><br></br>
                    <input type="pass" id="songs_genre" name="songs_genre"></input><br></br>
                    <label for="songs_year">Year:</label><br></br>
                    <input type="text" id="songs_year" name="songs_year"></input><br></br>
                     <label for="song_type">Song or album (Enter "song" or "album"):</label><br></br>
                    <input type="text" id="song_type" name="song_type"></input><br></br>
                    <label for="album_id">Album id (Optional for songs and not needed for albums) :</label><br></br>
                    <input type="text" id="album_id" name="album_id"></input><br></br>
                    <button type="submit">Submit</button>
                </form>
           </div>
    )


}

export default AddSong;