package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.service.SongService;

@Controller
public class SongController {
	@Autowired
	SongService songService;

	@PostMapping("/addsong")
	public String addSong(@ModelAttribute Song song) {
		boolean songStatus = songService.songExists(song.getName());
		if (songStatus == false) {
			songService.addSong(song);
			System.out.println("New Song Added");
		} else {
			System.out.println("Song already exists!!");
		}
		return "adminhome";
	}

	@GetMapping("/veiwsongs")
	public String veiwSongs(Model model) {
		List<Song> songList= songService.fetchAllSongs();
		model.addAttribute("songs", songList);
		//System.out.println(songList);
		return "displaysongs";
	}
	@GetMapping("/playsongs")
	public String playsongs(Model model) {
		boolean premium = false;
		if (premium) {
			List<Song> songList = songService.fetchAllSongs();
			model.addAttribute("songs", songList);
			return "displaysongs";
		} else {
			return "subscriptionform";
		}
	}
}