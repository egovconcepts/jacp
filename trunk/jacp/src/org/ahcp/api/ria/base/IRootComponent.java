package org.ahcp.api.ria.base;

/**
 * all root components have containing sub components (workspace ->
 * perspectives; perspective - editors) and listeners; all sub components have
 * to be registerd
 * 
 * @author Andy Moncsek
 * 
 * @param <T>
 *            component to register
 * @param <H>
 *            handler where component have to be registered
 */
public interface IRootComponent<T, H> {

	/**
	 * register component at listener
	 * 
	 * @param component
	 * @param handler
	 */
	abstract public void registerComponent(T component, H handler);

}
