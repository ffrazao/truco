package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.frazao.dominio.jogo.Jogador;

public class Aposta {

	private List<Jogador> aceitouApostaList;

	private Aposta ampliarAposta;

	private Jogador jogador;

	public Aposta(Jogador jogador) {
		super();
		this.jogador = jogador;
	}

	public void addAceitou(Jogador jogador) {
		if (aceitouApostaList == null) {
			aceitouApostaList = new ArrayList<>();
		}
		aceitouApostaList.add(jogador);
	}

	public List<Jogador> getAceitouApostaList() {
		return Collections.unmodifiableList(aceitouApostaList);
	}

	public Aposta getAmpliarAposta() {
		return ampliarAposta;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setAmpliarAposta(Aposta ampliarAposta) {
		this.ampliarAposta = ampliarAposta;
	}

}
