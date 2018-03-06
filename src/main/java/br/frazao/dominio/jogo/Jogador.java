package br.frazao.dominio.jogo;

import br.frazao.dominio.elementos.Baralho;

public interface Jogador extends Comparable<Jogador> {

	public Integer cortar(Integer totalCartas, Integer limite);

	public void embaralhar(Baralho baralho);

	public Baralho getBaralho();

	public String getNome();

	public Jogada jogar(Jogo jogo);

	public void ordenar(Baralho baralho);

}
