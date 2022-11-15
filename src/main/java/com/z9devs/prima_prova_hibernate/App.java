package com.z9devs.prima_prova_hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

import com.z9devs.prima_prova_hibernate.entities.Author;
import com.z9devs.prima_prova_hibernate.entities.Book;
import com.z9devs.prima_prova_hibernate.entities.Name;

public class App 
{
    public static void main( String[] args )
    {   
    	// https://docs.jboss.org/hibernate/orm/3.5/api/org/hibernate/cfg/Configuration.html
    	// https://stackoverflow.com/questions/18736594/location-of-hibernate-cfg-xml-in-project
        Configuration con = new Configuration()
        		.configure()
        		.addAnnotatedClass(Book.class)
        		.addAnnotatedClass(Author.class);
        
        // https://docs.jboss.org/hibernate/orm/4.3/javadocs/org/hibernate/service/ServiceRegistry.html
        // https://www.youtube.com/watch?v=IQBcEThMRrY
        ServiceRegistry reg = new StandardServiceRegistryBuilder()
        		.applySettings(con.getProperties())
        		.build();

        SessionFactory sf = con.buildSessionFactory(reg);
        
        Session session = sf.openSession();
        
        
        Session session2 = sf.openSession();
        
        Statistics stats = sf.getStatistics();
        stats.setStatisticsEnabled(true);
        
        
        
        // read(1, session);
        //readAuthor(2, session);
        
        //firstLevelCachingExample(session, 1, 1);
        secondLevelCachingExampleQuery(session, session2, 2, 2);
        // hqlExample(session);
        //hqlExampleSelectColumns(session);
        // hqlExampleSelectSingleColumn(session);
        // sqlExample(session);
        // sqlColumnsExample(session );
        
        // stats.logSummary();
        System.out.println(stats.getEntityLoadCount());
        // readAuthors(session);
        // createBookOfAuthor(22, "Gomorra", 2006, session);
    }
    
    private static void createAuthor(int id, String first_name, String second_name, Session s) {
    	Author a = new Author();
    	
    	Name n = new Name();
    	n.setFirst_name(first_name);
    	n.setSecond_name(second_name);

        a.setId(id);
        a.setName(n);
        
        Transaction tx = s.beginTransaction();
        s.save(a);
        
        tx.commit();
        s.close();
    }
    
    private static void readAuthor(int id, Session s) {
    	Transaction tx = s.beginTransaction();
    	try {
    		Author a = s.get(Author.class, id);
    		tx.commit();
    		System.out.println(a.getName().getFirst_name() + " " + a.getName().getSecond_name()+":\n");
            
            for(Book b : a.getBooks()) {
            	System.out.println("- " + b.getTitle());
            }
    	} catch (Exception e) {
    		tx.rollback();
    	} finally {
			s.close();
			
		}
        
        
        
    }
    
    private static void readAuthors(Session s) {
    	List<Author> result=null;
    	Transaction tx = s.beginTransaction();
    	Query q = s.createQuery("from Author where first_name = 'Primo'");
    	result=q.list();
    	tx.commit();
    	
    	for(Author a : result)
    		System.out.println(a.getName().getSecond_name());
    }
    
   private static void readBook(int id, Session s) {
	   Transaction tx = s.beginTransaction();
       Book b = s.get(Book.class, id);
       
       tx.commit();
       System.out.println(b);
   }
   
   private static void createBookOfAuthor(int authorId, String bookTitle, int bookYear, Session s) {
	   Transaction tx = s.beginTransaction();
	   Author a = s.get(Author.class, authorId);
	   Book b = new Book();
	   b.setTitle(bookTitle);
	   b.setYear(bookYear);
	   b.setAuthor(a);
	   s.save(b);
	   tx.commit();
   }
   
   private static void firstLevelCachingExample(Session s, int id1, int id2) {
	   Transaction tx = s.beginTransaction();
	   Author a = s.get(Author.class, id1);
	   Author b = s.get(Author.class, id2);
	   
	   tx.commit();
	   
	   System.out.println(a.getName().getSecond_name());
	   System.out.println(b.getName().getSecond_name());
	   
   }
   
   private static void secondLevelCachingExample(Session s1, Session s2, int id1, int id2) 
   {
	   s1.beginTransaction();
	   Author a = s1.get(Author.class, id1);
	   System.out.println(a.getName().getSecond_name());
	   s1.getTransaction().commit();
	   s1.close();
	   
	   s2.beginTransaction();
	   Author a2 = s2.get(Author.class, id2);
	   System.out.println(a2.getName().getSecond_name());
	   s2.getTransaction().commit();
	   s2.close();
	   
   }
   
   private static void secondLevelCachingExampleQuery(Session s1, Session s2, int id1, int id2) 
   {
	   s1.beginTransaction();
	   Query q1 = s1.createQuery("from Author where second_name='ballard'");
	   q1.setCacheable(true);
	   Author a = (Author) q1.uniqueResult();
	   System.out.println(a.getName().getSecond_name());
	   s1.getTransaction().commit();
	   s1.close();
	   
	   s2.beginTransaction();
	   Query q2 = s2.createQuery("from Author where second_name='ballard'");
	   q2.setCacheable(true);
	   Author a2 = (Author) q2.uniqueResult();
	   System.out.println(a2.getName().getSecond_name());
	   s2.getTransaction().commit();
	   s2.close();
	   
   }
   
   private static void hqlExample(Session s) {
	   s.beginTransaction();
	   Query q = s.createQuery("from Author");
	   List<Author> l = q.list();
	   s.getTransaction().commit();
	   for(Author a : l) {
		   System.out.println(a.getName().getSecond_name());
	   }
   }
   
   private static void hqlExampleSelectColumns(Session s) {
	   s.beginTransaction();
	   
	   Query q =s.createQuery("select id, name from Author");
	   List<Object[]> authors = (List<Object[]>) q.list();
	   
	   for(Object[] a : authors)
	   {
		   System.out.println(((Name) a[1]).getFirst_name());
	   }
	   
	   s.getTransaction().commit();
   }
   
   private static void hqlExampleSelectSingleColumn(Session s) {
	   s.beginTransaction();
	   
	   Query q =s.createQuery("select name from Author where first_name like :b");
	   q.setParameter("b", "%pri%");
	   List<Name> authors = (List<Name>) q.list();
	   
	   for(Name a : authors)
	   {
		   System.out.println(a.getFirst_name());
	   }
	   
	   s.getTransaction().commit();
   }
   
   private static void sqlExample(Session s ) {
	   s.beginTransaction();
	   
	   NativeQuery q = s.createSQLQuery("select * from authors");
	   q.addEntity(Author.class);
	   List<Author> a = q.list();
	   
	   for(Author o : a) {
		   System.out.println(o.getName().getFirst_name());
	   }
	   s.getTransaction().commit();
   }
   
   private static void sqlColumnsExample(Session s ) {
	   s.beginTransaction();
	   
	   NativeQuery q = s.createSQLQuery("select id, first_name from authors");
	   q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	   List a = q.list();
	   
	   for(Object o : a) {
		   Map m = (Map) o;
		   System.out.println(m.get("first_name"));
	   }
	   s.getTransaction().commit();
   }
   
   private static void objectStateExample(Session s) {
	  s.beginTransaction();
	  Name n = new Name();
	  n.setFirst_name("Chuck");
	  n.setSecond_name("Palahniuk");
	  
	  // Ora l'oggetto è in TRANSIENT state:
	  // oggetto esiste in java ma se chiudo java l'oggetto si 
	  // perde
	  Author a = new Author();
	  a.setName(n);
	  
	  // In questo modo salvo l'oggetto che passa in PERSISTENT
	  // state
	  s.save(a);
	  
	  // Ora cambio il nome, mi aspetterei di vedere il cambiamento
	  // solo lato java e non sul db, però in realtà cambia anche 
	  // sul db preché ora l'oggetto è in persistent state, quindi
	  // qualsiasi modifica qui su java si riflette sul db
	  n.setFirst_name("Mario");
	  a.setName(n);
	  
	  
	  s.getTransaction().commit();
	  
	  // Con detach, l'oggetto diventa in DETACHED state, il che 
	  // vuol dire che, se lo modifico su java, non avverranno 
	  // le modifiche sul db, come avverrebbe se l'oggetto fosse
	  // in persistent state
	  s.detach(a);
	  n.setFirst_name("Giulio");
	  a.setName(n);
	  
	  // Il metodo remove porta l'oggetto instato REMOVED
	  s.remove(a);
   }
   
   private static void getVsLoad(Session s) {
	   s.beginTransaction();
	   
	   s.getTransaction().commit();
   }
}
