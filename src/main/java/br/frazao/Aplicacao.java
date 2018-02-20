package br.frazao;

import java.util.stream.Stream;

import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogo;
import br.frazao.dominio.jogo.Mesa;
import br.frazao.dominio.truco.JogadorTruco;
import br.frazao.dominio.truco.MesaTruco;
import br.frazao.dominio.truco.Truco;

public class Aplicacao {

	public static void main(String[] args) {
		Jogo jogo = new Truco(true, false);
		
		Mesa mesa = new MesaTruco();
		mesa.adicionarJogador(new JogadorTruco("João"));
		mesa.adicionarJogador(new JogadorTruco("Maria"));
		mesa.adicionarJogador(new JogadorTruco("José"));
		mesa.adicionarJogador(new JogadorTruco("Ana"));
		
		mesa.getJogadorList().forEach(j -> System.out.format("Ocupo a cadeira número %d. Eu sou %s, antes de mim vem %s e depois de mim vem %s.\n", mesa.getJogador(j), j, mesa.getJogadorAntes(j).get(), mesa.getJogadorDepois(j).get()));
		
		Stream.of(Numero.valoresOrdenados()).forEach(n -> System.out.format("Ocupo a posição número %d. Eu sou o numero %s, antes de mim vem %s e depois de mim vem %s.\n", n.getOrdem(), n, n.getNumeroAntes(n), n.getNumeroDepois(n)));
		
		Stream.of(Naipe.valoresOrdenados()).forEach(n -> System.out.format("Ocupo a posição número %d. Eu sou o naipe %s, antes de mim vem %s e depois de mim vem %s.\n", n.getOrdem(), n, n.getNaipeAntes(n), n.getNaipeDepois(n)));
		
		//jogo.jogar(mesa);
	}

}
