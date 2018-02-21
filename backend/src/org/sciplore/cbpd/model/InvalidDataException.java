package org.sciplore.cbpd.model;

import org.sciplore.preamble.License;

@License (author="Mario Lipinski")

public class InvalidDataException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidDataException() {}

	public InvalidDataException(String msg) 
	{
		super(msg);
	}
}
