package me.jezza.oc.client.lib;

import me.jezza.oc.common.interfaces.Request;

import java.util.concurrent.CancellationException;

/**
 * @author Jezza
 */
public abstract class AbstractRequest implements Request {
	protected boolean retrieved;
	protected boolean cancelled;
	protected boolean released;

	protected AbstractRequest() {
	}

	@Override
	public void cancel() {
		this.cancelled = true;
	}

	@Override
	public boolean acquired() {
		return validAcquisition() && !retrieved;
	}

	protected boolean confirmAcquisition() {
		if (!validAcquisition())
			throw new IllegalStateException("Request has not been acquired.");
		if (released)
			throw new IllegalStateException("Request has already been released.");
		if (cancelled)
			throw new CancellationException();
		if (retrieved)
			return false;
		retrieved = true;
		onAcquisition();
		return true;
	}

	public boolean cancelled() {
		return cancelled;
	}

	public boolean retrieved() {
		return retrieved;
	}

	public boolean released() {
		return released;
	}

	@Override
	public void release() {
		if (released)
			throw new IllegalStateException("Request as already been released.");
		released = true;
		onRelease();
	}

	protected abstract void onRelease();

	protected abstract void onAcquisition();

	protected abstract boolean validAcquisition();
}
