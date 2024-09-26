package com.polifono.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Util {

	/**
	 * Explicação: Verifica se dentre uma determinada lista de objetos, algum dos valores está vazio "" ou 0 ou sem argumentos (no caso de uma lista).
	 */
	@SuppressWarnings ("rawtypes")
	public static boolean isEmpty(Object... vargs) {
		if (vargs == null || vargs.length <= 0) {
			return true;
		}

		for (Object arg: vargs) {
			if(arg==null) return true;
			if(arg instanceof Number && isNumberEmpty((Number)arg)) return true;
			if(arg instanceof String && isTextEmpty((String)arg)) return true;
			if(arg instanceof List && isListEmpty((List)arg)) return true;
			if(arg instanceof Object[] && isArrayEmpty((Object[])arg)) return true;
		}
		return false;
	}

	/**
	 * Verifica se o número é nulo ou igual a zero.
	 *
	 * @return true se o número é nulo ou igual a zero.
	 */
	public static boolean isNumberEmpty(Number n) {
		return n == null || n.intValue() == 0;
	}

	/**
	 * Verifica se uma String é nula.
	 */
	public static boolean isTextEmpty(String arg) {
        return arg == null || arg.trim().isEmpty();
    }

	/**
	 * Verifica se uma lista é nula/vazia.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isListEmpty(List arg0) {
        return arg0 == null || arg0.isEmpty();
    }

	/**
	 *
	 * Verifica se um array é vazio.
	 */
	public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

	/**
	 * Lê uma URL.
	 */
	public static String readURL(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}
}
