import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.ElementMatrix;
import model.SparseMatrix;
import service.SimplexService;

public class SimplexTests {

    public static void main(String[] args) {
        test3();
    }
    
	public static void test(SparseMatrix A, double[] b, double[] c) {

		long time1 = System.currentTimeMillis();
		SimplexService lp = new SimplexService(A, b, c, null);
		try {
            lp.solve();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		long time2 = System.currentTimeMillis();
		System.out.println("optimality status = "+lp.checkSolution(A, b, c));
		System.out.println("value = " + lp.value());
		double[] x = lp.primal();
		
		for (int i = 0; i < x.length; i++) {
			System.out.println("x[" + i + "] = " + x[i]);
		}
//		double[] y = lp.dual();
//		for (int j = 0; j < y.length; j++) {
//			System.out.println("y[" + j + "] = " + y[j]);
//		}
		System.out.println("temps d'exécution = "+(time2-time1));
	}

	@Test
	@DisplayName("basic test 1 with 3 variables and 5 constraints.")
	public static void test1() {
	    
		SparseMatrix A = new SparseMatrix();
		ArrayList<ElementMatrix> line0 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line1 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line2 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line3 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line4 = new ArrayList<ElementMatrix>();
		line0.add(new ElementMatrix(0,0,-1.0));		line0.add(new ElementMatrix(0,1,1.0));		/*line0.add(new ElementMatrix(0,2,0.0));*/
		line1.add(new ElementMatrix(1,0,1.0));		line1.add(new ElementMatrix(1,1,4.0));		/*line1.add(new ElementMatrix(1,2,0.0));*/
		line2.add(new ElementMatrix(2,0,2.0));		line2.add(new ElementMatrix(2,1,1.0));		/*line2.add(new ElementMatrix(2,2,0.0));*/
		line3.add(new ElementMatrix(3,0,3.0));		line3.add(new ElementMatrix(3,1,-4.0));		/*line3.add(new ElementMatrix(3,2,0.0));*/
		/*line4.add(new ElementMatrix(4,0,0.0));*/	/*line4.add(new ElementMatrix(4,1,0.0));*/	line4.add(new ElementMatrix(4,2,1.0));
		A.addLine(line0);
		A.addLine(line1);
		A.addLine(line2);
		A.addLine(line3);
		A.addLine(line4);

		//        double[][] A = {
		//            { -1,  1,  0 },
		//            {  1,  4,  0 },
		//            {  2,  1,  0 },
		//            {  3, -4,  0 },
		//            {  0,  0,  1 },
		//        };
		double[] c = { 1, 1, 1 };
		double[] b = { 5, 45, 27, 24, 4 };
		test(A, b, c);
		
	}

	@Test
	@DisplayName("basic test 2 with 2 variables and 3 constraints.")
	// x0 = 12, x1 = 28, opt = 800
	public static void test2() {
		double[] c = {  13.0,  23.0 };
		double[] b = { 480.0, 160.0, 1190.0 };

		SparseMatrix A = new SparseMatrix();
		ArrayList<ElementMatrix> line0 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line1 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line2 = new ArrayList<ElementMatrix>();
		line0.add(new ElementMatrix(0,0,5.0));		line0.add(new ElementMatrix(0,1,15.0));
		line1.add(new ElementMatrix(1,0,4.0));		line1.add(new ElementMatrix(1,1,4.0));
		line2.add(new ElementMatrix(2,0,35.0));		line2.add(new ElementMatrix(2,1,20.0));
		A.addLine(line0);
		A.addLine(line1);
		A.addLine(line2);

		//        double[][] A = {
				//            {  5.0, 15.0 },
				//            {  4.0,  4.0 },
		//            { 35.0, 20.0 },
		//        };
		test(A, b, c);
	}

	@Test
	@DisplayName("unbounded test with 4 variables and 2 constraints.")
	public static void test3() {
		double[] c = { 2.0, 3.0, -1.0, -12.0 };
		double[] b = {  3.0,   2.0 };

		SparseMatrix A = new SparseMatrix();
		ArrayList<ElementMatrix> line0 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line1 = new ArrayList<ElementMatrix>();
		line0.add(new ElementMatrix(0,0,-2.0));		line0.add(new ElementMatrix(0,1,-9.0));		line0.add(new ElementMatrix(0,2,1.0));		line0.add(new ElementMatrix(0,3,9.0));
		line1.add(new ElementMatrix(1,0,1.0));		line1.add(new ElementMatrix(1,1,1.0));		line1.add(new ElementMatrix(1,2,-1.0));		line1.add(new ElementMatrix(1,3,-2.0));
		A.addLine(line0);
		A.addLine(line1);
		//        double[][] A = {
				//            { -2.0, -9.0,  1.0,  9.0 },
		//            {  1.0,  1.0, -1.0, -2.0 },
		//        };
		test(A, b, c);
	}

	@Test
	@DisplayName("degenerate test with 4 variables and 3 constraints - cycles if you choose most positive objective function coefficient.")
	public static void test4() {
		double[] c = { 10.0, -57.0, -9.0, -24.0 };
		double[] b = {  0.0,   0.0,  1.0 };

		SparseMatrix A = new SparseMatrix();
		ArrayList<ElementMatrix> line0 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line1 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line2 = new ArrayList<ElementMatrix>();
		line0.add(new ElementMatrix(0,0,0.5));		line0.add(new ElementMatrix(0,1,-5.5));		line0.add(new ElementMatrix(0,2,-2.5));		line0.add(new ElementMatrix(0,3,9.0));
		line1.add(new ElementMatrix(1,0,0.5));		line1.add(new ElementMatrix(1,1,-1.5));		line1.add(new ElementMatrix(1,2,-0.5));		line1.add(new ElementMatrix(1,3,1.0));
		line2.add(new ElementMatrix(2,0,1.0));		/*line2.add(new ElementMatrix(2,1,0.0));		line2.add(new ElementMatrix(2,2,0.0));		line2.add(new ElementMatrix(2,3,0.0));*/
		A.addLine(line0);
		A.addLine(line1);
		A.addLine(line2);
		//        double[][] A = {
		//            { 0.5, -5.5, -2.5, 9.0 },
		//            { 0.5, -1.5, -0.5, 1.0 },
		//            { 1.0,  0.0,  0.0, 0.0 },
		//        };
		test(A, b, c);
	}
	
	@Test
	@DisplayName("test with 2 variables and 2 constraints and negative coefficients.")
	// test with negative coefficients
	public static void test5() {
		double[] c = { 1.0 , 4.0 };
		double[] b = { 1.0 , -1.0 };

		SparseMatrix A = new SparseMatrix();
		ArrayList<ElementMatrix> line0 = new ArrayList<ElementMatrix>();
		ArrayList<ElementMatrix> line1 = new ArrayList<ElementMatrix>();
		line0.add(new ElementMatrix(0,0,1.0));		line0.add(new ElementMatrix(0,1,1.0));
		line1.add(new ElementMatrix(1,0,-1.0));
		A.addLine(line0);
		A.addLine(line1);
//		double[] c = { 1.0 , 4.0 };
//		double[] b = { 1.0 , -1.0 };
//		double[][] A = {
//				{ 1.0 , 1.0 },
//				{ -1.0, 0.0 },
//		};
		test(A, b, c);
	}

	@Test
	@DisplayName("random test with N variables and M constraints and negative coefficients.")
	public static void randomTest(int M, int N, int seed) {

		Random rand1 = new Random(seed);
		Random rand2 = new Random(seed+1); 

		double[] c = new double[N];
		double[] b = new double[M];
		SparseMatrix A = new SparseMatrix();
		for (int j = 0; j < N; j++) {
			c[j] = 1001.0D * rand1.nextDouble();
		}
		for (int i = 0; i < M; i++) {
			b[i] = 1001.0D * rand1.nextDouble();
		}
		for (int i = 0; i < M; i++) {

			ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
			int nbElements = 1 /*+ (int) (6F *rand2.nextFloat())*/;

			for (int j = i ; j < i+nbElements && j<N ; j++) {
				line.add(new ElementMatrix(i, j, 101.0D * rand1.nextDouble()));
			}
			A.addLine(line);
		}
		test(A, b, c);        
	}
}
