package abstractfactory;

public interface PizzaIngredientFactory {
 
	// TODO - add method signatures
	
	/************** START *******************/
	public Dough createDough();
	public Sauce createSauce();
	public Cheese createCheese();
	public Clams createClam();
	public Veggies[] createVeggies();
	public Pepperoni createPepperoni();
	/**************** END ******************/
 
}
