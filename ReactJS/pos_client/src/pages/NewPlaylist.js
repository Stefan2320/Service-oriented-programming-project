import { useNavigate } from 'react-router-dom';

const NewPlaylist = ({jwt,role,id}) => {

    const navigate = useNavigate();
    const  handleSubmit = (e) =>{


              e.preventDefault();
              const name = e.target.elements.playlist_name.value
              const melodii = []

                const requestOptions = {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' ,'Authorization': jwt},
                        body: JSON.stringify({  name : name,
                                                melodii: melodii
                                               })
                    };
                    fetch('http://localhost:8080/playlistcollection/'+id+'/playlist', requestOptions)
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
                        );
                    navigate("/home")

    }

    return(
       <div>
                <form onSubmit={handleSubmit} >
                    <label for="playlist_name">Playlist name:</label><br></br>
                    <input type="text" id="playlist_name" name="playlist_name"></input><br></br>
                    <button type="submit">Submit</button>
                </form>
           </div>
    )

}

export default NewPlaylist;
