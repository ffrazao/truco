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
	public int compareTo(Jogador o) {
		return getNome().compareTo(o.getNome());
	}

	@Override
	public List<Carta> cortar(Baralho baralho) {
		int totalCartas = baralho.getCartas().size();
		int corte = (totalCartas * (-1)) + (int) (Math.random() * (totalCartas));
		return baralho.descarta(baralho.getCartas(corte)).get();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JogadorTruco other = (JogadorTruco) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
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
	public String toString() {
		return String.format("%s", nome);
	}

}
