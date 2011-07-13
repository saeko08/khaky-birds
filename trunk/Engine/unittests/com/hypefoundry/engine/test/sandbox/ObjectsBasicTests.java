package com.hypefoundry.engine.test.sandbox;

import android.test.AndroidTestCase;


public class ObjectsBasicTests extends AndroidTestCase 
{
	private int m_objectsCreated = 0;
	
	private class MyFirstObject
	{
		public MyFirstObject() 
		{
			m_objectsCreated += 1;
		}
	}
	
	public void testSimpleObjectInstantiation ()
	{	
		assertEquals( 0, m_objectsCreated );	
		new MyFirstObject();
		assertEquals( 1, m_objectsCreated );
		
		MyFirstObject objectNew1 = null;
		assertEquals( 1, m_objectsCreated );
		assertEquals( objectNew1, null );
		
		objectNew1 = new MyFirstObject();
		assertTrue( objectNew1 != null );
		
		assertEquals( 2, m_objectsCreated );
	}
	
	public void testPrimitiveTypes()
	{
		int a = 300;
		assertEquals( 300, a );
		
		long b = 300;
		assertEquals( 300, b );
		
		byte c = (byte) 300;
		assertEquals( 44, c ); // clamped by the cast
		
		float d = 300;
		assertTrue( 300 == d );
		assertEquals( 300.0f, d );
		
		char e = 300;
		assertEquals( 300, e );
	}
	
	// ------------------------------------------------------
	
	String testStr = "";
	
	class Animal
	{
		public Animal( int id )
		{
			testStr += "Animal(int);";
		}
		
		public Animal()
		{
			testStr += "Animal();";
		}
	}
	
	class Cat extends Animal
	{
		public Cat()
		{
			super( 4 );
			testStr += "Cat();";
		}
		
		public Cat( int id )
		{
			testStr += "Cat(int);";
		}
	}
	
	public void testClassTypes()
	{	
		new Animal(9);
		assertEquals ("Animal(int);", testStr);
		
		testStr = "";
		
		new Cat(7);
		assertEquals ("Animal();Cat(int);", testStr);
		
		testStr = "";
		
		new Cat();
		assertEquals ("Animal(int);Cat();", testStr);
	}
	
	// ------------------------------------------------------
	
	class Toy
	{
		void throwAway()
		{
		}
	}
	
	class Bike extends Toy
	{
		private float m_speed;
		
		public Bike ()
		{
			m_speed = 0;
		}
		
		void changeSpeed (float speed)
		{
			m_speed = speed;
		}

		public float getSpeed() 
		{	
			return m_speed;
		}
		
		@Override
		void throwAway()
		{
			changeSpeed(100);
		}
		
	}
	
	class Boy 
	{
		void giveToy (Toy newToy)
		{
			newToy.throwAway();
		}
	}
	
	public void testPolymorphicMethods()
	{	
		Bike mruczek = new Bike();
		assertEquals(0.0f, mruczek.getSpeed());
				
		mruczek.changeSpeed(5);
		assertEquals(5.0f, mruczek.getSpeed());
		
		// ----- World -----
		Boy rafal = new Boy();
		rafal.giveToy(mruczek);

		assertEquals(100.0f, mruczek.getSpeed());
	}
}
