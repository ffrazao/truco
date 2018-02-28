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

	private JogadorTruco jogador;

	private boolean virada;

	public JogadaTruco(JogadorTruco jogador, Carta carta, boolean virada) {
		super();
		this.jogador = jogador;
		this.carta = carta;
		this.virada = virada;
	}

	public void apostar(Truco truco) {
		getApostaList().add(Aposta.apostar(getJogador()));
	}

	public List<Aposta> getApostaList() {
		return Collections.unmodifiableList(apostaList);
	}

	@Override
	public Optional<Carta> getCarta() {
		return virada ? (Optional<Carta>) null : Optional.ofNullable(this.carta);
	}

	@Override
	public JogadorTruco getJogador() {
		return this.jogador;
	}

	boolean isVirada() {
		return virada;
	}

	@Override
	public String toString() {
		return String.format("Jogador %s, jogou %s", getJogador().getNome(), isVirada() ? null : getCarta());
	}

}
