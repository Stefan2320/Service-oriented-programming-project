




const Song = ({name,genre,year}) => {
    return(
    <div>
        <h1>{name}</h1>
        <p>{genre}</p>
        <p>{year}</p>
    </div>
    );
}

export default Song;