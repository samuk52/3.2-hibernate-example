package com.z9devs.prima_prova_hibernate.entities;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * @author tomas
 *
 */
/**
 * @author tomas
 *
 */
@Entity
@Table(name="authors")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Author 
{
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private Name name;
	@OneToMany(mappedBy="author", fetch=FetchType.LAZY)
	private List<Book> books;
	
	
	/*
	 * Annotazioni:
	 * 
	 * - Entity = serve a indicare un'entità da mappare (può avere prop name)
	 * - Table = viene usato per passare il nome della tabella
	 * - Column = viene usato per passare il nome della colonna
	 * - Id = viene usato per indicare l'id
	 * - Transient = viene usato per indicare un dato che non deve essere
	 * 				 salvato nel deb, un dato temporaneo
	 * - Embeddable = serve per embeddare una classe all'interno di un'entity
	 * - OneToOne = relazione 1 a 1
	 * - OneToMany = relazione 1 a N (aggiungere nuova tabella)
	 * - ManyToOne = relazione N a 1 (non aggiunge nuova tabella)
	 * - ManyToMany = relazione N a N (aggiunge nuova tabella) 
	 */
	
	public int getId() {     
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	
	
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	/*
	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + ", books=" + books + "]";
	}
	
	*/
	
	
	
	
}
