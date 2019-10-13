package Prototype1;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class PlanManager {
	
	protected SessionFactory sessionFactory;
	
	protected void setup() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	protected void exit() {
		sessionFactory.close();
	}
	
	protected void create(String username, int foodid) {
		Session session = sessionFactory.openSession();
		
		Plan plan = new Plan();
		plan.setUsername(username);
		plan.setFoodid(foodid);
		plan.setCount(1);
	    
	    Transaction tx=null;
		try {
			tx = session.beginTransaction();
			//Update Plan java object
			plan.setUsername(username);
			plan.setFoodid(foodid);
			plan.setCount(1);
			
			//Save java object Plan to database
			session.save(plan);
			session.flush();
			tx.commit();
		}catch(Exception se) {
			tx.rollback();
		}finally {
			if(session.isOpen()) session.close();
		}
	}
	
	protected String read(String username) {
		Session session = sessionFactory.openSession();
		String result = "";
		
		List<Plan> plans = session.createQuery("from Plan where username='" + username + "'").list();
		
		Object[] plan = plans.toArray();
		
		//Plan[] planArray = (Plan[]) planList.toArray();
		
		
		for (int i=0; i<plan.length; i++) {
		  
		  result += ((Plan) plan[i]).getFoodid() + " " + ((Plan) plan[i]).getCount() + " ";
		}
		System.out.println(result);
		
		//for (int i=0; i<planList.size(); i++) {
		//	result += planArray[i].getFoodid() + " " + planArray[i].getCount() + " ";
		//}
		
		return result;
	}
	
	protected void update() {
		
	}
	
	protected void delete() {
		
	}

}
