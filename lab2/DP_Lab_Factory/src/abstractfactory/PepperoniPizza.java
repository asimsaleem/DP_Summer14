package abstractfactory;

public class PepperoniPizza extends Pizza {
 
	/**** START ***/
	PizzaIngredientFactory ingredientFactory;
	/**** END ****/

	public PepperoniPizza(PizzaIngredientFactory ingredientFactory) {
		// TODO - add required code
		
		/** START **/
		this.ingredientFactory = ingredientFactory;
		/** END **/
	}
 
	@Override
	void prepare() {
		// TODO - implement method
		
		/*** START ***/
		System.out.println("Preparing " + name);
		dough = ingredientFactory.createDough();
		sauce = ingredientFactory.createSauce();
		cheese = ingredientFactory.createCheese();
		pepperoni = ingredientFactory.createPepperoni();
		/*** END ***/
	}
}
