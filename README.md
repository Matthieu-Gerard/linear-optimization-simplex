# linear-optimization-simplex
Here is a Simplex algorithm for solving linear minimization / maximization problems.

It is designed to solve a linear problems with a sparse matrix. Each row of the matrix is modeled using a list of elements e_i^j and we don't need to store 0 values.

Further improvements will come : 
- some bugs are detected with maximization objective.
- solutions with negative x_i are not allowed for the moment (only positive x_i are authorized for the moment).
- the capacity of adding new variable(s) (and related coefficients column(s) in the matrix) to execute a colunm generation (https://en.wikipedia.org/wiki/Column_generation).
- some tricks might improve the speed.

Here is another example of simplex implementation in Java : http://home.apache.org/~luc/commons-math-3.6-RC2-site/jacoco/org.apache.commons.math3.optimization.linear/index.source.html 
