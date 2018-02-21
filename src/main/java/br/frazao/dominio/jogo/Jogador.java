package br.frazao.dominio.jogo;

import br.frazao.dominio.elementos.Baralho;

public interface Jogador extends Comparable<Jogador> {

	public Integer cortar(Integer totalCartas, Integer limite);

	public Baralho getBaralho();

	public String getNome();

	public <J extends Jogo> Jogada jogar(J jogo);

}
