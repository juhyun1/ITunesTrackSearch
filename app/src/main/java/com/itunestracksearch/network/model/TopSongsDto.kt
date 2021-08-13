package com.itunestracksearch.network.model

data class TopSongsDto(
    val artistName: String,//:"방탄소년단"
    val id: Int, //1574764723"
    val releaseDate: String,//2021-07-09"
    val name: String,//Permission to Dance"
    val collectionName: String,//Butter / Permission to Dance - EP"
    val kind: String,//song"
    val copyright: String,//℗ 2021 BIGHIT MUSIC"
    val artistId: Int, //883131348"
    val artistUrl: String,//https://music.apple.com/kr/artist/%EB%B0%A9%ED%83%84%EC%86%8C%EB%85%84%EB%8B%A8/883131348?app=music"
    val artworkUrl100: String,//https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/55/14/36/55143686-0d3a-e2d0-361b-388a9f64200d/196006991739.jpg/200x200bb.png"
    val genres: List<GenreDto>,//[{"genreId":"51","name":"K-Pop","url":"https://itunes.apple.com/kr/genre/id51"},{"genreId":"34","name":"음악","url":"https://itunes.apple.com/kr/genre/id34"},{"genreId":"14","name":"팝","url":"https://itunes.apple.com/kr/genre/id14"}]
    val url: String,//https://music.apple.com/kr/album/permission-to-dance/1574764445?i=1574764723\u0026app=music"
)