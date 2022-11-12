package example;

import java.util.ArrayList;
import java.util.Random;

import controller.Solver;
import model.LinearModel;
import model.Objective;
import model.Variable;
import model.constraint.LinearConstraintType;
import model.constraint.LinearConstraint;
import model.matrix.ElementMatrix;

public class LinearProblemExample {

	public static void main(String[] args) {
		try                 { test5();             }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");

		/**/
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
		/**/
		try                 { randomTest( 10000 , 1000 , 1 ); }
		catch (Exception e) { e.printStackTrace(); }
		System.out.println("--------------------------------");
	}

	public static void test(LinearModel model) {

	    Solver solver = new Solver(model);
	    
		long time1 = System.currentTimeMillis();
		solver.execute();
		long time2 = System.currentTimeMillis();

		System.out.println("optimality status = "+solver.checkOptimality());
		System.out.println("value = " + solver.getObjectiveValue());
		
		double[] x = solver.getPrimalSolution();
		for (Variable variable : model.getListOfVariables()) {
		    int index = variable.getIndex();
			System.out.println(variable.getName() + " = " + x[index]);
		}
		//		double[] y = lp.dual();
		//		for (int j = 0; j < y.length; j++) {
		//			System.out.println("y[" + j + "] = " + y[j]);
		//		}
		System.out.println("execution time = "+(time2-time1)+" ms");
	}

	public static void test1() {
	    
	    System.out.println("test1");
		LinearModel model = new LinearModel();

		Variable[] variable = new Variable[3];
		for (int iVariable=0 ; iVariable<3 ; iVariable++) {
			variable[iVariable] = model.newVariable("x["+iVariable+"]");
		}

		LinearConstraint constraint1 = new LinearConstraint(new double[] {-1,1}, new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 5);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {1,4},  new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 45);
        LinearConstraint constraint3 = new LinearConstraint(new double[] {2,1},  new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 27);
        LinearConstraint constraint4 = new LinearConstraint(new double[] {3,-4}, new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 24);
        LinearConstraint constraint5 = new LinearConstraint(new double[] {1},    new Variable[] {variable[2]}               , LinearConstraintType.LEQ , 4);
		
		model.addConstraint(constraint1);
		model.addConstraint(constraint2);
		model.addConstraint(constraint3);
		model.addConstraint(constraint4);
		model.addConstraint(constraint5);

		model.setListOfCosts(new double[] { 1.0 , 1.0, 1.0 });
		model.setObjectif(Objective.MAXIMIZE);

		//        double[][] A = {
		//            { -1,  1,  0 },
		//            {  1,  4,  0 },
		//            {  2,  1,  0 },
		//            {  3, -4,  0 },
		//            {  0,  0,  1 },
		//        };
		//		double[] c = { 1, 1, 1 };
		//		double[] b = { 5, 45, 27, 24, 4 };
		test(model);
	}


	// x0 = 12, x1 = 28, opt = 800
	public static void test2() {
	    System.out.println("test2");
	    LinearModel model = new LinearModel();

	    Variable[] variable = new Variable[2];
	    for (int iVariable=0 ; iVariable<2 ; iVariable++) {
	        variable[iVariable] = model.newVariable("x["+iVariable+"]");
	    }

	    LinearConstraint constraint1 = new LinearConstraint(new double[] {5,15} , new Variable[] {variable[0] , variable[1]}  , LinearConstraintType.LEQ , 480);
	    LinearConstraint constraint2 = new LinearConstraint(new double[] {4,4}  ,  new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 160);
	    LinearConstraint constraint3 = new LinearConstraint(new double[] {35,20}, new Variable[] {variable[0] , variable[1]}  , LinearConstraintType.LEQ , 1190);

	    model.addConstraint(constraint1);
	    model.addConstraint(constraint2);
	    model.addConstraint(constraint3);
	    
	    model.setListOfCosts(new double[] {13,23});
	    model.setObjectif(Objective.MAXIMIZE);

		//		double[] c = {  13.0,  23.0 };
		//		double[] b = { 480.0, 160.0, 1190.0 };
		//
		//        double[][] A = {
		//            {  5.0, 15.0 },
		//            {  4.0,  4.0 },
		//            { 35.0, 20.0 },
		//        };
		test(model);
	}

	// unbounded
	public static void test3() {
	    System.out.println("test3");
        LinearModel model = new LinearModel();

		Variable[] variable = new Variable[4];
		for (int iVariable=0 ; iVariable<4 ; iVariable++) {
			variable[iVariable] = model.newVariable("x"+iVariable);
		}

		LinearConstraint constraint1 = new LinearConstraint(new double[] {-2,-9,1.0,9},   new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , LinearConstraintType.LEQ , 3.0);
		LinearConstraint constraint2 = new LinearConstraint(new double[] {1,1,-1,-2},     new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , LinearConstraintType.LEQ , 2.0);
		
		model.addConstraint(constraint1);
		model.addConstraint(constraint2);

		model.setListOfCosts(new double[] {2.0,3.0,-1.0,-12.0});
		model.setObjectif(Objective.MAXIMIZE);

		//		double[] c = { 2.0, 3.0, -1.0, -12.0 };
		//		double[] b = {  3.0,   2.0 };
		//        double[][] A = {
		//            { -2.0, -9.0,  1.0,  9.0 },
		//            {  1.0,  1.0, -1.0, -2.0 },
		//        };
		test(model);
	}

	// degenerate - cycles if you choose most positive objective function coefficient
	public static void test4() {
	    System.out.println("test4");
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[4];
        for (int iVariable=0 ; iVariable<4 ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
        }

        LinearConstraint constraint1 = new LinearConstraint(new double[] {0.5,-5.5,-2.5,9.0},   new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , LinearConstraintType.LEQ , 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {0.5,-1.5,-0.5,1.0},   new Variable[] {variable[0] , variable[1], variable[2] , variable[3]} , LinearConstraintType.LEQ , 0.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[] {1.0},                     new Variable[] {variable[0]} , LinearConstraintType.LEQ , 1.0);
        
        model.addConstraint(constraint1);
        model.addConstraint(constraint2);
        model.addConstraint(constraint3);
        
		model.setListOfCosts(new double[] {10.0,-57.0,-9.0,-24.0});
		model.setObjectif(Objective.MAXIMIZE);

		//		double[] c = { 10.0, -57.0, -9.0, -24.0 };
		//		double[] b = {  0.0,   0.0,  1.0 };
		//        double[][] A = {
		//            { 0.5, -5.5, -2.5, 9.0 },
		//            { 0.5, -1.5, -0.5, 1.0 },
		//            { 1.0,  0.0,  0.0, 0.0 },
		//        };
		test(model);
	}

	// test with negative coefficient in the matrix : add equation greater than
	public static void test5() {
	    System.out.println("test5");
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[2];
        for (int iVariable=0 ; iVariable<2 ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
        }
		
        LinearConstraint constraint1 = new LinearConstraint(new double[] {1.0,1.0}, new Variable[] {variable[0]  , variable[1]} , LinearConstraintType.LEQ , 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {-1.0},    new Variable[] {variable[0]} ,                LinearConstraintType.LEQ , -1.0);
		
        model.addConstraint(constraint1);
        model.addConstraint(constraint2);
		
		model.setListOfCosts(new double[] {1.0,4.0});
		model.setObjectif(Objective.MAXIMIZE);
		
		//		double[] c = { 1.0 , 4.0 };
		//		double[] b = {  1.0, -1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { -1.0 , 0.0 },
		//        };
		test(model);
	}
	
	// test with an equality equation
	public static void test6() {
	    System.out.println("test6");
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[2];
        for (int iVariable=0 ; iVariable<2 ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
        }
		
        LinearConstraint constraint1 = new LinearConstraint(new double[] {1.0,1.0}, new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {1.0},     new Variable[] {variable[0]} ,               LinearConstraintType.LEQ , 1.0);
        
        model.addConstraint(constraint1);
        model.addConstraint(constraint2);
           
		model.setListOfCosts(new double[] {4.0 , 1.0});
		model.setObjectif(Objective.MAXIMIZE);
		
		//		double[] c = { 0.0 , 4.0 };
		//		double[] b = {  1.0, 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },    equality
		//        };
		test(model);
	}

	// test with an equality equation
	public static void test7() {
	    System.out.println("test7");
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[2];
        for (int iVariable=0 ; iVariable<2 ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
        }
		
        LinearConstraint constraint1 = new LinearConstraint(new double[] {1.0,1.0}, new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 4.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {1.0},     new Variable[] {variable[0]} ,               LinearConstraintType.LEQ , 2.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[] {1.0},     new Variable[] {variable[1]} ,               LinearConstraintType.LEQ , 1.0);
        
        model.addConstraint(constraint1);
        model.addConstraint(constraint2);
        model.addConstraint(constraint3);
		
		model.setListOfCosts(new double[] {1.0 , 2.0});
		model.setObjectif(Objective.MAXIMIZE);
		
		//		double[] c = { 1.0 , 2.0 };
		//		double[] b = { 4.0 , 2.0 , 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },	equality
		//            { 0.0 , 1.0 },    equality
		//        };
		test(model);
	}
	
	// test with geq constraint
	public static void test8() {
	    System.out.println("test8");
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[2];
        for (int iVariable=0 ; iVariable<2 ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
        }
		
        LinearConstraint constraint1 = new LinearConstraint(new double[] {1.0,1.0},     new Variable[] {variable[0] , variable[1]} , LinearConstraintType.LEQ , 4.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {1.0},         new Variable[] {variable[0]} ,               LinearConstraintType.LEQ , 2.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[] {1.0},         new Variable[] {variable[1]} ,               LinearConstraintType.LEQ , 1.0);
        
        model.addConstraint(constraint1);
        model.addConstraint(constraint2);
        model.addConstraint(constraint3);
		
		model.setListOfCosts(new double[] {1.0 , 2.0});
		model.setObjectif(Objective.MAXIMIZE);
		
		//		double[] c = { 1.0 , 2.0 };
		//		double[] b = { 4.0 , 2.0 , 1.0 };
		//        double[][] A = {
		//            { 1.0 , 1.0 },
		//            { 1.0 , 0.0 },	geq
		//            { 0.0 , 1.0 },    equality
		//        };
		test(model);
	}

	public static void randomTest(int M, int N, int seed) {

		Random rand1 = new Random(seed);
        LinearModel model = new LinearModel();

        Variable[] variable = new Variable[N];
        for (int iVariable=0 ; iVariable<N ; iVariable++) {
            variable[iVariable] = model.newVariable("x["+iVariable+"]");
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
		
		int totalNumberOfElements = 0;
		for (int i = 0; i < M; i++) {

			// creation des coefficient au hasard
			ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
			int numberOfElements = 1 + (int) ( 24*rand1.nextFloat() / 2.0D );
			for (int j = i ; j < i+numberOfElements && j<N ; j++) {
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
			totalNumberOfElements += index;
			
			LinearConstraint constraint = new LinearConstraint( listCoefficients , listVariables , LinearConstraintType.LEQ , 101.0D * rand1.nextDouble() );
			model.addConstraint(constraint);
		}
	    System.out.println("randomTest N="+N+"  M="+M+"  numberOfCoeff="+totalNumberOfElements);
		
		//
		// creation de la fonction coût
		//
		model.setListOfCosts( c );
		model.setObjectif(Objective.MAXIMIZE);

		test(model);        
	}
}
