package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kodnest.tunehub.entity.Playlist;
import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.service.PlaylistService;
import com.kodnest.tunehub.service.SongService;

@Controller
public class PlaylistController {

	@Autowired
	SongService songService;

	@Autowired
	PlaylistService playlistService;

//It will fetch all the song objects present in the db and the admin can select which song to add on to the playlist
//This is for the admin
	@GetMapping("/createplaylist")
	public String createPlaylist(Model model) {
		List<Song> songList = songService.fetchAllSongs();
		model.addAttribute("songs", songList);
		return "createplaylist";
	}

	@PostMapping("/addplaylist")
	public String addplaylist(@ModelAttribute Playlist playlist) {
//updating the playlist table
		playlistService.addplaylist(playlist);

//updating the song table
		List<Song> songList = playlist.getSong();
		for (Song s : songList) {
			s.getPlaylists().add(playlist);
			songService.updateSong(s);
		}
		return "adminhome";
	}

	@GetMapping("/viewplaylists")
	public String viewplaylists(Model model) {
		List<Playlist> playlists = playlistService.getAllPlaylists();
		model.addAttribute("playlists", playlists);
		return "viewplaylists";
	}
	

}