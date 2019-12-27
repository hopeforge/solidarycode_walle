package com.indracompany.walle.infrastructure.util;

import java.io.Serializable;
import java.util.Date;

public class ConverterUtil implements Serializable {
	private static final long serialVersionUID = -3544745499910467788L;

	public static int converteDataParaMeses(Date dtNascimento) {
		Date today = new Date();
		return (today.getMonth()) - dtNascimento.getMonth() + ((today.getYear() - dtNascimento.getYear()) * 12);
	}
}
