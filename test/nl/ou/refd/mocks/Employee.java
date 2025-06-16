package nl.ou.refd.mocks;

public class Employee extends LegacyEmployee {
	
	private String workplace;
 
	public Employee(String name, double monthlySalary, String workplace) {
		super(name, monthlySalary);
		this.workplace = workplace;
	}
 
	//#1
	public void salaryBonus(int bonus) {
		this.setMonthlySalary((this.getMonthlySalary() + bonus) * 1.01);
	}

	//#2
	@Override
	public String toString() {
		return this.name + ", earns " + this.monthlySalary + ", works at " + this.getWorkplace();
	}

	//#3
	public void setName(String name) {
		this.name = name;
	}

	//#4
	public String getName() {
		return this.name;
	}

	//#5
	public void setMonthlySalary(double monthlySalary) {
		this.monthlySalary = monthlySalary;
	}

	//#6
	public double getMonthlySalary() {
		return this.monthlySalary;
	}

	//#7
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	//#8
	public String getWorkplace() {
		return this.workplace;
	}
}
