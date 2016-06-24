package com.polifono.util;

import java.util.List;

public class Util {

	/**
	 *
	 * Explicação: Verifica se dentre uma determinada lista de objetos, algum dos valores está vazio "" ou 0 ou sem argumentos (no caso de uma lista).
	 *
	 * @param vargs
	 * @return
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
	 * @param n
	 * @return true se o número é nulo ou igual a zero.
	 */
	public static boolean isNumberEmpty(Number n) {
		return n == null || n.intValue() == 0;
	}

	/**
	 * Verifica se uma String é nula.
	 *
	 * @since 15/05/2012
	 * @param String
	 * @return boolean
	 */
	public static boolean isTextEmpty(String arg) {
		if (arg == null || arg.trim().length() == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 *
	 * Verifica se uma lista é nula/vazia.
	 *
	 * @param arg0
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isListEmpty(List arg0) {
		if (arg0 == null || arg0.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 *
	 * Verifica se um array é vazio.
	 *
	 * @author Felipe Lima - <a href="mailto:felipe.lima@wises.com.br">felipe.lima@wises.com.br</a>
	 * @date Dec 18, 2012
	 *
	 * @param array
	 * @return
	 */
	public static boolean isArrayEmpty(Object[] array) {
		if (array == null || array.length <= 0) {
			return true;
		}
		
		return false;
	}
}