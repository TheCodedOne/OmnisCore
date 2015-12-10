package me.jezza.oc.common.core.network.exceptions;

public abstract class NetworkException extends Exception {

	public NetworkException(String message, Object... data) {
		super(String.format(message, data));
	}

	public NetworkException(Throwable cause, String message, Object... data) {
		super(String.format(message, data), cause);
	}

}