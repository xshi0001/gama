/**
* Name: Operators
* Author: Patrick Taillandier
* Description: A model which illustrates the use of univariate statistical operators of GAMA 
* Tags: statistic
*/

model statistic_operators

global {
	init tet {
		list data <- [1,2,3,4,5,6,7,10,20,100];
		write "data: " + data;
		write "min: " + min(data);
		write "max: " + max(data);
		write "sum: " + sum(data);
		write "mean: " + mean(data);
		write "median: " + median(data);
		write "standard_deviation: " + standard_deviation(data);
		write "geometric_mean: " + geometric_mean(data);
		write "harmonic_mean: " + harmonic_mean(data);
		write "variance: " + harmonic_mean(data);
		write "mean_deviation: " + mean_deviation(data);
		write "kurtosis: " + kurtosis(data);
		write "skewness: " + skewness(data);
		write "gamma_rnd: " + gamma_rnd(0.1,1); 
		write "gini index: " + gini([10.0, 1.0, 2.0, 0.0]);
	}
}

experiment test_operators type: gui;