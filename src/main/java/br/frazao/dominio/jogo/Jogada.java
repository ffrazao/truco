package br.frazao.dominio.jogo;

import java.util.Optional;

import br.frazao.dominio.elementos.Carta;

public interface Jogada {
	
	public Jogador getJogador();
	
	public Optional<Carta> getCarta();

}
