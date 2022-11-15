package com.z9devs.prima_prova_hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name 
{
	@Column(name="first_name")
	private String first_name;
	@Column(name="second_name")
	private String second_name;
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getSecond_name() {
		return second_name;
	}
	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}
	@Override
	public String toString() {
		return "Name [first_name=" + first_name + ", second_name=" + second_name + "]";
	}
	
	
}
