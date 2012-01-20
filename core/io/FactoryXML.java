package core.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FactoryXML {

	public static Object newInstance(Node xml) {
		try {
			Class<?> c = Class.forName(xml.getNodeName());
			Constructor<?> constructor = 
				c.getConstructor(new Class[] { Class.forName("org.w3c.dom.Element") });
			return constructor.newInstance(xml);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
