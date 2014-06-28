package abstractfactory;

public class VeggiePizza extends Pizza{
 
	/**** START ***/
	PizzaIngredientFactory ingredientFactory;
	/**** END ****/
 	
	public VeggiePizza(PizzaIngredientFactory ingredientFactory) {
		// TODO - add required code
		
		/********* START ***********/
		this.ingredientFactory = ingredientFactory;
		/********* END *************/
	}
 
	void prepare() {
		// TODO - implement method
		
		/*** START ***/
		System.out.println("Preparing " + name);
		
		dough = ingredientFactory.createDough();
		sauce = ingredientFactory.createSauce();
		cheese = ingredientFactory.createCheese();
		veggies = ingredientFactory.createVeggies();
		
		/*** END ***/
	}
}
