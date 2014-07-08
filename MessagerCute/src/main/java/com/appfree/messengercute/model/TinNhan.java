package com.appfree.messengercute.model;

public class TinNhan {
	public int id;
	public String noidung;
	public String loaitinnhan;

	public TinNhan(int id, String noidung, String loaitinnhan) {
		super();
		this.id = id;
		this.noidung = noidung;
		this.loaitinnhan = loaitinnhan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoidung() {
		return noidung;
	}

	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}

	public String getLoaitinnhan() {
		return loaitinnhan;
	}

	public void setLoaitinnhan(String loaitinnhan) {
		this.loaitinnhan = loaitinnhan;
	}
}
