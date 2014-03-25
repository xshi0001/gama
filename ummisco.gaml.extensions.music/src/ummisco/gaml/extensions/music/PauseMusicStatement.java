package ummisco.gaml.extensions.music;

import java.util.List;

import ummisco.gaml.extensions.music.PauseMusicStatement.PauseMusicValidator;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.compilation.ISymbol;
import msi.gaml.descriptions.IDescription;
import msi.gaml.statements.AbstractStatementSequence;
import msi.gaml.types.IType;

@symbol(name = IKeyword.PAUSE_MUSIC, kind = ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true)
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT })
@validator(PauseMusicValidator.class)
public class PauseMusicStatement extends AbstractStatementSequence {

	public static class PauseMusicValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription cd) {
			// what to validate?
		}
	}

	private AbstractStatementSequence sequence = null;

	public PauseMusicStatement(IDescription desc) {
		super(desc);
	}

	@Override
	public void setChildren(final List<? extends ISymbol> com) {
		sequence = new AbstractStatementSequence(description);
		sequence.setName("commands of " + getName());
		sequence.setChildren(com);
	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		
		IAgent currentAgent = scope.getAgentScope();
		
		GamaMusicPlayer musicPlayer = MusicPlayerBroker.getInstance().getMusicPlayer(currentAgent);
		musicPlayer.pause();

		if (sequence != null) {
			Object[] result = new Object[1];
			scope.execute(sequence, currentAgent, null, result);
		}
		
		
		return null;
	}
}
