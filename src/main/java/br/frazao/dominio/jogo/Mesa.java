package br.frazao.dominio.jogo;

import java.util.List;
import java.util.Optional;

public interface Mesa {
	
	public void adicionarJogador(Jogador jogador);
	
	public Optional<Jogador> getJogador(Integer posicao);
	
	public Integer getJogador(Jogador jogador);
	
	public Optional<Jogador> getJogadorAntes(Jogador jogador);
	
	public Optional<Jogador> getJogadorDepois(Jogador jogador);

	public List<Jogador> getJogadorList();

	public void removerJogador(Jogador jogador);

}
