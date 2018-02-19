package br.frazao;

import br.frazao.dominio.jogo.Jogo;
import br.frazao.dominio.jogo.Mesa;
import br.frazao.dominio.truco.JogadorTruco;
import br.frazao.dominio.truco.MesaTruco;
import br.frazao.dominio.truco.Truco;

public class Aplicacao {

	public static void main(String[] args) {
		Jogo jogo = new Truco(true, false);
		
		Mesa mesa = new MesaTruco();
		mesa.adicionaJogador(new JogadorTruco("Joao"));
		mesa.adicionaJogador(new JogadorTruco("Maria"));
		mesa.adicionaJogador(new JogadorTruco("Jose"));
		mesa.adicionaJogador(new JogadorTruco("Ana"));
		
		jogo.jogar(mesa);
	}

}
