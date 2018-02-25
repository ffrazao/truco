package br.frazao.dominio.jogo;

import br.frazao.dominio.elementos.Baralho;

public interface Jogador extends Comparable<Jogador> {

	public Integer cortar(Integer totalCartas, Integer limite);

	public void embaralhar(Baralho baralho);

	public Baralho getBaralho();

	public String getNome();

	public <J extends Jogo> Jogada jogar(J jogo);

	public void ordenar(Baralho baralho);

}
