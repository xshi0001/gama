/**
 * Created by drogoul, 13 f�vr. 2012
 * 
 */
package msi.gama.metamodel.population;

import java.util.List;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.*;
import msi.gaml.compilation.*;
import msi.gaml.descriptions.IDescription;

/**
 * The class EntitiesPlaceHolder.
 * 
 * @author drogoul
 * @since 13 f�vr. 2012
 * 
 */
@symbol(kind = ISymbolKind.ABSTRACT_SECTION, name = { IKeyword.ENTITIES }, with_sequence = true)
@inside(kinds = ISymbolKind.MODEL)
public class EntitiesPlaceHolder extends Symbol {

	public EntitiesPlaceHolder(final IDescription desc) {
		super(desc);
	}

	/**
	 * @see msi.gaml.compilation.Symbol#setChildren(java.util.List)
	 */
	@Override
	public void setChildren(final List<? extends ISymbol> children) {}

}
