package br.frazao.dominio.truco;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.jogo.Jogada;
import br.frazao.dominio.jogo.Jogador;

public class JogadaTruco implements Jogada {

	private List<Aposta> apostaList;

	private Carta carta;

	private Jogador jogador;

	private boolean virada;

	public List<Aposta> getApostaList() {
		return Collections.unmodifiableList(apostaList);
	}

	@Override
	public Optional<Carta> getCarta() {
		return virada ? (Optional<Carta>) null : Optional.ofNullable(this.carta);
	}

	@Override
	public Jogador getJogador() {
		return this.jogador;
	}
	
	public void apostar(Truco truco) {
		getApostaList().add(Aposta.apostar(getJogador()));
	}

}
