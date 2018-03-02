package br.frazao.dominio.truco;

import java.util.List;
import java.util.Set;

import br.frazao.dominio.jogo.Resultado;

public class ResultadoTruco extends Resultado {

	private List<JogadaTruco> jogadaTrucoList;

	private ResultadoTrucoTipo resultado;

	private Integer tentos;

	private Set<JogadorTruco> vencedores;

	public ResultadoTruco(List<JogadaTruco> jogadaTrucoList, ResultadoTrucoTipo resultado, Set<JogadorTruco> vencedores, Integer tentos) {
		super();
		this.jogadaTrucoList = jogadaTrucoList;
		this.resultado = resultado;
		this.vencedores = vencedores;
		this.tentos = tentos;
	}

	public List<JogadaTruco> getJogadaTrucoList() {
		return jogadaTrucoList;
	}

	public ResultadoTrucoTipo getResultado() {
		return resultado;
	}

	public Integer getTentos() {
		return tentos;
	}

	public Set<JogadorTruco> getVencedores() {
		return vencedores;
	}

}
