package a2;

public class Inspector {
    public void inspect(Object obj, boolean recursive)
    {
    	try{
    		Class metaObject = obj.getClass();
    		Field[] fieldObjects = metaObject.getDeclaredFields();
    		
    		
    		// Print out field Objects
    		System.out.println("Now looking at field objects");
    		for(Field f : fieldObjects)
    		{
    			try {
    				Class fieldType = f.getType();
    				
    				// Check if its array and go through all elements if so
    				
    				// Check if its primitive, if yes then simply print out
    				
    				// If its a reference type, need to go through that recursively if the recursive flag is true
    				
    				
    			}
    			
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	catch(Exception e){
    		
    	}
    }
}
