package com.itunestracksearch.network.model

data class ItunesRssDto (
    val title: String, //":"Top Songs",
    val id: String, //https://rss.itunes.apple.com/api/v1/kr/apple-music/top-songs/all/10/explicit.json",
    val copyright: String, //Copyright © 2018 Apple Inc. Все права защищены.",
    val country: String, //kr",
    val icon: String, //http://itunes.apple.com/favicon.ico",
    val updated: String, //2021-07-19T01:18:18.000-07:00",
    val results: List<TopSongsDto>
)