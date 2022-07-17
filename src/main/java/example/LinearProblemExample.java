package example;

import java.util.ArrayList;
import java.util.Random;

import model.LinearProblem;
import model.Variable;
import model.matrix.ElementMatrix;

public class LinearProblemExample {

	public static void main(String[] args) {
		try                 { test5();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");

		/*
		try                 { test2();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");

		try                 { test3();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");

		try                 { test4();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");

		try                 { test5();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");
		
		try                 { test6();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");
		
		try                 { test7();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");
		
		try                 { test8();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");
    */
//		try                 { randomTest( 10000 , 1000 , 1 ); }
//		catch (Exception e) { e.printStackTrace(); }
//		System.out.println("--------------------------------");
	}

	public static void test(LinearProblem lp) {

		long time1 = System.currentTimeMillis();
		lp.solve();
		long time2 = System.currentTimeMillis();

		System.out.println("optimality status = "+lp.checkOptimality());
		System.out.println("value = " + lp.getObjectiveValue());
		for (Variable variable : lp.getListVariables()) {
			System.out.println(variable.getName() + " = " + lp.getValue(variable));
		}
		//		double[] y = lp.dual();
		//		for (int j = 0; j < y.length; j++) {
		//			System.out.println("y[" + j + "] = " + y[j]);
		//		}
		System.out.println("temps d'exécution = "+(time2-time1));
	}

	public static void test1() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[3];
		for (int iVariable=0 ; iVariable<3 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}

		lp.addLinearConstraintLeq(new double[] {-1,1}, 	new Variable[] {variable[0] , variable[1]}	, 5);
		lp.addLinearConstraintLeq(new double[] {1,4}, 	new Variable[] {variable[0] , variable[1]}	, 45);
		lp.addLinearConstraintLeq(new double[] {2,1}, 	new Variable[] {variable[0] , variable[1]}	, 27);
		lp.addLinearConstraintLeq(new double[] {3,-4}, 	new Variable[] {variable[0] , variable[1]}	, 24);
		lp.addLinearConstraintLeq(new double[] {1}, 	new Variable[] {variable[2]}				, 4);

		lp.addLinearObjectif(new double[] {1,1,1}, new Variable[] {variable[0] , variable[1], variable[2]});
		lp.setMaximizeObjective();

		//        double[][] A = {
		//            { -1,  1,  0 },
		//            {  1,  4,  0 },
		//            {  2,  1,  0 },
		//            {  3, -4,  0 },
		//            {  0,  0,  1 },
		//        };
		//		double[] c = { 1, 1, 1 };
		//		double[] b = { 5, 45, 27, 24, 4 };
		test(lp);
	}


	// x0 = 12, x1 = 28, opt = 800
	public static void test2() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[2];
		for (int iVariable=0 ; iVariable<2 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}

		lp.addLinearConstraintLeq(new double[] {5,15}, 	new Variable[] {variable[0] , variable[1]}	, 480);
		lp.addLinearConstraintLeq(new double[] {4,4}, 	new Variable[] {variable[0] , variable[1]}	, 160);
		lp.addLinearConstraintLeq(new double[] {35,20}, new Variable[] {variable[0] , variable[1]}	, 1190);

		lp.addLinearObjectif(new double[] {13,23}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();

		//		double[] c = {  13.0,  23.0 };
		//		double[] b = { 480.0, 160.0, 1190.0 };
		//
		//        double[][] A = {
		//            {  5.0, 15.0 },
		//            {  4.0,  4.0 },
		//            { 35.0, 20.0 },
		//        };
		test(lp);
	}

	// unbounded
	public static void test3() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[4];
		for (int iVariable=0 ; iVariable<4 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}

		lp.addLinearConstraintLeq(new double[] {-2,-9,1.0,9}, 	new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , 3.0);
		lp.addLinearConstraintLeq(new double[] {1,1,-1,-2}, 	new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , 2.0);

		lp.addLinearObjectif(new double[] {3.0,2.0}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();

		//		double[] c = { 2.0, 3.0, -1.0, -12.0 };
		//		double[] b = {  3.0,   2.0 };
		//        double[][] A = {
		//            { -2.0, -9.0,  1.0,  9.0 },
		//            {  1.0,  1.0, -1.0, -2.0 },
		//        };
		test(lp);
	}

	// degenerate - cycles if you choose most positive objective function coefficient
	public static void test4() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[4];
		for (int iVariable=0 ; iVariable<4 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}

		lp.addLinearConstraintLeq(new double[] {0.5,-5.5,-2.5,9.0}, 	new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , 0.0);
		lp.addLinearConstraintLeq(new double[] {0.5,-1.5,-0.5,1.0}, 	new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , 0.0);
		lp.addLinearConstraintLeq(new double[] {1.0}, 					new Variable[] {variable[0]} , 1.0);

		lp.addLinearObjectif(new double[] {10.0,-57.0,-9.0,-24.0}, new Variable[] {variable[0] , variable[1], variable[2] , variable[3]});
		lp.setMaximizeObjective();

		//		double[] c = { 10.0, -57.0, -9.0, -24.0 };
		//		double[] b = {  0.0,   0.0,  1.0 };
		//        double[][] A = {
		//            { 0.5, -5.5, -2.5, 9.0 },
		//            { 0.5, -1.5, -0.5, 1.0 },
		//            { 1.0,  0.0,  0.0, 0.0 },
		//        };
		test(lp);
	}

	// test with negative coefficient in the matrix : add equation greater than
	public static void test5() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[2];
		for (int iVariable=0 ; iVariable<2 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}
		
		lp.addLinearConstraintLeq(new double[] {1.0,1.0}, 	new Variable[] {variable[0] , variable[1]} , 1.0);
		lp.addLinearConstraintLeq(new double[] {-1.0}, 		new Variable[] {variable[0]} , -1.0);
		
		lp.addLinearObjectif(new double[] {1.0,4.0}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();
		
		//		double[] c = { 1.0 , 4.0 };
		//		double[] b = {  1.0, -1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { -1.0 , 0.0 },
		//        };
		test(lp);
	}
	
	// test with an equality equation
	public static void test6() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[2];
		for (int iVariable=0 ; iVariable<2 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}
		
		lp.addLinearConstraintLeq(new double[] {1.0,1.0}, 	new Variable[] {variable[0] , variable[1]} , 1.0);
		lp.addLinearConstraintEq(new double[] {1.0}, 		new Variable[] {variable[0]} , 1.0);
		
		lp.addLinearObjectif(new double[] {4.0 , 1.0}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();
		
		//		double[] c = { 0.0 , 4.0 };
		//		double[] b = {  1.0, 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },    equality
		//        };
		test(lp);
	}

	// test with an equality equation
	public static void test7() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[2];
		for (int iVariable=0 ; iVariable<2 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}
		
		lp.addLinearConstraintLeq(new double[] {1.0,1.0}, 	new Variable[] {variable[0] , variable[1]} , 4.0);
		lp.addLinearConstraintEq(new double[] {1.0}, 		new Variable[] {variable[0]} , 2.0);
		lp.addLinearConstraintEq(new double[] {1.0}, 		new Variable[] {variable[1]} , 1.0);
		
		lp.addLinearObjectif(new double[] {1.0 , 2.0}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();
		
		//		double[] c = { 1.0 , 2.0 };
		//		double[] b = { 4.0 , 2.0 , 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },	equality
		//            { 0.0 , 1.0 },    equality
		//        };
		test(lp);
	}
	
	// test with geq constraint
	public static void test8() {

		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[2];
		for (int iVariable=0 ; iVariable<2 ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}
		
		lp.addLinearConstraintLeq(new double[] {1.0,1.0}, 	new Variable[] {variable[0] , variable[1]} , 4.0);
		lp.addLinearConstraintGeq(new double[] {1.0}, 		new Variable[] {variable[0]} , 2.0);
		lp.addLinearConstraintEq(new double[] {1.0}, 		new Variable[] {variable[1]} , 1.0);
		
		lp.addLinearObjectif(new double[] {1.0 , 2.0}, new Variable[] {variable[0] , variable[1]});
		lp.setMaximizeObjective();
		
		//		double[] c = { 1.0 , 2.0 };
		//		double[] b = { 4.0 , 2.0 , 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },	geq
		//            { 0.0 , 1.0 },    equality
		//        };
		test(lp);
	}

	public static void randomTest(int M, int N, int seed) {

		Random rand1 = new Random(seed);
		LinearProblem lp = new LinearProblem();

		Variable[] variable = new Variable[N];
		for (int iVariable=0 ; iVariable<N ; iVariable++) {
			variable[iVariable] = lp.newVariable("x"+iVariable);
		}
		// creation des couts dans la fonction objective
		double[] c = new double[N];
		double[] b = new double[M];
		for (int j = 0; j < N; j++) {
			c[j] = 1001.0D * rand1.nextDouble();
		}
		for (int i = 0; i < M; i++) {
			b[i] = 1001.0D * rand1.nextDouble();
		}
		for (int i = 0; i < M; i++) {

			// creation des coefficient au hasard
			ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
			int nbElements = 1 /*+ (int) (6F *rand2.nextFloat())*/;
			for (int j = i ; j < i+nbElements && j<N ; j++) {
				line.add(new ElementMatrix(i, j, 101.0D * rand1.nextDouble()));
			}

			// creation de la contrainte correspondante
			double[] listCoefficients = new double[line.size()];
			Variable[] listVariables = new Variable[line.size()];
			int index = 0;
			for(ElementMatrix element : line) {
				listCoefficients[index] = element.getValue();
				listVariables[index] = variable[element.getColumnIndex()];
				index ++;
			}

			lp.addLinearConstraintLeq(listCoefficients, listVariables, b[i]);
		}
		//
		// creation de la fonction coût
		//
		lp.addLinearObjectif( c , variable );
		lp.setMaximizeObjective();

		test(lp);        
	}
}
