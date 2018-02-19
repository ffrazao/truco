package br.frazao.dominio.elementos;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Fundo {

	PRETO("Preto", 1), VERMELHO("Vermelho", 2);

	public static Fundo[] valoresOrdenados() {
		return Stream.of(values()).sorted((n1, n2) -> Integer.compare(n1.getOrdem(), n2.getOrdem())).collect(Collectors.toList()).toArray(new Fundo[Fundo.values().length]);
	}

	private String descricao;

	private int ordem;

	private Fundo(String descricao, int ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return getDescricao();
	}

}
