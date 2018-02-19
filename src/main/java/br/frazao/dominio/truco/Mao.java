package br.frazao.dominio.truco;

import java.util.Collections;
import java.util.List;

import br.frazao.dominio.jogo.Jogador;

public class Mao {
	
	private Jogador mao;

	public Jogador getMao() {
		return mao;
	}

	private List<JogadaTruco> jogadaList;

	public JogadaTruco getJogada() {
		return getJogada(this.jogadaList.size() - 1);
	}

	public JogadaTruco getJogada(int numeroJogada) {
		return this.jogadaList.get(this.jogadaList.size() - 1);
	}

	public List<JogadaTruco> getJogadaList() {
		return Collections.unmodifiableList(jogadaList);
	}

	public boolean isCangou() {
		return isCangou(this.jogadaList.size() - 1);
	}

	public boolean isCangou(int numeroJogada) {
		return this.jogadaList.get(this.jogadaList.size() - 1) != null;
	}

	public Mao jogar(Truco truco) {
		return null;
	}

}
