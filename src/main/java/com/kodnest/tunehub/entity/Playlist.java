package com.kodnest.tunehub.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Playlist {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	int id;
	String name;
	@ManyToMany
	List<Song> song;
	public Playlist() {
		super();
	}
	public Playlist(int id, String name, List<Song> song) {
		super();
		this.id = id;
		this.name = name;
		this.song = song;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Song> getSong() {
		return song;
	}
	public void setSong(List<Song> song) {
		this.song = song;
	}
}
