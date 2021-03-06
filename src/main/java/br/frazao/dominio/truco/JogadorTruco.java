package br.frazao.dominio.truco;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.jogo.Jogada;
import br.frazao.dominio.jogo.Jogador;
import br.frazao.dominio.jogo.Jogo;

public class JogadorTruco implements Jogador {

	private Baralho baralho;

	private String nome;

	private Set<JogadorTruco> time;

	public JogadorTruco(String nome, JogadorTruco... parceiroList) {
		this.nome = Objects.requireNonNull(nome);
		Stream.of(parceiroList).forEach(this::adicionarParceiro);
	}

	public void adicionarParceiro(JogadorTruco parceiro) {
		this.getTime().add(Objects.requireNonNull(parceiro)); // ele é meu
																// parceiro
		parceiro.getTime().add(this); // eu sou parceiro dele
		this.getTime().stream().filter(p -> !p.equals(this)).forEach(p -> {
			// os meus parceiros também são parceiros dele
			p.getTime().add(parceiro);
			parceiro.getTime().add(p);
		});
		parceiro.getTime().stream().forEach(p -> {
			// os parceiros dele também são meus parceiros
			p.getTime().add(this);
			this.getTime().add(p);
		});
	}

	@Override
	public int compareTo(Jogador o) {
		return Normalizer.normalize(getNome(), Normalizer.Form.NFD).compareTo(Normalizer.normalize(o.getNome(), Normalizer.Form.NFD));
	}

	@Override
	public Integer cortar(Integer totalCartas, Integer minimoCartasDoCorte) {
		Integer result = new Random().nextInt(totalCartas + 1 - (-totalCartas + 1)) + (-totalCartas + 1);
		return result < -minimoCartasDoCorte || result > minimoCartasDoCorte ? result : minimoCartasDoCorte;
	}

	public void embaralhar(Baralho baralho) {
		Collections.shuffle(baralho.getCartas());
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

	public Set<JogadorTruco> getTime() {
		if (this.time == null) {
			this.time = new TreeSet<>();
			this.time.add(this);
		}
		return this.time;
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
		return jogar((Truco) jogo);
	}

	public JogadaTruco jogar(Truco truco) {
		JogadaTruco result = new JogadaTruco(this, getBaralho().descarta(getBaralho().getCartas(1)).get().get(0), false);
		result.apostar(this, truco);
		return result;
	}

	public void ordenar(Baralho baralho) {
		Collections.sort(baralho.getCartas());
	}

	public void removeParceiro(JogadorTruco parceiro) {
		// ele é meu parceiro
		this.getTime().remove(Objects.requireNonNull(parceiro));
		// eu sou parceiro dele
		parceiro.getTime().remove(this);

		// os meus parceiros também são parceiros dele
		this.getTime().stream().filter(p -> !p.equals(this)).forEach(p -> {
			p.getTime().remove(parceiro);
			parceiro.getTime().remove(p);
		});
		// os parceiros dele também são meus parceiros
		parceiro.getTime().stream().forEach(p -> {
			p.getTime().remove(this);
			this.getTime().remove(p);
		});
	}

	@Override
	public String toString() {
		return String.format("%s", nome);
	}

	public boolean aceitarAposta(Truco truco) {
		if (Math.random() * 1 > .5) {
			truco.getMao().get().getJogada().apostar(this, truco);
			return true;
		} else {
			return false;
		}
	}

}