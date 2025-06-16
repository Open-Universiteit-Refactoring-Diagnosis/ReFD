package nl.ou.refd.mocks;

public class App {
	public static void main(String[] args) throws Exception {
		LegacyEmployee emp1 = new LegacyEmployee("Peter", 2999);
		emp1.salaryBonus(100);
		System.out.println(emp1);
	}
}