package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.frazao.dominio.jogo.Jogador;
import br.frazao.dominio.jogo.Mesa;

public class MesaTruco implements Mesa {

	private List<Jogador> jogadorList;

	@Override
	public void adicionarJogador(Jogador jogador) {
		getJogadorList().add(jogador);
	}

	@Override
	public Optional<Jogador> getJogador(Integer posicao) {
		return Optional.ofNullable(getJogadorList().get(posicao));
	}

	@Override
	public Integer getJogador(Jogador jogador) {
		return getJogadorList().indexOf(jogador);
	}

	@Override
	public Optional<Jogador> getJogadorAntes(Jogador jogador) {
		int posicao = getJogador(jogador);
		posicao = posicao - 1 < 0 ? getJogadorList().size() - 1 : posicao - 1;
		return getJogador(posicao);
	}

	@Override
	public Optional<Jogador> getJogadorDepois(Jogador jogador) {
		int posicao = getJogador(jogador);
		posicao = posicao + 1 >= getJogadorList().size() ? 0 : posicao + 1;
		return getJogador(posicao);
	}

	@Override
	public List<Jogador> getJogadorList() {
		if (this.jogadorList == null) {
			this.jogadorList = new ArrayList<>();
		}
		return this.jogadorList;
	}

	@Override
	public void removerJogador(Jogador jogador) {
		getJogadorList().remove(jogador);
	}

}