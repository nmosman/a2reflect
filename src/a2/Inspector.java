
package a2;

import java.lang.reflect.*;
import java.util.ArrayList;

public class Inspector {
	
	private ArrayList<Integer> objectCodes;
	
	public Inspector()
	{
		objectCodes = new ArrayList<Integer>();
	}
	public boolean hasBeenInspected(Object obj)
	{
		return objectCodes.contains(obj.hashCode());
	}
	
	
	public boolean isWrapperClass(Object obj)
	{
		return (obj instanceof Character ||
		obj instanceof Integer ||
		obj instanceof Float ||
		obj instanceof Long ||
		obj instanceof Short ||
		obj instanceof Double ||
		obj instanceof Byte ||
		obj instanceof Boolean);
	}
	public String getValueByString(Object obj)
	{
		
		if(obj != null)
		{
			// Check if wrapper class
			if(isWrapperClass(obj))
			{
				return String.valueOf(obj);
			}
			
			else
			{
				// Otherwise obj is a reference 
				
				Class elem = obj.getClass();
				String val = elem.getName() + " " + elem.hashCode();
				return val;
			}
		}
		
		return "null";	
	}
	
	public void printObjectArray(Object[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null)
			{
				System.out.println("Array["+ i  + "]: " + getValueByString(array[i]));
			}
		}
	}
	
	private void checkFields(Object obj, Field[] fObj)
	{
		for(Field f: fObj)
		{
			try
			{
				f.setAccessible(true);
				
				String typeString = null;
				String fieldValString = null;
				
				Object val = f.get(obj);
				
				String modifier = Modifier.toString(f.getModifiers());
				
				// check if field is an array
				Class fieldType = f.getType();
				
				if(fieldType.isArray())
				{
					Class arrayType = fieldType.getComponentType();
					int length = Array.getLength(val);
					
					typeString = arrayType.getName() + "[" + length + "]";
					
					System.out.println("Field: " + modifier + " " + typeString + " " + f.getName());
					
					Object oArray[] = new Object[length];
					
					for( int i = 0; i < length; i++)
					{
						oArray[i] = Array.get(val, i);
					}
					
					printObjectArray(oArray);
				}
				else 
				{
					if(fieldType.isPrimitive())
					{
						typeString = fieldType.toString();
						fieldValString = val.toString();
						System.out.println("Field: " + modifier + " " + typeString + " " + f.getName());
						System.out.println(" Value : " + fieldValString);
					}
					else if(val != null)
					{
						// is a reference type and since we are not recursing, just print name and hash code
						fieldValString = val.getClass().getName() +  " " + val.hashCode();
						System.out.println("Field: " + modifier + " " + typeString + " " + f.getName());
						System.out.println(" Value : " + fieldValString);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public void checkSuperClasses(Object obj, Class superClassObj)
	{
		System.out.println();
		System.out.println("CHECKING SUPER CLASS...");
		
		//see if there's another super class...
		Class superSuperClass = superClassObj.getSuperclass();
		if( superSuperClass != null)
		{
			System.out.println("Superclass: " + superSuperClass.getName());
			
		}
		
		//check interfaces, constructors, methods and fields
		if(superClassObj.getInterfaces() != null)
		{
			Class interfaces[] = superClassObj.getInterfaces();
			
			//print interfaces
			System.out.println("Interfaces: ");
			
			for( Class i : interfaces)
			{
				System.out.print(i.getName() + ",");
			}
			System.out.println();
		
		}
		
		
		checkConstructors(superClassObj.getConstructors());
		
		checkMethods(superClassObj.getDeclaredMethods());
		
		checkFields(obj, superClassObj.getDeclaredFields() );
		
		// check up super classes and then interfaces
		
		if(superSuperClass != null)
		{
			System.out.println("The current superclass " + superClassObj.getName() + " has a superclass named " + superSuperClass.getName());
			checkSuperClasses(obj, superSuperClass);
			
			
		}
		else
		{
			System.out.println("The superclass " + superClassObj.getName() + " has no known superclasses");		
		}
		
		// check if there are any interfaces to go up
		Class higherInterfaces[] = superClassObj.getInterfaces();
		if( higherInterfaces.length > 0)
		{
			for(Class i : higherInterfaces)
			{
				System.out.println("The superclass " + superClassObj.getName() + " has the interface " + i.getName() + " '");
				checkInterface(obj, i);
				
			}
		}
		else
		{
			System.out.println("The superclass " + superClassObj.getName() + " has no interfaces");
			
			
		}
		
		System.out.println("END OF CHECKING SUPERCLASS: " + superClassObj.getName());
		
		
	}
	
	
	private void checkConstructors(Constructor[] cObj)
	{
		for(Constructor c : cObj)
		{
			
			Class paramTypes[] = c.getParameterTypes();
			
			String modifier = Modifier.toString(c.getModifiers());
			
			System.out.print("Constructor: " + modifier + " " + c.getName() +"( ");
			
			for (Class p : paramTypes)
			{
				System.out.print(p.getName() + ",");
				
			}
			System.out.print(")");
			System.out.println();
			
		}
	}
	
	private void checkMethods(Method[] mObj)
	{
		for(Method m : mObj)
		{
			
			Class exceptionTypes[] = m.getExceptionTypes();
			Class paramTypes[] = m.getParameterTypes();
			Class returnType = m.getReturnType();
			
			String modifier = Modifier.toString(m.getModifiers());
			
			System.out.print("Method: " + modifier + " " + returnType.getName()
													+ " "  + m.getName()  + "( ");
			
			for (Class p : paramTypes)
			{
				System.out.print(p.getName() + ",");
				
			}
			System.out.print(")");
			
			if(exceptionTypes.length > 0)
			{
				System.out.print(" throws ");
				
				for (Class e : exceptionTypes)
				{
					System.out.print(e.getName() + ",");
					
				}
				System.out.print(")\n");
				
			}
		}
	}
	
	public void checkInterface(Object obj, Class interfaceObj)
	{
		System.out.println("Now checking interface...");
		
		Class interfaceSuperObj = interfaceObj.getSuperclass();
		
		checkMethods(interfaceObj.getDeclaredMethods());
		
		//NOTE: It is technically possible to have fields in interfaces
		//BUT, this is considered bad design practice for OOP, so 
		//I won't be checking through fields with interfaces!
		
		
		
		// check if the interface extends a class
		if(interfaceSuperObj != null)
		{
			System.out.println("The inferface " + interfaceObj.getName() + " has a superclass named " +  interfaceSuperObj.getName());
			checkSuperClasses(obj, interfaceSuperObj);
		}
		
		else
		{
			System.out.println("Interface " + interfaceObj.getName() + " has no superclasses");
		}
		
		
		System.out.println("Done inspecting interface " + interfaceObj.getName() + " ...");
		
	}
	
	private void recursiveFieldInspect(Object obj, Field[] fObj, boolean recursive)
	{
		

		// Print out field Objects
		System.out.println("Now looking at field objects");
		for(Field f : fObj)
		{
			f.setAccessible(true);
			try {
				Class fieldType = f.getType();
				
				
				// Check if its primitive, if yes then simply print out
				if(fieldType.isPrimitive())
				{
					System.out.println("Primitive Type");
					String fieldName = f.getName();
					System.out.println("Field: " + fieldName + " ");
					
					
				}
				// Check if its array and go through all elements if so
				
				else if(fieldType.isArray())
				{
					// Now we have two cases - we either have:
					// An array of primitives (no recursion needed)
					// Or an array of references
					String fieldName = f.getName();
					
					System.out.println("Array Type");
					System.out.println("Field: " + fieldName);
					
					Object value = f.get(obj);
					
					int length = Array.getLength(value);
					
					Object[] oArray = new Object[length];
					for(int i = 0; i < length; i++)
					{
						oArray[i] = Array.get(value, i);
					}
					
					Class arrayType = fieldType.getComponentType();
					
					
					// Check to see if we're dealing with primitive array
					if(arrayType.isPrimitive())
					{
						System.out.println("Primitive Array ");
						//print out array 
					}
					else
					{
						// we have objects in array, so we'll probably need to recurse on these objects sadly
						if(oArray.length <= 0 )
						{
							System.out.println("Empty Array...");
						}
						else
						{
							for(int i = 0; i < oArray.length; i++)
							{
								Object o = oArray[i];
								String oType = "";
								
								
								if(o != null)
								{
									if(!hasBeenInspected(o))
									{
										inspect(o, recursive);
									}
									else
									{
										System.out.println("Object has been inspected before!");
									}
									
								}
								else
								{
									System.out.println("Null Object");
								}
								
							}
						}
					}
				}
				
				// If its a reference type, need to go through that recursively if the recursive flag is true
				
				
				else if(!fieldType.isPrimitive())
				{
					System.out.println("Field: " + f.getName() +  " Reference Type");
					
					//ensure the reference isn't null
					
					if(f.get(obj) != null)
					{
						// let's not inspect the same object again
						if(!hasBeenInspected(obj))
						{
							inspect(obj, recursive);
						}
						
						else
						{
							System.out.println("Object has been inspectead already!");
						}
					}
					else
					{
						System.out.println("Null object");
					}
				}
	
				
			
				
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void checkCurrentClass(Object obj, Class currObj)
	{
		System.out.println("Checking class : " + obj);
		
		Class superClassObj = currObj.getSuperclass();
		System.out.println("Superclass : " + superClassObj.getName());
		
		//interfaces
		
		Class interfaceObj[] = currObj.getInterfaces();
		System.out.println("Interfaces: ");
	
		
		for( Class i : interfaceObj)
		{
			System.out.print(i.getName() + ",");
		}
		System.out.println();
		
		
		Method mObj[] = currObj.getDeclaredMethods();
		checkMethods(mObj);
		
		Constructor cObj[] = currObj.getConstructors();
		checkConstructors(cObj);
		
		
		Field[] fObj = currObj.getDeclaredFields();
		checkFields(obj, fObj);
		
		if(currObj.isArray())
		{
			Class arrayType = currObj.getComponentType();
			System.out.println("Component Type: " + arrayType.getName());
			int length = Array.getLength(obj);
			
			Object oArray[] = new Object[length];
			
			for( int i = 0; i < length; i++)
			{
				oArray[i] = Array.get(obj, i);
			}
			
			printObjectArray(oArray);
		}
	}
	
    public void inspect(Object obj, boolean recursive)
    {
    	objectCodes.add(obj.hashCode());
    	try{
    		Class metaObject = obj.getClass();
    		Field[] fObj = metaObject.getDeclaredFields();
    		
    		
    		
    		// Check current class
    		checkCurrentClass(obj,  metaObject);
    		// Check super class
    		Class superClassObj = metaObject.getSuperclass();
    		
    		checkSuperClasses(obj, superClassObj);
    		
    		
    		// Check interfaces
    		Class [] interfaceObj = metaObject.getInterfaces();
    		for(Class i: interfaceObj)
    		{
    			checkInterface(obj, i);
    		}
    		
    	// Now check to see if we need to recursively check the class
    		if(recursive)
    		{
    			System.out.println("Recursive flag is true; now recursing down class..");
    			recursiveFieldInspect(obj, fObj, recursive);
    			System.out.println("Done recursing through class");
    		}
    	}
    	
    	// 
    	catch(Exception e){
    		
    	}
    }
}
