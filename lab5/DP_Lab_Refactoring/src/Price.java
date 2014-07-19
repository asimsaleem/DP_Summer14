public abstract class Price {

	public Price() {
		super();
	}

	public abstract int getPriceCode();
	
	protected abstract double getCharge(int daysRented);

	int getFrequentRenterPoints(int daysRented) {
		return 1;
	}
}