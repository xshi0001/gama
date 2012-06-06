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
package msi.gama.util.file;

import java.io.File;
import java.util.*;
import msi.gama.common.util.StringUtils;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.runtime.*;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gama.util.matrix.IMatrix;
import msi.gaml.types.*;

/**
 * Written by drogoul Modified on 7 ao�t 2010
 * 
 * @todo Description
 * 
 */

public abstract class GamaFile<K, V> implements IGamaFile<K, V> {

	private File file;

	protected final String path;

	protected boolean writable = false;

	protected IContainer<K, V> buffer;

	public GamaFile(final IScope scope, final String pathName) throws GamaRuntimeException {
		if ( pathName == null ) { throw new GamaRuntimeException("Attempt to create a null file"); }
		path = pathName;
		if ( scope != null ) {
			// file = new File(scope.getSimulationScope().getModel().getRelativeFilePath(pathName,
			// false));
			checkValidity();
			setWritable(false);
		}
	}

	public GamaFile(final String pathName) throws GamaRuntimeException {
		if ( pathName == null ) { throw new GamaRuntimeException("Attempt to create a null file"); }
		path = pathName;
		// file = new File(pathName);
		checkValidity();
	}

	protected void checkValidity() throws GamaRuntimeException {
		if ( getFile().isDirectory() ) { throw new GamaRuntimeException(
			getFile().getAbsolutePath() + " is a folder. Files can not overwrite folders"); }
	}

	@Override
	public void setWritable(final boolean w) {
		writable = w;
		getFile().setWritable(w);
	}

	protected abstract void fillBuffer() throws GamaRuntimeException;

	protected abstract void flushBuffer() throws GamaRuntimeException;

	@Override
	public final void setContents(final IContainer<K, V> cont) throws GamaRuntimeException {
		if ( writable ) {
			buffer = cont;
		}
	}

	protected abstract IGamaFile _copy();

	protected abstract boolean _isFixedLength();

	protected String _stringValue() throws GamaRuntimeException {
		getContents();
		return buffer.stringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#add(java.lang.Object, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void add(final K index, final V value, final Object param) throws GamaRuntimeException {
		getContents();
		buffer.add(index, value, param);
		flushBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#add(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void add(final V value, final Object param) throws GamaRuntimeException {
		getContents();
		buffer.add(value, param);
		flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#addAll(msi.gama.interfaces.IGamaContainer,
	 * java.lang.Object)
	 */
	@Override
	public void addAll(final IContainer value, final Object param) throws GamaRuntimeException {
		getContents();
		buffer.addAll(value, param);
		flushBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#addAll(java.lang.Object,
	 * msi.gama.interfaces.IGamaContainer, java.lang.Object)
	 */
	@Override
	public void addAll(final K index, final IContainer value, final Object param)
		throws GamaRuntimeException {
		getContents();
		buffer.addAll(index, value, param);
		flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#checkBounds(java.lang.Object, boolean)
	 */
	@Override
	public boolean checkBounds(final K index, final boolean forAdding) {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return false;
		}
		return buffer.checkBounds(index, forAdding);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#checkIndex(java.lang.Object)
	 */
	@Override
	public boolean checkIndex(final Object index) {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return false;
		}
		return buffer.checkIndex(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#checkValue(java.lang.Object)
	 */
	@Override
	public boolean checkValue(final Object value) {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return false;
		}
		return buffer.checkValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#clear()
	 */
	@Override
	public void clear() throws GamaRuntimeException {
		getContents();
		buffer.clear();
		flushBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o) throws GamaRuntimeException {
		getContents();
		return buffer.contains(o);

	}

	@Override
	public IGamaFile copy() {
		return _copy();
	}

	@Override
	// @getter( IKeyword.EXISTS)
	public Boolean exists() {
		return getFile().exists();
	}

	/*
	 * @see msi.gama.interfaces.IGamaContainer#first()
	 */
	@Override
	public V first() throws GamaRuntimeException {
		getContents();
		return buffer.first();
	}

	/*
	 * @see msi.gama.interfaces.IGamaContainer#get(java.lang.Object)
	 */
	@Override
	public V get(final K index) throws GamaRuntimeException {
		getContents();
		return buffer.get(index);
	}

	@Override
	// @getter( IKeyword.EXTENSION)
	public String getExtension() {
		String path = getFile().getPath();
		int mid = path.lastIndexOf(".");
		if ( mid == -1 ) { return ""; }
		return path.substring(mid + 1, path.length());
	}

	@Override
	// @getter( IKeyword.NAME)
	public String getName() {
		return getFile().getName();
	}

	@Override
	// @getter( IKeyword.PATH)
	public String getPath() {
		return getFile().getPath();
	}

	@Override
	// @getter( IKeyword.CONTENTS)
	public IContainer getContents() throws GamaRuntimeException {
		if ( getFile() == null ) { return null; }
		if ( !getFile().exists() ) { throw new GamaRuntimeException("File " +
			getFile().getAbsolutePath() + " does not exist"); }
		fillBuffer();
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return true;
		}
		return buffer.isEmpty();
	}

	@Override
	public boolean isFixedLength() {
		return _isFixedLength();
	}

	@Override
	// @getter( IKeyword.ISFOLDER)
	public Boolean isFolder() {
		return getFile().isDirectory();
	}

	@Override
	public boolean isPgmFile() {
		return getName().toLowerCase().contains(GamaFileType.pgmSuffix);
	}

	@Override
	// @getter( IKeyword.READABLE)
	public Boolean isReadable() {
		return getFile().canRead();
	}

	@Override
	// @getter( IKeyword.WRITABLE)
	public Boolean isWritable() {
		return getFile().canWrite();
	}

	@Override
	public Iterator iterator() {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return GamaList.EMPTY_LIST.iterator();
		}
		return buffer.iterator();
	}

	@Override
	public V last() throws GamaRuntimeException {
		getContents();
		return buffer.last();
	}

	@Override
	public int length() {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			return 0;
		}
		return buffer.length();
	}

	@Override
	public IList listValue(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.listValue(scope);
	}

	@Override
	public Map mapValue(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.mapValue(scope);
	}

	@Override
	public IMatrix matrixValue(final IScope scope) throws GamaRuntimeException {
		return matrixValue(scope, null);
	}

	@Override
	public IMatrix matrixValue(final IScope scope, final ILocation preferredSize)
		throws GamaRuntimeException {
		return _matrixValue(scope, preferredSize);
	}

	protected IMatrix _matrixValue(final IScope scope, final ILocation preferredSize)
		throws GamaRuntimeException {
		getContents();
		return buffer.matrixValue(scope, preferredSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#max()
	 */
	@Override
	public V max(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.max(scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#min()
	 */
	@Override
	public V min(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.min(scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#product()
	 */
	@Override
	public Object product(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.product(scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#put(java.lang.Object, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void put(final K index, final V value, final Object param) throws GamaRuntimeException {
		getContents();
		buffer.put(index, value, param);
		flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#putAll(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void putAll(final V value, final Object param) throws GamaRuntimeException {
		getContents();
		buffer.putAll(value, param);
		flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#removeAll(java.lang.Object)
	 */
	@Override
	public boolean removeAll(final IContainer<?, V> value) throws GamaRuntimeException {
		getContents();
		boolean result = buffer.removeAll(value);
		flushBuffer();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#removeAt(java.lang.Object)
	 */
	@Override
	public Object removeAt(final K index) throws GamaRuntimeException {
		getContents();
		Object result = buffer.removeAt(index);
		flushBuffer();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#removeFirst(java.lang.Object)
	 */
	@Override
	public boolean removeFirst(final V value) throws GamaRuntimeException {
		getContents();
		boolean result = buffer.removeFirst(value);
		flushBuffer();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#reverse()
	 */
	@Override
	public IContainer reverse() throws GamaRuntimeException {
		getContents();
		return buffer.reverse();
		// No side effect
	}

	@Override
	public String stringValue() throws GamaRuntimeException {
		return _stringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.IGamaContainer#sum()
	 */
	@Override
	public Object sum(final IScope scope) throws GamaRuntimeException {
		getContents();
		return buffer.sum(scope);
	}

	@Override
	public String toGaml() {
		return (writable ? "write(" : "read(") + getKeyword() + "(" +
			StringUtils.toGamlString(getPath()) + "))";
	}

	@Override
	public abstract String getKeyword();

	//
	// @Override
	// public String toJava() {
	// return "new File(" + getName() + ")";
	// }

	@Override
	public IType type() {
		return Types.get(IType.FILE);
	}

	@Override
	public V any() {
		try {
			getContents();
		} catch (GamaRuntimeException e) {
			GAMA.reportError(e);
			e.printStackTrace();
		}
		return buffer.any();
	}

	protected File getFile() {
		if ( file == null ) {
			if ( GAMA.getModel() != null ) {
				file = new File(GAMA.getModel().getRelativeFilePath(path, false));
			}
		}
		return file;
	}
}
