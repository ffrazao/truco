package br.frazao.dominio.jogo;

import java.util.List;

public interface Mesa {
	
	public void adicionaJogador(Jogador jogador);
	
	public Jogador getJogador(Integer posicao);
	
	public Jogador getJogador(Jogador jogador);
	
	public List<Jogador> getMesa();

}
