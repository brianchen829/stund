/*
 * This file is part of JSTUN. 
 * 
 * Copyright (c) 2005 Thomas King <king@t-king.de> - All rights
 * reserved.
 * 
 * This software is licensed under either the GNU Public License (GPL),
 * or the Apache 2.0 license. Copies of both license agreements are
 * included in this distribution.
 */

package com.brianchen.util;

import org.apache.log4j.Logger;

public class UtilityException extends Exception {
	private static Logger logger = Logger.getLogger(UtilityException.class);
	private static final long serialVersionUID = 3545800974716581680L;

	public UtilityException(String mesg) {
		super(mesg);
		logger.error(mesg);
	}

}
