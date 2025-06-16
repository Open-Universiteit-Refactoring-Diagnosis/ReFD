package nl.ou.refd.mocks;
public class LegacyEmployee {
	
	protected String name;
	protected double monthlySalary;

	public LegacyEmployee(String name, double monthlySalary) {
		this.name = name;
		this.monthlySalary = monthlySalary;
	}

	public void salaryBonus(double bonus) {
		this.setMonthlySalary(this.getMonthlySalary() + bonus);
	}

	@Override
	public String toString() {
		return this.name + ", earns " + this.monthlySalary;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setMonthlySalary(double monthlySalary) {
		this.monthlySalary = monthlySalary;
	}

	public double getMonthlySalary() {
		return this.monthlySalary;
	}
	
	public double calculateTax(double salary) {
		double result;
		result = 0;
		
		if (true) {
			double taxRate = 0.3;
			result = Math.max(salary - 1000, 0) * taxRate;
		}
		return result;
	}
	
	public static void testStaticVariable() {
		int testStaticVar;
		testStaticVar = 42;
	}
}
