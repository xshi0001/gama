/*
 * GAMA - V1.4 http://gama-platform.googlecode.com
 * 
 * (c) 2007-2011 UMI 209 UMMISCO IRD/UPMC & Partners (see below)
 * 
 * Developers :
 * 
 * - Alexis Drogoul, UMI 209 UMMISCO, IRD/UPMC (Kernel, Metamodel, GAML), 2007-2012
 * - Vo Duc An, UMI 209 UMMISCO, IRD/UPMC (SWT, multi-level architecture), 2008-2012
 * - Patrick Taillandier, UMR 6228 IDEES, CNRS/Univ. Rouen (Batch, GeoTools & JTS), 2009-2012
 * - Beno�t Gaudou, UMR 5505 IRIT, CNRS/Univ. Toulouse 1 (Documentation, Tests), 2010-2012
 * - Phan Huy Cuong, DREAM team, Univ. Can Tho (XText-based GAML), 2012
 * - Pierrick Koch, UMI 209 UMMISCO, IRD/UPMC (XText-based GAML), 2010-2011
 * - Romain Lavaud, UMI 209 UMMISCO, IRD/UPMC (RCP environment), 2010
 * - Francois Sempe, UMI 209 UMMISCO, IRD/UPMC (EMF model, Batch), 2007-2009
 * - Edouard Amouroux, UMI 209 UMMISCO, IRD/UPMC (C++ initial porting), 2007-2008
 * - Chu Thanh Quang, UMI 209 UMMISCO, IRD/UPMC (OpenMap integration), 2007-2008
 */
package msi.gaml.operators;

import msi.gama.metamodel.shape.*;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gaml.expressions.IExpression;
import org.uncommons.maths.statistics.DataSet;

/**
 * Written by drogoul Modified on 15 janv. 2011
 * 
 * @todo Description
 * 
 */
public class Stats {

	private static DataSet from(final GamaList values) {
		DataSet d = new DataSet(values.size());
		for ( Object o : values ) {
			if ( o instanceof Number ) {
				d.addValue(((Number) o).doubleValue());
			}
		}
		return d;
	}

	@operator(value = "mean", can_be_const = true, type = ITypeProvider.CHILD_CONTENT_TYPE)
	@doc(
		value = "the mean of all the elements of the operand",
		comment = "the elements of the operand are summed (see sum for more details about the sum of container elements ) and then the sum value is divided by the number of elements.",
		specialCases = {"if the container contains points, the result will be a point"},
		examples = {"mean ([4.5, 3.5, 5.5, 7.0]) -> 5.125 "},
		see = {"sum"})	
	public static Object getMean(final IScope scope, final IContainer l)
		throws GamaRuntimeException {
		if ( l.length() == 0 ) { return Double.valueOf(0d); }
		Object s = l.sum(null);
		if ( s instanceof Number ) { return ((Number) s).doubleValue() / l.length(); }
		if ( s instanceof ILocation ) { return Points.divide((GamaPoint) s, l.length()); }
		return Cast.asFloat(scope, s) / l.length();
	}

	// Penser a faire ces calculs sur les points, egalement (et les entiers ?)

	@operator(value = "median")
	@doc(
		value = "the median of all the elements of the operand.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded.",
		specialCases = {""},
		examples = {"median ([4.5, 3.5, 5.5, 7.0]) -> 5.0"},
		see = {"mean"})		
	public static Double opMedian(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getMedian();
	}

	//
	// public static Object opRCompute(final IScope scope, final String operator, final Map
	// arguments) {
	// String s = generateRProgram(operator, arguments);
	// Object o = executeRFunction(s);
	// return o;
	// }

	@operator(value = "standard_deviation")
	@doc(
		value = "the standard deviation on the elements of the operand. See <A href=\"http://en.wikipedia.org/wiki/Standard_deviation\">Standard_deviation</A> for more details.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded.",
		specialCases = {""},
		examples = {"standard_deviation ([4.5, 3.5, 5.5, 7.0]) -> 1.2930100540985752"},
		see = {"mean", "mean_deviation"})		
	public static Double opStDev(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getStandardDeviation();
	}

	@operator(value = "geometric_mean")
	@doc(
		value = "the geometric mean of the elements of the operand. See <A href=\"http://en.wikipedia.org/wiki/Geometric_mean\">Geometric_mean</A> for more details.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded.",
		specialCases = {""},
		examples = {"geometric_mean ([4.5, 3.5, 5.5, 7.0]) -> 4.962326343467649"},
		see = {"mean", "median", "harmonic_mean"})		
	public static Double opGeomMean(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getGeometricMean();
	}

	@operator(value = "harmonic_mean")
	@doc(
		value = "the harmonic mean of the elements of the operand. See <A href=\"http://en.wikipedia.org/wiki/Harmonic_mean\">Harmonic_mean</A> for more details.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded.",
		specialCases = {""},
		examples = {"	harmonic_mean ([4.5, 3.5, 5.5, 7.0]) -> 4.804159445407279"},
		see = {"mean", "median", "geometric_mean"})		
	public static Double opHarmonicMean(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getHarmonicMean();
	}

	@operator(value = "variance")
	@doc(
		value = "the variance of the elements of the operand. See <A href=\"http://en.wikipedia.org/wiki/Variance\">Variance</A> for more details.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded. ",
		examples = {"variance ([4.5, 3.5, 5.5, 7.0]) -> 1.671875	"},
		see = {"mean", "median"})		
	public static Double opVariance(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getVariance();
	}

	@operator(value = "mean_deviation")
	@doc(
		value = "the deviation from the mean of all the elements of the operand. See <A href= \"http://en.wikipedia.org/wiki/Absolute_deviation\" >Mean_deviation</A> for more details.",
		comment = "The operator casts all the numerical element of the list into float. The elements that are not numerical are discarded.",
		examples = {"mean_deviation ([4.5, 3.5, 5.5, 7.0]) -> 1.125"},
		see = {"mean", "standard_deviation"})		
	public static Double opMeanDeviation(final IScope scope, final GamaList values) {
		DataSet d = from(values);
		return d.getMeanDeviation();
	}

	@operator(value = { "frequency_of" }, priority = IPriority.ITERATOR, iterator = true)	
	public static GamaMap frequencyOf(final IScope scope, final IContainer original,
		final IExpression filter) throws GamaRuntimeException {
		if ( original == null ) { return new GamaMap(); }
		final GamaMap result = new GamaMap();
		for ( Object each : original ) {
			scope.setEach(each);
			Object key = filter.value(scope);
			if ( !result.containsKey(key) ) {
				result.put(key, 1);
			} else {
				result.put(key, (Integer) result.get(key) + 1);
			}
		}
		return result;
	}

}
