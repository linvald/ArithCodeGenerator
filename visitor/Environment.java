/*
 * Created on 11-10-2003 by jesper
 * This class should 
 */
package visitor;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author jesper
 */
public class Environment {
	private ArrayList entries = new ArrayList();
	
		public Environment(){}

		
	
		
		public void addEntry(Entry entry){
			entries.add(entry);
		}
		public Entry retrieve(String id){
			Entry entry = new Entry();
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				entry = (Entry) iter.next();
				if(entry.name.equals(id)){
					return entry;
				}
			}
			return entry;
		}
		public void clearTable(){
			this.entries.clear();
		}
}
