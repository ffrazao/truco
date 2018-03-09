package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.jogo.Jogada;

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

	public void apostar(JogadorTruco apostador, Truco truco) {
		JogadorTruco jogador = apostador;

		// adicionar aposta
		getApostaList().add(Aposta.criar(apostador));

		// execução paralela
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<JogadorTruco>> resposta = new ArrayList<>();

		// perguntar aos demais jogadores se vão aceitar ou não a aposta
		final AtomicReference<JogadorTruco> jogadorAR = new AtomicReference<>();
		do {
			if (!jogador.getTime().contains(apostador)) {
				jogadorAR.set(jogador);
				resposta.add(executor.submit(() -> jogadorAR.get().aceitarAposta(truco) ? jogadorAR.get() : null));
			}
		} while (apostador != (jogador = (JogadorTruco) truco.getMesa().getJogadorDepois(jogador).get()));

		resposta.forEach(jogadorAceitou -> {
			try {
				if (jogadorAceitou.get() != null) {
					if (getAposta().getJogador().equals(apostador)) {
						getAposta().getAceitouApostaList().add(jogadorAceitou.get());
					}
				}
			} catch (InterruptedException | ExecutionException e) {
			}
		});
	}

	private Aposta getAposta() {
		return getAposta(getApostaList().size() - 1);
	}

	private Aposta getAposta(int posicao) {
		return getApostaList().get(posicao);
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
