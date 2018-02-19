package br.frazao.dominio.jogo;

import java.util.List;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;

public interface Jogador {

	public List<Carta> cortar(Baralho baralho);

	public Baralho getBaralho();

	public String getNome();

	public <J extends Jogo> Jogada jogar(J jogo);

}
