package com.kodnest.tunehub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kodnest.tunehub.entity.Playlist;

@Service
public interface PlaylistService {

	void addplaylist(Playlist playlist);

	List<Playlist> getAllPlaylists();
	
}
