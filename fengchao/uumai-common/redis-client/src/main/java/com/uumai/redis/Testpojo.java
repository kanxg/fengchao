package com.uumai.redis;

import java.io.Serializable;

public class Testpojo implements Serializable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Testpojo [name=" + name + "]";
	}
	
	
}
