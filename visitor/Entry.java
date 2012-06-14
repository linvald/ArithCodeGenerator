/*
 * Created on 07-10-2003
 * 
 */
package visitor;

/**
 * @author Jesper Linvald
 * @version
 * 
 * Description:
 */
public class Entry {
	public Integer inte;
	public String name;
	
	public Entry(){}
	
	public Entry(Integer kind, String name){
		this.inte = kind;
		this.name = name;
	}

	
}
