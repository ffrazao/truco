package br.frazao.dominio.elementos;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Naipe {

	COPAS("Copas", 2, '\u2665', "RED"), ESPADAS("Espadas", 3, '\u2660', "BLACK"), OUROS("Ouros", 4, '\u2666', "RED"), PAUS("Paus", 1, '\u2663', "BLACK");

	public static Naipe[] valoresOrdenados() {
		return Stream.of(values()).sorted((n1, n2) -> Integer.compare(n1.getOrdem(), n2.getOrdem())).collect(Collectors.toList()).toArray(new Naipe[Naipe.values().length]);
	}

	private String cor;

	private String descricao;

	private int ordem;

	private Character simbolo;

	private Naipe(String descricao, int ordem, Character simbolo, String cor) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.simbolo = simbolo;
		this.cor = cor;
	}

	public String getCor() {
		return cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public Naipe getNaipeAntes(Naipe naipe) {
		int posicao = naipe.getOrdem() - 1 <= 0 ? Naipe.values().length : naipe.getOrdem() - 1;
		return Stream.of(Naipe.values()).filter(n -> n.getOrdem() == posicao).findAny().get();
	}

	public Naipe getNaipeDepois(Naipe naipe) {
		int posicao = naipe.getOrdem() + 1 > Naipe.values().length ? 1 : naipe.getOrdem() + 1;
		return Stream.of(Naipe.values()).filter(n -> n.getOrdem() == posicao).findAny().get();
	}

	public int getOrdem() {
		return ordem;
	}

	public Character getSimbolo() {
		return simbolo;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
}
