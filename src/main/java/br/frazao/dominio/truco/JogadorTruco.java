package br.frazao.dominio.truco;

import java.util.List;
import java.util.Objects;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.jogo.Jogada;
import br.frazao.dominio.jogo.Jogador;
import br.frazao.dominio.jogo.Jogo;

public class JogadorTruco implements Jogador {

	private Baralho baralho;

	private String nome;

	public JogadorTruco(String nome) {
		this.nome = nome;
	}

	@Override
	public Baralho getBaralho() {
		if (this.baralho == null) {
			this.baralho = Baralho.criar();
		}
		return this.baralho;
	}

	@Override
	public String getNome() {
		return Objects.requireNonNull(this.nome);
	}

	public boolean isMaoDeOnze() {
		return false;
	}

	@Override
	public Jogada jogar(Jogo jogo) {
		Truco truco = (Truco) jogo;
		
		return null;
	}

	@Override
	public List<Carta> cortar(Baralho baralho) {
		int totalCartas = baralho.getCartas().size();
		int corte = (totalCartas * (-1)) + (int) (Math.random() * (totalCartas));
		return baralho.descarta(baralho.getCartas(corte)).get();
	}

}
